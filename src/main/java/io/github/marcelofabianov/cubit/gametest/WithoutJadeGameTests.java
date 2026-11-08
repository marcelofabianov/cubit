package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The GameTests run as a dedicated server without Jade, so this proves the mod opens and the robot
 * works without it (AGENTS.md, rule 24, and answer 665).
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class WithoutJadeGameTests {
  private static final String ARENA = "arena";
  private static final String JADE_ID = "jade";
  private static final int FLOOR_Y = 1;
  private static final int MIDDLE_Z = 2;
  private static final int ROBOT_X = 6;
  private static final BlockPos FLOOR = new BlockPos(ROBOT_X, FLOOR_Y, MIDDLE_Z);
  private static final String JADE_LOADED = "These tests must run without Jade";
  private static final String NOT_WORKING = "The robot should work without Jade";

  private WithoutJadeGameTests() {}

  @GameTest(template = ARENA)
  public static void theRobotWorksWithoutJade(GameTestHelper helper) {
    helper.assertFalse(ModList.get().isLoaded(JADE_ID), JADE_LOADED);
    RobotEntity robot =
        GameTestRobots.placeNewRobot(helper, GameTestRobots.onlinePlayer(helper), FLOOR);

    helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, NOT_WORKING);
    helper.succeed();
  }
}
