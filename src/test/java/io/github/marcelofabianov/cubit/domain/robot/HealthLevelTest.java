package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HealthLevelTest {
  private static final float MAX = 25f;
  private static final float HALF = 12.5f;
  private static final float TINY = 0.01f;
  private static final int FULL_PERCENT = 100;
  private static final int HALF_PERCENT = 50;

  @Test
  void fullHealthIsAHundredPercent() {
    assertEquals(FULL_PERCENT, HealthLevel.percent(MAX, MAX));
  }

  @Test
  void halfHealthIsFiftyPercent() {
    assertEquals(HALF_PERCENT, HealthLevel.percent(HALF, MAX));
  }

  @Test
  void anyHealthShowsAtLeastOnePercent() {
    assertEquals(1, HealthLevel.percent(TINY, MAX));
  }

  @Test
  void noHealthIsZero() {
    assertEquals(0, HealthLevel.percent(0, MAX));
  }

  @Test
  void healthAboveTheMaximumStaysAtAHundred() {
    assertEquals(FULL_PERCENT, HealthLevel.percent(MAX + 1, MAX));
  }
}
