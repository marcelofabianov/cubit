package io.github.marcelofabianov.cubit.domain.dock;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;

/**
 * When a Cubit goes back to a Dock by itself: with the switch on, active, following its owner and
 * at the return charge of its tier or below, unless it is already on its way (answers 1065 to
 * 1068).
 */
public record AutoReturn(boolean switchedOn, Percentage returnCharge) {
  public boolean returns(PowerState state, Battery battery, boolean following, boolean onTrip) {
    return switchedOn
        && state == PowerState.ACTIVE
        && following
        && !onTrip
        && battery.hasAtMost(returnCharge);
  }
}
