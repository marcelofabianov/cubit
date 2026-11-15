package io.github.marcelofabianov.cubit.domain.behavior;

import java.util.Locale;
import java.util.Optional;

/**
 * Why a Cubit with a job hibernates away from the Dock. The base has only the full inventory, and
 * each job adds its own (answers 1165 and 1172). The keys are a save contract.
 */
public enum HibernationReason {
  INVENTORY_FULL;

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
