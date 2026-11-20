package io.github.zenndg.cubit.domain.behavior;

/** What one marked chest holds of the item the Organizer carries, and whether it fits there. */
public record ChestStock(int sameItems, boolean room) {
  private static final String NEGATIVE = "A chest cannot hold a negative count: ";

  public ChestStock {
    if (sameItems < 0) {
      throw new IllegalArgumentException(NEGATIVE + sameItems);
    }
  }
}
