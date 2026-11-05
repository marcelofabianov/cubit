package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;

/** How a robot reacts to fire. Only an active robot moves, so the others just keep burning. */
public enum BurningAction {
  NONE,
  SEEK_WATER,
  WANDER;

  public static BurningAction decide(PowerState state, boolean onFire, boolean waterInReach) {
    if (!onFire || state != PowerState.ACTIVE) {
      return NONE;
    }
    return waterInReach ? SEEK_WATER : WANDER;
  }
}
