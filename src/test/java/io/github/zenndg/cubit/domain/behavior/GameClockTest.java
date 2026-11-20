package io.github.zenndg.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class GameClockTest {
  private static final long DAWN_OF_FIRST_DAY = 0L;
  private static final long NOON = 6_000L;
  private static final long DAY_42_AT_18_30 = 41L * 24_000L + 12_500L;
  private static final long JUST_BEFORE_DAWN = 23_999L;
  private static final int SIX = 6;
  private static final int TWELVE = 12;
  private static final int EIGHTEEN = 18;
  private static final int FIVE = 5;
  private static final int THIRTY = 30;
  private static final int FIFTY_NINE = 59;
  private static final long FIRST_DAY = 1L;
  private static final long DAY_42 = 42L;

  /** The world starts at 06:00 of the first day, as the game counts it. */
  @Test
  void theWorldStartsAtSixInTheMorning() {
    assertEquals(new GameClock(FIRST_DAY, SIX, 0), GameClock.of(DAWN_OF_FIRST_DAY));
    assertEquals(new GameClock(FIRST_DAY, TWELVE, 0), GameClock.of(NOON));
  }

  @Test
  void readsDayHourAndMinute() {
    assertEquals(new GameClock(DAY_42, EIGHTEEN, THIRTY), GameClock.of(DAY_42_AT_18_30));
    assertEquals(new GameClock(FIRST_DAY, FIVE, FIFTY_NINE), GameClock.of(JUST_BEFORE_DAWN));
  }

  @Test
  void rejectsANegativeTime() {
    assertThrows(IllegalArgumentException.class, () -> GameClock.of(-1L));
  }
}
