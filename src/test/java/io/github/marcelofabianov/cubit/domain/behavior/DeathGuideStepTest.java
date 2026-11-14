package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DeathGuideStepTest {
  private static final int ARRIVAL = 3;
  private static final int LEAD = 8;
  private static final int STUCK_TICKS = 200;
  private static final DeathGuideSpec SPEC = new DeathGuideSpec(ARRIVAL, LEAD, STUCK_TICKS);
  private static final double FAR = 100.0;
  private static final double CLOSE = 2.0;
  private static final double NEAR_OWNER = 4.0;
  private static final double AHEAD_OF_OWNER = 9.0;

  @Test
  void movesOnWhileTheOwnerKeepsUp() {
    assertEquals(
        DeathGuideStep.MOVE,
        DeathGuideStep.of(FAR, NEAR_OWNER, DeathGuideProgress.start(FAR), SPEC));
  }

  /** It never goes further ahead than the lead, to stay in the owner's chunks (answer 1126). */
  @Test
  void waitsWhenTooFarAheadOfTheOwner() {
    assertEquals(
        DeathGuideStep.WAIT,
        DeathGuideStep.of(FAR, AHEAD_OF_OWNER, DeathGuideProgress.start(FAR), SPEC));
  }

  @Test
  void arrivesCloseToTheSpot() {
    assertEquals(
        DeathGuideStep.ARRIVED,
        DeathGuideStep.of(CLOSE, AHEAD_OF_OWNER, DeathGuideProgress.start(FAR), SPEC));
  }

  /** Without getting closer for the whole time, the guide gives up (answer 1123). */
  @Test
  void givesUpWithoutProgress() {
    DeathGuideProgress progress = DeathGuideProgress.start(FAR);
    for (int tick = 0; tick < STUCK_TICKS; tick++) {
      progress = progress.after(FAR, false);
    }

    assertEquals(DeathGuideStep.STUCK, DeathGuideStep.of(FAR, NEAR_OWNER, progress, SPEC));
  }

  @Test
  void theNumbersArePositive() {
    assertThrows(IllegalArgumentException.class, () -> new DeathGuideSpec(0, LEAD, STUCK_TICKS));
    assertThrows(IllegalArgumentException.class, () -> new DeathGuideSpec(ARRIVAL, 0, STUCK_TICKS));
    assertThrows(IllegalArgumentException.class, () -> new DeathGuideSpec(ARRIVAL, LEAD, 0));
  }
}
