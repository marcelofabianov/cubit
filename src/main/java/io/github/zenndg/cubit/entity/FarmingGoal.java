package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.robot.WorkArea;
import java.util.EnumSet;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

/**
 * The Farmer in its area, in place of the round: it walks next to the block of its task and does it
 * with the hoe in the claw, or fetches seeds from a chest of its area (answers 1224, 1229 and
 * 1234). Unloading comes first, by its priority.
 */
final class FarmingGoal extends Goal {
  /** The robot speed is already in its movement attribute, so the route does not change it. */
  private static final double ROUTE_SPEED_MODIFIER = 1;

  /** Close enough to reach the block, as a player reaches what is next to them. */
  private static final double REACH_BLOCKS = 2.25;

  private static final double PROGRESS_BLOCKS = 0.5;
  private static final int SEED_STACK = 64;

  private final RobotEntity robot;
  private int repathCountdown;
  private double nearest;
  private int stuckTicks;

  FarmingGoal(RobotEntity robot) {
    this.robot = robot;
    setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    return farming().active();
  }

  @Override
  public boolean canContinueToUse() {
    return canUse();
  }

  @Override
  public boolean requiresUpdateEveryTick() {
    return true;
  }

  @Override
  public void start() {
    resetProgress();
  }

  @Override
  public void stop() {
    robot.getNavigation().stop();
    RobotJobLook.showTool(robot, false);
  }

  @Override
  public void tick() {
    Optional<WorkArea> area = robot.workArea().here();
    if (area.isEmpty() || farming().resting()) {
      return;
    }
    farming().plan(area.get());
    Optional<Destination> next = destination(area.get());
    if (next.isEmpty()) {
      RobotJobLook.showTool(robot, false);
      return;
    }
    if (reaches(next.get().pos())) {
      arrive(next.get());
    } else {
      RobotJobLook.showTool(robot, false);
      walkTo(next.get().pos());
    }
  }

  /** The task first, then the chest with seeds, then the chest to refill the fertilizer. */
  private Optional<Destination> destination(WorkArea area) {
    Optional<Destination> task =
        farming().task().map(found -> new Destination(found.pos(), Errand.TASK));
    if (task.isPresent()) {
      return task;
    }
    return farming()
        .seedChest(area)
        .map(pos -> new Destination(pos, Errand.SEEDS))
        .or(() ->
            farming().fertilizerChest(area).map(pos -> new Destination(pos, Errand.FERTILIZER)));
  }

  private void arrive(Destination next) {
    robot.getNavigation().stop();
    Vec3 center = Vec3.atCenterOf(next.pos());
    robot.getLookControl().setLookAt(center.x, center.y, center.z);
    resetProgress();
    switch (next.errand()) {
      case TASK -> {
        RobotJobLook.showTool(robot, true);
        farming().act();
      }
      case SEEDS -> FarmSeeds.fetch(robot, next.pos(), SEED_STACK);
      default -> FarmSeeds.fetchFertilizer(robot, next.pos());
    }
  }

  /** Where the Farmer goes next, and why. */
  private record Destination(BlockPos pos, Errand errand) {}

  private enum Errand {
    TASK,
    SEEDS,
    FERTILIZER
  }

  private boolean reaches(BlockPos goal) {
    return robot.position().distanceTo(Vec3.atCenterOf(goal)) <= REACH_BLOCKS;
  }

  private void walkTo(BlockPos goal) {
    Vec3 center = Vec3.atBottomCenterOf(goal.above());
    double distance = robot.position().distanceTo(center);
    if (stuck(distance)) {
      farming().giveUp();
      resetProgress();
      return;
    }
    if (--repathCountdown <= 0) {
      repathCountdown = CubitMod.CONFIG.followRepathTicks();
      robot.getNavigation().moveTo(center.x, center.y, center.z, ROUTE_SPEED_MODIFIER);
    }
  }

  private boolean stuck(double distance) {
    if (distance < nearest - PROGRESS_BLOCKS) {
      nearest = distance;
      stuckTicks = 0;
      return false;
    }
    return ++stuckTicks >= CubitMod.CONFIG.dockStuckTicks();
  }

  private void resetProgress() {
    nearest = Double.MAX_VALUE;
    stuckTicks = 0;
    repathCountdown = 0;
  }

  private RobotFarming farming() {
    return robot.workArea().work().farming();
  }
}
