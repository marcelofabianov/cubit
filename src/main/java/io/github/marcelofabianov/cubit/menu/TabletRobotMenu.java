package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.dock.DockGoal;
import io.github.marcelofabianov.cubit.domain.robot.AreaCorner;
import io.github.marcelofabianov.cubit.domain.robot.FollowCall;
import io.github.marcelofabianov.cubit.domain.robot.WorkArea;
import io.github.marcelofabianov.cubit.registry.CubitMenus;
import java.util.Optional;
import java.util.function.Supplier;
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

  /** Makes the chosen Cubit follow its owner, before the rows to scroll (answers 1073 to 1075). */
  public static final int FOLLOW_BUTTON = 17;

  private static final int ONE_SLOT = 1;
  private static final int DOCK_VALUE = 0;
  private static final int FOLLOW_VALUE = 1;
  private static final int VALUES = 2;
  private static final int HAS_AREA = 0;
  private static final int FIRST_CORNER = 1;
  private static final int CORNER_VALUES = 6;
  private static final int MIN_CORNER = 0;
  private static final int MAX_CORNER = 3;
  private static final int AREA_VALUES = FIRST_CORNER + 2 * CORNER_VALUES;
  private static final int HALF_BITS = 16;
  private static final int HALF_MASK = 0xFFFF;

  /** The client shows only the corners and the size, so the dimension is not sent. */
  private static final String SHOWN_DIMENSION = "shown";

  private final TabletOpening tablet;
  private final DockGoalControl dock;
  private final FollowControl follow;
  private final Supplier<Optional<WorkArea>> area;
  private final ContainerData dockValues;
  private final ContainerData areaValues = new SimpleContainerData(AREA_VALUES);

  private TabletRobotMenu(
      int containerId,
      Inventory playerInventory,
      RobotMenuContents contents,
      RobotMenuServer server,
      TabletOpening tablet,
      Controls controls) {
    super(CubitMenus.TABLET_ROBOT.get(), containerId, playerInventory, contents, server, true);
    this.tablet = tablet;
    this.dock = controls.dock();
    this.follow = controls.follow();
    this.area = controls.area();
    this.dockValues = new SimpleContainerData(VALUES);
    addDataSlots(dockValues);
    addDataSlots(areaValues);
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
        new Controls(DockGoalControl.NONE, FollowControl.NONE, Optional::empty));
  }

  /** The buttons of the header that act on the Cubit itself, read on the server. */
  public record Controls(
      DockGoalControl dock, FollowControl follow, Supplier<Optional<WorkArea>> area) {}

  /** On the server, where the values come from the Cubit itself. */
  public static TabletRobotMenu onServer(
      int containerId,
      Inventory playerInventory,
      RobotMenuContents contents,
      RobotMenuSource source,
      TabletOpening tablet,
      Controls controls) {
    return new TabletRobotMenu(
        containerId, playerInventory, contents, serverOf(source), tablet, controls);
  }

  /** The server reads the goal at the interval of the dock search, not on every tick. */
  @Override
  public void broadcastChanges() {
    dockValues.set(DOCK_VALUE, dock.goal().ordinal());
    dockValues.set(FOLLOW_VALUE, follow.goal().ordinal());
    putArea(area.get());
    super.broadcastChanges();
  }

  public DockGoal dockGoal() {
    return ScreenEnums.of(DockGoal.values(), dockValues.get(DOCK_VALUE));
  }

  /** Six whole numbers, each in two halves, because each value travels in 16 bits. */
  private void putArea(Optional<WorkArea> shown) {
    areaValues.set(HAS_AREA, shown.isPresent() ? 1 : 0);
    shown.ifPresent(found -> {
      int[] numbers = {
        found.min().x(), found.min().y(), found.min().z(),
        found.max().x(), found.max().y(), found.max().z()
      };
      for (int index = 0; index < numbers.length; index++) {
        areaValues.set(FIRST_CORNER + 2 * index, numbers[index] & HALF_MASK);
        areaValues.set(FIRST_CORNER + 2 * index + 1, numbers[index] >>> HALF_BITS);
      }
    });
  }

  /** The area of the Cubit, for the Resources tab (answer 1051). */
  public Optional<WorkArea> area() {
    if (areaValues.get(HAS_AREA) == 0) {
      return Optional.empty();
    }
    return Optional.of(new WorkArea(corner(MIN_CORNER), corner(MAX_CORNER), SHOWN_DIMENSION));
  }

  private AreaCorner corner(int first) {
    return new AreaCorner(areaNumber(first), areaNumber(first + 1), areaNumber(first + 2));
  }

  private int areaNumber(int index) {
    int low = areaValues.get(FIRST_CORNER + 2 * index) & HALF_MASK;
    return (areaValues.get(FIRST_CORNER + 2 * index + 1) << HALF_BITS) | low;
  }

  public FollowCall followCall() {
    return ScreenEnums.of(FollowCall.values(), dockValues.get(FOLLOW_VALUE));
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
    if (button == FOLLOW_BUTTON) {
      return stillValid(player) && follow.call(player);
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
