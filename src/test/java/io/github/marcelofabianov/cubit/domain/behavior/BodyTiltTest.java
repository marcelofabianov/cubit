package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BodyTiltTest {
  private static final float FULL_TILT = 8f;
  private static final float ONE_STEP = 1f;
  private static final float HALF_TICK = 0.5f;
  private static final float HALF_STEP = 0.5f;
  private static final int TICKS_TO_FULL_TILT = 8;
  private static final int MANY_TICKS = 20;
  private static final float PRECISION = 0.001f;

  private static BodyTilt afterTicks(BodyTilt start, GazeDirection level, int ticks) {
    BodyTilt current = start;
    for (int tick = 0; tick < ticks; tick++) {
      current = current.next(level);
    }
    return current;
  }

  @Test
  void itStartsStraight() {
    assertEquals(0f, BodyTilt.STRAIGHT.at(1f), PRECISION);
  }

  @Test
  void itTiltsUpSlowly() {
    assertEquals(ONE_STEP, BodyTilt.STRAIGHT.next(GazeDirection.UP).at(1f), PRECISION);
  }

  @Test
  void itTiltsUpOnlyUpToTheLimit() {
    BodyTilt full = afterTicks(BodyTilt.STRAIGHT, GazeDirection.UP, TICKS_TO_FULL_TILT);
    assertEquals(FULL_TILT, full.at(1f), PRECISION);
    assertEquals(
        FULL_TILT, afterTicks(BodyTilt.STRAIGHT, GazeDirection.UP, MANY_TICKS).at(1f), PRECISION);
  }

  @Test
  void itTiltsDownOnlyUpToTheLimit() {
    assertEquals(-ONE_STEP, BodyTilt.STRAIGHT.next(GazeDirection.DOWN).at(1f), PRECISION);
    assertEquals(
        -FULL_TILT,
        afterTicks(BodyTilt.STRAIGHT, GazeDirection.DOWN, MANY_TICKS).at(1f),
        PRECISION);
  }

  @Test
  void itReturnsSlowlyWhenTheOwnerComesBack() {
    BodyTilt full = afterTicks(BodyTilt.STRAIGHT, GazeDirection.UP, MANY_TICKS);

    assertEquals(FULL_TILT - ONE_STEP, full.next(GazeDirection.LEVEL).at(1f), PRECISION);
    assertEquals(0f, afterTicks(full, GazeDirection.LEVEL, MANY_TICKS).at(1f), PRECISION);
  }

  @Test
  void theDrawingBlendsTheLastTwoTicks() {
    BodyTilt tilted = BodyTilt.STRAIGHT.next(GazeDirection.UP);

    assertEquals(0f, tilted.at(0f), PRECISION);
    assertEquals(HALF_STEP, tilted.at(HALF_TICK), PRECISION);
  }
}
