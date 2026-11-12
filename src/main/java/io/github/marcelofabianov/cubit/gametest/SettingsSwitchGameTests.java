package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotScreenReadout;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The solar switch and the Power button of the settings tab (answers 857 to 861). */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class SettingsSwitchGameTests {
  private static final String EMPTY = "empty";
  private static final String WRONG_SWITCH = "Wrong state of the solar switch";
  private static final String WRONG_STATE = "Wrong power state";
  private static final String STILL_FOLLOWING = "The robot should free the place to follow";
  private static final String WRONG_ANSWER = "Wrong answer to the Power button";
  private static final BlockPos FLOOR = new BlockPos(1, 1, 1);
  private static final BlockPos SECOND_FLOOR = new BlockPos(2, 1, 1);

  private SettingsSwitchGameTests() {}

  @GameTest(template = EMPTY)
  public static void theSolarSwitchStartsOnAndTheItemKeepsIt(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    helper.assertTrue(robot.solar().isOn(), WRONG_SWITCH);

    robot.solar().toggle();
    ItemStack collected = collect(owner, robot);

    RobotState saved = collected.get(CubitDataComponents.ROBOT_STATE.get());
    helper.assertFalse(saved.solarOn(), WRONG_SWITCH);
    RobotEntity again = GameTestRobots.placeRobot(helper, owner, collected, FLOOR);
    helper.assertFalse(again.solar().isOn(), WRONG_SWITCH);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void savingKeepsTheSolarSwitch(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    robot.solar().toggle();

    RobotEntity loaded = GameTestRobots.loadRobot(helper, robot.saveWithoutId(new CompoundTag()));

    helper.assertFalse(loaded.solar().isOn(), WRONG_SWITCH);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void theSolarButtonTogglesThePanel(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeNear(helper, owner, FLOOR);
    RobotMenu menu = RobotScreenReadout.serverMenu(0, owner.getInventory(), robot);

    menu.clickMenuButton(owner, RobotMenu.SOLAR_BUTTON);

    helper.assertFalse(robot.solar().isOn(), WRONG_SWITCH);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void powerSwitchesOffAndFreesThePlaceToFollow(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeNear(helper, owner, FLOOR);
    helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_STATE);
    RobotMenu menu = RobotScreenReadout.serverMenu(0, owner.getInventory(), robot);

    boolean pressed = menu.clickMenuButton(owner, RobotMenu.POWER_BUTTON);

    helper.assertTrue(pressed, WRONG_ANSWER);
    helper.assertValueEqual(robot.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_STATE);
    helper.assertTrue(
        owner.getData(CubitAttachments.FOLLOWING_ROBOT).robot().isEmpty(), STILL_FOLLOWING);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void aRobotSwitchedOffByPowerTurnsOnWithAClick(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeNear(helper, owner, FLOOR);
    RobotScreenReadout.serverMenu(0, owner.getInventory(), robot)
        .clickMenuButton(owner, RobotMenu.POWER_BUTTON);

    GameTestRobots.clickRobot(owner, robot, ItemStack.EMPTY);

    helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_STATE);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void powerRefusesARobotThatIsNotActive(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    placeNear(helper, owner, FLOOR);
    RobotEntity second = placeNear(helper, owner, SECOND_FLOOR);
    helper.assertValueEqual(second.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_STATE);
    RobotMenu menu = RobotScreenReadout.serverMenu(0, owner.getInventory(), second);

    boolean pressed = menu.clickMenuButton(owner, RobotMenu.POWER_BUTTON);

    helper.assertFalse(pressed, WRONG_ANSWER);
    helper.assertFalse(menu.powerAvailable(), WRONG_ANSWER);
    helper.succeed();
  }

  private static RobotEntity placeNear(GameTestHelper helper, ServerPlayer owner, BlockPos floor) {
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, floor);
    owner.moveTo(robot.getX(), robot.getY(), robot.getZ());
    return robot;
  }

  private static ItemStack collect(Player owner, RobotEntity robot) {
    owner.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    owner.setShiftKeyDown(true);
    robot.interact(owner, InteractionHand.MAIN_HAND);
    owner.setShiftKeyDown(false);
    return owner.getInventory().getItem(0);
  }
}
