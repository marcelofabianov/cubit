package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * Walks ahead of the owner to the spot where the owner fell, in place of following (answer 1112). A
 * trip to a Dock wins, as the auto return does, and cancels the guide (answer 1121).
 */
final class DeathGuideGoal extends Goal {
  private final RobotEntity robot;

  DeathGuideGoal(RobotEntity robot) {
    this.robot = robot;
    setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    return robot.powerState() == PowerState.ACTIVE
        && !robot.docking().hasTrip()
        && robot.docking().deathGuide().guidingNow();
  }

  @Override
  public boolean canContinueToUse() {
    return canUse();
  }

  /** The time without progress counts ticks, so this goal runs on all of them. */
  @Override
  public boolean requiresUpdateEveryTick() {
    return true;
  }

  /**
   * A trip to a Dock or a Cubit that is no longer active ends the guide. A more urgent goal, such
   * as putting out a fire, only pauses it.
   */
  @Override
  public void stop() {
    robot.getNavigation().stop();
    if (robot.docking().hasTrip() || robot.powerState() != PowerState.ACTIVE) {
      robot.docking().deathGuide().cancel();
    }
  }

  @Override
  public void tick() {
    robot.docking().deathGuide().walk();
  }
}
