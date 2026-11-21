package io.github.zenndg.cubit.menu;

import io.github.zenndg.cubit.block.DeskGrid;
import io.github.zenndg.cubit.block.DeskRecipes;
import io.github.zenndg.cubit.block.EngineeringDeskBlockEntity;
import io.github.zenndg.cubit.domain.desk.DeskOrder;
import io.github.zenndg.cubit.registry.CubitRecipes;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

/**
 * The buttons of the desk screen, read on the server (rules 19 and 20): the order, the recipe of
 * the catalog, and the marks the player puts by hand in the grid (answers 1426 and 1444). A locked
 * recipe is never chosen (answer 1441).
 */
public final class DeskControl {
  public static final int MINUS = 0;
  public static final int PLUS = 1;
  public static final int MINUS_SHIFT = 2;
  public static final int PLUS_SHIFT = 3;
  public static final int CANCEL = 4;
  public static final int CHOOSE_FIRST = 1_000;
  public static final int MARK_FIRST = 2_000;

  private DeskControl() {}

  public static int choose(int recipeIndex) {
    return CHOOSE_FIRST + recipeIndex;
  }

  public static int mark(int cell) {
    return MARK_FIRST + cell;
  }

  static boolean click(
      EngineeringDeskBlockEntity desk, ServerPlayer player, NonNullList<ItemStack> marks, int id) {
    switch (id) {
      case MINUS -> desk.adjustOrder(-DeskOrder.STEP);
      case PLUS -> desk.adjustOrder(DeskOrder.STEP);
      case MINUS_SHIFT -> desk.adjustOrder(-DeskOrder.SHIFT_STEP);
      case PLUS_SHIFT -> desk.adjustOrder(DeskOrder.SHIFT_STEP);
      case CANCEL -> desk.cancelOrder();
      default -> {
        return chooseOrMark(desk, player, marks, id);
      }
    }
    return true;
  }

  private static boolean chooseOrMark(
      EngineeringDeskBlockEntity desk, ServerPlayer player, NonNullList<ItemStack> marks, int id) {
    RecipeManager recipes = player.serverLevel().getRecipeManager();
    if (id >= MARK_FIRST && id < MARK_FIRST + DeskGrid.CELLS) {
      marks.set(id - MARK_FIRST, player.containerMenu.getCarried().copyWithCount(1));
      recognize(desk, player, marks, recipes);
      return true;
    }
    List<RecipeHolder<CraftingRecipe>> all = DeskRecipes.all(recipes);
    int index = id - CHOOSE_FIRST;
    if (index < 0 || index >= all.size()) {
      return false;
    }
    return pick(desk, player, all.get(index));
  }

  /** Marks that make an unlocked recipe choose it, as the crafting table would (answer 1426). */
  private static void recognize(
      EngineeringDeskBlockEntity desk,
      ServerPlayer player,
      NonNullList<ItemStack> marks,
      RecipeManager recipes) {
    CraftingInput input = CraftingInput.of(DeskGrid.SIDE, DeskGrid.SIDE, marks);
    recipes
        .getRecipeFor(CubitRecipes.ENGINEERING_TYPE.get(), input, player.serverLevel())
        .ifPresent(found -> pick(desk, player, found));
  }

  private static boolean pick(
      EngineeringDeskBlockEntity desk, ServerPlayer player, RecipeHolder<CraftingRecipe> found) {
    if (!DeskRecipes.unlocked(player, found)) {
      return false;
    }
    if (!desk.recipeId().map(found.id()::equals).orElse(false)) {
      desk.choose(found.id());
    }
    return true;
  }
}
