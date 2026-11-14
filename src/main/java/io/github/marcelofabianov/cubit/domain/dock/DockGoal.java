package io.github.marcelofabianov.cubit.domain.dock;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;

/**
 * Whether `Go to Dock` of the Tablet can send the Cubit, or why not. Only an active Cubit goes, and
 * only with a Dock in reach (answers 1020 and 1041).
 */
public enum DockGoal {
  READY,
  SWITCHED_OFF,
  HIBERNATING,
  NO_DOCK;

  public static DockGoal of(PowerState state, boolean dockInReach) {
    return switch (state) {
      case ACTIVE -> dockInReach ? READY : NO_DOCK;
      case HIBERNATING -> HIBERNATING;
      case INACTIVE_EXHAUSTED, INACTIVE_SWITCHED_OFF -> SWITCHED_OFF;
    };
  }

  public boolean ready() {
    return this == READY;
  }
}
