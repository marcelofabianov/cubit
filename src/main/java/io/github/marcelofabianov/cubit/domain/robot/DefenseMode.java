package io.github.marcelofabianov.cubit.domain.robot;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

/** Who the robot shocks. The tier decides which modes the owner may choose (answer 834). */
public enum DefenseMode {
  PEACEFUL,
  RETALIATE,
  AGGRESSIVE;

  /** A new robot, and one saved before the modes existed, never attacks (answer 830). */
  public static final DefenseMode INITIAL = PEACEFUL;

  /** The name used in config lists and saves, so it cannot change without a migration. */
  public String key() {
    return name().toLowerCase(Locale.ROOT);
  }

  public static Optional<DefenseMode> fromKey(String key) {
    return Arrays.stream(values()).filter(mode -> mode.key().equals(key)).findFirst();
  }
}
