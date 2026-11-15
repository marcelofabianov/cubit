package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.behavior.SeedChoice;
import io.github.marcelofabianov.cubit.domain.robot.UnloadChest;
import io.github.marcelofabianov.cubit.domain.robot.WorkArea;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * The seeds of the Farmer: any item that plants a plant, also from other mods, in its inventory or
 * in the chests of the Unload app inside its area, and which one goes on each farmland (answers
 * 1233, 1234 and 1236).
 */
final class FarmSeeds {
  private FarmSeeds() {}

  static boolean isSeed(ItemStack stack) {
    return stack.getItem() instanceof BlockItem item && item.getBlock() instanceof BushBlock;
  }

  /** The seed for the farmland, by the plants around it, or the one it has the most of. */
  static Optional<Item> choose(Container inventory, Level level, BlockPos plant) {
    Map<String, Integer> counts = new HashMap<>();
    for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
      ItemStack stack = inventory.getItem(slot);
      if (isSeed(stack)) {
        counts.merge(keyOf(stack.getItem()), stack.getCount(), Integer::sum);
      }
    }
    return SeedChoice.pick(counts, neighbors(level, plant))
        .map(
            key -> BuiltInRegistries.ITEM.get(net.minecraft.resources.ResourceLocation.parse(key)));
  }

  static boolean hasAny(Container inventory) {
    for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
      if (isSeed(inventory.getItem(slot))) {
        return true;
      }
    }
    return false;
  }

  /** Takes one of the item out of the inventory, or nothing if it has none. */
  static boolean takeOne(Container inventory, Item item) {
    for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
      if (inventory.getItem(slot).is(item)) {
        inventory.removeItem(slot, 1);
        return true;
      }
    }
    return false;
  }

  /** A chest of the Unload app inside the area that holds seeds, if any. */
  static Optional<BlockPos> chestWithSeeds(RobotEntity robot, WorkArea area) {
    return chestWith(robot, area, FarmSeeds::isSeed);
  }

  /** A chest of the Unload app inside the area that holds what the Farmer needs, if any. */
  static Optional<BlockPos> chestWith(
      RobotEntity robot, WorkArea area, Predicate<ItemStack> wanted) {
    String dimension = robot.level().dimension().location().toString();
    for (UnloadChest chest : robot.docking().unload().chests().chests()) {
      BlockPos pos = BlockPos.of(chest.block());
      boolean inside = area.contains(pos.getX(), pos.getY(), pos.getZ());
      if (chest.isIn(dimension) && inside && holds(robot, pos, wanted)) {
        return Optional.of(pos);
      }
    }
    return Optional.empty();
  }

  /** Fills the fertilizer slot from the chest, and what does not fit stays there (rule 13). */
  static void fetchFertilizer(RobotEntity robot, BlockPos chest) {
    Optional<IItemHandler> handler = robot.docking().unload().handlerAt(chest);
    RobotFertilizerSlot slot = robot.robotInventory().fertilizerSlot();
    for (int index = 0; handler.isPresent() && index < handler.get().getSlots(); index++) {
      ItemStack seen = handler.get().getStackInSlot(index);
      if (slot.isEmpty() && RobotFertilizerSlot.accepts(seen)) {
        ItemStack taken = handler.get().extractItem(index, seen.getMaxStackSize(), false);
        ItemStack rest = slot.fill(taken);
        handler.get().insertItem(index, rest, false);
      }
    }
  }

  /** Brings up to one stack of seeds from the chest, never more than fits (rule 13). */
  static void fetch(RobotEntity robot, BlockPos chest, int stack) {
    Optional<IItemHandler> handler = robot.docking().unload().handlerAt(chest);
    int left = stack;
    for (int slot = 0; handler.isPresent() && slot < handler.get().getSlots() && left > 0; slot++) {
      ItemStack seen = handler.get().getStackInSlot(slot);
      if (isSeed(seen)) {
        ItemStack taken = handler.get().extractItem(slot, left, false);
        left -= taken.getCount();
        FarmWork.store(robot, taken);
      }
    }
  }

  private static boolean holds(RobotEntity robot, BlockPos chest, Predicate<ItemStack> wanted) {
    Optional<IItemHandler> handler = robot.docking().unload().handlerAt(chest);
    for (int slot = 0; handler.isPresent() && slot < handler.get().getSlots(); slot++) {
      if (wanted.test(handler.get().getStackInSlot(slot))) {
        return true;
      }
    }
    return false;
  }

  private static List<String> neighbors(Level level, BlockPos plant) {
    List<String> keys = new ArrayList<>();
    for (Direction side : Direction.Plane.HORIZONTAL) {
      BlockPos near = plant.relative(side);
      BlockState state = level.isLoaded(near) ? level.getBlockState(near) : null;
      if (state != null && state.getBlock() instanceof BushBlock) {
        keys.add(keyOf(state.getBlock().getCloneItemStack(level, near, state).getItem()));
      }
    }
    return keys;
  }

  private static String keyOf(Item item) {
    return BuiltInRegistries.ITEM.getKey(item).toString();
  }
}
