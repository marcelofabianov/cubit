package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.ChatTopic;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotNotices;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The robot loves redstone, so it says so when its owner mines it (answers 619 and 620). The owner
 * breaks the block through the game, so the test also proves the event reaches the robot.
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class RedstoneChatGameTests {
  private static final String ARENA = "arena";
  private static final String ALERT_BATCH = "redstone_chat_alert";
  private static final int FLOOR_Y = 1;
  private static final int STAND_Y = 2;
  private static final int MIDDLE_Z = 2;
  private static final int ROBOT_X = 6;
  private static final int ORE_X = 8;
  private static final int NEXT_TICKS = 2;
  private static final BlockPos FLOOR = new BlockPos(ROBOT_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos ORE = new BlockPos(ORE_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos NEAR = new BlockPos(ORE_X, STAND_Y, MIDDLE_Z);
  private static final String NOT_SAID = "The robot should have spoken of the redstone";
  private static final String SAID = "The robot should not have spoken of the redstone";

  private RedstoneChatGameTests() {}

  @GameTest(template = ARENA)
  public static void minedRedstoneIsARemark(GameTestHelper helper) {
    mineAfterAMoment(helper, Blocks.REDSTONE_ORE, true, false);
  }

  @GameTest(template = ARENA)
  public static void deepslateRedstoneCountsToo(GameTestHelper helper) {
    mineAfterAMoment(helper, Blocks.DEEPSLATE_REDSTONE_ORE, true, false);
  }

  @GameTest(template = ARENA)
  public static void anotherOreIsNoRemark(GameTestHelper helper) {
    mineAfterAMoment(helper, Blocks.IRON_ORE, false, false);
  }

  @GameTest(template = ARENA)
  public static void theSwitchSilencesTheRedstone(GameTestHelper helper) {
    mineAfterAMoment(helper, Blocks.REDSTONE_ORE, false, true);
  }

  /** The alert of a mob is not calm, so the robot keeps quiet. */
  @GameTest(template = ARENA, batch = ALERT_BATCH)
  public static void anAlertRobotKeepsQuiet(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    helper.setBlock(ORE, Blocks.REDSTONE_ORE);
    helper.spawnWithNoFreeWill(EntityType.ZOMBIE, NEAR);
    robot.scanners().scanMobsNow();

    helper.runAfterDelay(
        NEXT_TICKS,
        () -> {
          owner.gameMode.destroyBlock(helper.absolutePos(ORE));
          helper.assertFalse(isSaid(robot), SAID);
          helper.succeed();
        });
  }

  /** A placed robot needs two ticks before its face is calm. */
  private static void mineAfterAMoment(
      GameTestHelper helper, Block ore, boolean expected, boolean switchedOff) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    helper.setBlock(ORE, ore);
    if (switchedOff) {
      robot.chatter().toggle();
    }

    helper.runAfterDelay(
        NEXT_TICKS,
        () -> {
          owner.gameMode.destroyBlock(helper.absolutePos(ORE));
          helper.assertValueEqual(isSaid(robot), expected, expected ? NOT_SAID : SAID);
          helper.succeed();
        });
  }

  private static boolean isSaid(RobotEntity robot) {
    return RobotNotices.isHoldingName(robot, ChatTopic.REDSTONE.name());
  }
}
