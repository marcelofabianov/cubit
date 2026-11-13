package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.OreFilter;
import io.github.marcelofabianov.cubit.domain.robot.PickupEntry;
import io.github.marcelofabianov.cubit.domain.robot.PickupFilter;
import io.github.marcelofabianov.cubit.domain.robot.PickupMode;
import io.github.marcelofabianov.cubit.domain.robot.PickupSettings;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Pickup by touch, the magnet and the pickup filter (answers 876 to 892). */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class PickupGameTests {
  private static final String ARENA = "arena";
  private static final String NOT_COLLECTED = "The robot should have picked up the item";
  private static final String COLLECTED = "The robot should have left the item";
  private static final String WRONG_COST = "The magnet should cost one item once";
  private static final String WRONG_SETTINGS = "The pickup settings should be kept";
  private static final String WRONG_REST = "What did not fit should stay on the ground";
  private static final String MISSING_ITEM = "minecraft:not_an_item";
  private static final BlockPos FLOOR = new BlockPos(1, 1, 1);
  private static final int FLOOR_LENGTH = 6;
  private static final float ITEM_HEIGHT = 2.1f;
  private static final float CENTER = 1.5f;
  private static final float THREE_BLOCKS_AWAY = 4.5f;
  private static final int WAIT_TICKS = 30;
  private static final int QUICK_ARRIVAL_TICKS = 25;
  private static final int LONG_DELAY = 200;
  private static final float HEALTH = 20f;
  private static final Energy CHARGE = new Energy(100_000);
  private static final long ADVANCED_COST = 20;
  private static final int HALF_STACK = 32;
  private static final int FULL_STACK = 64;

  private PickupGameTests() {}

  @GameTest(template = ARENA)
  public static void theRobotPicksUpWhatItTouches(GameTestHelper helper) {
    RobotEntity robot = place(helper, RobotTier.BASE);
    dropBeside(helper, Items.DIRT);

    helper.succeedWhen(() -> helper.assertTrue(holds(robot, Items.DIRT), NOT_COLLECTED));
  }

  @GameTest(template = ARENA)
  public static void theBlacklistLeavesTheListedItems(GameTestHelper helper) {
    RobotEntity robot = place(helper, RobotTier.BASE);
    robot.pickup().setSettings(PickupSettings.INITIAL.withFilter(only(PickupMode.BLACKLIST)));
    dropBeside(helper, Items.DIRT);

    helper.runAfterDelay(
        WAIT_TICKS,
        () -> {
          helper.assertFalse(holds(robot, Items.DIRT), COLLECTED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void theWhitelistTakesOnlyTheListedItems(GameTestHelper helper) {
    RobotEntity robot = place(helper, RobotTier.BASE);
    robot.pickup().setSettings(PickupSettings.INITIAL.withFilter(only(PickupMode.WHITELIST)));
    dropBeside(helper, Items.DIRT);
    dropBeside(helper, Items.STONE);

    helper.runAfterDelay(
        WAIT_TICKS,
        () -> {
          helper.assertTrue(holds(robot, Items.DIRT), NOT_COLLECTED);
          helper.assertFalse(holds(robot, Items.STONE), COLLECTED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void withPickupOffNothingIsTaken(GameTestHelper helper) {
    RobotEntity robot = place(helper, RobotTier.ADVANCED);
    robot.pickup().setSettings(PickupSettings.INITIAL.toggledPickup());
    dropBeside(helper, Items.DIRT);
    dropAway(helper, Items.STONE);

    helper.runAfterDelay(WAIT_TICKS, () -> assertEmpty(helper, robot));
  }

  @GameTest(template = ARENA)
  public static void itWaitsForThePickupDelay(GameTestHelper helper) {
    RobotEntity robot = place(helper, RobotTier.BASE);
    dropBeside(helper, Items.DIRT).setPickUpDelay(LONG_DELAY);

    helper.runAfterDelay(WAIT_TICKS, () -> assertEmpty(helper, robot));
  }

  @GameTest(template = ARENA)
  public static void itNeverTakesARobot(GameTestHelper helper) {
    RobotEntity robot = place(helper, RobotTier.BASE);
    dropBeside(helper, CubitItems.GENERAL_ROBOT.get());
    dropBeside(helper, CubitItems.DAMAGED_GENERAL_ROBOT.get());

    helper.runAfterDelay(WAIT_TICKS, () -> assertEmpty(helper, robot));
  }

  @GameTest(template = ARENA)
  public static void aFullInventoryTakesWhatFits(GameTestHelper helper) {
    RobotEntity robot = place(helper, RobotTier.BASE);
    Container inventory = robot.inventory();
    for (int slot = 1; slot < inventory.getContainerSize(); slot++) {
      inventory.setItem(slot, new ItemStack(Items.STONE, FULL_STACK));
    }
    inventory.setItem(0, new ItemStack(Items.DIRT, HALF_STACK));
    ItemEntity ground = dropBeside(helper, new ItemStack(Items.DIRT, FULL_STACK));

    helper.succeedWhen(() -> {
      helper.assertValueEqual(inventory.getItem(0).getCount(), FULL_STACK, NOT_COLLECTED);
      helper.assertValueEqual(ground.getItem().getCount(), HALF_STACK, WRONG_REST);
    });
  }

  @GameTest(template = ARENA)
  public static void theMagnetPullsAndChargesEachItemOnce(GameTestHelper helper) {
    RobotEntity robot = place(helper, RobotTier.ADVANCED);
    long before = robot.state().energy().amount();
    dropAway(helper, Items.DIRT);

    helper.succeedWhen(() -> {
      helper.assertTrue(holds(robot, Items.DIRT), NOT_COLLECTED);
      helper.assertValueEqual(before - robot.state().energy().amount(), ADVANCED_COST, WRONG_COST);
    });
  }

  /** The first magnet check runs at tick 10, and the item arrives soon after (answer 928). */
  @GameTest(template = ARENA)
  public static void aPulledItemArrivesInAFractionOfASecond(GameTestHelper helper) {
    RobotEntity robot = place(helper, RobotTier.ADVANCED);
    dropAway(helper, Items.DIRT);

    helper.runAfterDelay(
        QUICK_ARRIVAL_TICKS,
        () -> {
          helper.assertTrue(holds(robot, Items.DIRT), NOT_COLLECTED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void baseHasNoMagnet(GameTestHelper helper) {
    RobotEntity robot = place(helper, RobotTier.BASE);
    dropAway(helper, Items.DIRT);

    helper.runAfterDelay(WAIT_TICKS, () -> assertEmpty(helper, robot));
  }

  @GameTest(template = ARENA)
  public static void withTheMagnetOffItemsAreNotPulled(GameTestHelper helper) {
    RobotEntity robot = place(helper, RobotTier.ADVANCED);
    robot.pickup().setSettings(PickupSettings.INITIAL.toggledMagnet());
    dropAway(helper, Items.DIRT);

    helper.runAfterDelay(WAIT_TICKS, () -> assertEmpty(helper, robot));
  }

  @GameTest(template = ARENA)
  public static void theItemAndTheSaveKeepTheSettings(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item(RobotTier.BASE), FLOOR);
    PickupSettings custom =
        PickupSettings.INITIAL
            .toggledMagnet()
            .toggledFetch()
            .withFilter(only(PickupMode.WHITELIST));
    robot.pickup().setSettings(custom);

    RobotEntity loaded = GameTestRobots.loadRobot(helper, robot.saveWithoutId(new CompoundTag()));
    ItemStack collected = collect(owner, robot);
    RobotState saved = collected.get(CubitDataComponents.ROBOT_STATE.get());

    helper.assertValueEqual(loaded.pickup().settings(), custom, WRONG_SETTINGS);
    helper.assertValueEqual(saved.pickup(), custom, WRONG_SETTINGS);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void anEntryTheGameLacksIsDropped(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    PickupFilter saved =
        new PickupFilter(
            PickupMode.BLACKLIST,
            List.of(PickupEntry.item(MISSING_ITEM), PickupEntry.item(dirtId())));
    ItemStack stack = item(RobotTier.BASE);
    RobotState state = stack.get(CubitDataComponents.ROBOT_STATE.get());
    stack.set(
        CubitDataComponents.ROBOT_STATE.get(),
        state.withPickup(PickupSettings.INITIAL.withFilter(saved)));

    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, stack, FLOOR);

    helper.assertValueEqual(
        robot.pickup().settings().filter().entries(),
        List.of(PickupEntry.item(dirtId())),
        WRONG_SETTINGS);
    helper.succeed();
  }

  private static RobotEntity place(GameTestHelper helper, RobotTier tier) {
    for (int x = 0; x < FLOOR_LENGTH; x++) {
      helper.setBlock(new BlockPos(x, FLOOR.getY(), FLOOR.getZ()), Blocks.STONE);
    }
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item(tier), FLOOR);
    robot.solar().toggle();
    return robot;
  }

  /** Lamp, scanners and chatter off, so nothing but the magnet spends. */
  private static ItemStack item(RobotTier tier) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(
        CubitDataComponents.ROBOT_STATE.get(),
        new RobotState(
            tier, CHARGE, HEALTH, false, RobotColor.WHITE, false, OreFilter.EMPTY, false));
    return item;
  }

  private static PickupFilter only(PickupMode mode) {
    return new PickupFilter(mode, List.of(PickupEntry.item(dirtId())));
  }

  private static String dirtId() {
    return BuiltInRegistries.ITEM.getKey(Items.DIRT).toString();
  }

  private static ItemEntity dropBeside(GameTestHelper helper, Item item) {
    return dropBeside(helper, new ItemStack(item));
  }

  private static ItemEntity dropBeside(GameTestHelper helper, ItemStack stack) {
    ItemEntity entity = helper.spawnItem(stack.getItem(), CENTER, ITEM_HEIGHT, CENTER);
    entity.setItem(stack);
    entity.setNoPickUpDelay();
    return entity;
  }

  private static void dropAway(GameTestHelper helper, Item item) {
    ItemEntity entity = helper.spawnItem(item, THREE_BLOCKS_AWAY, ITEM_HEIGHT, CENTER);
    entity.setNoPickUpDelay();
  }

  private static boolean holds(RobotEntity robot, Item item) {
    return robot.inventory().countItem(item) > 0;
  }

  private static void assertEmpty(GameTestHelper helper, RobotEntity robot) {
    helper.assertTrue(robot.inventory().isEmpty(), COLLECTED);
    helper.succeed();
  }

  private static ItemStack collect(Player owner, RobotEntity robot) {
    owner.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    owner.setShiftKeyDown(true);
    robot.interact(owner, InteractionHand.MAIN_HAND);
    owner.setShiftKeyDown(false);
    return owner.getInventory().getItem(0);
  }
}
