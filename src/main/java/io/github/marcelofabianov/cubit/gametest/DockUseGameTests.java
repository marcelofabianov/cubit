package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.DockStatusProperty;
import io.github.marcelofabianov.cubit.block.InductionDockBlock;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The robot using the base: going there, hibernating, charging, leaving and waiting its turn. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DockUseGameTests {
  private static final String ARENA = "arena";
  private static final int ROBOT_X = 5;
  private static final int TIMEOUT = 400;
  private static final int SETTLE_TICKS = 5;
  private static final int CHARGE_TICKS = 20;
  private static final int HOLD_TICKS = 80;
  private static final float LOW_HEALTH = 5f;
  private static final BlockPos FLOOR = DockScene.FLOOR;
  private static final BlockPos DOCK = DockScene.DOCK;
  private static final BlockPos ROBOT_FLOOR =
      new BlockPos(ROBOT_X, DockScene.FLOOR_Y, DockScene.MIDDLE_Z);
  private static final Energy PLENTY = new Energy(100_000);
  private static final Energy STOCK = new Energy(50_000);
  private static final Energy LOW = new Energy(1_000);
  private static final Energy ALMOST_FULL = new Energy(299_500);
  private static final String WRONG_POWER = "Wrong power state";
  private static final String KEPT_LINK = "The robot in the base still follows";
  private static final String NOT_BACK = "The robot did not go back to following";
  private static final String LOST_ENERGY = "Energy was created or lost";
  private static final String NOT_HEALED = "The base did not heal the robot";
  private static final String NOT_MOVED = "The robot stayed on the plate";
  private static final String WRONG_ITEM = "The item should stay in the hand";
  private static final String NO_ACCESS = "The inventory should open in the base";
  private static final String NOT_CHARGED = "The robot did not charge";
  private static final String WRONG_POSITION = "The robot is not on the plate";
  private static final double ON_THE_PLATE = 0.6;

  private DockUseGameTests() {}

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void anEmptyRobotPlacedOnTheDockHibernatesAtOnce(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, FLOOR, Energy.ZERO);

    RobotEntity robot = DockScene.placeOnDock(helper, owner, Energy.ZERO);

    helper.assertValueEqual(robot.powerState(), PowerState.HIBERNATING, WRONG_POWER);
    helper.assertTrue(robot.docking().isDockedAt(helper.absolutePos(DOCK)), WRONG_POWER);
    helper.assertTrue(dock.isOccupied(), WRONG_POWER);
    helper.assertTrue(
        robot.position().distanceTo(helper.absoluteVec(Vec3.atBottomCenterOf(DOCK))) < ON_THE_PLATE,
        WRONG_POSITION);
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotEnteringTheBaseFreesTheFollowingPlace(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, FLOOR, STOCK);

    DockScene.placeOnDock(helper, owner, PLENTY);

    helper.assertValueEqual(
        owner.getData(CubitAttachments.FOLLOWING_ROBOT).robot(), Optional.empty(), KEPT_LINK);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aBusyDockKeepsTheItemInTheHand(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    DockScene.placeDock(helper, owner, FLOOR, STOCK);
    DockScene.placeOnDock(helper, owner, PLENTY);
    ItemStack second = DockScene.robotItem(PLENTY);

    DockScene.useOnDock(helper, owner, second);

    helper.assertFalse(second.isEmpty(), WRONG_ITEM);
    helper.assertValueEqual(GameTestRobots.robots(helper).size(), 1, WRONG_ITEM);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theDockOfSomeoneElseKeepsTheItemInTheHand(GameTestHelper helper) {
    DockScene.placeDock(helper, helper.makeMockPlayer(GameType.SURVIVAL), FLOOR, STOCK);
    ItemStack item = DockScene.robotItem(PLENTY);

    DockScene.useOnDock(helper, helper.makeMockPlayer(GameType.SURVIVAL), item);

    helper.assertFalse(item.isEmpty(), WRONG_ITEM);
    helper.assertTrue(GameTestRobots.robots(helper).isEmpty(), WRONG_ITEM);
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theBaseChargesWithoutCreatingOrLosingEnergy(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    InductionDockBlockEntity dock = DockScene.placeDock(helper, owner, FLOOR, STOCK);
    RobotEntity robot = DockScene.placeOnDock(helper, owner, LOW);
    long total = STOCK.amount() + LOW.amount();

    helper.runAfterDelay(
        CHARGE_TICKS,
        () -> {
          long charge = robot.state().energy().amount();
          helper.assertTrue(charge > LOW.amount(), NOT_CHARGED);
          helper.assertValueEqual(dock.energy().amount() + charge, total, LOST_ENERGY);
          helper.assertBlockProperty(DOCK, InductionDockBlock.STATUS, DockStatusProperty.CHARGING);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void anEmptyStockShowsNoEnergyAndTheRobotStays(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    DockScene.placeDock(helper, owner, FLOOR, Energy.ZERO);
    RobotEntity robot = DockScene.placeOnDock(helper, owner, LOW);

    helper.runAfterDelay(
        CHARGE_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.HIBERNATING, WRONG_POWER);
          helper.assertValueEqual(robot.state().energy(), LOW, LOST_ENERGY);
          helper.assertBlockProperty(DOCK, InductionDockBlock.STATUS, DockStatusProperty.NO_ENERGY);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theBaseHealsEvenWithoutEnergy(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    DockScene.placeDock(helper, owner, FLOOR, Energy.ZERO);
    RobotEntity robot = DockScene.placeOnDock(helper, owner, LOW);
    robot.setHealth(LOW_HEALTH);

    helper.runAfterDelay(
        CHARGE_TICKS,
        () -> {
          helper.assertTrue(robot.getHealth() > LOW_HEALTH, NOT_HEALED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aFullRobotShowsCompleteAndLeavesAfterAMoment(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, FLOOR, STOCK);
    RobotEntity robot = DockScene.placeOnDock(helper, owner, ALMOST_FULL);

    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.HIBERNATING, WRONG_POWER);
          helper.assertBlockProperty(DOCK, InductionDockBlock.STATUS, DockStatusProperty.COMPLETE);
        });
    helper.runAfterDelay(
        HOLD_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_POWER);
          helper.assertValueEqual(
              owner.getData(CubitAttachments.FOLLOWING_ROBOT).robot(),
              Optional.of(robot.getUUID()),
              NOT_BACK);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void clickingReleasesTheRobotInTheBase(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, FLOOR, Energy.ZERO);
    RobotEntity robot = DockScene.placeOnDock(helper, owner, PLENTY);
    Vec3 onPlate = robot.position();

    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          DockScene.click(helper, owner, DOCK);
          helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_POWER);
          helper.assertFalse(robot.position().equals(onPlate), NOT_MOVED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void leavingWithAnotherFollowingStaysSwitchedOff(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, FLOOR, Energy.ZERO);
    RobotEntity docked = DockScene.placeOnDock(helper, owner, PLENTY);
    RobotEntity follower = DockScene.placeRobot(helper, owner, ROBOT_FLOOR, PLENTY);
    helper.assertValueEqual(follower.powerState(), PowerState.ACTIVE, WRONG_POWER);

    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          DockScene.click(helper, owner, DOCK);
          helper.assertValueEqual(
              docked.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_POWER);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theInventoryOpensInTheBaseAndCollectingWorks(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, FLOOR, STOCK);
    RobotEntity robot = DockScene.placeOnDock(helper, owner, LOW);
    Vec3 near = robot.position();
    owner.moveTo(near.x, near.y, near.z);

    helper.assertTrue(robot.canUseInventory(owner), NO_ACCESS);
    owner.setShiftKeyDown(true);
    robot.interact(owner, InteractionHand.MAIN_HAND);

    helper.assertFalse(robot.isAlive(), NO_ACCESS);
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aHibernatingRobotCannotBePushed(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    DockScene.placeDock(helper, owner, FLOOR, Energy.ZERO);
    RobotEntity robot = DockScene.placeOnDock(helper, owner, LOW);

    helper.assertFalse(robot.isPushable(), WRONG_POSITION);
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void breakingTheDockReleasesTheRobot(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, FLOOR, STOCK);
    RobotEntity robot = DockScene.placeOnDock(helper, owner, LOW);

    helper.getLevel().destroyBlock(helper.absolutePos(DOCK), true);

    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_POWER);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void hibernationIsSavedAndALostDockReleasesTheRobot(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, FLOOR, STOCK);
    RobotEntity robot = DockScene.placeOnDock(helper, owner, LOW);
    CompoundTag saved = robot.saveWithoutId(new CompoundTag());

    RobotEntity loaded = CubitEntities.GENERAL_ROBOT.get().create(helper.getLevel());
    loaded.load(saved);

    helper.assertTrue(loaded.docking().isDockedAt(helper.absolutePos(DOCK)), WRONG_POWER);
    helper.assertValueEqual(loaded.powerState(), PowerState.HIBERNATING, WRONG_POWER);
    helper.succeed();
  }
}
