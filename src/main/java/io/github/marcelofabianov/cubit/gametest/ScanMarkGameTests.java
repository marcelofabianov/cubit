package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotScanMarks;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * What the scanners mark for the owner to see a box around (answers 596, 597, 606 and 608). Each
 * test has a batch of its own, so it never sees the mobs and the ores of the test next to it.
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ScanMarkGameTests {
  private static final String ARENA = "arena";
  private static final String ORES_BATCH = "scan_marks_ores";
  private static final String WAITING_BATCH = "scan_marks_waiting";
  private static final String FILTER_BATCH = "scan_marks_filter";
  private static final String MOBS_BATCH = "scan_marks_mobs";
  private static final String NOTHING_BATCH = "scan_marks_nothing";
  private static final int FLOOR_Y = 1;
  private static final int STAND_Y = 2;
  private static final int MIDDLE_Z = 2;
  private static final int ROBOT_X = 6;
  private static final int CORNER = 4;
  private static final BlockPos FLOOR = new BlockPos(ROBOT_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos DIAMOND = new BlockPos(ROBOT_X + 2, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos IRON = new BlockPos(ROBOT_X - 2, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos SECOND_DIAMOND = new BlockPos(ROBOT_X + 3, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos NEAR = new BlockPos(ROBOT_X + 2, STAND_Y, MIDDLE_Z);
  private static final int MOB_CORNER_X = 7;
  private static final int MOB_CORNER_Y = 5;
  private static final BlockPos CUBE_CORNER =
      new BlockPos(ROBOT_X + CORNER, STAND_Y + CORNER, MIDDLE_Z);
  private static final BlockPos MOB_CUBE_CORNER =
      new BlockPos(ROBOT_X + MOB_CORNER_X, STAND_Y + MOB_CORNER_Y, MIDDLE_Z);
  private static final String WRONG_ORES = "Wrong ores marked";
  private static final String WRONG_MOBS = "Wrong mobs marked";

  private ScanMarkGameTests() {}

  @GameTest(template = ARENA, batch = ORES_BATCH)
  public static void everyNewOreOfASweepIsMarkedOnce(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    helper.setBlock(DIAMOND, Blocks.DIAMOND_ORE);
    helper.setBlock(IRON, Blocks.IRON_ORE);

    robot.scanners().scanOresNow();
    assertOres(helper, robot, Set.of(DIAMOND, IRON));

    robot.scanners().scanOresNow();
    assertOres(helper, robot, Set.of());
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = WAITING_BATCH)
  public static void aNewOreIsMarkedWhileItsTextWaits(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    helper.setBlock(DIAMOND, Blocks.DIAMOND_ORE);
    robot.scanners().scanOresNow();

    helper.setBlock(SECOND_DIAMOND, Blocks.DIAMOND_ORE);
    robot.scanners().scanOresNow();

    assertOres(helper, robot, Set.of(SECOND_DIAMOND));
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = FILTER_BATCH)
  public static void anOreOnlyOfTheFilterIsMarked(GameTestHelper helper) {
    Player owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    helper.setBlock(DIAMOND, Blocks.COAL_ORE);

    robot.scanners().scanOresNow();
    assertOres(helper, robot, Set.of());

    GameTestRobots.clickRobot(owner, robot, new ItemStack(Items.COAL_ORE));
    robot.scanners().scanOresNow();
    assertOres(helper, robot, Set.of(DIAMOND));
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = MOBS_BATCH)
  public static void theMobsInRangeAreMarkedAtEachScan(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    Zombie zombie = helper.spawnWithNoFreeWill(EntityType.ZOMBIE, NEAR);

    robot.scanners().scanMobsNow();
    helper.assertValueEqual(marks(robot).lastMobs(), List.of(zombie.getId()), WRONG_MOBS);

    zombie.discard();
    robot.scanners().scanMobsNow();
    helper.assertValueEqual(marks(robot).lastMobs(), List.of(), WRONG_MOBS);
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = NOTHING_BATCH)
  public static void nothingOutOfTheSphereIsMarked(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    helper.setBlock(CUBE_CORNER, Blocks.DIAMOND_ORE);
    helper.spawnWithNoFreeWill(EntityType.ZOMBIE, MOB_CUBE_CORNER);

    robot.scanners().scanMobsNow();
    robot.scanners().scanOresNow();

    assertOres(helper, robot, Set.of());
    helper.assertValueEqual(marks(robot).lastMobs(), List.of(), WRONG_MOBS);
    helper.succeed();
  }

  private static RobotEntity robot(GameTestHelper helper) {
    return GameTestRobots.placeNewRobot(helper, GameTestRobots.onlinePlayer(helper), FLOOR);
  }

  private static RobotScanMarks marks(RobotEntity robot) {
    return robot.scanners().marks();
  }

  private static void assertOres(GameTestHelper helper, RobotEntity robot, Set<BlockPos> relative) {
    Set<BlockPos> expected = Set.copyOf(relative.stream().map(helper::absolutePos).toList());
    helper.assertValueEqual(Set.copyOf(marks(robot).lastOres()), expected, WRONG_ORES);
  }
}
