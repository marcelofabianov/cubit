package io.github.marcelofabianov.cubit.domain.robot;

import java.util.Optional;

/**
 * The name the owner gives a Cubit on the Tablet, with the rules of the name tag of the game: up to
 * 50 characters, without the spaces at the ends, and an empty name removes the name (answers 1015
 * to 1017).
 */
public record RobotName(String value) {
  /** The limit of the anvil, so the Tablet names as the name tag does. */
  public static final int MAX_LENGTH = 50;

  private static final String INVALID_MESSAGE = "A name is cut and has up to 50 characters: ";

  public RobotName {
    if (value == null || !value.strip().equals(value) || value.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(INVALID_MESSAGE + value);
    }
  }

  /** What the player typed, or nothing when it does not fit. */
  public static Optional<RobotName> typed(String text) {
    String cut = text.strip();
    return cut.length() > MAX_LENGTH ? Optional.empty() : Optional.of(new RobotName(cut));
  }

  public boolean removes() {
    return value.isEmpty();
  }
}
