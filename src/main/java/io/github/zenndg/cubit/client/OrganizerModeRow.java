package io.github.zenndg.cubit.client;

import io.github.zenndg.cubit.client.ScreenPainter.Box;
import io.github.zenndg.cubit.domain.robot.OrganizerMode;
import io.github.zenndg.cubit.menu.OrganizerMenuPart;
import io.github.zenndg.cubit.menu.RobotMenu;
import io.github.zenndg.cubit.menu.WorkKeys;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * The line Mode of the Work tab, with the card of the Organizer, in the place of the switch of the
 * job: the name and a button with the mode, which switches between Organize and Supply (1290).
 */
final class OrganizerModeRow {
  private static final int LINE = 9;
  private static final int GAP = 4;
  private static final int BUTTON_WIDTH = 64;
  private static final int BUTTON_HEIGHT = 14;
  private static final int ROW_HEIGHT = 18;

  private final ScreenPainter painter;
  private final RobotMenu menu;
  private final int left;
  private final int right;
  private final int top;

  OrganizerModeRow(ScreenPainter painter, RobotMenu menu, Box row) {
    this.painter = painter;
    this.menu = menu;
    this.left = row.x();
    this.right = row.x() + row.width();
    this.top = row.y();
  }

  boolean shown() {
    return menu.organizer().mode().isPresent();
  }

  void render(GuiGraphics graphics, double mouseX, double mouseY) {
    Optional<OrganizerMode> mode = menu.organizer().mode();
    if (mode.isEmpty()) {
      return;
    }
    graphics.fill(left, top, right, top + ROW_HEIGHT, ScreenPainter.ROW);
    painter.text(
        graphics,
        Component.translatable(WorkKeys.ORGANIZER_MODE),
        new Box(left + GAP, top + (ROW_HEIGHT - LINE) / 2 + 1, button().x() - left - GAP * 2, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    Component name =
        Component.translatable(WorkKeys.organizerMode(mode.get().key()))
            .withStyle(ChatFormatting.BOLD);
    Box button = button();
    FlatButton.paint(graphics, button, name, false, button.contains((int) mouseX, (int) mouseY));
  }

  OptionalInt buttonAt(double x, double y) {
    return shown() && button().contains((int) x, (int) y)
        ? OptionalInt.of(OrganizerMenuPart.MODE_BUTTON)
        : OptionalInt.empty();
  }

  boolean renderDetail(GuiGraphics graphics, Font font, int mouseX, int mouseY, int y) {
    if (!shown() || y < top || y >= top + ROW_HEIGHT) {
      return false;
    }
    graphics.renderTooltip(
        font, Component.translatable(WorkKeys.ORGANIZER_MODE_DETAIL), mouseX, mouseY);
    return true;
  }

  private Box button() {
    return new Box(
        right - GAP - BUTTON_WIDTH,
        top + (ROW_HEIGHT - BUTTON_HEIGHT) / 2,
        BUTTON_WIDTH,
        BUTTON_HEIGHT);
  }
}
