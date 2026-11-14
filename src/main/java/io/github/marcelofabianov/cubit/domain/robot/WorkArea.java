package io.github.marcelofabianov.cubit.domain.robot;

import io.github.marcelofabianov.cubit.domain.tier.AreaSpec;

/**
 * The area of a Cubit: a box of whole blocks between two corners, in one dimension (answers 342 and
 * 1051). The base is the side the limits of the tier count, and the height comes from the corners.
 */
public record WorkArea(AreaCorner min, AreaCorner max, String dimension) {
  private static final int CORNERS = 4;
  private static final double HALF = 0.5;
  private static final String INVERTED_MESSAGE = "The corners of an area are inverted";
  private static final String NO_DIMENSION_MESSAGE = "An area needs a dimension";

  public WorkArea {
    if (min.x() > max.x() || min.y() > max.y() || min.z() > max.z()) {
      throw new IllegalArgumentException(INVERTED_MESSAGE);
    }
    if (dimension.isBlank()) {
      throw new IllegalArgumentException(NO_DIMENSION_MESSAGE);
    }
  }

  /** The two clicked corners, in any order. */
  public static WorkArea between(AreaCorner first, AreaCorner second, String dimension) {
    return new WorkArea(
        new AreaCorner(
            Math.min(first.x(), second.x()),
            Math.min(first.y(), second.y()),
            Math.min(first.z(), second.z())),
        new AreaCorner(
            Math.max(first.x(), second.x()),
            Math.max(first.y(), second.y()),
            Math.max(first.z(), second.z())),
        dimension);
  }

  public int width() {
    return max.x() - min.x() + 1;
  }

  public int height() {
    return max.y() - min.y() + 1;
  }

  public int depth() {
    return max.z() - min.z() + 1;
  }

  /** Within the base and the height of the tier (answers 342 and 1050). */
  public boolean fits(AreaSpec spec) {
    return spec.unlocked()
        && width() <= spec.sizeBlocks()
        && depth() <= spec.sizeBlocks()
        && height() <= spec.heightBlocks();
  }

  public boolean isIn(String level) {
    return dimension.equals(level);
  }

  /**
   * A point inside the area, with one block over its top, where the items lie and the Cubit stands
   * on the marked blocks: two corners on the ground make a flat area that still holds them.
   */
  public boolean contains(double x, double y, double z) {
    return x >= min.x()
        && x < max.x() + 1
        && y >= min.y()
        && y < top()
        && z >= min.z()
        && z < max.z() + 1;
  }

  /** Where the area ends upwards: the block over its top, which also holds the items. */
  public int top() {
    return max.y() + 2;
  }

  public double centerX() {
    return (min.x() + max.x() + 1) * HALF;
  }

  public double centerZ() {
    return (min.z() + max.z() + 1) * HALF;
  }

  /** The center of the base within a radius of a point, as the reach of a tier (answer 1058). */
  public boolean centerWithin(double x, double z, int radius) {
    double dx = centerX() - x;
    double dz = centerZ() - z;
    return dx * dx + dz * dz <= (double) radius * radius;
  }

  /**
   * The corners of the base, in order around it, for the round along the edges (answer 1062). The
   * height of the round comes from where the Cubit walks, inside the volume. Any index goes around.
   */
  public AreaCorner corner(int index) {
    return switch (Math.floorMod(index, CORNERS)) {
      case 0 -> new AreaCorner(min.x(), min.y(), min.z());
      case 1 -> new AreaCorner(max.x(), min.y(), min.z());
      case 2 -> new AreaCorner(max.x(), min.y(), max.z());
      default -> new AreaCorner(min.x(), min.y(), max.z());
    };
  }
}
