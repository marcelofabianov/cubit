package io.github.marcelofabianov.cubit.domain.behavior;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.LongPredicate;

/**
 * Which spot the Fisher takes: one whose stand no other Fisher holds, with open water first,
 * because only it gives treasure, and then the nearest (answers 1188 and 1189).
 */
public final class FishingSpots {
  private static final Comparator<FishingSpot> OPEN_THEN_NEAREST =
      Comparator.comparing((FishingSpot spot) -> !spot.open())
          .thenComparingDouble(FishingSpot::distanceSquared);

  private FishingSpots() {}

  public static Optional<FishingSpot> best(Iterable<FishingSpot> spots, LongPredicate standFree) {
    FishingSpot best = null;
    for (FishingSpot spot : spots) {
      if (standFree.test(spot.stand())
          && (best == null || OPEN_THEN_NEAREST.compare(spot, best) < 0)) {
        best = spot;
      }
    }
    return Optional.ofNullable(best);
  }
}
