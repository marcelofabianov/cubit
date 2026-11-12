package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.tier.PickupSpec;

/** When the robot picks up and when its magnet pulls (answers 876, 877, 879 and 884). */
public final class PickupRules {
  private PickupRules() {}

  /** Touch pickup is free and works on every tier, but only while active. */
  public static boolean collects(PowerState state, boolean pickupEnabled) {
    return state == PowerState.ACTIVE && pickupEnabled;
  }

  /**
   * Pulling without picking up would only pile items, so the magnet needs pickup on, its own
   * switch, an unlocked tier and charge for the whole cost of one item.
   */
  public static boolean magnetPulls(
      PowerState state,
      boolean pickupEnabled,
      boolean magnetEnabled,
      PickupSpec spec,
      Battery battery) {
    return collects(state, pickupEnabled)
        && magnetEnabled
        && spec.magnetUnlocked()
        && battery.charge().amount() >= spec.magnetCostPerItem().amount();
  }

  public static Battery payForPull(Battery battery, PickupSpec spec) {
    return battery.consume(spec.magnetCostPerItem()).battery();
  }
}
