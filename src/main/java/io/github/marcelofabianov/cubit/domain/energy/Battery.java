package io.github.marcelofabianov.cubit.domain.energy;

/** An immutable battery. Operations report how much FE moved, so callers never lose the rest. */
public record Battery(Energy capacity, Energy charge) {
  public record Charged(Battery battery, Energy accepted) {}

  public record Consumed(Battery battery, Energy consumed) {}

  private static final String NO_CAPACITY_MESSAGE = "Battery capacity must be positive";
  private static final String OVERCHARGE_MESSAGE = "Charge above capacity: ";
  private static final String NO_STEPS_MESSAGE = "A level needs positive steps: ";

  public Battery {
    if (capacity.isZero()) {
      throw new IllegalArgumentException(NO_CAPACITY_MESSAGE);
    }
    if (charge.amount() > capacity.amount()) {
      throw new IllegalArgumentException(OVERCHARGE_MESSAGE + charge.amount());
    }
  }

  public static Battery withCharge(Energy capacity, Percentage percentage) {
    return new Battery(
        capacity, new Energy(capacity.amount() * percentage.value() / Percentage.MAX));
  }

  public Charged recharge(Energy offered) {
    Energy accepted = offered.min(capacity.minus(charge));
    return new Charged(new Battery(capacity, charge.plus(accepted)), accepted);
  }

  public Consumed consume(Energy requested) {
    Energy consumed = requested.min(charge);
    return new Consumed(new Battery(capacity, charge.minus(consumed)), consumed);
  }

  public boolean isEmpty() {
    return charge.isZero();
  }

  /** How many of the steps of a bar to fill. Only an empty battery shows none. */
  public int level(int steps) {
    if (steps <= 0) {
      throw new IllegalArgumentException(NO_STEPS_MESSAGE + steps);
    }
    if (isEmpty()) {
      return 0;
    }
    long rounded = (charge.amount() * steps + capacity.amount() / 2) / capacity.amount();
    return (int) Math.max(rounded, 1);
  }

  public boolean isFull() {
    return charge.equals(capacity);
  }

  public boolean hasAtLeast(Percentage percentage) {
    return charge.amount() * Percentage.MAX >= capacity.amount() * percentage.value();
  }
}
