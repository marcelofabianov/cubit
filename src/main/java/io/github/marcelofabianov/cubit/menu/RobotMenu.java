package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.item.EnergySlotItems;
import io.github.marcelofabianov.cubit.registry.CubitMenus;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/** The inventory screen of a robot. The server decides who may keep it open. */
public final class RobotMenu extends AbstractContainerMenu {
  public static final int SLOTS_PER_ROW = 9;
  private static final int SLOT_SIZE = 18;
  private static final int FIRST_SLOT_X = 8;
  private static final int FIRST_SLOT_Y = 18;
  private static final int PLAYER_INVENTORY_GAP = 31;
  private static final int HOTBAR_GAP = 58;
  private static final int PLAYER_ROWS = 3;
  private static final int ENERGY_SLOT_SIZE = 1;
  public static final int ENERGY_SLOT_X = -19;

  private final Container container;
  private final Predicate<Player> stillValid;
  private int shownBatteryPercent;

  public RobotMenu(
      int containerId,
      Inventory playerInventory,
      Container container,
      Container energySlot,
      IntSupplier batteryPercent,
      Predicate<Player> stillValid) {
    super(CubitMenus.ROBOT.get(), containerId);
    this.container = container;
    this.stillValid = stillValid;
    int rows = rowsFor(container.getContainerSize());
    addRobotSlots(rows);
    addEnergySlot(energySlot);
    addDataSlot(
        new DataSlot() {
          @Override
          public int get() {
            return batteryPercent.getAsInt();
          }

          @Override
          public void set(int value) {
            shownBatteryPercent = value;
          }
        });
    addPlayerSlots(playerInventory, rows);
  }

  /** On the client. The real items come from the server, and only the size travels here. */
  public static RobotMenu onClient(
      int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
    return new RobotMenu(
        containerId,
        playerInventory,
        new SimpleContainer(data.readVarInt()),
        new SimpleContainer(ENERGY_SLOT_SIZE),
        () -> 0,
        player -> true);
  }

  /** Only the size travels to the client, because the server sends the items. */
  public static void open(
      Player player,
      Component title,
      Container container,
      Container energySlot,
      IntSupplier batteryPercent,
      Predicate<Player> stillValid) {
    player.openMenu(
        new SimpleMenuProvider(
            (containerId, playerInventory, opener) ->
                new RobotMenu(
                    containerId,
                    playerInventory,
                    container,
                    energySlot,
                    batteryPercent,
                    stillValid),
            title),
        data -> writeSize(data, container));
  }

  private static void writeSize(FriendlyByteBuf data, Container container) {
    data.writeVarInt(container.getContainerSize());
  }

  private void addEnergySlot(Container energySlot) {
    addSlot(
        new Slot(energySlot, 0, ENERGY_SLOT_X, FIRST_SLOT_Y) {
          @Override
          public boolean mayPlace(ItemStack stack) {
            return EnergySlotItems.accepts(stack);
          }
        });
  }

  public int batteryPercent() {
    return shownBatteryPercent;
  }

  public int rows() {
    return rowsFor(container.getContainerSize());
  }

  private static int rowsFor(int slots) {
    return (slots + SLOTS_PER_ROW - 1) / SLOTS_PER_ROW;
  }

  private void addRobotSlots(int rows) {
    for (int slot = 0; slot < container.getContainerSize(); slot++) {
      addSlot(
          new Slot(
              container,
              slot,
              FIRST_SLOT_X + slot % SLOTS_PER_ROW * SLOT_SIZE,
              FIRST_SLOT_Y + slot / SLOTS_PER_ROW * SLOT_SIZE) {
            @Override
            public boolean mayPlace(ItemStack stack) {
              return stack.getItem().canFitInsideContainerItems();
            }
          });
    }
  }

  private void addPlayerSlots(Inventory playerInventory, int rows) {
    int inventoryY = rows * SLOT_SIZE + PLAYER_INVENTORY_GAP;
    for (int row = 0; row < PLAYER_ROWS; row++) {
      for (int column = 0; column < SLOTS_PER_ROW; column++) {
        addSlot(
            new Slot(
                playerInventory,
                column + row * SLOTS_PER_ROW + SLOTS_PER_ROW,
                FIRST_SLOT_X + column * SLOT_SIZE,
                inventoryY + row * SLOT_SIZE));
      }
    }
    for (int column = 0; column < SLOTS_PER_ROW; column++) {
      addSlot(
          new Slot(
              playerInventory, column, FIRST_SLOT_X + column * SLOT_SIZE, inventoryY + HOTBAR_GAP));
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
    int inventorySlots = container.getContainerSize();
    int robotSlots = inventorySlots + ENERGY_SLOT_SIZE;
    boolean fits =
        index < robotSlots
            ? moveItemStackTo(stack, robotSlots, slots.size(), true)
            : moveItemStackTo(stack, inventorySlots, robotSlots, false)
                || moveItemStackTo(stack, 0, inventorySlots, false);
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
