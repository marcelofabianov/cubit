package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.BatteryIndicator;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotLooks;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** A dedicated server draws nothing, so these check what the client receives and its files. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class RobotLooksGameTests {
  private static final String ARENA = "arena";
  private static final String TEXTURE_FOLDER = "/assets/cubit/textures/entity/";
  private static final String COLLAR_FILE = "general_robot_collar/%s.png";
  private static final String BATTERY_FILE = "general_robot_battery/%s.png";
  private static final String FRAME_NAME = "frame";
  private static final String NO_FILE = "Missing picture: ";
  private static final String BAD_FILE = "A picture could not be read: ";
  private static final String WRONG_TIER = "Wrong tier for the collar";
  private static final String WRONG_INDICATOR = "Wrong battery indicator";
  private static final String WRONG_STATE = "The robot should be switched off";

  private static final int TIMEOUT = 200;
  private static final int SETTLE_TICKS = 3;
  private static final float HEALTH = 25f;
  private static final Energy ONE_FE = new Energy(1);
  private static final Energy AT_CRITICAL = new Energy(15_000);
  private static final Energy QUARTER = new Energy(75_000);
  private static final Energy HALF = new Energy(150_000);
  private static final Energy FULL = new Energy(300_000);
  private static final int FLOOR_Y = 1;
  private static final int START_X = 1;
  private static final int MIDDLE_Z = 2;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos SECOND_FLOOR = FLOOR.offset(MIDDLE_Z, 0, 0);

  private RobotLooksGameTests() {}

  @GameTest(template = ARENA)
  public static void theClientReceivesTheTierOfTheRobot(GameTestHelper helper) {
    RobotEntity robot = place(helper, RobotTier.ELITE, HALF);
    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertTrue(RobotLooks.tier(robot) == RobotTier.ELITE, WRONG_TIER);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void theClientReceivesTheChargeOfTheRobot(GameTestHelper helper) {
    RobotEntity empty = place(helper, RobotTier.BASE, Energy.ZERO, FLOOR);
    RobotEntity critical = place(helper, RobotTier.BASE, AT_CRITICAL, SECOND_FLOOR);
    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          assertIndicator(helper, empty, BatteryIndicator.EMPTY);
          assertIndicator(helper, critical, BatteryIndicator.CRITICAL_1);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void aRobotSwitchedOffStillShowsItsCharge(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    RobotEntity switchedOff =
        GameTestRobots.placeRobot(helper, owner, item(RobotTier.BASE, FULL), SECOND_FLOOR);
    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertTrue(
              switchedOff.powerState() == PowerState.INACTIVE_SWITCHED_OFF, WRONG_STATE);
          assertIndicator(helper, switchedOff, BatteryIndicator.NORMAL_4);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theIndicatorRisesWhileTheBatteryCharges(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot =
        GameTestRobots.placeRobot(helper, owner, item(RobotTier.BASE, QUARTER), FLOOR);
    robot.energySlot().setItem(0, new ItemStack(Items.REDSTONE_BLOCK));
    helper.runAfterDelay(
        SETTLE_TICKS, () -> assertIndicator(helper, robot, BatteryIndicator.NORMAL_1));
    helper.succeedWhen(() -> assertIndicator(helper, robot, BatteryIndicator.NORMAL_2));
  }

  @GameTest(template = ARENA)
  public static void aMinimalChargeIsNotShownAsEmpty(GameTestHelper helper) {
    RobotEntity robot = place(helper, RobotTier.BASE, ONE_FE);
    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          assertIndicator(helper, robot, BatteryIndicator.CRITICAL_1);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void everyTierHasACollarPicture(GameTestHelper helper) {
    for (RobotTier tier : RobotTier.values()) {
      assertFile(helper, COLLAR_FILE.formatted(tier.key()));
    }
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void everyIndicatorHasAPicture(GameTestHelper helper) {
    assertFile(helper, BATTERY_FILE.formatted(FRAME_NAME));
    for (BatteryIndicator indicator : BatteryIndicator.values()) {
      if (indicator != BatteryIndicator.EMPTY) {
        assertFile(helper, BATTERY_FILE.formatted(indicator.name().toLowerCase(Locale.ROOT)));
      }
    }
    helper.succeed();
  }

  private static RobotEntity place(GameTestHelper helper, RobotTier tier, Energy energy) {
    return place(helper, tier, energy, FLOOR);
  }

  private static RobotEntity place(
      GameTestHelper helper, RobotTier tier, Energy energy, BlockPos floor) {
    return GameTestRobots.placeRobot(helper, ownerBeside(helper), item(tier, energy), floor);
  }

  private static ItemStack item(RobotTier tier, Energy energy) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(CubitDataComponents.ROBOT_STATE.get(), new RobotState(tier, energy, HEALTH));
    return item;
  }

  private static ServerPlayer ownerBeside(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    Vec3 beside = helper.absoluteVec(Vec3.atBottomCenterOf(FLOOR.above().south()));
    owner.moveTo(beside.x, beside.y, beside.z);
    return owner;
  }

  private static void assertIndicator(
      GameTestHelper helper, RobotEntity robot, BatteryIndicator expected) {
    helper.assertTrue(RobotLooks.batteryIndicator(robot) == expected, WRONG_INDICATOR);
  }

  private static void assertFile(GameTestHelper helper, String name) {
    String path = TEXTURE_FOLDER + name;
    try (InputStream file = RobotLooksGameTests.class.getResourceAsStream(path)) {
      helper.assertTrue(file != null, NO_FILE + path);
    } catch (IOException exception) {
      helper.fail(BAD_FILE + path);
    }
  }
}
