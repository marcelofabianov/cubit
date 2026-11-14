package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.item.TierLook;
import io.github.marcelofabianov.cubit.menu.DefenseKeys;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;

/**
 * The row of the exclusive slots, above the inventory of the robot: the torch slot, dimmed on a
 * tier without torches, after the energy slot on the Tablet (answers 980, 984 and 1104).
 */
final class RobotExclusiveRow {
  private static final int MARGIN = 8;
  private static final int LINE = 9;
  private static final int TITLE_Y = RobotMenu.EXCLUSIVE_SLOT_Y - LINE;
  private static final int SLOT_SIZE = 18;

  private final ScreenPainter painter;
  private final RobotMenu menu;

  RobotExclusiveRow(ScreenPainter painter, RobotMenu menu) {
    this.painter = painter;
    this.menu = menu;
  }

  /** In the coordinates of the screen, behind the items. */
  void renderFrames(GuiGraphics graphics, int left, int top) {
    int x = left + menu.layout().torchSlotX() - 1;
    int y = top + RobotMenu.EXCLUSIVE_SLOT_Y - 1;
    ScreenPainter.slot(graphics, x, y);
    if (!unlocked()) {
      graphics.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, ScreenToggle.DISABLED);
    }
  }

  /** In the coordinates of the window. */
  void renderLabels(GuiGraphics graphics) {
    Component title =
        Component.translatable(ScreenKeys.EXCLUSIVE_SLOTS).withStyle(ChatFormatting.BOLD);
    painter.text(graphics, title, MARGIN, TITLE_Y, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
  }

  /** An empty torch slot names itself, or the tier that unlocks it, as the energy slot does. */
  boolean renderTooltip(GuiGraphics graphics, Font font, Slot hovered, int mouseX, int mouseY) {
    if (hovered == null || hovered.hasItem()) {
      return false;
    }
    if (hovered.index == menu.energySlotIndex()) {
      graphics.renderTooltip(font, Component.translatable(ScreenKeys.REDSTONE), mouseX, mouseY);
      return true;
    }
    if (hovered.index != menu.torchSlotIndex()) {
      return false;
    }
    Component text =
        unlocked()
            ? Component.translatable(ScreenKeys.TORCHES)
            : menu.torchUnlockTier()
                .map(tier ->
                    (Component) Component.translatable(DefenseKeys.UNLOCKS_AT, TierLook.name(tier)))
                .orElse(Component.translatable(ScreenKeys.TORCHES));
    graphics.renderTooltip(font, text, mouseX, mouseY);
    return true;
  }

  private boolean unlocked() {
    return menu.switchAvailable(RobotMenu.TORCHES_BUTTON);
  }
}
