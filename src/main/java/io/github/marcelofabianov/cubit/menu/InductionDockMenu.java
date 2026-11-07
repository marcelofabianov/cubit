package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.item.EnergySlotItems;
import io.github.marcelofabianov.cubit.registry.CubitMenus;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/** The screen of a dock. The server decides who may keep it open. */
public final class InductionDockMenu extends AbstractContainerMenu {
  public static final int SLOT_X = 80;
  public static final int SLOT_Y = 20;
  public static final int PLAYER_INVENTORY_Y = 51;
  private static final int SLOT_SIZE = 18;
  private static final int FIRST_SLOT_X = 8;
  private static final int HOTBAR_GAP = 58;
  private static final int PLAYER_ROWS = 3;
  private static final int SLOTS_PER_ROW = 9;
  private static final int DOCK_SLOTS = 1;

  private final Predicate<Player> stillValid;
  private int shownEnergyPercent;

  private InductionDockMenu(
      int containerId,
      Inventory playerInventory,
      Container slot,
      IntSupplier energyPercent,
      Predicate<Player> stillValid) {
    super(CubitMenus.INDUCTION_DOCK.get(), containerId);
    this.stillValid = stillValid;
    addSlot(
        new Slot(slot, 0, SLOT_X, SLOT_Y) {
          @Override
          public boolean mayPlace(ItemStack stack) {
            return EnergySlotItems.accepts(stack);
          }
        });
    addDataSlot(
        new DataSlot() {
          @Override
          public int get() {
            return energyPercent.getAsInt();
          }

          @Override
          public void set(int value) {
            shownEnergyPercent = value;
          }
        });
    addPlayerSlots(playerInventory);
  }

  public static InductionDockMenu onServer(
      int containerId, Inventory playerInventory, InductionDockBlockEntity dock) {
    return new InductionDockMenu(
        containerId,
        playerInventory,
        dock.slot(),
        () -> dock.energyLevel(Percentage.MAX),
        player ->
            !dock.isRemoved()
                && dock.isOwner(player)
                && player.canInteractWithBlock(
                    dock.getBlockPos(), Container.DEFAULT_DISTANCE_BUFFER));
  }

  /** On the client. The real item and the energy come from the server. */
  public static InductionDockMenu onClient(
      int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
    return new InductionDockMenu(
        containerId, playerInventory, new SimpleContainer(DOCK_SLOTS), () -> 0, player -> true);
  }

  public int energyPercent() {
    return shownEnergyPercent;
  }

  private void addPlayerSlots(Inventory playerInventory) {
    for (int row = 0; row < PLAYER_ROWS; row++) {
      for (int column = 0; column < SLOTS_PER_ROW; column++) {
        addSlot(
            new Slot(
                playerInventory,
                column + row * SLOTS_PER_ROW + SLOTS_PER_ROW,
                FIRST_SLOT_X + column * SLOT_SIZE,
                PLAYER_INVENTORY_Y + row * SLOT_SIZE));
      }
    }
    for (int column = 0; column < SLOTS_PER_ROW; column++) {
      addSlot(
          new Slot(
              playerInventory,
              column,
              FIRST_SLOT_X + column * SLOT_SIZE,
              PLAYER_INVENTORY_Y + HOTBAR_GAP));
    }
  }

  @Override
  public ItemStack quickMoveStack(Player player, int index) {
    Slot slot = slots.get(index);
    if (!slot.hasItem()) {
      return ItemStack.EMPTY;
    }
    ItemStack stack = slot.getItem();
    ItemStack moved = stack.copy();
    boolean fits =
        index < DOCK_SLOTS
            ? moveItemStackTo(stack, DOCK_SLOTS, slots.size(), true)
            : moveItemStackTo(stack, 0, DOCK_SLOTS, false);
    if (!fits) {
      return ItemStack.EMPTY;
    }
    if (stack.isEmpty()) {
      slot.setByPlayer(ItemStack.EMPTY);
    } else {
      slot.setChanged();
    }
    return moved;
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValid.test(player);
  }
}
