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

  /** The tier a module of this tier leads to. Ultimate is the last one. */
  public Optional<RobotTier> next() {
    int index = ordinal() + 1;
    return index < values().length ? Optional.of(values()[index]) : Optional.empty();
  }

  /** The tier a robot needs before a module of this tier. Base is the first one. */
  public Optional<RobotTier> previous() {
    return ordinal() == 0 ? Optional.empty() : Optional.of(values()[ordinal() - 1]);
  }

  public static Optional<RobotTier> fromKey(String key) {
    return Arrays.stream(values()).filter(tier -> tier.key().equals(key)).findFirst();
  }
}
