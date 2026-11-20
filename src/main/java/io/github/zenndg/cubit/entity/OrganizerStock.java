package io.github.zenndg.cubit.entity;

import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

/**
 * Moves items between inventories through the capability of NeoForge, as the Organizer does: what
 * leaves one side always lands on the other, and what does not fit goes back (rule 13).
 */
final class OrganizerStock {
  private OrganizerStock() {}

  /** Checks the chunk first: reading a block must never load one (AGENTS.md, rule 22). */
  static Optional<IItemHandler> at(Level level, BlockPos pos, Direction face) {
    if (!level.hasChunkAt(pos)) {
      return Optional.empty();
    }
    return Optional.ofNullable(level.getCapability(Capabilities.ItemHandler.BLOCK, pos, face));
  }

  /** Whether the handler gives anything out, as a hopper below a machine would see it (1289). */
  static boolean givesOut(IItemHandler handler) {
    for (int slot = 0; slot < handler.getSlots(); slot++) {
      if (!handler.extractItem(slot, handler.getSlotLimit(slot), true).isEmpty()) {
        return true;
      }
    }
    return false;
  }

  /** How many of the same item, with the same data, the handler holds (answer 1293). */
  static int count(IItemHandler handler, ItemStack like) {
    int found = 0;
    for (int slot = 0; slot < handler.getSlots(); slot++) {
      ItemStack seen = handler.getStackInSlot(slot);
      if (ItemStack.isSameItemSameComponents(seen, like)) {
        found += seen.getCount();
      }
    }
    return found;
  }

  /** Whether at least one of the stack fits. */
  static boolean fits(IItemHandler handler, ItemStack stack) {
    return ItemHandlerHelper.insertItemStacked(handler, stack.copy(), true).getCount()
        < stack.getCount();
  }

  /** Moves up to the max of what the predicate wants, and says how many moved. */
  static int move(IItemHandler from, IItemHandler to, Predicate<ItemStack> wanted, int max) {
    int moved = 0;
    for (int slot = 0; slot < from.getSlots() && moved < max; slot++) {
      ItemStack seen = from.getStackInSlot(slot);
      if (!seen.isEmpty() && wanted.test(seen)) {
        moved += moveSlot(from, slot, to, max - moved);
      }
    }
    return moved;
  }

  /** One slot: only what the other side takes leaves, and a refusal goes back (rule 13). */
  private static int moveSlot(IItemHandler from, int slot, IItemHandler to, int max) {
    ItemStack offered = from.extractItem(slot, max, true);
    int room =
        offered.getCount() - ItemHandlerHelper.insertItemStacked(to, offered, true).getCount();
    if (room <= 0) {
      return 0;
    }
    ItemStack taken = from.extractItem(slot, room, false);
    ItemStack left = ItemHandlerHelper.insertItemStacked(to, taken, false);
    if (!left.isEmpty()) {
      ItemStack back = from.insertItem(slot, left, false);
      ItemHandlerHelper.insertItemStacked(from, back, false);
    }
    return taken.getCount() - left.getCount();
  }
}
