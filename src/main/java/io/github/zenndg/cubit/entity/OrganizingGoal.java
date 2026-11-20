package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.robot.WorkArea;
import java.util.EnumSet;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

/**
 * The Organizer in its area, in place of the round: it walks next to the block or the item of its
 * task and does it with the clipboard raised (answers 1285 to 1304). Its own storing replaces the
 * common unload, and the Unload button of the Tablet still comes first, by its priority.
 */
final class OrganizingGoal extends Goal {
  /** The robot speed is already in its movement attribute, so the route does not change it. */
  private static final double ROUTE_SPEED_MODIFIER = 1;

  /** Close enough to reach the block, as a player reaches what is next to them. */
  private static final double REACH_BLOCKS = 2.25;

  private static final double PROGRESS_BLOCKS = 0.5;

  private final RobotEntity robot;
  private int repathCountdown;
  private double nearest;
  private int stuckTicks;

  OrganizingGoal(RobotEntity robot) {
    this.robot = robot;
    setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    return organizing().active();
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
    if (area.isEmpty() || organizing().resting()) {
      return;
    }
    organizing().plan(area.get());
    Optional<OrganizerTask> task = organizing().task();
    if (task.isEmpty()) {
      RobotJobLook.showTool(robot, false);
      return;
    }
    if (reaches(task.get().pos())) {
      arrive(task.get().pos());
    } else {
      RobotJobLook.showTool(robot, false);
      walkTo(task.get().pos());
    }
  }

  private void arrive(BlockPos pos) {
    robot.getNavigation().stop();
    Vec3 center = Vec3.atCenterOf(pos);
    robot.getLookControl().setLookAt(center.x, center.y, center.z);
    resetProgress();
    RobotJobLook.showTool(robot, true);
    organizing().act();
  }

  private boolean reaches(BlockPos goal) {
    return robot.position().distanceTo(Vec3.atCenterOf(goal)) <= REACH_BLOCKS;
  }

  private void walkTo(BlockPos goal) {
    Vec3 center = Vec3.atBottomCenterOf(goal);
    double distance = robot.position().distanceTo(center);
    if (stuck(distance)) {
      organizing().giveUp();
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

  private RobotOrganizing organizing() {
    return robot.workArea().work().organizing();
  }
}
