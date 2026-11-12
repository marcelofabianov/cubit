package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.client.ScreenPainter.Box;
import io.github.marcelofabianov.cubit.client.ScreenPainter.Colors;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import java.util.OptionalInt;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * The settings tab of the robot screen, as in the concept image: the switches that exist, the
 * scanner filter and the status. Every number comes from the server.
 */
final class RobotSettingsPanel {
  private static final int LEFT = 7;
  private static final int RIGHT = 165;
  private static final int LINE = 9;
  private static final int DETAIL_LINE = 7;
  private static final Box FUNCTIONS = new Box(8, 18, 150, LINE);
  private static final int FUNCTIONS_RULE = 27;
  private static final int FIRST_FUNCTION = 31;
  private static final int FUNCTION_HEIGHT = 18;
  private static final int ICON_X = 11;
  private static final int ICON_TOP = 2;
  private static final int NAME_X = 30;
  private static final int NAME_TOP = 3;
  private static final int DETAIL_TOP = 11;
  private static final int NAME_WIDTH = 106;
  private static final int TOGGLE_X = 139;
  private static final int TOGGLE_TOP = 4;
  private static final int TOGGLE_WIDTH = 26;
  private static final int TOGGLE_HEIGHT = 11;
  private static final int KNOB_WIDTH = 10;
  private static final Box FILTER_TITLE = new Box(8, 108, 150, LINE);
  private static final int FILTER_RULE = 117;
  private static final int ORE_FRAME_X = 8;
  private static final int ORE_FRAME_Y = 122;
  private static final Box LOOKING = new Box(32, 123, 134, LINE);
  private static final Box HINT = new Box(32, 133, 134, DETAIL_LINE);
  private static final Box STATUS_TITLE = new Box(8, 148, 150, LINE);
  private static final int STATUS_RULE = 157;
  private static final int FIRST_STATUS = 161;
  private static final int STATUS_HEIGHT = 14;
  private static final int STATUS_TEXT_TOP = 3;
  private static final int STATUS_LABEL_X = 10;
  private static final int STATUS_WIDTH = 155;
  private static final int SWITCHES = 4;
  private static final int FOLLOWING_ROW = 3;
  private static final int POWER_X = 154;
  private static final int POWER_Y = 4;
  private static final int POWER_SIZE = 14;
  private static final int POWER_ICON_INSET = 1;
  private static final int TOGGLE_ON = 0xFF4CAF50;
  private static final int TOGGLE_OFF = 0xFFF4F4F4;
  private static final int KNOB_OFF = 0xFFCFCFCF;
  private static final int DISABLED = 0x88C6C6C6;
  private static final String[] NAMES = {
    ScreenKeys.LAMP, ScreenKeys.SCANNERS, ScreenKeys.CHATTER, ScreenKeys.SOLAR_PANEL
  };

  private final ScreenPainter painter;
  private final RobotMenu menu;
  private final ItemStack oreIcon;

  RobotSettingsPanel(ScreenPainter painter, RobotMenu menu, ItemStack oreIcon) {
    this.painter = painter;
    this.menu = menu;
    this.oreIcon = oreIcon;
  }

  /** Drawn in the coordinates of the window. */
  void render(GuiGraphics graphics) {
    heading(graphics, ScreenKeys.FUNCTIONS, FUNCTIONS, FUNCTIONS_RULE);
    for (int button = 0; button < SWITCHES; button++) {
      renderFunction(graphics, button, FIRST_FUNCTION + button * FUNCTION_HEIGHT);
    }
    heading(graphics, ScreenKeys.SCANNER_FILTER, FILTER_TITLE, FILTER_RULE);
    renderFilter(graphics);
    heading(graphics, ScreenKeys.STATUS, STATUS_TITLE, STATUS_RULE);
    renderStatus(graphics);
    renderPower(graphics);
  }

  /** The Power button in the header, dimmed while it cannot switch the robot off. */
  private void renderPower(GuiGraphics graphics) {
    ScreenPainter.sunken(graphics, POWER_X, POWER_Y, POWER_SIZE, POWER_SIZE, ScreenPainter.WHITE);
    ScreenSprites.power(graphics, POWER_X + POWER_ICON_INSET, POWER_Y + POWER_ICON_INSET);
    if (!menu.powerAvailable()) {
      graphics.fill(POWER_X, POWER_Y, POWER_X + POWER_SIZE, POWER_Y + POWER_SIZE, DISABLED);
    }
  }

  boolean overPower(double x, double y) {
    return x >= POWER_X && x < POWER_X + POWER_SIZE && y >= POWER_Y && y < POWER_Y + POWER_SIZE;
  }

  /** The switch or the Power button under the mouse, when it can change. */
  OptionalInt switchAt(double x, double y) {
    if (overPower(x, y)) {
      return menu.powerAvailable() ? OptionalInt.of(RobotMenu.POWER_BUTTON) : OptionalInt.empty();
    }
    for (int button = 0; button < SWITCHES; button++) {
      int top = FIRST_FUNCTION + button * FUNCTION_HEIGHT + TOGGLE_TOP;
      boolean inside =
          x >= TOGGLE_X && x < TOGGLE_X + TOGGLE_WIDTH && y >= top && y < top + TOGGLE_HEIGHT;
      if (inside && menu.switchAvailable(button)) {
        return OptionalInt.of(button);
      }
    }
    return OptionalInt.empty();
  }

  private void heading(GuiGraphics graphics, String key, Box box, int rule) {
    Component title = Component.translatable(key).withStyle(ChatFormatting.BOLD);
    painter.text(graphics, title, box, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
    graphics.fill(LEFT, rule, RIGHT, rule + 1, ScreenPainter.SHADE);
    graphics.fill(LEFT, rule + 1, RIGHT, rule + 2, ScreenPainter.WHITE);
  }

  private void renderFunction(GuiGraphics graphics, int button, int top) {
    if (button % 2 == 0) {
      graphics.fill(LEFT, top, RIGHT, top + FUNCTION_HEIGHT, ScreenPainter.ROW);
    }
    ScreenSprites.function(graphics, ICON_X, top + ICON_TOP, button);
    Box name = new Box(NAME_X, top + NAME_TOP, NAME_WIDTH, LINE);
    painter.text(
        graphics,
        Component.translatable(NAMES[button]),
        name,
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    Box detail = new Box(NAME_X, top + DETAIL_TOP, NAME_WIDTH, DETAIL_LINE);
    painter.text(
        graphics, detailOf(button), detail, ScreenPainter.DETAIL, ScreenPainter.DETAIL_SCALE);
    renderToggle(graphics, button, top + TOGGLE_TOP);
  }

  private Component detailOf(int button) {
    return switch (button) {
      case RobotMenu.LAMP_BUTTON -> RobotTexts.lampDetail(menu);
      case RobotMenu.SCANNERS_BUTTON -> RobotTexts.scannersDetail(menu);
      case RobotMenu.SOLAR_BUTTON -> RobotTexts.solarDetail(menu);
      default -> Component.translatable(ScreenKeys.CHATTER_DETAIL);
    };
  }

  /** On: green with the knob on the right. Off: light with the knob on the left. */
  private void renderToggle(GuiGraphics graphics, int button, int top) {
    boolean on = menu.switchOn(button);
    int right = TOGGLE_X + TOGGLE_WIDTH;
    ScreenPainter.sunken(
        graphics, TOGGLE_X, top, TOGGLE_WIDTH, TOGGLE_HEIGHT, on ? TOGGLE_ON : TOGGLE_OFF);
    int knobX = on ? right - 1 - KNOB_WIDTH : TOGGLE_X + 1;
    graphics.fill(
        knobX,
        top + 1,
        knobX + KNOB_WIDTH,
        top + TOGGLE_HEIGHT - 1,
        on ? ScreenPainter.WHITE : KNOB_OFF);
    Component label = Component.translatable(on ? ScreenKeys.SWITCH_ON : ScreenKeys.SWITCH_OFF);
    int labelX = on ? TOGGLE_X + 2 : knobX + KNOB_WIDTH + 1;
    int color = on ? ScreenPainter.WHITE : ScreenPainter.DETAIL;
    painter.text(graphics, label, labelX, top + 2, color, ScreenPainter.DETAIL_SCALE);
    if (!menu.switchAvailable(button)) {
      graphics.fill(TOGGLE_X, top, right, top + TOGGLE_HEIGHT, DISABLED);
    }
  }

  private void renderFilter(GuiGraphics graphics) {
    ScreenPainter.slot(graphics, ORE_FRAME_X, ORE_FRAME_Y);
    graphics.renderItem(oreIcon, ORE_FRAME_X + 1, ORE_FRAME_Y + 1);
    Component looking = RobotTexts.filter(menu).copy().withStyle(ChatFormatting.BOLD);
    painter.text(graphics, looking, LOOKING, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
    painter.text(
        graphics,
        Component.translatable(ScreenKeys.FILTER_HINT),
        HINT,
        ScreenPainter.DETAIL,
        ScreenPainter.DETAIL_SCALE);
  }

  private void renderStatus(GuiGraphics graphics) {
    Colors solar = new Colors(ScreenPainter.DETAIL, RobotTexts.solarColor(menu.solar()));
    statusRow(graphics, 0, ScreenKeys.SOLAR_PANEL, RobotTexts.solar(menu), solar);
    statusRow(graphics, 1, ScreenKeys.ENERGY_USE, RobotTexts.energyUse(menu), Colors.VALUE_FIRST);
    statusRow(graphics, 2, ScreenKeys.HEALTH, RobotTexts.health(menu), Colors.VALUE_FIRST);
    statusRow(
        graphics,
        FOLLOWING_ROW,
        ScreenKeys.FOLLOWING,
        RobotTexts.following(menu),
        Colors.VALUE_FIRST);
  }

  private void statusRow(
      GuiGraphics graphics, int row, String label, Component value, Colors colors) {
    int top = FIRST_STATUS + row * STATUS_HEIGHT;
    if (row % 2 == 0) {
      graphics.fill(LEFT, top, RIGHT, top + STATUS_HEIGHT, ScreenPainter.ROW);
    }
    Box box = new Box(STATUS_LABEL_X, top + STATUS_TEXT_TOP, STATUS_WIDTH - STATUS_LABEL_X, LINE);
    painter.pair(
        graphics,
        box,
        Component.translatable(label),
        value.copy().withStyle(ChatFormatting.BOLD),
        colors);
  }
}
