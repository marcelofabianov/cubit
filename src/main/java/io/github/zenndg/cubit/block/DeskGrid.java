package io.github.zenndg.cubit.block;

import io.github.zenndg.cubit.item.EngineeringRecipe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;

/**
 * The marks a recipe puts in the 3x3 grid of the desk: a shaped recipe keeps its shape, from the
 * top left, and the others fill the grid in order (answer 1426). The marks are never real items.
 */
public final class DeskGrid {
  public static final int SIDE = 3;
  public static final int CELLS = SIDE * SIDE;

  private DeskGrid() {}

  public static List<Ingredient> of(Recipe<?> recipe) {
    ShapedRecipe shaped = shapedOf(recipe);
    return shaped == null ? inOrder(recipe.getIngredients()) : shaped(shaped);
  }

  private static ShapedRecipe shapedOf(Recipe<?> recipe) {
    if (recipe instanceof ShapedRecipe shaped) {
      return shaped;
    }
    return recipe instanceof EngineeringRecipe engineering ? engineering.shaped() : null;
  }

  private static List<Ingredient> shaped(ShapedRecipe recipe) {
    List<Ingredient> cells = empty();
    List<Ingredient> ingredients = recipe.getIngredients();
    for (int row = 0; row < recipe.getHeight() && row < SIDE; row++) {
      for (int column = 0; column < recipe.getWidth() && column < SIDE; column++) {
        cells.set(row * SIDE + column, ingredients.get(row * recipe.getWidth() + column));
      }
    }
    return cells;
  }

  private static List<Ingredient> inOrder(List<Ingredient> ingredients) {
    List<Ingredient> cells = empty();
    for (int index = 0; index < ingredients.size() && index < CELLS; index++) {
      cells.set(index, ingredients.get(index));
    }
    return cells;
  }

  private static List<Ingredient> empty() {
    return new ArrayList<>(Collections.nCopies(CELLS, Ingredient.EMPTY));
  }
}
