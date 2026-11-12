package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.DockableRobot;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.ownership.FollowingOutcome;
import io.github.marcelofabianov.cubit.domain.ownership.OwnerProtection;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import io.github.marcelofabianov.cubit.player.DeadRobotLinks;
import io.github.marcelofabianov.cubit.player.FollowingRobotLink;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import io.github.marcelofabianov.cubit.registry.CubitSounds;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
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
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

/** A robot in the world. It adapts the game to the domain, where the rules live. */
public final class RobotEntity extends PathfinderMob implements GeoEntity, DockableRobot {
  private static final double ONE_BLOCK_STEP = 1;
  private static final float BLOCKED_ROUTE = -1f;
  private static final float OPEN_ROUTE = 0f;

  static {
    RobotSyncedData.load();
  }

  private final RobotType robotType;
  private final Supplier<Item> item;
  private final Supplier<Item> damagedItem;
  private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

  private UUID ownerId;
  private RobotTier tier = RobotTier.BASE;
  private Battery battery;
  private PowerState powerState = PowerState.INACTIVE_SWITCHED_OFF;
  private final RobotSafePosition safePosition = new RobotSafePosition(this);
  private final RobotEnergyUse energyUse = new RobotEnergyUse(this);
  private final RobotInventory inventory;
  private final RobotDocking docking = new RobotDocking(this);
  private final RobotFace face = new RobotFace(this);
  private final RobotNotices notices = new RobotNotices(this);
  private final RobotLamp lamp = new RobotLamp(this);
  private final RobotSolar solar = new RobotSolar(this);
  private final RobotScanners scanners = new RobotScanners(this);
  private final RobotChatter chatter = new RobotChatter(this);
  private final RobotDefense defense = new RobotDefense(this);

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
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    RobotSyncedData.define(builder);
  }

  @Override
  protected void registerGoals() {
    RobotGoals.register(this, goalSelector);
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
    safePosition.markHere();
    inventory.readFrom(item, spec.inventorySlots());
    RobotSettings.apply(this, state);
    setCustomName(item.get(DataComponents.CUSTOM_NAME));

    FollowingOutcome outcome =
        RobotFollowing.policy().onArrival(getUUID(), RobotFollowing.follower(player), battery);
    powerState = outcome.state();
    player.setData(CubitAttachments.FOLLOWING_ROBOT, new FollowingRobotLink(outcome.follower()));
    RobotAudio.onPlaced(this);
    face.onInteraction();
  }

  public RobotState state() {
    return new RobotState(
        tier,
        battery.charge(),
        getHealth(),
        lamp.switchedOn(),
        RobotLooks.color(this),
        scanners.switchedOn(),
        scanners.oreFilter(),
        chatter.switchedOn(),
        defense.mode());
  }

  public PowerState powerState() {
    return powerState;
  }

  public RobotDocking docking() {
    return docking;
  }

  RobotFace face() {
    return face;
  }

  public RobotLamp lampSwitch() {
    return lamp;
  }

  public RobotChatter chatter() {
    return chatter;
  }

  public RobotScanners scanners() {
    return scanners;
  }

  public RobotDefense defense() {
    return defense;
  }

  RobotNotices notices() {
    return notices;
  }

  Battery battery() {
    return battery;
  }

  void setBattery(Battery battery) {
    this.battery = battery;
  }

  void setTier(RobotTier tier) {
    this.tier = tier;
  }

  void setPowerState(PowerState state) {
    RobotAudio.onStateChange(this, powerState, state);
    powerState = state;
  }

  Item itemType() {
    return item.get();
  }

  RobotInventory robotInventory() {
    return inventory;
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

  public Optional<BlockPos> lastSafePosition() {
    return safePosition.position();
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
    return RobotInteractions.use(this, player, hand);
  }

  /** Checked on the server while the screen is open, because the robot can die or switch off. */
  public boolean canUseInventory(Player player) {
    return RobotInteractions.canUseInventory(this, player);
  }

  public Container inventory() {
    return inventory.container();
  }

  public Container energySlot() {
    return inventory.energySlot().container();
  }

  @Override
  protected void customServerAiStep() {
    // Only in water or on fire may it route through water, to walk out or to put itself out.
    setPathfindingMalus(PathType.WATER, isInWater() || isOnFire() ? OPEN_ROUTE : BLOCKED_ROUTE);
    super.customServerAiStep();
    safePosition.tick();
    Energy chargeBefore = battery.charge();
    docking.tick();
    energyUse.tick(chargeBefore, lamp.lit());
    solar.tick();
    face.tick();
    RobotLooks.update(this);
    lamp.tick();
    notices.tick();
    scanners.tick();
    chatter.tick();
    defense.tick();
    RobotLoadSpeed.update(this, inventory.isFull());
    inventory.dropSurplus(
        stack -> Containers.dropItemStack(level(), getX(), getY(), getZ(), stack));
    if (tickCount % CubitMod.CONFIG.followRepathTicks() == 0) {
      RobotInteractions.switchOffWithoutThePlace(this);
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
      BlockPos safe = safePosition.position().orElse(null);
      DamagedRobotDrop.leave(serverLevel, this, damagedItem.get(), safe, inventory);
      if (ownerId != null) {
        DeadRobotLinks.onRobotDied(serverLevel.getServer(), ownerId, getUUID());
      }
    }
  }

  /** On fire, the short circuit already loops, so the hits of the fire do not repeat it. */
  @Override
  protected SoundEvent getHurtSound(DamageSource source) {
    return isOnFire() ? null : CubitSounds.SHORT_CIRCUIT.get();
  }

  /** The route asks this. Tracks take no fall damage, so the game default of 3 is too careful. */
  @Override
  public int getMaxFallDistance() {
    return CubitMod.CONFIG.maxDropBlocks();
  }

  @Override
  public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
    return false;
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    SavedRobotValues.write(tag, this);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    ownerId = SavedRobotValues.owner(tag, getUUID());
    tier = SavedRobotValues.tier(tag, getUUID());
    battery = new RobotState(tier, SavedRobotValues.energy(tag), getHealth()).batteryFor(spec());
    powerState = SavedRobotValues.powerState(tag, getUUID());
    SavedRobotValues.readContents(tag, inventory, registryAccess(), spec().inventorySlots());
    docking.load(tag);
    RobotSettings.apply(this, SavedRobotValues.settings(tag, tier, getUUID()));
    safePosition.restore(SavedRobotValues.lastSafePosition(tag));
    RobotInteractions.applyAttributes(this);
  }

  /** Asked here, so a robot named before this rule also shows its name. */
  @Override
  public boolean isCustomNameVisible() {
    return hasCustomName();
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
  public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    RobotAnimations.register(this, controllers);
  }

  @Override
  public AnimatableInstanceCache getAnimatableInstanceCache() {
    return animationCache;
  }

  TierSpec spec() {
    return specAt(tier);
  }

  TierSpec specAt(RobotTier at) {
    return CubitMod.CONFIG.tierCatalog().spec(robotType, at);
  }
}
