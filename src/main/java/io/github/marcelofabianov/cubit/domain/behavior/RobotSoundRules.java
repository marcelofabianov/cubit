package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

/** Which sound each situation plays. The caller informs the situation and plays the sound. */
public final class RobotSoundRules {
  private RobotSoundRules() {}

  /** Entering the base has the recharge sound, and leaving it switched off changes nothing. */
  public static Optional<PowerSound> powerSound(PowerState from, PowerState to) {
    if (to == PowerState.ACTIVE && from != PowerState.ACTIVE) {
      return Optional.of(PowerSound.POWER_ON);
    }
    if (from == PowerState.ACTIVE && to != PowerState.ACTIVE && to != PowerState.HIBERNATING) {
      return Optional.of(PowerSound.POWER_OFF);
    }
    return Optional.empty();
  }

  public static Set<ContinuousSound> playing(
      PowerState state, boolean walking, boolean burning, boolean charging) {
    Set<ContinuousSound> sounds = EnumSet.noneOf(ContinuousSound.class);
    if (state == PowerState.ACTIVE && walking) {
      sounds.add(ContinuousSound.TRACKS);
    }
    if (burning) {
      sounds.add(ContinuousSound.SHORT_CIRCUIT);
    }
    if (charging) {
      sounds.add(ContinuousSound.RECHARGING);
    }
    return sounds;
  }

  /** A full battery or an empty dock change nothing, so they play no recharge sound. */
  public static boolean isCharging(Energy before, Energy after) {
    return after.amount() > before.amount();
  }
}
