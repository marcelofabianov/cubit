package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.ownership.Ownership;
import io.github.marcelofabianov.cubit.domain.robot.FollowCall;
import io.github.marcelofabianov.cubit.menu.FollowControl;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

/**
 * `Follow` of the Tablet: the chosen Cubit takes the place to follow, and the one that followed
 * switches off where it is, as with `Turn on`. A Cubit in the Dock leaves it at once (answers 1073
 * to 1075).
 */
public final class RobotFollowCall implements FollowControl {
  private final RobotEntity robot;

  public RobotFollowCall(RobotEntity robot) {
    this.robot = robot;
  }

  @Override
  public FollowCall goal() {
    Player owner = robot.onlineOwner();
    return FollowCall.of(
        robot.powerState(),
        robot.battery(),
        RobotInteractions.followedPlayer(robot) != null,
        owner != null && owner.level() == robot.level());
  }

  /** Checked again now, only for the owner. */
  @Override
  public boolean call(Player player) {
    if (!Ownership.owns(robot.ownerId(), player.getUUID()) || !goal().ready()) {
      return false;
    }
    if (robot.powerState() == PowerState.INACTIVE_SWITCHED_OFF) {
      return RobotInteractions.turnOn(robot, player) == InteractionResult.CONSUME;
    }
    RobotInteractions.takeThePlace(robot, player);
    if (robot.powerState() == PowerState.HIBERNATING) {
      robot.releaseFromDock();
    }
    return true;
  }
}
