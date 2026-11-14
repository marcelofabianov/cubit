package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.client.ScreenPainter.Box;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * The label of a row with a switch: the name, and after it the detail, in smaller text, while there
 * is room before the switch, so a wider window shows what the narrow one keeps in the tooltip
 * (answers 1102 and 1103). A detail that does not fit is cut, with the whole of it in its tooltip.
 */
final class SwitchRowLabel {
  private static final int LINE = 9;
  private static final int DETAIL_LINE = 7;
  private static final int GAP = 6;
  private static final int DETAIL_DROP = 1;

  /** Narrower than this, the detail would only be a few letters, so it stays in the tooltip. */
  private static final int MIN_DETAIL_WIDTH = 40;

  private SwitchRowLabel() {}

  static void draw(
      ScreenPainter painter, GuiGraphics graphics, Text text, int x, int top, int end) {
    int nameWidth = Math.min(painter.width(text.name(), ScreenPainter.FULL_SCALE), end - x);
    painter.text(
        graphics,
        text.name(),
        new Box(x, top, Math.max(nameWidth, 0), LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    int detailX = x + nameWidth + GAP;
    if (text.detail() != null && end - detailX >= MIN_DETAIL_WIDTH) {
      painter.text(
          graphics,
          text.detail(),
          new Box(detailX, top + DETAIL_DROP, end - detailX, DETAIL_LINE),
          ScreenPainter.DETAIL,
          ScreenPainter.DETAIL_SCALE);
    }
  }

  /** The name of the switch and its detail, which may be absent, as for a locked row. */
  record Text(Component name, Component detail) {}
}
