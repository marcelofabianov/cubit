package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.config.CubitConfig;
import io.github.zenndg.cubit.item.RobotRechargeRecipe;
import io.github.zenndg.cubit.item.RobotRepairRecipe;
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
  private static final String ROBOT_BOTTOM_ROW = "OGO";
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
  private static final char LAMP = 'L';
  private static final char DIAMOND = 'D';
  private static final char CHEST = 'C';
  private static final String MODULE_TOP_ROW = "TTT";
  private static final String MODULE_MIDDLE_ROW = "SCS";
  private static final String MODULE_BOTTOM_ROW = "BBB";
  private static final String BANK_TOP_ROW = "KTK";
  private static final String BANK_MIDDLE_ROW = "SCS";
  private static final String BANK_BOTTOM_ROW = "KBK";
  private static final char TOP = 'T';
  private static final char SIDE = 'S';
  private static final char CORE = 'C';
  private static final char BOTTOM = 'B';
  private static final char CORNER = 'K';
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
    tablet(output);
    modules(output);
    powerBanks(output);
    fisherCard(output);
    farmerCard(output);
    organizerCard(output);
    cookCard(output);
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
        .define(GLASS, Items.GLASS_PANE)
        .unlockedBy(HAS_DIAMOND, has(Items.DIAMOND))
        .save(output);
  }

  /**
   * Each module has its own recipe, without the one before it (answer 799): a row on top, the side
   * material around the core and a row below (answer 1331).
   */
  private void modules(RecipeOutput output) {
    module(
            CubitItems.ADVANCED_MODULE.get(),
            Items.OBSIDIAN,
            Items.LAPIS_LAZULI,
            Items.DIAMOND,
            Items.GOLD_INGOT)
        .save(output);
    module(
            CubitItems.ELITE_MODULE.get(),
            Items.OBSIDIAN,
            Items.PURPLE_DYE,
            Items.DIAMOND_BLOCK,
            Items.GOLD_BLOCK)
        .save(output);
    module(
            CubitItems.ULTIMATE_MODULE.get(),
            Items.NETHERITE_INGOT,
            Items.NETHERITE_INGOT,
            Items.DIAMOND_BLOCK,
            Items.BLAZE_POWDER)
        .save(output);
  }

  private static ShapedRecipeBuilder module(
      ItemLike result, ItemLike top, ItemLike side, ItemLike core, ItemLike bottom) {
    return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
        .pattern(MODULE_TOP_ROW)
        .pattern(MODULE_MIDDLE_ROW)
        .pattern(MODULE_BOTTOM_ROW)
        .define(TOP, top)
        .define(SIDE, side)
        .define(CORE, core)
        .define(BOTTOM, bottom)
        .unlockedBy(HAS_ROBOT, has(CubitItems.GENERAL_ROBOT.get()));
  }

  /**
   * The Base from scratch and each next one around the previous (answers 902 and 1331 to 1334):
   * gold on top between the corners, the core between the sides and the tier material below. The
   * Base unlocks with a Cubit, the others with the previous bank (answer 908).
   */
  private void powerBanks(RecipeOutput output) {
    bank(
            CubitItems.BASE_POWER_BANK.get(),
            Items.QUARTZ,
            Items.EMERALD,
            Items.REDSTONE_BLOCK,
            Items.OBSIDIAN)
        .unlockedBy(HAS_ROBOT, has(CubitItems.GENERAL_ROBOT.get()))
        .save(output);
    RecipeOutput upgrades = new PowerBankRecipeOutput(output);
    upgrade(
            CubitItems.BASE_POWER_BANK.get(),
            CubitItems.ADVANCED_POWER_BANK.get(),
            Items.QUARTZ,
            Items.LAPIS_LAZULI)
        .save(upgrades);
    upgrade(
            CubitItems.ADVANCED_POWER_BANK.get(),
            CubitItems.ELITE_POWER_BANK.get(),
            Items.QUARTZ,
            Items.PURPLE_DYE)
        .save(upgrades);
    upgrade(
            CubitItems.ELITE_POWER_BANK.get(),
            CubitItems.ULTIMATE_POWER_BANK.get(),
            Items.NETHERITE_INGOT,
            Items.BLAZE_ROD)
        .save(upgrades);
  }

  private static ShapedRecipeBuilder bank(
      ItemLike result, ItemLike corner, ItemLike side, ItemLike core, ItemLike bottom) {
    return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
        .pattern(BANK_TOP_ROW)
        .pattern(BANK_MIDDLE_ROW)
        .pattern(BANK_BOTTOM_ROW)
        .define(CORNER, corner)
        .define(TOP, Items.GOLD_INGOT)
        .define(SIDE, side)
        .define(CORE, core)
        .define(BOTTOM, bottom);
  }

  private static ShapedRecipeBuilder upgrade(
      ItemLike previous, ItemLike next, ItemLike corner, ItemLike bottom) {
    String criterion = HAS_PREFIX + BuiltInRegistries.ITEM.getKey(previous.asItem()).getPath();
    return bank(next, corner, Items.REDSTONE_BLOCK, previous, bottom)
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
