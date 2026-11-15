package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.client.ScreenPainter.Box;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.menu.WorkKeys;
import java.util.OptionalInt;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * The Work tab, in the screen of the Cubit and in the one of the Tablet: the card in the slot, the
 * job, the state with the reason to hibernate, and Resume, active only while the Cubit hibernates
 * away from the Dock (answers 1167 and 1168). The mode and the filters come with each job.
 */
final class RobotWorkPanel {
  private static final int LEFT = 7;
  private static final int LINE = 9;
  private static final int CARD_Y = 20;
  private static final int ICON_X = 9;
  private static final int NAME_X = 30;
  private static final int NAME_TOP = 4;
  private static final int ROW_HEIGHT = 18;
  private static final int STATE_Y = 44;
  private static final int BUTTON_Y = 58;
  private static final int BUTTON_WIDTH = 64;
  private static final int BUTTON_HEIGHT = 14;

  private final ScreenPainter painter;
  private final RobotMenu menu;
  private final int right;

  RobotWorkPanel(ScreenPainter painter, RobotMenu menu, int right) {
    this.painter = painter;
    this.menu = menu;
    this.right = right;
  }

  /** Drawn in the coordinates of the panel, with the mouse in the same ones. */
  void render(GuiGraphics graphics, double mouseX, double mouseY) {
    graphics.fill(LEFT, CARD_Y, right, CARD_Y + ROW_HEIGHT, ScreenPainter.ROW);
    ItemStack card = menu.getSlot(menu.cardSlotIndex()).getItem();
    if (!card.isEmpty()) {
      graphics.renderItem(card, ICON_X, CARD_Y + 1);
    }
    Component job = RobotTexts.jobName(menu.job()).copy().withStyle(ChatFormatting.BOLD);
    painter.text(
        graphics,
        job,
        new Box(NAME_X, CARD_Y + NAME_TOP, right - NAME_X, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    painter.status(
        graphics,
        RobotTexts.status(menu),
        new Box(LEFT, STATE_Y, right - LEFT, LINE),
        RobotTexts.statusColor(menu.status()));
    Box button = button();
    FlatButton.paint(
        graphics,
        button,
        Component.translatable(WorkKeys.RESUME).withStyle(ChatFormatting.BOLD),
        !asleep(),
        button.contains((int) mouseX, (int) mouseY));
  }

  /** Resume under the point, only while the Cubit hibernates away from the Dock. */
  OptionalInt buttonAt(double x, double y) {
    return asleep() && button().contains((int) x, (int) y)
        ? OptionalInt.of(RobotMenu.RESUME_BUTTON)
        : OptionalInt.empty();
  }

  /** What Resume does, or why it waits, as its tooltip. */
  boolean renderDetail(GuiGraphics graphics, Font font, int mouseX, int mouseY, int left, int top) {
    if (!button().contains(mouseX - left, mouseY - top)) {
      return false;
    }
    String key = asleep() ? WorkKeys.RESUME_DETAIL : WorkKeys.RESUME_ONLY_ASLEEP;
    graphics.renderTooltip(font, Component.translatable(key), mouseX, mouseY);
    return true;
  }

  private boolean asleep() {
    return menu.hibernationReason().isPresent();
  }

  private static Box button() {
    return new Box(LEFT, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
  }
}
