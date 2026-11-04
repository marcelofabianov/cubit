package io.github.marcelofabianov.cubit.domain.tier;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

/** The robot evolution levels, in order. */
public enum RobotTier {
  BASE,
  ADVANCED,
  ELITE,
  ULTIMATE;

  /** The name used in config keys and saves, so it cannot change without a migration. */
  public String key() {
    return name().toLowerCase(Locale.ROOT);
  }

  public static Optional<RobotTier> fromKey(String key) {
    return Arrays.stream(values()).filter(tier -> tier.key().equals(key)).findFirst();
  }
}
