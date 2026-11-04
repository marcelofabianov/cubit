package io.github.marcelofabianov.cubit.domain.energy;

/** A whole percentage from 0 to 100. */
public record Percentage(int value) {
  public static final int MAX = 100;

  public Percentage {
    if (value < 0 || value > MAX) {
      throw new IllegalArgumentException("Percentage out of range: " + value);
    }
  }
}
