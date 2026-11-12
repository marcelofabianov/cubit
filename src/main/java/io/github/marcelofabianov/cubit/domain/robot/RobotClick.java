package io.github.marcelofabianov.cubit.domain.robot;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;

/** What a right click on a robot does. The order matters: shift always collects the robot. */
public enum RobotClick {
  IGNORE,
  COLLECT,
  UPGRADE,
  PAINT,
  CHOOSE_ORE,
  OPEN,
  CHARGE_OR_TURN_ON;

  public static RobotClick of(
      boolean owner, boolean module, boolean shift, boolean dye, boolean ore, PowerState state) {
    if (!owner) {
      return IGNORE;
    }
    if (shift) {
      return COLLECT;
    }
    if (module) {
      return UPGRADE;
    }
    if (dye) {
      return PAINT;
    }
    if (ore) {
      return CHOOSE_ORE;
    }
    return state.isInteractive() ? OPEN : CHARGE_OR_TURN_ON;
  }
}
