package io.github.marcelofabianov.cubit.domain.dock;

import io.github.marcelofabianov.cubit.domain.energy.Battery;

/** How a robot stays in the base. The numbers come from the config, given by the caller. */
public final class DockStay {
  private DockStay() {}

  /** Every tier takes the same time to heal, because the amount follows its maximum health. */
  public static float healthPerTick(int maxHealth, int fullHealTicks) {
    return (float) maxHealth / fullHealTicks;
  }

  /** The robot leaves the base only with its battery and its health full (answer 963). */
  public static boolean complete(Battery battery, boolean healthFull) {
    return battery.isFull() && healthFull;
  }

  /**
   * A complete robot waits a moment, so the player sees the complete state, and then leaves. The
   * ticks count while it is complete.
   */
  public static boolean shouldLeave(
      Battery battery, boolean healthFull, int ticksComplete, int holdTicks) {
    return complete(battery, healthFull) && ticksComplete >= holdTicks;
  }
}
