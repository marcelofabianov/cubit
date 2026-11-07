package io.github.marcelofabianov.cubit.domain.ownership;

import java.util.Optional;
import java.util.UUID;

/** Who owns a robot or a dock. Without an owner, nothing matches, so the answer is always no. */
public final class Ownership {
  private Ownership() {}

  public static boolean owns(Optional<UUID> owner, UUID candidate) {
    return owner.filter(candidate::equals).isPresent();
  }

  public static boolean sameOwner(Optional<UUID> first, Optional<UUID> second) {
    return first.isPresent() && first.equals(second);
  }
}
