package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.RobotName;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotScreenReadout;
import io.github.marcelofabianov.cubit.entity.RobotTablet;
import io.github.marcelofabianov.cubit.menu.TabletRobotMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The screen of a Cubit on the Tablet: at any distance, only for the owner, with the inventory only
 * to read, the Power of the header and Rename (answers 999, 1015 to 1018 and 1033).
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class TabletScreenGameTests {
  private static final String ARENA = "arena";
  private static final BlockPos FLOOR = new BlockPos(1, 1, 1);
  private static final double FAR_AWAY = 200;
  private static final int FIRST_SLOT = 0;
  private static final int LEFT_BUTTON = 0;
  private static final String NAME = "Bolt";
  private static final String PADDED_NAME = "  Bolt  ";
  private static final String NO_NAME = "";
  private static final String LETTER = "a";
  private static final String WRONG_OPEN = "The screen of the Tablet opened for the wrong player";
  private static final String MOVED = "The Tablet moved an item of the Cubit";
  private static final String WRONG_STATE = "The Power of the Tablet left the wrong state";
  private static final String WRONG_NAME = "The Tablet gave the wrong name";

  private TabletScreenGameTests() {}

  /** The Tablet reads the Cubit at any distance, and never for someone else (answer 1018). */
  @GameTest(template = ARENA)
  public static void theScreenStaysOpenFarAwayOnlyForTheOwner(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    ServerPlayer stranger = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    TabletRobotMenu menu = RobotScreenReadout.tabletMenu(0, owner.getInventory(), robot);

    owner.moveTo(robot.getX() + FAR_AWAY, robot.getY(), robot.getZ());

    helper.assertTrue(menu.stillValid(owner), WRONG_OPEN);
    helper.assertFalse(menu.stillValid(stranger), WRONG_OPEN);
    helper.succeed();
  }

  /** No click and no shift click moves an item in or out of the Cubit (answer 999). */
  @GameTest(template = ARENA)
  public static void theInventoryOfTheCubitIsOnlyRead(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    robot.inventory().setItem(FIRST_SLOT, new ItemStack(Items.COBBLESTONE));
    TabletRobotMenu menu = RobotScreenReadout.tabletMenu(0, owner.getInventory(), robot);

    menu.clicked(FIRST_SLOT, LEFT_BUTTON, ClickType.PICKUP, owner);
    menu.clicked(FIRST_SLOT, LEFT_BUTTON, ClickType.QUICK_MOVE, owner);
    menu.clicked(FIRST_SLOT, LEFT_BUTTON, ClickType.THROW, owner);

    helper.assertTrue(robot.inventory().getItem(FIRST_SLOT).is(Items.COBBLESTONE), MOVED);
    helper.assertTrue(menu.getCarried().isEmpty(), MOVED);
    helper.assertTrue(menu.quickMoveStack(owner, FIRST_SLOT).isEmpty(), MOVED);
    helper.succeed();
  }

  /** The Power of the header switches off an active Cubit and turns it on again (1033). */
  @GameTest(template = ARENA)
  public static void powerSwitchesTheCubitOffAndOn(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_STATE);
    TabletRobotMenu menu = RobotScreenReadout.tabletMenu(0, owner.getInventory(), robot);

    menu.clickMenuButton(owner, TabletRobotMenu.POWER_TOGGLE_BUTTON);
    helper.assertValueEqual(robot.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_STATE);
    menu.clickMenuButton(owner, TabletRobotMenu.POWER_TOGGLE_BUTTON);

    helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_STATE);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aStrangerCannotUsePower(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    ServerPlayer stranger = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    TabletRobotMenu menu = RobotScreenReadout.tabletMenu(0, stranger.getInventory(), robot);

    menu.clickMenuButton(stranger, TabletRobotMenu.POWER_TOGGLE_BUTTON);

    helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_STATE);
    helper.succeed();
  }

  /** The spaces at the ends are cut, and an empty name removes the name (1015 and 1017). */
  @GameTest(template = ARENA)
  public static void renameNamesAndAnEmptyNameRemovesIt(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    RobotTablet.rename(owner, robot.getUUID(), PADDED_NAME);
    helper.assertValueEqual(robot.getCustomName().getString(), NAME, WRONG_NAME);
    RobotTablet.rename(owner, robot.getUUID(), NO_NAME);

    helper.assertFalse(robot.hasCustomName(), WRONG_NAME);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aLongNameAndAStrangerAreRefused(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    ServerPlayer stranger = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    RobotTablet.rename(owner, robot.getUUID(), NAME);

    boolean tooLong =
        RobotTablet.rename(owner, robot.getUUID(), LETTER.repeat(RobotName.MAX_LENGTH + 1));
    boolean byStranger = RobotTablet.rename(stranger, robot.getUUID(), NO_NAME);

    helper.assertFalse(tooLong || byStranger, WRONG_NAME);
    helper.assertValueEqual(robot.getCustomName().getString(), NAME, WRONG_NAME);
    helper.succeed();
  }
}
