package io.github.marcelofabianov.cubit.domain.robot;

import java.util.Arrays;
import java.util.Locale;

/** Whether the pickup filter lists what the robot leaves or what it takes (answer 422). */
public enum PickupMode {
  BLACKLIST,
  WHITELIST;

  /** A new robot takes everything, with an empty blacklist (answer 423). */
  public static final PickupMode INITIAL = BLACKLIST;

  /** The name used in saves, so it cannot change without a migration (answer 891). */
  public String key() {
    return name().toLowerCase(Locale.ROOT);
  }

  /** A missing or unknown name is the blacklist, as on a robot saved before pickup existed. */
  public static PickupMode fromKeyOrInitial(String key) {
    return Arrays.stream(values())
        .filter(mode -> mode.key().equals(key))
        .findFirst()
        .orElse(INITIAL);
  }

  public PickupMode toggled() {
    return this == BLACKLIST ? WHITELIST : BLACKLIST;
  }
}
