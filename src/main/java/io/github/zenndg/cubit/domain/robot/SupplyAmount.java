package io.github.zenndg.cubit.domain.robot;

/**
 * How many of the item a destination should hold, from 1 to 64, set with − and +, 1 at a time and,
 * with Shift, 8 (answer 1300). The Cubit fills up to it and never past it (answer 1294).
 */
public record SupplyAmount(int count) {
  private static final int LOWEST = 1;
  private static final int HIGHEST = 64;
  private static final int STEP = 1;
  private static final int BIG_STEP = 8;
  private static final String INVALID = "Invalid supply amount: ";

  public static final SupplyAmount MIN = new SupplyAmount(LOWEST);
  public static final SupplyAmount MAX = new SupplyAmount(HIGHEST);

  public SupplyAmount {
    if (count < LOWEST || count > HIGHEST) {
      throw new IllegalArgumentException(INVALID + count);
    }
  }

  /** One press of − or +, by 8 with Shift, stopping at 1 and at 64. */
  public SupplyAmount step(int direction, boolean big) {
    int next = count + Integer.signum(direction) * (big ? BIG_STEP : STEP);
    return new SupplyAmount(Math.clamp(next, LOWEST, HIGHEST));
  }

  /** What the destination still needs, given what it holds. */
  public int missing(int present) {
    return Math.max(count - present, 0);
  }
}
