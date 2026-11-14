package io.github.marcelofabianov.cubit.gametest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.RobotNotice;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotNotices;
import io.github.marcelofabianov.cubit.player.FollowingRobotLink;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The text itself only shows on a client, so these check what the server decides and its file. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class RobotNoticeGameTests {
  private static final String ARENA = "arena";
  private static final String LANGUAGE_FILE = "/assets/cubit/lang/en_us.json";
  private static final String NOTICE_FORMAT = "Cubit: %s";
  private static final String CRITICAL_BATTERY_TEXT = "Critical battery";
  private static final String HURT_TEXT = "That hurts!";
  private static final String OWNER_HURT_TEXT = "Careful!";
  private static final String NEED_RECHARGE_TEXT = "I need to recharge";
  private static final String ITEMS_UNLOADED_TEXT = "Items unloaded";
  private static final String NO_ROOM_TEXT = "No room in the marked chests";
  private static final Map<RobotNotice, String> TEXTS =
      Map.of(
          RobotNotice.CRITICAL_BATTERY,
          CRITICAL_BATTERY_TEXT,
          RobotNotice.HURT,
          HURT_TEXT,
          RobotNotice.OWNER_HURT,
          OWNER_HURT_TEXT,
          RobotNotice.NEED_RECHARGE,
          NEED_RECHARGE_TEXT,
          RobotNotice.ITEMS_UNLOADED,
          ITEMS_UNLOADED_TEXT,
          RobotNotice.NO_ROOM,
          NO_ROOM_TEXT);
  private static final String NO_LANGUAGE_FILE = "The language file is missing";
  private static final String BAD_LANGUAGE_FILE = "The language file could not be read";
  private static final String WRONG_TEXT = "Wrong text for the notice: ";
  private static final String WRONG_FORMAT = "Wrong prefix for the notices";
  private static final String SHOULD_WARN = "The robot should have spoken";
  private static final String SHOULD_NOT_WARN = "The robot should not have spoken";
  private static final String WRONG_INTERVAL = "Wrong interval between notices";
  private static final String WRONG_STATE = "The robot should be switched off";
  private static final String WRONG_ACTIVE_STATE = "The robot should still be active";

  private static final int SETTLE_TICKS = 3;
  private static final int REPEAT_TICKS = 500;
  private static final int OWNER_HURT_TIME = 10;
  private static final float SMALL_HIT = 1f;
  private static final float HEALTH = 25f;
  private static final Energy AT_CRITICAL = new Energy(15_000);
  private static final Energy ABOVE_CRITICAL = new Energy(16_000);
  private static final int FLOOR_Y = 1;
  private static final int START_X = 1;
  private static final int MIDDLE_Z = 2;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos SECOND_FLOOR = FLOOR.offset(MIDDLE_Z, 0, 0);

  private RobotNoticeGameTests() {}

  @GameTest(template = ARENA)
  public static void theRobotThatFollowsWarnsAtTheCriticalBattery(GameTestHelper helper) {
    RobotEntity robot =
        GameTestRobots.placeRobot(helper, ownerBeside(helper), item(AT_CRITICAL), FLOOR);
    assertAfterSettling(helper, robot, RobotNotice.CRITICAL_BATTERY, true);
  }

  @GameTest(template = ARENA)
  public static void aRobotAboveTheCriticalBatteryDoesNotWarn(GameTestHelper helper) {
    RobotEntity robot =
        GameTestRobots.placeRobot(helper, ownerBeside(helper), item(ABOVE_CRITICAL), FLOOR);
    assertAfterSettling(helper, robot, RobotNotice.CRITICAL_BATTERY, false);
  }

  @GameTest(template = ARENA)
  public static void aRobotThatDoesNotFollowDoesNotWarn(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    RobotEntity switchedOff =
        GameTestRobots.placeRobot(helper, owner, item(AT_CRITICAL), SECOND_FLOOR);
    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertTrue(
              switchedOff.powerState() == PowerState.INACTIVE_SWITCHED_OFF, WRONG_STATE);
          helper.assertFalse(
              RobotNotices.isHolding(switchedOff, RobotNotice.CRITICAL_BATTERY), SHOULD_NOT_WARN);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void aRobotWithTheLinkButNoEnergyDoesNotWarn(GameTestHelper helper) {
    RobotEntity robot =
        GameTestRobots.placeRobot(helper, ownerBeside(helper), item(Energy.ZERO), FLOOR);
    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertTrue(robot.powerState() == PowerState.INACTIVE_EXHAUSTED, WRONG_STATE);
          helper.assertFalse(
              RobotNotices.isHolding(robot, RobotNotice.CRITICAL_BATTERY), SHOULD_NOT_WARN);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void anActiveRobotWithoutTheFollowLinkDoesNotWarn(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item(AT_CRITICAL), FLOOR);
    owner.setData(CubitAttachments.FOLLOWING_ROBOT, new FollowingRobotLink(Optional.empty()));
    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertTrue(robot.powerState() == PowerState.ACTIVE, WRONG_ACTIVE_STATE);
          helper.assertFalse(
              RobotNotices.isHolding(robot, RobotNotice.CRITICAL_BATTERY), SHOULD_NOT_WARN);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void theRobotThatFollowsComplainsWhenHurt(GameTestHelper helper) {
    RobotEntity robot =
        GameTestRobots.placeRobot(helper, ownerBeside(helper), item(ABOVE_CRITICAL), FLOOR);
    robot.hurt(helper.getLevel().damageSources().generic(), SMALL_HIT);
    assertAfterSettling(helper, robot, RobotNotice.HURT, true);
  }

  @GameTest(template = ARENA)
  public static void aRobotUnhurtDoesNotComplain(GameTestHelper helper) {
    RobotEntity robot =
        GameTestRobots.placeRobot(helper, ownerBeside(helper), item(ABOVE_CRITICAL), FLOOR);
    assertAfterSettling(helper, robot, RobotNotice.HURT, false);
  }

  @GameTest(template = ARENA)
  public static void aRobotSwitchedOffDoesNotComplain(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    RobotEntity switchedOff =
        GameTestRobots.placeRobot(helper, owner, item(ABOVE_CRITICAL), SECOND_FLOOR);
    switchedOff.hurt(helper.getLevel().damageSources().generic(), SMALL_HIT);
    assertAfterSettling(helper, switchedOff, RobotNotice.HURT, false);
  }

  @GameTest(template = ARENA)
  public static void theRobotThatFollowsWarnsAnOwnerHurtNearby(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item(ABOVE_CRITICAL), FLOOR);
    owner.hurtTime = OWNER_HURT_TIME;
    assertAfterSettling(helper, robot, RobotNotice.OWNER_HURT, true);
  }

  @GameTest(template = ARENA)
  public static void anOwnerUnhurtGetsNoWarning(GameTestHelper helper) {
    RobotEntity robot =
        GameTestRobots.placeRobot(helper, ownerBeside(helper), item(ABOVE_CRITICAL), FLOOR);
    assertAfterSettling(helper, robot, RobotNotice.OWNER_HURT, false);
  }

  @GameTest(template = ARENA)
  public static void theIntervalComesFromTheConfig(GameTestHelper helper) {
    helper.assertTrue(CubitMod.CONFIG.noticeRepeatTicks() == REPEAT_TICKS, WRONG_INTERVAL);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void everyNoticeHasItsText(GameTestHelper helper) {
    try (InputStream file = RobotNoticeGameTests.class.getResourceAsStream(LANGUAGE_FILE)) {
      helper.assertTrue(file != null, NO_LANGUAGE_FILE);
      JsonObject language =
          JsonParser.parseReader(new InputStreamReader(file, StandardCharsets.UTF_8))
              .getAsJsonObject();
      for (RobotNotice notice : RobotNotice.values()) {
        helper.assertTrue(
            TEXTS.get(notice).equals(language.get(RobotNotices.keyOf(notice)).getAsString()),
            WRONG_TEXT + notice);
      }
      helper.assertTrue(
          NOTICE_FORMAT.equals(language.get(RobotNotices.FORMAT_KEY).getAsString()), WRONG_FORMAT);
    } catch (IOException exception) {
      helper.fail(BAD_LANGUAGE_FILE);
    }
    helper.succeed();
  }

  private static void assertAfterSettling(
      GameTestHelper helper, RobotEntity robot, RobotNotice notice, boolean spoke) {
    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertTrue(
              RobotNotices.isHolding(robot, notice) == spoke,
              spoke ? SHOULD_WARN : SHOULD_NOT_WARN);
          helper.succeed();
        });
  }

  private static ItemStack item(Energy energy) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(CubitDataComponents.ROBOT_STATE.get(), new RobotState(RobotTier.BASE, energy, HEALTH));
    return item;
  }

  private static ServerPlayer ownerBeside(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    Vec3 beside = helper.absoluteVec(Vec3.atBottomCenterOf(FLOOR.above().south()));
    owner.moveTo(beside.x, beside.y, beside.z);
    return owner;
  }
}
