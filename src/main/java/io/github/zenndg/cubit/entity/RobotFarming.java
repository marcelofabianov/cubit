package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.behavior.SpotReservations;
import io.github.zenndg.cubit.domain.energy.PowerState;
import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.domain.robot.WorkArea;
import io.github.zenndg.cubit.domain.tier.FarmerSpec;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;

/**
 * The work of the Farmer in its area: it scans for ripe plants, ground to till and empty farmland,
 * holds the block it goes to, and does one action at a time, with the pause of its tier (answers
 * 1215 to 1236). Its fertilizer slot is part of its inventory (answers 1243 to 1246).
 */
public final class RobotFarming {
  /** The blocks every Farmer holds, only in the memory of the server (answer 1224). */
  private static final SpotReservations PLANTS = new SpotReservations();

  private final RobotEntity robot;
  private FarmingScan scan;
  private List<FarmTask> plan = List.of();
  private FarmTask task;
  private long nextScan = Long.MIN_VALUE;
  private int pause;

  RobotFarming(RobotEntity robot) {
    this.robot = robot;
  }

  /** The card of the Farmer, active in its area: the work that replaces the round (1166). */
  boolean active() {
    return farmer() && robot.workArea().working() && robot.powerState() == PowerState.ACTIVE;
  }

  /** Only the Farmer keeps the farmland, and the others follow the game (answer 1223). */
  public boolean tramples() {
    return !farmer();
  }

  Optional<FarmTask> task() {
    return Optional.ofNullable(task);
  }

  /** Called on the server on every tick, so a Farmer that stopped working lets go of its plant. */
  void tick() {
    if (pause > 0) {
      pause--;
    }
    if (!active() && task != null) {
      drop();
    }
  }

  boolean resting() {
    return pause > 0;
  }

  /** Scans when it is time, and takes the nearest free task of the last scan. */
  void plan(WorkArea area) {
    long now = robot.level().getGameTime();
    if (scan == null && now >= nextScan) {
      scan = new FarmingScan(robot.level(), area, spec().specialCrops());
    }
    if (scan != null) {
      scan.step(CubitMod.CONFIG.scanBlocksPerTick());
      if (scan.done()) {
        plan = new ArrayList<>(scan.found());
        scan = null;
        nextScan = now + CubitMod.CONFIG.farmerScanIntervalTicks();
      }
    }
    if (task == null) {
      task = nearestFree().orElse(null);
    }
  }

  /** Does the task, pays for it and rests, as the tier says (answers 1226 and 1235). */
  void act() {
    FarmTask done = task;
    drop();
    plan.remove(done);
    if (!FarmPlants.actionAt(robot.level(), done.pos(), spec().specialCrops())
        .filter(done.action()::equals)
        .isPresent()) {
      return;
    }
    if (done.action() == FarmAction.PLANT) {
      FarmWork.plant(robot, done.pos(), spec().boneMeal());
    } else {
      robot.setBattery(spec().payForAction(robot.battery()));
      perform(done);
    }
    pause = spec().actionPauseTicks();
  }

  /** A block it cannot reach is left out until the next scan. */
  void giveUp() {
    plan.remove(task);
    drop();
  }

  /** Lets go of the task, as when its card leaves or it stops working. */
  void drop() {
    PLANTS.release(robot.getUUID());
    task = null;
  }

  private void perform(FarmTask done) {
    switch (done.action()) {
      case HARVEST -> FarmWork.harvest(robot, done.pos(), spec());
      case TALL -> FarmWork.cutTall(robot, done.pos(), spec());
      case FRUIT -> FarmWork.pick(robot, done.pos(), spec());
      default -> FarmWork.till(robot, done.pos());
    }
  }

  /** Planting needs a seed, so empty farmland waits while it has none (answer 1234). */
  private Optional<FarmTask> nearestFree() {
    String dimension = robot.level().dimension().location().toString();
    boolean seeds = FarmSeeds.hasAny(robot.inventory());
    boolean room = !robot.robotInventory().isFull();
    return plan.stream()
        .filter(found -> seeds || found.action() != FarmAction.PLANT)
        .filter(found ->
            room || found.action() == FarmAction.PLANT || found.action() == FarmAction.TILL)
        .filter(found -> PLANTS.freeFor(dimension, found.pos().asLong(), robot.getUUID()))
        .min(Comparator.comparingDouble(found -> robot.blockPosition().distSqr(found.pos())))
        .filter(found -> PLANTS.reserve(dimension, found.pos().asLong(), robot.getUUID()));
  }

  /** Empty farmland and no seeds: the chest with seeds it should go to, if any (1234). */
  Optional<BlockPos> seedChest(WorkArea area) {
    boolean waiting = plan.stream().anyMatch(found -> found.action() == FarmAction.PLANT);
    return waiting && task == null && !FarmSeeds.hasAny(robot.inventory())
        ? FarmSeeds.chestWithSeeds(robot, area)
        : Optional.empty();
  }

  /**
   * An empty fertilizer slot, on a tier that takes it, and no task: the chest to refill it from.
   */
  Optional<BlockPos> fertilizerChest(WorkArea area) {
    RobotFertilizerSlot slot = robot.robotInventory().fertilizerSlot();
    return spec().boneMeal() && slot.isEmpty() && task == null
        ? FarmSeeds.chestWith(robot, area, RobotFertilizerSlot::accepts)
        : Optional.empty();
  }

  private boolean farmer() {
    return robot.workArea().work().job().filter(RobotJob.FARMER::equals).isPresent();
  }

  private FarmerSpec spec() {
    return robot.spec().jobs().farmer();
  }
}
