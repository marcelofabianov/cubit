package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.client.ScreenPainter.Box;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.menu.WorkKeys;
import java.util.OptionalInt;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * The row of the unload interval in the Work tab, with or without a card: the minutes that hold
 * now, or only when full, between the buttons − and + (answers 1250 to 1253).
 */
final class UnloadIntervalRow {
  private static final int LEFT = 7;
  private static final int ROW_Y = 100;
  private static final int ROW_HEIGHT = 18;
  private static final int LINE = 9;
  private static final int GAP = 4;
  private static final int BUTTON_SIZE = 12;
  private static final int VALUE_WIDTH = 56;
  private static final String DOWN = "−";
  private static final String UP = "+";

  private final ScreenPainter painter;
  private final RobotMenu menu;
  private final int right;

  UnloadIntervalRow(ScreenPainter painter, RobotMenu menu, int right) {
    this.painter = painter;
    this.menu = menu;
    this.right = right;
  }

  void render(GuiGraphics graphics, double mouseX, double mouseY) {
    graphics.fill(LEFT, ROW_Y, right, ROW_Y + ROW_HEIGHT, ScreenPainter.ROW);
    int textY = ROW_Y + (ROW_HEIGHT - LINE) / 2;
    painter.text(
        graphics,
        Component.translatable(WorkKeys.UNLOAD_INTERVAL),
        new Box(LEFT + GAP, textY, valueX() - LEFT - GAP * 2, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    painter.text(
        graphics,
        value().copy().withStyle(ChatFormatting.BOLD),
        new Box(valueX(), textY, VALUE_WIDTH, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    paintButton(graphics, down(), DOWN, mouseX, mouseY);
    paintButton(graphics, up(), UP, mouseX, mouseY);
  }

  OptionalInt buttonAt(double x, double y) {
    if (down().contains((int) x, (int) y)) {
      return OptionalInt.of(RobotMenu.UNLOAD_INTERVAL_DOWN_BUTTON);
    }
    return up().contains((int) x, (int) y)
        ? OptionalInt.of(RobotMenu.UNLOAD_INTERVAL_UP_BUTTON)
        : OptionalInt.empty();
  }

  boolean renderDetail(GuiGraphics graphics, Font font, int mouseX, int mouseY, int y) {
    if (y < ROW_Y || y >= ROW_Y + ROW_HEIGHT) {
      return false;
    }
    graphics.renderTooltip(
        font, Component.translatable(WorkKeys.UNLOAD_INTERVAL_DETAIL), mouseX, mouseY);
    return true;
  }

  private Component value() {
    int minutes = menu.unloadIntervalMinutes();
    return minutes == 0
        ? Component.translatable(WorkKeys.ONLY_WHEN_FULL)
        : Component.translatable(WorkKeys.UNLOAD_MINUTES, minutes);
  }

  private void paintButton(GuiGraphics graphics, Box box, String label, double x, double y) {
    FlatButton.paint(
        graphics, box, Component.literal(label), false, box.contains((int) x, (int) y));
  }

  private int valueX() {
    return down().x() + BUTTON_SIZE + GAP;
  }

  private Box down() {
    return new Box(
        right - GAP - BUTTON_SIZE * 2 - VALUE_WIDTH - GAP * 2, buttonY(), BUTTON_SIZE, BUTTON_SIZE);
  }

  private Box up() {
    return new Box(right - GAP - BUTTON_SIZE, buttonY(), BUTTON_SIZE, BUTTON_SIZE);
  }

  private static int buttonY() {
    return ROW_Y + (ROW_HEIGHT - BUTTON_SIZE) / 2;
  }
}
