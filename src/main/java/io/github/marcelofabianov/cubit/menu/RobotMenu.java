package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.item.EnergySlotItems;
import io.github.marcelofabianov.cubit.registry.CubitMenus;
import java.util.function.IntConsumer;
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
  public static final int LAMP_BUTTON = 0;
  public static final String LAMP_ON_KEY = "gui.cubit.lamp.on";
  public static final String LAMP_OFF_KEY = "gui.cubit.lamp.off";
  public static final int SCANNERS_BUTTON = 1;
  public static final String SCANNERS_ON_KEY = "gui.cubit.scanners.on";
  public static final String SCANNERS_OFF_KEY = "gui.cubit.scanners.off";
  private static final int TRUE_VALUE = 1;
  private static final int FALSE_VALUE = 0;

  private final Container container;
  private final Predicate<Player> stillValid;
  private int shownBatteryPercent;
  private int shownHealthPercent;
  private int shownLampOn;
  private int shownLampAvailable;
  private int shownScannersOn;
  private int shownScannersAvailable;
  private final RobotSwitch lamp;
  private final RobotSwitch scanners;

  public RobotMenu(
      int containerId,
      Inventory playerInventory,
      Container container,
      Container energySlot,
      IntSupplier batteryPercent,
      IntSupplier healthPercent,
      RobotSwitch lamp,
      RobotSwitch scanners,
      Predicate<Player> stillValid) {
    super(CubitMenus.ROBOT.get(), containerId);
    this.container = container;
    this.stillValid = stillValid;
    this.lamp = lamp;
    this.scanners = scanners;
    int rows = rowsFor(container.getContainerSize());
    addRobotSlots();
    addEnergySlot(energySlot);
    addDataSlot(shownValue(batteryPercent, value -> shownBatteryPercent = value));
    addDataSlot(shownValue(healthPercent, value -> shownHealthPercent = value));
    addDataSlot(shownValue(() -> asValue(lamp.isOn()), value -> shownLampOn = value));
    addDataSlot(shownValue(() -> asValue(lamp.isAvailable()), value -> shownLampAvailable = value));
    addDataSlot(shownValue(() -> asValue(scanners.isOn()), value -> shownScannersOn = value));
    addDataSlot(
        shownValue(() -> asValue(scanners.isAvailable()), value -> shownScannersAvailable = value));
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
        () -> 0,
        RobotSwitch.NONE,
        RobotSwitch.NONE,
        player -> true);
  }

  /** Only the size travels to the client, because the server sends the items. */
  public static void open(
      Player player,
      Component title,
      Container container,
      Container energySlot,
      IntSupplier batteryPercent,
      IntSupplier healthPercent,
      RobotSwitch lamp,
      RobotSwitch scanners,
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
                    healthPercent,
                    lamp,
                    scanners,
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

  public int healthPercent() {
    return shownHealthPercent;
  }

  public boolean lampOn() {
    return shownLampOn == TRUE_VALUE;
  }

  public boolean lampAvailable() {
    return shownLampAvailable == TRUE_VALUE;
  }

  public boolean scannersOn() {
    return shownScannersOn == TRUE_VALUE;
  }

  public boolean scannersAvailable() {
    return shownScannersAvailable == TRUE_VALUE;
  }

  /** The server checks this menu before calling, and it is checked again for any other caller. */
  @Override
  public boolean clickMenuButton(Player player, int button) {
    RobotSwitch pressed = switchOf(button);
    if (pressed == null || !stillValid(player) || !pressed.isAvailable()) {
      return false;
    }
    pressed.toggle();
    return true;
  }

  private RobotSwitch switchOf(int button) {
    return switch (button) {
      case LAMP_BUTTON -> lamp;
      case SCANNERS_BUTTON -> scanners;
      default -> null;
    };
  }

  private static int asValue(boolean value) {
    return value ? TRUE_VALUE : FALSE_VALUE;
  }

  private static DataSlot shownValue(IntSupplier source, IntConsumer shown) {
    return new DataSlot() {
      @Override
      public int get() {
        return source.getAsInt();
      }

      @Override
      public void set(int value) {
        shown.accept(value);
      }
    };
  }

  public int rows() {
    return rowsFor(container.getContainerSize());
  }

  private static int rowsFor(int slots) {
    return (slots + SLOTS_PER_ROW - 1) / SLOTS_PER_ROW;
  }

  private void addRobotSlots() {
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
