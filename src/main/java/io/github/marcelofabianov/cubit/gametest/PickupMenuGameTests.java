package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.OreFilter;
import io.github.marcelofabianov.cubit.domain.robot.PickupEntry;
import io.github.marcelofabianov.cubit.domain.robot.PickupMode;
import io.github.marcelofabianov.cubit.domain.robot.PickupSettings;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotScreenReadout;
import io.github.marcelofabianov.cubit.menu.PickupControl.PickupButton;
import io.github.marcelofabianov.cubit.menu.PickupDisplay;
import io.github.marcelofabianov.cubit.menu.PickupScreenValues;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The behavior tab of the robot menu: switches, mode and the ghost slots (answers 878 to 895). */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class PickupMenuGameTests {
  private static final String EMPTY = "empty";
  private static final String WRONG_SETTINGS = "Wrong pickup settings";
  private static final String WRONG_ANSWER = "Wrong answer to the button";
  private static final String CONSUMED = "The ghost slot must not take the item";
  private static final String WRONG_SLOTS = "Wrong number of filter slots";
  private static final BlockPos FLOOR = new BlockPos(1, 1, 1);
  private static final float HEALTH = 20f;
  private static final Energy CHARGE = new Energy(100_000);
  private static final int LEFT_CLICK = 0;
  private static final int RIGHT_CLICK = 1;
  private static final int FIRST_FILTER_SLOT = 0;
  private static final int BASE_FILTER_SLOTS = 3;
  private static final int STACK = 16;

  private PickupMenuGameTests() {}

  @GameTest(template = EMPTY)
  public static void theOwnerTogglesPickupAndTheMode(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeNear(helper, owner, RobotTier.BASE);
    RobotMenu menu = RobotScreenReadout.serverMenu(0, owner.getInventory(), robot);

    helper.assertTrue(menu.clickMenuButton(owner, RobotMenu.PICKUP_BUTTON), WRONG_ANSWER);
    helper.assertTrue(menu.clickMenuButton(owner, RobotMenu.PICKUP_MODE_BUTTON), WRONG_ANSWER);

    PickupSettings settings = robot.pickup().settings();
    helper.assertFalse(settings.pickupOn(), WRONG_SETTINGS);
    helper.assertValueEqual(settings.filter().mode(), PickupMode.WHITELIST, WRONG_SETTINGS);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void baseRefusesTheMagnet(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeNear(helper, owner, RobotTier.BASE);
    RobotMenu menu = RobotScreenReadout.serverMenu(0, owner.getInventory(), robot);

    helper.assertFalse(menu.clickMenuButton(owner, RobotMenu.MAGNET_BUTTON), WRONG_ANSWER);
    helper.assertTrue(robot.pickup().settings().magnetOn(), WRONG_SETTINGS);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void withPickupOffTheMagnetCannotChange(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeNear(helper, owner, RobotTier.ADVANCED);
    robot.pickup().press(owner, PickupButton.PICKUP);

    helper.assertFalse(robot.pickup().press(owner, PickupButton.MAGNET), WRONG_ANSWER);
    robot.pickup().press(owner, PickupButton.PICKUP);
    helper.assertTrue(robot.pickup().press(owner, PickupButton.MAGNET), WRONG_ANSWER);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void theFetchButtonTogglesFetch(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeNear(helper, owner, RobotTier.BASE);
    RobotMenu menu = RobotScreenReadout.serverMenu(0, owner.getInventory(), robot);

    helper.assertTrue(menu.clickMenuButton(owner, RobotMenu.FETCH_BUTTON), WRONG_ANSWER);
    helper.assertFalse(robot.pickup().settings().fetchOn(), WRONG_SETTINGS);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void withPickupOffFetchCannotChange(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeNear(helper, owner, RobotTier.BASE);
    robot.pickup().press(owner, PickupButton.PICKUP);

    helper.assertFalse(robot.pickup().press(owner, PickupButton.FETCH), WRONG_ANSWER);
    robot.pickup().press(owner, PickupButton.PICKUP);
    helper.assertTrue(robot.pickup().press(owner, PickupButton.FETCH), WRONG_ANSWER);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void onlyTheOwnerChangesThePickup(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeNear(helper, owner, RobotTier.ELITE);
    Player stranger = helper.makeMockPlayer(GameType.SURVIVAL);

    helper.assertFalse(robot.pickup().press(stranger, PickupButton.PICKUP), WRONG_ANSWER);
    helper.assertFalse(
        robot.pickup().place(stranger, FIRST_FILTER_SLOT, new ItemStack(Items.DIRT)), WRONG_ANSWER);
    helper.assertValueEqual(robot.pickup().settings(), PickupSettings.INITIAL, WRONG_SETTINGS);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void aGhostSlotCopiesTheItemAndAnEmptyCursorClearsIt(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeNear(helper, owner, RobotTier.BASE);
    RobotMenu menu = RobotScreenReadout.serverMenu(0, owner.getInventory(), robot);
    int ghost = menu.filterFirstSlot();
    menu.setCarried(new ItemStack(Items.DIRT, STACK));

    menu.clicked(ghost, LEFT_CLICK, ClickType.PICKUP, owner);

    helper.assertValueEqual(menu.getCarried().getCount(), STACK, CONSUMED);
    helper.assertValueEqual(
        robot.pickup().settings().filter().entries(),
        List.of(PickupEntry.item(PickupDisplay.idOf(new ItemStack(Items.DIRT)))),
        WRONG_SETTINGS);
    menu.setCarried(ItemStack.EMPTY);
    menu.clicked(ghost, LEFT_CLICK, ClickType.PICKUP, owner);
    helper.assertTrue(robot.pickup().settings().filter().entries().isEmpty(), WRONG_SETTINGS);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void aRightClickTurnsTheSlotIntoATagOnlyOnElite(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity base = placeNear(helper, owner, RobotTier.BASE);
    RobotEntity elite = placeNear(helper, owner, RobotTier.ELITE);
    for (RobotEntity robot : List.of(base, elite)) {
      robot.pickup().place(owner, FIRST_FILTER_SLOT, new ItemStack(Items.OAK_LOG));
      robot.pickup().cycle(owner, FIRST_FILTER_SLOT);
    }

    helper.assertFalse(
        base.pickup().settings().filter().entries().getFirst().tag(), WRONG_SETTINGS);
    helper.assertTrue(
        elite.pickup().settings().filter().entries().getFirst().tag(), WRONG_SETTINGS);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void theMenuSendsTheSlotsOfTheTier(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeNear(helper, owner, RobotTier.BASE);
    RobotMenu menu = RobotScreenReadout.serverMenu(0, owner.getInventory(), robot);

    menu.broadcastChanges();

    helper.assertValueEqual(
        PickupScreenValues.filterSlots(menu.pickupValues()), BASE_FILTER_SLOTS, WRONG_SLOTS);
    helper.assertFalse(PickupScreenValues.magnetAvailable(menu.pickupValues()), WRONG_SLOTS);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void shiftClickNeverFillsTheFilter(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeNear(helper, owner, RobotTier.BASE);
    robot.pickup().place(owner, FIRST_FILTER_SLOT, new ItemStack(Items.DIRT));
    RobotMenu menu = RobotScreenReadout.serverMenu(0, owner.getInventory(), robot);
    menu.broadcastChanges();
    int firstRobotSlot = 0;
    robot.inventory().setItem(firstRobotSlot, new ItemStack(Items.DIRT, STACK));

    menu.quickMoveStack(owner, firstRobotSlot);

    helper.assertValueEqual(
        menu.slots.get(menu.filterFirstSlot()).getItem().getCount(), 1, CONSUMED);
    helper.succeed();
  }

  private static RobotEntity placeNear(GameTestHelper helper, ServerPlayer owner, RobotTier tier) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(
        CubitDataComponents.ROBOT_STATE.get(),
        new RobotState(
            tier, CHARGE, HEALTH, false, RobotColor.WHITE, false, OreFilter.EMPTY, false));
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item, FLOOR);
    owner.moveTo(robot.getX(), robot.getY(), robot.getZ());
    return robot;
  }
}
