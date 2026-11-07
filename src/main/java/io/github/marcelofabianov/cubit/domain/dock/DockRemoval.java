package io.github.marcelofabianov.cubit.domain.dock;

import io.github.marcelofabianov.cubit.domain.ownership.Ownership;
import java.util.Optional;
import java.util.UUID;

/**
 * Who may break a dock. An operator in creative removes abandoned docks and those without owner.
 */
public final class DockRemoval {
  private DockRemoval() {}

  public static boolean allows(
      boolean creative, boolean operator, Optional<UUID> owner, UUID player) {
    return (creative && operator) || Ownership.owns(owner, player);
  }
}
