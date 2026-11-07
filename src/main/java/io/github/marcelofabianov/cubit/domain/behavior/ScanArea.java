package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * The cube a scanner reads around the robot, with each block numbered. A number is all a slow scan
 * has to keep between ticks, so reading it in slices allocates nothing (AGENTS.md, rule 21).
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
