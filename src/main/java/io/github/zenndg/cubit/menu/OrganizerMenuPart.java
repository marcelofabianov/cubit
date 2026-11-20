package io.github.zenndg.cubit.menu;

import io.github.zenndg.cubit.domain.robot.OrganizerMode;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * The Organizer inside the robot menu: the line Mode of the Work tab and the Supply tab, with five
 * rows at a time, the wheel that scrolls them and a ghost slot per row, which only shows a copy of
 * the item of the order, so nothing goes in or out of it (answers 1290, 1295, 1300 and 1303).
 */
public final class OrganizerMenuPart {
  public static final int MODE_BUTTON = 27;
  public static final int SCROLL_UP_BUTTON = 28;
  public static final int SCROLL_DOWN_BUTTON = 29;
  public static final int FIRST_DOWN_BUTTON = 30;
  public static final int FIRST_UP_BUTTON = FIRST_DOWN_BUTTON + OrganizerScreenValues.ROWS;
  public static final int FIRST_BIG_DOWN_BUTTON = FIRST_UP_BUTTON + OrganizerScreenValues.ROWS;
  public static final int FIRST_BIG_UP_BUTTON = FIRST_BIG_DOWN_BUTTON + OrganizerScreenValues.ROWS;
  public static final int ROWS = OrganizerScreenValues.ROWS;
  public static final int ROW_Y = 60;
  public static final int ROW_HEIGHT = 18;
  public static final int SLOT_X = 96;
  private static final int LAST_BUTTON = FIRST_BIG_UP_BUTTON + ROWS - 1;
  private static final int DOWN = -1;
  private static final int UP = 1;
  private static final int SLOT_TOP = 1;

  private final OrganizerControl control;
  private final OrganizerScreenValues values = new OrganizerScreenValues();
  private final SimpleContainer shown = new SimpleContainer(ROWS);
  private int firstSlot = -1;
  private int offset;
  private boolean visible;

  OrganizerMenuPart(OrganizerControl control) {
    this.control = control;
  }

  /** The index of the ghost slot of the first row shown, for the screens. */
  public int firstSlot() {
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
    for (int row = 0; row < ROWS; row++) {
      add.accept(new GhostSlot(row, SLOT_X, ROW_Y + row * ROW_HEIGHT + SLOT_TOP));
    }
  }

  /** On the server only, before the menu sends its changes. */
  void refresh() {
    List<SupplyRow> rows = control.rows();
    offset = Math.clamp(offset, 0, Math.max(rows.size() - ROWS, 0));
    values.refresh(control, offset);
    for (int row = 0; row < ROWS; row++) {
      int index = offset + row;
      ItemStack item = index < rows.size() ? rows.get(index).wanted() : ItemStack.EMPTY;
      if (!ItemStack.matches(item, shown.getItem(row))) {
        shown.setItem(row, item);
      }
    }
  }

  static boolean handles(int button) {
    return button >= MODE_BUTTON && button <= LAST_BUTTON;
  }

  /** The mode, the wheel, and − and + of each row, by 8 with Shift (answer 1300). */
  boolean press(Player player, int button) {
    if (button == MODE_BUTTON) {
      return control.switchMode(player);
    }
    if (button == SCROLL_UP_BUTTON || button == SCROLL_DOWN_BUTTON) {
      offset += button == SCROLL_UP_BUTTON ? DOWN : UP;
      return true;
    }
    int step = button - FIRST_DOWN_BUTTON;
    int row = step % ROWS;
    int direction = (step / ROWS) % 2 == 0 ? DOWN : UP;
    boolean big = button >= FIRST_BIG_DOWN_BUTTON;
    return control.step(player, offset + row, direction, big);
  }

  boolean owns(int slotId) {
    return firstSlot >= 0 && slotId >= firstSlot && slotId < firstSlot + ROWS;
  }

  /** A plain click sets the item of the order from the cursor, and an empty hand clears it. */
  void click(int slotId, ClickType type, Player player, ItemStack carried) {
    if (type == ClickType.PICKUP) {
      control.place(player, offset + slotId - firstSlot, carried);
    }
  }

  /** What the screens read, on the client. */
  public Optional<OrganizerMode> mode() {
    int value = OrganizerScreenValues.mode(values);
    return value == 0
        ? Optional.empty()
        : Optional.of(ScreenEnums.of(OrganizerMode.values(), value - 1));
  }

  public int count() {
    return OrganizerScreenValues.count(values);
  }

  public int limit() {
    return OrganizerScreenValues.limit(values);
  }

  public int offset() {
    return OrganizerScreenValues.offset(values);
  }

  public BlockPos pos(int row) {
    return OrganizerScreenValues.pos(values, row);
  }

  public int face(int row) {
    return OrganizerScreenValues.face(values, row);
  }

  public int amount(int row) {
    return OrganizerScreenValues.amount(values, row);
  }

  /** A copy of the item of an order, shown only in the Supply tab, for the rows that exist. */
  private final class GhostSlot extends Slot {
    private final int row;

    GhostSlot(int row, int x, int y) {
      super(shown, row, x, y);
      this.row = row;
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
      return visible && offset() + row < count();
    }
  }
}
