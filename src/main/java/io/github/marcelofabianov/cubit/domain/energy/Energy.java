package io.github.marcelofabianov.cubit.domain.energy;

/** A whole amount of Forge Energy. Never negative, so a transfer cannot create or lose FE. */
public record Energy(long amount) {
  public static final Energy ZERO = new Energy(0);
  private static final String NEGATIVE_MESSAGE = "Energy cannot be negative: ";

  public Energy {
    if (amount < 0) {
      throw new IllegalArgumentException(NEGATIVE_MESSAGE + amount);
    }
  }

  public Energy plus(Energy other) {
    return new Energy(Math.addExact(amount, other.amount));
  }

  public Energy minus(Energy other) {
    return new Energy(amount - other.amount);
  }

  public Energy min(Energy other) {
    return amount <= other.amount ? this : other;
  }

  public boolean isZero() {
    return amount == 0;
  }
}
