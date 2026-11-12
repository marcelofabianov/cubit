package io.github.marcelofabianov.cubit.menu;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/**
 * Puts a stack in the whole robot inventory, also in the rows the screen does not show. It fills
 * the stacks of the same item first and then the empty slots, as the game does.
 */
public final class RobotStorage {
  private RobotStorage() {}

  /** True when anything moved. The stack keeps what did not fit. */
  public static boolean insert(Container inventory, ItemStack stack) {
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

  /** True when at least part of the stack fits, without moving anything. */
  public static boolean hasRoomFor(Container inventory, ItemStack stack) {
    if (!stack.getItem().canFitInsideContainerItems()) {
      return false;
    }
    for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
      ItemStack held = inventory.getItem(slot);
      if (held.isEmpty()) {
        return true;
      }
      int max = Math.min(held.getMaxStackSize(), inventory.getMaxStackSize());
      if (ItemStack.isSameItemSameComponents(held, stack) && held.getCount() < max) {
        return true;
      }
    }
    return false;
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
