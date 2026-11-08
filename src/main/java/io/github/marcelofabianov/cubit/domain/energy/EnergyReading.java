package io.github.marcelofabianov.cubit.domain.energy;

/**
 * A charge read against its capacity, to show as a percentage and in FE, such as in Jade (answers
 * 660 and 662). Like the bars, it never shows zero while some energy is left (answer 269).
 */
public record EnergyReading(Energy charge, Energy capacity) {
  private static final String MORE_THAN_CAPACITY = "A charge cannot pass its capacity: ";
  private static final int FULL = Percentage.MAX;

  public EnergyReading {
    if (charge.amount() > capacity.amount()) {
      throw new IllegalArgumentException(MORE_THAN_CAPACITY + charge.amount());
    }
  }

  /** Read data comes from outside the domain, so a charge past the capacity is capped to it. */
  public static EnergyReading capped(long charge, long capacity) {
    Energy limit = new Energy(Math.max(capacity, 0));
    return new EnergyReading(new Energy(Math.clamp(charge, 0, limit.amount())), limit);
  }

  public int percent() {
    if (charge.isZero() || capacity.isZero()) {
      return 0;
    }
    return (int) Math.max(1, charge.amount() * FULL / capacity.amount());
  }
}
