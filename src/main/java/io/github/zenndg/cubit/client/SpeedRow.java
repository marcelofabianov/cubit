package io.github.zenndg.cubit.client;

import io.github.zenndg.cubit.client.ScreenPainter.Box;
import io.github.zenndg.cubit.menu.RobotMenu;
import io.github.zenndg.cubit.menu.ScreenKeys;
import io.github.zenndg.cubit.menu.SpeedButtons;
import java.util.OptionalInt;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * The speed row, the last of the switches of the Settings tab and of the Resources tab of the
 * Tablet: the speedometer, the name and, in place of ON and OFF, −, the percentage and + (answers
 * 1269 to 1271). The buttons always show, and the server stops at both ends, as in the interval.
 */
final class SpeedRow {
  private static final int LINE = 9;
  private static final int GAP = 3;
  private static final int BUTTON_SIZE = 12;
  private static final int VALUE_WIDTH = 24;
  private static final String DOWN = "−";
  private static final String UP = "+";

  private final ScreenPainter painter;
  private final RobotMenu menu;
  private final Layout layout;

  /** Where the row goes in its panel, which also tells whether the detail shows in the line. */
  record Layout(
      int left,
      int right,
      int height,
      int iconX,
      int iconTop,
      float iconScale,
      int nameX,
      int nameTop,
      boolean detailInLine) {}

  SpeedRow(ScreenPainter painter, RobotMenu menu, Layout layout) {
    this.painter = painter;
    this.menu = menu;
    this.layout = layout;
  }

  void render(GuiGraphics graphics, int top, boolean striped) {
    if (striped) {
      graphics.fill(layout.left(), top, layout.right(), top + layout.height(), ScreenPainter.ROW);
    }
    graphics.pose().pushPose();
    graphics.pose().translate(layout.iconX(), top + layout.iconTop(), 0);
    graphics.pose().scale(layout.iconScale(), layout.iconScale(), 1);
    ScreenSprites.speed(graphics, 0, 0);
    graphics.pose().popPose();
    Component detail = layout.detailInLine() ? detail() : null;
    SwitchRowLabel.draw(
        painter,
        graphics,
        new SwitchRowLabel.Text(Component.translatable(ScreenKeys.SPEED), detail),
        layout.nameX(),
        top + layout.nameTop(),
        downX() - GAP);
    Component value =
        Component.translatable(ScreenKeys.SPEED_VALUE, menu.speedPercent())
            .withStyle(ChatFormatting.BOLD);
    painter.text(
        graphics,
        value,
        new Box(downX() + BUTTON_SIZE + GAP, textY(top), VALUE_WIDTH, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    FlatButton.paint(graphics, down(top), Component.literal(DOWN), false, false);
    FlatButton.paint(graphics, up(top), Component.literal(UP), false, false);
  }

  OptionalInt buttonAt(double x, double y, int top) {
    if (down(top).contains((int) x, (int) y)) {
      return OptionalInt.of(SpeedButtons.DOWN_BUTTON);
    }
    return up(top).contains((int) x, (int) y)
        ? OptionalInt.of(SpeedButtons.UP_BUTTON)
        : OptionalInt.empty();
  }

  /** The range, with the lowest percentage of the config, and that slower costs more (1271). */
  Component detail() {
    return Component.translatable(ScreenKeys.SPEED_DETAIL, menu.speedMinPercent());
  }

  private int textY(int top) {
    return top + (layout.height() - LINE) / 2 + 1;
  }

  private int downX() {
    return layout.right() - GAP - BUTTON_SIZE * 2 - VALUE_WIDTH - GAP * 2;
  }

  private Box down(int top) {
    return new Box(downX(), buttonY(top), BUTTON_SIZE, BUTTON_SIZE);
  }

  private Box up(int top) {
    return new Box(layout.right() - GAP - BUTTON_SIZE, buttonY(top), BUTTON_SIZE, BUTTON_SIZE);
  }

  private int buttonY(int top) {
    return top + (layout.height() - BUTTON_SIZE) / 2;
  }
}
