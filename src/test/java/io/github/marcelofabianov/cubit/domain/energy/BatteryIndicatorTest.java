package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BatteryIndicatorTest {
  private static final Energy CAPACITY = new Energy(300_000);
  private static final Percentage CRITICAL = new Percentage(10);
  private static final Percentage HIGH_CRITICAL = new Percentage(75);
  private static final Percentage NO_CRITICAL = new Percentage(0);
  private static final Percentage ALWAYS_CRITICAL = new Percentage(100);
  private static final long ONE_FE = 1;
  private static final long AT_CRITICAL = 30_000;
  private static final long ABOVE_CRITICAL = 30_001;
  private static final long QUARTER = 75_000;
  private static final long BELOW_SECOND_LEVEL = 112_499;
  private static final long START_OF_SECOND_LEVEL = 112_500;
  private static final long HALF = 150_000;
  private static final long THREE_QUARTERS = 225_000;
  private static final long ABOVE_HIGH_CRITICAL = 225_001;
  private static final long FULL = 300_000;

  private static BatteryIndicator indicator(long charge, Percentage critical) {
    return BatteryIndicator.of(new Battery(CAPACITY, new Energy(charge)), critical);
  }

  private static BatteryIndicator indicator(long charge) {
    return indicator(charge, CRITICAL);
  }

  @Test
  void onlyAnEmptyBatteryShowsNoFill() {
    assertEquals(BatteryIndicator.EMPTY, indicator(0));
    assertEquals(BatteryIndicator.CRITICAL_1, indicator(ONE_FE));
  }

  @Test
  void theEmptyIndicatorWinsOverTheCriticalColor() {
    assertEquals(BatteryIndicator.EMPTY, indicator(0, ALWAYS_CRITICAL));
  }

  @Test
  void theFillTurnsCriticalAtTheConfiguredLimit() {
    assertEquals(BatteryIndicator.CRITICAL_1, indicator(AT_CRITICAL));
    assertEquals(BatteryIndicator.NORMAL_1, indicator(ABOVE_CRITICAL));
  }

  @Test
  void theFillGrowsByQuarters() {
    assertEquals(BatteryIndicator.NORMAL_1, indicator(QUARTER));
    assertEquals(BatteryIndicator.NORMAL_1, indicator(BELOW_SECOND_LEVEL));
    assertEquals(BatteryIndicator.NORMAL_2, indicator(START_OF_SECOND_LEVEL));
    assertEquals(BatteryIndicator.NORMAL_2, indicator(HALF));
    assertEquals(BatteryIndicator.NORMAL_3, indicator(THREE_QUARTERS));
    assertEquals(BatteryIndicator.NORMAL_4, indicator(FULL));
  }

  @Test
  void aHighCriticalLimitKeepsTheRealLevel() {
    assertEquals(BatteryIndicator.CRITICAL_2, indicator(HALF, HIGH_CRITICAL));
    assertEquals(BatteryIndicator.CRITICAL_3, indicator(THREE_QUARTERS, HIGH_CRITICAL));
    assertEquals(BatteryIndicator.NORMAL_3, indicator(ABOVE_HIGH_CRITICAL, HIGH_CRITICAL));
    assertEquals(BatteryIndicator.CRITICAL_4, indicator(FULL, ALWAYS_CRITICAL));
  }

  @Test
  void aZeroCriticalLimitNeverShowsTheCriticalColor() {
    assertEquals(BatteryIndicator.NORMAL_1, indicator(ONE_FE, NO_CRITICAL));
  }
}
