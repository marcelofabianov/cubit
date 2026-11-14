package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.RobotNotice;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.robot.TabletApp;
import io.github.marcelofabianov.cubit.domain.robot.UnloadCall;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotNotices;
import io.github.marcelofabianov.cubit.item.TabletItem;
import io.github.marcelofabianov.cubit.player.TabletSelection;
import io.github.marcelofabianov.cubit.player.TabletUse;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The unload: the Unload app marks the chests, `Unload` sends any Cubit, and only a full Cubit in
 * its area goes by itself, in the order of the marks (answers 1086 to 1095).
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class UnloadGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 400;
  private static final int SETTLE_TICKS = 60;
  private static final int FLOOR_Y = 1;
  private static final BlockPos ROBOT_FLOOR = new BlockPos(4, FLOOR_Y, 2);
  private static final BlockPos CHEST = new BlockPos(8, FLOOR_Y + 1, 0);
  private static final BlockPos OTHER_CHEST = new BlockPos(9, FLOOR_Y + 1, 0);
  private static final BlockPos STONE = new BlockPos(10, FLOOR_Y + 1, 0);
  private static final BlockPos FIRST_CORNER = new BlockPos(14, FLOOR_Y, 0);
  private static final BlockPos SECOND_CORNER = new BlockPos(20, FLOOR_Y, 4);
  private static final Energy CHARGE = new Energy(300_000);
  private static final float HEALTH = 50f;
  private static final int STACK = 64;
  private static final String NOT_MARKED = "The Unload app did not mark the chest";
  private static final String STILL_MARKED = "The chest has the wrong mark";
  private static final String NOT_UNLOADED = "The Cubit did not unload in the chest";
  private static final String UNLOADED = "The Cubit should not unload by itself";
  private static final String NOT_SAID = "The Cubit did not say it unloaded";
  private static final String WRONG_CALL = "Unload gave the wrong answer";
  private static final String NOT_SAVED = "The chests were not saved";

  private UnloadGameTests() {}

  @GameTest(template = ARENA)
  public static void theUnloadAppMarksAndUnmarksAChest(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.BASE);
    helper.setBlock(CHEST, Blocks.CHEST);
    ItemStack tablet = tablet();

    TabletUse.click(owner, tablet, block(helper, CHEST));
    helper.assertValueEqual(robot.docking().unload().chests().chests().size(), 1, NOT_MARKED);
    owner.setShiftKeyDown(true);
    TabletUse.click(owner, tablet, block(helper, CHEST));
    owner.setShiftKeyDown(false);

    helper.assertTrue(robot.docking().unload().chests().isEmpty(), STILL_MARKED);
    helper.succeed();
  }

  /**
   * Shift and a click on any block take out the marked chest, so a new one far away can take its
   * place, even with the limit of one chest (answer 1099).
   */
  @GameTest(template = ARENA)
  public static void shiftAndAClickAnywhereFreeThePlaceForANewChest(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.BASE);
    helper.setBlock(CHEST, Blocks.CHEST);
    helper.setBlock(OTHER_CHEST, Blocks.BARREL);
    helper.setBlock(STONE, Blocks.STONE);
    ItemStack tablet = tablet();
    TabletUse.click(owner, tablet, block(helper, CHEST));

    owner.setShiftKeyDown(true);
    TabletUse.click(owner, tablet, block(helper, STONE));
    owner.setShiftKeyDown(false);
    TabletUse.click(owner, tablet, block(helper, OTHER_CHEST));

    helper.assertFalse(robot.docking().unload().marks(helper.absolutePos(CHEST)), STILL_MARKED);
    helper.assertTrue(robot.docking().unload().marks(helper.absolutePos(OTHER_CHEST)), NOT_MARKED);
    helper.succeed();
  }

  /** A click on a marked chest sends the Cubit to unload only there (answer 1098). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aClickOnAMarkedChestUnloadsThere(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.BASE);
    helper.setBlock(CHEST, Blocks.CHEST);
    ItemStack tablet = tablet();
    TabletUse.click(owner, tablet, block(helper, CHEST));
    robot.inventory().setItem(0, new ItemStack(Items.COBBLESTONE, STACK));

    TabletUse.click(owner, tablet, block(helper, CHEST));

    helper.assertTrue(robot.docking().unload().marks(helper.absolutePos(CHEST)), STILL_MARKED);
    helper.succeedWhen(() -> helper.assertTrue(chestHas(helper, CHEST), NOT_UNLOADED));
  }

  @GameTest(template = ARENA)
  public static void theTierLimitsTheChestsAndABlockNeedsAnInventory(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.BASE);
    helper.setBlock(CHEST, Blocks.CHEST);
    helper.setBlock(OTHER_CHEST, Blocks.BARREL);
    helper.setBlock(STONE, Blocks.STONE);
    ItemStack tablet = tablet();

    TabletUse.click(owner, tablet, block(helper, STONE));
    TabletUse.click(owner, tablet, block(helper, CHEST));
    TabletUse.click(owner, tablet, block(helper, OTHER_CHEST));

    helper.assertValueEqual(robot.docking().unload().chests().chests().size(), 1, NOT_MARKED);
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theButtonUnloadsTheCubitThatFollowsAndItSaysSo(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.BASE);
    helper.setBlock(CHEST, Blocks.CHEST);
    TabletUse.click(owner, tablet(), block(helper, CHEST));
    robot.inventory().setItem(0, new ItemStack(Items.COBBLESTONE, STACK));

    helper.assertValueEqual(robot.docking().unload().goal(), UnloadCall.READY, WRONG_CALL);
    helper.assertTrue(robot.docking().unload().unload(owner), WRONG_CALL);

    helper.succeedWhen(() -> {
      helper.assertTrue(chestHas(helper, CHEST), NOT_UNLOADED);
      helper.assertTrue(robot.inventory().isEmpty(), NOT_UNLOADED);
      helper.assertTrue(RobotNotices.isHolding(robot, RobotNotice.ITEMS_UNLOADED), NOT_SAID);
    });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aFullCubitInItsAreaUnloadsByItself(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.ADVANCED);
    helper.setBlock(CHEST, Blocks.CHEST);
    ItemStack tablet = tablet();
    TabletUse.click(owner, tablet, block(helper, CHEST));
    markArea(helper, owner, robot);
    fill(robot.inventory());

    helper.succeedWhen(() -> helper.assertTrue(chestHas(helper, CHEST), NOT_UNLOADED));
  }

  /** The Cubit that follows only unloads by the button (answer 1087). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aFullCubitThatFollowsDoesNotUnloadByItself(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.BASE);
    helper.setBlock(CHEST, Blocks.CHEST);
    TabletUse.click(owner, tablet(), block(helper, CHEST));
    fill(robot.inventory());

    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertFalse(chestHas(helper, CHEST), UNLOADED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void withoutRoomTheButtonSaysWhy(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.BASE);
    helper.setBlock(CHEST, Blocks.CHEST);
    TabletUse.click(owner, tablet(), block(helper, CHEST));
    ChestBlockEntity chest = helper.getBlockEntity(CHEST);
    fill(chest);

    helper.assertValueEqual(robot.docking().unload().goal(), UnloadCall.EMPTY, WRONG_CALL);
    robot.inventory().setItem(0, new ItemStack(Items.DIRT, STACK));
    helper.assertFalse(robot.docking().unload().unload(owner), WRONG_CALL);
    helper.assertValueEqual(robot.docking().unload().goal(), UnloadCall.NO_CHEST, WRONG_CALL);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theChestsAreSavedWithTheCubit(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.BASE);
    helper.setBlock(CHEST, Blocks.CHEST);
    TabletUse.click(owner, tablet(), block(helper, CHEST));

    CompoundTag saved = robot.saveWithoutId(new CompoundTag());
    RobotEntity loaded = GameTestRobots.loadRobot(helper, saved);

    helper.assertValueEqual(
        loaded.docking().unload().chests(), robot.docking().unload().chests(), NOT_SAVED);
    helper.succeed();
  }

  private static void markArea(GameTestHelper helper, ServerPlayer owner, RobotEntity robot) {
    ItemStack tablet = new ItemStack(CubitItems.TABLET.get());
    TabletItem.setApp(tablet, TabletApp.AREA);
    TabletUse.click(owner, tablet, new TabletUse.Living(robot));
    TabletUse.click(owner, tablet, block(helper, FIRST_CORNER));
    TabletUse.click(owner, tablet, block(helper, SECOND_CORNER));
  }

  private static void fill(Container container) {
    for (int slot = 0; slot < container.getContainerSize(); slot++) {
      container.setItem(slot, new ItemStack(Items.COBBLESTONE, STACK));
    }
  }

  private static boolean chestHas(GameTestHelper helper, BlockPos pos) {
    ChestBlockEntity chest = helper.getBlockEntity(pos);
    return !chest.isEmpty();
  }

  private static TabletUse.Block block(GameTestHelper helper, BlockPos pos) {
    return new TabletUse.Block(helper.absolutePos(pos), Optional.empty());
  }

  private static RobotEntity chosenRobot(
      GameTestHelper helper, ServerPlayer owner, RobotTier tier) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(CubitDataComponents.ROBOT_STATE.get(), new RobotState(tier, CHARGE, HEALTH));
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item, ROBOT_FLOOR);
    TabletSelection.choose(owner, robot.getUUID());
    return robot;
  }

  private static ItemStack tablet() {
    ItemStack tablet = new ItemStack(CubitItems.TABLET.get());
    TabletItem.setApp(tablet, TabletApp.UNLOAD);
    return tablet;
  }
}
