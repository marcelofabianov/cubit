package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.client.ScreenPainter.Box;
import io.github.marcelofabianov.cubit.domain.robot.PickupEntry;
import io.github.marcelofabianov.cubit.menu.BehaviorKeys;
import io.github.marcelofabianov.cubit.menu.PickupScreenValues;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;

/**
 * The behavior tab: the pickup, magnet and fetch switches and the ghost slots of the pickup filter
 * (answers 878 to 888 and 938). Without the Functions heading and with rows of 12 px, so three rows
 * of the filter fit above the player inventory, which stays shown (answers 893, 898 and 940). The
 * detail of each switch shows in its tooltip.
 */
final class RobotBehaviorPanel {
  private static final int LEFT = 7;
  private static final int DEFAULT_RIGHT = 165;
  private static final int LINE = 9;
  private static final int FIRST_ROW = 18;
  private static final int ROW_HEIGHT = 12;
  private static final int ICON_X = 11;
  private static final float ICON_SCALE = 0.75f;
  private static final int NAME_X = 30;
  private static final int NAME_TOP = 2;
  private static final int TOGGLE_INSET = 26;
  private static final int NAME_END_GAP = 3;
  private static final int TOGGLE_TOP = 0;
  private static final int FILTER_TITLE_X = 8;
  private static final int TITLE_GAP = 4;
  private static final int FILTER_TITLE_Y = 57;
  private static final int FILTER_RULE = 66;
  private static final int MODE_Y = 54;
  private static final int MODE_WIDTH = 56;
  private static final int MODE_HEIGHT = 11;
  private static final int MARK_SIZE = 3;
  private static final int MARK_INSET = 14;
  private static final int MARK_COLOR = 0xFFF5C400;
  private static final int[] BUTTONS = {
    RobotMenu.PICKUP_BUTTON, RobotMenu.MAGNET_BUTTON, RobotMenu.FETCH_BUTTON
  };
  private static final String[] NAMES = {
    BehaviorKeys.PICKUP, BehaviorKeys.MAGNET, BehaviorKeys.FETCH
  };

  private final ScreenPainter painter;
  private final RobotMenu menu;
  private final RobotSwitchLook switches;
  private final int right;

  RobotBehaviorPanel(ScreenPainter painter, RobotMenu menu) {
    this(painter, menu, DEFAULT_RIGHT);
  }

  /** The Tablet stretches the filter heading and moves the list button to its right side. */
  RobotBehaviorPanel(ScreenPainter painter, RobotMenu menu, int right) {
    this.painter = painter;
    this.menu = menu;
    this.switches = new RobotSwitchLook(menu);
    this.right = right;
  }

  private int toggleX() {
    return right - TOGGLE_INSET;
  }

  /** The list button stands at the right side, over the end of the filter. */
  private int modeX() {
    return right - MODE_WIDTH;
  }

  /** The filter starts at the left side, with as many columns as the window holds (1105). */
  private int columns() {
    return menu.filterColumns();
  }

  private ContainerData values() {
    return menu.pickupValues();
  }

  /** In the coordinates of the window, behind the items of the ghost slots. */
  void renderFrames(GuiGraphics graphics, int left, int top) {
    int slots = PickupScreenValues.filterSlots(values());
    for (int slot = 0; slot < slots; slot++) {
      ScreenPainter.slot(
          graphics,
          left + RobotMenu.FIRST_SLOT_X - 1 + slot % columns() * RobotMenu.SLOT_SIZE,
          top + filterTop() - 1 + slot / columns() * RobotMenu.SLOT_SIZE);
    }
  }

  /** In the coordinates of the window, over the items, so the tag mark shows. */
  void render(GuiGraphics graphics) {
    for (int row = 0; row < BUTTONS.length; row++) {
      renderRow(graphics, row);
    }
    renderFilter(graphics);
  }

  /** Only the filter, as the Filters tab of the Tablet shows it (answer 1037). */
  void renderFilter(GuiGraphics graphics) {
    Component title = Component.translatable(BehaviorKeys.FILTER).withStyle(ChatFormatting.BOLD);
    painter.text(
        graphics,
        title,
        new Box(FILTER_TITLE_X, FILTER_TITLE_Y, modeX() - FILTER_TITLE_X - TITLE_GAP, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    graphics.fill(LEFT, FILTER_RULE, right, FILTER_RULE + 1, ScreenPainter.SHADE);
    graphics.fill(LEFT, FILTER_RULE + 1, right, FILTER_RULE + 2, ScreenPainter.WHITE);
    renderMode(graphics);
    renderTagMarks(graphics);
  }

  /** The icons shrink to the height of the row, so the three rows fit (answers 940 and 1039). */
  private void renderRow(GuiGraphics graphics, int row) {
    int top = FIRST_ROW + row * ROW_HEIGHT;
    if (row % 2 == 0) {
      graphics.fill(LEFT, top, right, top + ROW_HEIGHT, ScreenPainter.ROW);
    }
    graphics.pose().pushPose();
    graphics.pose().translate(ICON_X, top, 0);
    graphics.pose().scale(ICON_SCALE, ICON_SCALE, 1);
    ScreenSprites.switchIcon(graphics, 0, 0, BUTTONS[row]);
    graphics.pose().popPose();
    SwitchRowLabel.draw(
        painter,
        graphics,
        new SwitchRowLabel.Text(Component.translatable(NAMES[row]), switches.detail(BUTTONS[row])),
        NAME_X,
        top + NAME_TOP,
        toggleX() - NAME_END_GAP);
    ScreenToggle.draw(
        graphics,
        painter,
        toggleX(),
        top + TOGGLE_TOP,
        switches.on(BUTTONS[row]),
        switches.available(BUTTONS[row]));
  }

  /** The detail of the switch under the mouse, as its tooltip, by the mouse (answer 940). */
  boolean renderDetail(GuiGraphics graphics, Font font, int mouseX, int mouseY, int left, int top) {
    Optional<Component> detail = detailAt(mouseX - left, mouseY - top);
    detail.ifPresent(text -> graphics.renderTooltip(font, text, mouseX, mouseY));
    return detail.isPresent();
  }

  private Optional<Component> detailAt(double x, double y) {
    if (x < LEFT || x >= right || y < FIRST_ROW) {
      return Optional.empty();
    }
    int row = (int) ((y - FIRST_ROW) / ROW_HEIGHT);
    return row < BUTTONS.length ? Optional.of(switches.detail(BUTTONS[row])) : Optional.empty();
  }

  /** The blue flat button of the screens, as Rename on the Tablet. */
  private void renderMode(GuiGraphics graphics) {
    Component mode =
        Component.translatable(BehaviorKeys.mode(PickupScreenValues.mode(values())))
            .withStyle(ChatFormatting.BOLD);
    FlatButton.paint(
        graphics, new Box(modeX(), MODE_Y, MODE_WIDTH, MODE_HEIGHT), mode, false, false);
  }

  private void renderTagMarks(GuiGraphics graphics) {
    for (int slot = 0; slot < PickupScreenValues.filterSlots(values()); slot++) {
      if (tagAt(slot).isPresent()) {
        int x = RobotMenu.FIRST_SLOT_X + slot % columns() * RobotMenu.SLOT_SIZE;
        int y = filterTop() + slot / columns() * RobotMenu.SLOT_SIZE;
        graphics.fill(
            x + MARK_INSET - 1, y, x + MARK_INSET - 1 + MARK_SIZE, y + MARK_SIZE, MARK_COLOR);
      }
    }
  }

  /** A filter slot that holds a tag shows the tag, not the item it stands for (answer 882). */
  boolean renderTagTooltip(GuiGraphics graphics, Font font, Slot hovered, int mouseX, int mouseY) {
    if (hovered == null) {
      return false;
    }
    Optional<String> tag = filterSlotOf(hovered).stream().boxed().findFirst().flatMap(this::tagAt);
    tag.ifPresent(name ->
        graphics.renderTooltip(
            font,
            Component.translatable(BehaviorKeys.TAG, PickupEntry.TAG_MARK + name),
            mouseX,
            mouseY));
    return tag.isPresent();
  }

  /** The tag of a filter slot, from the item the server shows in it. */
  Optional<String> tagAt(int slot) {
    Slot shown = menu.slots.get(menu.filterFirstSlot() + slot);
    return PickupScreenValues.tagAt(values(), slot, shown.getItem());
  }

  /** The index of a filter slot, when the slot belongs to the filter. */
  OptionalInt filterSlotOf(Slot slot) {
    int index = slot.index - menu.filterFirstSlot();
    return index >= 0 && index < PickupScreenValues.MAX_FILTER_SLOTS
        ? OptionalInt.of(index)
        : OptionalInt.empty();
  }

  /** The switch or the mode button under the mouse, when it can change. */
  OptionalInt buttonAt(double x, double y) {
    OptionalInt mode = modeAt(x, y);
    if (mode.isPresent()) {
      return mode;
    }
    for (int row = 0; row < BUTTONS.length; row++) {
      int top = FIRST_ROW + row * ROW_HEIGHT + TOGGLE_TOP;
      if (ScreenToggle.contains(toggleX(), top, x, y) && switches.available(BUTTONS[row])) {
        return OptionalInt.of(BUTTONS[row]);
      }
    }
    return OptionalInt.empty();
  }

  /** The button of the list of the filter, which turns it between allow and deny. */
  OptionalInt modeAt(double x, double y) {
    boolean inside =
        x >= modeX() && x < modeX() + MODE_WIDTH && y >= MODE_Y && y < MODE_Y + MODE_HEIGHT;
    return inside ? OptionalInt.of(RobotMenu.PICKUP_MODE_BUTTON) : OptionalInt.empty();
  }

  /** The top of the filter, from the heading, so the Tablet can start its tab there. */
  static int filterHeadingY() {
    return MODE_Y;
  }

  private static int filterTop() {
    return RobotMenu.PICKUP_FILTER_Y;
  }
}
