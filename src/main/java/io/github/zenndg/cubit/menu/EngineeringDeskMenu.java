package io.github.zenndg.cubit.menu;

import io.github.zenndg.cubit.block.DeskGrid;
import io.github.zenndg.cubit.block.DeskMaterialFilter;
import io.github.zenndg.cubit.block.EngineeringDeskBlockEntity;
import io.github.zenndg.cubit.domain.desk.DeskSpec;
import io.github.zenndg.cubit.domain.desk.DeskStatus;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.item.EnergySlotItems;
import io.github.zenndg.cubit.registry.CubitMenus;
import java.util.function.Predicate;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
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
 * The screen of the Engineering Desk, with the Project and Storage tabs (answer 1473). Anyone may
 * use it (answer 1431), the server decides what it does, and the open tab is only a choice of the
 * client.
 */
public final class EngineeringDeskMenu extends AbstractContainerMenu {
  public static final int WIDTH = 256;
  public static final int ENERGY_SLOT_X = 224;
  public static final int ENERGY_SLOT_Y = 104;
  public static final int STORAGE_Y = 30;
  public static final int INPUT_X = 8;
  public static final int OUTPUT_X = 140;
  public static final int STORAGE_COLUMNS = 6;
  public static final int PLAYER_INVENTORY_Y = 146;
  public static final int SLOT_SIZE = 18;
  private static final int PLAYER_X = 48;
  private static final int HOTBAR_GAP = 58;
  private static final int PLAYER_ROWS = 3;
  private static final int SLOTS_PER_ROW = 9;
  private static final int INPUT_FIRST = 1;
  private static final int OUTPUT_FIRST = INPUT_FIRST + DeskSpec.MAX_SLOTS;
  private static final int DESK_SLOTS = OUTPUT_FIRST + DeskSpec.MAX_SLOTS;

  private final Predicate<Player> stillValid;
  private final ContainerData values;
  private final Runnable refresh;
  private final EngineeringDeskBlockEntity desk;
  private final NonNullList<ItemStack> marks =
      NonNullList.withSize(DeskGrid.CELLS, ItemStack.EMPTY);
  private boolean storageShown;

  private EngineeringDeskMenu(
      int containerId,
      Inventory playerInventory,
      Containers containers,
      ContainerData values,
      EngineeringDeskBlockEntity desk) {
    super(CubitMenus.ENGINEERING_DESK.get(), containerId);
    this.desk = desk;
    this.values = values;
    this.stillValid =
        desk == null
            ? player -> true
            : player ->
                !desk.isRemoved()
                    && player.canInteractWithBlock(
                        desk.getBlockPos(), Container.DEFAULT_DISTANCE_BUFFER);
    this.refresh = values instanceof DeskScreenValues server ? server::refresh : () -> {};
    addSlot(new EnergySlot(containers.energySlot()));
    addStorage(containers.input(), INPUT_X, true, playerInventory.player);
    addStorage(containers.output(), OUTPUT_X, false, playerInventory.player);
    addDataSlots(values);
    addPlayerSlots(playerInventory);
  }

  private record Containers(Container energySlot, Container input, Container output) {}

  public static EngineeringDeskMenu onServer(
      int containerId, Inventory playerInventory, EngineeringDeskBlockEntity desk) {
    DeskScreenValues values = new DeskScreenValues(desk);
    values.refresh();
    return new EngineeringDeskMenu(
        containerId,
        playerInventory,
        new Containers(desk.energySlot(), desk.input(), desk.output()),
        values,
        desk);
  }

  /** On the client. The real items and the numbers come from the server. */
  public static EngineeringDeskMenu onClient(
      int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
    data.readBlockPos();
    return new EngineeringDeskMenu(
        containerId,
        playerInventory,
        new Containers(
            new SimpleContainer(1),
            new SimpleContainer(DeskSpec.MAX_SLOTS),
            new SimpleContainer(DeskSpec.MAX_SLOTS)),
        new SimpleContainerData(DeskScreenValues.COUNT),
        null);
  }

  public static void writeOpening(RegistryFriendlyByteBuf data, EngineeringDeskBlockEntity desk) {
    data.writeBlockPos(desk.getBlockPos());
  }

  private void addStorage(Container items, int left, boolean input, Player player) {
    for (int index = 0; index < DeskSpec.MAX_SLOTS; index++) {
      int x = left + (index % STORAGE_COLUMNS) * SLOT_SIZE;
      int y = STORAGE_Y + (index / STORAGE_COLUMNS) * SLOT_SIZE;
      addSlot(new StorageSlot(items, index, x, y, input, player));
    }
  }

  private void addPlayerSlots(Inventory playerInventory) {
    for (int row = 0; row < PLAYER_ROWS; row++) {
      for (int column = 0; column < SLOTS_PER_ROW; column++) {
        int index = column + row * SLOTS_PER_ROW + SLOTS_PER_ROW;
        int x = PLAYER_X + column * SLOT_SIZE;
        addSlot(new Slot(playerInventory, index, x, PLAYER_INVENTORY_Y + row * SLOT_SIZE));
      }
    }
    for (int column = 0; column < SLOTS_PER_ROW; column++) {
      int x = PLAYER_X + column * SLOT_SIZE;
      addSlot(new Slot(playerInventory, column, x, PLAYER_INVENTORY_Y + HOTBAR_GAP));
    }
  }

  @Override
  public void broadcastChanges() {
    refresh.run();
    super.broadcastChanges();
  }

  @Override
  public boolean clickMenuButton(Player player, int id) {
    if (desk == null || !(player instanceof ServerPlayer server) || !stillValid(player)) {
      return false;
    }
    return DeskControl.click(desk, server, marks, id);
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValid.test(player);
  }

  /** The tab the client shows. Only the slots of that tab can be clicked. */
  public void showStorage(boolean shown) {
    storageShown = shown;
  }

  public boolean storageShown() {
    return storageShown;
  }

  /** The marks put by hand, kept only while the screen is open (answer 1426). */
  public NonNullList<ItemStack> marks() {
    return marks;
  }

  public Energy stock() {
    return DeskScreenValues.energy(values, DeskScreenValues.STOCK);
  }

  public Energy capacity() {
    return DeskScreenValues.energy(values, DeskScreenValues.CAPACITY);
  }

  public DeskStatus status() {
    return ScreenEnums.of(DeskStatus.values(), values.get(DeskScreenValues.STATUS));
  }

  public int progressPercent() {
    return values.get(DeskScreenValues.PROGRESS);
  }

  public int total() {
    return values.get(DeskScreenValues.TOTAL);
  }

  public int done() {
    return values.get(DeskScreenValues.DONE);
  }

  public int maxOrder() {
    return values.get(DeskScreenValues.MAX_ORDER);
  }

  /** The place of the chosen recipe in the catalog, or a negative number without one. */
  public int recipeIndex() {
    return values.get(DeskScreenValues.RECIPE);
  }

  public RobotTier tier() {
    return ScreenEnums.of(RobotTier.values(), values.get(DeskScreenValues.TIER));
  }

  public int slots() {
    return values.get(DeskScreenValues.SLOTS);
  }

  @Override
  public ItemStack quickMoveStack(Player player, int index) {
    Slot slot = slots.get(index);
    if (!slot.hasItem()) {
      return ItemStack.EMPTY;
    }
    ItemStack stack = slot.getItem();
    ItemStack before = stack.copy();
    boolean moved =
        index < DESK_SLOTS
            ? moveItemStackTo(stack, DESK_SLOTS, slots.size(), true)
            : moveIntoDesk(stack);
    if (!moved) {
      return ItemStack.EMPTY;
    }
    slot.setChanged();
    return before;
  }

  /** Materials go to the input, and redstone that is not a material goes to the energy slot. */
  private boolean moveIntoDesk(ItemStack stack) {
    int activeEnd = INPUT_FIRST + Math.max(slots(), 0);
    return moveItemStackTo(stack, INPUT_FIRST, activeEnd, false)
        || moveItemStackTo(stack, 0, INPUT_FIRST, false);
  }

  private final class EnergySlot extends Slot {
    EnergySlot(Container container) {
      super(container, 0, ENERGY_SLOT_X, ENERGY_SLOT_Y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
      return !EnergySlotItems.energyOf(stack).isZero();
    }

    @Override
    public boolean isActive() {
      return !storageShown;
    }
  }

  private final class StorageSlot extends Slot {
    private final boolean input;
    private final Player player;

    StorageSlot(Container items, int index, int x, int y, boolean input, Player player) {
      super(items, index, x, y);
      this.input = input;
      this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
      return input && DeskMaterialFilter.accepts(player.level().getRecipeManager(), stack);
    }

    @Override
    public boolean isActive() {
      return storageShown && getContainerSlot() < slots();
    }
  }
}
