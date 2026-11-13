package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.OreFilter;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotScreenReadout;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The robot places torches where mobs could spawn (answers 971 to 984). The arena is 24 blocks long
 * and 5 wide, with its stone floor at y=1. A stone roof at y=4 turns it into a cave.
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class TorchGameTests {
  private static final String ARENA = "arena";
  private static final int PLACE_TIMEOUT = 200;
  private static final int WAIT_TICKS = 100;
  private static final int LENGTH = 24;
  private static final int WIDTH = 5;
  private static final int ROOF_Y = 4;
  private static final int SEARCH_MARGIN = 6;
  private static final int SLOT = 0;
  private static final int TORCHES = 16;
  private static final long TORCH_COST = 20;
  private static final float HEALTH = 50f;
  private static final Energy CHARGE = new Energy(100_000);
  private static final BlockPos FLOOR = new BlockPos(12, 1, 2);
  private static final String NOT_PLACED = "The robot should have placed a torch";
  private static final String PLACED = "The robot should not have placed a torch";
  private static final String WRONG_COST = "A torch should cost its number once";
  private static final String WRONG_SLOT = "The torch slot took the wrong item";
  private static final String NOT_KEPT = "The item should keep the torches and the switch";
  private static final String WRONG_SWITCH = "Wrong answer of the torch switch";

  private TorchGameTests() {}

  @GameTest(template = ARENA, timeoutTicks = PLACE_TIMEOUT)
  public static void theRobotLightsADarkCave(GameTestHelper helper) {
    buildRoof(helper);
    RobotEntity robot = withTorches(helper, place(helper, RobotTier.ADVANCED));
    long before = robot.state().energy().amount();

    helper.succeedWhen(() -> {
      helper.assertTrue(robot.torches().slot().getItem(SLOT).getCount() < TORCHES, NOT_PLACED);
      int used = TORCHES - robot.torches().slot().getItem(SLOT).getCount();
      helper.assertValueEqual(countTorches(helper), used, NOT_PLACED);
      helper.assertTrue(before - robot.state().energy().amount() >= used * TORCH_COST, WRONG_COST);
    });
  }

  @GameTest(template = ARENA, timeoutTicks = PLACE_TIMEOUT)
  public static void baseNeverPlacesTorches(GameTestHelper helper) {
    buildRoof(helper);
    RobotEntity robot = place(helper, RobotTier.BASE);

    helper.assertFalse(
        robot.torches().slot().canPlaceItem(SLOT, new ItemStack(Items.TORCH)), WRONG_SLOT);
    helper.runAfterDelay(WAIT_TICKS, () -> assertNoTorch(helper));
  }

  @GameTest(template = ARENA, timeoutTicks = PLACE_TIMEOUT)
  public static void withTheSwitchOffNoTorchIsPlaced(GameTestHelper helper) {
    buildRoof(helper);
    RobotEntity robot = withTorches(helper, place(helper, RobotTier.ADVANCED));
    robot.torches().toggle();

    helper.runAfterDelay(WAIT_TICKS, () -> assertNoTorch(helper));
  }

  /**
   * A cave already lit gets no torch, so none is wasted (answer 971). The rule for the sky by day
   * lives in the unit tests, because changing the time here would change it for every test.
   */
  @GameTest(template = ARENA, timeoutTicks = PLACE_TIMEOUT)
  public static void aLitCaveGetsNoTorch(GameTestHelper helper) {
    buildRoof(helper, Blocks.GLOWSTONE);
    withTorches(helper, place(helper, RobotTier.ADVANCED));

    helper.runAfterDelay(WAIT_TICKS, () -> assertNoTorch(helper));
  }

  @GameTest(template = ARENA)
  public static void theSlotTakesOnlyPlainTorches(GameTestHelper helper) {
    RobotEntity robot = place(helper, RobotTier.ADVANCED);

    helper.assertTrue(
        robot.torches().slot().canPlaceItem(SLOT, new ItemStack(Items.TORCH)), WRONG_SLOT);
    helper.assertFalse(
        robot.torches().slot().canPlaceItem(SLOT, new ItemStack(Items.SOUL_TORCH)), WRONG_SLOT);
    helper.assertFalse(
        robot.torches().slot().canPlaceItem(SLOT, new ItemStack(Items.DIRT)), WRONG_SLOT);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theItemAndTheSaveKeepTheTorches(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item(RobotTier.ADVANCED), FLOOR);
    withTorches(helper, robot);
    robot.torches().toggle();

    RobotEntity loaded = GameTestRobots.loadRobot(helper, robot.saveWithoutId(new CompoundTag()));
    helper.assertValueEqual(loaded.torches().slot().getItem(SLOT).getCount(), TORCHES, NOT_KEPT);
    helper.assertFalse(loaded.torches().isOn(), NOT_KEPT);

    ItemStack collected = GameTestRobots.collect(owner, robot);
    helper.assertFalse(collected.get(CubitDataComponents.ROBOT_STATE.get()).torchesOn(), NOT_KEPT);
    helper.assertValueEqual(
        collected.get(CubitDataComponents.TORCH_SLOT.get()).copyOne().getCount(),
        TORCHES,
        NOT_KEPT);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theTorchButtonTogglesTheSwitch(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item(RobotTier.ADVANCED), FLOOR);
    owner.moveTo(robot.getX(), robot.getY(), robot.getZ());
    RobotMenu menu = RobotScreenReadout.serverMenu(0, owner.getInventory(), robot);

    helper.assertTrue(menu.clickMenuButton(owner, RobotMenu.TORCHES_BUTTON), WRONG_SWITCH);
    helper.assertFalse(robot.torches().isOn(), WRONG_SWITCH);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void baseRefusesTheTorchButton(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item(RobotTier.BASE), FLOOR);
    RobotMenu menu = RobotScreenReadout.serverMenu(0, owner.getInventory(), robot);

    helper.assertFalse(menu.clickMenuButton(owner, RobotMenu.TORCHES_BUTTON), WRONG_SWITCH);
    helper.assertTrue(robot.torches().isOn(), WRONG_SWITCH);
    helper.succeed();
  }

  private static RobotEntity place(GameTestHelper helper, RobotTier tier) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    return GameTestRobots.placeRobot(helper, owner, item(tier), FLOOR);
  }

  /** Lamp, scanners and chatter off, so nothing but the torches spends. */
  private static ItemStack item(RobotTier tier) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(
        CubitDataComponents.ROBOT_STATE.get(),
        new RobotState(
            tier, CHARGE, HEALTH, false, RobotColor.WHITE, false, OreFilter.EMPTY, false));
    return item;
  }

  private static RobotEntity withTorches(GameTestHelper helper, RobotEntity robot) {
    robot.torches().slot().setItem(SLOT, new ItemStack(Items.TORCH, TORCHES));
    return robot;
  }

  private static void buildRoof(GameTestHelper helper) {
    buildRoof(helper, Blocks.STONE);
  }

  private static void buildRoof(GameTestHelper helper, Block block) {
    for (int x = 0; x < LENGTH; x++) {
      for (int z = 0; z < WIDTH; z++) {
        helper.setBlock(new BlockPos(x, ROOF_Y, z), block);
      }
    }
  }

  private static int countTorches(GameTestHelper helper) {
    int count = 0;
    for (int x = -SEARCH_MARGIN; x <= LENGTH + SEARCH_MARGIN; x++) {
      for (int y = -SEARCH_MARGIN; y <= ROOF_Y + SEARCH_MARGIN; y++) {
        for (int z = -SEARCH_MARGIN; z <= WIDTH + SEARCH_MARGIN; z++) {
          BlockPos pos = new BlockPos(x, y, z);
          if (helper.getBlockState(pos).is(Blocks.TORCH)
              || helper.getBlockState(pos).is(Blocks.WALL_TORCH)) {
            count++;
          }
        }
      }
    }
    return count;
  }

  private static void assertNoTorch(GameTestHelper helper) {
    helper.assertValueEqual(countTorches(helper), 0, PLACED);
    helper.succeed();
  }
}
