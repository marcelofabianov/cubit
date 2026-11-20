package io.github.zenndg.cubit.domain.behavior;

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
    return reason(withCard, inArea, full, canUnload, Optional.empty());
  }

  /**
   * The job may also stop it, as the Fisher with no water (answer 1188). The full inventory comes
   * first, because it is what Resume asks to fix before the job can go on.
   */
  public static Optional<HibernationReason> reason(
      boolean withCard,
      boolean inArea,
      boolean full,
      boolean canUnload,
      Optional<HibernationReason> jobReason) {
    if (!withCard || !inArea) {
      return Optional.empty();
    }
    if (full && !canUnload) {
      return Optional.of(HibernationReason.INVENTORY_FULL);
    }
    return jobReason;
  }
}
