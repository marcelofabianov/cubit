package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;

/** Walks to the dock that called the robot. The trip itself lives in {@link RobotDocking}. */
final class GoToDockGoal extends Goal {
  private final RobotEntity robot;

  /** Reads the docking from the robot, because goals are registered before its fields exist. */
  GoToDockGoal(RobotEntity robot) {
    this.robot = robot;
    setFlags(EnumSet.of(Flag.MOVE));
  }

  @Override
  public boolean canUse() {
    return robot.powerState() == PowerState.ACTIVE && robot.docking().hasTrip();
  }

  /** The stuck time and the queue count ticks, so this goal runs on all of them. */
  @Override
  public boolean requiresUpdateEveryTick() {
    return true;
  }

  @Override
  public void tick() {
    robot.docking().walk();
  }

  @Override
  public void stop() {
    robot.getNavigation().stop();
  }
}
