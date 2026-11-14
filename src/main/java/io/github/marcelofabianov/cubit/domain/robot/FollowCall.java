package io.github.marcelofabianov.cubit.domain.robot;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;

/**
 * Whether `Follow` of the Tablet can make the chosen Cubit follow its owner now, or why not: it
 * comes switched off with charge, from the Dock or active without the place, from any distance in
 * the same dimension (answers 1073 to 1075).
 */
public enum FollowCall {
  READY,
  FOLLOWING,
  NO_CHARGE,
  OTHER_DIMENSION;

  public static FollowCall of(
      PowerState state, Battery battery, boolean following, boolean sameDimension) {
    if (following) {
      return FOLLOWING;
    }
    if (!sameDimension) {
      return OTHER_DIMENSION;
    }
    if (state == PowerState.INACTIVE_EXHAUSTED || battery.isEmpty()) {
      return NO_CHARGE;
    }
    return READY;
  }

  public boolean ready() {
    return this == READY;
  }
}
