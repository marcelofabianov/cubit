package io.github.marcelofabianov.cubit.domain.dock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.dock.DockTrip.Action;
import org.junit.jupiter.api.Test;

class DockTripTest {
  private static final int STUCK_LIMIT = 3;
  private static final double FAR = 10;
  private static final double SMALL_ADVANCE = 9.95;
  private static final double REAL_ADVANCE = 9.8;
  private static final double AT_THE_ENTRANCE = 1.5;
  private static final double OUTSIDE_THE_ENTRANCE = 1.51;
  private static final int NEGATIVE = -1;

  private static DockTrip stuckFor(int ticks) {
    DockTrip trip = DockTrip.START;
    for (int tick = 0; tick < ticks; tick++) {
      trip = trip.advance(FAR, STUCK_LIMIT, false).trip();
    }
    return trip;
  }

  @Test
  void atTheEntranceTheRobotArrives() {
    assertEquals(
        Action.ARRIVE, DockTrip.START.advance(AT_THE_ENTRANCE, STUCK_LIMIT, false).action());
  }

  @Test
  void justOutsideTheEntranceTheRobotWalks() {
    assertEquals(
        Action.WALK, DockTrip.START.advance(OUTSIDE_THE_ENTRANCE, STUCK_LIMIT, false).action());
  }

  @Test
  void arrivingDoesNotCountAsTimeStuck() {
    DockTrip stuck = stuckFor(STUCK_LIMIT);

    assertEquals(stuck, stuck.advance(AT_THE_ENTRANCE, STUCK_LIMIT, false).trip());
  }

  @Test
  void upToTheLimitTheRobotKeepsWalking() {
    assertEquals(Action.WALK, stuckFor(STUCK_LIMIT - 1).advance(FAR, STUCK_LIMIT, false).action());
  }

  @Test
  void pastTheLimitARobotHeadingToAFreeDockTeleports() {
    assertEquals(Action.TELEPORT, stuckFor(STUCK_LIMIT).advance(FAR, STUCK_LIMIT, false).action());
  }

  @Test
  void pastTheLimitARobotHeadingToABusyDockWaitsWhereItIs() {
    assertEquals(Action.ARRIVE, stuckFor(STUCK_LIMIT).advance(FAR, STUCK_LIMIT, true).action());
  }

  @Test
  void aRealAdvanceStartsTheCountAgain() {
    DockTrip.Step step = stuckFor(STUCK_LIMIT).advance(REAL_ADVANCE, STUCK_LIMIT, false);

    assertEquals(Action.WALK, step.action());
    assertEquals(new DockTrip(REAL_ADVANCE, 1), step.trip());
  }

  @Test
  void aTinyAdvanceDoesNotCount() {
    assertEquals(
        Action.TELEPORT, stuckFor(STUCK_LIMIT).advance(SMALL_ADVANCE, STUCK_LIMIT, false).action());
  }

  @Test
  void aFreeDockIsEnteredOnlyByTheNextInLine() {
    assertTrue(DockTrip.entersOnArrival(false, true));
    assertFalse(DockTrip.entersOnArrival(false, false));
  }

  @Test
  void aBusyDockIsNeverEntered() {
    assertFalse(DockTrip.entersOnArrival(true, true));
  }

  @Test
  void invalidValuesAreRejected() {
    assertThrows(IllegalArgumentException.class, () -> new DockTrip(FAR, NEGATIVE));
    assertThrows(IllegalArgumentException.class, () -> new DockTrip(Double.NaN, 0));
  }
}
