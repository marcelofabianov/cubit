package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.domain.behavior.FishingSpot;
import io.github.zenndg.cubit.domain.behavior.OpenWater;
import io.github.zenndg.cubit.domain.robot.WorkArea;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

/**
 * The search for the spots of the Fisher in its area, spread over the ticks with the budget of
 * blocks of the scanners (rules 21 and 22): a place to stand on the shore of still water, and the
 * point of water it casts to, within the reach, with open water first (answers 1188 and 1208).
 */
final class FishingScan {
  /** A point closer than this to the stand has the shore inside its 5 by 5, so it is never open. */
  private static final int OPEN_MIN_DISTANCE = OpenWater.HALF_SIDE + 1;

  private final RobotEntity robot;
  private final WorkArea area;
  private final FishingWater water;
  private final Map<Long, Boolean> openCache = new HashMap<>();
  private final List<FishingSpot> found = new ArrayList<>();
  private final long volume;
  private long next;

  FishingScan(RobotEntity robot, WorkArea area) {
    this.robot = robot;
    this.area = area;
    this.water = new FishingWater(robot.level());
    this.volume = (long) area.width() * area.depth() * area.height();
  }

  boolean done() {
    return next >= volume;
  }

  List<FishingSpot> found() {
    return found;
  }

  /** Reads until the budget of this tick is spent or the area is over. */
  void step(int budget, int reach) {
    int limit = water.reads() + budget;
    while (!done() && water.reads() < limit) {
      BlockPos stand = standAt(next++);
      if (onShore(stand) && standable(stand)) {
        spotFrom(stand, reach);
      }
    }
  }

  /** The feet of the Cubit, one block over each marked block, as it stands in its area. */
  private BlockPos standAt(long index) {
    int x = (int) (index % area.width());
    int z = (int) (index / area.width() % area.depth());
    int y = (int) (index / ((long) area.width() * area.depth()));
    return new BlockPos(area.min().x() + x, area.min().y() + y + 1, area.min().z() + z);
  }

  /**
   * As FollowOwnerGoal.canStandAt, but the shore counts too: the game marks a block by the water as
   * a water border, not as walkable, and the robot still walks on it.
   */
  private boolean standable(BlockPos stand) {
    if (!robot.level().hasChunkAt(stand)) {
      return false;
    }
    PathType type = WalkNodeEvaluator.getPathTypeStatic(robot, stand);
    return (type == PathType.WALKABLE || type == PathType.WATER_BORDER)
        && robot
            .level()
            .noCollision(robot, robot.getBoundingBox().move(stand.subtract(robot.blockPosition())));
  }

  private boolean onShore(BlockPos stand) {
    for (Direction side : Direction.Plane.HORIZONTAL) {
      if (water.fishable(stand.below().relative(side))) {
        return true;
      }
    }
    return false;
  }

  /** The nearest open point within the reach, or else the nearest still water. */
  private void spotFrom(BlockPos stand, int reach) {
    BlockPos ground = stand.below();
    BlockPos closed = null;
    for (BlockPos point : PointsByDistance.around(ground, reach)) {
      if (!inArea(point) || !water.fishable(point)) {
        continue;
      }
      if (distance(ground, point) >= OPEN_MIN_DISTANCE && openAt(point)) {
        found.add(spot(stand, point, true));
        return;
      }
      closed = closed == null ? point : closed;
    }
    if (closed != null) {
      found.add(spot(stand, closed, false));
    }
  }

  private boolean openAt(BlockPos point) {
    return openCache.computeIfAbsent(point.asLong(), key -> water.open(point));
  }

  private boolean inArea(BlockPos point) {
    return area.contains(point.getX(), point.getY(), point.getZ());
  }

  private FishingSpot spot(BlockPos stand, BlockPos point, boolean open) {
    double distance = robot.blockPosition().distSqr(stand);
    return new FishingSpot(stand.asLong(), point.asLong(), open, distance);
  }

  private static int distance(BlockPos from, BlockPos to) {
    return Math.max(Math.abs(from.getX() - to.getX()), Math.abs(from.getZ() - to.getZ()));
  }
}
