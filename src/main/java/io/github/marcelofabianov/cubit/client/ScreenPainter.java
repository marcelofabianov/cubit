package io.github.marcelofabianov.cubit.client;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

/**
 * Draws the parts the screens of the mod share, in the flat style of the concept images. A text
 * that does not fit is cut, and its tooltip shows it whole.
 */
final class ScreenPainter {
  static final int TEXT = 0xFF3F3F3F;
  static final int DETAIL = 0xFF5F5F5F;
  static final int WHITE = 0xFFFFFFFF;
  static final int WINDOW = 0xFFC6C6C6;
  static final int EDGE = 0xFF000000;
  static final int SHADE = 0xFF555555;
  static final int DARK = 0xFF373737;
  static final int SLOT = 0xFF8B8B8B;
  static final int BAR_BACK = 0xFF2A2A2A;
  static final int ORANGE = 0xFFF2B33A;
  static final int GREEN = 0xFF4CAF50;
  static final int RED = 0xFFD23A3A;
  static final int PORTRAIT = 0xFFE9E7E1;
  static final int ROW = 0xFFBDBDBD;
  static final int STATUS_GREEN = 0xFF2E7D32;
  static final int STATUS_BLUE = 0xFF2A8FB0;
  static final int STATUS_ORANGE = 0xFFC27A12;
  static final int STATUS_RED = 0xFFC62828;
  static final int STATUS_GRAY = 0xFF757575;
  static final int STATUS_PURPLE = 0xFF7B4FB0;

  /** Every text of the screens shrinks by the same factor, in proportion (answer 705). */
  static final float TEXT_FACTOR = 0.75f;

  static final float FULL_SCALE = TEXT_FACTOR;
  static final float DETAIL_SCALE = 0.75f * TEXT_FACTOR;
  static final int SLOT_SIZE = 18;
  private static final int SHADOW = 2;
  private static final int HIGHLIGHT = 3;
  private static final int DOT_SIZE = 3;
  private static final int DOT_GAP = 3;
  private static final int DOT_TOP = 1;
  private static final String CUT = "...";
  private static final int PAIR_GAP = 6;

  private final List<Cut> cuts = new ArrayList<>();
  private Font font;

  /** Called at the start of each frame, so the tooltips follow what is drawn now. */
  void begin(Font font) {
    this.font = font;
    cuts.clear();
  }

  static void window(GuiGraphics graphics, int x, int y, int width, int height) {
    graphics.fill(x, y, x + width, y + height, EDGE);
    graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, SHADE);
    graphics.fill(x + 1, y + 1, x + width - HIGHLIGHT, y + height - HIGHLIGHT, WHITE);
    graphics.fill(
        x + HIGHLIGHT, y + HIGHLIGHT, x + width - HIGHLIGHT, y + height - HIGHLIGHT, WINDOW);
  }

  static void sunken(GuiGraphics graphics, int x, int y, int width, int height, int fill) {
    graphics.fill(x, y, x + width, y + height, WHITE);
    graphics.fill(x, y, x + width - 1, y + height - 1, DARK);
    graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, fill);
  }

  static void slot(GuiGraphics graphics, int x, int y) {
    sunken(graphics, x, y, SLOT_SIZE, SLOT_SIZE, SLOT);
  }

  /** A bar shows at least one pixel while anything is left, as the other bars of the mod. */
  static void bar(GuiGraphics graphics, Box box, long value, long max, int color) {
    graphics.fill(box.x(), box.y(), box.right(), box.bottom(), BAR_BACK);
    int inner = box.width() - SHADOW;
    int filled =
        max <= 0 || value <= 0 ? 0 : (int) Math.min(inner, (value * inner + max - 1) / max);
    graphics.fill(box.x() + 1, box.y() + 1, box.x() + 1 + filled, box.bottom() - 1, color);
  }

  /** Draws from the left, cut to the width it may use. */
  void text(GuiGraphics graphics, Component text, Box box, int color, float scale) {
    FormattedCharSequence shown = fit(text, box, scale);
    draw(graphics, shown, box.x(), box.y(), color, scale);
  }

  /** Draws so the text ends at the right side of the box. */
  void textRight(GuiGraphics graphics, Component text, Box box, int color, float scale) {
    FormattedCharSequence shown = fit(text, box, scale);
    int width = Math.round(font.width(shown) * scale);
    draw(graphics, shown, box.right() - width, box.y(), color, scale);
  }

  /** A label on the left and its value on the right of the same line, at full size. */
  void pair(GuiGraphics graphics, Box box, Component label, Component value, Colors colors) {
    text(graphics, label, box, colors.label(), FULL_SCALE);
    int used = Math.min(width(label, FULL_SCALE) + PAIR_GAP, box.width());
    Box room = new Box(box.x() + used, box.y(), box.width() - used, box.height());
    textRight(graphics, value, room, colors.value(), FULL_SCALE);
  }

  /** A status, like "● Charging", ending at the right side of the box. */
  void status(GuiGraphics graphics, Component text, Box box, int color) {
    Box room = new Box(box.x() + DOT_SIZE + DOT_GAP, box.y(), box.width() - DOT_SIZE, box.height());
    FormattedCharSequence shown = fit(text, room, FULL_SCALE);
    int textX = box.right() - Math.round(font.width(shown) * FULL_SCALE);
    draw(graphics, shown, textX, box.y(), color, FULL_SCALE);
    int dotX = textX - DOT_GAP - DOT_SIZE;
    int dotY = box.y() + DOT_TOP;
    graphics.fill(dotX, dotY, dotX + DOT_SIZE, dotY + DOT_SIZE, color);
  }

  void text(GuiGraphics graphics, Component text, int x, int y, int color, float scale) {
    draw(graphics, text.getVisualOrderText(), x, y, color, scale);
  }

  int width(Component text, float scale) {
    return Math.round(font.width(text) * scale);
  }

  /** The tooltip of a cut text under the mouse, if any. */
  void cutTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
    for (Cut cut : cuts) {
      if (cut.box().contains(mouseX, mouseY)) {
        graphics.renderTooltip(font, cut.text(), mouseX, mouseY);
        return;
      }
    }
  }

  private FormattedCharSequence fit(Component text, Box box, float scale) {
    int room = (int) (box.width() / scale);
    if (font.width(text) <= room) {
      return text.getVisualOrderText();
    }
    cuts.add(new Cut(box, text));
    FormattedText kept = font.substrByWidth(text, room - font.width(CUT));
    return Language.getInstance()
        .getVisualOrder(FormattedText.composite(kept, FormattedText.of(CUT)));
  }

  private void draw(
      GuiGraphics graphics, FormattedCharSequence text, int x, int y, int color, float scale) {
    graphics.pose().pushPose();
    graphics.pose().translate(x, y, 0);
    graphics.pose().scale(scale, scale, 1f);
    graphics.drawString(font, text, 0, 0, color, false);
    graphics.pose().popPose();
  }

  /** A rectangle on the screen, in screen coordinates. */
  record Box(int x, int y, int width, int height) {
    int right() {
      return x + width;
    }

    int bottom() {
      return y + height;
    }

    boolean contains(int pointX, int pointY) {
      return pointX >= x && pointX < right() && pointY >= y && pointY < bottom();
    }

    Box moved(int left, int top) {
      return new Box(left + x, top + y, width, height);
    }
  }

  /** The colors of a label and its value. */
  record Colors(int label, int value) {
    static final Colors TITLE_FIRST = new Colors(TEXT, DETAIL);
    static final Colors VALUE_FIRST = new Colors(DETAIL, TEXT);
  }

  private record Cut(Box box, Component text) {}
}
