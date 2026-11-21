package io.github.zenndg.cubit.gametest;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.block.InductionDockBlock;
import io.github.zenndg.cubit.block.InductionDockBlockEntity;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.entity.RobotEntity;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import io.github.zenndg.cubit.registry.CubitItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The Induction Dock goes up with the Modules of the Cubit (answers 1373 to 1382). */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DockTierGameTests {
  private static final String ARENA = "arena";
  private static final Energy PLENTY = new Energy(1_000_000);
  private static final Energy LOW = new Energy(20_000);
  private static final Energy KEPT = new Energy(200_000);
  private static final int CHARGE_TICKS = 10;
  private static final long BASE_RATE = 500;
  private static final int BASE_SLOTS = 27;
  private static final int ADVANCED_SLOTS = 36;
  private static final long ADVANCED_CAPACITY = 1_200_000;
  private static final String WRONG_TIER = "Wrong tier of the dock";
  private static final String WRONG_HAND = "Wrong Module left in the hand";
  private static final String WRONG_NUMBERS = "Wrong numbers of the tier";
  private static final String WRONG_STATE = "Both halves should show the tier";
  private static final String SLOW = "The robot should charge at the rate of the dock";
  private static final String WRONG_SLOT = "Wrong slot open for the tier";

  private DockTierGameTests() {}

  @GameTest(template = ARENA)
  public static void theModuleOfTheNextTierRaisesTheDockAndIsUsedUp(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, DockScene.FLOOR, KEPT);

    click(helper, owner, new ItemStack(CubitItems.ADVANCED_MODULE.get()));

    helper.assertValueEqual(dock.tier(), RobotTier.ADVANCED, WRONG_TIER);
    helper.assertTrue(owner.getMainHandItem().isEmpty(), WRONG_HAND);
    helper.assertValueEqual(dock.capacity().amount(), ADVANCED_CAPACITY, WRONG_NUMBERS);
    helper.assertValueEqual(dock.storageSlots(), ADVANCED_SLOTS, WRONG_NUMBERS);
    helper.assertValueEqual(dock.energy(), KEPT, WRONG_NUMBERS);
    helper.assertValueEqual(tierShown(helper, DockScene.DOCK), RobotTier.ADVANCED, WRONG_STATE);
    helper.assertValueEqual(
        tierShown(helper, DockScene.DOCK.above()), RobotTier.ADVANCED, WRONG_STATE);
    helper.succeed();
  }

  /** As in the Cubit, a Module that skips a tier stays in the hand (answer 1377). */
  @GameTest(template = ARENA)
  public static void aModuleThatSkipsATierStaysInTheHand(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, DockScene.FLOOR, PLENTY);

    click(helper, owner, new ItemStack(CubitItems.ELITE_MODULE.get()));

    helper.assertValueEqual(dock.tier(), RobotTier.BASE, WRONG_TIER);
    helper.assertTrue(owner.getMainHandItem().is(CubitItems.ELITE_MODULE.get()), WRONG_HAND);
    helper.succeed();
  }

  /** Only the owner raises the dock (answer 1378). */
  @GameTest(template = ARENA)
  public static void anotherPlayerCannotRaiseTheDock(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, DockScene.FLOOR, PLENTY);
    Player other = helper.makeMockPlayer(GameType.SURVIVAL);

    click(helper, other, new ItemStack(CubitItems.ADVANCED_MODULE.get()));

    helper.assertValueEqual(dock.tier(), RobotTier.BASE, WRONG_TIER);
    helper.assertTrue(other.getMainHandItem().is(CubitItems.ADVANCED_MODULE.get()), WRONG_HAND);
    helper.succeed();
  }

  /** The item keeps the tier, and the dock comes back with it (answer 1373). */
  @GameTest(template = ARENA)
  public static void theItemOfTheDockKeepsTheTier(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, DockScene.FLOOR, PLENTY);
    click(helper, owner, new ItemStack(CubitItems.ADVANCED_MODULE.get()));
    ItemStack item = new ItemStack(CubitItems.INDUCTION_DOCK.get());
    item.applyComponents(dock.collectComponents());
    helper.assertValueEqual(
        item.get(CubitDataComponents.DOCK_TIER.get()), RobotTier.ADVANCED, WRONG_TIER);

    helper.destroyBlock(DockScene.DOCK);
    GameTestRobots.use(helper, owner, item, DockScene.FLOOR);
    InductionDockBlockEntity placed = helper.getBlockEntity(DockScene.DOCK);

    helper.assertValueEqual(placed.tier(), RobotTier.ADVANCED, WRONG_TIER);
    helper.assertValueEqual(tierShown(helper, DockScene.DOCK), RobotTier.ADVANCED, WRONG_STATE);
    helper.succeed();
  }

  /** The Base keeps its 27 slots, and the next tier opens a row more (answer 1374). */
  @GameTest(template = ARENA)
  public static void theTierOpensTheSlotsOfTheStorage(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, DockScene.FLOOR, PLENTY);
    ItemStack toolbox = new ItemStack(CubitItems.TOOLBOX.get());

    helper.assertTrue(dock.storage().canPlaceItem(BASE_SLOTS - 1, toolbox), WRONG_SLOT);
    helper.assertFalse(dock.storage().canPlaceItem(BASE_SLOTS, toolbox), WRONG_SLOT);
    click(helper, owner, new ItemStack(CubitItems.ADVANCED_MODULE.get()));
    helper.assertTrue(dock.storage().canPlaceItem(BASE_SLOTS, toolbox), WRONG_SLOT);
    helper.assertTrue(
        dock.storage()
            .canPlaceItem(BASE_SLOTS, new ItemStack(CubitItems.GENERAL_ROBOT_CASING.get())),
        WRONG_SLOT);
    helper.succeed();
  }

  /** A Base Cubit in an Ultimate dock charges at the rate of the dock (answer 1375). */
  @GameTest(template = ARENA)
  public static void theRobotChargesAtTheRateOfTheDock(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    ItemStack item = new ItemStack(CubitItems.INDUCTION_DOCK.get());
    item.set(CubitDataComponents.DOCK_ENERGY.get(), PLENTY.amount());
    item.set(CubitDataComponents.DOCK_TIER.get(), RobotTier.ULTIMATE);
    GameTestRobots.use(helper, owner, item, DockScene.FLOOR);
    RobotEntity robot = DockScene.placeOnDock(helper, owner, LOW);
    long before = chargeOf(robot);

    helper.runAfterDelay(
        CHARGE_TICKS,
        () -> {
          long gained = chargeOf(robot) - before;
          helper.assertTrue(gained > BASE_RATE * CHARGE_TICKS, SLOW);
          helper.succeed();
        });
  }

  /** In survival, so the Module is used up as in the game. */
  private static void click(GameTestHelper helper, Player player, ItemStack stack) {
    if (player instanceof ServerPlayer serverPlayer) {
      serverPlayer.setGameMode(GameType.SURVIVAL);
    }
    player.setItemInHand(InteractionHand.MAIN_HAND, stack);
    DockScene.clickWithHeldItem(helper, player, DockScene.DOCK);
  }

  private static long chargeOf(RobotEntity robot) {
    return robot.state().energy().amount();
  }

  private static RobotTier tierShown(GameTestHelper helper, BlockPos pos) {
    return helper.getBlockState(pos).getValue(InductionDockBlock.TIER).tier();
  }
}
