package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.FishingPace;
import io.github.marcelofabianov.cubit.domain.behavior.FishingSpot;
import io.github.marcelofabianov.cubit.domain.behavior.FishingSpots;
import io.github.marcelofabianov.cubit.domain.behavior.FishingTimes;
import io.github.marcelofabianov.cubit.domain.behavior.HibernationReason;
import io.github.marcelofabianov.cubit.domain.behavior.SpotReservations;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.RobotJob;
import io.github.marcelofabianov.cubit.domain.robot.WorkArea;
import io.github.marcelofabianov.cubit.domain.tier.FisherSpec;
import io.github.marcelofabianov.cubit.menu.RobotStorage;
import io.github.marcelofabianov.cubit.menu.RobotSwitch;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/**
 * The work of the Fisher in its area: it finds a spot on the shore, holds it, and fishes from the
 * table of the game with the Lure and the Luck of the Sea of its tier (answers 1183 to 1191). Only
 * Keep junk is saved, because the spot is found again after loading (answer 1204).
 */
public final class RobotFishing implements RobotSwitch {
  /** The stands of every Fisher, only in the memory of the server (answer 1189). */
  private static final SpotReservations STANDS = new SpotReservations();

  private final RobotEntity robot;
  private boolean keepJunk = true;
  private FishingScan scan;
  private FishingSpot spot;
  private boolean noWater;
  private int ticksToBite;
  private final Set<Long> unreachable = new HashSet<>();

  RobotFishing(RobotEntity robot) {
    this.robot = robot;
  }

  public boolean keepJunk() {
    return keepJunk;
  }

  /** Keep junk, the switch of the Work tab, only with the card of the Fisher (answer 1191). */
  @Override
  public boolean isOn() {
    return keepJunk;
  }

  @Override
  public boolean isAvailable() {
    return robot.workArea().work().job().filter(RobotJob.FISHER::equals).isPresent();
  }

  @Override
  public void toggle() {
    keepJunk = !keepJunk;
  }

  public void setKeepJunk(boolean on) {
    keepJunk = on;
  }

  /** The card of the Fisher, active in its area: the work that replaces the round (answer 1166). */
  boolean active() {
    return robot.workArea().work().job().filter(RobotJob.FISHER::equals).isPresent()
        && robot.workArea().working()
        && robot.powerState() == PowerState.ACTIVE;
  }

  /** The reason to hibernate once a whole scan found no free spot (answer 1188). */
  Optional<HibernationReason> reason() {
    return noWater ? Optional.of(HibernationReason.NO_WATER) : Optional.empty();
  }

  Optional<FishingSpot> spot() {
    return Optional.ofNullable(spot);
  }

  /** Starts over: on arriving, on Resume and when the spot stops serving (answer 1204). */
  void reset() {
    STANDS.release(robot.getUUID());
    scan = null;
    spot = null;
    noWater = false;
    ticksToBite = 0;
    setFishing(false);
  }

  /** On Resume the stands it could not reach are tried again too. */
  void restart() {
    unreachable.clear();
    reset();
  }

  /** A stand it could not reach is left out until Resume, and it looks again. */
  void giveUp() {
    if (spot != null) {
      unreachable.add(spot.stand());
    }
    reset();
  }

  /** Called on the server on every tick, so a Fisher that stopped working lets go of its stand. */
  void tick() {
    if (!active() && (spot != null || scan != null)) {
      reset();
    }
  }

  /** One step of the scan, within the budget of blocks of this tick. */
  void search(WorkArea area) {
    if (scan == null) {
      scan = new FishingScan(robot, area);
    }
    scan.step(CubitMod.CONFIG.scanBlocksPerTick(), CubitMod.CONFIG.fisherCastReachBlocks());
    if (!scan.done()) {
      return;
    }
    String dimension = robot.level().dimension().location().toString();
    spot =
        FishingSpots.best(
                scan.found(),
                stand ->
                    !unreachable.contains(stand)
                        && STANDS.freeFor(dimension, stand, robot.getUUID()))
            .orElse(null);
    scan = null;
    noWater = spot == null || !STANDS.reserve(dimension, spot.stand(), robot.getUUID());
    if (noWater) {
      spot = null;
    }
    ticksToBite = drawTicksToBite();
  }

  /** One tick at the stand: the wait goes by, as in the game, and the bite brings the catch. */
  void fish() {
    setFishing(true);
    BlockPos water = BlockPos.of(spot.water());
    RandomSource random = robot.getRandom();
    boolean raining = robot.level().isRainingAt(water.above());
    boolean sky = robot.level().canSeeSky(water.above());
    ticksToBite -= FishingPace.ticks(raining, sky, random.nextFloat(), random.nextFloat());
    if (ticksToBite <= 0) {
      ticksToBite = drawTicksToBite();
      catchAt(water);
    }
  }

  void setFishing(boolean fishing) {
    robot.getEntityData().set(RobotSyncedData.FISHING, fishing);
  }

  /** Checks the spot again, as the water may have gone, and then takes what the table gives. */
  private void catchAt(BlockPos water) {
    FishingWater reader = new FishingWater(robot.level());
    if (!reader.fishable(water) || robot.robotInventory().isFull()) {
      if (!reader.fishable(water)) {
        reset();
      }
      return;
    }
    FisherSpec spec = robot.spec().jobs().fisher();
    robot.setBattery(spec.payForCatch(robot.battery()));
    Vec3 point = Vec3.atCenterOf(water);
    ServerLevel level = (ServerLevel) robot.level();
    for (ItemStack stack :
        FishingCatch.roll(level, point, reader.open(water), spec.luckLevel(), keepJunk)) {
      store(stack);
    }
    FishingEffects.splash(level, point);
  }

  private void store(ItemStack stack) {
    RobotStorage.insert(robot.inventory(), stack);
    if (!stack.isEmpty()) {
      Containers.dropItemStack(robot.level(), robot.getX(), robot.getY(), robot.getZ(), stack);
    }
  }

  private int drawTicksToBite() {
    FishingTimes times = CubitMod.CONFIG.fishingTimes();
    RandomSource random = robot.getRandom();
    int wait = random.nextIntBetweenInclusive(times.waitMinTicks(), times.waitMaxTicks());
    int bite = random.nextIntBetweenInclusive(times.biteMinTicks(), times.biteMaxTicks());
    return times.ticksToBite(wait, bite, robot.spec().jobs().fisher().lureLevel());
  }
}
