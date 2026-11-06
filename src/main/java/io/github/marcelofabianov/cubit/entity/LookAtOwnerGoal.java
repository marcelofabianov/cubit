package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.behavior.OwnerLook;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

/**
 * Turns a robot to its owner while nothing else is using its look. The game already sends the head
 * rotation of a mob to the client, so this needs no data of its own.
 */
final class LookAtOwnerGoal extends Goal {
  private static final float LOOK_YAW_SPEED = 10f;

  private final RobotEntity robot;

  LookAtOwnerGoal(RobotEntity robot) {
    this.robot = robot;
    setFlags(EnumSet.of(Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    Player owner = robot.onlineOwner();
    return owner != null
        && OwnerLook.applies(
            robot.powerState(), owner.level() == robot.level(), robot.distanceTo(owner));
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
  public void tick() {
    Player owner = robot.onlineOwner();
    if (owner != null) {
      robot.getLookControl().setLookAt(owner, LOOK_YAW_SPEED, robot.getMaxHeadXRot());
    }
  }
}
