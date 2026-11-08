package io.github.marcelofabianov.cubit.domain.energy;

/** A whole percentage from 0 to 100. */
public record Percentage(int value) {
  public static final int MAX = 100;
  private static final String OUT_OF_RANGE_MESSAGE = "Percentage out of range: ";

  public Percentage {
    if (value < 0 || value > MAX) {
      throw new IllegalArgumentException(OUT_OF_RANGE_MESSAGE + value);
    }
  }
}
