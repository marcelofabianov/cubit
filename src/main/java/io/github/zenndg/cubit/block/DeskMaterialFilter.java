package io.github.zenndg.cubit.block;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;

/**
 * The input of the desk takes only what some recipe of the mod uses, so the waste of an automation
 * never clogs it (answer 1446). The items are read again when the recipes reload.
 */
public final class DeskMaterialFilter {
  private static RecipeManager readFrom;
  private static Set<Item> materials = Set.of();

  private DeskMaterialFilter() {}

  public static synchronized boolean accepts(RecipeManager recipes, ItemStack stack) {
    if (recipes != readFrom) {
      materials = read(recipes);
      readFrom = recipes;
    }
    return materials.contains(stack.getItem());
  }

  private static Set<Item> read(RecipeManager recipes) {
    Set<Item> items = new HashSet<>();
    for (var holder : DeskRecipes.all(recipes)) {
      for (Ingredient ingredient : DeskGrid.of(holder.value())) {
        for (ItemStack stack : ingredient.getItems()) {
          items.add(stack.getItem());
        }
      }
    }
    return Set.copyOf(items);
  }
}
