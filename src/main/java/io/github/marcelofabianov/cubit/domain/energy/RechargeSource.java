package io.github.marcelofabianov.cubit.domain.energy;

/** Where the battery got energy in the last tick, as the line under it on the robot screen. */
public enum RechargeSource {
  DOCK,
  DOCK_NO_POWER,
  DOCK_FULL,
  SLOT,
  SOLAR,
  NONE;

  /** In the base, the dock is the only source. Outside it, the slot comes before the panel. */
  public static RechargeSource of(PowerState state, RechargeView view, Battery battery) {
    if (state == PowerState.HIBERNATING) {
      if (battery.isFull()) {
        return DOCK_FULL;
      }
      return view.dockHasEnergy() ? DOCK : DOCK_NO_POWER;
    }
    if (view.slotCharged()) {
      return SLOT;
    }
    return view.solarCharged() ? SOLAR : NONE;
  }

  /** What charged the battery in the last tick, read by the caller. */
  public record RechargeView(boolean dockHasEnergy, boolean slotCharged, boolean solarCharged) {}
}
