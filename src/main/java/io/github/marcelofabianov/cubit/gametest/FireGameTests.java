package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** A burning robot reacts in the world. The fire lasts longer than each test, unless put out. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class FireGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 300;
  private static final int STRAIGHT_TO_WATER_TIMEOUT = 100;
  private static final int CLIMB_TO_WATER_TIMEOUT = 150;
  private static final int SETTLE_TICKS = 60;
  private static final int WANDER_TICKS = 120;
  private static final float LONG_FIRE_SECONDS = 60f;
  private static final float SHORT_FIRE_SECONDS = 1f;
  private static final int FLOOR_Y = 1;
  private static final int START_X = 1;
  private static final int MIDDLE_Z = 2;
  private static final int LAST_Z = 4;
  private static final int NEAR_WATER_X = 7;
  private static final int FAR_WATER_X = 22;
  private static final int LAVA_X = 4;
  private static final int HOLE_X = 5;
  private static final int COVERED_WATER_X = 2;
  private static final double AWAY_FROM_COVERED_WATER = 3;
  private static final int MAX_STILL_TICKS = 30;
  private static final int NEVER_STOPS_TICKS = 200;
  private static final double TINY_MOVE = 0.05;
  private static final int WALL_X = 5;
  private static final int WALL_HEIGHT = 3;
  private static final int STAIRS_X = 3;
  private static final int STAIRS_HEIGHT = 3;
  private static final int PLATFORM_END_X = 9;
  private static final int POOL_X = 7;
  private static final double WALK_X = 13.5;
  private static final double FAR_X = 22.5;
  private static final double NEAR_DISTANCE = 3.6;
  private static final double SMALL_MOVE = 0.1;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos SECOND_FLOOR = new BlockPos(START_X, FLOOR_Y, LAST_Z);
  private static final String TELEPORTED = "Robot teleported: fire=%s, wet=%s, tick=%d";
  private static final String DID_NOT_WANDER = "The burning robot did not wander";
  private static final String STILL_BURNING = "The robot did not reach the water";
  private static final String STOPPED = "The burning robot stood still";
  private static final String STUCK = "The robot stayed stuck on water under the floor";
  private static final String PUT_OUT = "The robot found water beyond the search radius";
  private static final String ENTERED_LAVA = "The burning robot walked into lava";
  private static final String MOVED = "The switched off robot moved";
  private static final String WRONG_POWER = "Wrong power state";
  private static final String DID_NOT_RETURN = "The robot did not follow again";

  private FireGameTests() {}

  private static void buildWallBeforeWater(GameTestHelper helper) {
    for (int z = 0; z <= LAST_Z; z++) {
      for (int y = FLOOR_Y + 1; y <= FLOOR_Y + WALL_HEIGHT; y++) {
        helper.setBlock(new BlockPos(WALL_X, y, z), Blocks.STONE);
      }
    }
    helper.setBlock(new BlockPos(WALL_X + 1, FLOOR_Y, MIDDLE_Z), Blocks.WATER);
  }

  /** Each column rises one more block, ending on a platform with a pool sunk into it. */
  private static void buildStairsToPool(GameTestHelper helper) {
    for (int z = 0; z <= LAST_Z; z++) {
      for (int x = STAIRS_X; x <= PLATFORM_END_X; x++) {
        int top = Math.min(x - STAIRS_X + 1, STAIRS_HEIGHT);
        for (int y = FLOOR_Y + 1; y <= FLOOR_Y + top; y++) {
          helper.setBlock(new BlockPos(x, y, z), Blocks.STONE);
        }
      }
    }
    helper.setBlock(new BlockPos(POOL_X, FLOOR_Y + STAIRS_HEIGHT, MIDDLE_Z), Blocks.WATER);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void burningRobotWandersInsteadOfFollowing(GameTestHelper helper) {
    ServerPlayer owner = MovementGameTests.ownerAt(helper, FAR_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    Vec3 start = robot.position();
    robot.igniteForSeconds(LONG_FIRE_SECONDS);

    helper.onEachTick(() ->
        helper.assertTrue(
            robot.distanceTo(owner) > NEAR_DISTANCE,
            TELEPORTED.formatted(
                robot.isOnFire(), robot.isInWaterRainOrBubble(), robot.tickCount)));
    helper.runAfterDelay(
        WANDER_TICKS,
        () -> {
          helper.assertTrue(robot.position().distanceTo(start) > SMALL_MOVE, DID_NOT_WANDER);
          helper.succeed();
        });
  }

  /** The short timeout separates going straight to the water from reaching it by chance. */
  @GameTest(template = ARENA, timeoutTicks = STRAIGHT_TO_WATER_TIMEOUT)
  public static void burningRobotGoesToNearbyWaterAndPutsItselfOut(GameTestHelper helper) {
    ServerPlayer owner = MovementGameTests.ownerAt(helper, FAR_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    helper.setBlock(new BlockPos(NEAR_WATER_X, FLOOR_Y, MIDDLE_Z), Blocks.WATER);
    robot.igniteForSeconds(LONG_FIRE_SECONDS);

    helper.succeedWhen(() -> helper.assertFalse(robot.isOnFire(), STILL_BURNING));
  }

  /** The stairs climb to a pool two blocks above the feet of the robot. */
  @GameTest(template = ARENA, timeoutTicks = CLIMB_TO_WATER_TIMEOUT)
  public static void burningRobotClimbsStairsToWaterAbove(GameTestHelper helper) {
    ServerPlayer owner = MovementGameTests.ownerAt(helper, FAR_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    buildStairsToPool(helper);
    robot.igniteForSeconds(LONG_FIRE_SECONDS);

    helper.succeedWhen(() -> helper.assertFalse(robot.isOnFire(), STILL_BURNING));
  }

  @GameTest(template = ARENA, timeoutTicks = STRAIGHT_TO_WATER_TIMEOUT)
  public static void burningRobotGoesDownToWaterBelow(GameTestHelper helper) {
    ServerPlayer owner = MovementGameTests.ownerAt(helper, FAR_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    helper.setBlock(new BlockPos(HOLE_X, FLOOR_Y, MIDDLE_Z), Blocks.AIR);
    helper.setBlock(new BlockPos(HOLE_X, 0, MIDDLE_Z), Blocks.WATER);
    robot.igniteForSeconds(LONG_FIRE_SECONDS);

    helper.succeedWhen(() -> helper.assertFalse(robot.isOnFire(), STILL_BURNING));
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void burningRobotKeepsWanderingWhenTheWaterCannotBeReached(GameTestHelper helper) {
    ServerPlayer owner = MovementGameTests.ownerAt(helper, FAR_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    buildWallBeforeWater(helper);
    robot.igniteForSeconds(LONG_FIRE_SECONDS);

    helper.runAfterDelay(
        WANDER_TICKS,
        () -> {
          Vec3 later = robot.position();
          helper.runAfterDelay(
              WANDER_TICKS,
              () -> {
                helper.assertTrue(robot.position().distanceTo(later) > SMALL_MOVE, DID_NOT_WANDER);
                helper.succeed();
              });
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void burningRobotNeverStopsOnOpenGround(GameTestHelper helper) {
    ServerPlayer owner = MovementGameTests.ownerAt(helper, FAR_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    assertNeverStops(helper, robot);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void burningRobotNeverStopsNextToLava(GameTestHelper helper) {
    ServerPlayer owner = MovementGameTests.ownerAt(helper, FAR_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    for (int z = 0; z <= LAST_Z; z++) {
      helper.setBlock(new BlockPos(START_X + 2, FLOOR_Y, z), Blocks.LAVA);
    }
    assertNeverStops(helper, robot);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void burningRobotNeverStopsInATightSpace(GameTestHelper helper) {
    ServerPlayer owner = MovementGameTests.ownerAt(helper, FAR_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    for (int y = FLOOR_Y + 1; y <= FLOOR_Y + WALL_HEIGHT; y++) {
      for (int z = 0; z <= LAST_Z; z++) {
        helper.setBlock(new BlockPos(START_X + 2, y, z), Blocks.STONE);
      }
    }
    assertNeverStops(helper, robot);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void burningRobotNeverStopsBehindAWallWithWater(GameTestHelper helper) {
    ServerPlayer owner = MovementGameTests.ownerAt(helper, FAR_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    buildWallBeforeWater(helper);
    assertNeverStops(helper, robot);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void burningRobotNeverStopsOverWaterUnderTheFloor(GameTestHelper helper) {
    ServerPlayer owner = MovementGameTests.ownerAt(helper, FAR_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    helper.setBlock(new BlockPos(COVERED_WATER_X, 0, MIDDLE_Z), Blocks.WATER);
    assertNeverStops(helper, robot);
  }

  private static void assertNeverStops(GameTestHelper helper, RobotEntity robot) {
    robot.igniteForSeconds(LONG_FIRE_SECONDS);
    Vec3[] lastPosition = {robot.position()};
    int[] stillTicks = {0};
    helper.onEachTick(() -> {
      if (robot.position().distanceTo(lastPosition[0]) > TINY_MOVE) {
        lastPosition[0] = robot.position();
        stillTicks[0] = 0;
      } else {
        stillTicks[0]++;
      }
      helper.assertTrue(stillTicks[0] < MAX_STILL_TICKS, STOPPED);
    });
    helper.runAfterDelay(NEVER_STOPS_TICKS, helper::succeed);
  }

  /** Stuck trying to reach the water, the robot would never get this far from it. */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void burningRobotIgnoresWaterUnderTheFloor(GameTestHelper helper) {
    ServerPlayer owner = MovementGameTests.ownerAt(helper, FAR_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    BlockPos water = new BlockPos(COVERED_WATER_X, 0, MIDDLE_Z);
    helper.setBlock(water, Blocks.WATER);
    robot.igniteForSeconds(LONG_FIRE_SECONDS);
    Vec3 waterCenter = Vec3.atCenterOf(helper.absolutePos(water));

    helper.succeedWhen(() ->
        helper.assertTrue(
            robot.position().distanceTo(waterCenter) > AWAY_FROM_COVERED_WATER, STUCK));
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void waterBeyondTheRadiusIsIgnored(GameTestHelper helper) {
    ServerPlayer owner = MovementGameTests.ownerAt(helper, WALK_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    helper.setBlock(new BlockPos(FAR_WATER_X, FLOOR_Y, MIDDLE_Z), Blocks.WATER);
    robot.igniteForSeconds(LONG_FIRE_SECONDS);

    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertTrue(robot.isOnFire(), PUT_OUT);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void burningRobotDoesNotWalkIntoLava(GameTestHelper helper) {
    ServerPlayer owner = MovementGameTests.ownerAt(helper, WALK_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    for (int z = 0; z <= LAST_Z; z++) {
      helper.setBlock(new BlockPos(LAVA_X, FLOOR_Y, z), Blocks.LAVA);
    }
    robot.igniteForSeconds(LONG_FIRE_SECONDS);

    helper.onEachTick(() -> helper.assertFalse(robot.isInLava(), ENTERED_LAVA));
    helper.runAfterDelay(WANDER_TICKS, helper::succeed);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void switchedOffRobotBurnsStandingStill(GameTestHelper helper) {
    ServerPlayer owner = MovementGameTests.ownerAt(helper, WALK_X);
    GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    RobotEntity second = GameTestRobots.placeNewRobot(helper, owner, SECOND_FLOOR);
    Vec3 start = second.position();
    second.igniteForSeconds(LONG_FIRE_SECONDS);

    helper.assertValueEqual(second.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_POWER);
    helper.runAfterDelay(
        WANDER_TICKS,
        () -> {
          helper.assertTrue(second.position().distanceTo(start) < SMALL_MOVE, MOVED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void robotFollowsAgainWhenTheFireGoesOut(GameTestHelper helper) {
    ServerPlayer owner = MovementGameTests.ownerAt(helper, WALK_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    robot.igniteForSeconds(SHORT_FIRE_SECONDS);

    helper.succeedWhen(() -> {
      helper.assertFalse(robot.isOnFire(), DID_NOT_RETURN);
      helper.assertTrue(robot.distanceTo(owner) < NEAR_DISTANCE, DID_NOT_RETURN);
    });
  }
}
