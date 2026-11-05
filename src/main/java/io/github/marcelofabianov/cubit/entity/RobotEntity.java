package io.github.marcelofabianov.cubit.entity;

import com.mojang.logging.LogUtils;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.DockableRobot;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.ownership.FollowingOutcome;
import io.github.marcelofabianov.cubit.domain.ownership.FollowingRobotPolicy;
import io.github.marcelofabianov.cubit.domain.ownership.OwnerProtection;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import io.github.marcelofabianov.cubit.player.DeadRobotLinks;
import io.github.marcelofabianov.cubit.player.FollowingRobotLink;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

/** A robot in the world. It adapts the game to the domain, where the rules live. */
public final class RobotEntity extends PathfinderMob implements GeoEntity, DockableRobot {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String OWNER_KEY = "Owner";
  private static final String TIER_KEY = "Tier";
  private static final String ENERGY_KEY = "Energy";
  private static final String POWER_STATE_KEY = "PowerState";
  private static final String INVENTORY_KEY = "Inventory";
  private static final String ENERGY_SLOT_KEY = "EnergySlot";
  private static final double INVENTORY_REACH_MARGIN = 4;
  private static final String NO_OWNER_MESSAGE = "Robot {} has no owner. Nobody can interact.";
  private static final double ONE_BLOCK_STEP = 1;
  private static final int BURNING_GOAL_PRIORITY = 0;
  private static final int DAMAGING_BLOCK_GOAL_PRIORITY = 1;
  private static final int DOCK_GOAL_PRIORITY = 2;
  private static final int FOLLOW_GOAL_PRIORITY = 3;
  private static final float BLOCKED_ROUTE = -1f;
  private static final float OPEN_ROUTE = 0f;

  private final RobotType robotType;
  private final Supplier<Item> item;
  private final Supplier<Item> damagedItem;
  private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

  private UUID ownerId;
  private RobotTier tier = RobotTier.BASE;
  private Battery battery;
  private PowerState powerState = PowerState.INACTIVE_SWITCHED_OFF;
  private BlockPos lastSafePosition;
  private final RobotStepMeter stepMeter = new RobotStepMeter();
  private final RobotInventory inventory;
  private final RobotDocking docking = new RobotDocking(this);

  public RobotEntity(
      EntityType<? extends RobotEntity> type,
      Level level,
      RobotType robotType,
      Supplier<Item> item,
      Supplier<Item> damagedItem) {
    super(type, level);
    this.robotType = robotType;
    this.item = item;
    this.damagedItem = damagedItem;
    this.battery = spec().newBattery(CubitMod.CONFIG.initialCharge());
    this.inventory = new RobotInventory(spec().inventorySlots());
    this.navigation = new RobotNavigation(this, level);
    this.jumpControl =
        new JumpControl(this) {
          // Tracks do not jump: the robot only climbs one block at a time, by its step height.
          @Override
          public void jump() {}
        };
    setCanPickUpLoot(false);
    setPathfindingMalus(PathType.WATER, BLOCKED_ROUTE);
  }

  @Override
  protected void registerGoals() {
    goalSelector.addGoal(BURNING_GOAL_PRIORITY, new BurningReactionGoal(this));
    goalSelector.addGoal(DAMAGING_BLOCK_GOAL_PRIORITY, new LeaveDamagingBlockGoal(this));
    goalSelector.addGoal(DOCK_GOAL_PRIORITY, new GoToDockGoal(this));
    goalSelector.addGoal(
        FOLLOW_GOAL_PRIORITY,
        new FollowOwnerGoal(
            this, CubitMod.CONFIG.followDecision(), CubitMod.CONFIG.followRepathTicks()));
  }

  /** Built without the tier numbers, because attributes are registered before the config loads. */
  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes().add(Attributes.STEP_HEIGHT, ONE_BLOCK_STEP);
  }

  public void placedBy(Player player, RobotState state, ItemStack item) {
    ownerId = player.getUUID();
    tier = state.tier();
    TierSpec spec = spec();
    battery = state.batteryFor(spec);
    RobotInteractions.applyAttributes(this);
    setHealth(state.healthFor(spec));
    setPersistenceRequired();
    lastSafePosition = blockPosition();
    inventory.readFrom(item, spec.inventorySlots());

    FollowingOutcome outcome = policy().onArrival(getUUID(), follower(player), battery);
    powerState = outcome.state();
    player.setData(CubitAttachments.FOLLOWING_ROBOT, new FollowingRobotLink(outcome.follower()));
  }

  /** The player this robot follows right now, when that player is online. */
  Player followedPlayer() {
    Player owner = onlineOwner();
    if (owner == null || !follower(owner).filter(getUUID()::equals).isPresent()) {
      return null;
    }
    return owner;
  }

  public RobotState state() {
    return new RobotState(tier, battery.charge(), getHealth());
  }

  public PowerState powerState() {
    return powerState;
  }

  RobotDocking docking() {
    return docking;
  }

  Battery battery() {
    return battery;
  }

  void setBattery(Battery battery) {
    this.battery = battery;
  }

  void setPowerState(PowerState state) {
    powerState = state;
  }

  Item itemType() {
    return item.get();
  }

  RobotInventory robotInventory() {
    return inventory;
  }

  /** For the item placed on a dock: the robot enters right away. */
  public void enterDock(InductionDockBlockEntity dock) {
    docking.enterNow(dock);
  }

  @Override
  public boolean answersDockCall() {
    return powerState == PowerState.ACTIVE && !docking.hasTrip();
  }

  @Override
  public int dockCallRadius() {
    return spec().callRadiusBlocks();
  }

  @Override
  public void callToDock(BlockPos dock) {
    docking.callTo(dock);
  }

  @Override
  public void releaseFromDock() {
    docking.release();
  }

  public boolean isDockedAt(BlockPos dock) {
    return docking.isDockedAt(dock);
  }

  public Optional<BlockPos> lastSafePosition() {
    return Optional.ofNullable(lastSafePosition);
  }

  @Override
  public Optional<UUID> ownerId() {
    return Optional.ofNullable(ownerId);
  }

  @Override
  protected InteractionResult mobInteract(Player player, InteractionHand hand) {
    if (hand != InteractionHand.MAIN_HAND) {
      return InteractionResult.PASS;
    }
    if (level().isClientSide()) {
      return InteractionResult.SUCCESS;
    }
    if (!player.getUUID().equals(ownerId)) {
      return InteractionResult.PASS;
    }
    if (player.isShiftKeyDown()) {
      return RobotInteractions.collect(this, player);
    }
    if (powerState.isInteractive()) {
      return RobotInteractions.openInventory(this, player);
    }
    if (inventory.energySlot().insert(player.getItemInHand(hand))) {
      return InteractionResult.CONSUME;
    }
    return RobotInteractions.turnOn(this, player);
  }

  /** Checked on the server while the screen is open, because the robot can die or switch off. */
  public boolean canUseInventory(Player player) {
    return isAlive()
        && powerState.isInteractive()
        && player.getUUID().equals(ownerId)
        && player.canInteractWithEntity(this, INVENTORY_REACH_MARGIN);
  }

  public Container inventory() {
    return inventory.container();
  }

  public Container energySlot() {
    return inventory.energySlot().container();
  }

  int batteryPercent() {
    return battery.level(Percentage.MAX);
  }

  /** Being pushed, or a stuck route, is not walking, so it costs nothing. */
  private void updateEnergy() {
    battery = spec().spendOneTick(battery, powerState, stepMeter.measure(this, inventory.isFull()));
    battery = inventory.energySlot().charge(battery, spec().rechargePerTick());
    PowerState next = CubitMod.CONFIG.powerRules().afterEnergyChange(powerState, battery);
    if (next != powerState) {
      powerState = next;
      getNavigation().stop();
    }
  }

  @Override
  protected void customServerAiStep() {
    // Only a robot in the water or on fire may route through water: to walk out or to put itself
    // out.
    setPathfindingMalus(PathType.WATER, isInWater() || isOnFire() ? OPEN_ROUTE : BLOCKED_ROUTE);
    super.customServerAiStep();
    rememberSafePosition();
    docking.tick();
    updateEnergy();
    RobotLoadSpeed.update(this, inventory.isFull());
    inventory.dropSurplus(
        stack -> Containers.dropItemStack(level(), getX(), getY(), getZ(), stack));
    if (tickCount % CubitMod.CONFIG.followRepathTicks() == 0) {
      RobotInteractions.switchOffWithoutThePlace(this);
    }
  }

  private void rememberSafePosition() {
    if (onGround() && !isOnFire() && !isInLava() && !isInWater()) {
      lastSafePosition = blockPosition();
    }
  }

  Player onlineOwner() {
    MinecraftServer server = getServer();
    return server == null || ownerId == null ? null : server.getPlayerList().getPlayer(ownerId);
  }

  @Override
  public boolean isInvulnerableTo(DamageSource source) {
    Optional<UUID> attacker = Optional.ofNullable(source.getEntity()).map(Entity::getUUID);
    return OwnerProtection.blocksDamage(ownerId(), attacker) || super.isInvulnerableTo(source);
  }

  @Override
  public void die(DamageSource source) {
    boolean alreadyDead = dead;
    super.die(source);
    if (dead && !alreadyDead && level() instanceof ServerLevel serverLevel) {
      DamagedRobotDrop.leave(serverLevel, this, damagedItem.get(), lastSafePosition, inventory);
      if (ownerId != null) {
        DeadRobotLinks.onRobotDied(serverLevel.getServer(), ownerId, getUUID());
      }
    }
  }

  @Override
  public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
    return false;
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    if (ownerId != null) {
      tag.putUUID(OWNER_KEY, ownerId);
    }
    tag.putString(TIER_KEY, tier.key());
    tag.putLong(ENERGY_KEY, battery.charge().amount());
    tag.putString(POWER_STATE_KEY, powerState.name());
    tag.put(INVENTORY_KEY, inventory.save(registryAccess()));
    tag.put(ENERGY_SLOT_KEY, inventory.energySlot().save(registryAccess()));
    docking.save(tag);
    SavedRobotValues.writeLastSafePosition(tag, lastSafePosition);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    ownerId = tag.hasUUID(OWNER_KEY) ? tag.getUUID(OWNER_KEY) : null;
    if (ownerId == null) {
      LOGGER.warn(NO_OWNER_MESSAGE, getUUID());
    }
    tier = SavedRobotValues.tier(tag.getString(TIER_KEY), getUUID());
    Energy energy = new Energy(Math.max(tag.getLong(ENERGY_KEY), 0));
    battery = new RobotState(tier, energy, getHealth()).batteryFor(spec());
    powerState = SavedRobotValues.powerState(tag.getString(POWER_STATE_KEY), getUUID());
    inventory.load(
        tag.getList(INVENTORY_KEY, Tag.TAG_COMPOUND), registryAccess(), spec().inventorySlots());
    inventory.energySlot().load(tag.getCompound(ENERGY_SLOT_KEY), registryAccess());
    docking.load(tag);
    lastSafePosition = SavedRobotValues.lastSafePosition(tag);
    RobotInteractions.applyAttributes(this);
  }

  /** Hibernating robots stay in place, or the plate would no longer hold them. */
  @Override
  public boolean isPushable() {
    return powerState != PowerState.HIBERNATING && super.isPushable();
  }

  @Override
  public boolean removeWhenFarAway(double distanceToClosestPlayer) {
    return false;
  }

  @Override
  public boolean canBeLeashed() {
    return false;
  }

  @Override
  public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

  @Override
  public AnimatableInstanceCache getAnimatableInstanceCache() {
    return animationCache;
  }

  TierSpec spec() {
    return CubitMod.CONFIG.tierCatalog().spec(robotType, tier);
  }

  static FollowingRobotPolicy policy() {
    return new FollowingRobotPolicy(CubitMod.CONFIG.powerRules());
  }

  static Optional<UUID> follower(Player player) {
    return player.getData(CubitAttachments.FOLLOWING_ROBOT).robot();
  }
}
