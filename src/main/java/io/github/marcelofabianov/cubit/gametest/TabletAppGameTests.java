package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.dock.DockGoal;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.robot.TabletApp;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.item.TabletItem;
import io.github.marcelofabianov.cubit.player.TabletSelection;
import io.github.marcelofabianov.cubit.player.TabletUse;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The apps Target and Dock of the Tablet, the switch of the app and Go to Dock (answers 1041 to
 * 1047).
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class TabletAppGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 300;
  private static final int FLOOR_Y = 1;
  private static final int MIDDLE_Z = 2;
  private static final BlockPos ROBOT_FLOOR = new BlockPos(4, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos NEAR_DOCK_FLOOR = new BlockPos(8, FLOOR_Y, 0);
  private static final BlockPos MOB_SPOT = new BlockPos(6, 2, MIDDLE_Z);
  private static final Energy CHARGE = new Energy(100_000);
  private static final Energy DOCK_ENERGY = Energy.ZERO;
  private static final float HEALTH = 25f;
  private static final int FORWARD = 1;
  private static final String NOT_MARKED = "The Dock app did not mark the Dock";
  private static final String STILL_MARKED = "The Dock app did not remove the mark";
  private static final String WRONG_GOAL = "Go to Dock gave the wrong goal";
  private static final String NOT_DOCKED = "The Cubit did not go to its preferred Dock";
  private static final String WRONG_TARGET = "The Target app marked the wrong mob";
  private static final String WRONG_APP = "The Tablet has the wrong app";
  private static final String NOT_SAVED = "The preferred Dock was not saved";

  private TabletAppGameTests() {}

  @GameTest(template = ARENA)
  public static void theDockAppMarksAndUnmarksADockOfThePlayer(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.BASE);
    InductionDockBlockEntity dock =
        DockScene.placeDock(helper, owner, DockScene.FLOOR, DOCK_ENERGY);
    ItemStack tablet = tablet(TabletApp.DOCK);

    TabletUse.click(owner, tablet, new TabletUse.Block(Optional.of(dock)));
    helper.assertTrue(robot.docking().preferred().marked().isPresent(), NOT_MARKED);
    TabletUse.click(owner, tablet, new TabletUse.Block(Optional.of(dock)));

    helper.assertTrue(robot.docking().preferred().marked().isEmpty(), STILL_MARKED);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theDockAppIgnoresADockOfSomeoneElse(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    ServerPlayer stranger = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.BASE);
    InductionDockBlockEntity dock =
        DockScene.placeDock(helper, stranger, DockScene.FLOOR, DOCK_ENERGY);

    TabletUse.click(owner, tablet(TabletApp.DOCK), new TabletUse.Block(Optional.of(dock)));

    helper.assertTrue(robot.docking().preferred().marked().isEmpty(), STILL_MARKED);
    helper.succeed();
  }

  /** The marked Dock wins over a nearer free one (answer 1041). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void goToDockSendsTheCubitToItsPreferredDock(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.BASE);
    InductionDockBlockEntity preferred =
        DockScene.placeDock(helper, owner, DockScene.FLOOR, DOCK_ENERGY);
    DockScene.placeDock(helper, owner, NEAR_DOCK_FLOOR, DOCK_ENERGY);
    robot.docking().preferred().click(owner, preferred);

    helper.assertValueEqual(robot.docking().preferred().goal(), DockGoal.READY, WRONG_GOAL);
    helper.assertTrue(robot.docking().preferred().go(owner), WRONG_GOAL);

    helper.succeedWhen(() ->
        helper.assertTrue(
            robot.docking().isDockedAt(helper.absolutePos(DockScene.DOCK)), NOT_DOCKED));
  }

  @GameTest(template = ARENA)
  public static void withoutADockInReachGoToDockSaysSo(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.BASE);

    helper.assertValueEqual(robot.docking().preferred().goal(), DockGoal.NO_DOCK, WRONG_GOAL);
    helper.assertFalse(robot.docking().preferred().go(owner), WRONG_GOAL);
    helper.succeed();
  }

  /** A marked Dock that is gone is forgotten once its chunk shows it (answer 1042). */
  @GameTest(template = ARENA)
  public static void aDestroyedPreferredDockIsForgotten(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.BASE);
    InductionDockBlockEntity dock =
        DockScene.placeDock(helper, owner, DockScene.FLOOR, DOCK_ENERGY);
    robot.docking().preferred().click(owner, dock);

    helper.setBlock(DockScene.DOCK.above(), Blocks.AIR);
    helper.setBlock(DockScene.DOCK, Blocks.AIR);
    robot.docking().preferred().go(owner);

    helper.assertTrue(robot.docking().preferred().marked().isEmpty(), STILL_MARKED);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void thePreferredDockIsSaved(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.BASE);
    InductionDockBlockEntity dock =
        DockScene.placeDock(helper, owner, DockScene.FLOOR, DOCK_ENERGY);
    robot.docking().preferred().click(owner, dock);

    RobotEntity loaded = GameTestRobots.loadRobot(helper, robot.saveWithoutId(new CompoundTag()));

    helper.assertValueEqual(
        loaded.docking().preferred().marked(), robot.docking().preferred().marked(), NOT_SAVED);
    helper.succeed();
  }

  /** The clicked mob becomes the target of Focus, by the rules of a hurt mob (answer 1043). */
  @GameTest(template = ARENA)
  public static void theTargetAppMarksTheMobForFocus(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.ADVANCED);
    robot.defense().choose(owner, DefenseMode.FOCUS);
    Mob mob = helper.spawn(EntityType.HUSK, MOB_SPOT);
    mob.setNoAi(true);

    TabletUse.click(owner, tablet(TabletApp.TARGET), new TabletUse.Living(mob));

    helper.assertTrue(robot.defense().focusTarget().is(mob.getUUID()), WRONG_TARGET);
    helper.succeed();
  }

  /** Out of Focus, the Target app goes back to Open on the click (answer 1044). */
  @GameTest(template = ARENA)
  public static void theTargetAppOutOfFocusGoesBackToOpen(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    chosenRobot(helper, owner, RobotTier.ADVANCED);
    ItemStack tablet = tablet(TabletApp.TARGET);

    TabletUse.click(owner, tablet, new TabletUse.Air());

    helper.assertValueEqual(TabletItem.appOf(tablet), TabletApp.OPEN, WRONG_APP);
    helper.succeed();
  }

  /** Shift and the wheel skip Target while the chosen Cubit is not in Focus. */
  @GameTest(template = ARENA)
  public static void theSwitchGoesOnlyThroughTheAppsThatMakeSense(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.ADVANCED);
    ItemStack tablet = tablet(TabletApp.OPEN);
    owner.setItemInHand(InteractionHand.MAIN_HAND, tablet);

    TabletUse.switchApp(owner, FORWARD);
    helper.assertValueEqual(TabletItem.appOf(tablet), TabletApp.DOCK, WRONG_APP);
    robot.defense().choose(owner, DefenseMode.FOCUS);
    TabletUse.switchApp(owner, FORWARD);
    TabletUse.switchApp(owner, FORWARD);

    helper.assertValueEqual(TabletItem.appOf(tablet), TabletApp.TARGET, WRONG_APP);
    helper.succeed();
  }

  private static RobotEntity chosenRobot(
      GameTestHelper helper, ServerPlayer owner, RobotTier tier) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(CubitDataComponents.ROBOT_STATE.get(), new RobotState(tier, CHARGE, HEALTH));
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item, ROBOT_FLOOR);
    TabletSelection.choose(owner, robot.getUUID());
    return robot;
  }

  private static ItemStack tablet(TabletApp app) {
    ItemStack tablet = new ItemStack(CubitItems.TABLET.get());
    TabletItem.setApp(tablet, app);
    return tablet;
  }
}
