package io.github.zenndg.cubit.gametest;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.block.InductionDockBlockEntity;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.energy.PowerState;
import io.github.zenndg.cubit.domain.robot.RobotState;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.entity.RobotEntity;
import io.github.zenndg.cubit.registry.CubitItems;
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

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theTabletRecipeMakesATablet(GameTestHelper helper) {
    ItemStack result = craft(helper, tabletGrid());

    helper.assertTrue(result.is(CubitItems.TABLET.get()), WRONG_RESULT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theBasePowerBankRecipeMakesABasePowerBank(GameTestHelper helper) {
    ItemStack result = craft(helper, basePowerBankGrid());

    helper.assertTrue(result.is(CubitItems.BASE_POWER_BANK.get()), WRONG_RESULT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void eachBankAlsoComesFromTheCasingAndItsCell(GameTestHelper helper) {
    helper.assertTrue(
        craft(helper, fromCasing(CubitItems.ADVANCED_POWER_CELL.get()))
            .is(CubitItems.ADVANCED_POWER_BANK.get()),
        WRONG_RESULT);
    helper.assertTrue(
        craft(helper, fromCasing(CubitItems.ELITE_POWER_CELL.get()))
            .is(CubitItems.ELITE_POWER_BANK.get()),
        WRONG_RESULT);
    helper.assertTrue(
        craft(helper, fromCasing(CubitItems.ULTIMATE_POWER_CELL.get()))
            .is(CubitItems.ULTIMATE_POWER_BANK.get()),
        WRONG_RESULT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theAdvancedPowerBankRecipeTakesTheBaseOne(GameTestHelper helper) {
    ItemStack result =
        craft(
            helper,
            nextBankGrid(Items.LAPIS_LAZULI, Items.GOLD_INGOT, CubitItems.BASE_POWER_BANK.get()));

    helper.assertTrue(result.is(CubitItems.ADVANCED_POWER_BANK.get()), WRONG_RESULT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theElitePowerBankRecipeTakesTheAdvancedOne(GameTestHelper helper) {
    ItemStack result =
        craft(
            helper,
            nextBankGrid(Items.PURPLE_DYE, Items.DIAMOND, CubitItems.ADVANCED_POWER_BANK.get()));

    helper.assertTrue(result.is(CubitItems.ELITE_POWER_BANK.get()), WRONG_RESULT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theUltimatePowerBankRecipeTakesTheEliteOne(GameTestHelper helper) {
    ItemStack result =
        craft(
            helper,
            nextBankGrid(
                Items.NETHERITE_INGOT, Items.BLAZE_ROD, CubitItems.ELITE_POWER_BANK.get()));

    helper.assertTrue(result.is(CubitItems.ULTIMATE_POWER_BANK.get()), WRONG_RESULT);
    helper.succeed();
  }

  /** The modules, chained in a cross like the cells (answers 1357 and 1358). */
  private static List<ItemStack> advancedModuleGrid() {
    return cross(Items.LAPIS_LAZULI, Items.GOLD_INGOT, CubitItems.GENERAL_ROBOT_CORE.get());
  }

  private static List<ItemStack> eliteModuleGrid() {
    return cross(Items.PURPLE_DYE, Items.DIAMOND, CubitItems.ADVANCED_MODULE.get());
  }

  private static List<ItemStack> ultimateModuleGrid() {
    return cross(Items.NETHERITE_INGOT, Items.BLAZE_ROD, CubitItems.ELITE_MODULE.get());
  }

  private static List<ItemStack> cross(Item topAndBottom, Item sides, Item center) {
    return stacks(
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

  private static List<ItemStack> tabletGrid() {
    return stacks(
        Items.GLASS_PANE,
        Items.REDSTONE,
        Items.GLASS_PANE,
        Items.QUARTZ,
        Items.DIAMOND,
        Items.QUARTZ,
        Items.QUARTZ,
        Items.OBSIDIAN,
        Items.QUARTZ);
  }

  /** The casing and the Base cell, without a shape (answers 1347 and 1355). */
  private static List<ItemStack> basePowerBankGrid() {
    return fromCasing(CubitItems.BASE_POWER_CELL.get());
  }

  /** Any bank from scratch: the casing and the cell of its tier, without a shape (answer 1355). */
  private static List<ItemStack> fromCasing(Item cell) {
    return stacks(
        Items.AIR,
        Items.AIR,
        Items.AIR,
        cell,
        Items.AIR,
        CubitItems.POWER_BANK_CASING.get(),
        Items.AIR,
        Items.AIR,
        Items.AIR);
  }

  /** The cross of the cell of the tier, with the previous bank in the center (answer 1355). */
  private static List<ItemStack> nextBankGrid(Item topAndBottom, Item sides, Item previous) {
    return cross(topAndBottom, sides, previous);
  }

  /** The four parts, without a shape, in scattered slots (answer 1346). */
  private static List<ItemStack> robotGrid() {
    return stacks(
        CubitItems.GENERAL_ROBOT_TREADS.get(),
        Items.AIR,
        Items.AIR,
        Items.AIR,
        CubitItems.GENERAL_ROBOT_CORE.get(),
        Items.AIR,
        CubitItems.GENERAL_ROBOT_VISOR.get(),
        Items.AIR,
        CubitItems.GENERAL_ROBOT_CASING.get());
  }

  private static List<ItemStack> dockGrid() {
    return stacks(
        Items.DIAMOND,
        Items.AIR,
        Items.AIR,
        Items.CHEST,
        Items.AIR,
        Items.AIR,
        Items.IRON_INGOT,
        Items.REDSTONE,
        Items.IRON_INGOT);
  }

  private static List<ItemStack> mirroredDockGrid() {
    return stacks(
        Items.AIR,
        Items.AIR,
        Items.DIAMOND,
        Items.AIR,
        Items.AIR,
        Items.CHEST,
        Items.IRON_INGOT,
        Items.REDSTONE,
        Items.IRON_INGOT);
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
