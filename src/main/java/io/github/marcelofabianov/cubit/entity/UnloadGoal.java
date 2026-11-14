package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import java.util.EnumSet;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * Walks to the marked chests in their order, puts in them what fits and lets the Cubit go back to
 * what it did: following or its area (answers 1087 to 1089). Stuck, it gives up the trip.
 */
final class UnloadGoal extends Goal {
  /** The robot speed is already in its movement attribute, so the route does not change it. */
  private static final double ROUTE_SPEED_MODIFIER = 1;

  /** Close enough to reach into the chest, as a player would. */
  private static final double REACH_BLOCKS = 2.5;

  private static final double PROGRESS_BLOCKS = 0.5;

  private final RobotEntity robot;
  private BlockPos target;
  private boolean moved;
  private int repathCountdown;
  private double nearest;
  private int stuckTicks;

  UnloadGoal(RobotEntity robot) {
    this.robot = robot;
    setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
  }

  /** Asked every other tick, but the automatic check only reads the chests by an interval. */
  @Override
  public boolean canUse() {
    return robot.powerState() == PowerState.ACTIVE
        && !robot.docking().hasTrip()
        && robot.docking().unload().wantsToGo();
  }

  @Override
  public boolean canContinueToUse() {
    return robot.powerState() == PowerState.ACTIVE && robot.docking().unload().onTrip();
  }

  /** The stuck time counts ticks, so this goal runs on all of them. */
  @Override
  public boolean requiresUpdateEveryTick() {
    return true;
  }

  @Override
  public void start() {
    moved = false;
    nextTarget();
  }

  @Override
  public void stop() {
    robot.getNavigation().stop();
    if (robot.docking().unload().onTrip()) {
      robot.docking().unload().finish(moved);
    }
    target = null;
  }

  @Override
  public void tick() {
    if (target == null) {
      return;
    }
    double distance = Math.sqrt(target.distToCenterSqr(robot.position()));
    if (distance <= REACH_BLOCKS) {
      robot.getLookControl().setLookAt(target.getCenter());
      moved |= robot.docking().unload().transferTo(target);
      nextTarget();
      return;
    }
    if (stuck(distance)) {
      robot.docking().unload().finish(moved);
      return;
    }
    if (--repathCountdown <= 0) {
      repathCountdown = CubitMod.CONFIG.followRepathTicks();
      robot
          .getNavigation()
          .moveTo(target.getX(), target.getY(), target.getZ(), ROUTE_SPEED_MODIFIER);
    }
  }

  /** The next chest with room, or the end of the trip when none is left or nothing is left. */
  private void nextTarget() {
    Optional<BlockPos> next =
        robot.docking().unload().isEmpty()
            ? Optional.empty()
            : robot.docking().unload().nextChest();
    if (next.isEmpty()) {
      robot.docking().unload().finish(moved);
      target = null;
      return;
    }
    target = next.get();
    repathCountdown = 0;
    nearest = Double.MAX_VALUE;
    stuckTicks = 0;
  }

  private boolean stuck(double distance) {
    if (distance < nearest - PROGRESS_BLOCKS) {
      nearest = distance;
      stuckTicks = 0;
      return false;
    }
    return ++stuckTicks >= CubitMod.CONFIG.dockStuckTicks();
  }
}
