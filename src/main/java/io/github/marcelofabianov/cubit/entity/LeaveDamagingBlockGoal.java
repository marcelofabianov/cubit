package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.DamagingBlockReaction;
import java.util.EnumSet;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

/** Carries out {@link DamagingBlockReaction}. The rule itself lives in the domain. */
final class LeaveDamagingBlockGoal extends Goal {
  /** The robot speed is already in its movement attribute, so this walk does not change it. */
  private static final double WALK_SPEED_MODIFIER = 1;

  private static final int EXIT_HEIGHT = 1;

  private final RobotEntity robot;
  private BlockPos exit;
  private int searchCountdown;

  LeaveDamagingBlockGoal(RobotEntity robot) {
    this.robot = robot;
    setFlags(EnumSet.of(Flag.MOVE));
  }

  @Override
  public boolean canUse() {
    if (!isHurt()) {
      searchCountdown = 0;
      return false;
    }
    if (searchCountdown-- > 0) {
      return false;
    }
    searchCountdown = reducedTickDelay(CubitMod.CONFIG.followRepathTicks());
    exit = findExit().orElse(null);
    return exit != null;
  }

  @Override
  public boolean canContinueToUse() {
    return isHurt();
  }

  /** The move control only holds one step, so a direct walk has to ask on every tick. */
  @Override
  public boolean requiresUpdateEveryTick() {
    return true;
  }

  @Override
  public void start() {
    robot.getNavigation().stop();
  }

  @Override
  public void tick() {
    Vec3 center = Vec3.atBottomCenterOf(exit);
    robot.getMoveControl().setWantedPosition(center.x, center.y, center.z, WALK_SPEED_MODIFIER);
  }

  @Override
  public void stop() {
    robot.getNavigation().stop();
    exit = null;
  }

  private boolean isHurt() {
    return DamagingBlockReaction.shouldLeave(
        robot.powerState(), DamagingBlocks.hurt(robot), robot.isOnFire(), robot.isInLava());
  }

  private Optional<BlockPos> findExit() {
    return BlockPos.findClosestMatch(
        robot.blockPosition(),
        CubitMod.CONFIG.burningWanderRadiusBlocks(),
        EXIT_HEIGHT,
        position -> DamagingBlocks.safeToStand(robot, position));
  }
}
