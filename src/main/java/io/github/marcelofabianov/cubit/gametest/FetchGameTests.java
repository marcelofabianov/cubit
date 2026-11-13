package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.robot.PickupSettings;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The following robot fetches the items on the ground around its owner (answers 927 and 936 to
 * 943). The arena is 24 blocks long, with its stone floor at y=1. The owner stands close enough
 * that the robot does not walk to follow, and the Base has no magnet, so only fetching moves it.
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class FetchGameTests {
  private static final String ARENA = "arena";
  private static final int FETCH_TIMEOUT = 200;
  private static final int WAIT_TICKS = 120;
  private static final int GIVE_UP_WAIT_TICKS = 160;
  private static final int FLOOR_Y = 1;
  private static final int START_X = 1;
  private static final int MIDDLE_Z = 2;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final double OWNER_X = 3.5;
  private static final double OWNER_Y = 2;
  private static final float LANE_Z = 2.5f;
  private static final float ITEM_Y = 2.1f;
  private static final float IN_REACH_X = 7.5f;
  private static final float OUT_OF_REACH_X = 9.5f;
  private static final int CAGE_X = 7;
  private static final int CAGE_HEIGHT = 3;
  private static final double NEAR_OWNER = 4;
  private static final String FETCH_KEY = "FetchEnabled";
  private static final String NOT_FETCHED = "The robot should have fetched the item";
  private static final String FETCHED = "The robot should have left the item";
  private static final String NOT_BACK = "The robot should be back by its owner";
  private static final String WRONG_SETTINGS = "An old save should have fetch on";

  private FetchGameTests() {}

  @GameTest(template = ARENA, timeoutTicks = FETCH_TIMEOUT)
  public static void theFollowerFetchesAnItemNearItsOwner(GameTestHelper helper) {
    RobotEntity robot = followingRobot(helper);
    drop(helper, IN_REACH_X);

    helper.succeedWhen(() -> helper.assertTrue(holdsDirt(robot), NOT_FETCHED));
  }

  @GameTest(template = ARENA, timeoutTicks = FETCH_TIMEOUT)
  public static void anItemBeyondTheRadiusStays(GameTestHelper helper) {
    RobotEntity robot = followingRobot(helper);
    drop(helper, OUT_OF_REACH_X);

    helper.runAfterDelay(WAIT_TICKS, () -> assertLeft(helper, robot));
  }

  @GameTest(template = ARENA, timeoutTicks = FETCH_TIMEOUT)
  public static void withFetchOffTheItemStays(GameTestHelper helper) {
    RobotEntity robot = followingRobot(helper);
    robot.pickup().setSettings(PickupSettings.INITIAL.toggledFetch());
    drop(helper, IN_REACH_X);

    helper.runAfterDelay(WAIT_TICKS, () -> assertLeft(helper, robot));
  }

  @GameTest(template = ARENA, timeoutTicks = FETCH_TIMEOUT)
  public static void withPickupOffTheItemStays(GameTestHelper helper) {
    RobotEntity robot = followingRobot(helper);
    robot.pickup().setSettings(PickupSettings.INITIAL.toggledPickup());
    drop(helper, IN_REACH_X);

    helper.runAfterDelay(WAIT_TICKS, () -> assertLeft(helper, robot));
  }

  /** Without a route, the robot gives up and stays by its owner (answer 942). */
  @GameTest(template = ARENA, timeoutTicks = FETCH_TIMEOUT)
  public static void anItemWithoutARouteIsGivenUp(GameTestHelper helper) {
    ServerPlayer owner = ownerInPlace(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    buildCage(helper);
    drop(helper, IN_REACH_X);

    helper.runAfterDelay(
        GIVE_UP_WAIT_TICKS,
        () -> {
          helper.assertFalse(holdsDirt(robot), FETCHED);
          helper.assertTrue(robot.distanceTo(owner) < NEAR_OWNER, NOT_BACK);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void aSaveWithoutTheKeyHasFetchOn(GameTestHelper helper) {
    RobotEntity robot = followingRobot(helper);
    robot.pickup().setSettings(PickupSettings.INITIAL.toggledFetch());
    CompoundTag saved = robot.saveWithoutId(new CompoundTag());
    saved.remove(FETCH_KEY);

    RobotEntity loaded = GameTestRobots.loadRobot(helper, saved);

    helper.assertTrue(loaded.pickup().settings().fetchOn(), WRONG_SETTINGS);
    helper.succeed();
  }

  private static RobotEntity followingRobot(GameTestHelper helper) {
    return GameTestRobots.placeNewRobot(helper, ownerInPlace(helper), FLOOR);
  }

  private static ServerPlayer ownerInPlace(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    Vec3 position = helper.absoluteVec(new Vec3(OWNER_X, OWNER_Y, LANE_Z));
    owner.moveTo(position.x, position.y, position.z);
    return owner;
  }

  private static void drop(GameTestHelper helper, float x) {
    ItemEntity item = helper.spawnItem(Items.DIRT, x, ITEM_Y, LANE_Z);
    item.setNoPickUpDelay();
  }

  /**
   * Walls all around the item, corners included, too high to climb, so there is no route and the
   * touch pickup cannot reach it from a corner.
   */
  private static void buildCage(GameTestHelper helper) {
    for (int y = FLOOR_Y + 1; y <= FLOOR_Y + CAGE_HEIGHT; y++) {
      for (int x = CAGE_X - 1; x <= CAGE_X + 1; x++) {
        for (int z = MIDDLE_Z - 1; z <= MIDDLE_Z + 1; z++) {
          if (x != CAGE_X || z != MIDDLE_Z) {
            helper.setBlock(new BlockPos(x, y, z), Blocks.STONE);
          }
        }
      }
    }
  }

  private static boolean holdsDirt(RobotEntity robot) {
    return robot.inventory().countItem(Items.DIRT) > 0;
  }

  private static void assertLeft(GameTestHelper helper, RobotEntity robot) {
    helper.assertFalse(holdsDirt(robot), FETCHED);
    helper.succeed();
  }
}
