package io.github.marcelofabianov.cubit.block;

import io.github.marcelofabianov.cubit.item.PowerBankItem;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

/**
 * What the automation sees of a dock, by any face: the input first, then the storage (answers 925
 * and 953).
 */
final class DockItemHandler {
  private DockItemHandler() {}

  /**
   * Redstone goes into the input, which never gives back, because the energy it becomes never comes
   * back out. A Power Bank never goes there by automation, only into the storage.
   */
  static IItemHandler of(Container input, IItemHandlerModifiable storage) {
    return new CombinedInvWrapper(
        new InvWrapper(input) {
          @Override
          public ItemStack insertItem(int index, ItemStack stack, boolean simulate) {
            return PowerBankItem.is(stack) ? stack : super.insertItem(index, stack, simulate);
          }

          @Override
          public ItemStack extractItem(int index, int amount, boolean simulate) {
            return ItemStack.EMPTY;
          }
        },
        storage);
  }
}
