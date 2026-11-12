package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.menu.RobotMenu;
import java.util.OptionalInt;
import net.minecraft.client.gui.GuiGraphics;

/**
 * The scrollbar beside the robot inventory, only when it has more rows than the screen shows
 * (answer 838). It only draws and reads the mouse, and the menu keeps the row on top.
 */
final class InventoryScrollbar {
  /** What the window grows to fit the bar. */
  static final int EXTRA_WIDTH = 14;

  private static final int TRACK_X = 174;
  private static final int TRACK_WIDTH = 12;
  private static final int TRACK_TOP = RobotMenu.FIRST_SLOT_Y - 1;
  private static final int THUMB_HEIGHT = 15;
  private static final int THUMB_INSET = 1;
  private static final int INVENTORY_LEFT = RobotMenu.FIRST_SLOT_X - 1;
  private static final double HALF = 0.5;

  private final RobotMenu menu;
  private boolean dragging;

  InventoryScrollbar(RobotMenu menu) {
    this.menu = menu;
  }

  boolean shown() {
    return menu.window().scrolls();
  }

  /** Drawn in the coordinates of the screen, with the window at left and top. */
  void render(GuiGraphics graphics, int left, int top) {
    if (!shown()) {
      return;
    }
    int x = left + TRACK_X;
    int y = top + TRACK_TOP;
    ScreenPainter.sunken(graphics, x, y, TRACK_WIDTH, trackHeight(), ScreenPainter.DARK);
    int thumbY = y + thumbOffset();
    graphics.fill(
        x + THUMB_INSET,
        thumbY,
        x + TRACK_WIDTH - THUMB_INSET,
        thumbY + THUMB_HEIGHT,
        ScreenPainter.WINDOW);
    graphics.fill(
        x + THUMB_INSET,
        thumbY + THUMB_HEIGHT - 1,
        x + TRACK_WIDTH - THUMB_INSET,
        thumbY + THUMB_HEIGHT,
        ScreenPainter.SHADE);
  }

  /** A click on the bar starts dragging, and gives the row under the mouse. */
  OptionalInt press(double x, double y) {
    if (!shown() || !onTrack(x, y)) {
      return OptionalInt.empty();
    }
    dragging = true;
    return OptionalInt.of(rowAt(y));
  }

  OptionalInt drag(double y) {
    return dragging ? OptionalInt.of(rowAt(y)) : OptionalInt.empty();
  }

  void release() {
    dragging = false;
  }

  /** The wheel works over the robot inventory and over the bar itself. */
  boolean overInventory(double x, double y) {
    return shown()
        && x >= INVENTORY_LEFT
        && x < TRACK_X + TRACK_WIDTH
        && y >= TRACK_TOP
        && y < TRACK_TOP + trackHeight();
  }

  private boolean onTrack(double x, double y) {
    return x >= TRACK_X
        && x < TRACK_X + TRACK_WIDTH
        && y >= TRACK_TOP
        && y < TRACK_TOP + trackHeight();
  }

  private int trackHeight() {
    return menu.rows() * RobotMenu.SLOT_SIZE;
  }

  private int thumbOffset() {
    int max = menu.window().maxTopRow();
    return max == 0 ? 0 : menu.topRow() * (trackHeight() - THUMB_HEIGHT) / max;
  }

  /** The row whose thumb would be centered under the mouse. */
  private int rowAt(double y) {
    double travel = trackHeight() - THUMB_HEIGHT;
    double share = (y - TRACK_TOP - THUMB_HEIGHT * HALF) / travel;
    return (int) Math.round(Math.clamp(share, 0, 1) * menu.window().maxTopRow());
  }
}
