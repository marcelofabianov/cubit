package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.robot.WorkArea;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

/**
 * A job done by errands in its area, in place of the round: the Cubit walks next to the block of
 * the errand and does it with the tool of the job in the claw, as the Organizer and the Cook do
 * (answers 1285 to 1304 and 1316). Unloading comes first, by its priority.
 */
final class ErrandGoal extends Goal {
  /** The robot speed is already in its movement attribute, so the route does not change it. */
  private static final double ROUTE_SPEED_MODIFIER = 1;

  /** Close enough to reach the block, as a player reaches what is next to them. */
  private static final double REACH_BLOCKS = 2.25;

  private static final double PROGRESS_BLOCKS = 0.5;

  private final RobotEntity robot;
  private final Supplier<JobErrands> errands;
  private int repathCountdown;
  private double nearest;
  private int stuckTicks;

  ErrandGoal(RobotEntity robot, Supplier<JobErrands> errands) {
    this.robot = robot;
    this.errands = errands;
    setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    return errands.get().active();
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
    if (area.isEmpty() || errands.get().resting()) {
      return;
    }
    errands.get().plan(area.get());
    Optional<BlockPos> target = errands.get().target();
    if (target.isEmpty()) {
      RobotJobLook.showTool(robot, false);
      return;
    }
    if (reaches(target.get())) {
      arrive(target.get());
    } else {
      RobotJobLook.showTool(robot, false);
      walkTo(target.get());
    }
  }

  private void arrive(BlockPos pos) {
    robot.getNavigation().stop();
    Vec3 center = Vec3.atCenterOf(pos);
    robot.getLookControl().setLookAt(center.x, center.y, center.z);
    resetProgress();
    RobotJobLook.showTool(robot, true);
    errands.get().act();
  }

  private boolean reaches(BlockPos goal) {
    return robot.position().distanceTo(Vec3.atCenterOf(goal)) <= REACH_BLOCKS;
  }

  private void walkTo(BlockPos goal) {
    Vec3 center = Vec3.atBottomCenterOf(goal);
    double distance = robot.position().distanceTo(center);
    if (stuck(distance)) {
      errands.get().giveUp();
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
    return ++stuckTicks >= CubitMod.CONFIG.dock().stuckTicks();
  }

  private void resetProgress() {
    nearest = Double.MAX_VALUE;
    stuckTicks = 0;
    repathCountdown = 0;
  }
}
