package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.item.TabletAppLook;
import io.github.marcelofabianov.cubit.item.TabletItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;

/**
 * A thin strip in the color of the active app at the bottom of the Tablet, in the hotbar and the
 * inventories, where the durability bar of a tool goes (answer 1084). Drawn, without a texture.
 */
final class TabletAppStrip implements IItemDecorator {
  private static final int LEFT = 2;
  private static final int TOP = 13;
  private static final int WIDTH = 13;
  private static final int HEIGHT = 2;
  private static final int SHADOW = 0xFF000000;
  private static final int OPAQUE = 0xFF000000;

  /** Over the item, below the count of the stack, as the bar of the game. */
  private static final int Z = 190;

  @Override
  public boolean render(GuiGraphics graphics, Font font, ItemStack stack, int x, int y) {
    int color = OPAQUE | TabletAppLook.color(TabletItem.appOf(stack));
    graphics.pose().pushPose();
    graphics.pose().translate(0, 0, Z);
    graphics.fill(x + LEFT, y + TOP, x + LEFT + WIDTH, y + TOP + HEIGHT, SHADOW);
    graphics.fill(x + LEFT, y + TOP, x + LEFT + WIDTH, y + TOP + 1, color);
    graphics.pose().popPose();
    return false;
  }
}
