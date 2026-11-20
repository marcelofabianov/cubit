package io.github.zenndg.cubit.domain.robot;

/**
 * A block marked with the Mark app: a machine to organize from, or a destination to supply, by its
 * block, its dimension and the face that was clicked, as the game numbers the six faces (answers
 * 1288, 1296 and 1297).
 */
public record OrganizerMark(long block, String dimension, int face) {
  private static final int FACES = 6;
  private static final String NO_DIMENSION = "A mark needs a dimension";
  private static final String INVALID_FACE = "Invalid face: ";

  public OrganizerMark {
    if (dimension.isBlank()) {
      throw new IllegalArgumentException(NO_DIMENSION);
    }
    if (face < 0 || face >= FACES) {
      throw new IllegalArgumentException(INVALID_FACE + face);
    }
  }

  /** The same block in the same dimension, whatever face was clicked. */
  public boolean samePlace(long otherBlock, String otherDimension) {
    return block == otherBlock && dimension.equals(otherDimension);
  }

  public boolean isIn(String level) {
    return dimension.equals(level);
  }
}
