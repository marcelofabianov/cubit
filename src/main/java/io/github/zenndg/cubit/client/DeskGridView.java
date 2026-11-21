package io.github.zenndg.cubit.client;

import io.github.zenndg.cubit.block.DeskGrid;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * The 3x3 grid of the desk. It shows the marks of the chosen recipe, or the ones the player puts by
 * hand, never real items (answer 1426).
 */
final class DeskGridView {
  static final int X = 96;
  static final int Y = 30;
  private static final int CELL = ScreenPainter.SLOT_SIZE;
  private static final int ICON_INSET = 1;
  private static final int MARK_VEIL = 0x60C6C6C6;
  private static final int CYCLE_TICKS = 20;

  void render(
      GuiGraphics graphics,
      int left,
      int top,
      List<Ingredient> recipe,
      NonNullList<ItemStack> hand,
      long tick) {
    for (int cell = 0; cell < DeskGrid.CELLS; cell++) {
      int x = left + X + (cell % DeskGrid.SIDE) * CELL;
      int y = top + Y + (cell / DeskGrid.SIDE) * CELL;
      ScreenPainter.slot(graphics, x, y);
      ItemStack mark = recipe.isEmpty() ? hand.get(cell) : shown(recipe.get(cell), tick);
      if (!mark.isEmpty()) {
        graphics.renderFakeItem(mark, x + ICON_INSET, y + ICON_INSET);
        graphics.fill(x + ICON_INSET, y + ICON_INSET, x + CELL - 1, y + CELL - 1, MARK_VEIL);
      }
    }
  }

  /** An ingredient with many items shows each in turn. */
  private static ItemStack shown(Ingredient ingredient, long tick) {
    ItemStack[] items = ingredient.getItems();
    return items.length == 0 ? ItemStack.EMPTY : items[(int) (tick / CYCLE_TICKS % items.length)];
  }

  Optional<Integer> at(double mouseX, double mouseY, int left, int top) {
    int column = (int) Math.floor((mouseX - left - X) / CELL);
    int row = (int) Math.floor((mouseY - top - Y) / CELL);
    boolean inside = column >= 0 && column < DeskGrid.SIDE && row >= 0 && row < DeskGrid.SIDE;
    return inside ? Optional.of(row * DeskGrid.SIDE + column) : Optional.empty();
  }
}
