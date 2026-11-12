package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.robot.PickupEntry;
import io.github.marcelofabianov.cubit.menu.PickupControl.PickupButton;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * The behavior tab inside the robot menu: its buttons, its values and the ghost slots of the pickup
 * filter. The slots only show copies, so nothing goes in or out of them (answers 880 and 895).
 */
final class PickupMenuPart {
  static final int FILTER_X = RobotMenu.FIRST_SLOT_X;
  static final int FILTER_Y = 70;
  private static final int LEFT_CLICK = 0;
  private static final int RIGHT_CLICK = 1;

  private final PickupControl control;
  private final PickupScreenValues values = new PickupScreenValues();
  private final SimpleContainer shown = new SimpleContainer(PickupScreenValues.MAX_FILTER_SLOTS);
  private int firstSlot = -1;
  private boolean visible;

  PickupMenuPart(PickupControl control) {
    this.control = control;
  }

  int firstSlot() {
    return firstSlot;
  }

  ContainerData values() {
    return values;
  }

  void show(boolean shownNow) {
    visible = shownNow;
  }

  /** Added after every other slot, so the ranges of the robot and the player do not move. */
  void addSlots(Consumer<Slot> add, int firstIndex) {
    firstSlot = firstIndex;
    for (int slot = 0; slot < PickupScreenValues.MAX_FILTER_SLOTS; slot++) {
      int x = FILTER_X + slot % RobotMenu.SLOTS_PER_ROW * RobotMenu.SLOT_SIZE;
      int y = FILTER_Y + slot / RobotMenu.SLOTS_PER_ROW * RobotMenu.SLOT_SIZE;
      add.accept(new GhostSlot(slot, x, y));
    }
  }

  /** On the server only, before the menu sends its changes. */
  void refresh() {
    values.refresh(control);
    List<PickupEntry> entries = control.settings().filter().entries();
    for (int slot = 0; slot < PickupScreenValues.MAX_FILTER_SLOTS; slot++) {
      ItemStack stack =
          slot < entries.size() ? PickupDisplay.stackOf(entries.get(slot)) : ItemStack.EMPTY;
      if (!ItemStack.matches(stack, shown.getItem(slot))) {
        shown.setItem(slot, stack);
      }
    }
  }

  boolean owns(int slotId) {
    return firstSlot >= 0
        && slotId >= firstSlot
        && slotId < firstSlot + PickupScreenValues.MAX_FILTER_SLOTS;
  }

  /** Only a plain click counts. A shift click or a drag never reaches the filter. */
  void click(int slotId, int button, ClickType type, Player player, ItemStack carried) {
    int slot = slotId - firstSlot;
    if (type != ClickType.PICKUP) {
      return;
    }
    if (button == LEFT_CLICK) {
      control.place(player, slot, carried);
    } else if (button == RIGHT_CLICK) {
      control.cycle(player, slot);
    }
  }

  boolean press(Player player, PickupButton button) {
    return control.press(player, button);
  }

  /** A copy of what the filter holds, shown only in the behavior tab and up to the tier. */
  private final class GhostSlot extends Slot {
    private final int index;

    GhostSlot(int index, int x, int y) {
      super(shown, index, x, y);
      this.index = index;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
      return false;
    }

    @Override
    public boolean mayPickup(Player player) {
      return false;
    }

    @Override
    public boolean isActive() {
      return visible && index < PickupScreenValues.filterSlots(values);
    }
  }
}
