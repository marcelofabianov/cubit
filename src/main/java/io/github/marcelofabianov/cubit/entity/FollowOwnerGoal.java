package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.behavior.BurningAction;
import io.github.marcelofabianov.cubit.domain.behavior.FollowAction;
import io.github.marcelofabianov.cubit.domain.behavior.FollowDecision;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

/** Carries out what {@link FollowDecision} decides. The rule itself lives in the domain. */
final class FollowOwnerGoal extends Goal {
  /** The robot speed is already in its movement attribute, so the route does not change it. */
  private static final double ROUTE_SPEED_MODIFIER = 1;

  private static final int TELEPORT_ATTEMPTS = 10;
  private static final int TELEPORT_HEIGHT_RANGE = 1;

  private final RobotEntity robot;
  private final FollowDecision decision;
  private final int repathTicks;
  private int repathCountdown;

  FollowOwnerGoal(RobotEntity robot, FollowDecision decision, int repathTicks) {
    this.robot = robot;
    this.decision = decision;
    this.repathTicks = repathTicks;
    setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    return nextAction(RobotInteractions.followedPlayer(robot)) != FollowAction.STOP;
  }

  @Override
  public boolean canContinueToUse() {
    return canUse();
  }

  @Override
  public void start() {
    repathCountdown = 0;
  }

  @Override
  public void stop() {
    robot.getNavigation().stop();
  }

  @Override
  public void tick() {
    Player owner = RobotInteractions.followedPlayer(robot);
    if (owner == null) {
      return;
    }
    RobotGaze.look(robot, owner);
    repathCountdown--;
    if (repathCountdown > 0) {
      return;
    }
    repathCountdown = adjustedTickDelay(repathTicks);
    if (nextAction(owner) == FollowAction.TELEPORT) {
      teleportNear(owner);
    } else {
      robot.getNavigation().moveTo(owner, ROUTE_SPEED_MODIFIER);
    }
  }

  private FollowAction nextAction(Player owner) {
    boolean reactingToFire =
        BurningAction.decide(robot.powerState(), robot.isOnFire(), robot.isInLava(), false)
            != BurningAction.NONE;
    if (owner == null || reactingToFire) {
      return FollowAction.STOP;
    }
    boolean sameDimension = owner.level() == robot.level();
    return decision.decide(robot.powerState(), robot.distanceTo(owner), sameDimension);
  }

  /** Without a safe spot, the robot stays and tries again on the next route update. */
  private void teleportNear(Player owner) {
    BlockPos center = owner.blockPosition();
    int radius = decision.stopDistanceBlocks();
    for (int attempt = 0; attempt < TELEPORT_ATTEMPTS; attempt++) {
      BlockPos target =
          center.offset(
              randomOffset(radius), randomOffset(TELEPORT_HEIGHT_RANGE), randomOffset(radius));
      if (canStandAt(robot, target)) {
        robot.moveTo(
            target.getBottomCenter().x,
            target.getY(),
            target.getBottomCenter().z,
            robot.getYRot(),
            robot.getXRot());
        robot.getNavigation().stop();
        return;
      }
    }
  }

  private int randomOffset(int range) {
    return robot.getRandom().nextIntBetweenInclusive(-range, range);
  }

  /** Checks the chunk first: reading a block must never load one (AGENTS.md, rule 22). */
  static boolean canStandAt(RobotEntity robot, BlockPos target) {
    Level level = robot.level();
    return level.hasChunkAt(target)
        && WalkNodeEvaluator.getPathTypeStatic(robot, target) == PathType.WALKABLE
        && level.noCollision(
            robot, robot.getBoundingBox().move(target.subtract(robot.blockPosition())));
  }
}
