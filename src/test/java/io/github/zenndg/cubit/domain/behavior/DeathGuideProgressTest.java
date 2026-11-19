package io.github.zenndg.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DeathGuideProgressTest {
  private static final double FAR = 100.0;
  private static final double CLOSER = 98.5;
  private static final double BARELY_CLOSER = 99.5;
  private static final boolean WAITING = true;
  private static final boolean MOVING = false;

  @Test
  void countsTicksWithoutGettingCloser() {
    DeathGuideProgress progress = DeathGuideProgress.start(FAR).after(FAR, MOVING);

    assertEquals(1, progress.stillTicks());
  }

  /** Getting at least one block closer counts as progress. */
  @Test
  void aBlockCloserStartsCountingAgain() {
    DeathGuideProgress progress =
        DeathGuideProgress.start(FAR).after(FAR, MOVING).after(CLOSER, MOVING);

    assertEquals(0, progress.stillTicks());
    assertEquals(CLOSER, progress.closest());
  }

  @Test
  void lessThanABlockIsNoProgress() {
    DeathGuideProgress progress = DeathGuideProgress.start(FAR).after(BARELY_CLOSER, MOVING);

    assertEquals(1, progress.stillTicks());
  }

  /** Waiting for the owner is not being stuck. */
  @Test
  void waitingDoesNotCount() {
    DeathGuideProgress progress =
        DeathGuideProgress.start(FAR).after(FAR, MOVING).after(FAR, WAITING);

    assertEquals(0, progress.stillTicks());
  }
}
