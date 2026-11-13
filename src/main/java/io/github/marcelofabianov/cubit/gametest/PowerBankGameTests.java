package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.item.PowerBankItem;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The Power Bank in the Cubit, in the Dock and in the crafting grid (answers 900 to 917). */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class PowerBankGameTests {
  private static final String ARENA = "arena";
  private static final String EMPTY = "empty";
  private static final String LOST = "Energy was created or lost";
  private static final String NOT_CHARGED = "The energy did not move";
  private static final String NOT_IN_SLOT = "The Power Bank should be in the slot";
  private static final String WRONG_RECIPE = "Wrong answer of the recipe";
  private static final String WRONG_CHARGE = "Wrong charge";
  private static final BlockPos FLOOR = new BlockPos(1, 1, 1);
  private static final int SLOT = 0;
  private static final int ONE = 1;
  private static final int TWO = 2;
  private static final int THREE = 3;
  private static final Energy SOME = new Energy(1_000);
  private static final Energy PLENTY = new Energy(100_000);
  private static final Energy CARRIED = new Energy(150_000);
  private static final Energy BASE_CAPACITY = new Energy(210_000);
  private static final Energy WAKE_CHARGE = new Energy(15_000);
  private static final float HEALTH = 20f;

  private PowerBankGameTests() {}

  @GameTest(template = EMPTY)
  public static void aPowerBankIsSingleAndHoldsTheNumberOfItsTier(GameTestHelper helper) {
    ItemStack bank = new ItemStack(CubitItems.BASE_POWER_BANK.get());

    helper.assertValueEqual(bank.getMaxStackSize(), ONE, WRONG_CHARGE);
    helper.assertValueEqual(
        PowerBankItem.batteryOf(bank), new Battery(BASE_CAPACITY, Energy.ZERO), WRONG_CHARGE);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theBankChargesTheRobotAndStaysEmptyInTheSlot(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = DockScene.placeRobot(helper, owner, FLOOR, Energy.ZERO);
    ItemStack bank = bank(CubitItems.BASE_POWER_BANK.get().getDefaultInstance(), SOME);
    robot.energySlot().setItem(SLOT, bank);

    helper.succeedWhen(() -> {
      ItemStack inSlot = robot.energySlot().getItem(SLOT);
      helper.assertTrue(PowerBankItem.is(inSlot), NOT_IN_SLOT);
      helper.assertTrue(PowerBankItem.batteryOf(inSlot).isEmpty(), NOT_CHARGED);
      helper.assertValueEqual(robot.state().energy(), SOME, LOST);
    });
  }

  @GameTest(template = ARENA)
  public static void theDockChargesTheBankInItsChargeSlot(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, DockScene.FLOOR, PLENTY);
    dock.chargeSlot().setItem(SLOT, CubitItems.BASE_POWER_BANK.get().getDefaultInstance());

    helper.succeedWhen(() -> {
      Energy inBank = PowerBankItem.batteryOf(dock.chargeSlot().getItem(SLOT)).charge();
      helper.assertFalse(inBank.isZero(), NOT_CHARGED);
      helper.assertValueEqual(inBank.plus(dock.energy()), PLENTY, LOST);
    });
  }

  @GameTest(template = ARENA)
  public static void aBankInTheInputSlotEmptiesIntoTheStock(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    InductionDockBlockEntity dock =
        DockScene.placeDock(helper, owner, DockScene.FLOOR, Energy.ZERO);
    dock.slot().setItem(SLOT, bank(CubitItems.BASE_POWER_BANK.get().getDefaultInstance(), PLENTY));

    helper.succeedWhen(() -> {
      ItemStack inSlot = dock.slot().getItem(SLOT);
      helper.assertTrue(PowerBankItem.is(inSlot), NOT_IN_SLOT);
      helper.assertTrue(PowerBankItem.batteryOf(inSlot).isEmpty(), NOT_CHARGED);
      helper.assertValueEqual(dock.energy(), PLENTY, LOST);
    });
  }

  @GameTest(template = ARENA)
  public static void theChargeSlotTakesOnlyAPowerBank(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, DockScene.FLOOR, PLENTY);

    helper.assertFalse(
        dock.chargeSlot().canPlaceItem(SLOT, new ItemStack(Items.REDSTONE)), WRONG_RECIPE);
    helper.assertTrue(
        dock.chargeSlot().canPlaceItem(SLOT, CubitItems.BASE_POWER_BANK.get().getDefaultInstance()),
        NOT_IN_SLOT);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aClickOnTheDockPutsTheBankInTheChargeSlot(GameTestHelper helper) {
    Player owner = DockScene.ownerBeside(helper);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, DockScene.FLOOR, PLENTY);

    owner.setItemInHand(
        InteractionHand.MAIN_HAND, CubitItems.BASE_POWER_BANK.get().getDefaultInstance());
    DockScene.clickWithHeldItem(helper, owner, DockScene.DOCK);

    helper.assertTrue(PowerBankItem.is(dock.chargeSlot().getItem(SLOT)), NOT_IN_SLOT);
    helper.assertTrue(dock.slot().isEmpty(), NOT_IN_SLOT);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aClickWithTheBankPutsItInTheSlotOfAnEmptyRobot(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = DockScene.placeRobot(helper, owner, FLOOR, Energy.ZERO);

    GameTestRobots.clickRobot(
        owner, robot, bank(CubitItems.BASE_POWER_BANK.get().getDefaultInstance(), SOME));

    helper.assertTrue(PowerBankItem.is(robot.energySlot().getItem(SLOT)), NOT_IN_SLOT);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void theGridWakesARobotWithWhatIsMissing(GameTestHelper helper) {
    ItemStack bank = bank(CubitItems.BASE_POWER_BANK.get().getDefaultInstance(), PLENTY);
    CraftingInput grid = CraftingInput.of(TWO, ONE, List.of(robotItem(Energy.ZERO), bank));
    RecipeHolder<CraftingRecipe> recipe = recipeFor(helper, grid).orElseThrow();

    ItemStack woken = recipe.value().assemble(grid, helper.getLevel().registryAccess());
    ItemStack left = recipe.value().getRemainingItems(grid).get(ONE);

    RobotState state = woken.get(CubitDataComponents.ROBOT_STATE.get());
    helper.assertValueEqual(state.energy(), WAKE_CHARGE, WRONG_CHARGE);
    helper.assertValueEqual(
        PowerBankItem.batteryOf(left).charge(), PLENTY.minus(WAKE_CHARGE), LOST);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void theGridRefusesAShortBankAndADamagedCubit(GameTestHelper helper) {
    ItemStack shortBank = bank(CubitItems.BASE_POWER_BANK.get().getDefaultInstance(), SOME);
    ItemStack fullBank = bank(CubitItems.BASE_POWER_BANK.get().getDefaultInstance(), PLENTY);
    ItemStack damaged = new ItemStack(CubitItems.DAMAGED_GENERAL_ROBOT.get());
    damaged.set(
        CubitDataComponents.ROBOT_STATE.get(), new RobotState(RobotTier.BASE, Energy.ZERO, 0f));

    helper.assertTrue(
        recipeFor(helper, CraftingInput.of(TWO, ONE, List.of(robotItem(Energy.ZERO), shortBank)))
            .isEmpty(),
        WRONG_RECIPE);
    helper.assertTrue(
        recipeFor(helper, CraftingInput.of(TWO, ONE, List.of(damaged, fullBank))).isEmpty(),
        WRONG_RECIPE);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void theNextBankKeepsTheCharge(GameTestHelper helper) {
    ItemStack base = bank(CubitItems.BASE_POWER_BANK.get().getDefaultInstance(), CARRIED);
    ItemStack gold = new ItemStack(Items.GOLD_INGOT);
    ItemStack quartz = new ItemStack(Items.QUARTZ);
    CraftingInput grid =
        CraftingInput.of(
            THREE, THREE, List.of(gold, quartz, gold, quartz, base, quartz, gold, quartz, gold));

    ItemStack next =
        recipeFor(helper, grid)
            .orElseThrow()
            .value()
            .assemble(grid, helper.getLevel().registryAccess());

    helper.assertTrue(next.is(CubitItems.ADVANCED_POWER_BANK.get()), WRONG_RECIPE);
    helper.assertValueEqual(PowerBankItem.batteryOf(next).charge(), CARRIED, LOST);
    helper.succeed();
  }

  private static ItemStack bank(ItemStack bank, Energy charge) {
    bank.set(CubitDataComponents.POWER_BANK_ENERGY.get(), charge.amount());
    return bank;
  }

  private static ItemStack robotItem(Energy energy) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(CubitDataComponents.ROBOT_STATE.get(), new RobotState(RobotTier.BASE, energy, HEALTH));
    return item;
  }

  private static Optional<RecipeHolder<CraftingRecipe>> recipeFor(
      GameTestHelper helper, CraftingInput grid) {
    return helper
        .getLevel()
        .getRecipeManager()
        .getRecipeFor(RecipeType.CRAFTING, grid, helper.getLevel());
  }
}
