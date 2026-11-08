package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.behavior.RobotScreenStatus;
import io.github.marcelofabianov.cubit.domain.dock.DockInputMeter;
import io.github.marcelofabianov.cubit.domain.dock.DockStatus;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotScreenReadout;
import io.github.marcelofabianov.cubit.menu.InductionDockMenu;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The screens of the robot and the dock: who uses them, what they show and their buttons. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ScreenGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 400;
  private static final int WALK_TICKS = 120;
  private static final int CHARGE_TICKS = 20;
  private static final int METER_TICKS = 3;
  private static final int ROBOT_X = 5;
  private static final int SECOND_Z = 4;
  private static final BlockPos FLOOR = DockScene.FLOOR;
  private static final BlockPos DOCK = DockScene.DOCK;
  private static final BlockPos ROBOT_FLOOR =
      new BlockPos(ROBOT_X, DockScene.FLOOR_Y, DockScene.MIDDLE_Z);
  private static final BlockPos SECOND_FLOOR = new BlockPos(ROBOT_X, DockScene.FLOOR_Y, SECOND_Z);
  private static final Energy PLENTY = new Energy(100_000);
  private static final Energy STOCK = new Energy(50_000);
  private static final Energy LOW = new Energy(1_000);
  private static final int BURST = 2_400;
  private static final int CONTAINER_ID = 1;
  private static final int SOME_IRON = 5;
  private static final int FIRST_PLAYER_ITEM = 9;
  private static final int ROBOT_SLOTS = 27;
  private static final int ENERGY_SLOTS = 1;
  private static final String NOT_CALLED = "The button did not call the robot";
  private static final String CALLED = "A stranger called the robot";
  private static final String WRONG_DOCK = "The dock screen shows the wrong values";
  private static final String WRONG_ROBOT = "The robot screen shows the wrong values";
  private static final String STILL_OPEN = "The screen stayed open with the robot off";
  private static final String DUPLICATED = "Moving items created or lost some";

  private ScreenGameTests() {}

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theCallButtonCallsTheNearestRobot(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, FLOOR, STOCK);
    RobotEntity robot = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, PLENTY);
    InductionDockMenu menu = InductionDockMenu.onServer(CONTAINER_ID, owner.getInventory(), dock);

    helper.assertTrue(menu.clickMenuButton(owner, InductionDockMenu.CALL_BUTTON), NOT_CALLED);

    helper.runAfterDelay(
        WALK_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.HIBERNATING, NOT_CALLED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void aStrangerCannotPressTheCallButton(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    ServerPlayer stranger = DockScene.ownerBeside(helper);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, FLOOR, STOCK);
    RobotEntity robot = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, PLENTY);
    InductionDockMenu menu = InductionDockMenu.onServer(CONTAINER_ID, owner.getInventory(), dock);

    helper.assertFalse(menu.clickMenuButton(stranger, InductionDockMenu.CALL_BUTTON), CALLED);
    helper.assertTrue(robot.answersDockCall(), CALLED);
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theDockScreenShowsTheRobotInTheBase(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, FLOOR, STOCK);
    RobotEntity robot = DockScene.placeOnDock(helper, owner, LOW);
    InductionDockMenu menu = InductionDockMenu.onServer(CONTAINER_ID, owner.getInventory(), dock);

    helper.runAfterDelay(
        CHARGE_TICKS,
        () -> {
          menu.broadcastChanges();
          helper.assertTrue(menu.hasRobot(), WRONG_DOCK);
          helper.assertValueEqual(menu.status(), DockStatus.CHARGING, WRONG_DOCK);
          helper.assertValueEqual(menu.stock(), dock.energy(), WRONG_DOCK);
          helper.assertValueEqual(menu.robotCharge(), robot.state().energy(), WRONG_DOCK);
          helper.assertValueEqual(menu.robotTier(), robot.state().tier(), WRONG_DOCK);
          helper.succeed();
        });
  }

  /** A burst counts over the whole second, so the screen shows its average per tick. */
  @GameTest(template = ARENA)
  public static void theDockScreenShowsTheAverageInput(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, FLOOR, Energy.ZERO);
    InductionDockMenu menu = InductionDockMenu.onServer(CONTAINER_ID, owner.getInventory(), dock);

    dock.energyStorage().receiveEnergy(BURST, false);

    helper.runAfterDelay(
        METER_TICKS,
        () -> {
          menu.broadcastChanges();
          Energy average = new Energy(BURST / DockInputMeter.TICKS_PER_SECOND);
          helper.assertValueEqual(menu.input(), average, WRONG_DOCK);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void theRobotScreenShowsTheRobotThatFollows(GameTestHelper helper) {
    ServerPlayer owner = playerNear(helper);
    RobotEntity robot = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, PLENTY);
    RobotMenu menu = RobotScreenReadout.serverMenu(CONTAINER_ID, owner.getInventory(), robot);

    menu.broadcastChanges();

    helper.assertValueEqual(menu.status(), RobotScreenStatus.FOLLOWING, WRONG_ROBOT);
    helper.assertTrue(menu.following(), WRONG_ROBOT);
    helper.assertValueEqual(menu.charge(), robot.state().energy(), WRONG_ROBOT);
    helper.succeed();
  }

  /** The game closes a screen whose menu stops being valid (answer 684). */
  @GameTest(template = ARENA)
  public static void theRobotScreenClosesWhenTheRobotSwitchesOff(GameTestHelper helper) {
    ServerPlayer owner = playerNear(helper);
    RobotEntity first = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, PLENTY);
    RobotMenu menu = RobotScreenReadout.serverMenu(CONTAINER_ID, owner.getInventory(), first);
    helper.assertTrue(menu.stillValid(owner), STILL_OPEN);
    RobotEntity second = DockScene.placeRobot(helper, owner, SECOND_FLOOR, PLENTY);

    second.interact(owner, InteractionHand.MAIN_HAND);

    helper.assertValueEqual(first.powerState(), PowerState.INACTIVE_SWITCHED_OFF, STILL_OPEN);
    helper.assertFalse(menu.stillValid(owner), STILL_OPEN);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void movingBetweenTheInventoriesNeverDuplicates(GameTestHelper helper) {
    ServerPlayer owner = playerNear(helper);
    RobotEntity robot = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, PLENTY);
    RobotMenu menu = RobotScreenReadout.serverMenu(CONTAINER_ID, owner.getInventory(), robot);
    owner.getInventory().setItem(FIRST_PLAYER_ITEM, new ItemStack(Items.IRON_INGOT, SOME_IRON));

    menu.quickMoveStack(owner, ROBOT_SLOTS + ENERGY_SLOTS);

    helper.assertValueEqual(robot.inventory().countItem(Items.IRON_INGOT), SOME_IRON, DUPLICATED);
    helper.assertValueEqual(owner.getInventory().countItem(Items.IRON_INGOT), 0, DUPLICATED);
    helper.succeed();
  }

  private static ServerPlayer playerNear(GameTestHelper helper) {
    ServerPlayer player = GameTestRobots.onlinePlayer(helper);
    Vec3 position = helper.absoluteVec(Vec3.atBottomCenterOf(ROBOT_FLOOR.above()));
    player.moveTo(position.x, position.y, position.z);
    return player;
  }
}
