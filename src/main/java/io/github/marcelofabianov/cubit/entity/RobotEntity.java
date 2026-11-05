package io.github.marcelofabianov.cubit.entity;

import com.mojang.logging.LogUtils;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.ownership.FollowingOutcome;
import io.github.marcelofabianov.cubit.domain.ownership.FollowingRobotPolicy;
import io.github.marcelofabianov.cubit.domain.ownership.OwnerProtection;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.player.DeadRobotLinks;
import io.github.marcelofabianov.cubit.player.FollowingRobotLink;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

/** A robot in the world. It adapts the game to the domain, where the rules live. */
public final class RobotEntity extends PathfinderMob implements GeoEntity {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String OWNER_KEY = "Owner";
  private static final String TIER_KEY = "Tier";
  private static final String ENERGY_KEY = "Energy";
  private static final String POWER_STATE_KEY = "PowerState";
  private static final String LAST_SAFE_POSITION_KEY = "LastSafePosition";
  private static final String INVENTORY_KEY = "Inventory";
  private static final double INVENTORY_REACH_MARGIN = 4;
  private static final String NO_OWNER_MESSAGE = "Robot {} has no owner. Nobody can interact.";
  private static final double ONE_BLOCK_STEP = 1;
  private static final int BURNING_GOAL_PRIORITY = 0;
  private static final int FOLLOW_GOAL_PRIORITY = 1;
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
  private final RobotInventory inventory;

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
    goalSelector.addGoal(
        FOLLOW_GOAL_PRIORITY,
        new FollowOwnerGoal(
            this, CubitMod.CONFIG.followDecision(), CubitMod.CONFIG.followRepathTicks()));
  }

  /** Built without the tier numbers, because attributes are registered before the config loads. */
  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes().add(Attributes.STEP_HEIGHT, ONE_BLOCK_STEP);
  }

  public void placedBy(Player player, RobotState state, ItemContainerContents contents) {
    ownerId = player.getUUID();
    tier = state.tier();
    TierSpec spec = spec();
    battery = state.batteryFor(spec);
    applyConfigAttributes();
    setHealth(state.healthFor(spec));
    setPersistenceRequired();
    lastSafePosition = blockPosition();
    inventory.fill(contents.stream().toList(), spec.inventorySlots());

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

  public Optional<BlockPos> lastSafePosition() {
    return Optional.ofNullable(lastSafePosition);
  }

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
      return collectBy(player);
    }
    return powerState == PowerState.ACTIVE ? openInventoryFor(player) : turnOnBy(player);
  }

  private InteractionResult openInventoryFor(Player player) {
    player.openMenu(
        new SimpleMenuProvider(
            (containerId, playerInventory, opener) ->
                new RobotMenu(containerId, playerInventory, inventory(), this::canUseInventory),
            getDisplayName()),
        data -> RobotMenu.writeSize(data, inventory()));
    return InteractionResult.CONSUME;
  }

  /** Checked on the server while the screen is open, because the robot can die or switch off. */
  public boolean canUseInventory(Player player) {
    return isAlive()
        && powerState == PowerState.ACTIVE
        && player.getUUID().equals(ownerId)
        && player.canInteractWithEntity(this, INVENTORY_REACH_MARGIN);
  }

  public Container inventory() {
    return inventory.container();
  }

  private InteractionResult collectBy(Player player) {
    ItemStack stack = new ItemStack(item.get());
    stack.set(CubitDataComponents.ROBOT_STATE.get(), state());
    stack.set(CubitDataComponents.ROBOT_INVENTORY.get(), inventory.contents());
    inventory.clear();
    player.setData(
        CubitAttachments.FOLLOWING_ROBOT,
        new FollowingRobotLink(policy().release(getUUID(), follower(player))));
    discard();
    // Drops the item at the player's feet when the inventory is full, so the robot is never lost.
    player.getInventory().placeItemBackInInventory(stack);
    return InteractionResult.CONSUME;
  }

  private InteractionResult turnOnBy(Player player) {
    FollowingOutcome outcome = policy().onTurnOn(getUUID(), follower(player), powerState, battery);
    if (outcome.state() == powerState) {
      return InteractionResult.PASS;
    }
    powerState = outcome.state();
    player.setData(CubitAttachments.FOLLOWING_ROBOT, new FollowingRobotLink(outcome.follower()));
    outcome.robotToSwitchOff().ifPresent(this::switchOff);
    return InteractionResult.CONSUME;
  }

  private void switchOff(UUID robotId) {
    MinecraftServer server = getServer();
    if (server == null) {
      return;
    }
    for (ServerLevel loadedLevel : server.getAllLevels()) {
      if (loadedLevel.getEntity(robotId) instanceof RobotEntity robot) {
        robot.powerState = PowerState.INACTIVE_SWITCHED_OFF;
        return;
      }
    }
  }

  @Override
  protected void customServerAiStep() {
    // Only a robot in the water or on fire may route through water: to walk out or to put itself
    // out.
    setPathfindingMalus(PathType.WATER, isInWater() || isOnFire() ? OPEN_ROUTE : BLOCKED_ROUTE);
    super.customServerAiStep();
    rememberSafePosition();
    inventory.dropSurplus(
        stack -> Containers.dropItemStack(level(), getX(), getY(), getZ(), stack));
    if (tickCount % CubitMod.CONFIG.followRepathTicks() == 0) {
      switchOffIfReplaced();
    }
  }

  private void rememberSafePosition() {
    if (onGround() && !isOnFire() && !isInLava() && !isInWater()) {
      lastSafePosition = blockPosition();
    }
  }

  /** A robot in an unloaded chunk misses the swap, so it finds out when it loads again. */
  private void switchOffIfReplaced() {
    Player owner = onlineOwner();
    if (powerState == PowerState.ACTIVE
        && owner != null
        && policy().otherRobotFollows(getUUID(), follower(owner))) {
      powerState = PowerState.INACTIVE_SWITCHED_OFF;
      getNavigation().stop();
    }
  }

  private Player onlineOwner() {
    MinecraftServer server = getServer();
    return server == null || ownerId == null ? null : server.getPlayerList().getPlayer(ownerId);
  }

  /** Applied again on load, so a config change reaches the robots already in the world. */
  private void applyConfigAttributes() {
    TierSpec spec = spec();
    getAttribute(Attributes.MAX_HEALTH).setBaseValue(spec.maxHealth());
    getAttribute(Attributes.MOVEMENT_SPEED)
        .setBaseValue(CubitMod.CONFIG.baseMovementSpeed() * spec.speedMultiplier());
    getAttribute(Attributes.FOLLOW_RANGE)
        .setBaseValue(CubitMod.CONFIG.followDecision().teleportDistanceBlocks());
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
      leaveDamagedItem(serverLevel);
      if (ownerId != null) {
        DeadRobotLinks.onRobotDied(serverLevel.getServer(), ownerId, getUUID());
      }
    }
  }

  /** Added directly, because the loot rules could remove a drop and lose the robot. */
  private void leaveDamagedItem(ServerLevel serverLevel) {
    ItemStack stack = new ItemStack(damagedItem.get());
    stack.set(CubitDataComponents.ROBOT_STATE.get(), state().damaged());
    stack.set(CubitDataComponents.ROBOT_OWNER.get(), ownerId);
    stack.set(CubitDataComponents.ROBOT_INVENTORY.get(), inventory.contents());
    inventory.clear();
    if (lastSafePosition != null) {
      stack.set(
          CubitDataComponents.LAST_SAFE_POSITION.get(),
          GlobalPos.of(serverLevel.dimension(), lastSafePosition));
    }
    ItemEntity drop = new ItemEntity(serverLevel, getX(), getY(), getZ(), stack);
    drop.setDefaultPickUpDelay();
    serverLevel.addFreshEntity(drop);
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
    if (lastSafePosition != null) {
      tag.putLong(LAST_SAFE_POSITION_KEY, lastSafePosition.asLong());
    }
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
    lastSafePosition =
        tag.contains(LAST_SAFE_POSITION_KEY)
            ? BlockPos.of(tag.getLong(LAST_SAFE_POSITION_KEY))
            : null;
    applyConfigAttributes();
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

  private TierSpec spec() {
    return CubitMod.CONFIG.tierCatalog().spec(robotType, tier);
  }

  private static FollowingRobotPolicy policy() {
    return new FollowingRobotPolicy(CubitMod.CONFIG.powerRules());
  }

  private static Optional<UUID> follower(Player player) {
    return player.getData(CubitAttachments.FOLLOWING_ROBOT).robot();
  }
}
