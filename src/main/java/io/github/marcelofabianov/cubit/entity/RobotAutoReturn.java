package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.RobotNotice;
import io.github.marcelofabianov.cubit.domain.dock.AutoReturn;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.menu.RobotSwitch;
import java.util.Optional;
import net.minecraft.core.BlockPos;

/**
 * The auto return: with a low battery, the Cubit that follows goes to the same Dock as `Go to
 * Dock`, says it once and, without a Dock in reach, tries again on the next interval (answers 1065
 * to 1068). It looks only once per interval of the dock search (rule 21).
 */
public final class RobotAutoReturn implements RobotSwitch {
  private final RobotEntity robot;
  private final RobotPreferredDock docks;
  private boolean switchedOn = true;
  private long nextCheck = Long.MIN_VALUE;

  RobotAutoReturn(RobotEntity robot, RobotPreferredDock docks) {
    this.robot = robot;
    this.docks = docks;
  }

  boolean switchedOn() {
    return switchedOn;
  }

  void setSwitchedOn(boolean switchedOn) {
    this.switchedOn = switchedOn;
  }

  @Override
  public boolean isOn() {
    return switchedOn;
  }

  @Override
  public boolean isAvailable() {
    return robot.powerState() != PowerState.HIBERNATING;
  }

  @Override
  public void toggle() {
    switchedOn = !switchedOn;
    nextCheck = Long.MIN_VALUE;
  }

  /** Called on the server while the robot is active. */
  void tick(boolean onTrip) {
    long now = robot.level().getGameTime();
    if (now < nextCheck) {
      return;
    }
    nextCheck = now + CubitMod.CONFIG.followRepathTicks();
    boolean following = RobotInteractions.followedPlayer(robot) != null;
    AutoReturn rule = new AutoReturn(switchedOn, robot.spec().returnCharge());
    if (!rule.returns(robot.powerState(), robot.battery(), following, onTrip)) {
      return;
    }
    Optional<BlockPos> dock = docks.dockToGo();
    if (dock.isPresent()) {
      robot.callToDock(dock.get());
      robot.notices().show(RobotNotice.NEED_RECHARGE);
    }
  }
}
