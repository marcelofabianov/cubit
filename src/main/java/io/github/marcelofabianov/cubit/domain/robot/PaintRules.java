package io.github.marcelofabianov.cubit.domain.robot;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;

/** When a dye changes the robot, and when the robot shows that it noticed. */
public final class PaintRules {
  private PaintRules() {}

  /** The same color does nothing, so the dye is not wasted. */
  public static boolean paints(RobotColor current, RobotColor dye) {
    return current != dye;
  }

  /** Off, the screen is dark. Hibernating, it always shows the charging face. */
  public static boolean reacts(PowerState state) {
    return state == PowerState.ACTIVE;
  }
}
