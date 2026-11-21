package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.config.CubitConfig;
import io.github.zenndg.cubit.item.RobotRechargeRecipe;
import io.github.zenndg.cubit.item.RobotRepairRecipe;
import io.github.zenndg.cubit.item.SolarDockRecipe;
import io.github.zenndg.cubit.registry.CubitBlocks;
import io.github.zenndg.cubit.registry.CubitItems;
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
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

/** The recipes become plain JSON files, which the pack can override with KubeJS. */
final class CubitRecipeProvider extends RecipeProvider {
  private static final String TOOLBOX_TOP_ROW = "III";
  private static final String TOOLBOX_MIDDLE_ROW = "IRI";
  private static final String TOOLBOX_BOTTOM_ROW = "IOI";
  private static final String DOCK_TOP_ROW = "D  ";
  private static final String TABLET_TOP_ROW = "GRG";
  private static final String TABLET_MIDDLE_ROW = "QDQ";
  private static final String TABLET_BOTTOM_ROW = "QOQ";
  private static final char GLASS = 'G';
  private static final char QUARTZ = 'Q';
  private static final String DOCK_MIDDLE_ROW = "C  ";
  private static final String DOCK_BOTTOM_ROW = "IRI";
  private static final char IRON = 'I';
  private static final char REDSTONE = 'R';
  private static final char OBSIDIAN = 'O';
  private static final char DIAMOND = 'D';
  private static final char CHEST = 'C';
  private static final String FROM_CASING_SUFFIX = "_from_casing";
  private static final char CORE = 'C';
  private static final String FISHER_TOP_ROW = "PCP";
  private static final String FISHER_MIDDLE_ROW = "RFR";
  private static final String FISHER_BOTTOM_ROW = "PRP";
  private static final char PAPER = 'P';
  private static final char ROD = 'F';
  private static final String FARMER_MIDDLE_ROW = "RHR";
  private static final char HOE = 'H';
  private static final String ORGANIZER_MIDDLE_ROW = "RFR";
  private static final char HOPPER = 'F';
  private static final String COOK_MIDDLE_ROW = "RSR";
  private static final char SMOKER = 'S';
  private static final String HAS_IRON = "has_iron";
  private static final String HAS_CORE = "has_core";
  private static final String HAS_DOCK = "has_dock";
  private static final String HAS_PANEL = "has_panel";
  private static final String PANEL_TOP_ROW = "GGG";
  private static final String PANEL_MIDDLE_ROW = "SCS";
  private static final String PANEL_BOTTOM_ROW = "III";
  private static final char SENSOR = 'S';
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
    toolbox(output);
    CubitPartRecipes.save(output);
    robot(output);
    dock(output);
    tablet(output);
    modules(output);
    powerBanks(output);
    fisherCard(output);
    farmerCard(output);
    organizerCard(output);
    cookCard(output);
    recharge(output);
    solarDock(output);
    RechargeSplashRecipe.save(output);
    String robotId = CubitConfig.GENERAL_ROBOT.id();
    output.accept(
        ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, REPAIR_PREFIX + robotId),
        new RobotRepairRecipe(
            Ingredient.of(CubitItems.DAMAGED_GENERAL_ROBOT.get()),
            Ingredient.of(CubitItems.TOOLBOX.get()),
            CubitItems.GENERAL_ROBOT.get()),
        null);
  }

  private void toolbox(RecipeOutput output) {
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CubitItems.TOOLBOX.get())
        .pattern(TOOLBOX_TOP_ROW)
        .pattern(TOOLBOX_MIDDLE_ROW)
        .pattern(TOOLBOX_BOTTOM_ROW)
        .define(IRON, Items.IRON_INGOT)
        .define(REDSTONE, Items.REDSTONE)
        .define(OBSIDIAN, Items.OBSIDIAN)
        .unlockedBy(HAS_IRON, has(Items.IRON_INGOT))
        .save(output);
  }

  /** The card of the Fisher: a fishing rod, a comparator, redstone and paper (answer 1192). */
  private void fisherCard(RecipeOutput output) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CubitItems.FISHER_CARD.get())
        .pattern(FISHER_TOP_ROW)
        .pattern(FISHER_MIDDLE_ROW)
        .pattern(FISHER_BOTTOM_ROW)
        .define(PAPER, Items.PAPER)
        .define(CORE, Items.COMPARATOR)
        .define(REDSTONE, Items.REDSTONE)
        .define(ROD, Items.FISHING_ROD)
        .unlockedBy(HAS_ROBOT, has(CubitItems.GENERAL_ROBOT.get()))
        .save(output);
  }

  /** The card of the Farmer, as the one of the Fisher with an iron hoe (answer 1228). */
  private void farmerCard(RecipeOutput output) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CubitItems.FARMER_CARD.get())
        .pattern(FISHER_TOP_ROW)
        .pattern(FARMER_MIDDLE_ROW)
        .pattern(FISHER_BOTTOM_ROW)
        .define(PAPER, Items.PAPER)
        .define(CORE, Items.COMPARATOR)
        .define(REDSTONE, Items.REDSTONE)
        .define(HOE, Items.IRON_HOE)
        .unlockedBy(HAS_ROBOT, has(CubitItems.GENERAL_ROBOT.get()))
        .save(output);
  }

  /** The card of the Organizer, as the one of the Farmer with a hopper (answer 1299). */
  private void organizerCard(RecipeOutput output) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CubitItems.ORGANIZER_CARD.get())
        .pattern(FISHER_TOP_ROW)
        .pattern(ORGANIZER_MIDDLE_ROW)
        .pattern(FISHER_BOTTOM_ROW)
        .define(PAPER, Items.PAPER)
        .define(CORE, Items.COMPARATOR)
        .define(REDSTONE, Items.REDSTONE)
        .define(HOPPER, Items.HOPPER)
        .unlockedBy(HAS_ROBOT, has(CubitItems.GENERAL_ROBOT.get()))
        .save(output);
  }

  /** The card of the Cook, as the one of the Farmer with a smoker (answer 1325). */
  private void cookCard(RecipeOutput output) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CubitItems.COOK_CARD.get())
        .pattern(FISHER_TOP_ROW)
        .pattern(COOK_MIDDLE_ROW)
        .pattern(FISHER_BOTTOM_ROW)
        .define(PAPER, Items.PAPER)
        .define(CORE, Items.COMPARATOR)
        .define(REDSTONE, Items.REDSTONE)
        .define(SMOKER, Items.SMOKER)
        .unlockedBy(HAS_ROBOT, has(CubitItems.GENERAL_ROBOT.get()))
        .save(output);
  }

  /** The four parts, without a shape, unlocked by the Neural Core (answers 1346 and 1350). */
  private void robot(RecipeOutput output) {
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CubitItems.GENERAL_ROBOT.get())
        .requires(CubitItems.GENERAL_ROBOT_CASING.get())
        .requires(CubitItems.GENERAL_ROBOT_VISOR.get())
        .requires(CubitItems.GENERAL_ROBOT_TREADS.get())
        .requires(CubitItems.GENERAL_ROBOT_CORE.get())
        .unlockedBy(HAS_CORE, has(CubitItems.GENERAL_ROBOT_CORE.get()))
        .save(output);
  }

  /**
   * The modules, chained like the cells, in a cross with the previous one in the center and the
   * Neural Core in the one of the Advanced (answers 1357 and 1358).
   */
  private void modules(RecipeOutput output) {
    module(
            CubitItems.ADVANCED_MODULE.get(),
            Items.LAPIS_LAZULI,
            Items.GOLD_INGOT,
            CubitItems.GENERAL_ROBOT_CORE.get())
        .save(output);
    module(
            CubitItems.ELITE_MODULE.get(),
            Items.PURPLE_DYE,
            Items.DIAMOND,
            CubitItems.ADVANCED_MODULE.get())
        .save(output);
    module(
            CubitItems.ULTIMATE_MODULE.get(),
            Items.NETHERITE_INGOT,
            Items.BLAZE_ROD,
            CubitItems.ELITE_MODULE.get())
        .save(output);
  }

  /** Every module unlocks with a Cubit (answer 805). */
  private static ShapedRecipeBuilder module(
      ItemLike result, ItemLike edge, ItemLike side, ItemLike center) {
    return CubitPartRecipes.cross(
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result), edge, side, center)
        .unlockedBy(HAS_ROBOT, has(CubitItems.GENERAL_ROBOT.get()));
  }

  /**
   * Each bank from scratch, with the casing and the cell of its tier, empty, and each next one from
   * the previous, in the cross of its cell, carrying the charge (answers 902 and 1355).
   */
  private void powerBanks(RecipeOutput output) {
    fromCasing(CubitItems.BASE_POWER_BANK.get(), CubitItems.BASE_POWER_CELL.get()).save(output);
    fromCasing(CubitItems.ADVANCED_POWER_BANK.get(), CubitItems.ADVANCED_POWER_CELL.get())
        .save(output, fromCasingId(CubitItems.ADVANCED_POWER_BANK.get()));
    fromCasing(CubitItems.ELITE_POWER_BANK.get(), CubitItems.ELITE_POWER_CELL.get())
        .save(output, fromCasingId(CubitItems.ELITE_POWER_BANK.get()));
    fromCasing(CubitItems.ULTIMATE_POWER_BANK.get(), CubitItems.ULTIMATE_POWER_CELL.get())
        .save(output, fromCasingId(CubitItems.ULTIMATE_POWER_BANK.get()));
    RecipeOutput upgrades = new PowerBankRecipeOutput(output);
    upgrade(
            CubitItems.BASE_POWER_BANK.get(),
            CubitItems.ADVANCED_POWER_BANK.get(),
            Items.LAPIS_LAZULI,
            Items.GOLD_INGOT)
        .save(upgrades);
    upgrade(
            CubitItems.ADVANCED_POWER_BANK.get(),
            CubitItems.ELITE_POWER_BANK.get(),
            Items.PURPLE_DYE,
            Items.DIAMOND)
        .save(upgrades);
    upgrade(
            CubitItems.ELITE_POWER_BANK.get(),
            CubitItems.ULTIMATE_POWER_BANK.get(),
            Items.NETHERITE_INGOT,
            Items.BLAZE_ROD)
        .save(upgrades);
  }

  /** The casing and the cell, without a shape, unlocked by the cell (answer 1355). */
  private static ShapelessRecipeBuilder fromCasing(ItemLike bank, ItemLike cell) {
    String criterion = HAS_PREFIX + BuiltInRegistries.ITEM.getKey(cell.asItem()).getPath();
    return ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, bank)
        .requires(CubitItems.POWER_BANK_CASING.get())
        .requires(cell)
        .unlockedBy(criterion, has(cell));
  }

  private static ResourceLocation fromCasingId(ItemLike bank) {
    return BuiltInRegistries.ITEM.getKey(bank.asItem()).withSuffix(FROM_CASING_SUFFIX);
  }

  private static ShapedRecipeBuilder upgrade(
      ItemLike previous, ItemLike next, ItemLike edge, ItemLike side) {
    String criterion = HAS_PREFIX + BuiltInRegistries.ITEM.getKey(previous.asItem()).getPath();
    return CubitPartRecipes.cross(
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, next), edge, side, previous)
        .unlockedBy(criterion, has(previous));
  }

  /**
   * The panel on a dock makes the Solar Dock, which keeps the state of the dock (answers 1386 and
   * 1387). The panel shows in the book with a dock, and the Solar Dock with the panel.
   */
  private void solarDock(RecipeOutput output) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CubitItems.DOCK_SOLAR_PANEL.get())
        .pattern(PANEL_TOP_ROW)
        .pattern(PANEL_MIDDLE_ROW)
        .pattern(PANEL_BOTTOM_ROW)
        .define(GLASS, Items.GLASS)
        .define(SENSOR, Items.DAYLIGHT_DETECTOR)
        .define(CORE, Items.REDSTONE_BLOCK)
        .define(IRON, Items.IRON_INGOT)
        .unlockedBy(HAS_DOCK, has(CubitItems.INDUCTION_DOCK.get()))
        .save(output);
    ResourceLocation id =
        ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, CubitBlocks.SOLAR_INDUCTION_DOCK_ID);
    AdvancementHolder unlock =
        output
            .advancement()
            .addCriterion(HAS_THE_RECIPE, RecipeUnlockedTrigger.unlocked(id))
            .addCriterion(HAS_PANEL, has(CubitItems.DOCK_SOLAR_PANEL.get()))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .requirements(AdvancementRequirements.Strategy.OR)
            .build(id.withPrefix(RECIPE_ADVANCEMENT_PREFIX));
    output.accept(
        id,
        new SolarDockRecipe(
            Ingredient.of(CubitItems.DOCK_SOLAR_PANEL.get()),
            Ingredient.of(CubitItems.INDUCTION_DOCK.get()),
            CubitItems.SOLAR_INDUCTION_DOCK.get()),
        unlock);
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
        .define(DIAMOND, Items.DIAMOND)
        .define(CHEST, Items.CHEST)
        .define(IRON, Items.IRON_INGOT)
        .define(REDSTONE, Items.REDSTONE)
        .unlockedBy(HAS_ROBOT, has(CubitItems.GENERAL_ROBOT.get()))
        .save(output);
  }

  /**
   * Glass and redstone on top, quartz on the sides, a diamond in the middle and obsidian below
   * (answer 1331).
   */
  private void tablet(RecipeOutput output) {
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CubitItems.TABLET.get())
        .pattern(TABLET_TOP_ROW)
        .pattern(TABLET_MIDDLE_ROW)
        .pattern(TABLET_BOTTOM_ROW)
        .define(GLASS, Items.GLASS_PANE)
        .define(QUARTZ, Items.QUARTZ)
        .define(REDSTONE, Items.REDSTONE)
        .define(DIAMOND, Items.DIAMOND)
        .define(OBSIDIAN, Items.OBSIDIAN)
        .unlockedBy(HAS_ROBOT, has(CubitItems.GENERAL_ROBOT.get()))
        .save(output);
  }
}
