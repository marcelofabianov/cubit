package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.item.RobotRechargeRecipe;
import io.github.marcelofabianov.cubit.item.RobotRepairRecipe;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

/** The recipes become plain JSON files, which the pack can override with KubeJS. */
final class CubitRecipeProvider extends RecipeProvider {
  private static final String TOOLBOX_TOP_ROW = "III";
  private static final String TOOLBOX_MIDDLE_ROW = "IRI";
  private static final String TOOLBOX_BOTTOM_ROW = "IOI";
  private static final String ROBOT_TOP_ROW = "RLR";
  private static final String ROBOT_MIDDLE_ROW = "IDI";
  private static final String ROBOT_BOTTOM_ROW = "O O";
  private static final String DOCK_TOP_ROW = "R  ";
  private static final String DOCK_MIDDLE_ROW = "I  ";
  private static final String DOCK_BOTTOM_ROW = "CRI";
  private static final char IRON = 'I';
  private static final char REDSTONE = 'R';
  private static final char OBSIDIAN = 'O';
  private static final char LAMP = 'L';
  private static final char DIAMOND = 'D';
  private static final char CHEST = 'C';
  private static final String MODULE_TOP_ROW = "MSM";
  private static final String MODULE_MIDDLE_ROW = "SCS";
  private static final String MODULE_BOTTOM_ROW = "MSM";
  private static final String ULTIMATE_TOP_ROW = "DRD";
  private static final String ULTIMATE_MIDDLE_ROW = "NSN";
  private static final String ULTIMATE_BOTTOM_ROW = "D D";
  private static final char MAIN = 'M';
  private static final char SIDE = 'S';
  private static final char CORE = 'C';
  private static final char NETHERITE = 'N';
  private static final char STAR = 'S';
  private static final String HAS_IRON = "has_iron";
  private static final String HAS_DIAMOND = "has_diamond";
  private static final String HAS_ROBOT = "has_robot";
  private static final String HAS_PREFIX = "has_";
  private static final String REPAIR_PREFIX = "repair_";
  private static final String RECHARGE_PREFIX = "recharge_";
  private static final String RECIPE_ADVANCEMENT_PREFIX = "recipes/misc/";
  private static final String HAS_THE_RECIPE = "has_the_recipe";
  private static final String HAS_POWER_BANK = "has_power_bank";

  CubitRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries);
  }

  @Override
  protected void buildRecipes(RecipeOutput output) {
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CubitItems.TOOLBOX.get())
        .pattern(TOOLBOX_TOP_ROW)
        .pattern(TOOLBOX_MIDDLE_ROW)
        .pattern(TOOLBOX_BOTTOM_ROW)
        .define(IRON, Items.IRON_INGOT)
        .define(REDSTONE, Items.REDSTONE)
        .define(OBSIDIAN, Items.OBSIDIAN)
        .unlockedBy(HAS_IRON, has(Items.IRON_INGOT))
        .save(output);
    robot(output);
    dock(output);
    modules(output);
    powerBanks(output);
    recharge(output);
    String robotId = CubitConfig.GENERAL_ROBOT.id();
    output.accept(
        ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, REPAIR_PREFIX + robotId),
        new RobotRepairRecipe(
            Ingredient.of(CubitItems.DAMAGED_GENERAL_ROBOT.get()),
            Ingredient.of(CubitItems.TOOLBOX.get()),
            CubitItems.GENERAL_ROBOT.get()),
        null);
  }

  private void robot(RecipeOutput output) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CubitItems.GENERAL_ROBOT.get())
        .pattern(ROBOT_TOP_ROW)
        .pattern(ROBOT_MIDDLE_ROW)
        .pattern(ROBOT_BOTTOM_ROW)
        .define(REDSTONE, Items.REDSTONE)
        .define(LAMP, Items.REDSTONE_LAMP)
        .define(IRON, Items.IRON_INGOT)
        .define(DIAMOND, Items.DIAMOND)
        .define(OBSIDIAN, Items.OBSIDIAN)
        .unlockedBy(HAS_DIAMOND, has(Items.DIAMOND))
        .save(output);
  }

  /** Each module has its own recipe, without the one before it (answers 799, 807 and 817). */
  private void modules(RecipeOutput output) {
    module(CubitItems.ADVANCED_MODULE.get(), Items.GOLD_INGOT, Items.QUARTZ)
        .define(CORE, Items.COMPARATOR)
        .save(output);
    module(CubitItems.ELITE_MODULE.get(), Items.DIAMOND, Items.PURPLE_DYE)
        .define(CORE, Items.REDSTONE_BLOCK)
        .save(output);
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CubitItems.ULTIMATE_MODULE.get())
        .pattern(ULTIMATE_TOP_ROW)
        .pattern(ULTIMATE_MIDDLE_ROW)
        .pattern(ULTIMATE_BOTTOM_ROW)
        .define(DIAMOND, Items.DIAMOND)
        .define(REDSTONE, Items.REDSTONE_BLOCK)
        .define(NETHERITE, Items.NETHERITE_INGOT)
        .define(STAR, Items.NETHER_STAR)
        .unlockedBy(HAS_ROBOT, has(CubitItems.GENERAL_ROBOT.get()))
        .save(output);
  }

  /** The main material in the corners and the second one on the sides (answer 818). */
  private static ShapedRecipeBuilder module(ItemLike result, ItemLike main, ItemLike side) {
    return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
        .pattern(MODULE_TOP_ROW)
        .pattern(MODULE_MIDDLE_ROW)
        .pattern(MODULE_BOTTOM_ROW)
        .define(MAIN, main)
        .define(SIDE, side)
        .unlockedBy(HAS_ROBOT, has(CubitItems.GENERAL_ROBOT.get()));
  }

  /**
   * The Base from scratch and each next one around the previous, in the corners and on the sides
   * like the Modules (answers 902 and 912). The Base unlocks with a Cubit, the others with the
   * previous bank (answer 908).
   */
  private void powerBanks(RecipeOutput output) {
    module(CubitItems.BASE_POWER_BANK.get(), Items.IRON_INGOT, Items.REDSTONE)
        .define(CORE, Items.REDSTONE_BLOCK)
        .save(output);
    RecipeOutput upgrades = new PowerBankRecipeOutput(output);
    upgrade(
            CubitItems.BASE_POWER_BANK.get(),
            CubitItems.ADVANCED_POWER_BANK.get(),
            Items.GOLD_INGOT,
            Items.QUARTZ)
        .save(upgrades);
    upgrade(
            CubitItems.ADVANCED_POWER_BANK.get(),
            CubitItems.ELITE_POWER_BANK.get(),
            Items.DIAMOND,
            Items.PURPLE_DYE)
        .save(upgrades);
    upgrade(
            CubitItems.ELITE_POWER_BANK.get(),
            CubitItems.ULTIMATE_POWER_BANK.get(),
            Items.NETHERITE_INGOT,
            Items.REDSTONE_BLOCK)
        .save(upgrades);
  }

  private static ShapedRecipeBuilder upgrade(
      ItemLike previous, ItemLike next, ItemLike main, ItemLike side) {
    String criterion = HAS_PREFIX + BuiltInRegistries.ITEM.getKey(previous.asItem()).getPath();
    return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, next)
        .pattern(MODULE_TOP_ROW)
        .pattern(MODULE_MIDDLE_ROW)
        .pattern(MODULE_BOTTOM_ROW)
        .define(MAIN, main)
        .define(SIDE, side)
        .define(CORE, previous)
        .unlockedBy(criterion, has(previous));
  }

  /** Waking a stored Cubit with any Power Bank, which unlocks the recipe (answers 908 and 910). */
  private void recharge(RecipeOutput output) {
    ResourceLocation id =
        ResourceLocation.fromNamespaceAndPath(
            CubitMod.MOD_ID, RECHARGE_PREFIX + CubitConfig.GENERAL_ROBOT.id());
    AdvancementHolder unlock =
        output
            .advancement()
            .addCriterion(HAS_THE_RECIPE, RecipeUnlockedTrigger.unlocked(id))
            .addCriterion(
                HAS_POWER_BANK, inventoryTrigger(ItemPredicate.Builder.item().of(powerBanks())))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .requirements(AdvancementRequirements.Strategy.OR)
            .build(id.withPrefix(RECIPE_ADVANCEMENT_PREFIX));
    output.accept(
        id,
        new RobotRechargeRecipe(
            Ingredient.of(CubitItems.GENERAL_ROBOT.get()),
            Ingredient.of(powerBanks()),
            CubitItems.GENERAL_ROBOT.get()),
        unlock);
  }

  private static ItemLike[] powerBanks() {
    return new ItemLike[] {
      CubitItems.BASE_POWER_BANK.get(),
      CubitItems.ADVANCED_POWER_BANK.get(),
      CubitItems.ELITE_POWER_BANK.get(),
      CubitItems.ULTIMATE_POWER_BANK.get()
    };
  }

  private void dock(RecipeOutput output) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CubitItems.INDUCTION_DOCK.get())
        .pattern(DOCK_TOP_ROW)
        .pattern(DOCK_MIDDLE_ROW)
        .pattern(DOCK_BOTTOM_ROW)
        .define(REDSTONE, Items.REDSTONE)
        .define(IRON, Items.IRON_INGOT)
        .define(CHEST, Items.CHEST)
        .unlockedBy(HAS_ROBOT, has(CubitItems.GENERAL_ROBOT.get()))
        .save(output);
  }
}
