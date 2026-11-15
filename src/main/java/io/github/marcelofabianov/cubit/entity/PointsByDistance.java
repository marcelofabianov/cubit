package io.github.marcelofabianov.cubit.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;

/** The points of one level within a square reach of a block, the nearest first. */
final class PointsByDistance {
  private PointsByDistance() {}

  static List<BlockPos> around(BlockPos center, int reach) {
    List<BlockPos> points = new ArrayList<>();
    for (int dx = -reach; dx <= reach; dx++) {
      for (int dz = -reach; dz <= reach; dz++) {
        if (dx != 0 || dz != 0) {
          points.add(center.offset(dx, 0, dz));
        }
      }
    }
    points.sort(Comparator.comparingDouble(center::distSqr));
    return points;
  }
}
