package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.robot.InventoryWindow;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * The rows of the robot inventory that the screen shows. The slots of the menu stay fixed, and
 * scrolling changes which part of the inventory they point to, on both sides (answer 838).
 */
final class ScrolledContainer implements Container {
  private final Container inventory;
  private final InventoryWindow window;
  private int topRow;

  ScrolledContainer(Container inventory, InventoryWindow window) {
    this.inventory = inventory;
    this.window = window;
  }

  int topRow() {
    return topRow;
  }

  /** Kept within the inventory, whatever the caller asks. */
  void setTopRow(int row) {
    topRow = window.clampTopRow(row);
  }

  private int slotOf(int viewSlot) {
    return window.slotOf(viewSlot, topRow);
  }

  @Override
  public int getContainerSize() {
    return window.visibleSlots();
  }

  @Override
  public boolean isEmpty() {
    for (int slot = 0; slot < getContainerSize(); slot++) {
      if (!getItem(slot).isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public ItemStack getItem(int slot) {
    return inventory.getItem(slotOf(slot));
  }

  @Override
  public ItemStack removeItem(int slot, int amount) {
    return inventory.removeItem(slotOf(slot), amount);
  }

  @Override
  public ItemStack removeItemNoUpdate(int slot) {
    return inventory.removeItemNoUpdate(slotOf(slot));
  }

  @Override
  public void setItem(int slot, ItemStack stack) {
    inventory.setItem(slotOf(slot), stack);
  }

  @Override
  public void setChanged() {
    inventory.setChanged();
  }

  @Override
  public boolean stillValid(Player player) {
    return inventory.stillValid(player);
  }

  /** Only the shown part, so the hidden rows are never emptied by the screen. */
  @Override
  public void clearContent() {
    for (int slot = 0; slot < getContainerSize(); slot++) {
      setItem(slot, ItemStack.EMPTY);
    }
  }
}
