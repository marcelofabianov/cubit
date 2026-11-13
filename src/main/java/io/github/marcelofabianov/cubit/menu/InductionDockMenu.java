package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.dock.DockStatus;
import io.github.marcelofabianov.cubit.domain.dock.DockStorageRules;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.HealTime;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.item.EnergySlotItems;
import io.github.marcelofabianov.cubit.registry.CubitMenus;
import java.util.Optional;
import java.util.function.Predicate;
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

/**
 * The screen of a dock, with the Dock and Storage tabs (answers 956 and 960). The server decides
 * who may keep it open and what it shows. The open tab is only a choice of the client.
 */
public final class InductionDockMenu extends AbstractContainerMenu {
  public static final int SLOT_X = 152;
  public static final int SLOT_Y = 33;
  public static final int PLAYER_INVENTORY_Y = 179;

  /** The top of the storage grid, under the title of the Storage tab. */
  public static final int STORAGE_Y = 18;

  public static final int CALL_BUTTON = 0;
  private static final int SLOT_SIZE = 18;
  private static final int FIRST_SLOT_X = 8;
  private static final int HOTBAR_GAP = 58;
  private static final int PLAYER_ROWS = 3;
  private static final int SLOTS_PER_ROW = 9;
  private static final int STORAGE_FIRST = 1;
  private static final int DOCK_SLOTS = STORAGE_FIRST + DockStorageRules.SLOTS;
  private static final String NO_NAME = "";

  private final Predicate<Player> stillValid;
  private final ContainerData values;
  private final Runnable call;
  private final Runnable refresh;
  private final Opening opening;
  private boolean storageShown;

  private InductionDockMenu(
      int containerId,
      Inventory playerInventory,
      Container slot,
      Container storage,
      ContainerData values,
      Server server,
      Opening opening) {
    super(CubitMenus.INDUCTION_DOCK.get(), containerId);
    this.stillValid = server.stillValid();
    this.call = server.call();
    this.refresh = server.refresh();
    this.values = values;
    this.opening = opening;
    addSlot(
        new Slot(slot, 0, SLOT_X, SLOT_Y) {
          @Override
          public boolean mayPlace(ItemStack stack) {
            return EnergySlotItems.accepts(stack);
          }

          @Override
          public boolean isActive() {
            return !storageShown;
          }
        });
    addStorageSlots(storage);
    addDataSlots(values);
    addPlayerSlots(playerInventory);
  }

  public static InductionDockMenu onServer(
      int containerId, Inventory playerInventory, InductionDockBlockEntity dock) {
    DockScreenValues values = new DockScreenValues(dock);
    values.refresh();
    Server server =
        new Server(
            player ->
                !dock.isRemoved()
                    && dock.isOwner(player)
                    && player.canInteractWithBlock(
                        dock.getBlockPos(), Container.DEFAULT_DISTANCE_BUFFER),
            dock::callNearestRobot,
            values::refresh);
    return new InductionDockMenu(
        containerId, playerInventory, dock.slot(), dock.storage(), values, server, Opening.NONE);
  }

  /** On the client. The real item and the numbers come from the server. */
  public static InductionDockMenu onClient(
      int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
    return new InductionDockMenu(
        containerId,
        playerInventory,
        new SimpleContainer(1),
        new SimpleContainer(DockStorageRules.SLOTS),
        new SimpleContainerData(DockScreenValues.COUNT),
        Server.NONE,
        Opening.read(data));
  }

  /** What travels once, when the screen opens, because it does not change while it is open. */
  public static void writeOpening(RegistryFriendlyByteBuf data, InductionDockBlockEntity dock) {
    Opening.of(dock).write(data);
  }

  @Override
  public void broadcastChanges() {
    refresh.run();
    super.broadcastChanges();
  }

  /** The server checks this menu before calling, and it is checked again for any other caller. */
  @Override
  public boolean clickMenuButton(Player player, int button) {
    if (button != CALL_BUTTON || !stillValid(player)) {
      return false;
    }
    call.run();
    return true;
  }

  public Energy stock() {
    return DockScreenValues.energy(values, DockScreenValues.STOCK);
  }

  public Energy capacity() {
    return DockScreenValues.energy(values, DockScreenValues.CAPACITY);
  }

  public Energy input() {
    return DockScreenValues.energy(values, DockScreenValues.INPUT);
  }

  public DockStatus status() {
    return ScreenEnums.of(DockStatus.values(), values.get(DockScreenValues.STATUS));
  }

  public int waiting() {
    return values.get(DockScreenValues.WAITING);
  }

  public boolean hasRobot() {
    return DockScreenValues.isTrue(values, DockScreenValues.ROBOT_PRESENT);
  }

  public Energy robotCharge() {
    return DockScreenValues.energy(values, DockScreenValues.ROBOT_CHARGE);
  }

  public Energy robotCapacity() {
    return DockScreenValues.energy(values, DockScreenValues.ROBOT_CAPACITY);
  }

  public int robotHealth() {
    return values.get(DockScreenValues.ROBOT_HEALTH);
  }

  public int robotMaxHealth() {
    return values.get(DockScreenValues.ROBOT_MAX_HEALTH);
  }

  public Energy robotRecharge() {
    return DockScreenValues.energy(values, DockScreenValues.ROBOT_RECHARGE);
  }

  public HealTime healTime() {
    HealTime.Kind kind =
        ScreenEnums.of(HealTime.Kind.values(), values.get(DockScreenValues.HEAL_KIND));
    return new HealTime(kind, Math.max(values.get(DockScreenValues.HEAL_MINUTES), 0));
  }

  public RobotExpression robotExpression() {
    return ScreenEnums.of(RobotExpression.values(), values.get(DockScreenValues.EXPRESSION));
  }

  public RobotTier robotTier() {
    return ScreenEnums.of(RobotTier.values(), values.get(DockScreenValues.TIER));
  }

  /** Empty when the server does not know the name of the owner. */
  public Optional<String> ownerName() {
    return opening.ownerName().isEmpty() ? Optional.empty() : Optional.of(opening.ownerName());
  }

  public Energy inputLimit() {
    return new Energy(Math.max(opening.inputLimit(), 0));
  }

  public int callRadius() {
    return opening.callRadius();
  }

  /** Only on the client: the slots of the closed tab are hidden, so they take no clicks. */
  public void showStorage(boolean shown) {
    storageShown = shown;
  }

  /** The container itself refuses what the storage does not keep, on both sides. */
  private void addStorageSlots(Container storage) {
    for (int index = 0; index < DockStorageRules.SLOTS; index++) {
      addSlot(
          new Slot(
              storage,
              index,
              FIRST_SLOT_X + index % SLOTS_PER_ROW * SLOT_SIZE,
              STORAGE_Y + index / SLOTS_PER_ROW * SLOT_SIZE) {
            @Override
            public boolean mayPlace(ItemStack stack) {
              return container.canPlaceItem(getContainerSlot(), stack);
            }

            @Override
            public boolean isActive() {
              return storageShown;
            }
          });
    }
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
            : moveToDock(stack);
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

  /** The Toolbox and the Power Bank go to the storage, and redstone to the input (959). */
  private boolean moveToDock(ItemStack stack) {
    return moveItemStackTo(stack, STORAGE_FIRST, DOCK_SLOTS, false)
        || moveItemStackTo(stack, 0, STORAGE_FIRST, false);
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValid.test(player);
  }

  /** What only the server does: check the player, call a robot and read the dock. */
  private record Server(Predicate<Player> stillValid, Runnable call, Runnable refresh) {
    private static final Server NONE = new Server(player -> true, () -> {}, () -> {});
  }

  /** The values that travel when the screen opens. */
  private record Opening(String ownerName, long inputLimit, int callRadius) {
    /** Only the client reads it, so the menu on the server keeps nothing. */
    private static final Opening NONE = new Opening(NO_NAME, 0, 0);

    /** The button names the radius of the tier Base, the only one in this phase (answer 693). */
    static Opening of(InductionDockBlockEntity dock) {
      return new Opening(
          dock.ownerName().orElse(NO_NAME),
          CubitMod.CONFIG.dockInputPerTick(),
          CubitMod.CONFIG
              .tierCatalog()
              .spec(CubitConfig.GENERAL_ROBOT, RobotTier.BASE)
              .callRadiusBlocks());
    }

    static Opening read(RegistryFriendlyByteBuf data) {
      return new Opening(data.readUtf(), data.readVarLong(), data.readVarInt());
    }

    void write(RegistryFriendlyByteBuf data) {
      data.writeUtf(ownerName);
      data.writeVarLong(inputLimit);
      data.writeVarInt(callRadius);
    }
  }
}
