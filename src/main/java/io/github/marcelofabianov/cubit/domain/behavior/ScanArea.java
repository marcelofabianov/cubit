package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * The sphere a scanner reads around the robot, numbered over the cube that holds it. A number is
 * all a slow scan has to keep between ticks, so reading it in slices allocates nothing (AGENTS.md,
 * rule 21). A sphere, so nothing found is farther than the radius in any direction (answer 604).
 */
public record ScanArea(int radius) {
  private static final String INVALID_MESSAGE = "A scan needs a radius: ";

  public ScanArea {
    if (radius < 1) {
      throw new IllegalArgumentException(INVALID_MESSAGE + radius);
    }
  }

  public int blocks() {
    return side() * side() * side();
  }

  /** Whether the numbered block lies within the radius, measured from the block of the robot. */
  public boolean holds(int index) {
    int x = xOf(index);
    int y = yOf(index);
    int z = zOf(index);
    return holdsDistance(x * x + y * y + z * z);
  }

  /** Whether something at this squared distance from the robot lies within the radius. */
  public boolean holdsDistance(double squaredDistance) {
    return squaredDistance <= (double) radius * radius;
  }

  public int xOf(int index) {
    return index % side() - radius;
  }

  public int yOf(int index) {
    return index / side() % side() - radius;
  }

  public int zOf(int index) {
    return index / side() / side() - radius;
  }

  private int side() {
    return radius * 2 + 1;
  }
}
