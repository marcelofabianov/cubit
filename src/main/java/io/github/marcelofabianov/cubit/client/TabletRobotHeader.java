package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.client.ScreenPainter.Box;
import io.github.marcelofabianov.cubit.client.ScreenPainter.Colors;
import io.github.marcelofabianov.cubit.domain.behavior.RobotScreenStatus;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.item.TierLook;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import io.github.marcelofabianov.cubit.menu.TabletRobotMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * The header of the screen of a Cubit on the Tablet, fixed in every tab: the portrait, the name and
 * the tier, the state, the bars, the recharge and the place (answers 1011 and 1035). The buttons
 * sit in a row under the bars, and the screen owns them.
 */
final class TabletRobotHeader {
  static final int WIDTH = 300;
  static final int MARGIN = 8;
  static final int BUTTON_Y = 68;
  static final int BUTTON_HEIGHT = 14;
  static final int TEXT_X = 50;
  static final int HEIGHT = 90;
  private static final int LINE = 9;
  private static final int DETAIL_LINE = 7;
  private static final int TITLE_Y = 8;
  private static final int TITLE_WIDTH = 150;
  private static final int PORTRAIT_Y = 22;
  private static final int BARS_WIDTH = 150;
  private static final Box STATUS = new Box(200, 8, 92, LINE);
  private static final Box HEALTH = new Box(TEXT_X, 22, BARS_WIDTH, LINE);
  private static final Box HEALTH_BAR = new Box(TEXT_X, 30, BARS_WIDTH, 7);
  private static final Box BATTERY = new Box(TEXT_X, 41, BARS_WIDTH, LINE);
  private static final Box BATTERY_BAR = new Box(TEXT_X, 49, BARS_WIDTH, 7);
  private static final Box RECHARGE = new Box(TEXT_X, 59, BARS_WIDTH, DETAIL_LINE);
  private static final Box PLACE_TITLE = new Box(210, 22, 82, LINE);
  private static final Box COORDINATES = new Box(210, 33, 82, LINE);
  private static final Box DIMENSION = new Box(210, 43, 82, LINE);
  private static final int RULE = 86;
  private static final String SPACE = " ";

  private final ScreenPainter painter;
  private final TabletRobotMenu menu;

  TabletRobotHeader(ScreenPainter painter, TabletRobotMenu menu) {
    this.painter = painter;
    this.menu = menu;
  }

  /** Drawn in the coordinates of the window, with the rule across its whole width. */
  void render(GuiGraphics graphics, Component name, long tick, int windowWidth) {
    RobotPortrait.draw(
        graphics,
        MARGIN,
        PORTRAIT_Y,
        new RobotPortrait.Look(menu.expression(), menu.tier(), RobotShell.argb(menu.shellColor())),
        tick);
    title(painter, graphics, name, menu.tier());
    RobotScreenStatus status = menu.status();
    painter.status(graphics, RobotTexts.status(menu), STATUS, RobotTexts.statusColor(status));
    renderBars(graphics, status);
    Component coordinates =
        Component.translatable(TabletKeys.COORDINATES, menu.blockX(), menu.blockY(), menu.blockZ());
    place(painter, graphics, coordinates, TabletDimensions.name(menu.tablet().dimension()));
    rule(graphics, windowWidth);
  }

  /** The name in bold and the label of the tier, after the button Back. */
  static void title(ScreenPainter painter, GuiGraphics graphics, Component name, RobotTier tier) {
    Component title =
        name.copy().withStyle(ChatFormatting.BOLD).append(SPACE).append(TierLook.name(tier));
    painter.text(
        graphics,
        title,
        new Box(TEXT_X, TITLE_Y, TITLE_WIDTH, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
  }

  static void place(
      ScreenPainter painter, GuiGraphics graphics, Component coordinates, Component dimension) {
    Component title = Component.translatable(TabletKeys.PLACE_TITLE).withStyle(ChatFormatting.BOLD);
    painter.text(graphics, title, PLACE_TITLE, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
    painter.text(
        graphics, coordinates, COORDINATES, ScreenPainter.DETAIL, ScreenPainter.FULL_SCALE);
    painter.text(graphics, dimension, DIMENSION, ScreenPainter.DETAIL, ScreenPainter.FULL_SCALE);
  }

  static void rule(GuiGraphics graphics, int width) {
    graphics.fill(MARGIN - 1, RULE, width - MARGIN, RULE + 1, ScreenPainter.SHADE);
    graphics.fill(MARGIN - 1, RULE + 1, width - MARGIN, RULE + 2, ScreenPainter.WHITE);
  }

  /** As the screen of the Cubit: the battery is red only when critical (answer 681). */
  private void renderBars(GuiGraphics graphics, RobotScreenStatus status) {
    Component health =
        Component.translatable(ScreenKeys.HEALTH_AMOUNT, menu.health(), menu.maxHealth());
    painter.pair(
        graphics, HEALTH, Component.translatable(ScreenKeys.HEALTH), health, Colors.TITLE_FIRST);
    ScreenPainter.bar(graphics, HEALTH_BAR, menu.health(), menu.maxHealth(), ScreenPainter.RED);
    painter.pair(
        graphics,
        BATTERY,
        Component.translatable(ScreenKeys.BATTERY),
        DockTexts.amount(menu.charge(), menu.capacity()),
        Colors.TITLE_FIRST);
    int color =
        status == RobotScreenStatus.CRITICAL_BATTERY ? ScreenPainter.RED : ScreenPainter.GREEN;
    ScreenPainter.bar(
        graphics, BATTERY_BAR, menu.charge().amount(), menu.capacity().amount(), color);
    painter.text(
        graphics,
        RobotTexts.recharge(menu),
        RECHARGE,
        ScreenPainter.DETAIL,
        ScreenPainter.DETAIL_SCALE);
  }
}
