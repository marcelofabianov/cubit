package io.github.zenndg.cubit.domain.behavior;

import java.util.Locale;
import java.util.Optional;

/**
 * Why a Cubit with a job hibernates away from the Dock: the full inventory, from the base, and the
 * Fisher with no water in its area (answers 1165, 1172 and 1188). The keys are a save contract.
 */
public enum HibernationReason {
  INVENTORY_FULL,
  NO_WATER;

  public String key() {
    return name().toLowerCase(Locale.ROOT);
  }

  public static Optional<HibernationReason> fromKey(String key) {
    for (HibernationReason reason : values()) {
      if (reason.key().equals(key)) {
        return Optional.of(reason);
      }
    }
    return Optional.empty();
  }
}
