package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class HealTimeTest {
  private static final float MAX = 25f;
  private static final float ONE_MINUTE_HEAL_LOSS = 23f;
  private static final float HALF = 12.5f;
  private static final float EMPTY = 0f;
  private static final float BELOW_ZERO = -1f;
  private static final int ONE_MINUTE = 1_200;
  private static final int FOUR_MINUTES = 4_800;
  private static final int TWO = 2;
  private static final int FOUR = 4;
  private static final int NEGATIVE = -1;

  @Test
  void fullHealthNeedsNoTime() {
    assertEquals(HealTime.Kind.FULL, HealTime.of(MAX, MAX, ONE_MINUTE).kind());
  }

  @Test
  void lessThanAMinuteSaysSo() {
    assertEquals(
        HealTime.Kind.UNDER_A_MINUTE, HealTime.of(ONE_MINUTE_HEAL_LOSS, MAX, ONE_MINUTE).kind());
  }

  @Test
  void theTimeIsAShareOfTheFullHealRoundedToMinutes() {
    assertEquals(new HealTime(HealTime.Kind.MINUTES, TWO), HealTime.of(HALF, MAX, FOUR_MINUTES));
    assertEquals(new HealTime(HealTime.Kind.MINUTES, FOUR), HealTime.of(EMPTY, MAX, FOUR_MINUTES));
  }

  @Test
  void invalidNumbersFallBackToSafeValues() {
    assertEquals(
        new HealTime(HealTime.Kind.MINUTES, FOUR), HealTime.of(BELOW_ZERO, MAX, FOUR_MINUTES));
    assertEquals(HealTime.Kind.FULL, HealTime.of(MAX, EMPTY, ONE_MINUTE).kind());
    assertEquals(HealTime.Kind.UNDER_A_MINUTE, HealTime.of(EMPTY, MAX, NEGATIVE).kind());
  }

  @Test
  void negativeMinutesAreRejected() {
    assertThrows(
        IllegalArgumentException.class, () -> new HealTime(HealTime.Kind.MINUTES, NEGATIVE));
  }
}
