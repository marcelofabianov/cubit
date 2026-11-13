package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.menu.RobotMenu;
import net.minecraft.client.gui.GuiGraphics;

/** The frames behind the slots of the robot screen, in the coordinates of the screen. */
final class RobotSlotFrames {
  private static final int PLAYER_ROWS = 3;

  private RobotSlotFrames() {}

  /** The visible rows of the inventory of the robot and its energy slot. */
  static void robot(GuiGraphics graphics, RobotMenu menu, int left, int top) {
    int frameX = left + RobotMenu.FIRST_SLOT_X - 1;
    for (int slot = 0; slot < menu.window().visibleSlots(); slot++) {
      ScreenPainter.slot(
          graphics,
          frameX + slot % RobotMenu.SLOTS_PER_ROW * RobotMenu.SLOT_SIZE,
          top + RobotMenu.FIRST_SLOT_Y - 1 + slot / RobotMenu.SLOTS_PER_ROW * RobotMenu.SLOT_SIZE);
    }
    ScreenPainter.slot(
        graphics, left + RobotMenu.ENERGY_SLOT_X - 1, top + RobotMenu.ENERGY_SLOT_Y - 1);
  }

  static void player(GuiGraphics graphics, RobotMenu menu, int left, int top) {
    int frameX = left + RobotMenu.FIRST_SLOT_X - 1;
    int playerTop = top + menu.playerInventoryY() - 1;
    for (int row = 0; row < PLAYER_ROWS; row++) {
      for (int column = 0; column < RobotMenu.SLOTS_PER_ROW; column++) {
        ScreenPainter.slot(
            graphics, frameX + column * RobotMenu.SLOT_SIZE, playerTop + row * RobotMenu.SLOT_SIZE);
      }
    }
    for (int column = 0; column < RobotMenu.SLOTS_PER_ROW; column++) {
      ScreenPainter.slot(
          graphics, frameX + column * RobotMenu.SLOT_SIZE, playerTop + RobotMenu.HOTBAR_GAP);
    }
  }
}
