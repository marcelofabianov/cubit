package io.github.zenndg.cubit.menu;

import io.github.zenndg.cubit.domain.robot.InventoryGrid;
import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.entity.RobotFertilizerSlot;
import io.github.zenndg.cubit.item.EnergySlotItems;
import io.github.zenndg.cubit.item.JobCardItem;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * The slots of the robot screen and when each group shows. Only the client hides them, by the open
 * tab, so the server keeps every slot usable for its own checks.
 */
final class RobotMenuSlots {
  private static final int PLAYER_ROWS = 3;

  private final boolean readOnly;
  private final RobotMenuLayout layout;
  private boolean robotShown = true;
  private boolean playerShown = true;

  RobotMenuSlots(RobotMenuLayout layout) {
    this.layout = layout;
    this.readOnly = layout.readOnly();
  }

  void show(boolean robot, boolean player) {
    robotShown = robot;
    playerShown = player;
  }

  /** The whole inventory, in the three rows and the columns of the tier (answer 1101). */
  void addRobotSlots(Consumer<Slot> add, Container inventory, InventoryGrid grid) {
    for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
      int x = RobotMenu.FIRST_SLOT_X + grid.columnOf(slot) * RobotMenu.SLOT_SIZE;
      int y = RobotMenu.FIRST_SLOT_Y + grid.rowOf(slot) * RobotMenu.SLOT_SIZE;
      add.accept(
          new Slot(inventory, slot, x, y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
              return !readOnly && stack.canFitInsideContainerItems();
            }

            @Override
            public boolean mayPickup(Player player) {
              return !readOnly;
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
        new Slot(energySlot, 0, layout.energySlotX(), layout.energySlotY()) {
          @Override
          public boolean mayPlace(ItemStack stack) {
            return !readOnly && EnergySlotItems.accepts(stack);
          }

          @Override
          public boolean mayPickup(Player player) {
            return !readOnly;
          }

          @Override
          public boolean isActive() {
            return robotShown;
          }
        });
  }

  /** Only plain torches, on a tier with torches, as the server tells (answers 973 and 983). */
  void addTorchSlot(Consumer<Slot> add, Container torchSlot, BooleanSupplier unlocked) {
    add.accept(
        new Slot(torchSlot, 0, layout.torchSlotX(), RobotMenu.EXCLUSIVE_SLOT_Y) {
          @Override
          public boolean mayPlace(ItemStack stack) {
            return !readOnly && unlocked.getAsBoolean() && stack.is(Items.TORCH);
          }

          @Override
          public boolean mayPickup(Player player) {
            return !readOnly;
          }

          @Override
          public boolean isActive() {
            return robotShown;
          }
        });
  }

  /**
   * Only fertilizer, on a tier that takes it, and only with the card of the Farmer, as the server
   * tells (answers 1243 and 1244).
   */
  void addFertilizerSlot(
      Consumer<Slot> add, Container fertilizerSlot, Container cardSlot, BooleanSupplier unlocked) {
    BooleanSupplier shown =
        () -> JobCardItem.jobOf(cardSlot.getItem(0)).filter(RobotJob.FARMER::equals).isPresent();
    add.accept(
        new Slot(fertilizerSlot, 0, layout.fertilizerSlotX(), RobotMenu.EXCLUSIVE_SLOT_Y) {
          @Override
          public boolean mayPlace(ItemStack stack) {
            return !readOnly && unlocked.getAsBoolean() && RobotFertilizerSlot.accepts(stack);
          }

          @Override
          public boolean mayPickup(Player player) {
            return !readOnly;
          }

          @Override
          public boolean isActive() {
            return robotShown && shown.getAsBoolean();
          }
        });
  }

  /** Only a card of a job the tier takes, as the server tells (answers 1162 and 1163). */
  void addCardSlot(Consumer<Slot> add, Container cardSlot, Predicate<RobotJob> accepted) {
    add.accept(
        new Slot(cardSlot, 0, layout.cardSlotX(), RobotMenu.EXCLUSIVE_SLOT_Y) {
          @Override
          public boolean mayPlace(ItemStack stack) {
            return !readOnly && JobCardItem.jobOf(stack).filter(accepted).isPresent();
          }

          @Override
          public boolean mayPickup(Player player) {
            return !readOnly;
          }

          @Override
          public int getMaxStackSize() {
            return 1;
          }

          @Override
          public boolean isActive() {
            return robotShown;
          }
        });
  }

  void addPlayerSlots(
      Consumer<Slot> add, Inventory playerInventory, int inventoryX, int inventoryY) {
    for (int row = 0; row < PLAYER_ROWS; row++) {
      for (int column = 0; column < RobotMenu.SLOTS_PER_ROW; column++) {
        int index = column + row * RobotMenu.SLOTS_PER_ROW + RobotMenu.SLOTS_PER_ROW;
        int x = inventoryX + column * RobotMenu.SLOT_SIZE;
        add.accept(
            new PlayerSlot(playerInventory, index, x, inventoryY + row * RobotMenu.SLOT_SIZE));
      }
    }
    for (int column = 0; column < RobotMenu.SLOTS_PER_ROW; column++) {
      int x = inventoryX + column * RobotMenu.SLOT_SIZE;
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
