package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.behavior.DoorRules;
import io.github.zenndg.cubit.domain.robot.MarkedDoor;
import io.github.zenndg.cubit.player.SavedDoorMarks;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

/**
 * The marked doors a Cubit may go through now: the ones its owner marked in its dimension, and only
 * while it is active and has charge for the cost of opening one (answers 1364 and 1368).
 */
final class DoorPassage {
  private DoorPassage() {}

  static Set<Long> passable(RobotEntity robot) {
    if (!(robot.level() instanceof ServerLevel level)
        || !DoorRules.opens(
            robot.powerState(), robot.battery(), CubitMod.CONFIG.doors().openCost())) {
      return Set.of();
    }
    String dimension = level.dimension().location().toString();
    return robot
        .ownerId()
        .map(owner -> SavedDoorMarks.of(level.getServer()).marksOf(owner).blocksIn(dimension))
        .orElse(Set.of());
  }

  /** Whether the owner of the Cubit marked this door, by its lower half. */
  static boolean isMarked(RobotEntity robot, BlockPos door) {
    if (!(robot.level() instanceof ServerLevel level)) {
      return false;
    }
    MarkedDoor marked = new MarkedDoor(door.asLong(), level.dimension().location().toString());
    return robot
        .ownerId()
        .map(owner -> SavedDoorMarks.of(level.getServer()).marksOf(owner).contains(marked))
        .orElse(false);
  }
}
