package io.github.marcelofabianov.cubit.domain.behavior;

import java.util.ArrayList;
import java.util.List;

/**
 * A snapshot of the blocks around the robot that the torch planner reads: which ones stop the
 * light, which ones are dangerous and where a torch could go. The game layer fills it once per
 * scan, so the planner never reads the world (answer 976).
 */
public final class TorchGrid {
  private static final String SIZE_MESSAGE = "A torch grid needs a positive size: ";
  private static final String OUTSIDE_MESSAGE = "The cell is outside the torch grid: ";
  private static final String BY = "x";
  private static final int WEST = 0;
  private static final int EAST = 1;
  private static final int DOWN = 2;
  private static final int UP = 3;
  private static final int NORTH = 4;
  private static final int SOUTH = 5;

  private final int sizeX;
  private final int sizeY;
  private final int sizeZ;
  private final boolean[] blocking;
  private final boolean[] dangerous;
  private final List<GridCell> candidates = new ArrayList<>();

  public TorchGrid(int sizeX, int sizeY, int sizeZ) {
    if (sizeX <= 0 || sizeY <= 0 || sizeZ <= 0) {
      throw new IllegalArgumentException(SIZE_MESSAGE + sizeX + BY + sizeY + BY + sizeZ);
    }
    this.sizeX = sizeX;
    this.sizeY = sizeY;
    this.sizeZ = sizeZ;
    blocking = new boolean[sizeX * sizeY * sizeZ];
    dangerous = new boolean[blocking.length];
  }

  /** A full block that stops the light, like a wall. */
  public void markBlocking(GridCell cell) {
    blocking[indexOf(cell)] = true;
  }

  /** A spot where a mob could spawn now. */
  public void markDangerous(GridCell cell) {
    dangerous[indexOf(cell)] = true;
  }

  /** A free block where the game would let a torch stand, on the floor or on a wall. */
  public void addCandidate(GridCell cell) {
    indexOf(cell);
    candidates.add(cell);
  }

  public boolean contains(GridCell cell) {
    return cell.x() >= 0
        && cell.x() < sizeX
        && cell.y() >= 0
        && cell.y() < sizeY
        && cell.z() >= 0
        && cell.z() < sizeZ;
  }

  List<GridCell> candidates() {
    return candidates;
  }

  int size() {
    return blocking.length;
  }

  boolean blocks(int index) {
    return blocking[index];
  }

  boolean isDangerous(int index) {
    return dangerous[index];
  }

  int indexOf(GridCell cell) {
    if (!contains(cell)) {
      throw new IllegalArgumentException(OUTSIDE_MESSAGE + cell);
    }
    return (cell.y() * sizeZ + cell.z()) * sizeX + cell.x();
  }

  /** The neighbors of an index, or -1 where the grid ends: west, east, down, up, north, south. */
  void neighbors(int index, int[] out) {
    int x = index % sizeX;
    int z = index / sizeX % sizeZ;
    int y = index / (sizeX * sizeZ);
    out[WEST] = x > 0 ? index - 1 : -1;
    out[EAST] = x < sizeX - 1 ? index + 1 : -1;
    out[DOWN] = y > 0 ? index - sizeX * sizeZ : -1;
    out[UP] = y < sizeY - 1 ? index + sizeX * sizeZ : -1;
    out[NORTH] = z > 0 ? index - sizeX : -1;
    out[SOUTH] = z < sizeZ - 1 ? index + sizeX : -1;
  }
}
