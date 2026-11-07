package io.github.marcelofabianov.cubit.block;

import io.github.marcelofabianov.cubit.domain.ownership.Ownership;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** Finds robots and docks around a position. Runs on a click or on an interval, never per tick. */
public final class DockFinder {
  private DockFinder() {}

  /** The robot of this owner that answers a call, the nearest one inside its own radius. */
  static Optional<DockableRobot> nearestRobot(ServerLevel level, BlockPos dock, UUID owner) {
    Vec3 center = Vec3.atCenterOf(dock);
    AABB area = new AABB(dock).inflate(TierSpec.MAX_CALL_RADIUS_BLOCKS);
    return level.getEntitiesOfClass(Mob.class, area).stream()
        .filter(mob -> mob instanceof DockableRobot robot && answers(robot, mob, center, owner))
        .min(Comparator.comparingDouble(mob -> mob.distanceToSqr(center)))
        .map(mob -> (DockableRobot) mob);
  }

  private static boolean answers(DockableRobot robot, Entity mob, Vec3 center, UUID owner) {
    int radius = robot.dockCallRadius();
    return robot.answersDockCall()
        && Ownership.owns(robot.ownerId(), owner)
        && mob.distanceToSqr(center) <= (double) radius * radius;
  }

  /** Reads only the chunks already loaded: a search must never load one (AGENTS.md, rule 22). */
  public static Optional<InductionDockBlockEntity> nearestFreeDock(
      ServerLevel level, BlockPos from, int radius, UUID owner) {
    InductionDockBlockEntity best = null;
    double bestDistance = (double) radius * radius;
    int minX = SectionPos.blockToSectionCoord(from.getX() - radius);
    int maxX = SectionPos.blockToSectionCoord(from.getX() + radius);
    int minZ = SectionPos.blockToSectionCoord(from.getZ() - radius);
    int maxZ = SectionPos.blockToSectionCoord(from.getZ() + radius);
    for (int x = minX; x <= maxX; x++) {
      for (int z = minZ; z <= maxZ; z++) {
        if (!level.hasChunk(x, z)) {
          continue;
        }
        for (BlockEntity entity : level.getChunk(x, z).getBlockEntities().values()) {
          double distance = entity.getBlockPos().distSqr(from);
          if (entity instanceof InductionDockBlockEntity dock
              && distance < bestDistance
              && Ownership.owns(dock.owner(), owner)
              && !dock.isOccupied()) {
            best = dock;
            bestDistance = distance;
          }
        }
      }
    }
    return Optional.ofNullable(best);
  }
}
