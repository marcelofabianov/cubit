package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.item.RobotRepairRecipe;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

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
  private static final String HAS_IRON = "has_iron";
  private static final String HAS_DIAMOND = "has_diamond";
  private static final String HAS_ROBOT = "has_robot";
  private static final String REPAIR_PREFIX = "repair_";

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
