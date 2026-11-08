package io.github.marcelofabianov.cubit.domain.behavior;

/** Where the eyes look. */
public enum GazeDirection {
  LEVEL,
  UP,
  DOWN;

  /** The movement of the robot comes first, so the other direction only counts without one. */
  public GazeDirection or(GazeDirection other) {
    return this == LEVEL ? other : this;
  }
}
