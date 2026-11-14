package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.dock.DockGoal;
import io.github.marcelofabianov.cubit.registry.CubitMenus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

/**
 * The screen of a Cubit on the Tablet: the same values and controls as the screen of the Cubit, at
 * any distance, with the inventory of the Cubit only to read (answers 999, 1018 and 1023).
 */
public final class TabletRobotMenu extends RobotMenu {
  /** After the torches, before the rows to scroll: turns the Cubit off or on (answer 1033). */
  public static final int POWER_TOGGLE_BUTTON = 14;

  /** Sends the Cubit to its Dock, before the rows to scroll (answers 1020 and 1041). */
  public static final int GO_TO_DOCK_BUTTON = 15;

  private static final int ONE_SLOT = 1;

  private final TabletOpening tablet;
  private final DockGoalControl dock;
  private final ContainerData dockValues;

  private TabletRobotMenu(
      int containerId,
      Inventory playerInventory,
      RobotMenuContents contents,
      RobotMenuServer server,
      TabletOpening tablet,
      DockGoalControl dock) {
    super(CubitMenus.TABLET_ROBOT.get(), containerId, playerInventory, contents, server, true);
    this.tablet = tablet;
    this.dock = dock;
    this.dockValues = new SimpleContainerData(1);
    addDataSlots(dockValues);
  }

  /** On the client. The real items and numbers come from the server. */
  public static TabletRobotMenu onClient(
      int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
    RobotScreenOpening opening = RobotScreenOpening.read(data);
    TabletOpening tablet = TabletOpening.read(data);
    RobotMenuContents contents =
        new RobotMenuContents(
            new SimpleContainer(opening.slots()),
            new SimpleContainer(ONE_SLOT),
            new SimpleContainer(ONE_SLOT),
            opening);
    return new TabletRobotMenu(
        containerId,
        playerInventory,
        contents,
        RobotMenuServer.onClient(),
        tablet,
        DockGoalControl.NONE);
  }

  /** On the server, where the values come from the Cubit itself. */
  public static TabletRobotMenu onServer(
      int containerId,
      Inventory playerInventory,
      RobotMenuContents contents,
      RobotMenuSource source,
      TabletOpening tablet,
      DockGoalControl dock) {
    return new TabletRobotMenu(
        containerId, playerInventory, contents, serverOf(source), tablet, dock);
  }

  /** The server reads the goal at the interval of the dock search, not on every tick. */
  @Override
  public void broadcastChanges() {
    dockValues.set(0, dock.goal().ordinal());
    super.broadcastChanges();
  }

  public DockGoal dockGoal() {
    return ScreenEnums.of(DockGoal.values(), dockValues.get(0));
  }

  /** Only the client hides the slots: the inventory of the Cubit, or the player and the filter. */
  public void showTabletTab(boolean inventory, boolean filters) {
    showParts(inventory, filters, filters);
  }

  public TabletOpening tablet() {
    return tablet;
  }

  /** The Power of the header switches an active Cubit off and a switched-off one on. */
  @Override
  public boolean clickMenuButton(Player player, int button) {
    if (button == GO_TO_DOCK_BUTTON) {
      return stillValid(player) && dock.go(player);
    }
    if (button != POWER_TOGGLE_BUTTON) {
      return super.clickMenuButton(player, button);
    }
    if (!stillValid(player)) {
      return false;
    }
    return power().available() ? power().switchOff(player) : power().turnOn(player);
  }

  /** No click reaches the slots of the Cubit, so nothing moves in or out (answer 999). */
  @Override
  public void clicked(int slotId, int button, ClickType type, Player player) {
    if (slotId < 0 || slotId >= robotSlots()) {
      super.clicked(slotId, button, type, player);
    }
  }

  /** A shift click would put the item in the Cubit, so it does nothing here. */
  @Override
  public ItemStack quickMoveStack(Player player, int index) {
    return ItemStack.EMPTY;
  }
}
