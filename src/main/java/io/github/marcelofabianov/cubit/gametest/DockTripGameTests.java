package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
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

/** The robot on its way to the base: called, stuck, without a dock, or waiting its turn. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DockTripGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 400;
  private static final int LONG_TIMEOUT = 600;
  private static final int WALK_TICKS = 120;
  private static final int STUCK_TICKS = 180;
  private static final int CHARGE_TICKS = 20;
  private static final int ROBOT_X = 5;
  private static final int OTHER_DOCK_X = 18;
  private static final BlockPos FLOOR = DockScene.FLOOR;
  private static final BlockPos DOCK = DockScene.DOCK;
  private static final BlockPos ROBOT_FLOOR =
      new BlockPos(ROBOT_X, DockScene.FLOOR_Y, DockScene.MIDDLE_Z);
  private static final BlockPos OTHER_DOCK_FLOOR =
      new BlockPos(OTHER_DOCK_X, DockScene.FLOOR_Y, DockScene.MIDDLE_Z);
  private static final Energy PLENTY = new Energy(100_000);
  private static final Energy STOCK = new Energy(50_000);
  private static final Energy LOW = new Energy(1_000);
  private static final String WRONG_POWER = "Wrong power state";
  private static final String TELEPORTED = "The robot should be walking to the dock";
  private static final int STILL_WALKING_TICKS = 25;
  private static final double SOME_BLOCKS = 0.5;
  private static final double TOO_FAR = 6;

  private DockTripGameTests() {}

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void clickingCallsTheNearestActiveRobot(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, FLOOR, STOCK);
    RobotEntity robot = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, PLENTY);

    DockScene.click(helper, owner, DOCK);

    Vec3 start = robot.position();
    helper.runAfterDelay(
        STILL_WALKING_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, TELEPORTED);
          double walked = robot.position().distanceTo(start);
          helper.assertTrue(walked > SOME_BLOCKS && walked < TOO_FAR, TELEPORTED);
        });

    helper.runAfterDelay(
        WALK_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.HIBERNATING, WRONG_POWER);
          helper.assertTrue(robot.docking().isDockedAt(helper.absolutePos(DOCK)), WRONG_POWER);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void clickingTheUpperPartAlsoCalls(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, FLOOR, STOCK);
    RobotEntity robot = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, PLENTY);

    DockScene.click(helper, owner, DOCK.above());

    helper.runAfterDelay(
        WALK_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.HIBERNATING, WRONG_POWER);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aStrangerCallsNobody(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, FLOOR, STOCK);
    RobotEntity robot = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, PLENTY);

    DockScene.click(helper, GameTestRobots.onlinePlayer(helper), DOCK);

    helper.runAfterDelay(
        WALK_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_POWER);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = LONG_TIMEOUT)
  public static void aRobotWithoutARouteTeleportsToTheDock(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, FLOOR, STOCK);
    RobotEntity robot = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, PLENTY);
    DockScene.buildWallAt(helper, ROBOT_X + 2);

    DockScene.click(helper, owner, DOCK);

    helper.runAfterDelay(
        STUCK_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.HIBERNATING, WRONG_POWER);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotGivesUpWhenTheDockIsGone(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, FLOOR, STOCK);
    RobotEntity robot = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, PLENTY);
    DockScene.click(helper, owner, DOCK);
    helper.setBlock(DOCK, Blocks.AIR);

    helper.runAfterDelay(
        WALK_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_POWER);
          helper.assertTrue(robot.answersDockCall(), WRONG_POWER);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = LONG_TIMEOUT)
  public static void aRobotGoesToAnotherFreeDock(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, FLOOR, STOCK);
    DockScene.placeDock(helper, owner, OTHER_DOCK_FLOOR, STOCK);
    DockScene.placeOnDock(helper, owner, LOW);
    RobotEntity robot = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, PLENTY);

    robot.callToDock(helper.absolutePos(DOCK));

    helper.runAfterDelay(
        STUCK_TICKS,
        () -> {
          helper.assertTrue(
              robot.docking().isDockedAt(helper.absolutePos(OTHER_DOCK_FLOOR.above())),
              WRONG_POWER);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = LONG_TIMEOUT)
  public static void aRobotWaitsForABusyDockAndEntersWhenItIsFree(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, FLOOR, STOCK);
    RobotEntity first = DockScene.placeOnDock(helper, owner, LOW);
    RobotEntity waiting = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, PLENTY);
    waiting.callToDock(helper.absolutePos(DOCK));

    helper.runAfterDelay(
        WALK_TICKS,
        () -> {
          helper.assertValueEqual(waiting.powerState(), PowerState.ACTIVE, WRONG_POWER);
          first.releaseFromDock();
          helper.runAfterDelay(
              CHARGE_TICKS,
              () -> {
                helper.assertValueEqual(waiting.powerState(), PowerState.HIBERNATING, WRONG_POWER);
                helper.succeed();
              });
        });
  }
}
