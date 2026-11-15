package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;
import java.util.function.LongPredicate;
import org.junit.jupiter.api.Test;

class FishingSpotsTest {
  private static final long NEAR_STAND = 1L;
  private static final long FAR_STAND = 2L;
  private static final long WATER = 9L;
  private static final double NEAR = 4;
  private static final double FAR = 64;
  private static final boolean OPEN = true;
  private static final boolean CLOSED = false;
  private static final LongPredicate ALL_FREE = stand -> true;
  private static final FishingSpot NEAR_CLOSED = new FishingSpot(NEAR_STAND, WATER, CLOSED, NEAR);
  private static final FishingSpot FAR_OPEN = new FishingSpot(FAR_STAND, WATER, OPEN, FAR);
  private static final FishingSpot NEAR_OPEN = new FishingSpot(NEAR_STAND, WATER, OPEN, NEAR);

  /** Open water gives treasure, so it comes before a nearer spot (answer 1188). */
  @Test
  void openWaterComesFirst() {
    assertEquals(
        Optional.of(FAR_OPEN), FishingSpots.best(List.of(NEAR_CLOSED, FAR_OPEN), ALL_FREE));
  }

  @Test
  void amongOpenSpotsTheNearestWins() {
    assertEquals(Optional.of(NEAR_OPEN), FishingSpots.best(List.of(FAR_OPEN, NEAR_OPEN), ALL_FREE));
  }

  /** A stand another Fisher holds is not taken (answer 1189). */
  @Test
  void aHeldStandIsSkipped() {
    LongPredicate farHeld = stand -> stand != FAR_STAND;
    assertEquals(
        Optional.of(NEAR_CLOSED), FishingSpots.best(List.of(NEAR_CLOSED, FAR_OPEN), farHeld));
  }

  @Test
  void withNoFreeSpotThereIsNone() {
    assertEquals(Optional.empty(), FishingSpots.best(List.of(NEAR_OPEN), stand -> false));
    assertEquals(Optional.empty(), FishingSpots.best(List.of(), ALL_FREE));
  }
}
