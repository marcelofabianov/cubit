package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;

/** How a robot reacts to fire. Only an active robot moves, so the others just keep burning. */
public enum BurningAction {
  NONE,
  LEAVE_LAVA,
  SEEK_WATER,
  WANDER;

  public static BurningAction decide(
      PowerState state, boolean onFire, boolean inLava, boolean waterInReach) {
    if (state != PowerState.ACTIVE || !(onFire || inLava)) {
      return NONE;
    }
    if (inLava) {
      return LEAVE_LAVA;
    }
    return waterInReach ? SEEK_WATER : WANDER;
  }
}
