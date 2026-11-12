package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/** The ON and OFF switch of the robot tabs. On: green, knob right. Off: light, knob left. */
final class ScreenToggle {
  static final int WIDTH = 26;
  static final int HEIGHT = 11;
  private static final int KNOB_WIDTH = 10;
  private static final int ON = 0xFF4CAF50;
  private static final int OFF = 0xFFF4F4F4;
  private static final int KNOB_OFF = 0xFFCFCFCF;
  static final int DISABLED = 0x88C6C6C6;

  private ScreenToggle() {}

  static void draw(
      GuiGraphics graphics, ScreenPainter painter, int x, int top, boolean on, boolean available) {
    int right = x + WIDTH;
    ScreenPainter.sunken(graphics, x, top, WIDTH, HEIGHT, on ? ON : OFF);
    int knobX = on ? right - 1 - KNOB_WIDTH : x + 1;
    graphics.fill(
        knobX, top + 1, knobX + KNOB_WIDTH, top + HEIGHT - 1, on ? ScreenPainter.WHITE : KNOB_OFF);
    Component label = Component.translatable(on ? ScreenKeys.SWITCH_ON : ScreenKeys.SWITCH_OFF);
    int labelX = on ? x + 2 : knobX + KNOB_WIDTH + 1;
    int color = on ? ScreenPainter.WHITE : ScreenPainter.DETAIL;
    painter.text(graphics, label, labelX, top + 2, color, ScreenPainter.DETAIL_SCALE);
    if (!available) {
      graphics.fill(x, top, right, top + HEIGHT, DISABLED);
    }
  }

  static boolean contains(int x, int top, double mouseX, double mouseY) {
    return mouseX >= x && mouseX < x + WIDTH && mouseY >= top && mouseY < top + HEIGHT;
  }
}
