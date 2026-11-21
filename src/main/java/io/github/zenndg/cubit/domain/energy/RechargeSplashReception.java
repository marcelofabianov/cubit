package io.github.zenndg.cubit.domain.energy;

import io.github.zenndg.cubit.domain.ownership.Ownership;
import java.util.Optional;
import java.util.UUID;

/**
 * What a Recharge Splash does to one robot: only the robots of the thrower in the radius receive,
 * one splash at a time (answers 1409 and 1416).
 */
public enum RechargeSplashReception {
  STARTS,
  STILL_RECHARGING,
  IGNORED;

  public static RechargeSplashReception of(
      Optional<UUID> owner, UUID thrower, boolean inRadius, RechargeSplashDose current) {
    if (!inRadius || !Ownership.owns(owner, thrower)) {
      return IGNORED;
    }
    return current.isActive() ? STILL_RECHARGING : STARTS;
  }
}
