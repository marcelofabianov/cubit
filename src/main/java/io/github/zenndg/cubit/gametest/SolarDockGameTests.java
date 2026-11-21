package io.github.zenndg.cubit.gametest;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.block.InductionDockBlockEntity;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import io.github.zenndg.cubit.registry.CubitItems;
import io.github.zenndg.cubit.registry.CubitRecipes;
import java.util.List;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The Solar Dock, made of the panel and a dock, under the sun and without it (1384 to 1392). */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class SolarDockGameTests {
  private static final String ARENA = "arena";
  private static final String EMPTY = "empty";
  private static final int TIMEOUT = 100;
  private static final int SUN_TICKS = 20;
  private static final int GRID = 3;
  private static final int SECOND_SLOT = 1;
  private static final long BASE_SOLAR = 25;
  private static final Energy KEPT = new Energy(120_000);
  private static final String NOT_GENERATED = "The panel should generate under the sun";
  private static final String GENERATED = "The panel should not generate without the sun";
  private static final String WRONG_RESULT = "Wrong result of the assembly";
  private static final String LOST = "The Solar Dock should keep the state of the dock";

  private SolarDockGameTests() {}

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = SolarGameTests.DAYLIGHT_BATCH)
  public static void theSolarDockGeneratesUnderTheSun(GameTestHelper helper) {
    InductionDockBlockEntity dock = place(helper, CubitItems.SOLAR_INDUCTION_DOCK.get());

    helper.runAfterDelay(
        SUN_TICKS,
        () -> {
          helper.assertTrue(dock.energy().amount() > 0, NOT_GENERATED);
          helper.assertValueEqual(dock.solar().given().amount(), BASE_SOLAR, NOT_GENERATED);
          helper.succeed();
        });
  }

  /** A block over the panel takes the sun away (answer 1384). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = SolarGameTests.DAYLIGHT_BATCH)
  public static void aRoofOverThePanelStopsIt(GameTestHelper helper) {
    InductionDockBlockEntity dock = place(helper, CubitItems.SOLAR_INDUCTION_DOCK.get());
    helper.setBlock(DockScene.DOCK.above(2), Blocks.STONE);

    assertNothingAfterAWhile(helper, dock);
  }

  /** Light that passes through glass still counts, as for the panel of the Cubit (1384). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = SolarGameTests.DAYLIGHT_BATCH)
  public static void glassOverThePanelLetsTheSunThrough(GameTestHelper helper) {
    InductionDockBlockEntity dock = place(helper, CubitItems.SOLAR_INDUCTION_DOCK.get());
    helper.setBlock(DockScene.DOCK.above(2), Blocks.GLASS);

    helper.runAfterDelay(
        SUN_TICKS,
        () -> {
          helper.assertTrue(dock.energy().amount() > 0, NOT_GENERATED);
          helper.succeed();
        });
  }

  /** A dock without the panel never generates, even under the sun. */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = SolarGameTests.DAYLIGHT_BATCH)
  public static void aNormalDockDoesNotGenerate(GameTestHelper helper) {
    InductionDockBlockEntity dock = place(helper, CubitItems.INDUCTION_DOCK.get());

    assertNothingAfterAWhile(helper, dock);
  }

  /** The default batch starts at midnight, when the Solar Dock is a normal one. */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void atNightTheSolarDockIsANormalDock(GameTestHelper helper) {
    InductionDockBlockEntity dock = place(helper, CubitItems.SOLAR_INDUCTION_DOCK.get());

    assertNothingAfterAWhile(helper, dock);
  }

  /** The panel and a dock make the Solar Dock, with the tier and the energy (answer 1384). */
  @GameTest(template = EMPTY)
  public static void theAssemblyKeepsTheStateOfTheDock(GameTestHelper helper) {
    ItemStack dock = new ItemStack(CubitItems.INDUCTION_DOCK.get());
    dock.set(CubitDataComponents.DOCK_TIER.get(), RobotTier.ELITE);
    dock.set(CubitDataComponents.DOCK_ENERGY.get(), KEPT.amount());
    List<ItemStack> grid =
        List.of(
            new ItemStack(CubitItems.DOCK_SOLAR_PANEL.get()),
            dock,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY);

    ItemStack result = craft(helper, grid);

    helper.assertTrue(result.is(CubitItems.SOLAR_INDUCTION_DOCK.get()), WRONG_RESULT);
    helper.assertValueEqual(result.get(CubitDataComponents.DOCK_TIER.get()), RobotTier.ELITE, LOST);
    helper.assertValueEqual(result.get(CubitDataComponents.DOCK_ENERGY.get()), KEPT.amount(), LOST);
    helper.assertTrue(grid.get(SECOND_SLOT).is(CubitItems.INDUCTION_DOCK.get()), LOST);
    helper.succeed();
  }

  private static InductionDockBlockEntity place(GameTestHelper helper, Item item) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    GameTestRobots.use(helper, owner, new ItemStack(item), DockScene.FLOOR);
    return helper.getBlockEntity(DockScene.DOCK);
  }

  private static void assertNothingAfterAWhile(
      GameTestHelper helper, InductionDockBlockEntity dock) {
    helper.runAfterDelay(
        SUN_TICKS,
        () -> {
          helper.assertValueEqual(dock.energy(), Energy.ZERO, GENERATED);
          helper.succeed();
        });
  }

  private static ItemStack craft(GameTestHelper helper, List<ItemStack> grid) {
    ServerLevel level = helper.getLevel();
    CraftingInput input = CraftingInput.of(GRID, GRID, grid);
    return level
        .getRecipeManager()
        .getRecipeFor(CubitRecipes.ENGINEERING_TYPE.get(), input, level)
        .map(recipe -> recipe.value().assemble(input, level.registryAccess()))
        .orElse(ItemStack.EMPTY);
  }
}
