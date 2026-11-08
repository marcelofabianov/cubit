package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class OwnerGazeTest {
  private static final double STILL = 0;
  private static final double SAME_LEVEL = 0;
  private static final double JUMP_START = 0.42;
  private static final double SLOW_RISE = 0.1;
  private static final double JUMP_LANDING = -0.38;
  private static final double FAST_FALL = -0.6;
  private static final double ONE_BLOCK_ABOVE = 1;
  private static final double ALMOST_ONE_BLOCK_ABOVE = 0.99;
  private static final double ONE_BLOCK_BELOW = -1;
  private static final double ALMOST_ONE_BLOCK_BELOW = -0.99;
  private static final int HOLD_TICKS = 10;

  private static OwnerGaze afterStillTicks(OwnerGaze gaze, int ticks) {
    OwnerGaze current = gaze;
    for (int tick = 0; tick < ticks; tick++) {
      current = current.next(STILL, SAME_LEVEL);
    }
    return current;
  }

  @Test
  void anOwnerAtTheSameLevelKeepsTheEyesLevel() {
    assertEquals(GazeDirection.LEVEL, OwnerGaze.LEVEL.next(STILL, SAME_LEVEL).direction());
  }

  @Test
  void aJumpMovesTheEyesUp() {
    assertEquals(GazeDirection.UP, OwnerGaze.LEVEL.next(JUMP_START, SAME_LEVEL).direction());
  }

  @Test
  void aSlowRiseDoesNotMoveTheEyes() {
    assertEquals(GazeDirection.LEVEL, OwnerGaze.LEVEL.next(SLOW_RISE, SAME_LEVEL).direction());
  }

  @Test
  void theLandingOfAJumpDoesNotMoveTheEyesDown() {
    assertEquals(GazeDirection.LEVEL, OwnerGaze.LEVEL.next(JUMP_LANDING, SAME_LEVEL).direction());
  }

  @Test
  void aFastFallMovesTheEyesDown() {
    assertEquals(GazeDirection.DOWN, OwnerGaze.LEVEL.next(FAST_FALL, SAME_LEVEL).direction());
  }

  @Test
  void theEyesHoldForAboutHalfASecondAndReturn() {
    OwnerGaze jumped = OwnerGaze.LEVEL.next(JUMP_START, SAME_LEVEL);

    assertEquals(GazeDirection.UP, afterStillTicks(jumped, HOLD_TICKS - 1).direction());
    assertEquals(GazeDirection.LEVEL, afterStillTicks(jumped, HOLD_TICKS).direction());
  }

  @Test
  void anOwnerOneBlockAboveKeepsTheEyesUpWhileItLasts() {
    OwnerGaze gaze = OwnerGaze.LEVEL.next(STILL, ONE_BLOCK_ABOVE);

    assertEquals(GazeDirection.UP, gaze.direction());
    assertEquals(GazeDirection.UP, gaze.next(STILL, ONE_BLOCK_ABOVE).direction());
    assertEquals(GazeDirection.LEVEL, gaze.next(STILL, SAME_LEVEL).direction());
  }

  @Test
  void anOwnerOneBlockBelowKeepsTheEyesDown() {
    assertEquals(GazeDirection.DOWN, OwnerGaze.LEVEL.next(STILL, ONE_BLOCK_BELOW).direction());
  }

  @Test
  void lessThanOneBlockIsTheSameLevel() {
    assertEquals(
        GazeDirection.LEVEL, OwnerGaze.LEVEL.next(STILL, ALMOST_ONE_BLOCK_ABOVE).direction());
    assertEquals(
        GazeDirection.LEVEL, OwnerGaze.LEVEL.next(STILL, ALMOST_ONE_BLOCK_BELOW).direction());
  }

  @Test
  void aQuickMovementWinsOverTheLevel() {
    assertEquals(GazeDirection.DOWN, OwnerGaze.LEVEL.next(FAST_FALL, ONE_BLOCK_ABOVE).direction());
    assertEquals(GazeDirection.UP, OwnerGaze.LEVEL.next(JUMP_START, ONE_BLOCK_BELOW).direction());
  }

  @Test
  void theLevelOfTheOwnerDependsOnlyOnTheHeight() {
    assertEquals(GazeDirection.UP, OwnerGaze.levelOf(ONE_BLOCK_ABOVE));
    assertEquals(GazeDirection.DOWN, OwnerGaze.levelOf(ONE_BLOCK_BELOW));
    assertEquals(GazeDirection.LEVEL, OwnerGaze.levelOf(ALMOST_ONE_BLOCK_ABOVE));
    assertEquals(GazeDirection.LEVEL, OwnerGaze.levelOf(ALMOST_ONE_BLOCK_BELOW));
    assertEquals(GazeDirection.LEVEL, OwnerGaze.levelOf(Double.NaN));
  }

  @Test
  void anInvalidHeightKeepsTheEyesLevel() {
    assertEquals(GazeDirection.LEVEL, OwnerGaze.LEVEL.next(Double.NaN, Double.NaN).direction());
  }

  @Test
  void theMovementOfTheRobotWinsOverTheOwner() {
    assertEquals(GazeDirection.UP, GazeDirection.UP.or(GazeDirection.DOWN));
    assertEquals(GazeDirection.DOWN, GazeDirection.DOWN.or(GazeDirection.UP));
    assertEquals(GazeDirection.UP, GazeDirection.LEVEL.or(GazeDirection.UP));
    assertEquals(GazeDirection.LEVEL, GazeDirection.LEVEL.or(GazeDirection.LEVEL));
  }
}
