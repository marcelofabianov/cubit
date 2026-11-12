package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.item.EnergySlotItems;
import java.util.function.Consumer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * The slots of the robot screen and when each group shows. Only the client hides them, by the open
 * tab, so the server keeps every slot usable for its own checks.
 */
final class RobotMenuSlots {
  private static final int PLAYER_ROWS = 3;

  private boolean robotShown = true;
  private boolean playerShown = true;

  void show(boolean robot, boolean player) {
    robotShown = robot;
    playerShown = player;
  }

  void addRobotSlots(Consumer<Slot> add, Container view) {
    for (int slot = 0; slot < view.getContainerSize(); slot++) {
      int x = RobotMenu.FIRST_SLOT_X + slot % RobotMenu.SLOTS_PER_ROW * RobotMenu.SLOT_SIZE;
      int y = RobotMenu.FIRST_SLOT_Y + slot / RobotMenu.SLOTS_PER_ROW * RobotMenu.SLOT_SIZE;
      add.accept(
          new Slot(view, slot, x, y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
              return stack.getItem().canFitInsideContainerItems();
            }

            @Override
            public boolean isActive() {
              return robotShown;
            }
          });
    }
  }

  void addEnergySlot(Consumer<Slot> add, Container energySlot) {
    add.accept(
        new Slot(energySlot, 0, RobotMenu.ENERGY_SLOT_X, RobotMenu.ENERGY_SLOT_Y) {
          @Override
          public boolean mayPlace(ItemStack stack) {
            return EnergySlotItems.accepts(stack);
          }

          @Override
          public boolean isActive() {
            return robotShown;
          }
        });
  }

  void addPlayerSlots(Consumer<Slot> add, Inventory playerInventory, int inventoryY) {
    for (int row = 0; row < PLAYER_ROWS; row++) {
      for (int column = 0; column < RobotMenu.SLOTS_PER_ROW; column++) {
        int index = column + row * RobotMenu.SLOTS_PER_ROW + RobotMenu.SLOTS_PER_ROW;
        int x = RobotMenu.FIRST_SLOT_X + column * RobotMenu.SLOT_SIZE;
        add.accept(
            new PlayerSlot(playerInventory, index, x, inventoryY + row * RobotMenu.SLOT_SIZE));
      }
    }
    for (int column = 0; column < RobotMenu.SLOTS_PER_ROW; column++) {
      int x = RobotMenu.FIRST_SLOT_X + column * RobotMenu.SLOT_SIZE;
      add.accept(new PlayerSlot(playerInventory, column, x, inventoryY + RobotMenu.HOTBAR_GAP));
    }
  }

  /** A slot of the player, hidden with its group by the open tab. */
  private final class PlayerSlot extends Slot {
    PlayerSlot(Inventory inventory, int index, int x, int y) {
      super(inventory, index, x, y);
    }

    @Override
    public boolean isActive() {
      return playerShown;
    }
  }
}
