package io.github.zenndg.cubit.entity;

import java.util.EnumSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

/**
 * The route of a Cubit goes through a shut door only when its owner marked it and the Cubit can pay
 * to open it, also for iron, which the mobs of the game never open (answers 1364 to 1368). The
 * other doors stay as the game sees them, shut for the Cubit.
 */
final class RobotNodeEvaluator extends WalkNodeEvaluator {
  private static final Set<PathType> SHUT =
      EnumSet.of(
          PathType.DOOR_WOOD_CLOSED, PathType.DOOR_IRON_CLOSED, PathType.FENCE, PathType.TRAPDOOR);
  private static final Set<PathType> TWO_HIGH =
      EnumSet.of(PathType.DOOR_WOOD_CLOSED, PathType.DOOR_IRON_CLOSED);

  private Set<Long> passable = Set.of();

  @Override
  public void prepare(PathNavigationRegion region, Mob mob) {
    super.prepare(region, mob);
    passable = mob instanceof RobotEntity robot ? DoorPassage.passable(robot) : Set.of();
  }

  @Override
  public void done() {
    super.done();
    passable = Set.of();
  }

  @Override
  public PathType getPathType(PathfindingContext context, int x, int y, int z) {
    PathType type = super.getPathType(context, x, y, z);
    if (passable.isEmpty() || !SHUT.contains(type)) {
      return type;
    }
    boolean marked =
        passable.contains(BlockPos.asLong(x, y, z))
            || (TWO_HIGH.contains(type) && passable.contains(BlockPos.asLong(x, y - 1, z)));
    return marked ? PathType.WALKABLE_DOOR : type;
  }
}
