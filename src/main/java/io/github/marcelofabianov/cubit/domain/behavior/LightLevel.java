package io.github.marcelofabianov.cubit.domain.behavior;

/** A light level of the game, which only goes from 0 to 15. */
public record LightLevel(int value) {
  public static final int MAX = 15;
  private static final String INVALID_MESSAGE = "Invalid light level: ";

  public LightLevel {
    if (value < 0 || value > MAX) {
      throw new IllegalArgumentException(INVALID_MESSAGE + value);
    }
  }

  public LightLevel max(LightLevel other) {
    return value >= other.value ? this : other;
  }
}
