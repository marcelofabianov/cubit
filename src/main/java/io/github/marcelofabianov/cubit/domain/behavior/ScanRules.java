package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;

/** When the scanners work and what counts as hostile. The caller reads the world. */
public final class ScanRules {
  private ScanRules() {}

  /** Off, or on a dock, a robot neither scans nor spends on it. */
  public static boolean runs(boolean switchedOn, PowerState state, boolean followsItsOwner) {
    return switchedOn && state == PowerState.ACTIVE && followsItsOwner;
  }

  /** Nothing at tick zero, so placing a robot does not cost a scan right away. */
  public static boolean isDue(int tick, int intervalTicks) {
    return tick > 0 && tick % intervalTicks == 0;
  }

  /** A neutral mob only counts while it goes after the owner. */
  public static boolean isHostile(boolean enemy, boolean neutralTargetingOwner) {
    return enemy || neutralTargetingOwner;
  }
}
