package io.github.marcelofabianov.cubit.menu;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/**
 * Puts a stack in the whole robot inventory, also in the rows the screen does not show. It fills
 * the stacks of the same item first and then the empty slots, as the game does.
 */
final class RobotStorage {
  private RobotStorage() {}

  /** True when anything moved. The stack keeps what did not fit. */
  static boolean insert(Container inventory, ItemStack stack) {
    if (!stack.getItem().canFitInsideContainerItems()) {
      return false;
    }
    int before = stack.getCount();
    merge(inventory, stack);
    fillEmpty(inventory, stack);
    if (stack.getCount() != before) {
      inventory.setChanged();
    }
    return stack.getCount() != before;
  }

  private static void merge(Container inventory, ItemStack stack) {
    for (int slot = 0; slot < inventory.getContainerSize() && !stack.isEmpty(); slot++) {
      ItemStack held = inventory.getItem(slot);
      if (ItemStack.isSameItemSameComponents(held, stack)) {
        int room = Math.min(held.getMaxStackSize(), inventory.getMaxStackSize()) - held.getCount();
        int moved = Math.min(room, stack.getCount());
        if (moved > 0) {
          held.grow(moved);
          stack.shrink(moved);
        }
      }
    }
  }

  private static void fillEmpty(Container inventory, ItemStack stack) {
    for (int slot = 0; slot < inventory.getContainerSize() && !stack.isEmpty(); slot++) {
      if (inventory.getItem(slot).isEmpty()) {
        int moved = Math.min(stack.getCount(), inventory.getMaxStackSize(stack));
        inventory.setItem(slot, stack.split(moved));
      }
    }
  }
}
