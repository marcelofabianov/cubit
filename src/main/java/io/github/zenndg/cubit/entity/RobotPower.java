package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.energy.PowerRules;
import io.github.zenndg.cubit.domain.ownership.Ownership;
import io.github.zenndg.cubit.menu.PowerControl;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

/**
 * Switches the robot off from its screen. It frees the place to follow, and the screen closes by
 * itself, because it only stays open on an active or hibernating robot (answers 857 and 858).
 */
final class RobotPower implements PowerControl {
  private final RobotEntity robot;

  RobotPower(RobotEntity robot) {
    this.robot = robot;
  }

  @Override
  public boolean available() {
    return PowerRules.canSwitchOff(robot.powerState());
  }

  @Override
  public boolean switchOff(Player player) {
    if (!Ownership.owns(robot.ownerId(), player.getUUID()) || !available()) {
      return false;
    }
    robot.setPowerState(CubitMod.CONFIG.powerRules().switchOff(robot.powerState()));
    RobotFollowing.release(robot, player);
    return true;
  }

  @Override
  public boolean turnOn(Player player) {
    return Ownership.owns(robot.ownerId(), player.getUUID())
        && RobotInteractions.turnOn(robot, player) == InteractionResult.CONSUME;
  }
}
