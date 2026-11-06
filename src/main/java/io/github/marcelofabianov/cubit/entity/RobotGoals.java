package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import net.minecraft.world.entity.ai.goal.GoalSelector;

/** What a robot does, from the most urgent to the least. */
final class RobotGoals {
  private static final int BURNING_PRIORITY = 0;
  private static final int DAMAGING_BLOCK_PRIORITY = 1;
  private static final int DOCK_PRIORITY = 2;
  private static final int FOLLOW_PRIORITY = 3;
  private static final int LOOK_AT_OWNER_PRIORITY = 4;

  private RobotGoals() {}

  static void register(RobotEntity robot, GoalSelector goals) {
    goals.addGoal(BURNING_PRIORITY, new BurningReactionGoal(robot));
    goals.addGoal(DAMAGING_BLOCK_PRIORITY, new LeaveDamagingBlockGoal(robot));
    goals.addGoal(DOCK_PRIORITY, new GoToDockGoal(robot));
    goals.addGoal(
        FOLLOW_PRIORITY,
        new FollowOwnerGoal(
            robot, CubitMod.CONFIG.followDecision(), CubitMod.CONFIG.followRepathTicks()));
    goals.addGoal(LOOK_AT_OWNER_PRIORITY, new LookAtOwnerGoal(robot));
  }
}
