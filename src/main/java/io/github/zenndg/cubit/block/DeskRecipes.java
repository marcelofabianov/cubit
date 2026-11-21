package io.github.zenndg.cubit.block;

import io.github.zenndg.cubit.registry.CubitRecipes;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

/**
 * The recipes of the Engineering Desk. A recipe is unlocked as in the recipe book of the game
 * (answers 1437 and 1441), and the server checks it for the player who chooses it.
 */
public final class DeskRecipes {
  private DeskRecipes() {}

  public static Optional<RecipeHolder<CraftingRecipe>> find(
      RecipeManager recipes, ResourceLocation id) {
    return recipes.byKey(id).flatMap(DeskRecipes::asDesk);
  }

  /**
   * Every recipe of the desk, in the order they unlock, the same on the server and the client, so
   * the catalog and the chosen recipe always match (answer 1499).
   */
  public static List<RecipeHolder<CraftingRecipe>> all(RecipeManager recipes) {
    return recipes.getAllRecipesFor(CubitRecipes.ENGINEERING_TYPE.get()).stream()
        .sorted(
            Comparator.comparingInt(
                    (RecipeHolder<CraftingRecipe> holder) -> DeskCatalogOrder.rank(holder.id()))
                .thenComparing(holder -> holder.id().toString()))
        .toList();
  }

  public static boolean unlocked(ServerPlayer player, RecipeHolder<?> recipe) {
    return player.getRecipeBook().contains(recipe);
  }

  private static Optional<RecipeHolder<CraftingRecipe>> asDesk(RecipeHolder<?> holder) {
    if (holder.value().getType() != CubitRecipes.ENGINEERING_TYPE.get()
        || !(holder.value() instanceof CraftingRecipe recipe)) {
      return Optional.empty();
    }
    return Optional.of(new RecipeHolder<>(holder.id(), recipe));
  }
}
