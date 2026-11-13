package io.github.marcelofabianov.cubit.domain.dock;

import java.util.Objects;
import java.util.Optional;

/**
 * The Dock the owner marked for a Cubit with the Dock app of the Tablet: its block and its
 * dimension. A click on the same Dock removes the mark (answers 1008 and 1045).
 */
public record PreferredDock(long block, String dimension) {
  private static final String INVALID_MESSAGE = "A preferred dock needs a dimension";

  public PreferredDock {
    if (dimension == null || dimension.isBlank()) {
      throw new IllegalArgumentException(INVALID_MESSAGE);
    }
  }

  /** The mark after a click on a Dock of the owner. */
  public static Optional<PreferredDock> clicked(
      Optional<PreferredDock> current, PreferredDock dock) {
    return current.filter(dock::equals).isPresent() ? Optional.empty() : Optional.of(dock);
  }

  public boolean isIn(String otherDimension) {
    return Objects.equals(dimension, otherDimension);
  }
}
