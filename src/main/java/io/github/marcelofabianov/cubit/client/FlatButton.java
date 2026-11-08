package io.github.marcelofabianov.cubit.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

/** The flat blue button of the concept images. A muted button is grey, but still works. */
final class FlatButton extends AbstractButton {
  private static final int BLUE = 0xFF5B8DEF;
  private static final int BLUE_LOWER = 0xFF4B74C4;
  private static final int GREY = 0xFF9C9C9C;
  private static final int GREY_LOWER = 0xFF7E7E7E;
  private static final int HOVER = 0x22FFFFFF;
  private static final int LOWER_HEIGHT = 3;
  private static final int TEXT_HEIGHT = 8;

  private final Runnable action;
  private boolean muted;

  FlatButton(int x, int y, int width, int height, Component message, Runnable action) {
    super(x, y, width, height, message);
    this.action = action;
  }

  void setMuted(boolean muted) {
    this.muted = muted;
  }

  @Override
  public void onPress() {
    action.run();
  }

  @Override
  protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    int left = getX();
    int top = getY();
    graphics.fill(left, top, left + width, top + height, ScreenPainter.EDGE);
    graphics.fill(
        left + 1, top + 1, left + width - 1, top + height - 1, muted ? GREY_LOWER : BLUE_LOWER);
    graphics.fill(
        left + 1, top + 1, left + width - 1, top + height - LOWER_HEIGHT, muted ? GREY : BLUE);
    if (isHoveredOrFocused()) {
      graphics.fill(left + 1, top + 1, left + width - 1, top + height - 1, HOVER);
    }
    var font = Minecraft.getInstance().font;
    int textX = left + (width - font.width(getMessage())) / 2;
    int textY = top + (height - TEXT_HEIGHT) / 2;
    graphics.drawString(font, getMessage(), textX, textY, ScreenPainter.WHITE, false);
  }

  @Override
  protected void updateWidgetNarration(NarrationElementOutput output) {
    defaultButtonNarrationText(output);
  }
}
