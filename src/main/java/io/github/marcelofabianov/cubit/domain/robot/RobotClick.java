package io.github.marcelofabianov.cubit.domain.robot;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;

/** What a right click on a robot does. The order matters: shift always collects the robot. */
public enum RobotClick {
  IGNORE,
  COLLECT,
  PAINT,
  OPEN,
  CHARGE_OR_TURN_ON;

  public static RobotClick of(boolean owner, boolean shift, boolean dye, PowerState state) {
    if (!owner) {
      return IGNORE;
    }
    if (shift) {
      return COLLECT;
    }
    if (dye) {
      return PAINT;
    }
    return state.isInteractive() ? OPEN : CHARGE_OR_TURN_ON;
  }
}
