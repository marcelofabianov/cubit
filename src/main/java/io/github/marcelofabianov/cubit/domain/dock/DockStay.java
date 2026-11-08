package io.github.marcelofabianov.cubit.domain.dock;

import io.github.marcelofabianov.cubit.domain.energy.Battery;

/** How a robot stays in the base. The numbers come from the config, given by the caller. */
public final class DockStay {
  private DockStay() {}

  /** Every tier takes the same time to heal, because the amount follows its maximum health. */
  public static float healthPerTick(int maxHealth, int fullHealTicks) {
    return (float) maxHealth / fullHealTicks;
  }

  /** A full robot waits a moment, so the player sees the complete state, and then leaves. */
  public static boolean shouldLeave(Battery battery, int ticksFull, int holdTicks) {
    return battery.isFull() && ticksFull >= holdTicks;
  }
}
