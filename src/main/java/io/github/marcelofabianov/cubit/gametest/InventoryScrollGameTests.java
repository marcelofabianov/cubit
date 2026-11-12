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
 * The robot screen keeps the height of the Base and scrolls the rows of a bigger inventory (answers
 * 838 and 839). The menu is opened as the server opens it for the owner.
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class InventoryScrollGameTests {
  private static final String EMPTY = "empty";
  private static final String WRONG_ROWS = "Wrong rows on the screen";
  private static final String WRONG_ITEM = "The slot shows the wrong item";
  private static final String WRONG_SCROLL = "Wrong answer to the scroll";
  private static final String NOT_STORED = "The item should be in a row the screen does not show";
  private static final String MOVED = "The item should have left the player";

  private static final float HEALTH = 25f;
  private static final Energy CHARGE = new Energy(100_000);
  private static final int VISIBLE_SLOTS = 27;
  private static final int ULTIMATE_SLOTS = 54;
  private static final int HIDDEN_SLOT = 40;
  private static final int TOP_ROW = 2;
  private static final int VIEW_SLOT_OF_HIDDEN = HIDDEN_SLOT - TOP_ROW * RobotMenu.SLOTS_PER_ROW;
  private static final int BEYOND_LAST_ROW = 4;
  private static final int FIRST_PLAYER_SLOT_IN_MENU = VISIBLE_SLOTS + 1;
  private static final int FIRST_PLAYER_INVENTORY_SLOT = 9;
  private static final int DIAMONDS = 3;
  private static final BlockPos FLOOR = new BlockPos(1, 1, 1);

  private InventoryScrollGameTests() {}

  @GameTest(template = EMPTY)
  public static void theBaseScreenShowsAllItsRowsWithoutScrolling(GameTestHelper helper) {
    RobotMenu menu = menuFor(helper, RobotTier.BASE);

    helper.assertFalse(menu.window().scrolls(), WRONG_ROWS);
    helper.assertValueEqual(menu.rows(), RobotMenu.VISIBLE_ROWS, WRONG_ROWS);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void theUltimateScreenKeepsTheHeightOfTheBase(GameTestHelper helper) {
    RobotMenu menu = menuFor(helper, RobotTier.ULTIMATE);

    helper.assertTrue(menu.window().scrolls(), WRONG_ROWS);
    helper.assertValueEqual(menu.rows(), RobotMenu.VISIBLE_ROWS, WRONG_ROWS);
    helper.assertValueEqual(menu.window().slots(), ULTIMATE_SLOTS, WRONG_ROWS);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void scrollingShowsTheHiddenRows(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = place(helper, owner, RobotTier.ULTIMATE);
    robot.inventory().setItem(HIDDEN_SLOT, new ItemStack(Items.DIAMOND));
    RobotMenu menu = RobotScreenReadout.serverMenu(0, owner.getInventory(), robot);

    boolean scrolled = menu.clickMenuButton(owner, RobotMenu.FIRST_SCROLL_BUTTON + TOP_ROW);

    helper.assertTrue(scrolled, WRONG_SCROLL);
    helper.assertTrue(menu.getSlot(VIEW_SLOT_OF_HIDDEN).getItem().is(Items.DIAMOND), WRONG_ITEM);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void aScrollPastTheLastRowIsRefused(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = place(helper, owner, RobotTier.ULTIMATE);
    RobotMenu menu = RobotScreenReadout.serverMenu(0, owner.getInventory(), robot);

    boolean scrolled = menu.clickMenuButton(owner, RobotMenu.FIRST_SCROLL_BUTTON + BEYOND_LAST_ROW);

    helper.assertFalse(scrolled, WRONG_SCROLL);
    helper.assertValueEqual(menu.topRow(), 0, WRONG_SCROLL);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void shiftClickFillsTheRowsTheScreenDoesNotShow(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = place(helper, owner, RobotTier.ULTIMATE);
    Container inventory = robot.inventory();
    for (int slot = 0; slot < VISIBLE_SLOTS; slot++) {
      inventory.setItem(slot, new ItemStack(Items.STONE, Items.STONE.getDefaultMaxStackSize()));
    }
    owner
        .getInventory()
        .setItem(FIRST_PLAYER_INVENTORY_SLOT, new ItemStack(Items.DIAMOND, DIAMONDS));
    RobotMenu menu = RobotScreenReadout.serverMenu(0, owner.getInventory(), robot);

    menu.quickMoveStack(owner, FIRST_PLAYER_SLOT_IN_MENU);

    helper.assertTrue(inventory.getItem(VISIBLE_SLOTS).is(Items.DIAMOND), NOT_STORED);
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
