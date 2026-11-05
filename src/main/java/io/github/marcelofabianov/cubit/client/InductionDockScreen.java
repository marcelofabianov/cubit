package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.menu.InductionDockMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/** A simple screen: one slot and one energy bar. The final look comes with the Fase 3 screens. */
final class InductionDockScreen extends AbstractContainerScreen<InductionDockMenu> {
  private static final String CHEST_TEXTURE_PATH = "textures/gui/container/generic_54.png";
  private static final ResourceLocation CHEST_TEXTURE =
      ResourceLocation.withDefaultNamespace(CHEST_TEXTURE_PATH);
  private static final int PLAYER_PANEL_HEIGHT = 96;
  private static final int PLAYER_PANEL_TEXTURE_Y = 126;
  private static final int TOP_HEIGHT = 40;
  private static final int LABEL_MARGIN = 94;
  private static final int PANEL_COLOR = 0xFFC6C6C6;
  private static final int SLOT_COLOR = 0xFF8B8B8B;
  private static final int BAR_COLOR = 0xFF55FF55;
  private static final int BAR_BACK_COLOR = 0xFF373737;
  private static final int SLOT_SIZE = 18;
  private static final int BAR_X = 104;
  private static final int BAR_Y = 24;
  private static final int BAR_WIDTH = 60;
  private static final int BAR_HEIGHT = 8;

  InductionDockScreen(InductionDockMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    imageHeight = TOP_HEIGHT + PLAYER_PANEL_HEIGHT;
    inventoryLabelY = imageHeight - LABEL_MARGIN;
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
    graphics.fill(left, top, left + imageWidth, top + TOP_HEIGHT, PANEL_COLOR);
    int slotX = left + InductionDockMenu.SLOT_X - 1;
    int slotY = top + InductionDockMenu.SLOT_Y - 1;
    graphics.fill(slotX, slotY, slotX + SLOT_SIZE, slotY + SLOT_SIZE, SLOT_COLOR);
    int barX = left + BAR_X;
    int barY = top + BAR_Y;
    graphics.fill(barX, barY, barX + BAR_WIDTH, barY + BAR_HEIGHT, BAR_BACK_COLOR);
    // Rounds up, because 1% is less than one pixel and the bar must still show some energy.
    int filled = (BAR_WIDTH * menu.energyPercent() + Percentage.MAX - 1) / Percentage.MAX;
    graphics.fill(barX, barY, barX + filled, barY + BAR_HEIGHT, BAR_COLOR);
    graphics.blit(
        CHEST_TEXTURE,
        left,
        top + TOP_HEIGHT,
        0,
        PLAYER_PANEL_TEXTURE_Y,
        imageWidth,
        PLAYER_PANEL_HEIGHT);
  }
}
