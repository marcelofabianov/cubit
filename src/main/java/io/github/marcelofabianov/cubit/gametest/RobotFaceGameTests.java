package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotFace;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** A dedicated server draws nothing, so these check what reaches the client: state and files. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class RobotFaceGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 200;
  private static final int HAPPY_TICKS = 60;
  private static final int MARGIN_TICKS = 5;
  private static final int OWNER_HURT_TIME = 10;
  private static final int FAR_BLOCKS = 17;
  private static final String WORRIED_FAR = "The robot should not see a far owner";
  private static final int HURT_TICKS = 100;
  private static final int DYING_TICKS = 60;
  private static final float SMALL_HIT = 1f;
  private static final float DYING_HEALTH = 2f;
  private static final int FLOOR_Y = 1;
  private static final int START_X = 1;
  private static final int MIDDLE_Z = 2;
  private static final long FULL = 300_000;
  private static final long CRITICAL = 15_000;
  private static final long ABOVE_CRITICAL = 16_000;
  private static final float HEALTH = 25f;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos SECOND_FLOOR = FLOOR.offset(MIDDLE_Z, 0, 0);
  private static final String SCREEN_FOLDER = "/assets/cubit/textures/entity/general_robot_screen/";
  private static final String PNG = ".png";
  private static final String LEVEL_HALF = "normal_level_half";
  private static final String LEVEL_CLOSED = "normal_level_closed";
  private static final String UP_OPEN = "normal_up_open";
  private static final String UP_HALF = "normal_up_half";
  private static final String UP_CLOSED = "normal_up_closed";
  private static final String DOWN_OPEN = "normal_down_open";
  private static final String DOWN_HALF = "normal_down_half";
  private static final String DOWN_CLOSED = "normal_down_closed";
  private static final List<String> EYE_FILES =
      List.of(
          LEVEL_HALF, LEVEL_CLOSED, UP_OPEN, UP_HALF, UP_CLOSED, DOWN_OPEN, DOWN_HALF, DOWN_CLOSED);
  private static final String NO_SCREEN = "Missing screen file: ";
  private static final String BAD_FILE = "A screen file could not be read: ";
  private static final String WRONG_EXPRESSION = "Wrong expression";

  private RobotFaceGameTests() {}

  @GameTest(template = ARENA)
  public static void everyExpressionHasAScreenFile(GameTestHelper helper) {
    for (RobotExpression expression : RobotExpression.values()) {
      String file = SCREEN_FOLDER + expression.name().toLowerCase(Locale.ROOT) + PNG;
      try (InputStream screen = RobotFaceGameTests.class.getResourceAsStream(file)) {
        helper.assertTrue(screen != null, NO_SCREEN + file);
      } catch (IOException exception) {
        helper.fail(BAD_FILE + file);
      }
    }
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void everyGazeAndBlinkFrameHasAScreenFile(GameTestHelper helper) {
    for (String name : EYE_FILES) {
      String file = SCREEN_FOLDER + name + PNG;
      try (InputStream screen = RobotFaceGameTests.class.getResourceAsStream(file)) {
        helper.assertTrue(screen != null, NO_SCREEN + file);
      } catch (IOException exception) {
        helper.fail(BAD_FILE + file);
      }
    }
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aPlacedRobotIsHappyAndThenNormal(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, itemWith(FULL), FLOOR);
    // The framework marks a finished test with a beacon, which gives a player nearby an
    // achievement. The robot would cheer it, so its chatter stays off here.
    robot.chatter().toggle();

    helper.runAfterDelay(
        MARGIN_TICKS, () -> assertExpression(helper, robot, RobotExpression.HAPPY));
    helper.runAfterDelay(
        HAPPY_TICKS + MARGIN_TICKS,
        () -> {
          assertExpression(helper, robot, RobotExpression.NORMAL);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void openingTheInventoryMakesTheRobotHappy(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, itemWith(FULL), FLOOR);

    helper.runAfterDelay(
        HAPPY_TICKS + MARGIN_TICKS,
        () -> {
          assertExpression(helper, robot, RobotExpression.NORMAL);
          robot.interact(owner, InteractionHand.MAIN_HAND);
        });
    helper.runAfterDelay(
        HAPPY_TICKS + MARGIN_TICKS + MARGIN_TICKS,
        () -> {
          assertExpression(helper, robot, RobotExpression.HAPPY);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aCriticalBatteryIsSleepyEvenWhenTouched(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, itemWith(CRITICAL), FLOOR);

    helper.succeedWhen(() -> assertExpression(helper, robot, RobotExpression.SLEEPY));
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aBatteryAboveTheLimitIsNotSleepy(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, itemWith(ABOVE_CRITICAL), FLOOR);

    helper.runAfterDelay(
        MARGIN_TICKS,
        () -> {
          assertExpression(helper, robot, RobotExpression.HAPPY);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aSwitchedOffRobotHasTheScreenOff(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    GameTestRobots.placeRobot(helper, owner, itemWith(FULL), FLOOR);
    RobotEntity second = GameTestRobots.placeRobot(helper, owner, itemWith(FULL), SECOND_FLOOR);

    helper.runAfterDelay(
        MARGIN_TICKS,
        () -> {
          assertExpression(helper, second, RobotExpression.OFF);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotOnADockIsCharging(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, DockScene.FLOOR, new Energy(CRITICAL));
    RobotEntity robot = DockScene.placeOnDock(helper, owner, new Energy(CRITICAL));

    helper.succeedWhen(() -> assertExpression(helper, robot, RobotExpression.CHARGING));
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aHurtRobotIsSadAndThenStops(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, itemWith(FULL), FLOOR);

    helper.runAfterDelay(
        HAPPY_TICKS + MARGIN_TICKS,
        () -> robot.hurt(helper.getLevel().damageSources().generic(), SMALL_HIT));
    helper.runAfterDelay(
        HAPPY_TICKS + MARGIN_TICKS + MARGIN_TICKS,
        () -> assertExpression(helper, robot, RobotExpression.HURT));
    helper.runAfterDelay(
        HAPPY_TICKS + MARGIN_TICKS + HURT_TICKS + MARGIN_TICKS,
        () -> {
          assertExpression(helper, robot, RobotExpression.NORMAL);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theCrossedEyesPassToTheTearsAndThenEnd(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, itemWith(FULL), FLOOR);
    robot.setHealth(DYING_HEALTH + SMALL_HIT);

    robot.hurt(helper.getLevel().damageSources().generic(), SMALL_HIT);

    helper.runAfterDelay(
        MARGIN_TICKS, () -> assertExpression(helper, robot, RobotExpression.DYING));
    helper.runAfterDelay(
        DYING_TICKS + MARGIN_TICKS, () -> assertExpression(helper, robot, RobotExpression.HURT));
    helper.runAfterDelay(
        HAPPY_TICKS + HURT_TICKS,
        () -> {
          assertExpression(helper, robot, RobotExpression.NORMAL);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void littleHealthWithoutDamageDoesNotShowTheCrossedEyes(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, itemWith(FULL), FLOOR);

    robot.setHealth(DYING_HEALTH);

    helper.runAfterDelay(
        MARGIN_TICKS,
        () -> {
          assertExpression(helper, robot, RobotExpression.HAPPY);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theFollowingRobotWorriesWhenItsOwnerIsHurtNearby(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, itemWith(FULL), FLOOR);

    helper.runAfterDelay(HAPPY_TICKS + MARGIN_TICKS, () -> owner.hurtTime = OWNER_HURT_TIME);
    helper.runAfterDelay(
        HAPPY_TICKS + MARGIN_TICKS + 2,
        () -> {
          assertExpression(helper, robot, RobotExpression.WORRIED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotDoesNotWorryAboutAFarOwner(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, itemWith(FULL), FLOOR);

    helper.runAfterDelay(
        HAPPY_TICKS + MARGIN_TICKS,
        () -> {
          Vec3 far = robot.position().add(FAR_BLOCKS, 0, 0);
          owner.moveTo(far.x, far.y, far.z);
          owner.hurtTime = OWNER_HURT_TIME;
        });
    helper.runAfterDelay(
        HAPPY_TICKS + MARGIN_TICKS + 2,
        () -> {
          helper.assertFalse(RobotFace.expression(robot) == RobotExpression.WORRIED, WORRIED_FAR);
          helper.succeed();
        });
  }

  private static void assertExpression(
      GameTestHelper helper, RobotEntity robot, RobotExpression expression) {
    helper.assertValueEqual(RobotFace.expression(robot), expression, WRONG_EXPRESSION);
  }

  private static ServerPlayer ownerBeside(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    Vec3 beside = helper.absoluteVec(Vec3.atBottomCenterOf(FLOOR.above().south()));
    owner.moveTo(beside.x, beside.y, beside.z);
    return owner;
  }

  private static ItemStack itemWith(long energy) {
    ItemStack stack = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    stack.set(
        CubitDataComponents.ROBOT_STATE.get(),
        new RobotState(RobotTier.BASE, new Energy(energy), HEALTH));
    return stack;
  }
}
