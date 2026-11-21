package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.behavior.DoorRules;
import io.github.zenndg.cubit.domain.energy.Energy;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;

/**
 * Opens the marked door on the route of the Cubit, paying for it, and shuts the doors it opened
 * once it left them and nothing stands in them (answers 1366 to 1370). It takes no flag, so it runs
 * along with whatever the Cubit is doing.
 */
final class DoorGoal extends Goal {
  private static final double REACH_SQUARED = 2.25;
  private static final double LEAVE_SQUARED = 4.0;
  private static final double HALF = 0.5;
  private static final int LOOK_AHEAD = 2;

  private final RobotEntity robot;
  private final Set<BlockPos> opened = new HashSet<>();
  private final Set<BlockPos> entered = new HashSet<>();

  DoorGoal(RobotEntity robot) {
    this.robot = robot;
  }

  @Override
  public boolean canUse() {
    if (!(robot.level() instanceof ServerLevel)) {
      return false;
    }
    return !opened.isEmpty() || doorAhead().isPresent();
  }

  @Override
  public boolean canContinueToUse() {
    return canUse();
  }

  @Override
  public boolean requiresUpdateEveryTick() {
    return true;
  }

  @Override
  public void tick() {
    ServerLevel level = (ServerLevel) robot.level();
    doorAhead().ifPresent(door -> open(level, door));
    shutBehind(level);
  }

  private void open(ServerLevel level, BlockPos door) {
    Energy cost = CubitMod.CONFIG.doors().openCost();
    if (!DoorRules.opens(robot.powerState(), robot.battery(), cost)) {
      return;
    }
    robot.setBattery(DoorRules.pay(robot.battery(), cost));
    DoorBlocks.setOpen(level, door, true, robot);
    opened.add(door);
  }

  /** A shut marked door on the next nodes of the route, within reach. */
  private Optional<BlockPos> doorAhead() {
    Path path = robot.getNavigation().getPath();
    if (path == null || path.isDone()) {
      return Optional.empty();
    }
    int first = Math.max(path.getNextNodeIndex() - 1, 0);
    int last = Math.min(path.getNextNodeIndex() + LOOK_AHEAD, path.getNodeCount());
    for (int index = first; index < last; index++) {
      Node node = path.getNode(index);
      Optional<BlockPos> door = shutMarkedDoor(new BlockPos(node.x, node.y, node.z));
      if (door.isPresent()) {
        return door;
      }
    }
    return Optional.empty();
  }

  private Optional<BlockPos> shutMarkedDoor(BlockPos pos) {
    return DoorBlocks.markedBlock(robot.level(), pos)
        .filter(door -> !DoorBlocks.isOpen(robot.level().getBlockState(door)))
        .filter(door -> horizontalDistanceSquared(door) <= REACH_SQUARED)
        .filter(door -> DoorPassage.isMarked(robot, door));
  }

  private void shutBehind(ServerLevel level) {
    Iterator<BlockPos> doors = opened.iterator();
    while (doors.hasNext()) {
      BlockPos door = doors.next();
      if (!DoorBlocks.isDoor(level.getBlockState(door))) {
        doors.remove();
        entered.remove(door);
      } else if (DoorRules.closes(true, isNear(door), isOccupied(level, door))) {
        DoorBlocks.setOpen(level, door, false, robot);
        doors.remove();
        entered.remove(door);
      }
    }
  }

  /**
   * In the block of the door, or, while it has not gone through it yet, still close to it: it shuts
   * as soon as it leaves the block it went through, or once it took another way (answer 1370).
   */
  private boolean isNear(BlockPos door) {
    boolean inside = robot.getBoundingBox().intersects(new AABB(door));
    if (inside) {
      entered.add(door);
      return true;
    }
    return !entered.contains(door) && horizontalDistanceSquared(door) <= LEAVE_SQUARED;
  }

  /** The owner or anything alive in the door, also in the upper half of a door. */
  private boolean isOccupied(ServerLevel level, BlockPos door) {
    AABB space = new AABB(door).expandTowards(0, 1, 0);
    return !level
        .getEntitiesOfClass(LivingEntity.class, space, entity -> entity != robot)
        .isEmpty();
  }

  private double horizontalDistanceSquared(BlockPos door) {
    return robot.distanceToSqr(door.getX() + HALF, robot.getY(), door.getZ() + HALF);
  }
}
