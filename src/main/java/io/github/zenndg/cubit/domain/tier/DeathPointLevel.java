package io.github.zenndg.cubit.domain.tier;

import java.util.Locale;
import java.util.Optional;

/**
 * What a tier does with the spot where the owner fell: nothing, mark it with the text on respawn,
 * or also guide the owner there (answers 1117 and 1129). The keys are a config contract.
 */
public enum DeathPointLevel {
  LOCKED,
  MARK,
  GUIDE;

  public String key() {
    return name().toLowerCase(Locale.ROOT);
  }

  public static Optional<DeathPointLevel> fromKey(String key) {
    for (DeathPointLevel level : values()) {
      if (level.key().equals(key)) {
        return Optional.of(level);
      }
    }
    return Optional.empty();
  }

  public boolean marks() {
    return this != LOCKED;
  }

  public boolean guides() {
    return this == GUIDE;
  }
}
