package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.domain.robot.OrganizerMode;
import io.github.zenndg.cubit.domain.robot.WorkArea;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * The orders of the Supply mode: each marked destination with an item wants it up to its amount,
 * through the face marked, and the Cubit takes it from any chest of the Unload app in its area
 * (answers 1294 to 1296). A destination it cannot reach now waits for the next look.
 */
final class OrganizerOrders {
  private OrganizerOrders() {}

  /** The first order that misses something: deliver what it carries, or fetch it first. */
  static Optional<OrganizerTask> next(
      RobotEntity robot, WorkArea area, List<OrganizerChests.Chest> chests) {
    List<OrganizerTarget> targets = targets(robot);
    String dimension = RobotUnload.dimensionOf(robot.level());
    IItemHandler carried = new net.neoforged.neoforge.items.wrapper.InvWrapper(robot.inventory());
    for (int index = 0; index < targets.size(); index++) {
      OrganizerTarget target = targets.get(index);
      BlockPos pos = target.pos();
      if (missing(robot, target, area) <= 0
          || !RobotOrganizing.SPOTS.freeFor(dimension, pos.asLong(), robot.getUUID())) {
        continue;
      }
      Optional<OrganizerTask> task = taskFor(target, index, carried, chests);
      if (task.isPresent()
          && RobotOrganizing.SPOTS.reserve(dimension, pos.asLong(), robot.getUUID())) {
        return task;
      }
    }
    return Optional.empty();
  }

  private static Optional<OrganizerTask> taskFor(
      OrganizerTarget target, int index, IItemHandler carried, List<OrganizerChests.Chest> chests) {
    if (OrganizerStock.count(carried, target.wanted()) > 0) {
      return Optional.of(new OrganizerTask(OrganizerTask.Kind.DELIVER, target.pos(), index));
    }
    return OrganizerChests.chestWith(chests, target.wanted())
        .map(chest -> new OrganizerTask(OrganizerTask.Kind.FETCH, chest, index));
  }

  /** Whether a stack it carries is for an order that still misses it, so it keeps it. */
  static boolean wanted(RobotEntity robot, ItemStack stack) {
    if (robot.workArea().work().organizing().mode() != OrganizerMode.SUPPLY) {
      return false;
    }
    Optional<WorkArea> area = robot.workArea().here();
    return area.isPresent()
        && targets(robot).stream()
            .anyMatch(target ->
                ItemStack.isSameItemSameComponents(target.wanted(), stack)
                    && missing(robot, target, area.get()) > 0);
  }

  /** Takes from the chest only what the order still misses (answer 1294). */
  static int fetch(RobotEntity robot, OrganizerTask task, IItemHandler carried) {
    Optional<OrganizerTarget> target = target(robot, task);
    Optional<WorkArea> area = robot.workArea().here();
    Optional<IItemHandler> chest = robot.docking().unload().handlerAt(task.pos());
    if (target.isEmpty() || area.isEmpty() || chest.isEmpty()) {
      return 0;
    }
    int need = missing(robot, target.get(), area.get());
    return OrganizerStock.move(chest.get(), carried, same(target.get()), need);
  }

  /** Puts into the destination, through its face, up to the amount and never past it. */
  static int deliver(RobotEntity robot, OrganizerTask task, IItemHandler carried) {
    Optional<OrganizerTarget> target = target(robot, task);
    Optional<WorkArea> area = robot.workArea().here();
    if (target.isEmpty() || area.isEmpty()) {
      return 0;
    }
    int need = missing(robot, target.get(), area.get());
    return OrganizerStock.at(robot.level(), task.pos(), target.get().face())
        .map(destination -> OrganizerStock.move(carried, destination, same(target.get()), need))
        .orElse(0);
  }

  /** What the destination still misses, or nothing for one outside the area or not loaded. */
  private static int missing(RobotEntity robot, OrganizerTarget target, WorkArea area) {
    BlockPos pos = target.pos();
    if (!target.hasOrder()
        || !target.mark().isIn(RobotUnload.dimensionOf(robot.level()))
        || !area.contains(pos.getX(), pos.getY(), pos.getZ())) {
      return 0;
    }
    return OrganizerStock.at(robot.level(), pos, target.face())
        .map(handler -> target.amount().missing(OrganizerStock.count(handler, target.wanted())))
        .orElse(0);
  }

  private static java.util.function.Predicate<ItemStack> same(OrganizerTarget target) {
    return stack -> ItemStack.isSameItemSameComponents(stack, target.wanted());
  }

  private static Optional<OrganizerTarget> target(RobotEntity robot, OrganizerTask task) {
    List<OrganizerTarget> targets = targets(robot);
    return task.target() >= 0 && task.target() < targets.size()
        ? Optional.of(targets.get(task.target()))
        : Optional.empty();
  }

  private static List<OrganizerTarget> targets(RobotEntity robot) {
    return robot.workArea().work().marks().targets();
  }
}
