package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.registry.CubitItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;

/** The Recharge Splash, shapeless, only from the game, as in _docs/itens.md (answer 1406). */
final class RechargeSplashRecipe {
  private static final String HAS_REDSTONE_BLOCK = "has_redstone_block";

  private RechargeSplashRecipe() {}

  static void save(RecipeOutput output) {
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CubitItems.RECHARGE_SPLASH.get())
        .requires(Items.GLASS_BOTTLE)
        .requires(Items.REDSTONE_BLOCK)
        .requires(Items.IRON_INGOT)
        .requires(Items.GUNPOWDER)
        .requires(Items.LAPIS_LAZULI)
        .unlockedBy(
            HAS_REDSTONE_BLOCK,
            InventoryChangeTrigger.TriggerInstance.hasItems(Items.REDSTONE_BLOCK))
        .save(output);
  }
}
