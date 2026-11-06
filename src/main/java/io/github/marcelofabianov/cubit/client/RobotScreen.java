package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.menu.RobotMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/** Draws the robot inventory with the chest texture of the game, as many rows as it needs. */
final class RobotScreen extends AbstractContainerScreen<RobotMenu> {
  private static final String CHEST_TEXTURE_PATH = "textures/gui/container/generic_54.png";
  private static final ResourceLocation CHEST_TEXTURE =
      ResourceLocation.withDefaultNamespace(CHEST_TEXTURE_PATH);
  private static final int ROW_HEIGHT = 18;
  private static final int TOP_HEIGHT = 17;
  private static final int BASE_HEIGHT = 114;
  private static final int PLAYER_PANEL_HEIGHT = 96;
  private static final int PLAYER_PANEL_TEXTURE_Y = 126;
  private static final int TITLE_AREA = 94;
  private static final int PANEL_WIDTH = 26;
  private static final int PANEL_HEIGHT = 70;
  private static final int PANEL_COLOR = 0xFFC6C6C6;
  private static final int SLOT_COLOR = 0xFF8B8B8B;
  private static final int BAR_COLOR = 0xFF55FF55;
  private static final int BAR_BACK_COLOR = 0xFF373737;
  private static final int SLOT_Y = 18;
  private static final int SLOT_SIZE = 18;
  private static final int BAR_TOP = 40;
  private static final int BAR_HEIGHT = 24;
  private static final int BAR_X = -14;
  private static final int HEALTH_BAR_X = -22;
  private static final int HEALTH_BAR_COLOR = 0xFFFF5555;
  private static final int BAR_WIDTH = 6;
  private static final int PERCENT = 100;

  RobotScreen(RobotMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    imageHeight = BASE_HEIGHT + menu.rows() * ROW_HEIGHT;
    inventoryLabelY = imageHeight - TITLE_AREA;
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    super.render(graphics, mouseX, mouseY, partialTick);
    renderTooltip(graphics, mouseX, mouseY);
  }

  /** The side panel is outside the chest texture, and a click there must not throw the item. */
  @Override
  protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top, int button) {
    boolean onPanel =
        mouseX >= left - PANEL_WIDTH
            && mouseX < left
            && mouseY >= top
            && mouseY < top + PANEL_HEIGHT;
    return !onPanel && super.hasClickedOutside(mouseX, mouseY, left, top, button);
  }

  private void renderEnergyPanel(GuiGraphics graphics, int left, int top) {
    graphics.fill(left - PANEL_WIDTH, top, left, top + PANEL_HEIGHT, PANEL_COLOR);
    int slotX = left + RobotMenu.ENERGY_SLOT_X - 1;
    int slotY = top + SLOT_Y - 1;
    graphics.fill(slotX, slotY, slotX + SLOT_SIZE, slotY + SLOT_SIZE, SLOT_COLOR);
    renderBar(graphics, left + HEALTH_BAR_X, top, menu.healthPercent(), HEALTH_BAR_COLOR);
    renderBar(graphics, left + BAR_X, top, menu.batteryPercent(), BAR_COLOR);
  }

  private void renderBar(GuiGraphics graphics, int barX, int top, int percent, int color) {
    int barBottom = top + BAR_TOP + BAR_HEIGHT;
    graphics.fill(barX, top + BAR_TOP, barX + BAR_WIDTH, barBottom, BAR_BACK_COLOR);
    // Rounds up, because 1% is less than one pixel and the bar must still show something.
    int filled = (BAR_HEIGHT * percent + PERCENT - 1) / PERCENT;
    graphics.fill(barX, barBottom - filled, barX + BAR_WIDTH, barBottom, color);
  }

  @Override
  protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
    int left = (width - imageWidth) / 2;
    int top = (height - imageHeight) / 2;
    renderEnergyPanel(graphics, left, top);
    int slotsHeight = menu.rows() * ROW_HEIGHT + TOP_HEIGHT;
    graphics.blit(CHEST_TEXTURE, left, top, 0, 0, imageWidth, slotsHeight);
    graphics.blit(
        CHEST_TEXTURE,
        left,
        top + slotsHeight,
        0,
        PLAYER_PANEL_TEXTURE_Y,
        imageWidth,
        PLAYER_PANEL_HEIGHT);
  }
}
