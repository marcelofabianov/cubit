package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.behavior.RobotScreenStatus;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.EnergyUseCause;
import io.github.marcelofabianov.cubit.domain.energy.RechargeSource;
import io.github.marcelofabianov.cubit.domain.energy.SolarStatus;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.item.EnergySlotItems;
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

  /** The defense modes come after the switches, in the order of the modes. */
  public static final int FIRST_DEFENSE_BUTTON = 3;

  private static final int PLAYER_ROWS = 3;
  private static final int ENERGY_SLOT_SIZE = 1;
  private static final int[] SWITCH_INDEXES = {
    RobotScreenValues.LAMP, RobotScreenValues.SCANNERS, RobotScreenValues.CHATTER
  };

  private final Container container;
  private final Predicate<Player> stillValid;
  private final ContainerData values;
  private final Runnable refresh;
  private final RobotSwitch[] switches;
  private final DefenseControl defense;
  private final RobotScreenOpening opening;
  private boolean inventoryShown = true;

  private RobotMenu(int containerId, Inventory playerInventory, Contents contents, Server server) {
    super(CubitMenus.ROBOT.get(), containerId);
    this.container = contents.inventory();
    this.stillValid = server.stillValid();
    this.values = server.values();
    this.refresh = server.refresh();
    this.switches = server.switches().clone();
    this.defense = server.defense();
    this.opening = contents.opening();
    addRobotSlots();
    addEnergySlot(contents.energySlot());
    addDataSlots(values);
    addPlayerSlots(playerInventory);
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
        new RobotScreenValues(source.reading(), source.defense(), source.switches());
    values.refresh();
    Server server =
        new Server(
            source.stillValid(), values, values::refresh, source.switches(), source.defense());
    return new RobotMenu(containerId, playerInventory, contents, server);
  }

  @Override
  public void broadcastChanges() {
    refresh.run();
    super.broadcastChanges();
  }

  /** The server checks this menu before calling, and it is checked again for any other caller. */
  @Override
  public boolean clickMenuButton(Player player, int button) {
    int mode = button - FIRST_DEFENSE_BUTTON;
    if (mode >= 0 && mode < DefenseMode.values().length) {
      return stillValid(player) && defense.choose(player, DefenseMode.values()[mode]);
    }
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

  /** Only the client hides the slots, while its settings tab is open. */
  public void showInventory(boolean shown) {
    inventoryShown = shown;
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
    return rowsFor(container.getContainerSize());
  }

  public int playerInventoryY() {
    return FIRST_SLOT_Y + rows() * SLOT_SIZE + PLAYER_INVENTORY_GAP;
  }

  private static int rowsFor(int slots) {
    return (slots + SLOTS_PER_ROW - 1) / SLOTS_PER_ROW;
  }

  private void addEnergySlot(Container energySlot) {
    addSlot(
        new Slot(energySlot, 0, ENERGY_SLOT_X, ENERGY_SLOT_Y) {
          @Override
          public boolean mayPlace(ItemStack stack) {
            return EnergySlotItems.accepts(stack);
          }

          @Override
          public boolean isActive() {
            return inventoryShown;
          }
        });
  }

  private void addRobotSlots() {
    for (int slot = 0; slot < container.getContainerSize(); slot++) {
      int x = FIRST_SLOT_X + slot % SLOTS_PER_ROW * SLOT_SIZE;
      int y = FIRST_SLOT_Y + slot / SLOTS_PER_ROW * SLOT_SIZE;
      addSlot(
          new Slot(container, slot, x, y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
              return stack.getItem().canFitInsideContainerItems();
            }

            @Override
            public boolean isActive() {
              return inventoryShown;
            }
          });
    }
  }

  private void addPlayerSlots(Inventory playerInventory) {
    int inventoryY = playerInventoryY();
    for (int row = 0; row < PLAYER_ROWS; row++) {
      for (int column = 0; column < SLOTS_PER_ROW; column++) {
        int index = column + row * SLOTS_PER_ROW + SLOTS_PER_ROW;
        int y = inventoryY + row * SLOT_SIZE;
        addSlot(new PlayerSlot(playerInventory, index, FIRST_SLOT_X + column * SLOT_SIZE, y));
      }
    }
    for (int column = 0; column < SLOTS_PER_ROW; column++) {
      int x = FIRST_SLOT_X + column * SLOT_SIZE;
      addSlot(new PlayerSlot(playerInventory, column, x, inventoryY + HOTBAR_GAP));
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

  /** A slot of the player that hides with the robot slots, while the settings tab is open. */
  private final class PlayerSlot extends Slot {
    PlayerSlot(Inventory inventory, int index, int x, int y) {
      super(inventory, index, x, y);
    }

    @Override
    public boolean isActive() {
      return inventoryShown;
    }
  }

  /** The containers of the robot and what travels when the screen opens. */
  public record Contents(Container inventory, Container energySlot, RobotScreenOpening opening) {}

  /** What the server reads from the robot: the screen values, the switches and who may use it. */
  public record RobotSource(
      Supplier<RobotScreenReading> reading,
      RobotSwitch[] switches,
      DefenseControl defense,
      Predicate<Player> stillValid) {}

  private record Server(
      Predicate<Player> stillValid,
      ContainerData values,
      Runnable refresh,
      RobotSwitch[] switches,
      DefenseControl defense) {
    private static final RobotSwitch[] NO_SWITCHES = {
      RobotSwitch.NONE, RobotSwitch.NONE, RobotSwitch.NONE
    };

    static Server onClient() {
      return new Server(
          player -> true,
          new SimpleContainerData(RobotScreenValues.COUNT),
          () -> {},
          NO_SWITCHES,
          DefenseControl.NONE);
    }
  }
}
