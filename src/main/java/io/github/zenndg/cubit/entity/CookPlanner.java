package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.domain.robot.WorkArea;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

/**
 * Chooses the next errand of the Cook, smokers first: take the cooked food, then keep the fuel and
 * the raw food going, only completing what is already its own in each oven (answers 1317 to 1320).
 */
final class CookPlanner {
  private static final int INPUT_SLOT = 0;
  private static final int FUEL_SLOT = 1;

  private final RobotEntity robot;

  CookPlanner(RobotEntity robot) {
    this.robot = robot;
  }

  Optional<CookTask> next(WorkArea area) {
    List<OrganizerChests.Chest> chests = OrganizerChests.inside(robot, area);
    List<BlockPos> ovens = ovens(area);
    for (BlockPos oven : ovens) {
      Optional<CookTask> found = taskFor(oven, chests);
      if (found.isPresent() && RobotCooking.reserve(robot, oven)) {
        return found;
      }
    }
    return Optional.empty();
  }

  /** The marked ovens inside the area and free, smokers first, in the order of the marks. */
  private List<BlockPos> ovens(WorkArea area) {
    Level level = robot.level();
    return robot.workArea().work().marks().targets().stream()
        .map(OrganizerTarget::pos)
        .filter(pos -> area.contains(pos.getX(), pos.getY(), pos.getZ()))
        .filter(pos -> CookOvens.isOven(level, pos))
        .filter(pos -> RobotCooking.free(robot, pos))
        .sorted(Comparator.comparing(pos -> !CookOvens.isSmoker(level, pos)))
        .toList();
  }

  private Optional<CookTask> taskFor(BlockPos oven, List<OrganizerChests.Chest> chests) {
    if (gives(oven, Direction.DOWN, CookPlanner::isFood) && !robot.robotInventory().isFull()) {
      return Optional.of(new CookTask(CookTask.Kind.TAKE, oven, ItemStack.EMPTY));
    }
    Optional<CookTask> fuel = supply(oven, FUEL_SLOT, CookOvens::isFuel, chests);
    return fuel.isPresent()
        ? fuel
        : supply(oven, INPUT_SLOT, stack -> CookOvens.isRaw(robot.level(), stack), chests);
  }

  /**
   * Fuel or raw food for one slot of the oven: only into an empty slot or one with the same item,
   * from what it carries, or else from a chest (answer 1320).
   */
  private Optional<CookTask> supply(
      BlockPos oven, int slot, Predicate<ItemStack> kind, List<OrganizerChests.Chest> chests) {
    Optional<ItemStack> held = slotOf(oven, slot);
    if (held.isEmpty()) {
      return Optional.empty();
    }
    ItemStack inSlot = held.get();
    if (!inSlot.isEmpty()
        && (!kind.test(inSlot) || inSlot.getCount() >= inSlot.getMaxStackSize())) {
      return Optional.empty();
    }
    Predicate<ItemStack> wanted =
        inSlot.isEmpty() ? kind : stack -> ItemStack.isSameItemSameComponents(stack, inSlot);
    boolean fuel = slot == FUEL_SLOT;
    if (carries(wanted)) {
      return Optional.of(
          new CookTask(fuel ? CookTask.Kind.FUEL : CookTask.Kind.FEED, oven, inSlot));
    }
    CookTask.Kind fetch = fuel ? CookTask.Kind.FETCH_FUEL : CookTask.Kind.FETCH_RAW;
    return chests.stream()
        .filter(chest -> holds(chest.handler(), wanted))
        .findFirst()
        .map(chest -> new CookTask(fetch, chest.pos(), inSlot));
  }

  /** What the oven holds in one slot, read through the side that reaches it, if loaded. */
  private Optional<ItemStack> slotOf(BlockPos oven, int slot) {
    return robot.level().hasChunkAt(oven)
            && robot.level().getBlockEntity(oven) instanceof net.minecraft.world.Container container
        ? Optional.of(container.getItem(slot))
        : Optional.empty();
  }

  private boolean gives(BlockPos oven, Direction face, Predicate<ItemStack> wanted) {
    return OrganizerStock.at(robot.level(), oven, face).filter(h -> offers(h, wanted)).isPresent();
  }

  private static boolean offers(IItemHandler handler, Predicate<ItemStack> wanted) {
    for (int slot = 0; slot < handler.getSlots(); slot++) {
      ItemStack offered = handler.extractItem(slot, handler.getSlotLimit(slot), true);
      if (!offered.isEmpty() && wanted.test(offered)) {
        return true;
      }
    }
    return false;
  }

  private boolean carries(Predicate<ItemStack> wanted) {
    return holds(new InvWrapper(robot.inventory()), wanted);
  }

  private static boolean holds(IItemHandler handler, Predicate<ItemStack> wanted) {
    for (int slot = 0; slot < handler.getSlots(); slot++) {
      if (wanted.test(handler.getStackInSlot(slot))) {
        return true;
      }
    }
    return false;
  }

  /** Only food leaves the output, so a furnace the player smelts in keeps its ingots (1320). */
  static boolean isFood(ItemStack stack) {
    return stack.has(DataComponents.FOOD);
  }
}
