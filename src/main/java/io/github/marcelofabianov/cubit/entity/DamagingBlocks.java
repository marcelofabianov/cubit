package io.github.marcelofabianov.cubit.entity;

import java.util.EnumSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;

/**
 * Tells which blocks hurt a robot. It goes by the route classification of the game, so blocks of
 * other mods count without a list, and looks where the game hurts a player: inside the collision
 * box, and under the feet. The ready reading of the game would also count the neighbours.
 */
final class DamagingBlocks {
  /** The same margin the game uses to find the blocks an entity is inside. */
  private static final double INSIDE_MARGIN = 1.0E-7;

  private static final Set<PathType> DAMAGING =
      EnumSet.of(PathType.DAMAGE_FIRE, PathType.DAMAGE_OTHER, PathType.DAMAGE_CAUTIOUS);

  /** A block with a dangerous neighbour is fine, because standing in its center touches nothing. */
  private static final Set<PathType> STANDABLE =
      EnumSet.of(
          PathType.WALKABLE, PathType.DANGER_FIRE, PathType.DANGER_OTHER, PathType.DAMAGE_CAUTIOUS);

  private DamagingBlocks() {}

  static boolean hurt(RobotEntity robot) {
    Level level = robot.level();
    AABB box = robot.getBoundingBox();
    BlockPos min =
        BlockPos.containing(
            box.minX + INSIDE_MARGIN, box.minY + INSIDE_MARGIN, box.minZ + INSIDE_MARGIN);
    BlockPos max =
        BlockPos.containing(
            box.maxX - INSIDE_MARGIN, box.maxY - INSIDE_MARGIN, box.maxZ - INSIDE_MARGIN);
    // Checks the chunks first: reading a block must never load one (AGENTS.md, rule 22).
    if (!level.hasChunksAt(min, max)) {
      return false;
    }
    PathfindingContext context = new PathfindingContext(level, robot);
    for (BlockPos position : BlockPos.betweenClosed(min, max)) {
      if (isDamaging(context, position)) {
        return true;
      }
    }
    return robot.onGround() && isDamaging(context, robot.getOnPosLegacy());
  }

  /** Checks the chunk first: reading a block must never load one (AGENTS.md, rule 22). */
  static boolean safeToStand(RobotEntity robot, BlockPos position) {
    Level level = robot.level();
    if (!level.hasChunkAt(position)) {
      return false;
    }
    PathfindingContext context = new PathfindingContext(level, robot);
    return STANDABLE.contains(WalkNodeEvaluator.getPathTypeStatic(robot, position))
        && !isDamaging(context, position)
        && !isDamaging(context, position.below());
  }

  private static boolean isDamaging(PathfindingContext context, BlockPos position) {
    return DAMAGING.contains(
        context.getPathTypeFromState(position.getX(), position.getY(), position.getZ()));
  }
}
