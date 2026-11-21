package io.github.zenndg.cubit.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.zenndg.cubit.block.DeskRecipes;
import io.github.zenndg.cubit.menu.DeskScreenKeys;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

/**
 * The catalog of the desk: every recipe of the mod, the locked ones dimmed and without their
 * materials, so the player sees all the mod has and what is already open (answer 1441).
 */
final class DeskCatalog {
  static final int X = 8;
  static final int Y = 30;
  static final int COLUMNS = 4;
  static final int ROWS = 4;
  private static final int CELL = ScreenPainter.SLOT_SIZE;
  private static final int ICON_INSET = 1;
  private static final int LOCKED_VEIL = 0x708B8B8B;
  private static final float LOCKED_SHADE = 0.3f;
  private static final int ICON_SIZE = 16;
  private static final int CHOSEN = 0xFF2A8FB0;

  private int firstRow;

  List<RecipeHolder<CraftingRecipe>> recipes() {
    Minecraft minecraft = Minecraft.getInstance();
    return minecraft.level == null
        ? List.of()
        : DeskRecipes.all(minecraft.level.getRecipeManager());
  }

  static boolean unlocked(RecipeHolder<?> recipe) {
    Minecraft minecraft = Minecraft.getInstance();
    return minecraft.player != null && minecraft.player.getRecipeBook().contains(recipe);
  }

  static ItemStack resultOf(RecipeHolder<CraftingRecipe> recipe) {
    Minecraft minecraft = Minecraft.getInstance();
    return minecraft.level == null
        ? ItemStack.EMPTY
        : recipe.value().getResultItem(minecraft.level.registryAccess());
  }

  void render(GuiGraphics graphics, int left, int top, int chosen) {
    List<RecipeHolder<CraftingRecipe>> all = recipes();
    for (int cell = 0; cell < COLUMNS * ROWS; cell++) {
      int index = firstRow * COLUMNS + cell;
      int x = left + X + (cell % COLUMNS) * CELL;
      int y = top + Y + (cell / COLUMNS) * CELL;
      ScreenPainter.slot(graphics, x, y);
      if (index >= all.size()) {
        continue;
      }
      if (index == chosen) {
        graphics.renderOutline(x, y, CELL, CELL, CHOSEN);
      }
      if (unlocked(all.get(index))) {
        graphics.renderItem(resultOf(all.get(index)), x + ICON_INSET, y + ICON_INSET);
      } else {
        renderLocked(graphics, resultOf(all.get(index)), x + ICON_INSET, y + ICON_INSET);
      }
    }
  }

  /**
   * A locked recipe shows its item dark and grey, as switched off, and the unlocked ones show it as
   * it is (answer 1441).
   */
  private static void renderLocked(GuiGraphics graphics, ItemStack item, int x, int y) {
    RenderSystem.setShaderColor(LOCKED_SHADE, LOCKED_SHADE, LOCKED_SHADE, 1f);
    graphics.renderItem(item, x, y);
    graphics.flush();
    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    graphics.fill(x, y, x + ICON_SIZE, y + ICON_SIZE, LOCKED_VEIL);
  }

  /** The recipe under the mouse, as its place in the whole catalog. */
  Optional<Integer> at(double mouseX, double mouseY, int left, int top) {
    int column = (int) Math.floor((mouseX - left - X) / CELL);
    int row = (int) Math.floor((mouseY - top - Y) / CELL);
    if (column < 0 || column >= COLUMNS || row < 0 || row >= ROWS) {
      return Optional.empty();
    }
    int index = (firstRow + row) * COLUMNS + column;
    return index < recipes().size() ? Optional.of(index) : Optional.empty();
  }

  void scroll(double delta) {
    int rows = (recipes().size() + COLUMNS - 1) / COLUMNS;
    int last = Math.max(0, rows - ROWS);
    firstRow = Math.max(0, Math.min(last, firstRow - (int) Math.signum(delta)));
  }

  /** The name of the item, and Locked under it, without the materials (answer 1441). */
  void tooltip(GuiGraphics graphics, Font font, int index, int mouseX, int mouseY) {
    RecipeHolder<CraftingRecipe> recipe = recipes().get(index);
    Component name = resultOf(recipe).getHoverName();
    if (unlocked(recipe)) {
      graphics.renderTooltip(font, name, mouseX, mouseY);
      return;
    }
    graphics.renderComponentTooltip(
        font,
        List.of(name, Component.translatable(DeskScreenKeys.LOCKED).withStyle(ChatFormatting.RED)),
        mouseX,
        mouseY);
  }
}
