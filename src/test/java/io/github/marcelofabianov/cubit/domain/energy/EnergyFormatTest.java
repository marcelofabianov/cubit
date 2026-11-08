package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EnergyFormatTest {
  private static final long LARGEST_EXACT = 999;
  private static final long ONE_THOUSAND = 1_000;
  private static final long FIFTEEN_THOUSAND = 15_000;
  private static final long ALMOST_215_THOUSAND = 214_999;
  private static final long LARGEST_IN_THOUSANDS = 999_999;
  private static final long ONE_MILLION = 1_000_000;
  private static final long TWO_POINT_ONE_MILLION = 2_100_000;
  private static final long ALMOST_TWO_POINT_TWO_MILLION = 2_199_999;
  private static final long EIGHT_MILLION = 8_000_000;
  private static final long SOLAR_RATE = 5;
  private static final long DOCK_INPUT = 2_400;
  private static final long DOCK_LIMIT = 5_000;

  private static final String ZERO_TEXT = "0";
  private static final String LARGEST_EXACT_TEXT = "999";
  private static final String ONE_THOUSAND_TEXT = "1k";
  private static final String FIFTEEN_THOUSAND_TEXT = "15k";
  private static final String ALMOST_215_THOUSAND_TEXT = "214k";
  private static final String LARGEST_IN_THOUSANDS_TEXT = "999k";
  private static final String ONE_MILLION_TEXT = "1M";
  private static final String TWO_POINT_ONE_MILLION_TEXT = "2.1M";
  private static final String EIGHT_MILLION_TEXT = "8M";
  private static final String SOLAR_RATE_TEXT = "5";
  private static final String DOCK_INPUT_TEXT = "2,400";
  private static final String DOCK_LIMIT_TEXT = "5,000";

  private static String compact(long amount) {
    return EnergyFormat.compact(new Energy(amount));
  }

  private static String rate(long amount) {
    return EnergyFormat.rate(new Energy(amount));
  }

  @Test
  void smallAmountsAreExact() {
    assertEquals(ZERO_TEXT, compact(0));
    assertEquals(LARGEST_EXACT_TEXT, compact(LARGEST_EXACT));
  }

  @Test
  void thousandsAreRoundedDownSoTheScreenNeverShowsMoreThanThereIs() {
    assertEquals(ONE_THOUSAND_TEXT, compact(ONE_THOUSAND));
    assertEquals(FIFTEEN_THOUSAND_TEXT, compact(FIFTEEN_THOUSAND));
    assertEquals(ALMOST_215_THOUSAND_TEXT, compact(ALMOST_215_THOUSAND));
    assertEquals(LARGEST_IN_THOUSANDS_TEXT, compact(LARGEST_IN_THOUSANDS));
  }

  @Test
  void millionsKeepOneDecimalOnlyWhenItIsNotZero() {
    assertEquals(ONE_MILLION_TEXT, compact(ONE_MILLION));
    assertEquals(TWO_POINT_ONE_MILLION_TEXT, compact(TWO_POINT_ONE_MILLION));
    assertEquals(TWO_POINT_ONE_MILLION_TEXT, compact(ALMOST_TWO_POINT_TWO_MILLION));
    assertEquals(EIGHT_MILLION_TEXT, compact(EIGHT_MILLION));
  }

  @Test
  void ratesAreWholeWithAThousandsSeparator() {
    assertEquals(SOLAR_RATE_TEXT, rate(SOLAR_RATE));
    assertEquals(DOCK_INPUT_TEXT, rate(DOCK_INPUT));
    assertEquals(DOCK_LIMIT_TEXT, rate(DOCK_LIMIT));
  }
}
