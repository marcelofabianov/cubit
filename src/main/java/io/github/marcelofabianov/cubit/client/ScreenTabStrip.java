package io.github.marcelofabianov.cubit.client;

import net.minecraft.client.gui.GuiGraphics;

/**
 * The look of the tabs on the right of a window, shared by the robot and the dock screens, so both
 * follow one pattern (answer 960). Each tab is given by the top of its row.
 */
final class ScreenTabStrip {
  static final int INSET = 3;
  static final int ICON_X = 5;
  static final int ICON_Y = 4;
  private static final int WIDTH = 26;
  private static final int HEIGHT = 24;
  private static final int IDLE = 0xFFA9A9A9;

  private ScreenTabStrip() {}

  /** Behind the window, so the window border runs under the closed tabs. */
  static void renderBehind(GuiGraphics graphics, int left, int top, int[] tabTops, int openTop) {
    for (int tabTop : tabTops) {
      int y = top + tabTop;
      graphics.fill(left, y, left + WIDTH, y + HEIGHT, ScreenPainter.EDGE);
      graphics.fill(
          left,
          y + 1,
          left + WIDTH - 1,
          y + HEIGHT - 1,
          tabTop == openTop ? ScreenPainter.WINDOW : IDLE);
    }
  }

  /** The open tab covers the border of the window, so both look like one piece. */
  static void renderOpen(GuiGraphics graphics, int left, int top, int openTop) {
    int y = top + openTop;
    graphics.fill(left, y + 1, left + WIDTH - 1, y + HEIGHT - 1, ScreenPainter.WINDOW);
  }

  /** A point given from the left of the tabs and the top of the window. */
  static boolean contains(double x, double y, int tabTop) {
    return x >= 0 && x < WIDTH && y >= tabTop && y < tabTop + HEIGHT;
  }
}
