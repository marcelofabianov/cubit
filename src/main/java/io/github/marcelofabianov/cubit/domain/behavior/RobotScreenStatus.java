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
  OFF,
  /** At the end, because snapshots keep the name and the menu the place (answer 1063). */
  IN_AREA,
  INVENTORY_FULL;

  /** What a Cubit in an area does now: working, or stopped with a full inventory (answer 1052). */
  public record AreaWork(boolean inArea, boolean inventoryFull) {
    public static final AreaWork NONE = new AreaWork(false, false);
  }

  /** A critical battery comes first only outside the base, where the robot spends energy. */
  public static RobotScreenStatus of(
      PowerState state, boolean following, Battery battery, Percentage critical) {
    return of(state, following, AreaWork.NONE, battery, critical);
  }

  /** In an area, the critical battery and then the full inventory come first (answer 1063). */
  public static RobotScreenStatus of(
      PowerState state, boolean following, AreaWork area, Battery battery, Percentage critical) {
    if (state == PowerState.HIBERNATING) {
      return HIBERNATING;
    }
    if (state != PowerState.ACTIVE) {
      return OFF;
    }
    if (battery.hasAtMost(critical)) {
      return CRITICAL_BATTERY;
    }
    if (area.inArea()) {
      return area.inventoryFull() ? INVENTORY_FULL : IN_AREA;
    }
    return following ? FOLLOWING : ACTIVE;
  }
}
