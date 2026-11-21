package io.github.zenndg.cubit.gametest;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.block.DeskGrid;
import io.github.zenndg.cubit.block.DeskPart;
import io.github.zenndg.cubit.block.EngineeringDeskBlock;
import io.github.zenndg.cubit.block.EngineeringDeskBlockEntity;
import io.github.zenndg.cubit.domain.desk.DeskStatus;
import io.github.zenndg.cubit.registry.CubitBlocks;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import io.github.zenndg.cubit.registry.CubitItems;
import io.github.zenndg.cubit.registry.CubitRecipes;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * The Engineering Desk in the world: its two halves, its faces, its order, its pauses and the
 * recipes that left the crafting table (answers 1424 to 1494).
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class EngineeringDeskGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 400;
  private static final int NEXT_TICK = 2;
  private static final int TWO_CRAFTS = 230;
  private static final int SOME_TICKS = 20;
  private static final int FLOOR_Y = 1;
  private static final int MIDDLE_Z = 2;
  private static final int START_X = 4;
  private static final int IRON_FOR_ONE = 7;
  private static final int TWO = 2;
  private static final int FULL_SIGNAL = 15;
  private static final long ENERGY = 50_000;
  private static final long COST_OF_TWO = 5_000;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos DESK = FLOOR.above();
  private static final String TOOLBOX_PATH = "toolbox";
  private static final String ROBOT_PATH = "general_robot";
  private static final ResourceLocation ROBOT =
      ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, ROBOT_PATH);
  private static final ResourceLocation TOOLBOX =
      ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, TOOLBOX_PATH);
  private static final String NO_RIGHT_HALF = "The right half is missing";
  private static final String WRONG_OUTPUT = "Wrong items in the output";
  private static final String WRONG_ENERGY = "Wrong energy after the crafts";
  private static final String WRONG_STATUS = "Wrong status of the desk";
  private static final String MATERIAL_REFUSED = "The left face refused a material";
  private static final String WASTE_TAKEN = "The left face took waste";
  private static final String TOP_OPEN = "The top of the desk is open";
  private static final String TABLE_CRAFTS = "The crafting table still makes items of the mod";
  private static final String WRONG_SIGNAL = "Wrong comparator signal";
  private static final String NOT_PULLED = "The desk did not pull the parts from the output";
  private static final String NESTS = "A desk with items fits in a container";
  private static final String EMPTY_REFUSED = "An empty desk does not fit in a container";

  private EngineeringDeskGameTests() {}

  private static EngineeringDeskBlockEntity place(GameTestHelper helper, long energy) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    ItemStack item = new ItemStack(CubitItems.ENGINEERING_DESK.get());
    item.set(CubitDataComponents.DESK_ENERGY.get(), energy);
    GameTestRobots.use(helper, owner, item, FLOOR);
    return helper.getBlockEntity(DESK);
  }

  private static void fillToolbox(EngineeringDeskBlockEntity desk, int times) {
    desk.input().setItem(0, new ItemStack(Items.IRON_INGOT, IRON_FOR_ONE * times));
    desk.input().setItem(1, new ItemStack(Items.REDSTONE, times));
    desk.input().setItem(2, new ItemStack(Items.OBSIDIAN, times));
  }

  private static BlockPos rightHalf(GameTestHelper helper) {
    Direction right = EngineeringDeskBlock.towardRight(helper.getBlockState(DESK));
    return DESK.relative(right);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theRightHalfGoesToTheRightOfThePlayer(GameTestHelper helper) {
    place(helper, 0);
    helper.assertBlockProperty(rightHalf(helper), EngineeringDeskBlock.PART, DeskPart.RIGHT);
    helper.assertBlockProperty(DESK, EngineeringDeskBlock.PART, DeskPart.LEFT);
    helper.assertTrue(
        helper.getBlockState(rightHalf(helper)).is(CubitBlocks.ENGINEERING_DESK.get()),
        NO_RIGHT_HALF);
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void anOrderOfTwoCraftsTwoItemsAndPaysForEach(GameTestHelper helper) {
    EngineeringDeskBlockEntity desk = place(helper, ENERGY);
    fillToolbox(desk, TWO);
    desk.choose(TOOLBOX);
    desk.adjustOrder(TWO);
    helper.runAfterDelay(
        TWO_CRAFTS,
        () -> {
          helper.assertTrue(desk.output().countItem(CubitItems.TOOLBOX.get()) == TWO, WRONG_OUTPUT);
          helper.assertTrue(desk.input().isEmpty(), WRONG_OUTPUT);
          helper.assertTrue(desk.energy().amount() == ENERGY - COST_OF_TWO, WRONG_ENERGY);
          helper.assertTrue(desk.status() == DeskStatus.IDLE, WRONG_STATUS);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void withoutEnergyItPausesAndKeepsTheMaterials(GameTestHelper helper) {
    EngineeringDeskBlockEntity desk = place(helper, 0);
    fillToolbox(desk, 1);
    desk.choose(TOOLBOX);
    desk.adjustOrder(1);
    helper.runAfterDelay(
        SOME_TICKS,
        () -> {
          helper.assertTrue(desk.status() == DeskStatus.NO_ENERGY, WRONG_STATUS);
          helper.assertTrue(desk.input().countItem(Items.IRON_INGOT) == IRON_FOR_ONE, WRONG_OUTPUT);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void missingMaterialsPause(GameTestHelper helper) {
    EngineeringDeskBlockEntity desk = place(helper, ENERGY);
    desk.choose(TOOLBOX);
    desk.adjustOrder(1);
    helper.runAfterDelay(
        NEXT_TICK,
        () -> {
          helper.assertTrue(desk.status() == DeskStatus.MISSING_MATERIALS, WRONG_STATUS);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRedstoneSignalPauses(GameTestHelper helper) {
    EngineeringDeskBlockEntity desk = place(helper, ENERGY);
    fillToolbox(desk, 1);
    desk.choose(TOOLBOX);
    desk.adjustOrder(1);
    helper.setBlock(DESK.above(), Blocks.REDSTONE_BLOCK);
    helper.runAfterDelay(
        NEXT_TICK,
        () -> {
          helper.assertTrue(desk.status() == DeskStatus.REDSTONE, WRONG_STATUS);
          helper.succeed();
        });
  }

  /** Choosing a recipe makes nothing until plus asks for an amount (answer 1506). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void choosingARecipeStartsNoOrder(GameTestHelper helper) {
    EngineeringDeskBlockEntity desk = place(helper, ENERGY);
    fillToolbox(desk, 1);
    desk.choose(TOOLBOX);
    helper.runAfterDelay(
        SOME_TICKS,
        () -> {
          helper.assertTrue(desk.status() == DeskStatus.IDLE, WRONG_STATUS);
          helper.assertTrue(desk.order().total() == 0, WRONG_STATUS);
          helper.assertTrue(desk.input().countItem(Items.IRON_INGOT) == IRON_FOR_ONE, WRONG_OUTPUT);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theComparatorReadsTheOrder(GameTestHelper helper) {
    EngineeringDeskBlockEntity desk = place(helper, 0);
    desk.choose(TOOLBOX);
    desk.adjustOrder(1);
    helper.runAfterDelay(
        NEXT_TICK,
        () -> {
          helper.assertTrue(desk.signal() == FULL_SIGNAL, WRONG_SIGNAL);
          desk.cancelOrder();
          helper.assertTrue(desk.signal() == 0, WRONG_SIGNAL);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theLeftFaceTakesOnlyMaterialsAndTheTopIsClosed(GameTestHelper helper) {
    place(helper, 0);
    Direction front = helper.getBlockState(DESK).getValue(EngineeringDeskBlock.FACING);
    IItemHandler left =
        helper
            .getLevel()
            .getCapability(
                Capabilities.ItemHandler.BLOCK, helper.absolutePos(DESK), front.getClockWise());
    ItemStack iron = new ItemStack(Items.IRON_INGOT);
    helper.assertTrue(left != null && left.insertItem(0, iron, true).isEmpty(), MATERIAL_REFUSED);
    ItemStack dirt = new ItemStack(Items.DIRT);
    helper.assertTrue(left.insertItem(0, dirt, true).getCount() == 1, WASTE_TAKEN);
    IItemHandler top =
        helper
            .getLevel()
            .getCapability(Capabilities.ItemHandler.BLOCK, helper.absolutePos(DESK), Direction.UP);
    helper.assertTrue(top == null, TOP_OPEN);
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theCraftingTableNoLongerMakesTheToolbox(GameTestHelper helper) {
    CraftingInput input = toolboxGrid();
    var recipes = helper.getLevel().getRecipeManager();
    helper.assertTrue(
        recipes.getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel()).isEmpty(),
        TABLE_CRAFTS);
    helper.assertTrue(
        recipes
            .getRecipeFor(CubitRecipes.ENGINEERING_TYPE.get(), input, helper.getLevel())
            .isPresent(),
        TABLE_CRAFTS);
    helper.succeed();
  }

  private static CraftingInput toolboxGrid() {
    ItemStack iron = new ItemStack(Items.IRON_INGOT);
    ItemStack redstone = new ItemStack(Items.REDSTONE);
    ItemStack obsidian = new ItemStack(Items.OBSIDIAN);
    return CraftingInput.of(
        DeskGrid.SIDE,
        DeskGrid.SIDE,
        List.of(iron, iron, iron, iron, redstone, iron, iron, obsidian, iron));
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aDeskWithItemsNeverNests(GameTestHelper helper) {
    EngineeringDeskBlockEntity desk = place(helper, 0);
    ItemStack empty = new ItemStack(CubitItems.ENGINEERING_DESK.get());
    empty.applyComponents(desk.collectComponents());
    helper.assertTrue(empty.canFitInsideContainerItems(), EMPTY_REFUSED);
    fillToolbox(desk, 1);
    ItemStack full = new ItemStack(CubitItems.ENGINEERING_DESK.get());
    full.applyComponents(desk.collectComponents());
    helper.assertFalse(full.canFitInsideContainerItems(), NESTS);
    helper.succeed();
  }

  /**
   * The parts of a Cubit, one in the input and three in the output, make a Cubit (1500 to 1502).
   */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aCascadePullsTheMissingPartsFromTheOutput(GameTestHelper helper) {
    EngineeringDeskBlockEntity desk = place(helper, ENERGY);
    desk.input().setItem(0, new ItemStack(CubitItems.GENERAL_ROBOT_CASING.get()));
    desk.output().setItem(0, new ItemStack(CubitItems.GENERAL_ROBOT_VISOR.get()));
    desk.output().setItem(1, new ItemStack(CubitItems.GENERAL_ROBOT_TREADS.get()));
    desk.output().setItem(2, new ItemStack(CubitItems.GENERAL_ROBOT_CORE.get()));
    desk.choose(ROBOT);
    desk.adjustOrder(1);
    helper.runAfterDelay(
        NEXT_TICK,
        () -> {
          helper.assertTrue(desk.status() == DeskStatus.CRAFTING, WRONG_STATUS);
          helper.assertTrue(
              desk.input().countItem(CubitItems.GENERAL_ROBOT_CORE.get()) == 1, NOT_PULLED);
          helper.assertTrue(
              desk.output().countItem(CubitItems.GENERAL_ROBOT_VISOR.get()) == 0, NOT_PULLED);
          helper.succeed();
        });
  }

  /** Without every part, between the input and the output, nothing moves (answer 1502). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void anIncompleteCascadeMovesNothing(GameTestHelper helper) {
    EngineeringDeskBlockEntity desk = place(helper, ENERGY);
    desk.output().setItem(0, new ItemStack(CubitItems.GENERAL_ROBOT_VISOR.get()));
    desk.choose(ROBOT);
    desk.adjustOrder(1);
    helper.runAfterDelay(
        NEXT_TICK,
        () -> {
          helper.assertTrue(desk.status() == DeskStatus.MISSING_MATERIALS, WRONG_STATUS);
          helper.assertTrue(
              desk.output().countItem(CubitItems.GENERAL_ROBOT_VISOR.get()) == 1, NOT_PULLED);
          helper.succeed();
        });
  }
}
