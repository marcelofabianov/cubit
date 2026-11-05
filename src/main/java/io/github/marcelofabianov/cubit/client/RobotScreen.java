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

  @Override
  protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
    int left = (width - imageWidth) / 2;
    int top = (height - imageHeight) / 2;
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
