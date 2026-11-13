package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.domain.robot.InventoryWindow;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.registry.CubitMenus;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/** The screen of a robot. The server decides who may keep it open and what it shows. */
public class RobotMenu extends RobotMenuReading {
  public static final int SLOTS_PER_ROW = 9;
  public static final int SLOT_SIZE = 18;
  public static final int FIRST_SLOT_X = 8;
  public static final int FIRST_SLOT_Y = 102;

  /** The row of the exclusive slots, above the inventory of the robot (answers 980 and 984). */
  public static final int EXCLUSIVE_SLOT_Y = 72;

  public static final int PLAYER_INVENTORY_GAP = 12;
  public static final int HOTBAR_GAP = 58;
  public static final int ENERGY_SLOT_X = 152;
  public static final int ENERGY_SLOT_Y = 34;
  public static final int LAMP_BUTTON = 0;
  public static final int SCANNERS_BUTTON = 1;
  public static final int CHATTER_BUTTON = 2;
  public static final int SOLAR_BUTTON = 3;

  /** The defense modes come after the switches, in the order of the modes. */
  public static final int FIRST_DEFENSE_BUTTON = 4;

  /** After the four defense modes (answer 857). */
  public static final int POWER_BUTTON = 8;

  /** The buttons of the behavior tab, after Power: pickup, magnet, the filter mode and fetch. */
  public static final int PICKUP_BUTTON = 9;

  public static final int MAGNET_BUTTON = 10;
  public static final int PICKUP_MODE_BUTTON = 11;
  public static final int FETCH_BUTTON = 12;

  /** The torch switch of the settings tab, after the behavior tab (answer 974). */
  public static final int TORCHES_BUTTON = 13;

  /** The top of the ghost slots of the pickup filter, in the behavior tab. */
  public static final int PICKUP_FILTER_Y = PickupMenuPart.FILTER_Y;

  /** The rows to scroll to come last, one button per row on top (answer 838). */
  public static final int FIRST_SCROLL_BUTTON = 16;

  /** The rows of the Base, so the screen keeps its height in every tier. */
  public static final int VISIBLE_ROWS = 3;

  private static final int ENERGY_SLOT_SIZE = 1;
  private static final int TORCH_SLOT_SIZE = 1;

  private final Container container;
  private final InventoryWindow window;
  private final ScrolledContainer view;
  private final Predicate<Player> stillValid;
  private final Runnable refresh;
  private final RobotSwitch[] switches;
  private final DefenseControl defense;
  private final PowerControl power;
  private final RobotScreenOpening opening;
  private final RobotMenuSlots slotGroups;
  private final PickupMenuPart pickup;

  /** The Tablet shows the inventory of the robot only to read it (answer 999). */
  protected RobotMenu(
      MenuType<?> type,
      int containerId,
      Inventory playerInventory,
      RobotMenuContents contents,
      RobotMenuServer server,
      boolean readOnly) {
    super(type, containerId, server.values());
    this.slotGroups = new RobotMenuSlots(readOnly);
    this.container = contents.inventory();
    this.window = new InventoryWindow(container.getContainerSize(), SLOTS_PER_ROW, VISIBLE_ROWS);
    this.view = new ScrolledContainer(container, window);
    this.stillValid = server.stillValid();
    this.refresh = server.refresh();
    this.switches = server.switches().clone();
    this.defense = server.defense();
    this.power = server.power();
    this.opening = contents.opening();
    this.pickup = new PickupMenuPart(server.pickup());
    slotGroups.addRobotSlots(this::addSlot, view);
    slotGroups.addEnergySlot(this::addSlot, contents.energySlot());
    slotGroups.addTorchSlot(
        this::addSlot,
        contents.torchSlot(),
        () -> RobotScreenValues.isAvailable(values, RobotScreenValues.TORCHES));
    addDataSlots(values);
    slotGroups.addPlayerSlots(this::addSlot, playerInventory, playerInventoryY());
    pickup.addSlots(this::addSlot, slots.size());
    addDataSlots(pickup.values());
  }

  /** On the client. The real items and numbers come from the server. */
  public static RobotMenu onClient(
      int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
    RobotScreenOpening opening = RobotScreenOpening.read(data);
    RobotMenuContents contents =
        new RobotMenuContents(
            new SimpleContainer(opening.slots()),
            new SimpleContainer(ENERGY_SLOT_SIZE),
            new SimpleContainer(TORCH_SLOT_SIZE),
            opening);
    return new RobotMenu(
        CubitMenus.ROBOT.get(),
        containerId,
        playerInventory,
        contents,
        RobotMenuServer.onClient(),
        false);
  }

  /** On the server, where the values come from the robot itself. */
  public static RobotMenu onServer(
      int containerId,
      Inventory playerInventory,
      RobotMenuContents contents,
      RobotMenuSource source) {
    return new RobotMenu(
        CubitMenus.ROBOT.get(), containerId, playerInventory, contents, serverOf(source), false);
  }

  static RobotMenuServer serverOf(RobotMenuSource source) {
    RobotScreenValues values =
        new RobotScreenValues(
            source.reading(),
            source.defense(),
            source.power(),
            source.torches(),
            source.switches());
    values.refresh();
    return new RobotMenuServer(
        source.stillValid(),
        values,
        values::refresh,
        source.switches(),
        source.defense(),
        source.power(),
        source.pickup());
  }

  @Override
  public void broadcastChanges() {
    refresh.run();
    pickup.refresh();
    super.broadcastChanges();
  }

  /** The server checks this menu before calling, and it is checked again for any other caller. */
  @Override
  public boolean clickMenuButton(Player player, int button) {
    if (button >= FIRST_SCROLL_BUTTON) {
      return scrollFromClient(player, button - FIRST_SCROLL_BUTTON);
    }
    if (button >= PICKUP_BUTTON && button <= FETCH_BUTTON) {
      return stillValid(player)
          && pickup.press(player, PickupControl.PickupButton.values()[button - PICKUP_BUTTON]);
    }
    if (button == POWER_BUTTON) {
      return stillValid(player) && power.switchOff(player);
    }
    int mode = button - FIRST_DEFENSE_BUTTON;
    if (mode >= 0 && mode < DefenseMode.values().length) {
      return stillValid(player) && defense.choose(player, DefenseMode.values()[mode]);
    }
    return toggleFromClient(player, button);
  }

  private boolean toggleFromClient(Player player, int button) {
    int index = switchIndex(button);
    if (index < 0 || index >= switches.length || !stillValid(player)) {
      return false;
    }
    RobotSwitch pressed = switches[index];
    if (!pressed.isAvailable()) {
      return false;
    }
    pressed.toggle();
    return true;
  }

  private boolean scrollFromClient(Player player, int row) {
    if (row > window.maxTopRow() || !stillValid(player)) {
      return false;
    }
    view.setTopRow(row);
    return true;
  }

  /** On the client, at once, so the screen does not wait for the server to show the rows. */
  public void scrollTo(int row) {
    view.setTopRow(row);
  }

  public int topRow() {
    return view.topRow();
  }

  public InventoryWindow window() {
    return window;
  }

  /** Only the client hides the slots. The behavior tab keeps the player's, to copy items. */
  public void showTab(boolean inventory, boolean behavior) {
    showParts(inventory, inventory || behavior, behavior);
  }

  protected final void showParts(boolean robot, boolean player, boolean filter) {
    slotGroups.show(robot, player);
    pickup.show(filter);
  }

  public ContainerData pickupValues() {
    return pickup.values();
  }

  public int filterFirstSlot() {
    return pickup.firstSlot();
  }

  /** A click on a ghost slot of the filter never moves an item (answer 880). */
  @Override
  public void clicked(int slotId, int button, ClickType type, Player player) {
    if (!pickup.owns(slotId)) {
      super.clicked(slotId, button, type, player);
    } else if (stillValid(player)) {
      pickup.click(slotId, button, type, player, getCarried());
    }
  }

  @Override
  public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
    return !pickup.owns(slot.index) && super.canTakeItemForPickAll(stack, slot);
  }

  protected final PowerControl power() {
    return power;
  }

  /** The slots of the robot come first: its inventory, the energy slot and the torch slot. */
  protected final int robotSlots() {
    return view.getContainerSize() + ENERGY_SLOT_SIZE + TORCH_SLOT_SIZE;
  }

  public RobotScreenOpening opening() {
    return opening;
  }

  /** The torch slot comes after the visible rows of the robot and the energy slot. */
  public int torchSlotIndex() {
    return view.getContainerSize() + ENERGY_SLOT_SIZE;
  }

  public Energy torchCost() {
    return RobotScreenValues.energy(values, RobotScreenValues.TORCH_COST);
  }

  public Optional<RobotTier> torchUnlockTier() {
    int value = values.get(RobotScreenValues.TORCH_UNLOCK_TIER);
    return value == 0
        ? Optional.empty()
        : Optional.of(ScreenEnums.of(RobotTier.values(), value - 1));
  }

  public int rows() {
    return window.visibleRows();
  }

  public int playerInventoryY() {
    return FIRST_SLOT_Y + rows() * SLOT_SIZE + PLAYER_INVENTORY_GAP;
  }

  @Override
  public ItemStack quickMoveStack(Player player, int index) {
    Slot slot = slots.get(index);
    if (!slot.hasItem()) {
      return ItemStack.EMPTY;
    }
    ItemStack stack = slot.getItem();
    ItemStack moved = stack.copy();
    int inventorySlots = view.getContainerSize();
    int robotSlots = robotSlots();
    boolean fits =
        index < robotSlots
            ? moveItemStackTo(
                stack, robotSlots, slots.size() - PickupScreenValues.MAX_FILTER_SLOTS, true)
            : moveItemStackTo(stack, inventorySlots, robotSlots, false)
                || RobotStorage.insert(container, stack);
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
