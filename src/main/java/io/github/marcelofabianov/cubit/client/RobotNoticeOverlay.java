package io.github.marcelofabianov.cubit.client;

import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

/**
 * Draws the notices of the robot just above what sits on the hotbar. The game draws its own message
 * at a fixed size and height, so the mod draws these. The numbers are only visual, so they stay
 * here and not in the config.
 */
final class RobotNoticeOverlay {
  private static final float FONT_SCALE = 0.75f;
  private static final int GAP_ABOVE_BARS = 3;
  private static final long SHOWN_MILLIS = 3000;
  private static final long FADE_MILLIS = 500;
  private static final int WHITE = 0xFFFFFF;
  private static final int OPAQUE = 255;

  /** Below this alpha the game draws the text as fully opaque. */
  private static final int MIN_ALPHA = 8;

  private static Component text;
  private static long shownAt;

  private RobotNoticeOverlay() {}

  static void show(Component notice) {
    text = notice;
    shownAt = Util.getMillis();
  }

  static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
    Minecraft minecraft = Minecraft.getInstance();
    if (text == null || minecraft.options.hideGui) {
      return;
    }
    long left = SHOWN_MILLIS - (Util.getMillis() - shownAt);
    if (left <= 0) {
      text = null;
      return;
    }
    int alpha = (int) Math.min(OPAQUE, left * OPAQUE / FADE_MILLIS);
    if (alpha > MIN_ALPHA) {
      draw(graphics, minecraft, alpha);
    }
  }

  private static void draw(GuiGraphics graphics, Minecraft minecraft, int alpha) {
    Font font = minecraft.font;
    int width = font.width(text);
    int bars = Math.max(minecraft.gui.leftHeight, minecraft.gui.rightHeight);
    graphics.pose().pushPose();
    graphics
        .pose()
        .translate(graphics.guiWidth() / 2f, graphics.guiHeight() - bars - GAP_ABOVE_BARS, 0f);
    graphics.pose().scale(FONT_SCALE, FONT_SCALE, 1f);
    graphics.drawStringWithBackdrop(
        font, text, -width / 2, -font.lineHeight, width, FastColor.ARGB32.color(alpha, WHITE));
    graphics.pose().popPose();
  }
}
