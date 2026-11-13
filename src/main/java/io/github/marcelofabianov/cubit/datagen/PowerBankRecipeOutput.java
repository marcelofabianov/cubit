package io.github.marcelofabianov.cubit.datagen;

import com.mojang.serialization.JsonOps;
import io.github.marcelofabianov.cubit.item.PowerBankUpgradeRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.neoforged.neoforge.common.conditions.ICondition;

/**
 * Turns the shaped recipe the game builder makes into the chained Power Bank type, through the
 * codecs, so the JSON and the unlock in the recipe book stay those of the game (answers 902, 908).
 */
final class PowerBankRecipeOutput implements RecipeOutput {
  private final RecipeOutput output;

  PowerBankRecipeOutput(RecipeOutput output) {
    this.output = output;
  }

  @Override
  public Advancement.Builder advancement() {
    return output.advancement();
  }

  @Override
  public void accept(
      ResourceLocation id,
      Recipe<?> recipe,
      AdvancementHolder advancement,
      ICondition... conditions) {
    ShapedRecipe shaped = (ShapedRecipe) recipe;
    var json = RecipeSerializer.SHAPED_RECIPE.codec().codec().encodeStart(JsonOps.INSTANCE, shaped);
    PowerBankUpgradeRecipe upgrade =
        PowerBankUpgradeRecipe.CODEC
            .codec()
            .parse(JsonOps.INSTANCE, json.getOrThrow())
            .getOrThrow();
    output.accept(id, upgrade, advancement, conditions);
  }
}
