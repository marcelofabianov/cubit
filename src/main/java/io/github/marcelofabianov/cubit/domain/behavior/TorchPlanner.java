package io.github.marcelofabianov.cubit.domain.behavior;

import java.util.Arrays;
import java.util.Optional;

/**
 * Chooses where a torch makes the most dangerous spots safe, so no torch is wasted (answers 971 and
 * 976). The light of the game starts at the level of the torch, falls one level per block and does
 * not cross a block that stops it.
 */
public final class TorchPlanner {
  private static final int NEIGHBORS = 6;
  private static final int UNLIT = -1;

  private TorchPlanner() {}

  /** The first of the best spots, or nothing when no spot would make any block safe. */
  public static Optional<GridCell> best(TorchGrid grid, LightLevel torch, LightLevel required) {
    int[] light = new int[grid.size()];
    int[] queue = new int[grid.size()];
    GridCell best = null;
    int bestCount = 0;
    for (GridCell candidate : grid.candidates()) {
      int count =
          covered(grid, grid.indexOf(candidate), torch.value(), required.value(), light, queue);
      if (count > bestCount) {
        best = candidate;
        bestCount = count;
      }
    }
    return Optional.ofNullable(best);
  }

  /** A breadth-first spread of the light, which only goes on while it can still make one safe. */
  private static int covered(
      TorchGrid grid, int start, int torch, int required, int[] light, int[] queue) {
    Arrays.fill(light, UNLIT);
    int[] next = new int[NEIGHBORS];
    int head = 0;
    int tail = 0;
    light[start] = torch;
    queue[tail++] = start;
    int count = 0;
    while (head < tail) {
      int index = queue[head++];
      if (grid.isDangerous(index)) {
        count++;
      }
      int spread = light[index] - 1;
      if (spread < required) {
        continue;
      }
      grid.neighbors(index, next);
      for (int neighbor : next) {
        if (neighbor != UNLIT && light[neighbor] == UNLIT && !grid.blocks(neighbor)) {
          light[neighbor] = spread;
          queue[tail++] = neighbor;
        }
      }
    }
    return count;
  }
}
