package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TrackFramesTest {
  private static final int FRAMES = 4;
  private static final int LAST_FRAME = 3;
  private static final double INSIDE_FIRST_FRAME = 0.39;
  private static final double START_OF_SECOND_FRAME = 0.4;
  private static final double INSIDE_THIRD_FRAME = 1.1;
  private static final double INSIDE_LAST_FRAME = 1.3;
  private static final double FULL_CYCLE = 1.6;
  private static final double NEGATIVE_DISTANCE = -0.1;

  @Test
  void thereAreFourFrames() {
    assertEquals(FRAMES, TrackFrames.COUNT);
  }

  @Test
  void theFrameAdvancesWithTheDistanceWalked() {
    assertEquals(0, TrackFrames.frameAt(0));
    assertEquals(0, TrackFrames.frameAt(INSIDE_FIRST_FRAME));
    assertEquals(1, TrackFrames.frameAt(START_OF_SECOND_FRAME));
    assertEquals(2, TrackFrames.frameAt(INSIDE_THIRD_FRAME));
    assertEquals(LAST_FRAME, TrackFrames.frameAt(INSIDE_LAST_FRAME));
  }

  @Test
  void theFramesRepeatAfterAFullCycle() {
    assertEquals(0, TrackFrames.frameAt(FULL_CYCLE));
    assertEquals(1, TrackFrames.frameAt(FULL_CYCLE + START_OF_SECOND_FRAME));
  }

  @Test
  void anInvalidDistanceShowsTheFirstFrame() {
    assertEquals(0, TrackFrames.frameAt(NEGATIVE_DISTANCE));
    assertEquals(0, TrackFrames.frameAt(Double.NaN));
    assertEquals(0, TrackFrames.frameAt(Double.POSITIVE_INFINITY));
  }
}
