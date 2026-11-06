package io.github.marcelofabianov.cubit.gametest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.GazeDirection;
import io.github.marcelofabianov.cubit.domain.behavior.RobotPose;
import io.github.marcelofabianov.cubit.domain.behavior.TrackFrames;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotFace;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** A dedicated server draws nothing, so these check that the files the client needs exist. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class RobotAnimationGameTests {
  private static final String ARENA = "arena";
  private static final String ANIMATION_FOLDER = "/assets/cubit/animations/entity/";
  private static final String ANIMATION_NAME = "general_robot.animation.json";
  private static final String ANIMATIONS_KEY = "animations";
  private static final String FRAME_FOLDER = "/assets/cubit/textures/entity/general_robot_tracks/";
  private static final String FRAME_NAME = "frame_%d.png";
  private static final String NO_ANIMATION_FILE = "The animation file is missing";
  private static final String BAD_ANIMATION_FILE = "The animation file could not be read";
  private static final String NO_ANIMATION = "No animation for the pose: ";
  private static final String NO_FRAME = "Missing track frame: ";
  private static final String BAD_FRAME = "A track frame could not be read: ";

  private static final String ARMS_RAISED = "arms_raised";
  private static final String WRONG_GAZE = "Wrong direction for the eyes";
  private static final String WRONG_LEVEL = "Wrong level for the owner";
  private static final double JUMP_BLOCKS = 0.42;
  private static final int MARGIN_TICKS = 3;
  private static final int TIMEOUT = 100;
  private static final int SETTLE_TICKS = 20;
  private static final int AFTER_MOVE_TICKS = 40;
  private static final int FLOOR_Y = 1;
  private static final int START_X = 1;
  private static final int MIDDLE_Z = 2;
  private static final double OTHER_LEVEL_BLOCKS = 2;
  private static final double BACK_THROUGH_THE_FLOOR_BLOCKS = -4;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos SECOND_FLOOR = FLOOR.offset(MIDDLE_Z, 0, 0);

  private RobotAnimationGameTests() {}

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theEyesFollowAnOwnerAtAnotherLevel(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    helper.runAfterDelay(SETTLE_TICKS, () -> moveOwnerUp(owner, OTHER_LEVEL_BLOCKS));
    helper.runAfterDelay(AFTER_MOVE_TICKS, () -> assertGaze(helper, robot, GazeDirection.UP));
    helper.runAfterDelay(
        AFTER_MOVE_TICKS + 1, () -> moveOwnerUp(owner, BACK_THROUGH_THE_FLOOR_BLOCKS));
    helper.runAfterDelay(
        2 * AFTER_MOVE_TICKS,
        () -> {
          assertGaze(helper, robot, GazeDirection.DOWN);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theBodyLeansToAnOwnerAtAnotherLevel(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    helper.runAfterDelay(SETTLE_TICKS, () -> moveOwnerUp(owner, OTHER_LEVEL_BLOCKS));
    helper.runAfterDelay(AFTER_MOVE_TICKS, () -> assertLevel(helper, robot, GazeDirection.UP));
    helper.runAfterDelay(
        AFTER_MOVE_TICKS + 1, () -> moveOwnerUp(owner, BACK_THROUGH_THE_FLOOR_BLOCKS));
    helper.runAfterDelay(
        2 * AFTER_MOVE_TICKS,
        () -> {
          assertLevel(helper, robot, GazeDirection.DOWN);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aJumpMovesTheEyesButDoesNotLeanTheBody(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    helper.runAfterDelay(SETTLE_TICKS, () -> moveOwnerUp(owner, JUMP_BLOCKS));
    helper.runAfterDelay(
        SETTLE_TICKS + MARGIN_TICKS,
        () -> {
          assertGaze(helper, robot, GazeDirection.UP);
          assertLevel(helper, robot, GazeDirection.LEVEL);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotSwitchedOffDoesNotLean(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    RobotEntity switchedOff = GameTestRobots.placeNewRobot(helper, owner, SECOND_FLOOR);

    helper.runAfterDelay(SETTLE_TICKS, () -> moveOwnerUp(owner, OTHER_LEVEL_BLOCKS));
    helper.runAfterDelay(
        AFTER_MOVE_TICKS,
        () -> {
          assertLevel(helper, switchedOff, GazeDirection.LEVEL);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theEyesOfARobotSwitchedOffIgnoreTheOwner(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    RobotEntity switchedOff = GameTestRobots.placeNewRobot(helper, owner, SECOND_FLOOR);

    helper.runAfterDelay(SETTLE_TICKS, () -> moveOwnerUp(owner, OTHER_LEVEL_BLOCKS));
    helper.runAfterDelay(
        AFTER_MOVE_TICKS,
        () -> {
          assertGaze(helper, switchedOff, GazeDirection.LEVEL);
          helper.succeed();
        });
  }

  private static ServerPlayer ownerBeside(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    Vec3 beside = helper.absoluteVec(Vec3.atBottomCenterOf(FLOOR.above().south()));
    owner.moveTo(beside.x, beside.y, beside.z);
    return owner;
  }

  private static void assertLevel(
      GameTestHelper helper, RobotEntity robot, GazeDirection expected) {
    helper.assertTrue(RobotFace.ownerLevel(robot) == expected, WRONG_LEVEL);
  }

  private static void moveOwnerUp(ServerPlayer owner, double blocks) {
    owner.moveTo(owner.getX(), owner.getY() + blocks, owner.getZ());
  }

  private static void assertGaze(GameTestHelper helper, RobotEntity robot, GazeDirection expected) {
    helper.assertTrue(RobotFace.ownerGaze(robot) == expected, WRONG_GAZE);
  }

  @GameTest(template = ARENA)
  public static void everyPoseHasAnAnimation(GameTestHelper helper) {
    String animationFile = ANIMATION_FOLDER + ANIMATION_NAME;
    try (InputStream file = RobotAnimationGameTests.class.getResourceAsStream(animationFile)) {
      helper.assertTrue(file != null, NO_ANIMATION_FILE);
      JsonObject animations =
          JsonParser.parseReader(new InputStreamReader(file, StandardCharsets.UTF_8))
              .getAsJsonObject()
              .getAsJsonObject(ANIMATIONS_KEY);
      for (RobotPose pose : RobotPose.values()) {
        String name = pose.name().toLowerCase(Locale.ROOT);
        helper.assertTrue(animations.has(name), NO_ANIMATION + name);
      }
      helper.assertTrue(animations.has(ARMS_RAISED), NO_ANIMATION + ARMS_RAISED);
    } catch (IOException exception) {
      helper.fail(BAD_ANIMATION_FILE);
    }
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void everyTrackFrameHasAFile(GameTestHelper helper) {
    for (int frame = 0; frame < TrackFrames.COUNT; frame++) {
      String path = FRAME_FOLDER + FRAME_NAME.formatted(frame);
      try (InputStream file = RobotAnimationGameTests.class.getResourceAsStream(path)) {
        helper.assertTrue(file != null, NO_FRAME + path);
      } catch (IOException exception) {
        helper.fail(BAD_FRAME + path);
      }
    }
    helper.succeed();
  }
}
