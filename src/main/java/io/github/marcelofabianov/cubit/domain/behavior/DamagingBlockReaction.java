package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;

/** Whether a robot leaves a block that hurts it. Only an active robot moves. */
public final class DamagingBlockReaction {
  private DamagingBlockReaction() {}

  /** Fire and lava hurt more, and have their own reaction, so they come first. */
  public static boolean shouldLeave(
      PowerState state, boolean onDamagingBlock, boolean onFire, boolean inLava) {
    return state == PowerState.ACTIVE && onDamagingBlock && !onFire && !inLava;
  }
}
