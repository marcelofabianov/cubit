package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.robot.OreFilter;
import io.github.marcelofabianov.cubit.domain.robot.OreKind;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotFace;
import io.github.marcelofabianov.cubit.entity.RobotNotices;
import io.github.marcelofabianov.cubit.entity.RobotOreScan;
import io.github.marcelofabianov.cubit.entity.RobotScreenReadout;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The scanners read the world, spend and warn. Each test that expects to find nothing has a batch
 * of its own, so it never sees the mobs and the ores of the test next to it.
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ScannerGameTests {
  private static final String ARENA = "arena";
  private static final String MOBS_BATCH = "scanner_mobs";
  private static final String QUIET_BATCH = "scanner_quiet";
  private static final String LEAVING_BATCH = "scanner_leaving";
  private static final String RARE_BATCH = "scanner_rare";
  private static final String COMMON_BATCH = "scanner_common";
  private static final String FILTER_BATCH = "scanner_filter";
  private static final String INTERVAL_BATCH = "scanner_interval";
  private static final String SWITCH_BATCH = "scanner_switch";
  private static final int FLOOR_Y = 1;
  private static final int STAND_Y = 2;
  private static final int MIDDLE_Z = 2;
  private static final int ROBOT_X = 6;
  private static final int NEAR_X = 8;
  private static final BlockPos FLOOR = new BlockPos(ROBOT_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos SECOND_FLOOR = new BlockPos(NEAR_X + NEAR_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos NEAR = new BlockPos(NEAR_X, STAND_Y, MIDDLE_Z);
  private static final BlockPos ORE = new BlockPos(NEAR_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos FAR_AWAY = new BlockPos(5_000_000, 64, 5_000_000);
  private static final int NEXT_TICKS = 2;
  private static final int MOB_SCAN_TICKS = 200;
  private static final int AFTER_MOB_SCAN_TICKS = 210;
  private static final int LONG_TIMEOUT = 300;
  private static final long MOB_SCAN_COST = 50;
  private static final long ORE_SCAN_COST = 50;
  private static final String CORNER_BATCH = "scanner_corner";
  private static final String EDGE_BATCH = "scanner_edge";
  private static final int RADIUS = 5;
  private static final int CORNER = 4;
  private static final int MOB_AHEAD = 7;
  private static final int MOB_CORNER_X = 7;
  private static final int MOB_CORNER_Y = 5;
  private static final BlockPos CUBE_CORNER =
      new BlockPos(ROBOT_X + CORNER, STAND_Y + CORNER, MIDDLE_Z);
  private static final BlockPos MOB_CUBE_CORNER =
      new BlockPos(ROBOT_X + MOB_CORNER_X, STAND_Y + MOB_CORNER_Y, MIDDLE_Z);
  private static final BlockPos ORE_AT_RADIUS = new BlockPos(ROBOT_X + RADIUS, STAND_Y, MIDDLE_Z);
  private static final BlockPos MOB_IN_RADIUS =
      new BlockPos(ROBOT_X + MOB_AHEAD, STAND_Y, MIDDLE_Z);
  private static final String ZOMBIE_NAME = "entity.minecraft.zombie";
  private static final String DIAMOND_TAG = "c:ores/diamond";
  private static final String COAL_TAG = "c:ores/coal";
  private static final OreKind COAL = new OreKind(COAL_TAG);
  private static final OreFilter COAL_FILTER = new OreFilter(List.of(COAL));
  private static final String INVALID_TAG = "minecraft:stone";
  private static final String SCANNERS_KEY = "ScannersEnabled";
  private static final String ORE_FILTER_KEY = "OreFilter";
  private static final String NO_ENEMY = "The scanner should have found the mob";
  private static final String ENEMY = "The scanner should have found no hostile mob";
  private static final String WRONG_COST = "Wrong cost of the scan";
  private static final String NO_WARNING = "The robot should have warned of it";
  private static final String WARNED = "The robot should not have warned";
  private static final String NO_ALERT = "The robot should show the alert expression";
  private static final String NOT_SURPRISED = "The robot should be surprised";
  private static final String SURPRISED = "The robot should not be surprised";
  private static final String WRONG_FILTER = "Wrong ore filter";
  private static final String SPENT_ITEM = "The ore in the hand should stay";
  private static final String WRONG_SWITCH = "Wrong switch of the scanners";
  private static final String READ_UNLOADED = "A block in an unloaded chunk was read";
  private static final String LOADED = "Reading loaded a chunk";

  private ScannerGameTests() {}

  @GameTest(template = ARENA, batch = MOBS_BATCH)
  public static void aMobInRangeWarnsSpendsAndAlerts(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    helper.spawnWithNoFreeWill(EntityType.ZOMBIE, NEAR);
    long before = charge(robot);

    robot.scanners().scanMobsNow();

    helper.assertTrue(robot.scanners().enemyInRange(), NO_ENEMY);
    helper.assertTrue(before - charge(robot) == MOB_SCAN_COST, WRONG_COST);
    helper.assertTrue(RobotNotices.isHoldingName(robot, ZOMBIE_NAME), NO_WARNING);
    helper.runAfterDelay(
        NEXT_TICKS,
        () -> {
          helper.assertValueEqual(RobotFace.expression(robot), RobotExpression.ALERT, NO_ALERT);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, batch = QUIET_BATCH)
  public static void peacefulAndCalmNeutralMobsAreNotHostile(GameTestHelper helper) {
    Player owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    helper.spawnWithNoFreeWill(EntityType.COW, NEAR);
    Wolf wolf = helper.spawnWithNoFreeWill(EntityType.WOLF, NEAR);

    robot.scanners().scanMobsNow();
    helper.assertFalse(robot.scanners().enemyInRange(), ENEMY);

    wolf.setTarget(owner);
    robot.scanners().scanMobsNow();
    helper.assertTrue(robot.scanners().enemyInRange(), NO_ENEMY);
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = LEAVING_BATCH)
  public static void theAlertEndsWhenTheMobIsGone(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    Zombie zombie = helper.spawnWithNoFreeWill(EntityType.ZOMBIE, NEAR);
    robot.scanners().scanMobsNow();

    zombie.discard();
    robot.scanners().scanMobsNow();

    helper.assertFalse(robot.scanners().enemyInRange(), ENEMY);
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = RARE_BATCH)
  public static void aRareOreWarnsSpendsAndSurprises(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    helper.setBlock(ORE, Blocks.DIAMOND_ORE);
    long before = charge(robot);

    robot.scanners().scanOresNow();

    helper.assertTrue(before - charge(robot) == ORE_SCAN_COST, WRONG_COST);
    helper.assertTrue(RobotNotices.isHoldingName(robot, DIAMOND_TAG), NO_WARNING);
    helper.assertTrue(robot.scanners().surprised(), NOT_SURPRISED);
    helper.succeed();
  }

  /** The corner of the cube is farther than the radius, so the sphere leaves it out (604, 606). */
  @GameTest(template = ARENA, batch = CORNER_BATCH)
  public static void theCornerOfTheCubeIsOutOfRange(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    helper.setBlock(CUBE_CORNER, Blocks.DIAMOND_ORE);
    helper.spawnWithNoFreeWill(EntityType.ZOMBIE, MOB_CUBE_CORNER);

    robot.scanners().scanMobsNow();
    robot.scanners().scanOresNow();

    helper.assertFalse(robot.scanners().enemyInRange(), ENEMY);
    helper.assertFalse(RobotNotices.isHoldingName(robot, DIAMOND_TAG), WARNED);
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = EDGE_BATCH)
  public static void theRadiusIsReachedStraightAhead(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    helper.setBlock(ORE_AT_RADIUS, Blocks.DIAMOND_ORE);
    helper.spawnWithNoFreeWill(EntityType.ZOMBIE, MOB_IN_RADIUS);

    robot.scanners().scanMobsNow();
    robot.scanners().scanOresNow();

    helper.assertTrue(robot.scanners().enemyInRange(), NO_ENEMY);
    helper.assertTrue(RobotNotices.isHoldingName(robot, DIAMOND_TAG), NO_WARNING);
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = COMMON_BATCH)
  public static void aCommonOreOnlyWarnsFromTheFilter(GameTestHelper helper) {
    Player owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    helper.setBlock(ORE, Blocks.COAL_ORE);

    robot.scanners().scanOresNow();
    helper.assertFalse(RobotNotices.isHoldingName(robot, COAL_TAG), WARNED);

    GameTestRobots.clickRobot(owner, robot, new ItemStack(Items.COAL_ORE));
    robot.scanners().scanOresNow();
    helper.assertTrue(RobotNotices.isHoldingName(robot, COAL_TAG), NO_WARNING);
    helper.assertFalse(robot.scanners().surprised(), SURPRISED);
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = FILTER_BATCH)
  public static void aClickWithAnOreSetsAndClearsTheFilter(GameTestHelper helper) {
    Player owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    ItemStack held = GameTestRobots.clickRobot(owner, robot, new ItemStack(Items.COAL_ORE));
    helper.assertValueEqual(robot.scanners().oreFilter(), COAL_FILTER, WRONG_FILTER);
    helper.assertTrue(held.is(Items.COAL_ORE), SPENT_ITEM);

    GameTestRobots.clickRobot(owner, robot, new ItemStack(Items.COAL_ORE));
    helper.assertValueEqual(robot.scanners().oreFilter(), OreFilter.EMPTY, WRONG_FILTER);
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = FILTER_BATCH)
  public static void onlyTheOwnerChoosesTheOre(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    ServerPlayer stranger = GameTestRobots.onlinePlayer(helper);

    GameTestRobots.clickRobot(stranger, robot, new ItemStack(Items.COAL_ORE));

    helper.assertValueEqual(robot.scanners().oreFilter(), OreFilter.EMPTY, WRONG_FILTER);
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = FILTER_BATCH)
  public static void aRobotSwitchedOffAlsoTakesTheOre(GameTestHelper helper) {
    Player owner = GameTestRobots.onlinePlayer(helper);
    GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    RobotEntity second = GameTestRobots.placeNewRobot(helper, owner, SECOND_FLOOR);

    GameTestRobots.clickRobot(owner, second, new ItemStack(Items.COAL_ORE));

    helper.assertValueEqual(second.scanners().oreFilter(), COAL_FILTER, WRONG_FILTER);
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = SWITCH_BATCH)
  public static void theSwitchStopsTheScansAndWhatTheyCost(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    robot.scanners().toggle();
    long before = charge(robot);

    robot.scanners().scanMobsNow();
    robot.scanners().scanOresNow();

    helper.assertTrue(before == charge(robot), WRONG_COST);
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = SWITCH_BATCH)
  public static void aRobotThatDoesNotFollowDoesNotScan(GameTestHelper helper) {
    Player owner = GameTestRobots.onlinePlayer(helper);
    GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    RobotEntity second = GameTestRobots.placeNewRobot(helper, owner, SECOND_FLOOR);
    long before = charge(second);

    second.scanners().scanMobsNow();
    second.scanners().scanOresNow();

    helper.assertTrue(before == charge(second), WRONG_COST);
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = SWITCH_BATCH)
  public static void theMenuButtonTogglesTheSwitchOnlyForTheOwner(GameTestHelper helper) {
    Player owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    Player stranger = GameTestRobots.onlinePlayer(helper);
    owner.moveTo(robot.position());
    stranger.moveTo(robot.position());

    menuOf(robot, stranger).clickMenuButton(stranger, RobotMenu.SCANNERS_BUTTON);
    helper.assertTrue(robot.scanners().isOn(), WRONG_SWITCH);

    menuOf(robot, owner).clickMenuButton(owner, RobotMenu.SCANNERS_BUTTON);
    helper.assertFalse(robot.scanners().isOn(), WRONG_SWITCH);
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = INTERVAL_BATCH, timeoutTicks = LONG_TIMEOUT)
  public static void aMobIsOnlyFoundAtTheInterval(GameTestHelper helper) {
    Player owner = GameTestRobots.onlinePlayer(helper);
    owner.moveTo(helper.absoluteVec(NEAR.getCenter()));
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    helper.spawnWithNoFreeWill(EntityType.ZOMBIE, NEAR);

    helper.runAfterDelay(
        MOB_SCAN_TICKS - NEXT_TICKS,
        () -> helper.assertFalse(robot.scanners().enemyInRange(), ENEMY));
    helper.runAfterDelay(
        AFTER_MOB_SCAN_TICKS,
        () -> {
          helper.assertTrue(robot.scanners().enemyInRange(), NO_ENEMY);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, batch = FILTER_BATCH)
  public static void aBlockInAnUnloadedChunkIsNotReadNorLoaded(GameTestHelper helper) {
    helper.assertTrue(RobotOreScan.kindsAt(helper.getLevel(), FAR_AWAY).isEmpty(), READ_UNLOADED);
    helper.assertFalse(helper.getLevel().hasChunkAt(FAR_AWAY), LOADED);
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = FILTER_BATCH)
  public static void savingKeepsTheSwitchAndTheFilter(GameTestHelper helper) {
    Player owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    GameTestRobots.clickRobot(owner, robot, new ItemStack(Items.COAL_ORE));
    robot.scanners().toggle();

    RobotEntity loaded = GameTestRobots.loadRobot(helper, robot.saveWithoutId(new CompoundTag()));

    helper.assertFalse(loaded.scanners().isOn(), WRONG_SWITCH);
    helper.assertValueEqual(loaded.scanners().oreFilter(), COAL_FILTER, WRONG_FILTER);
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = FILTER_BATCH)
  public static void anOldSaveHasTheScannersOnAndNoFilter(GameTestHelper helper) {
    RobotEntity robot = robot(helper);
    CompoundTag saved = robot.saveWithoutId(new CompoundTag());
    saved.remove(SCANNERS_KEY);
    saved.remove(ORE_FILTER_KEY);

    RobotEntity loaded = GameTestRobots.loadRobot(helper, saved);

    helper.assertTrue(loaded.scanners().isOn(), WRONG_SWITCH);
    helper.assertValueEqual(loaded.scanners().oreFilter(), OreFilter.EMPTY, WRONG_FILTER);
    helper.succeed();
  }

  @GameTest(template = ARENA, batch = FILTER_BATCH)
  public static void collectingKeepsTheSwitchAndTheFilter(GameTestHelper helper) {
    Player owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    GameTestRobots.clickRobot(owner, robot, new ItemStack(Items.COAL_ORE));
    robot.scanners().toggle();

    owner.setShiftKeyDown(true);
    GameTestRobots.clickRobot(owner, robot, ItemStack.EMPTY);
    owner.setShiftKeyDown(false);
    ItemStack item =
        owner.getInventory().items.stream()
            .filter(stack -> !stack.isEmpty())
            .findFirst()
            .orElseThrow();
    RobotEntity placed = GameTestRobots.placeRobot(helper, owner, item, SECOND_FLOOR);

    helper.assertFalse(placed.scanners().isOn(), WRONG_SWITCH);
    helper.assertValueEqual(placed.scanners().oreFilter(), COAL_FILTER, WRONG_FILTER);
    helper.succeed();
  }

  private static RobotEntity robot(GameTestHelper helper) {
    return GameTestRobots.placeNewRobot(helper, GameTestRobots.onlinePlayer(helper), FLOOR);
  }

  private static long charge(RobotEntity robot) {
    return robot.state().energy().amount();
  }

  private static RobotMenu menuOf(RobotEntity robot, Player player) {
    return RobotScreenReadout.serverMenu(0, player.getInventory(), robot);
  }
}
