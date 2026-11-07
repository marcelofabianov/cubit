package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LeftBehindWatchTest {
  private static final int NEAR = 3;
  private static final int FAR = 16;
  private static final int TELEPORT = 20;
  private static final double CLOSE = 2;
  private static final double WALKING = 10;
  private static final double AT_THE_LIMIT = 16;
  private static final double FARTHER = 18;
  private static final double JUMPED = 25;

  private static LeftBehindWatch.Step step(LeftBehindWatch watch, double distance) {
    return watch.next(distance, NEAR, FAR, TELEPORT);
  }

  @Test
  void nearbyItSaysNothing() {
    assertFalse(step(LeftBehindWatch.READY, WALKING).speaks());
  }

  @Test
  void itSpeaksWhenTheOwnerGetsFar() {
    assertTrue(step(LeftBehindWatch.READY, AT_THE_LIMIT).speaks());
  }

  @Test
  void itSpeaksOnceWhileTheOwnerGetsFarther() {
    LeftBehindWatch after = step(LeftBehindWatch.READY, AT_THE_LIMIT).watch();

    assertFalse(step(after, FARTHER).speaks());
    assertFalse(step(step(after, WALKING).watch(), AT_THE_LIMIT).speaks());
  }

  @Test
  void itCanSpeakAgainAfterGettingClose() {
    LeftBehindWatch after = step(LeftBehindWatch.READY, AT_THE_LIMIT).watch();
    LeftBehindWatch close = step(after, CLOSE).watch();

    assertTrue(step(close, AT_THE_LIMIT).speaks());
  }

  @Test
  void aJumpPastTheTeleportSaysNothingAndWaitsToGetClose() {
    LeftBehindWatch.Step jump = step(LeftBehindWatch.READY, JUMPED);

    assertFalse(jump.speaks());
    assertFalse(step(jump.watch(), AT_THE_LIMIT).speaks());
  }
}
