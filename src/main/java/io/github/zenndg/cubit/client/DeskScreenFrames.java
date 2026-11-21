package io.github.zenndg.cubit.client;

import io.github.zenndg.cubit.menu.DeskScreenKeys;
import io.github.zenndg.cubit.menu.EngineeringDeskMenu;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/** The slot frames of the desk screen: the inventories of its tier and the one of the player. */
final class DeskScreenFrames {
  private static final int PLAYER_X = 47;
  private static final int ROWS = 3;
  private static final int COLUMNS = 9;
  private static final int HOTBAR_GAP = 58;

  private DeskScreenFrames() {}

  static void player(GuiGraphics graphics, int left, int top) {
    int firstRow = top + EngineeringDeskMenu.PLAYER_INVENTORY_Y - 1;
    for (int row = 0; row < ROWS; row++) {
      for (int column = 0; column < COLUMNS; column++) {
        ScreenPainter.slot(
            graphics,
            left + PLAYER_X + column * ScreenPainter.SLOT_SIZE,
            firstRow + row * ScreenPainter.SLOT_SIZE);
      }
    }
    for (int column = 0; column < COLUMNS; column++) {
      ScreenPainter.slot(
          graphics, left + PLAYER_X + column * ScreenPainter.SLOT_SIZE, firstRow + HOTBAR_GAP);
    }
  }

  /** The input on the left and the output on the right, as on the desk (answer 1473). */
  static void storage(GuiGraphics graphics, int left, int top, int slots) {
    for (int index = 0; index < slots; index++) {
      int column = index % EngineeringDeskMenu.STORAGE_COLUMNS;
      int row = index / EngineeringDeskMenu.STORAGE_COLUMNS;
      int y = top + EngineeringDeskMenu.STORAGE_Y - 1 + row * ScreenPainter.SLOT_SIZE;
      int offset = column * ScreenPainter.SLOT_SIZE - 1;
      ScreenPainter.slot(graphics, left + EngineeringDeskMenu.INPUT_X + offset, y);
      ScreenPainter.slot(graphics, left + EngineeringDeskMenu.OUTPUT_X + offset, y);
    }
  }

  static void storageLabels(GuiGraphics graphics, Font font, int y) {
    graphics.drawString(
        font,
        Component.translatable(DeskScreenKeys.INPUT),
        EngineeringDeskMenu.INPUT_X,
        y,
        ScreenPainter.TEXT,
        false);
    graphics.drawString(
        font,
        Component.translatable(DeskScreenKeys.OUTPUT),
        EngineeringDeskMenu.OUTPUT_X,
        y,
        ScreenPainter.TEXT,
        false);
  }
}
