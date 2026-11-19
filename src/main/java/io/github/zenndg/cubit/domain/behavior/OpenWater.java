package io.github.zenndg.cubit.domain.behavior;

import java.util.List;

/**
 * The open water of the game, from FishingHook.calculateOpenWater: a volume of 5 by 4 by 5 around
 * the point, read in layers from the bottom, each of one kind only, with water under air and
 * nothing else (answer 1183). Only open water gives treasure.
 */
public final class OpenWater {
  /** The half side of each layer: two blocks on each side of the point, as in the game. */
  public static final int HALF_SIDE = 2;

  /** The first layer is one block under the point, and the last is two above. */
  public static final int LOWEST_LAYER = -1;

  public static final int HIGHEST_LAYER = 2;

  private OpenWater() {}

  /** A layer is of one kind when all its blocks are, and broken otherwise, as in the game. */
  public static WaterCell layer(Iterable<WaterCell> cells) {
    WaterCell kind = null;
    for (WaterCell cell : cells) {
      if (kind != null && kind != cell) {
        return WaterCell.INVALID;
      }
      kind = cell;
    }
    return kind == null ? WaterCell.INVALID : kind;
  }

  /** The layers from the bottom: water may only be under air, and no layer may be broken. */
  public static boolean isOpen(List<WaterCell> layers) {
    WaterCell previous = WaterCell.INVALID;
    for (WaterCell layer : layers) {
      if (layer == WaterCell.INVALID) {
        return false;
      }
      if (layer == WaterCell.ABOVE_WATER && previous == WaterCell.INVALID) {
        return false;
      }
      if (layer == WaterCell.INSIDE_WATER && previous == WaterCell.ABOVE_WATER) {
        return false;
      }
      previous = layer;
    }
    return !layers.isEmpty();
  }
}
