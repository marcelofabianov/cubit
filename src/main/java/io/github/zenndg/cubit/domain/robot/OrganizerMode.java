package io.github.zenndg.cubit.domain.robot;

import java.util.Locale;
import java.util.Optional;

/**
 * The mode of the Organizer, one at a time, chosen in the Work tab: it starts organizing (answers
 * 1290 and 1301). The keys are a contract of the save.
 */
public enum OrganizerMode {
  ORGANIZE,
  SUPPLY;

  public static final OrganizerMode INITIAL = ORGANIZE;

  public String key() {
    return name().toLowerCase(Locale.ROOT);
  }

  /** The other mode, as the line Mode of the Work tab switches. */
  public OrganizerMode next() {
    return this == ORGANIZE ? SUPPLY : ORGANIZE;
  }

  public static Optional<OrganizerMode> fromKey(String key) {
    for (OrganizerMode mode : values()) {
      if (mode.key().equals(key)) {
        return Optional.of(mode);
      }
    }
    return Optional.empty();
  }
}
