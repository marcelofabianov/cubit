package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.ContinuousSound;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotAudio;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotSolar;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.BeforeBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The solar panel on the server. The other tests run at night, so they do not depend on the sky,
 * and the tests that need the sun run in a batch of their own.
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class SolarGameTests {
  static final String DAYLIGHT_BATCH = "daylight";
  private static final String DEFAULT_BATCH = "defaultBatch";
  private static final long NOON = 6000;
  private static final long MIDNIGHT = 18000;
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 200;
  private static final int SETTLED_TICKS = 45;
  private static final int MEASURED_TICKS = 20;
  private static final long GAINED_IN_THE_MEASURE = 100;
  private static final int FLOOR_Y = 1;
  private static final int MIDDLE_Z = 2;
  private static final int START_X = 3;
  private static final int ROOF_HEIGHT = 3;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final float HEALTH = 25f;
  private static final Energy SOME_ENERGY = new Energy(100_000);
  private static final Energy FULL = new Energy(300_000);
  private static final String ENTITY = "/assets/cubit/textures/entity/general_robot_solar/";
  private static final String GLOW_FILE = ENTITY + "glow.png";
  private static final String NOT_CHARGING = "The panel should be charging";
  private static final String CHARGING = "The panel should not be charging";
  private static final String WRONG_GAIN = "Wrong energy from the panel";
  private static final String GAINED = "The panel gave energy";
  private static final String NOISY = "The panel should not play the recharge sound";
  private static final String MISSING_FILE = "A file of the panel is missing: ";
  private static final String UNREADABLE_FILE = "A file of the panel cannot be read: ";

  private SolarGameTests() {}

  @BeforeBatch(batch = DEFAULT_BATCH)
  public static void startAtNight(ServerLevel level) {
    level.setDayTime(MIDNIGHT);
  }

  @BeforeBatch(batch = DAYLIGHT_BATCH)
  public static void startAtNoon(ServerLevel level) {
    level.setDayTime(NOON);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = DAYLIGHT_BATCH)
  public static void theSunFillsTheBatteryInSilence(GameTestHelper helper) {
    RobotEntity robot = place(helper, helper.makeMockPlayer(GameType.SURVIVAL), SOME_ENERGY);

    helper.runAfterDelay(
        SETTLED_TICKS,
        () -> {
          helper.assertTrue(RobotSolar.isCharging(robot), NOT_CHARGING);
          helper.assertTrue(!RobotAudio.playing(robot).contains(ContinuousSound.RECHARGING), NOISY);
          long before = robot.state().energy().amount();
          helper.runAfterDelay(
              MEASURED_TICKS,
              () -> {
                long gained = robot.state().energy().amount() - before;
                helper.assertValueEqual(gained, GAINED_IN_THE_MEASURE, WRONG_GAIN);
                helper.succeed();
              });
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = DAYLIGHT_BATCH)
  public static void anOpaqueBlockAboveStopsThePanel(GameTestHelper helper) {
    assertRoof(helper, Blocks.STONE, false);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = DAYLIGHT_BATCH)
  public static void glassAboveLetsThePanelWork(GameTestHelper helper) {
    assertRoof(helper, Blocks.GLASS, true);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = DAYLIGHT_BATCH)
  public static void aFullBatteryTakesNothingAndStopsTheSignal(GameTestHelper helper) {
    RobotEntity robot = place(helper, helper.makeMockPlayer(GameType.SURVIVAL), FULL);

    helper.runAfterDelay(
        SETTLED_TICKS,
        () -> {
          helper.assertTrue(!RobotSolar.isCharging(robot), CHARGING);
          helper.assertValueEqual(robot.state().energy(), FULL, GAINED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = DAYLIGHT_BATCH)
  public static void aRobotWithoutEnergyDoesNotUseThePanel(GameTestHelper helper) {
    RobotEntity robot = place(helper, helper.makeMockPlayer(GameType.SURVIVAL), Energy.ZERO);

    helper.runAfterDelay(
        SETTLED_TICKS,
        () -> {
          helper.assertTrue(robot.powerState() != PowerState.ACTIVE, CHARGING);
          helper.assertTrue(!RobotSolar.isCharging(robot), CHARGING);
          helper.assertValueEqual(robot.state().energy(), Energy.ZERO, GAINED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = DAYLIGHT_BATCH)
  public static void aHibernatingRobotDoesNotUseThePanel(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, DockScene.FLOOR, Energy.ZERO);
    RobotEntity robot = DockScene.placeOnDock(helper, owner, SOME_ENERGY);

    helper.runAfterDelay(
        SETTLED_TICKS,
        () -> {
          helper.assertTrue(!RobotSolar.isCharging(robot), CHARGING);
          helper.assertValueEqual(robot.state().energy(), SOME_ENERGY, GAINED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theBaseTierGetsNothingAtNight(GameTestHelper helper) {
    RobotEntity robot = place(helper, helper.makeMockPlayer(GameType.SURVIVAL), SOME_ENERGY);

    helper.runAfterDelay(
        SETTLED_TICKS,
        () -> {
          helper.assertTrue(!RobotSolar.isCharging(robot), CHARGING);
          helper.assertValueEqual(robot.state().energy(), SOME_ENERGY, GAINED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void theFilesOfThePanelExist(GameTestHelper helper) {
    for (String file : List.of(GLOW_FILE)) {
      try (InputStream stream = SolarGameTests.class.getResourceAsStream(file)) {
        helper.assertTrue(stream != null, MISSING_FILE + file);
      } catch (IOException exception) {
        throw new IllegalStateException(UNREADABLE_FILE + file, exception);
      }
    }
    helper.succeed();
  }

  private static void assertRoof(GameTestHelper helper, Block roof, boolean charges) {
    RobotEntity robot = place(helper, helper.makeMockPlayer(GameType.SURVIVAL), SOME_ENERGY);
    helper.setBlock(FLOOR.above(ROOF_HEIGHT), roof);

    helper.runAfterDelay(
        SETTLED_TICKS,
        () -> {
          helper.assertValueEqual(RobotSolar.isCharging(robot), charges, WRONG_GAIN);
          helper.succeed();
        });
  }

  /** The lamp stays switched off, so what it spends does not mix with what the panel gives. */
  private static RobotEntity place(GameTestHelper helper, Player owner, Energy energy) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(
        CubitDataComponents.ROBOT_STATE.get(),
        new RobotState(RobotTier.BASE, energy, HEALTH, false));
    return GameTestRobots.placeRobot(helper, owner, item, FLOOR);
  }
}
