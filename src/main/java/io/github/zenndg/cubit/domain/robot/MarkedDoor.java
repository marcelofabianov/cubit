package io.github.zenndg.cubit.domain.robot;

/**
 * A door, a trapdoor or a fence gate marked with the Doors app, by its block and its dimension
 * (answers 1360 and 1365). A door is kept by its lower half.
 */
public record MarkedDoor(long block, String dimension) {
  private static final String NO_DIMENSION_MESSAGE = "A door needs a dimension";

  public MarkedDoor {
    if (dimension.isBlank()) {
      throw new IllegalArgumentException(NO_DIMENSION_MESSAGE);
    }
  }

  public boolean isIn(String level) {
    return dimension.equals(level);
  }
}
