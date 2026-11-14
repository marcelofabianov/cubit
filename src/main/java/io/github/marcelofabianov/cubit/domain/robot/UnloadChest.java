package io.github.marcelofabianov.cubit.domain.robot;

/** A block with an inventory marked with the Unload app, by its block and its dimension. */
public record UnloadChest(long block, String dimension) {
  private static final String NO_DIMENSION_MESSAGE = "A chest needs a dimension";

  public UnloadChest {
    if (dimension.isBlank()) {
      throw new IllegalArgumentException(NO_DIMENSION_MESSAGE);
    }
  }

  public boolean isIn(String level) {
    return dimension.equals(level);
  }
}
