package io.github.marcelofabianov.cubit.domain.behavior;

import java.util.Optional;

/**
 * When a Cubit with a job hibernates away from the Dock: with a card, in its area, full and with
 * nowhere to unload. Without a card it keeps the rule of the area (answers 196, 1052, 1088 and
 * 1165). Resume asks this again, so a reason that did not go away puts it back to sleep (253).
 */
public final class AwayHibernation {
  private AwayHibernation() {}

  public static Optional<HibernationReason> reason(
      boolean withCard, boolean inArea, boolean full, boolean canUnload) {
    if (withCard && inArea && full && !canUnload) {
      return Optional.of(HibernationReason.INVENTORY_FULL);
    }
    return Optional.empty();
  }
}
