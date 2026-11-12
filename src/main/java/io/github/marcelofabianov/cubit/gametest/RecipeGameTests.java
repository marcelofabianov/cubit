package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The recipes run through the real recipe manager, with the grids of the documents. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class RecipeGameTests {
  private static final String EMPTY_TEMPLATE = "empty";
  private static final int GRID_SIZE = 3;
  private static final BlockPos FLOOR = new BlockPos(1, 0, 1);
  private static final long INITIAL_ENERGY_AMOUNT = 75_000;
  private static final float MAX_HEALTH = 25f;
  private static final RobotState NEW_ROBOT =
      new RobotState(RobotTier.BASE, new Energy(INITIAL_ENERGY_AMOUNT), MAX_HEALTH);
  private static final String WRONG_RESULT = "Wrong recipe result";
  private static final String WRONG_STATE = "Wrong state of the crafted robot";
  private static final String WRONG_POWER = "Wrong power state of the crafted robot";
  private static final String WRONG_ENERGY = "The crafted dock should start empty";

  private RecipeGameTests() {}

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theRobotRecipeMakesARobot(GameTestHelper helper) {
    ItemStack result = craft(helper, robotGrid());

    helper.assertTrue(result.is(CubitItems.GENERAL_ROBOT.get()), WRONG_RESULT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theCraftedRobotIsBaseWithTheInitialChargeAndFullHealth(GameTestHelper helper) {
    Player player = helper.makeMockPlayer(GameType.SURVIVAL);

    RobotEntity robot =
        GameTestRobots.placeRobot(helper, player, craft(helper, robotGrid()), FLOOR);

    helper.assertValueEqual(robot.state(), NEW_ROBOT, WRONG_STATE);
    helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_POWER);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theDockRecipeMakesADock(GameTestHelper helper) {
    ItemStack result = craft(helper, dockGrid());

    helper.assertTrue(result.is(CubitItems.INDUCTION_DOCK.get()), WRONG_RESULT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theMirroredDockRecipeAlsoMakesADock(GameTestHelper helper) {
    ItemStack result = craft(helper, mirroredDockGrid());

    helper.assertTrue(result.is(CubitItems.INDUCTION_DOCK.get()), WRONG_RESULT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theCraftedDockStartsWithAnEmptyStock(GameTestHelper helper) {
    Player player = helper.makeMockPlayer(GameType.SURVIVAL);

    GameTestRobots.use(helper, player, craft(helper, dockGrid()), FLOOR);

    InductionDockBlockEntity dock = helper.getBlockEntity(FLOOR.above());
    helper.assertValueEqual(dock.energy(), Energy.ZERO, WRONG_ENERGY);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theAdvancedModuleRecipeMakesAnAdvancedModule(GameTestHelper helper) {
    ItemStack result = craft(helper, advancedModuleGrid());

    helper.assertTrue(result.is(CubitItems.ADVANCED_MODULE.get()), WRONG_RESULT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theEliteModuleRecipeMakesAnEliteModule(GameTestHelper helper) {
    ItemStack result = craft(helper, eliteModuleGrid());

    helper.assertTrue(result.is(CubitItems.ELITE_MODULE.get()), WRONG_RESULT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theUltimateModuleRecipeMakesAnUltimateModule(GameTestHelper helper) {
    ItemStack result = craft(helper, ultimateModuleGrid());

    helper.assertTrue(result.is(CubitItems.ULTIMATE_MODULE.get()), WRONG_RESULT);
    helper.succeed();
  }

  /** The grids of _docs/itens.md: the main material in the corners and the core in the middle. */
  private static List<ItemStack> advancedModuleGrid() {
    return stacks(
        Items.GOLD_INGOT,
        Items.QUARTZ,
        Items.GOLD_INGOT,
        Items.QUARTZ,
        Items.COMPARATOR,
        Items.QUARTZ,
        Items.GOLD_INGOT,
        Items.QUARTZ,
        Items.GOLD_INGOT);
  }

  private static List<ItemStack> eliteModuleGrid() {
    return stacks(
        Items.DIAMOND,
        Items.PURPLE_DYE,
        Items.DIAMOND,
        Items.PURPLE_DYE,
        Items.REDSTONE_BLOCK,
        Items.PURPLE_DYE,
        Items.DIAMOND,
        Items.PURPLE_DYE,
        Items.DIAMOND);
  }

  private static List<ItemStack> ultimateModuleGrid() {
    return stacks(
        Items.DIAMOND,
        Items.REDSTONE_BLOCK,
        Items.DIAMOND,
        Items.NETHERITE_INGOT,
        Items.NETHER_STAR,
        Items.NETHERITE_INGOT,
        Items.DIAMOND,
        Items.AIR,
        Items.DIAMOND);
  }

  private static List<ItemStack> robotGrid() {
    return stacks(
        Items.REDSTONE,
        Items.REDSTONE_LAMP,
        Items.REDSTONE,
        Items.IRON_INGOT,
        Items.DIAMOND,
        Items.IRON_INGOT,
        Items.OBSIDIAN,
        Items.AIR,
        Items.OBSIDIAN);
  }

  private static List<ItemStack> dockGrid() {
    return stacks(
        Items.REDSTONE,
        Items.AIR,
        Items.AIR,
        Items.IRON_INGOT,
        Items.AIR,
        Items.AIR,
        Items.CHEST,
        Items.REDSTONE,
        Items.IRON_INGOT);
  }

  private static List<ItemStack> mirroredDockGrid() {
    return stacks(
        Items.AIR,
        Items.AIR,
        Items.REDSTONE,
        Items.AIR,
        Items.AIR,
        Items.IRON_INGOT,
        Items.IRON_INGOT,
        Items.REDSTONE,
        Items.CHEST);
  }

  private static List<ItemStack> stacks(Item... items) {
    return List.of(items).stream().map(ItemStack::new).toList();
  }

  private static ItemStack craft(GameTestHelper helper, List<ItemStack> grid) {
    ServerLevel level = helper.getLevel();
    CraftingInput input = CraftingInput.of(GRID_SIZE, GRID_SIZE, grid);
    return level
        .getRecipeManager()
        .getRecipeFor(RecipeType.CRAFTING, input, level)
        .map(recipe -> recipe.value().assemble(input, level.registryAccess()))
        .orElse(ItemStack.EMPTY);
  }
}
