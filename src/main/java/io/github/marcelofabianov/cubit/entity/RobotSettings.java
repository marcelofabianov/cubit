package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;

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
    robot
        .pickup()
        .setSettings(
            state.pickup().withFilter(PickupFilterCheck.existing(state.pickup().filter())));
  }

  /** The state the robot would keep as an item now, with the settings read from its parts. */
  static RobotState stateOf(RobotEntity robot, RobotTier tier, Energy charge) {
    return new RobotState(
        tier,
        charge,
        robot.getHealth(),
        robot.lampSwitch().switchedOn(),
        RobotLooks.color(robot),
        robot.scanners().switchedOn(),
        robot.scanners().oreFilter(),
        robot.chatter().switchedOn(),
        robot.defense().mode(),
        robot.solar().switchedOn(),
        robot.pickup().settings());
  }
}
