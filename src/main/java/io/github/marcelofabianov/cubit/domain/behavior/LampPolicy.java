package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;

/** When the lamp lights. Two limits keep it from flickering when the light is close to one. */
public record LampPolicy(LightLevel onBelow, LightLevel offFrom) {
  private static final String REVERSED_MESSAGE = "The lamp would go off before it could go on: ";

  public LampPolicy {
    if (offFrom.value() < onBelow.value()) {
      throw new IllegalArgumentException(REVERSED_MESSAGE + onBelow + offFrom);
    }
  }

  /** For numbers from outside: the upper limit never stays below the lower one. */
  public static LampPolicy fitting(LightLevel onBelow, LightLevel offFrom) {
    return new LampPolicy(onBelow, onBelow.max(offFrom));
  }

  public static boolean allows(boolean switchedOn, PowerState state) {
    return switchedOn && state == PowerState.ACTIVE;
  }

  public boolean staysLit(
      boolean switchedOn, PowerState state, LightLevel ambient, boolean wasLit) {
    if (!allows(switchedOn, state)) {
      return false;
    }
    if (ambient.value() < onBelow.value()) {
      return true;
    }
    return wasLit && ambient.value() < offFrom.value();
  }
}
