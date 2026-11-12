package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.robot.RobotState;

/** Applies the settings a robot keeps, the same way when it is placed and when it loads. */
final class RobotSettings {
  private RobotSettings() {}

  /** The filter is fitted to the tier, because the config may have changed in the meantime. */
  static void apply(RobotEntity robot, RobotState state) {
    robot.lampSwitch().setSwitchedOn(state.lampOn());
    robot.scanners().setSwitchedOn(state.scannersOn());
    robot.chatter().setSwitchedOn(state.chatterOn());
    robot
        .scanners()
        .setOreFilter(state.oreFilter().fitting(robot.spec().scanners().oreFilterSlots()));
    RobotLooks.setColor(robot, state.color());
    robot.defense().setMode(state.defenseMode());
    robot.solar().setSwitchedOn(state.solarOn());
  }
}
