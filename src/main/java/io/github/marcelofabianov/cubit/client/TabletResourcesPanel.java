package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.client.ScreenPainter.Box;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.menu.BehaviorKeys;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import io.github.marcelofabianov.cubit.menu.TabletRobotMenu;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * The Features tab of the Tablet: the ten switches of the Cubit in one list, and its area, with the
 * same controls of the server as the screen of the Cubit. A row the tier locks is dimmed, with the
 * tier that unlocks it (answer 1037).
 */
final class TabletResourcesPanel {
  private static final int LEFT = 7;
  private static final int LINE = 9;
  private static final int DETAIL_LINE = 7;
  private static final int TITLE_X = 8;
  private static final int TITLE_Y = 4;
  private static final int TITLE_INSET = 15;
  private static final int RULE = 13;
  private static final int FIRST_ROW = 17;
  private static final int ROW_HEIGHT = 14;
  private static final int ICON_X = 11;
  private static final int ICON_TOP = 1;
  private static final float ICON_SCALE = 0.75f;
  private static final int NAME_X = 30;
  private static final int NAME_TOP = 3;
  private static final int NOTE_TOP = 4;
  private static final int NOTE_WIDTH = 80;
  private static final int NOTE_GAP = 3;
  private static final int TOGGLE_INSET = 26;
  private static final int TOGGLE_TOP = 1;
  private static final int AREA_GAP = 6;
  private static final int[] BUTTONS = {
    RobotMenu.LAMP_BUTTON,
    RobotMenu.SCANNERS_BUTTON,
    RobotMenu.CHATTER_BUTTON,
    RobotMenu.SOLAR_BUTTON,
    RobotMenu.PICKUP_BUTTON,
    RobotMenu.MAGNET_BUTTON,
    RobotMenu.FETCH_BUTTON,
    RobotMenu.TORCHES_BUTTON,
    RobotMenu.AUTO_RETURN_BUTTON,
    RobotMenu.UNLOAD_BUTTON
  };
  private static final String[] NAMES = {
    ScreenKeys.LAMP,
    ScreenKeys.SCANNERS,
    ScreenKeys.CHATTER,
    ScreenKeys.SOLAR_PANEL,
    BehaviorKeys.PICKUP,
    BehaviorKeys.MAGNET,
    BehaviorKeys.FETCH,
    ScreenKeys.TORCHES,
    ScreenKeys.AUTO_RETURN,
    ScreenKeys.UNLOAD
  };

  private final ScreenPainter painter;
  private final TabletRobotMenu menu;
  private final RobotSwitchLook switches;
  private final int right;

  /** The rows run to the right side of the window, with the switch at the end. */
  TabletResourcesPanel(ScreenPainter painter, TabletRobotMenu menu, int right) {
    this.painter = painter;
    this.menu = menu;
    this.switches = new RobotSwitchLook(menu);
    this.right = right;
  }

  private int toggleX() {
    return right - TOGGLE_INSET;
  }

  private int noteX() {
    return toggleX() - NOTE_GAP - NOTE_WIDTH;
  }

  /** Drawn in the coordinates of the screen of the Cubit. */
  void render(GuiGraphics graphics) {
    Component title =
        Component.translatable(TabletKeys.TAB_RESOURCES).withStyle(ChatFormatting.BOLD);
    painter.text(
        graphics,
        title,
        new Box(TITLE_X, TITLE_Y, right - TITLE_INSET, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    graphics.fill(LEFT, RULE, right, RULE + 1, ScreenPainter.SHADE);
    graphics.fill(LEFT, RULE + 1, right, RULE + 2, ScreenPainter.WHITE);
    for (int row = 0; row < BUTTONS.length; row++) {
      renderRow(graphics, row, FIRST_ROW + row * ROW_HEIGHT);
    }
    renderArea(graphics, FIRST_ROW + BUTTONS.length * ROW_HEIGHT + AREA_GAP);
  }

  /** The area of the Cubit, with its size and corners, or none (answer 1051). */
  private void renderArea(GuiGraphics graphics, int top) {
    Component label = Component.translatable(TabletKeys.AREA_LINE).withStyle(ChatFormatting.BOLD);
    painter.text(
        graphics,
        label,
        new Box(LEFT + 1, top, NAME_X, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    Component value =
        menu.area()
            .map(area ->
                Component.translatable(
                    TabletKeys.AREA_SUMMARY,
                    area.width(),
                    area.height(),
                    area.depth(),
                    area.min().x(),
                    area.min().y(),
                    area.min().z(),
                    area.max().x(),
                    area.max().y(),
                    area.max().z()))
            .orElseGet(() -> Component.translatable(TabletKeys.AREA_NONE));
    painter.text(
        graphics,
        value,
        new Box(NAME_X + LEFT, top, right - NAME_X - LEFT, LINE),
        ScreenPainter.DETAIL,
        ScreenPainter.FULL_SCALE);
  }

  private void renderRow(GuiGraphics graphics, int row, int top) {
    if (row % 2 == 0) {
      graphics.fill(LEFT, top, right, top + ROW_HEIGHT, ScreenPainter.ROW);
    }
    int button = BUTTONS[row];
    renderIcon(graphics, button, top + ICON_TOP);
    painter.text(
        graphics,
        Component.translatable(NAMES[row]),
        new Box(NAME_X, top + NAME_TOP, noteX() - NAME_X, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    ScreenToggle.draw(
        graphics,
        painter,
        toggleX(),
        top + TOGGLE_TOP,
        switches.on(button),
        switches.available(button));
    Optional<RobotTier> locked = switches.lockedUntil(button);
    if (locked.isPresent()) {
      painter.textRight(
          graphics,
          RobotSwitchLook.unlocksAt(locked.get()),
          new Box(noteX(), top + NOTE_TOP, NOTE_WIDTH, DETAIL_LINE),
          ScreenPainter.DETAIL,
          ScreenPainter.DETAIL_SCALE);
      graphics.fill(LEFT, top, right, top + ROW_HEIGHT, ScreenToggle.DISABLED);
    }
  }

  /** The icons of the switches of the screen of the Cubit. */
  private static void renderIcon(GuiGraphics graphics, int button, int top) {
    graphics.pose().pushPose();
    graphics.pose().translate(ICON_X, top, 0);
    graphics.pose().scale(ICON_SCALE, ICON_SCALE, 1);
    ScreenSprites.switchIcon(graphics, 0, 0, button);
    graphics.pose().popPose();
  }

  /** The switch under the mouse, when it can change. */
  OptionalInt switchAt(double x, double y) {
    for (int row = 0; row < BUTTONS.length; row++) {
      int top = FIRST_ROW + row * ROW_HEIGHT + TOGGLE_TOP;
      if (ScreenToggle.contains(toggleX(), top, x, y) && switches.available(BUTTONS[row])) {
        return OptionalInt.of(BUTTONS[row]);
      }
    }
    return OptionalInt.empty();
  }

  /** The detail of the switch under the mouse, as its tooltip. */
  boolean renderDetail(GuiGraphics graphics, Font font, int mouseX, int mouseY, int left, int top) {
    double x = mouseX - left;
    double y = mouseY - top;
    int row = (int) Math.floor((y - FIRST_ROW) / ROW_HEIGHT);
    if (x < LEFT || x >= right || y < FIRST_ROW || row >= BUTTONS.length) {
      return false;
    }
    graphics.renderTooltip(font, switches.detail(BUTTONS[row]), mouseX, mouseY);
    return true;
  }
}
