package io.github.zenndg.cubit.domain.behavior;

/**
 * Where the Fisher stands, on the shore, and the point of the water it fishes, within the reach of
 * its cast, with whether that water is open and how far the stand is from the Cubit (answers 1188
 * and 1208). The blocks are packed positions, as the game keeps them.
 */
public record FishingSpot(long stand, long water, boolean open, double distanceSquared) {
  private static final String NEGATIVE_DISTANCE = "A distance cannot be negative: ";

  public FishingSpot {
    if (!(distanceSquared >= 0)) {
      throw new IllegalArgumentException(NEGATIVE_DISTANCE + distanceSquared);
    }
  }
}
