package io.github.marcelofabianov.cubit.domain.energy;

/**
 * Splits an amount of energy in two 16-bit halves and joins them back. A screen value travels in 16
 * bits, and a battery holds more than that.
 */
public final class EnergyHalves {
  private static final int HALF_BITS = 16;
  private static final int HALF_MASK = 0xFFFF;
  private static final long LARGEST = 0xFFFF_FFFFL;

  private EnergyHalves() {}

  public static int low(Energy energy) {
    return (int) (shown(energy) & HALF_MASK);
  }

  public static int high(Energy energy) {
    return (int) (shown(energy) >>> HALF_BITS);
  }

  /** The halves may arrive sign-extended, so only their 16 bits count. */
  public static Energy join(int low, int high) {
    long joined = ((long) (high & HALF_MASK) << HALF_BITS) | (low & HALF_MASK);
    return new Energy(joined);
  }

  /** Two halves hold up to 32 bits, far above any battery, so a larger amount shows the top. */
  private static long shown(Energy energy) {
    return Math.min(energy.amount(), LARGEST);
  }
}
