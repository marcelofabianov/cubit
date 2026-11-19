package io.github.zenndg.cubit.client;

import io.github.zenndg.cubit.client.ScreenPainter.Box;
import io.github.zenndg.cubit.domain.behavior.GameClock;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.menu.DeathKeys;
import io.github.zenndg.cubit.menu.TabletRobotMenu;
import io.github.zenndg.cubit.network.CubitNetwork;
import io.github.zenndg.cubit.network.DeathLogPayload;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * The Deaths tab of the Tablet: one row per death of the owner, the newest first, with the day and
 * the hour of the game, the dimension, the coordinates and the cause, and the guide and delete
 * buttons in the last column (answers 1111 and 1119). It follows the width of the window, and the
 * wheel scrolls a log longer than the window.
 */
final class TabletDeathsPanel {
  private static final int LEFT = 7;
  private static final int LINE = 9;
  private static final int TITLE_X = 8;
  private static final int TITLE_Y = 4;
  private static final int TITLE_INSET = 15;
  private static final int RULE = 13;
  private static final int HEAD_Y = 17;
  private static final int FIRST_ROW = 27;
  private static final int ROW_HEIGHT = 16;
  private static final int FIRST_LINE = 2;
  private static final int SECOND_LINE = 9;
  private static final int WHEN_X = 10;
  private static final int WHEN_WIDTH = 58;
  private static final int WHERE_X = 70;
  private static final int WHERE_GAP = 4;
  private static final int BUTTON_TOP = 2;
  private static final int BUTTON_HEIGHT = 12;
  private static final int GUIDE_WIDTH = 44;
  private static final int DELETE_WIDTH = 40;
  private static final int BUTTON_GAP = 3;
  private static final int BUTTON_INSET = 2;
  private static final String CLOCK = "%02d:%02d";

  private final ScreenPainter painter;
  private final TabletRobotMenu menu;
  private final int right;
  private final int rowsShown;
  private int scroll;

  /** The rows run to the right side of the window, and as many as fit the height are shown. */
  TabletDeathsPanel(ScreenPainter painter, TabletRobotMenu menu, int right, int height) {
    this.painter = painter;
    this.menu = menu;
    this.right = right;
    this.rowsShown = Math.max(1, (height - FIRST_ROW) / ROW_HEIGHT);
  }

  private int deleteX() {
    return right - BUTTON_INSET - DELETE_WIDTH;
  }

  private int guideX() {
    return deleteX() - BUTTON_GAP - GUIDE_WIDTH;
  }

  /** Drawn in the coordinates of the screen of the Cubit, with the mouse in the same ones. */
  void render(GuiGraphics graphics, double mouseX, double mouseY) {
    Component title = Component.translatable(DeathKeys.TAB_TITLE).withStyle(ChatFormatting.BOLD);
    painter.text(
        graphics,
        title,
        new Box(TITLE_X, TITLE_Y, right - TITLE_INSET, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    graphics.fill(LEFT, RULE, right, RULE + 1, ScreenPainter.SHADE);
    graphics.fill(LEFT, RULE + 1, right, RULE + 2, ScreenPainter.WHITE);
    List<DeathLogPayload.Row> rows = rows();
    if (rows.isEmpty()) {
      detail(
          graphics, Component.translatable(DeathKeys.EMPTY), new Box(WHEN_X, HEAD_Y, right, LINE));
      return;
    }
    renderHead(graphics);
    clampScroll(rows.size());
    for (int shown = 0; shown < rowsShown && scroll + shown < rows.size(); shown++) {
      int index = scroll + shown;
      renderRow(graphics, rows.get(index), shown, new Point(mouseX, mouseY));
    }
  }

  private void renderHead(GuiGraphics graphics) {
    detail(
        graphics,
        Component.translatable(DeathKeys.WHEN),
        new Box(WHEN_X, HEAD_Y, WHEN_WIDTH, LINE));
    detail(
        graphics,
        Component.translatable(DeathKeys.WHERE),
        new Box(WHERE_X, HEAD_Y, whereWidth(), LINE));
  }

  private void renderRow(GuiGraphics graphics, DeathLogPayload.Row row, int shown, Point mouse) {
    int top = FIRST_ROW + shown * ROW_HEIGHT;
    if (shown % 2 == 0) {
      graphics.fill(LEFT, top, right, top + ROW_HEIGHT, ScreenPainter.ROW);
    }
    GameClock clock = GameClock.of(row.dayTime());
    Component when =
        Component.translatable(
            DeathKeys.DAY_TIME, clock.day(), CLOCK.formatted(clock.hour(), clock.minute()));
    text(graphics, when, new Box(WHEN_X, top + FIRST_LINE, WHEN_WIDTH, LINE));
    detail(
        graphics,
        DeathKeys.dimension(row.dimension()),
        new Box(WHEN_X, top + SECOND_LINE, WHEN_WIDTH, LINE));
    Component where =
        Component.translatable(
            DeathKeys.COORDINATES, row.block().getX(), row.block().getY(), row.block().getZ());
    text(graphics, where, new Box(WHERE_X, top + FIRST_LINE, whereWidth(), LINE));
    detail(graphics, row.cause(), new Box(WHERE_X, top + SECOND_LINE, whereWidth(), LINE));
    renderButtons(graphics, row, top, mouse);
  }

  /** Guide turns into Cancel while the Cubit guides to this death (answer 1111). */
  private void renderButtons(GuiGraphics graphics, DeathLogPayload.Row row, int top, Point mouse) {
    boolean guiding = guiding(row);
    FlatButton.paint(
        graphics,
        guideBox(top),
        bold(guiding ? DeathKeys.CANCEL : DeathKeys.GUIDE),
        !guiding && !row.call().ready(),
        guideBox(top).contains((int) mouse.x(), (int) mouse.y()));
    FlatButton.paint(
        graphics,
        deleteBox(top),
        bold(DeathKeys.DELETE),
        false,
        deleteBox(top).contains((int) mouse.x(), (int) mouse.y()));
  }

  /** Sends the press of a button under the point to the server, which checks it again. */
  boolean click(double x, double y) {
    List<DeathLogPayload.Row> rows = rows();
    for (int shown = 0; shown < rowsShown && scroll + shown < rows.size(); shown++) {
      int index = scroll + shown;
      DeathLogPayload.Row row = rows.get(index);
      int top = FIRST_ROW + shown * ROW_HEIGHT;
      if (guideBox(top).contains((int) x, (int) y)) {
        return pressGuide(index, row);
      }
      if (deleteBox(top).contains((int) x, (int) y)) {
        CubitNetwork.deleteDeath(index, row.dayTime());
        return true;
      }
    }
    return false;
  }

  private boolean pressGuide(int index, DeathLogPayload.Row row) {
    boolean guiding = guiding(row);
    if (!guiding && !row.call().ready()) {
      return false;
    }
    CubitNetwork.guideToDeath(menu.tablet().robot(), index, row.dayTime(), !guiding);
    return true;
  }

  /** The wheel moves one row at a time. */
  void scroll(double delta) {
    scroll -= (int) Math.signum(delta);
    clampScroll(rows().size());
  }

  /** The reason of the button under the mouse, as its tooltip. */
  boolean renderDetail(GuiGraphics graphics, Font font, int mouseX, int mouseY, int left, int top) {
    int x = mouseX - left;
    int y = mouseY - top;
    List<DeathLogPayload.Row> rows = rows();
    for (int shown = 0; shown < rowsShown && scroll + shown < rows.size(); shown++) {
      int rowTop = FIRST_ROW + shown * ROW_HEIGHT;
      if (guideBox(rowTop).contains(x, y)) {
        graphics.renderTooltip(font, guideDetail(rows.get(scroll + shown)), mouseX, mouseY);
        return true;
      }
      if (deleteBox(rowTop).contains(x, y)) {
        graphics.renderTooltip(
            font, Component.translatable(DeathKeys.DELETE_DETAIL), mouseX, mouseY);
        return true;
      }
    }
    return false;
  }

  private Component guideDetail(DeathLogPayload.Row row) {
    if (guiding(row)) {
      return Component.translatable(DeathKeys.CANCEL_DETAIL);
    }
    return switch (row.call()) {
      case READY -> Component.translatable(DeathKeys.GUIDE_DETAIL);
      case LOCKED -> lockedDetail();
      case SWITCHED_OFF -> Component.translatable(DeathKeys.SWITCHED_OFF);
      case DEATH_ELSEWHERE -> Component.translatable(DeathKeys.DEATH_ELSEWHERE);
      case CUBIT_ELSEWHERE -> Component.translatable(DeathKeys.CUBIT_ELSEWHERE);
      case NOT_FOLLOWING -> Component.translatable(DeathKeys.NOT_FOLLOWING);
    };
  }

  /** The first tier that guides, from the config, as `Unlocks at Elite` (answer 1117). */
  private static Component lockedDetail() {
    int tier = TabletDeathLog.current().guideTier();
    RobotTier[] tiers = RobotTier.values();
    if (tier < 0 || tier >= tiers.length) {
      return Component.translatable(DeathKeys.SWITCHED_OFF);
    }
    return RobotSwitchLook.unlocksAt(tiers[tier]);
  }

  private static boolean guiding(DeathLogPayload.Row row) {
    return TabletDeathLog.current().guiding() == row.dayTime();
  }

  private void clampScroll(int size) {
    scroll = Math.clamp(scroll, 0, Math.max(0, size - rowsShown));
  }

  private int whereWidth() {
    return guideX() - WHERE_GAP - WHERE_X;
  }

  private Box guideBox(int top) {
    return new Box(guideX(), top + BUTTON_TOP, GUIDE_WIDTH, BUTTON_HEIGHT);
  }

  private Box deleteBox(int top) {
    return new Box(deleteX(), top + BUTTON_TOP, DELETE_WIDTH, BUTTON_HEIGHT);
  }

  private static List<DeathLogPayload.Row> rows() {
    return TabletDeathLog.current().rows();
  }

  private void text(GuiGraphics graphics, Component text, Box box) {
    painter.text(graphics, text, box, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
  }

  private void detail(GuiGraphics graphics, Component text, Box box) {
    painter.text(graphics, text, box, ScreenPainter.DETAIL, ScreenPainter.FULL_SCALE);
  }

  private static Component bold(String key) {
    return Component.translatable(key).withStyle(ChatFormatting.BOLD);
  }

  /** The mouse, in the coordinates of the panel. */
  private record Point(double x, double y) {}
}
