package io.github.zenndg.cubit.block;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/**
 * The output inventory of the desk. A craft only ends when everything it gives fits, so nothing is
 * lost (rule 13 and answer 1443).
 */
final class DeskOutput {
  private DeskOutput() {}

  static boolean fits(Container output, int slots, List<ItemStack> stacks) {
    List<ItemStack> copy = new ArrayList<>(slots);
    for (int slot = 0; slot < slots; slot++) {
      copy.add(output.getItem(slot).copy());
    }
    for (ItemStack stack : stacks) {
      if (!add(copy, stack.copy(), output.getMaxStackSize())) {
        return false;
      }
    }
    return true;
  }

  static void insert(Container output, int slots, List<ItemStack> stacks) {
    List<ItemStack> live = new ArrayList<>(slots);
    for (int slot = 0; slot < slots; slot++) {
      live.add(output.getItem(slot));
    }
    for (ItemStack stack : stacks) {
      add(live, stack.copy(), output.getMaxStackSize());
    }
    for (int slot = 0; slot < slots; slot++) {
      output.setItem(slot, live.get(slot));
    }
  }

  private static boolean add(List<ItemStack> slots, ItemStack stack, int containerMax) {
    for (ItemStack present : slots) {
      if (!present.isEmpty() && ItemStack.isSameItemSameComponents(present, stack)) {
        int room = Math.min(present.getMaxStackSize(), containerMax) - present.getCount();
        int moved = Math.min(room, stack.getCount());
        present.grow(moved);
        stack.shrink(moved);
      }
    }
    for (int index = 0; index < slots.size() && !stack.isEmpty(); index++) {
      if (slots.get(index).isEmpty()) {
        slots.set(index, stack.copy());
        stack.setCount(0);
      }
    }
    return stack.isEmpty();
  }
}
