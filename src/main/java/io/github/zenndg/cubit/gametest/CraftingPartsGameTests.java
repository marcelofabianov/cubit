package io.github.zenndg.cubit.gametest;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.List;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The parts of the Cubit and of the Power Banks, with the grids of _docs/itens.md (1338-1352). */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class CraftingPartsGameTests {
  private static final String EMPTY_TEMPLATE = "empty";
  private static final int GRID_SIZE = 3;
  private static final int PART_STACK_SIZE = 64;
  private static final String WRONG_RESULT = "Wrong recipe result";
  private static final String WRONG_STACK = "A part should stack up to 64";

  private CraftingPartsGameTests() {}

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theCasingRecipeMakesACasing(GameTestHelper helper) {
    assertMakes(
        helper,
        CubitItems.GENERAL_ROBOT_CASING.get(),
        grid(
            Items.REDSTONE_TORCH,
            Items.IRON_INGOT,
            Items.REDSTONE_TORCH,
            Items.IRON_INGOT,
            Items.AIR,
            Items.IRON_INGOT,
            Items.OBSIDIAN,
            Items.IRON_INGOT,
            Items.OBSIDIAN));
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theVisorRecipeMakesAVisor(GameTestHelper helper) {
    assertMakes(
        helper,
        CubitItems.GENERAL_ROBOT_VISOR.get(),
        grid(
            Items.AIR,
            Items.AIR,
            Items.AIR,
            Items.DIAMOND,
            Items.OBSIDIAN,
            Items.DIAMOND,
            Items.AIR,
            Items.AIR,
            Items.AIR));
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theTreadsRecipeMakesTreads(GameTestHelper helper) {
    assertMakes(
        helper,
        CubitItems.GENERAL_ROBOT_TREADS.get(),
        grid(
            Items.IRON_INGOT,
            Items.IRON_INGOT,
            Items.IRON_INGOT,
            Items.PISTON,
            Items.AIR,
            Items.PISTON,
            Items.AIR,
            Items.AIR,
            Items.AIR));
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theCoreRecipeMakesACore(GameTestHelper helper) {
    assertMakes(
        helper,
        CubitItems.GENERAL_ROBOT_CORE.get(),
        grid(
            Items.AIR,
            Items.REDSTONE,
            Items.AIR,
            Items.REDSTONE,
            Items.DIAMOND,
            Items.REDSTONE,
            Items.AIR,
            Items.REDSTONE,
            Items.AIR));
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void thePowerBankCasingRecipeMakesACasing(GameTestHelper helper) {
    assertMakes(
        helper,
        CubitItems.POWER_BANK_CASING.get(),
        grid(
            Items.QUARTZ,
            Items.GOLD_INGOT,
            Items.QUARTZ,
            Items.QUARTZ,
            Items.AIR,
            Items.QUARTZ,
            Items.QUARTZ,
            Items.OBSIDIAN,
            Items.QUARTZ));
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theBaseCellRecipeMakesABaseCell(GameTestHelper helper) {
    assertMakes(
        helper,
        CubitItems.BASE_POWER_CELL.get(),
        cross(Items.COPPER_INGOT, Items.REDSTONE, Items.REDSTONE_BLOCK));
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theAdvancedCellRecipeMakesAnAdvancedCell(GameTestHelper helper) {
    assertMakes(
        helper,
        CubitItems.ADVANCED_POWER_CELL.get(),
        cross(Items.LAPIS_LAZULI, Items.GOLD_INGOT, CubitItems.BASE_POWER_CELL.get()));
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theEliteCellRecipeMakesAnEliteCell(GameTestHelper helper) {
    assertMakes(
        helper,
        CubitItems.ELITE_POWER_CELL.get(),
        cross(Items.PURPLE_DYE, Items.DIAMOND, CubitItems.ADVANCED_POWER_CELL.get()));
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theUltimateCellRecipeMakesAnUltimateCell(GameTestHelper helper) {
    assertMakes(
        helper,
        CubitItems.ULTIMATE_POWER_CELL.get(),
        cross(Items.NETHERITE_INGOT, Items.BLAZE_ROD, CubitItems.ELITE_POWER_CELL.get()));
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void thePartsStackUpTo64(GameTestHelper helper) {
    for (Item part : parts()) {
      helper.assertValueEqual(new ItemStack(part).getMaxStackSize(), PART_STACK_SIZE, WRONG_STACK);
    }
    helper.succeed();
  }

  private static List<Item> parts() {
    return List.of(
        CubitItems.GENERAL_ROBOT_CASING.get(),
        CubitItems.GENERAL_ROBOT_VISOR.get(),
        CubitItems.GENERAL_ROBOT_TREADS.get(),
        CubitItems.GENERAL_ROBOT_CORE.get(),
        CubitItems.POWER_BANK_CASING.get(),
        CubitItems.BASE_POWER_CELL.get(),
        CubitItems.ADVANCED_POWER_CELL.get(),
        CubitItems.ELITE_POWER_CELL.get(),
        CubitItems.ULTIMATE_POWER_CELL.get());
  }

  /** A cell is a cross: one material on top and below, another on the sides (answer 1354). */
  private static List<ItemStack> cross(Item topAndBottom, Item sides, Item center) {
    return grid(
        Items.AIR,
        topAndBottom,
        Items.AIR,
        sides,
        center,
        sides,
        Items.AIR,
        topAndBottom,
        Items.AIR);
  }

  private static void assertMakes(GameTestHelper helper, Item expected, List<ItemStack> grid) {
    ServerLevel level = helper.getLevel();
    CraftingInput input = CraftingInput.of(GRID_SIZE, GRID_SIZE, grid);
    ItemStack result =
        level
            .getRecipeManager()
            .getRecipeFor(RecipeType.CRAFTING, input, level)
            .map(recipe -> recipe.value().assemble(input, level.registryAccess()))
            .orElse(ItemStack.EMPTY);
    helper.assertTrue(result.is(expected), WRONG_RESULT);
    helper.succeed();
  }

  private static List<ItemStack> grid(Item... items) {
    return List.of(items).stream().map(ItemStack::new).toList();
  }
}
