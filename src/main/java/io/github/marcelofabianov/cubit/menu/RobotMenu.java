package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.behavior.RobotScreenStatus;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.EnergyUseCause;
import io.github.marcelofabianov.cubit.domain.energy.RechargeSource;
import io.github.marcelofabianov.cubit.domain.energy.SolarStatus;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.domain.robot.InventoryWindow;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.registry.CubitMenus;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/** The screen of a robot. The server decides who may keep it open and what it shows. */
public final class RobotMenu extends AbstractContainerMenu {
  public static final int SLOTS_PER_ROW = 9;
  public static final int SLOT_SIZE = 18;
  public static final int FIRST_SLOT_X = 8;
  public static final int FIRST_SLOT_Y = 72;
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

  /** The buttons of the behavior tab, after Power: pickup, magnet and the filter mode. */
  public static final int PICKUP_BUTTON = 9;

  public static final int MAGNET_BUTTON = 10;
  public static final int PICKUP_MODE_BUTTON = 11;

  /** The top of the ghost slots of the pickup filter, in the behavior tab. */
  public static final int PICKUP_FILTER_Y = PickupMenuPart.FILTER_Y;

  /** The rows to scroll to come last, one button per row on top (answer 838). */
  public static final int FIRST_SCROLL_BUTTON = 16;

  /** The rows of the Base, so the screen keeps its height in every tier. */
  public static final int VISIBLE_ROWS = 3;

  private static final int ENERGY_SLOT_SIZE = 1;
  private static final int[] SWITCH_INDEXES = {
    RobotScreenValues.LAMP,
    RobotScreenValues.SCANNERS,
    RobotScreenValues.CHATTER,
    RobotScreenValues.SOLAR_SWITCH
  };

  private final Container container;
  private final InventoryWindow window;
  private final ScrolledContainer view;
  private final Predicate<Player> stillValid;
  private final ContainerData values;
  private final Runnable refresh;
  private final RobotSwitch[] switches;
  private final DefenseControl defense;
  private final PowerControl power;
  private final RobotScreenOpening opening;
  private final RobotMenuSlots slotGroups = new RobotMenuSlots();
  private final PickupMenuPart pickup;

  private RobotMenu(int containerId, Inventory playerInventory, Contents contents, Server server) {
    super(CubitMenus.ROBOT.get(), containerId);
    this.container = contents.inventory();
    this.window = new InventoryWindow(container.getContainerSize(), SLOTS_PER_ROW, VISIBLE_ROWS);
    this.view = new ScrolledContainer(container, window);
    this.stillValid = server.stillValid();
    this.values = server.values();
    this.refresh = server.refresh();
    this.switches = server.switches().clone();
    this.defense = server.defense();
    this.power = server.power();
    this.opening = contents.opening();
    this.pickup = new PickupMenuPart(server.pickup());
    slotGroups.addRobotSlots(this::addSlot, view);
    slotGroups.addEnergySlot(this::addSlot, contents.energySlot());
    addDataSlots(values);
    slotGroups.addPlayerSlots(this::addSlot, playerInventory, playerInventoryY());
    pickup.addSlots(this::addSlot, slots.size());
    addDataSlots(pickup.values());
  }

  /** On the client. The real items and numbers come from the server. */
  public static RobotMenu onClient(
      int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
    RobotScreenOpening opening = RobotScreenOpening.read(data);
    Contents contents =
        new Contents(
            new SimpleContainer(opening.slots()), new SimpleContainer(ENERGY_SLOT_SIZE), opening);
    return new RobotMenu(containerId, playerInventory, contents, Server.onClient());
  }

  /** On the server, where the values come from the robot itself. */
  public static RobotMenu onServer(
      int containerId, Inventory playerInventory, Contents contents, RobotSource source) {
    RobotScreenValues values =
        new RobotScreenValues(
            source.reading(), source.defense(), source.power(), source.switches());
    values.refresh();
    Server server =
        new Server(
            source.stillValid(),
            values,
            values::refresh,
            source.switches(),
            source.defense(),
            source.power(),
            source.pickup());
    return new RobotMenu(containerId, playerInventory, contents, server);
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
    if (button >= PICKUP_BUTTON && button <= PICKUP_MODE_BUTTON) {
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
    if (button < 0 || button >= switches.length || !stillValid(player)) {
      return false;
    }
    RobotSwitch pressed = switches[button];
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
    slotGroups.show(inventory, inventory || behavior);
    pickup.show(behavior);
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

  public RobotScreenOpening opening() {
    return opening;
  }

  public Energy charge() {
    return RobotScreenValues.energy(values, RobotScreenValues.CHARGE);
  }

  public Energy capacity() {
    return RobotScreenValues.energy(values, RobotScreenValues.CAPACITY);
  }

  public int health() {
    return values.get(RobotScreenValues.HEALTH);
  }

  public int maxHealth() {
    return values.get(RobotScreenValues.MAX_HEALTH);
  }

  public RobotScreenStatus status() {
    return ScreenEnums.of(RobotScreenStatus.values(), values.get(RobotScreenValues.STATUS));
  }

  public RechargeSource recharge() {
    return ScreenEnums.of(RechargeSource.values(), values.get(RobotScreenValues.RECHARGE));
  }

  public Energy rechargeRate() {
    return RobotScreenValues.energy(values, RobotScreenValues.RECHARGE_RATE);
  }

  public SolarStatus solar() {
    return ScreenEnums.of(SolarStatus.values(), values.get(RobotScreenValues.SOLAR));
  }

  public Energy solarRate() {
    return RobotScreenValues.energy(values, RobotScreenValues.SOLAR_RATE);
  }

  public Energy use() {
    return RobotScreenValues.energy(values, RobotScreenValues.USE);
  }

  public List<EnergyUseCause> causes() {
    return RobotScreenValues.causes(values);
  }

  public boolean following() {
    return RobotScreenValues.isTrue(values, RobotScreenValues.FOLLOWING);
  }

  public RobotExpression expression() {
    return ScreenEnums.of(RobotExpression.values(), values.get(RobotScreenValues.EXPRESSION));
  }

  public boolean powerAvailable() {
    return RobotScreenValues.isTrue(values, RobotScreenValues.POWER);
  }

  public DefenseMode defenseMode() {
    return ScreenEnums.of(DefenseMode.values(), values.get(RobotScreenValues.DEFENSE));
  }

  public RobotTier tier() {
    return ScreenEnums.of(RobotTier.values(), values.get(RobotScreenValues.TIER));
  }

  /** By the button of the switch: {@link #LAMP_BUTTON}, {@link #SCANNERS_BUTTON} or chatter. */
  public boolean switchOn(int button) {
    return RobotScreenValues.isTrue(values, SWITCH_INDEXES[button]);
  }

  public boolean switchAvailable(int button) {
    return RobotScreenValues.isAvailable(values, SWITCH_INDEXES[button]);
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
    int robotSlots = inventorySlots + ENERGY_SLOT_SIZE;
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

  /** The containers of the robot and what travels when the screen opens. */
  public record Contents(Container inventory, Container energySlot, RobotScreenOpening opening) {}

  /** What the server reads from the robot: the screen values, the switches and who may use it. */
  public record RobotSource(
      Supplier<RobotScreenReading> reading,
      RobotSwitch[] switches,
      DefenseControl defense,
      PowerControl power,
      PickupControl pickup,
      Predicate<Player> stillValid) {}

  private record Server(
      Predicate<Player> stillValid,
      ContainerData values,
      Runnable refresh,
      RobotSwitch[] switches,
      DefenseControl defense,
      PowerControl power,
      PickupControl pickup) {
    private static final RobotSwitch[] NO_SWITCHES = {
      RobotSwitch.NONE, RobotSwitch.NONE, RobotSwitch.NONE, RobotSwitch.NONE
    };

    static Server onClient() {
      return new Server(
          player -> true,
          new SimpleContainerData(RobotScreenValues.COUNT),
          () -> {},
          NO_SWITCHES,
          DefenseControl.NONE,
          PowerControl.NONE,
          PickupControl.NONE);
    }
  }
}
