package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.config.CubitConfig;
import io.github.zenndg.cubit.item.RobotRepairRecipe;
import io.github.zenndg.cubit.registry.CubitItems;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * The recipe of the Engineering Desk, the only one of the mod in the crafting table, and the
 * repair, which now has an unlock of its own (answers 1427 and 1430).
 */
final class DeskRecipeData {
  private static final String DESK_TOP_ROW = "GRG";
  private static final String DESK_MIDDLE_ROW = "CTC";
  private static final String DESK_BOTTOM_ROW = "III";
  private static final char GLASS = 'G';
  private static final char REDSTONE = 'R';
  private static final char CHEST = 'C';
  private static final char TABLE = 'T';
  private static final char IRON = 'I';
  private static final String HAS_TABLE = "has_crafting_table";
  private static final String HAS_DAMAGED = "has_damaged_robot";
  private static final String HAS_TOOLBOX = "has_toolbox";
  private static final String HAS_THE_RECIPE = "has_the_recipe";
  private static final String REPAIR_PREFIX = "repair_";
  private static final String RECIPE_ADVANCEMENT_PREFIX = "recipes/misc/";

  /** The name of the criterion of the recipes open from the start (answer 1498). */
  static final String FROM_START = "from_start";

  private DeskRecipeData() {}

  /** Met on the first tick of the player, so the recipe is open from the start. */
  static Criterion<PlayerTrigger.TriggerInstance> fromStart() {
    return PlayerTrigger.TriggerInstance.tick();
  }

  /** The only recipe of the mod in the crafting table, shown from the start (answer 1430). */
  static void desk(RecipeOutput output) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CubitItems.ENGINEERING_DESK.get())
        .pattern(DESK_TOP_ROW)
        .pattern(DESK_MIDDLE_ROW)
        .pattern(DESK_BOTTOM_ROW)
        .define(GLASS, Items.GLASS_PANE)
        .define(REDSTONE, Items.REDSTONE)
        .define(CHEST, Items.CHEST)
        .define(TABLE, Items.CRAFTING_TABLE)
        .define(IRON, Items.IRON_INGOT)
        .unlockedBy(
            HAS_TABLE, InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRAFTING_TABLE))
        .save(output);
  }

  /** The repair, unlocked with a Damaged Cubit or a Toolbox, which always had it at hand. */
  static void repair(RecipeOutput output) {
    ResourceLocation id =
        ResourceLocation.fromNamespaceAndPath(
            CubitMod.MOD_ID, REPAIR_PREFIX + CubitConfig.GENERAL_ROBOT.id());
    AdvancementHolder unlock =
        output
            .advancement()
            .addCriterion(HAS_THE_RECIPE, RecipeUnlockedTrigger.unlocked(id))
            .addCriterion(
                HAS_DAMAGED,
                InventoryChangeTrigger.TriggerInstance.hasItems(
                    CubitItems.DAMAGED_GENERAL_ROBOT.get()))
            .addCriterion(
                HAS_TOOLBOX,
                InventoryChangeTrigger.TriggerInstance.hasItems(CubitItems.TOOLBOX.get()))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .requirements(AdvancementRequirements.Strategy.OR)
            .build(id.withPrefix(RECIPE_ADVANCEMENT_PREFIX));
    output.accept(
        id,
        new RobotRepairRecipe(
            Ingredient.of(CubitItems.DAMAGED_GENERAL_ROBOT.get()),
            Ingredient.of(CubitItems.TOOLBOX.get()),
            CubitItems.GENERAL_ROBOT.get()),
        unlock);
  }
}
