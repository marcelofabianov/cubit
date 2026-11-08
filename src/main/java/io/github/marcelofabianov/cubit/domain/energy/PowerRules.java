package io.github.marcelofabianov.cubit.domain.energy;

/** Decides state changes caused by energy. The wake threshold comes from the config. */
public record PowerRules(Percentage wakeThreshold) {
  public PowerState afterEnergyChange(PowerState state, Battery battery) {
    if (state == PowerState.ACTIVE && battery.isEmpty()) {
      return PowerState.INACTIVE_EXHAUSTED;
    }
    if (state == PowerState.INACTIVE_EXHAUSTED && battery.hasAtLeast(wakeThreshold)) {
      return PowerState.ACTIVE;
    }
    return state;
  }

  public PowerState turnOn(PowerState state, Battery battery) {
    if (state == PowerState.INACTIVE_SWITCHED_OFF && !battery.isEmpty()) {
      return PowerState.ACTIVE;
    }
    return state;
  }

  public PowerState leaveBase(Battery battery) {
    return battery.isEmpty() ? PowerState.INACTIVE_EXHAUSTED : PowerState.ACTIVE;
  }
}
