package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.behavior.RobotNotice;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotNotices;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The auto return: the Cubit that follows goes to a Dock with a low battery, to the same Dock as
 * `Go to Dock`, and the switch keeps it from going (answers 1065 to 1068).
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class AutoReturnGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 400;
  private static final int WALK_TICKS = 160;
  private static final int SHORT_TICKS = 20;
  private static final int ROBOT_X = 5;
  private static final int NEARER_DOCK_X = 8;
  private static final BlockPos ROBOT_FLOOR =
      new BlockPos(ROBOT_X, DockScene.FLOOR_Y, DockScene.MIDDLE_Z);
  private static final BlockPos NEARER_DOCK_FLOOR =
      new BlockPos(NEARER_DOCK_X, DockScene.FLOOR_Y, 0);

  /** Below the 10% of the return and above the 5% that would switch the Base off. */
  private static final Energy LOW = new Energy(24_000);

  private static final Energy PLENTY = new Energy(100_000);
  private static final Energy STOCK = new Energy(50_000);
  private static final String NOT_DOCKED = "The Cubit did not go back to the Dock";
  private static final String DOCKED = "The Cubit went to the Dock without a reason";
  private static final String NOT_SAID = "The Cubit did not say it needs to recharge";
  private static final String NOT_KEPT = "The auto return switch was not kept";

  private AutoReturnGameTests() {}

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aFollowingCubitWithALowBatteryGoesBackToTheDock(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, DockScene.FLOOR, STOCK);
    RobotEntity robot = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, LOW);

    helper.runAfterDelay(
        SHORT_TICKS,
        () ->
            helper.assertTrue(RobotNotices.isHolding(robot, RobotNotice.NEED_RECHARGE), NOT_SAID));
    helper.runAfterDelay(WALK_TICKS, () -> assertDocked(helper, robot, DockScene.DOCK));
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theSwitchOffKeepsTheCubitFollowing(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, DockScene.FLOOR, STOCK);
    RobotEntity robot = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, LOW);
    robot.docking().autoReturn().toggle();

    helper.runAfterDelay(WALK_TICKS, () -> assertStillOut(helper, robot));
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aboveTheReturnChargeTheCubitKeepsFollowing(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, DockScene.FLOOR, STOCK);
    RobotEntity robot = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, PLENTY);

    helper.runAfterDelay(WALK_TICKS, () -> assertStillOut(helper, robot));
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void withoutADockTheCubitTriesAgainLater(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    RobotEntity robot = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, LOW);

    helper.runAfterDelay(
        SHORT_TICKS, () -> DockScene.placeDock(helper, owner, DockScene.FLOOR, STOCK));
    helper.runAfterDelay(WALK_TICKS, () -> assertDocked(helper, robot, DockScene.DOCK));
  }

  /** The other Dock is nearer, so only the mark of the Tablet sends the Cubit to the far one. */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theCubitGoesBackToItsPreferredDock(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, NEARER_DOCK_FLOOR, STOCK);
    InductionDockBlockEntity preferred = DockScene.placeDock(helper, owner, DockScene.FLOOR, STOCK);
    RobotEntity robot = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, LOW);
    robot.docking().preferred().click(owner, preferred);

    helper.runAfterDelay(WALK_TICKS, () -> assertDocked(helper, robot, DockScene.DOCK));
  }

  @GameTest(template = ARENA)
  public static void theSwitchIsKeptInTheItemAndInTheSave(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, PLENTY);
    robot.docking().autoReturn().toggle();

    CompoundTag saved = robot.saveWithoutId(new CompoundTag());
    RobotEntity loaded = GameTestRobots.loadRobot(helper, saved);
    helper.assertFalse(loaded.docking().autoReturn().isOn(), NOT_KEPT);
    ItemStack item = GameTestRobots.collect(owner, robot);

    helper.assertFalse(item.get(CubitDataComponents.ROBOT_STATE.get()).autoReturnOn(), NOT_KEPT);
    helper.succeed();
  }

  private static void assertDocked(GameTestHelper helper, RobotEntity robot, BlockPos dock) {
    helper.assertValueEqual(robot.powerState(), PowerState.HIBERNATING, NOT_DOCKED);
    helper.assertTrue(robot.docking().isDockedAt(helper.absolutePos(dock)), NOT_DOCKED);
    helper.succeed();
  }

  private static void assertStillOut(GameTestHelper helper, RobotEntity robot) {
    helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, DOCKED);
    helper.succeed();
  }
}
