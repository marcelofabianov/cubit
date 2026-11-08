package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;

/** What the top of the robot screen says about it. */
public enum RobotScreenStatus {
  CRITICAL_BATTERY,
  FOLLOWING,
  ACTIVE,
  HIBERNATING,
  /** The screen closes for an inactive robot, but it may still draw once before that. */
  OFF;

  /** A critical battery comes first only outside the base, where the robot spends energy. */
  public static RobotScreenStatus of(
      PowerState state, boolean following, Battery battery, Percentage critical) {
    if (state == PowerState.HIBERNATING) {
      return HIBERNATING;
    }
    if (state != PowerState.ACTIVE) {
      return OFF;
    }
    if (battery.hasAtMost(critical)) {
      return CRITICAL_BATTERY;
    }
    return following ? FOLLOWING : ACTIVE;
  }
}
