package io.github.zenndg.cubit.client;

import io.github.zenndg.cubit.client.ScreenPainter.Box;
import io.github.zenndg.cubit.menu.OrganizerMenuPart;
import io.github.zenndg.cubit.menu.RobotMenu;
import io.github.zenndg.cubit.menu.WorkKeys;
import java.util.OptionalInt;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

/**
 * The Supply tab of the Organizer, in the screen of the Cubit and in the one of the Tablet: one row
 * per marked destination, five at a time, with the place and the face, the ghost slot of the item,
 * and − and + for the amount, by 8 with Shift (answers 1294, 1295, 1300 and 1303).
 */
final class SupplyPanel {
  private static final int LEFT = 7;
  private static final int TITLE_Y = 48;
  private static final int LINE = 9;
  private static final int GAP = 4;
  private static final int BUTTON_SIZE = 12;
  private static final int VALUE_WIDTH = 18;
  private static final int TEXT_LEFT = LEFT + GAP;
  private static final String DOWN = "−";
  private static final String UP = "+";
  private static final String PLACE = "%s, %s, %s %s";
  private static final String FACE_UP = "↑";
  private static final String FACE_DOWN = "↓";
  private static final String FACE_SIDE = "→";

  private final ScreenPainter painter;
  private final RobotMenu menu;
  private final int right;

  SupplyPanel(ScreenPainter painter, RobotMenu menu, int right) {
    this.painter = painter;
    this.menu = menu;
    this.right = right;
  }

  void render(GuiGraphics graphics, double mouseX, double mouseY) {
    OrganizerMenuPart part = menu.organizer();
    Component title =
        Component.translatable(WorkKeys.SUPPLY_TITLE, part.count(), part.limit())
            .withStyle(ChatFormatting.BOLD);
    painter.text(
        graphics,
        title,
        new Box(LEFT, TITLE_Y, right - LEFT, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    if (part.count() == 0) {
      painter.text(
          graphics,
          Component.translatable(WorkKeys.SUPPLY_EMPTY),
          new Box(LEFT, OrganizerMenuPart.ROW_Y + GAP, right - LEFT, LINE),
          ScreenPainter.DETAIL,
          ScreenPainter.FULL_SCALE);
      return;
    }
    for (int row = 0; row < OrganizerMenuPart.ROWS && part.offset() + row < part.count(); row++) {
      renderRow(graphics, row, mouseX, mouseY);
    }
  }

  private void renderRow(GuiGraphics graphics, int row, double mouseX, double mouseY) {
    OrganizerMenuPart part = menu.organizer();
    int top = top(row);
    if (row % 2 == 0) {
      graphics.fill(LEFT, top, right, top + OrganizerMenuPart.ROW_HEIGHT, ScreenPainter.ROW);
    }
    BlockPos pos = part.pos(row);
    Component place =
        Component.literal(
            PLACE.formatted(pos.getX(), pos.getY(), pos.getZ(), arrow(part.face(row))));
    painter.text(
        graphics,
        place,
        new Box(TEXT_LEFT, textY(top), slotX() - TEXT_LEFT - GAP, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.DETAIL_SCALE);
    ScreenPainter.slot(graphics, slotX() - 1, top);
    Component amount =
        Component.literal(String.valueOf(part.amount(row))).withStyle(ChatFormatting.BOLD);
    painter.text(
        graphics,
        amount,
        new Box(valueX(), textY(top), VALUE_WIDTH, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    paint(graphics, down(top), DOWN, mouseX, mouseY);
    paint(graphics, up(top), UP, mouseX, mouseY);
  }

  /** − or + under the point, by 8 with Shift, or the wheel is handled by the screen. */
  OptionalInt buttonAt(double x, double y, boolean shift) {
    for (int row = 0; row < OrganizerMenuPart.ROWS; row++) {
      if (menu.organizer().offset() + row >= menu.organizer().count()) {
        break;
      }
      int top = top(row);
      if (down(top).contains((int) x, (int) y)) {
        return OptionalInt.of(
            (shift ? OrganizerMenuPart.FIRST_BIG_DOWN_BUTTON : OrganizerMenuPart.FIRST_DOWN_BUTTON)
                + row);
      }
      if (up(top).contains((int) x, (int) y)) {
        return OptionalInt.of(
            (shift ? OrganizerMenuPart.FIRST_BIG_UP_BUTTON : OrganizerMenuPart.FIRST_UP_BUTTON)
                + row);
      }
    }
    return OptionalInt.empty();
  }

  /** The wheel moves the rows, one at a time. */
  static int scrollButton(double delta) {
    return delta > 0 ? OrganizerMenuPart.SCROLL_UP_BUTTON : OrganizerMenuPart.SCROLL_DOWN_BUTTON;
  }

  /** How to choose the item over an empty slot, and how the amount works over the buttons. */
  boolean renderDetail(GuiGraphics graphics, Font font, int mouseX, int mouseY, int left, int top) {
    int x = mouseX - left;
    int y = mouseY - top;
    for (int row = 0;
        row < OrganizerMenuPart.ROWS && menu.organizer().offset() + row < menu.organizer().count();
        row++) {
      int rowTop = top(row);
      if (y < rowTop || y >= rowTop + OrganizerMenuPart.ROW_HEIGHT) {
        continue;
      }
      String key =
          x >= down(rowTop).x() ? WorkKeys.SUPPLY_AMOUNT_DETAIL : WorkKeys.SUPPLY_ITEM_DETAIL;
      boolean overSlot = x >= slotX() && x < down(rowTop).x();
      boolean emptySlot = menu.getSlot(menu.organizer().firstSlot() + row).getItem().isEmpty();
      if (x >= down(rowTop).x() || (overSlot && emptySlot)) {
        graphics.renderTooltip(font, Component.translatable(key), mouseX, mouseY);
        return true;
      }
    }
    return false;
  }

  private static String arrow(int face) {
    Direction direction = Direction.from3DDataValue(face);
    return direction == Direction.UP
        ? FACE_UP
        : direction == Direction.DOWN ? FACE_DOWN : FACE_SIDE;
  }

  private void paint(GuiGraphics graphics, Box box, String label, double x, double y) {
    FlatButton.paint(
        graphics, box, Component.literal(label), false, box.contains((int) x, (int) y));
  }

  private static int top(int row) {
    return OrganizerMenuPart.ROW_Y + row * OrganizerMenuPart.ROW_HEIGHT;
  }

  private static int textY(int top) {
    return top + (OrganizerMenuPart.ROW_HEIGHT - LINE) / 2 + 1;
  }

  private static int slotX() {
    return OrganizerMenuPart.SLOT_X;
  }

  private static int downX() {
    return slotX() + RobotMenu.SLOT_SIZE + GAP;
  }

  private static int valueX() {
    return downX() + BUTTON_SIZE + GAP;
  }

  private static Box down(int top) {
    return new Box(downX(), buttonY(top), BUTTON_SIZE, BUTTON_SIZE);
  }

  private static Box up(int top) {
    return new Box(valueX() + VALUE_WIDTH, buttonY(top), BUTTON_SIZE, BUTTON_SIZE);
  }

  private static int buttonY(int top) {
    return top + (OrganizerMenuPart.ROW_HEIGHT - BUTTON_SIZE) / 2;
  }
}
