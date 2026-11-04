package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.player.FollowingRobotLink;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Moving only exists in the world. The arena is 24 blocks long. Relative y=0 is one layer below a
 * template, so its stone floor is at y=1.
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class MovementGameTests {
  private static final String ARENA = "arena";
  private static final int WALK_TIMEOUT = 300;
  private static final int TELEPORT_TIMEOUT = 40;
  private static final int SETTLE_TICKS = 60;
  private static final int LONG_SETTLE_TICKS = 120;
  private static final int FLOOR_Y = 1;
  private static final int START_X = 1;
  private static final int MIDDLE_Z = 2;
  private static final int LAST_Z = 4;
  private static final int WALL_X = 6;
  private static final int WATER_END_X = 9;
  private static final int POOL_TARGET_X = 5;
  private static final int POOL_DEPTH = 2;
  private static final double NEAR_X = 3.5;
  private static final double WALK_X = 13.5;
  private static final double FAR_X = 22.5;
  private static final double OWNER_Y = 2;
  private static final double OWNER_Z = 2.5;
  private static final double NEAR_DISTANCE = 3.6;
  private static final double SMALL_MOVE = 0.1;
  private static final float BIG_FALL = 100f;
  private static final float DAMAGE_MULTIPLIER = 1f;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos SECOND_FLOOR = new BlockPos(START_X, FLOOR_Y, LAST_Z);
  private static final UUID OTHER_ROBOT = new UUID(0, 1);
  private static final String DID_NOT_REACH = "The robot did not reach its owner";
  private static final String MOVED = "The robot moved when it should stay";
  private static final String ENTERED_WATER = "The route went through water";
  private static final String ENTERED_LAVA = "The route went through lava";
  private static final String LAVA_DID_NOT_HURT = "Lava did not hurt the robot";
  private static final String CROSSED_WALL = "The robot crossed a wall two blocks high";
  private static final String NOT_BELOW_WATER = "The robot did not walk under the water";
  private static final String WRONG_POWER = "Wrong power state";
  private static final String WRONG_LINK = "Wrong following robot";
  private static final String FALL_HURT = "The fall hurt the robot";
  private static final String DROWNS = "The robot cannot stay underwater";

  private MovementGameTests() {}

  @GameTest(template = ARENA, timeoutTicks = WALK_TIMEOUT)
  public static void followerWalksToItsOwner(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper, WALK_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    helper.succeedWhen(
        () -> helper.assertTrue(robot.distanceTo(owner) < NEAR_DISTANCE, DID_NOT_REACH));
  }

  @GameTest(template = ARENA, timeoutTicks = WALK_TIMEOUT)
  public static void followerStaysStillWhenAlreadyClose(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper, NEAR_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    Vec3 start = robot.position();

    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertTrue(robot.position().distanceTo(start) < SMALL_MOVE, MOVED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TELEPORT_TIMEOUT)
  public static void followerFarAwayTeleportsNearItsOwner(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper, FAR_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    helper.succeedWhen(
        () -> helper.assertTrue(robot.distanceTo(owner) < NEAR_DISTANCE, DID_NOT_REACH));
  }

  @GameTest(template = ARENA, timeoutTicks = WALK_TIMEOUT)
  public static void switchedOffRobotNeitherWalksNorTeleports(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper, FAR_X);
    GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    RobotEntity second = GameTestRobots.placeNewRobot(helper, owner, SECOND_FLOOR);
    Vec3 start = second.position();

    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertTrue(second.position().distanceTo(start) < SMALL_MOVE, MOVED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = WALK_TIMEOUT)
  public static void robotSwitchesOffWhenItFindsAnotherFollowing(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper, NEAR_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    owner.setData(
        CubitAttachments.FOLLOWING_ROBOT, new FollowingRobotLink(Optional.of(OTHER_ROBOT)));

    helper.succeedWhen(() ->
        helper.assertValueEqual(robot.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_POWER));
  }

  @GameTest(template = ARENA, timeoutTicks = WALK_TIMEOUT)
  public static void climbsOneBlockWithoutJumping(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper, WALK_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    buildWall(helper, 1);

    helper.succeedWhen(
        () -> helper.assertTrue(robot.distanceTo(owner) < NEAR_DISTANCE, DID_NOT_REACH));
  }

  @GameTest(template = ARENA, timeoutTicks = WALK_TIMEOUT)
  public static void doesNotCrossAWallTwoBlocksHigh(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper, WALK_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    buildWall(helper, 2);

    helper.runAfterDelay(
        LONG_SETTLE_TICKS,
        () -> {
          int wallX = helper.absolutePos(new BlockPos(WALL_X, FLOOR_Y, MIDDLE_Z)).getX();
          helper.assertTrue(robot.blockPosition().getX() < wallX, CROSSED_WALL);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = WALK_TIMEOUT)
  public static void routeGoesAroundWater(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper, WALK_X);
    for (int z = 0; z < LAST_Z; z++) {
      helper.setBlock(new BlockPos(WALL_X, FLOOR_Y, z), Blocks.WATER);
    }
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    helper.onEachTick(() -> helper.assertFalse(robot.isInWater(), ENTERED_WATER));
    helper.succeedWhen(
        () -> helper.assertTrue(robot.distanceTo(owner) < NEAR_DISTANCE, DID_NOT_REACH));
  }

  @GameTest(template = ARENA, timeoutTicks = WALK_TIMEOUT)
  public static void robotInWaterWalksOnTheBottomWithoutDrowning(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper, WALK_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    buildPool(helper);
    float health = robot.getHealth();
    int targetX = helper.absolutePos(new BlockPos(POOL_TARGET_X, FLOOR_Y, MIDDLE_Z)).getX();
    int bottomY = robot.blockPosition().getY();

    helper.assertTrue(robot.canBreatheUnderwater(), DROWNS);
    helper.succeedWhen(() -> {
      helper.assertTrue(robot.isInWater(), NOT_BELOW_WATER);
      helper.assertTrue(robot.blockPosition().getX() >= targetX, NOT_BELOW_WATER);
      helper.assertValueEqual(robot.blockPosition().getY(), bottomY, NOT_BELOW_WATER);
      helper.assertValueEqual(robot.getHealth(), health, DROWNS);
    });
  }

  @GameTest(template = ARENA, timeoutTicks = WALK_TIMEOUT)
  public static void doesNotEnterWaterEvenWithoutAnotherRoute(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper, WALK_X);
    for (int z = 0; z <= LAST_Z; z++) {
      helper.setBlock(new BlockPos(WALL_X, FLOOR_Y, z), Blocks.WATER);
    }
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    helper.onEachTick(() -> helper.assertFalse(robot.isInWater(), ENTERED_WATER));
    helper.runAfterDelay(LONG_SETTLE_TICKS, helper::succeed);
  }

  @GameTest(template = ARENA, timeoutTicks = WALK_TIMEOUT)
  public static void followerWithoutBatteryKeepsThePlaceAndStaysStill(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper, FAR_X);
    ItemStack stack = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    stack.set(
        CubitDataComponents.ROBOT_STATE.get(), new RobotState(RobotTier.BASE, Energy.ZERO, 1f));
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, stack, FLOOR);
    Vec3 start = robot.position();

    helper.assertValueEqual(robot.powerState(), PowerState.INACTIVE_EXHAUSTED, WRONG_POWER);
    helper.assertValueEqual(
        owner.getData(CubitAttachments.FOLLOWING_ROBOT).robot(),
        Optional.of(robot.getUUID()),
        WRONG_LINK);
    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertTrue(robot.position().distanceTo(start) < SMALL_MOVE, MOVED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = WALK_TIMEOUT)
  public static void doesNotEnterLavaEvenWithoutAnotherRoute(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper, WALK_X);
    for (int z = 0; z <= LAST_Z; z++) {
      helper.setBlock(new BlockPos(WALL_X, FLOOR_Y, z), Blocks.LAVA);
    }
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    helper.onEachTick(() -> helper.assertFalse(robot.isInLava(), ENTERED_LAVA));
    helper.runAfterDelay(LONG_SETTLE_TICKS, helper::succeed);
  }

  @GameTest(template = ARENA, timeoutTicks = WALK_TIMEOUT)
  public static void lavaHurtsAndSetsTheRobotOnFire(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper, NEAR_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    float health = robot.getHealth();

    helper.getLevel().setBlockAndUpdate(robot.blockPosition(), Blocks.LAVA.defaultBlockState());

    helper.succeedWhen(() -> {
      helper.assertTrue(robot.getHealth() < health, LAVA_DID_NOT_HURT);
      helper.assertTrue(robot.isOnFire(), LAVA_DID_NOT_HURT);
    });
  }

  @GameTest(template = ARENA)
  public static void fallingDoesNotHurt(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper, NEAR_X);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    float health = robot.getHealth();

    robot.causeFallDamage(BIG_FALL, DAMAGE_MULTIPLIER, helper.getLevel().damageSources().fall());

    helper.assertValueEqual(robot.getHealth(), health, FALL_HURT);
    helper.succeed();
  }

  private static ServerPlayer ownerAt(GameTestHelper helper, double x) {
    ServerPlayer owner = helper.makeMockServerPlayerInLevel();
    Vec3 position = helper.absoluteVec(new Vec3(x, OWNER_Y, OWNER_Z));
    owner.moveTo(position.x, position.y, position.z);
    return owner;
  }

  /** Two blocks deep, with walls the robot cannot climb. */
  private static void buildPool(GameTestHelper helper) {
    for (int y = FLOOR_Y + 1; y <= FLOOR_Y + POOL_DEPTH; y++) {
      for (int x = START_X - 1; x <= WATER_END_X; x++) {
        helper.setBlock(new BlockPos(x, y, MIDDLE_Z - 1), Blocks.STONE);
        helper.setBlock(new BlockPos(x, y, MIDDLE_Z + 1), Blocks.STONE);
        boolean endWall = x < START_X || x == WATER_END_X;
        helper.setBlock(new BlockPos(x, y, MIDDLE_Z), endWall ? Blocks.STONE : Blocks.WATER);
      }
    }
  }

  private static void buildWall(GameTestHelper helper, int height) {
    for (int z = 0; z <= LAST_Z; z++) {
      for (int y = FLOOR_Y + 1; y <= FLOOR_Y + height; y++) {
        helper.setBlock(new BlockPos(WALL_X, y, z), Blocks.STONE);
      }
    }
  }
}
