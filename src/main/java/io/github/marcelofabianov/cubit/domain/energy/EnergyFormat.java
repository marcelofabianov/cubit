package io.github.marcelofabianov.cubit.domain.energy;

import java.util.Locale;

/** How the screens write energy: amounts short, like 214k, and rates whole, like 2,400. */
public final class EnergyFormat {
  private static final long THOUSAND = 1_000;
  private static final long MILLION = 1_000_000;
  private static final long TENTHS_PER_UNIT = 10;
  private static final String THOUSANDS_SUFFIX = "k";
  private static final String MILLIONS_SUFFIX = "M";
  private static final String DECIMAL_POINT = ".";
  private static final String GROUPED = "%,d";

  private EnergyFormat() {}

  /** Rounded down, so the screen never shows more energy than there is. */
  public static String compact(Energy energy) {
    long amount = energy.amount();
    if (amount < THOUSAND) {
      return Long.toString(amount);
    }
    if (amount < MILLION) {
      return amount / THOUSAND + THOUSANDS_SUFFIX;
    }
    long tenths = amount / (MILLION / TENTHS_PER_UNIT);
    long whole = tenths / TENTHS_PER_UNIT;
    long decimal = tenths % TENTHS_PER_UNIT;
    String number = decimal == 0 ? Long.toString(whole) : whole + DECIMAL_POINT + decimal;
    return number + MILLIONS_SUFFIX;
  }

  /** The root locale always groups with a comma, whatever the language of the player. */
  public static String rate(Energy energy) {
    return String.format(Locale.ROOT, GROUPED, energy.amount());
  }
}
