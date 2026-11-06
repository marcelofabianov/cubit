package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RobotEyesTest {
  private static final int OPEN_TICKS = 100;
  private static final int CYCLE_TICKS = 105;
  private static final int HOLD_TICKS = 10;
  private static final int THREE_TICKS = 3;
  private static final int FOUR_TICKS = 4;
  private static final double RISE = 0.1;
  private static final double FALL = -0.1;
  private static final double TINY = 0.001;

  @Test
  void theEyesStayOpenForFiveSeconds() {
    assertEquals(BlinkFrame.OPEN, BlinkCycle.frameAt(0));
    assertEquals(BlinkFrame.OPEN, BlinkCycle.frameAt(OPEN_TICKS - 1));
  }

  @Test
  void theBlinkClosesAndOpensInThreeSteps() {
    assertEquals(BlinkFrame.HALF, BlinkCycle.frameAt(OPEN_TICKS));
    assertEquals(BlinkFrame.HALF, BlinkCycle.frameAt(OPEN_TICKS + 1));
    assertEquals(BlinkFrame.CLOSED, BlinkCycle.frameAt(OPEN_TICKS + 2));
    assertEquals(BlinkFrame.HALF, BlinkCycle.frameAt(OPEN_TICKS + THREE_TICKS));
    assertEquals(BlinkFrame.HALF, BlinkCycle.frameAt(OPEN_TICKS + FOUR_TICKS));
  }

  @Test
  void theCycleRepeats() {
    assertEquals(BlinkFrame.OPEN, BlinkCycle.frameAt(CYCLE_TICKS));
    assertEquals(BlinkFrame.CLOSED, BlinkCycle.frameAt(CYCLE_TICKS + OPEN_TICKS + 2));
  }

  @Test
  void aNegativeTickDoesNotBreakTheCycle() {
    assertEquals(BlinkFrame.OPEN, BlinkCycle.frameAt(-CYCLE_TICKS));
    assertEquals(BlinkFrame.HALF, BlinkCycle.frameAt(-1));
  }

  @Test
  void theEyesFollowTheMovement() {
    assertEquals(GazeDirection.UP, GazeTracker.LEVEL.next(RISE).direction());
    assertEquals(GazeDirection.DOWN, GazeTracker.LEVEL.next(FALL).direction());
  }

  @Test
  void aTinyMovementDoesNotMoveTheEyes() {
    assertEquals(GazeDirection.LEVEL, GazeTracker.LEVEL.next(TINY).direction());
    assertEquals(GazeDirection.LEVEL, GazeTracker.LEVEL.next(-TINY).direction());
  }

  @Test
  void theDirectionHoldsForAWhileAfterTheMovement() {
    GazeTracker tracker = GazeTracker.LEVEL.next(RISE);
    for (int tick = 1; tick < HOLD_TICKS; tick++) {
      tracker = tracker.next(0);
      assertEquals(GazeDirection.UP, tracker.direction());
    }
    assertEquals(GazeDirection.LEVEL, tracker.next(0).direction());
  }

  @Test
  void aNewMovementRestartsTheHoldInItsDirection() {
    GazeTracker tracker = GazeTracker.LEVEL.next(RISE).next(0).next(FALL);
    assertEquals(GazeDirection.DOWN, tracker.direction());
  }
}
