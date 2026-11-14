package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.client.ScreenPainter.Box;
import io.github.marcelofabianov.cubit.client.ScreenPainter.Colors;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import java.util.OptionalInt;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * The settings tab of the robot screen, as in the concept image: the switches that exist, the
 * scanner filter and the status. The seven switches take rows of 12 px, with the detail in their
 * tooltip, so the torches, the auto return and the unload fit (answers 974, 1066 and 1093). Every
 * number comes from the server.
 */
final class RobotSettingsPanel {
  private static final int LEFT = 7;
  private static final int DEFAULT_RIGHT = 165;
  private static final int HEADING_INSET = 15;
  private static final int TOGGLE_INSET = 26;
  private static final int NAME_END_GAP = 3;
  private static final int POWER_INSET = 11;
  private static final int TEXT_INSET = 31;
  private static final int STATUS_INSET = 10;
  private static final int LINE = 9;
  private static final int DETAIL_LINE = 7;
  private static final int TITLE_X = 8;
  private static final int FUNCTIONS_Y = 18;
  private static final int FUNCTIONS_RULE = 27;
  private static final int FIRST_FUNCTION = 31;
  private static final int FUNCTION_HEIGHT = 12;
  private static final int ICON_X = 11;
  private static final float ICON_SCALE = 0.75f;
  private static final int NAME_X = 30;
  private static final int NAME_TOP = 2;
  private static final int TOGGLE_TOP = 0;
  private static final int FILTER_TITLE_Y = 120;
  private static final int FILTER_RULE = 129;
  private static final int ORE_FRAME_X = 8;
  private static final int ORE_FRAME_Y = 134;
  private static final int TEXT_X = 32;
  private static final int LOOKING_Y = 135;
  private static final int HINT_Y = 145;
  private static final int STATUS_TITLE_Y = 160;
  private static final int STATUS_RULE = 169;
  private static final int FIRST_STATUS = 173;
  private static final int STATUS_HEIGHT = 14;
  private static final int STATUS_TEXT_TOP = 3;
  private static final int STATUS_LABEL_X = 10;
  private static final int[] BUTTONS = {
    RobotMenu.LAMP_BUTTON,
    RobotMenu.SCANNERS_BUTTON,
    RobotMenu.CHATTER_BUTTON,
    RobotMenu.SOLAR_BUTTON,
    RobotMenu.TORCHES_BUTTON,
    RobotMenu.AUTO_RETURN_BUTTON,
    RobotMenu.UNLOAD_BUTTON
  };
  private static final int FOLLOWING_ROW = 3;
  private static final int POWER_Y = 4;
  private static final int POWER_SIZE = 14;
  private static final int POWER_ICON_INSET = 1;
  private static final String[] NAMES = {
    ScreenKeys.LAMP,
    ScreenKeys.SCANNERS,
    ScreenKeys.CHATTER,
    ScreenKeys.SOLAR_PANEL,
    ScreenKeys.TORCHES,
    ScreenKeys.AUTO_RETURN,
    ScreenKeys.UNLOAD
  };

  private final ScreenPainter painter;
  private final RobotMenu menu;
  private final ItemStack oreIcon;
  private final RobotSwitchLook switches;
  private final int right;

  RobotSettingsPanel(ScreenPainter painter, RobotMenu menu, ItemStack oreIcon) {
    this(painter, menu, oreIcon, DEFAULT_RIGHT);
  }

  /** The rows run to the right side of the window, which grows with the tier (answer 1102). */
  RobotSettingsPanel(ScreenPainter painter, RobotMenu menu, ItemStack oreIcon, int right) {
    this.painter = painter;
    this.menu = menu;
    this.oreIcon = oreIcon;
    this.switches = new RobotSwitchLook(menu);
    this.right = right;
  }

  private int toggleX() {
    return right - TOGGLE_INSET;
  }

  private int powerX() {
    return right - POWER_INSET;
  }

  private Box heading(int y) {
    return new Box(TITLE_X, y, right - HEADING_INSET, LINE);
  }

  /** Drawn in the coordinates of the window. */
  void render(GuiGraphics graphics) {
    heading(graphics, ScreenKeys.FUNCTIONS, heading(FUNCTIONS_Y), FUNCTIONS_RULE);
    for (int row = 0; row < BUTTONS.length; row++) {
      renderFunction(graphics, row, FIRST_FUNCTION + row * FUNCTION_HEIGHT);
    }
    heading(graphics, ScreenKeys.SCANNER_FILTER, heading(FILTER_TITLE_Y), FILTER_RULE);
    renderFilter(graphics);
    heading(graphics, ScreenKeys.STATUS, heading(STATUS_TITLE_Y), STATUS_RULE);
    renderStatus(graphics);
    renderPower(graphics);
  }

  /** The Power button in the header, dimmed while it cannot switch the robot off. */
  private void renderPower(GuiGraphics graphics) {
    ScreenPainter.sunken(graphics, powerX(), POWER_Y, POWER_SIZE, POWER_SIZE, ScreenPainter.WHITE);
    ScreenSprites.power(graphics, powerX() + POWER_ICON_INSET, POWER_Y + POWER_ICON_INSET);
    if (!menu.powerAvailable()) {
      graphics.fill(
          powerX(), POWER_Y, powerX() + POWER_SIZE, POWER_Y + POWER_SIZE, ScreenToggle.DISABLED);
    }
  }

  boolean overPower(double x, double y) {
    return x >= powerX() && x < powerX() + POWER_SIZE && y >= POWER_Y && y < POWER_Y + POWER_SIZE;
  }

  /** The switch or the Power button under the mouse, when it can change. */
  OptionalInt switchAt(double x, double y) {
    if (overPower(x, y)) {
      return menu.powerAvailable() ? OptionalInt.of(RobotMenu.POWER_BUTTON) : OptionalInt.empty();
    }
    for (int row = 0; row < BUTTONS.length; row++) {
      int top = FIRST_FUNCTION + row * FUNCTION_HEIGHT + TOGGLE_TOP;
      if (ScreenToggle.contains(toggleX(), top, x, y) && menu.switchAvailable(BUTTONS[row])) {
        return OptionalInt.of(BUTTONS[row]);
      }
    }
    return OptionalInt.empty();
  }

  private void heading(GuiGraphics graphics, String key, Box box, int rule) {
    Component title = Component.translatable(key).withStyle(ChatFormatting.BOLD);
    painter.text(graphics, title, box, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
    graphics.fill(LEFT, rule, right, rule + 1, ScreenPainter.SHADE);
    graphics.fill(LEFT, rule + 1, right, rule + 2, ScreenPainter.WHITE);
  }

  private void renderFunction(GuiGraphics graphics, int row, int top) {
    if (row % 2 == 0) {
      graphics.fill(LEFT, top, right, top + FUNCTION_HEIGHT, ScreenPainter.ROW);
    }
    int button = BUTTONS[row];
    graphics.pose().pushPose();
    graphics.pose().translate(ICON_X, top, 0);
    graphics.pose().scale(ICON_SCALE, ICON_SCALE, 1);
    ScreenSprites.switchIcon(graphics, 0, 0, button);
    graphics.pose().popPose();
    SwitchRowLabel.draw(
        painter,
        graphics,
        new SwitchRowLabel.Text(Component.translatable(NAMES[row]), switches.detail(button)),
        NAME_X,
        top + NAME_TOP,
        toggleX() - NAME_END_GAP);
    renderToggle(graphics, button, top + TOGGLE_TOP);
  }

  /** The detail of the switch under the mouse, as its tooltip, by the mouse (answer 974). */
  boolean renderDetail(GuiGraphics graphics, Font font, int mouseX, int mouseY, int left, int top) {
    double x = mouseX - left;
    double y = mouseY - top;
    int row = (int) Math.floor((y - FIRST_FUNCTION) / FUNCTION_HEIGHT);
    if (x < LEFT || x >= right || y < FIRST_FUNCTION || row >= BUTTONS.length) {
      return false;
    }
    graphics.renderTooltip(font, switches.detail(BUTTONS[row]), mouseX, mouseY);
    return true;
  }

  private void renderToggle(GuiGraphics graphics, int button, int top) {
    ScreenToggle.draw(
        graphics, painter, toggleX(), top, menu.switchOn(button), menu.switchAvailable(button));
  }

  private void renderFilter(GuiGraphics graphics) {
    ScreenPainter.slot(graphics, ORE_FRAME_X, ORE_FRAME_Y);
    graphics.renderItem(oreIcon, ORE_FRAME_X + 1, ORE_FRAME_Y + 1);
    Component looking = RobotTexts.filter(menu).copy().withStyle(ChatFormatting.BOLD);
    painter.text(
        graphics,
        looking,
        new Box(TEXT_X, LOOKING_Y, right - TEXT_INSET, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    painter.text(
        graphics,
        Component.translatable(ScreenKeys.FILTER_HINT),
        new Box(TEXT_X, HINT_Y, right - TEXT_INSET, DETAIL_LINE),
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
      graphics.fill(LEFT, top, right, top + STATUS_HEIGHT, ScreenPainter.ROW);
    }
    Box box =
        new Box(STATUS_LABEL_X, top + STATUS_TEXT_TOP, right - STATUS_INSET - STATUS_LABEL_X, LINE);
    painter.pair(
        graphics,
        box,
        Component.translatable(label),
        value.copy().withStyle(ChatFormatting.BOLD),
        colors);
  }
}
