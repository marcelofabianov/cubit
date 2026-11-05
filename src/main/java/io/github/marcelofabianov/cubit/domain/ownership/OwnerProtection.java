package io.github.marcelofabianov.cubit.domain.ownership;

import java.util.Optional;
import java.util.UUID;

/**
 * The owner cannot hurt their own robot. Fire from a lighter has no attacker, so it still hurts.
 */
public final class OwnerProtection {
  private OwnerProtection() {}

  public static boolean blocksDamage(Optional<UUID> owner, Optional<UUID> attacker) {
    return owner.isPresent() && owner.equals(attacker);
  }
}
