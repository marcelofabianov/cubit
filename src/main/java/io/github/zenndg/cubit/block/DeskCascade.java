package io.github.zenndg.cubit.block;

import java.util.List;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * The cascade of the desk: when the input misses materials and the input and the output together
 * make one item of the recipe, the desk pulls what is missing from the output, as far as it fits
 * (answers 1500 to 1503). Items only move between its inventories, so nothing is created or lost.
 */
final class DeskCascade {
  private DeskCascade() {}

  static void pull(List<Ingredient> grid, Container input, Container output, int slots) {
    int[] fromInput = new int[slots];
    int[] fromOutput = new int[slots];
    for (Ingredient mark : grid) {
      if (mark.isEmpty()) {
        continue;
      }
      int slot = serving(mark, input, slots, fromInput);
      if (slot >= 0) {
        fromInput[slot]++;
        continue;
      }
      int other = serving(mark, output, slots, fromOutput);
      if (other < 0) {
        return;
      }
      fromOutput[other]++;
    }
    move(output, input, slots, fromOutput);
  }

  private static int serving(Ingredient mark, Container items, int slots, int[] taken) {
    for (int slot = 0; slot < slots; slot++) {
      ItemStack stack = items.getItem(slot);
      if (stack.getCount() > taken[slot] && mark.test(stack)) {
        return slot;
      }
    }
    return -1;
  }

  /** Each item leaves the output only after the input took it. */
  private static void move(Container output, Container input, int slots, int[] wanted) {
    for (int slot = 0; slot < slots; slot++) {
      for (int count = 0; count < wanted[slot]; count++) {
        ItemStack one = output.getItem(slot).copyWithCount(1);
        if (one.isEmpty() || !DeskOutput.fits(input, slots, List.of(one))) {
          break;
        }
        DeskOutput.insert(input, slots, List.of(one));
        output.removeItem(slot, 1);
      }
    }
  }
}
