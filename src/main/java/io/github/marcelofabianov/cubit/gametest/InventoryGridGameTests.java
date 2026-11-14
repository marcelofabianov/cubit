package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotScreenReadout;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The whole inventory of the robot shows at once, in three rows, with more columns in the bigger
 * tiers, and the screen only grows sideways (answer 1101). The menu is opened as the server opens
 * it for the owner.
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class InventoryGridGameTests {
  private static final String EMPTY = "empty";
  private static final String WRONG_GRID = "Wrong columns or rows on the screen";
  private static final String WRONG_ITEM = "The slot shows the wrong item";
  private static final String NOT_LEFT = "The grid does not start at the left side";
  private static final String NOT_STORED = "The item should be in the last part of the inventory";
  private static final String MOVED = "The item should have left the player";
  private static final String NOT_IN_ROW = "The energy slot is not first in the exclusive row";

  private static final float HEALTH = 25f;
  private static final Energy CHARGE = new Energy(100_000);
  private static final int BASE_COLUMNS = 9;
  private static final int ULTIMATE_COLUMNS = 18;
  private static final int ULTIMATE_SLOTS = 54;
  private static final int THREE_ROWS = 3;
  private static final int LAST_SLOT = ULTIMATE_SLOTS - 1;
  private static final int BASE_SLOTS = 27;
  private static final int ENERGY_AND_TORCH_SLOTS = 2;
  private static final int FIRST_PLAYER_INVENTORY_SLOT = 9;
  private static final int DIAMONDS = 3;
  private static final BlockPos FLOOR = new BlockPos(1, 1, 1);

  private InventoryGridGameTests() {}

  @GameTest(template = EMPTY)
  public static void theBaseKeepsNineColumnsAndItsWidth(GameTestHelper helper) {
    RobotMenu menu = menuFor(helper, RobotTier.BASE);

    helper.assertValueEqual(menu.grid().columns(), BASE_COLUMNS, WRONG_GRID);
    helper.assertValueEqual(menu.extraWidth(), 0, WRONG_GRID);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void theUltimateShowsItsWholeInventoryInThreeRows(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = place(helper, owner, RobotTier.ULTIMATE);
    robot.inventory().setItem(LAST_SLOT, new ItemStack(Items.DIAMOND));
    RobotMenu menu = RobotScreenReadout.serverMenu(0, owner.getInventory(), robot);

    helper.assertValueEqual(menu.grid().columns(), ULTIMATE_COLUMNS, WRONG_GRID);
    helper.assertValueEqual(menu.rows(), THREE_ROWS, WRONG_GRID);
    helper.assertTrue(menu.getSlot(LAST_SLOT).getItem().is(Items.DIAMOND), WRONG_ITEM);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void theInventoryOfThePlayerStaysAtTheLeft(GameTestHelper helper) {
    RobotMenu menu = menuFor(helper, RobotTier.ULTIMATE);
    int firstPlayerSlot = ULTIMATE_SLOTS + ENERGY_AND_TORCH_SLOTS;

    helper.assertValueEqual(menu.getSlot(firstPlayerSlot).x, RobotMenu.FIRST_SLOT_X, NOT_LEFT);
    helper.succeed();
  }

  /** The filter starts at the left and grows sideways with the window (answer 1105). */
  @GameTest(template = EMPTY)
  public static void theFilterStartsAtTheLeftWithTheColumnsOfTheWindow(GameTestHelper helper) {
    RobotMenu menu = menuFor(helper, RobotTier.ULTIMATE);
    int secondRow = menu.filterFirstSlot() + ULTIMATE_COLUMNS;

    helper.assertValueEqual(
        menu.getSlot(menu.filterFirstSlot()).x, RobotMenu.FIRST_SLOT_X, NOT_LEFT);
    helper.assertValueEqual(menu.getSlot(secondRow).x, RobotMenu.FIRST_SLOT_X, NOT_LEFT);
    helper.succeed();
  }

  /** On the Tablet, the energy slot comes first in the row of the exclusive slots (1104). */
  @GameTest(template = EMPTY)
  public static void theTabletPutsTheEnergySlotInTheExclusiveRow(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = place(helper, owner, RobotTier.ADVANCED);
    RobotMenu menu = RobotScreenReadout.tabletMenu(0, owner.getInventory(), robot);

    helper.assertValueEqual(
        menu.getSlot(menu.energySlotIndex()).y, RobotMenu.EXCLUSIVE_SLOT_Y, NOT_IN_ROW);
    helper.assertValueEqual(
        menu.getSlot(menu.torchSlotIndex()).x,
        RobotMenu.FIRST_SLOT_X + RobotMenu.SLOT_SIZE,
        NOT_IN_ROW);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void shiftClickFillsTheWholeInventory(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = place(helper, owner, RobotTier.ULTIMATE);
    Container inventory = robot.inventory();
    for (int slot = 0; slot < BASE_SLOTS; slot++) {
      inventory.setItem(slot, new ItemStack(Items.STONE, Items.STONE.getDefaultMaxStackSize()));
    }
    owner
        .getInventory()
        .setItem(FIRST_PLAYER_INVENTORY_SLOT, new ItemStack(Items.DIAMOND, DIAMONDS));
    RobotMenu menu = RobotScreenReadout.serverMenu(0, owner.getInventory(), robot);

    menu.quickMoveStack(owner, ULTIMATE_SLOTS + ENERGY_AND_TORCH_SLOTS);

    helper.assertTrue(inventory.getItem(BASE_SLOTS).is(Items.DIAMOND), NOT_STORED);
    helper.assertTrue(owner.getInventory().getItem(FIRST_PLAYER_INVENTORY_SLOT).isEmpty(), MOVED);
    helper.succeed();
  }

  private static RobotMenu menuFor(GameTestHelper helper, RobotTier tier) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    return RobotScreenReadout.serverMenu(0, owner.getInventory(), place(helper, owner, tier));
  }

  private static RobotEntity place(GameTestHelper helper, ServerPlayer owner, RobotTier tier) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(CubitDataComponents.ROBOT_STATE.get(), new RobotState(tier, CHARGE, HEALTH));
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item, FLOOR);
    owner.moveTo(robot.getX(), robot.getY(), robot.getZ());
    return robot;
  }
}
