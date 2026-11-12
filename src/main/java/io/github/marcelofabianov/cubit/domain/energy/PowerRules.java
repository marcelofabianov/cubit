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

  /** Only an active robot: in the base it hibernates, and off it is already off (answer 857). */
  public static boolean canSwitchOff(PowerState state) {
    return state == PowerState.ACTIVE;
  }

  /** The owner switches the robot off on its screen. It turns on again with a click. */
  public PowerState switchOff(PowerState state) {
    return canSwitchOff(state) ? PowerState.INACTIVE_SWITCHED_OFF : state;
  }

  public PowerState leaveBase(Battery battery) {
    return battery.isEmpty() ? PowerState.INACTIVE_EXHAUSTED : PowerState.ACTIVE;
  }
}
