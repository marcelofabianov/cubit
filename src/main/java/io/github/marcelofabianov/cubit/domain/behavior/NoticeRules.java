package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;

/** When a robot speaks to its owner. */
public final class NoticeRules {
  private NoticeRules() {}

  /** A robot switched off or on a dock has its screen dark or charging, so it stays quiet. */
  public static boolean speaks(PowerState state) {
    return state == PowerState.ACTIVE;
  }
}
