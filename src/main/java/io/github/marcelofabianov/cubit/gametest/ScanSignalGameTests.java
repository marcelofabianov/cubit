package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.ScanSignal;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotScanners;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * What the antennas and the halo show, as the server sends it to every client (answers 638 to 640).
 * Each test has a batch of its own, so it never sees the mobs and the ores next to it.
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ScanSignalGameTests {
  private static final String ARENA = "arena";
  private static final String MOB_BATCH = "scan_signal_mob";
  private static final String ORE_BATCH = "scan_signal_ore";
  private static final String FILTER_BATCH = "scan_signal_filter";
  private static final String BOTH_BATCH = "scan_signal_both";
  private static final String HURT_BATCH = "scan_signal_hurt";
  private static final String OFF_BATCH = "scan_signal_off";
  private static final int FLOOR_Y = 1;
  private static final int STAND_Y = 2;
  private static final int MIDDLE_Z = 2;
  private static final int ROBOT_X = 6;
  private static final int NEAR_X = 8;
  private static final int NEXT_TICKS = 2;
  private static final float SMALL_DAMAGE = 1f;
  private static final BlockPos FLOOR = new BlockPos(ROBOT_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos ORE = new BlockPos(NEAR_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos NEAR = new BlockPos(NEAR_X, STAND_Y, MIDDLE_Z);
  private static final String WRONG_SIGNAL = "Wrong signal of the scanners";

  private ScanSignalGameTests() {}

  @GameTest(template = ARENA, batch = MOB_BATCH)
  public static void aMobInRangeTurnsItRed(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    helper.spawnWithNoFreeWill(EntityType.ZOMBIE, NEAR);
    robot.scanners().scanMobsNow();

    expectAfterATick(helper, robot, ScanSignal.MOB);
  }

  @GameTest(template = ARENA, batch = ORE_BATCH)
  public static void aRareOreTurnsItBlue(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    helper.setBlock(ORE, Blocks.DIAMOND_ORE);
    robot.scanners().scanOresNow();

    expectAfterATick(helper, robot, ScanSignal.ORE);
  }

  @GameTest(template = ARENA, batch = FILTER_BATCH)
  public static void anOreOfTheFilterTurnsItBlueToo(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    helper.setBlock(ORE, Blocks.COAL_ORE);
    GameTestRobots.clickRobot(owner, robot, new ItemStack(Items.COAL_ORE));
    robot.scanners().scanOresNow();

    expectAfterATick(helper, robot, ScanSignal.ORE);
  }

  @GameTest(template = ARENA, batch = BOTH_BATCH)
  public static void aMobWinsOverAnOre(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    helper.setBlock(ORE, Blocks.DIAMOND_ORE);
    helper.spawnWithNoFreeWill(EntityType.ZOMBIE, NEAR);
    robot.scanners().scanOresNow();
    robot.scanners().scanMobsNow();

    expectAfterATick(helper, robot, ScanSignal.MOB);
  }

  /** A wound changes the face, but not the red of a mob that is still in range. */
  @GameTest(template = ARENA, batch = HURT_BATCH)
  public static void aWoundKeepsTheRed(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    helper.spawnWithNoFreeWill(EntityType.ZOMBIE, NEAR);
    robot.scanners().scanMobsNow();
    robot.hurt(helper.getLevel().damageSources().generic(), SMALL_DAMAGE);

    expectAfterATick(helper, robot, ScanSignal.MOB);
  }

  @GameTest(template = ARENA, batch = OFF_BATCH)
  public static void theSwitchTurnsItOff(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    helper.spawnWithNoFreeWill(EntityType.ZOMBIE, NEAR);
    robot.scanners().scanMobsNow();
    robot.scanners().toggle();

    expectAfterATick(helper, robot, ScanSignal.NONE);
  }

  private static RobotEntity robot(GameTestHelper helper) {
    return GameTestRobots.placeNewRobot(helper, GameTestRobots.onlinePlayer(helper), FLOOR);
  }

  private static void expectAfterATick(
      GameTestHelper helper, RobotEntity robot, ScanSignal expected) {
    helper.runAfterDelay(
        NEXT_TICKS,
        () -> {
          helper.assertValueEqual(RobotScanners.signal(robot), expected, WRONG_SIGNAL);
          helper.succeed();
        });
  }
}
