package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotScreenReadout;
import io.github.marcelofabianov.cubit.menu.PickupScreenValues;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The robot keeps its items in every path, and only its owner reaches them. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class InventoryGameTests {
  private static final String ARENA = "arena";
  private static final int FLOOR_Y = 1;
  private static final int START_X = 1;
  private static final int MIDDLE_Z = 2;
  private static final int LAST_Z = 4;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos SECOND_FLOOR = new BlockPos(START_X, FLOOR_Y, LAST_Z);
  private static final int SOME_SLOT = 13;
  private static final int SOME_COUNT = 5;
  private static final int BASE_SLOTS = 27;
  private static final int PLAYER_SLOTS = 36;
  private static final int ENERGY_SLOTS = 1;
  private static final int TORCH_SLOTS = 1;
  private static final int ABOVE_THE_SLOTS = 30;
  private static final int SETTLE_TICKS = 5;
  private static final double FAR_AWAY = 30;
  private static final String NOT_OPENED = "The screen did not open";
  private static final String OPENED = "The screen opened for the wrong player or state";
  private static final String STILL_VALID = "The screen stayed valid";
  private static final String ENTERED = "A forbidden item entered the robot";
  private static final String REJECTED = "A normal item did not enter the robot";
  private static final String WRONG_CONTENT = "Wrong robot inventory";
  private static final String DUPLICATED = "The items stayed in the dead or collected robot";
  private static final String LOST = "Items were lost";
  private static final String NO_REPAIR = "The repair recipe did not match";

  private InventoryGameTests() {}

  /** A mock player cannot receive the packet that opens a screen, so the manual script does. */
  @GameTest(template = ARENA)
  public static void theOwnerOpensTheInventoryOfAnActiveRobot(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    helper.assertValueEqual(
        robot.interact(owner, InteractionHand.MAIN_HAND), InteractionResult.CONSUME, NOT_OPENED);
    helper.assertValueEqual(
        menuOf(robot, owner).slots.size(),
        BASE_SLOTS
            + ENERGY_SLOTS
            + TORCH_SLOTS
            + PLAYER_SLOTS
            + PickupScreenValues.MAX_FILTER_SLOTS,
        NOT_OPENED);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aStrangerDoesNotOpenTheInventory(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    Player stranger = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    helper.assertValueEqual(
        robot.interact(stranger, InteractionHand.MAIN_HAND), InteractionResult.PASS, OPENED);
    Vec3 nextToTheRobot = robot.position();
    stranger.moveTo(nextToTheRobot.x, nextToTheRobot.y, nextToTheRobot.z);
    helper.assertFalse(robot.canUseInventory(stranger), OPENED);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aSwitchedOffRobotTurnsOnWithoutOpening(GameTestHelper helper) {
    ServerPlayer owner = playerNear(helper);
    GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    RobotEntity second = GameTestRobots.placeNewRobot(helper, owner, SECOND_FLOOR);
    helper.assertValueEqual(second.powerState(), PowerState.INACTIVE_SWITCHED_OFF, OPENED);
    helper.assertFalse(second.canUseInventory(owner), OPENED);

    second.interact(owner, InteractionHand.MAIN_HAND);

    helper.assertValueEqual(second.powerState(), PowerState.ACTIVE, OPENED);
    helper.assertFalse(owner.containerMenu instanceof RobotMenu, OPENED);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theScreenClosesWhenThePlayerWalksAway(GameTestHelper helper) {
    ServerPlayer owner = playerNear(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    helper.assertTrue(robot.canUseInventory(owner), NOT_OPENED);

    owner.moveTo(owner.getX() + FAR_AWAY, owner.getY(), owner.getZ());

    helper.assertFalse(robot.canUseInventory(owner), STILL_VALID);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theScreenClosesWhenTheRobotDies(GameTestHelper helper) {
    ServerPlayer owner = playerNear(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    RobotMenu menu = menuOf(robot, owner);
    helper.assertTrue(menu.stillValid(owner), NOT_OPENED);

    robot.kill();

    helper.assertFalse(menu.stillValid(owner), STILL_VALID);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void robotsAndShulkerBoxesDoNotEnterTheInventory(GameTestHelper helper) {
    ServerPlayer owner = playerNear(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    Slot slot = menuOf(robot, owner).slots.getFirst();

    helper.assertFalse(slot.mayPlace(new ItemStack(CubitItems.GENERAL_ROBOT.get())), ENTERED);
    helper.assertFalse(
        slot.mayPlace(new ItemStack(CubitItems.DAMAGED_GENERAL_ROBOT.get())), ENTERED);
    helper.assertFalse(slot.mayPlace(new ItemStack(Items.SHULKER_BOX)), ENTERED);
    helper.assertTrue(slot.mayPlace(new ItemStack(Items.IRON_INGOT)), REJECTED);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void collectingAndPlacingKeepTheItemsInTheirSlots(GameTestHelper helper) {
    ServerPlayer owner = playerNear(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    ItemStack iron = new ItemStack(Items.IRON_INGOT, SOME_COUNT);
    robot.inventory().setItem(SOME_SLOT, iron.copy());

    owner.setShiftKeyDown(true);
    robot.interact(owner, InteractionHand.MAIN_HAND);
    owner.setShiftKeyDown(false);

    helper.assertTrue(robot.inventory().isEmpty(), DUPLICATED);
    ItemStack item = collectedItem(owner);
    RobotEntity placed = GameTestRobots.placeRobot(helper, owner, item, SECOND_FLOOR);
    assertHoldsIron(helper, placed);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void deathKeepsTheItemsInTheDamagedRobot(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    robot.inventory().setItem(SOME_SLOT, new ItemStack(Items.IRON_INGOT, SOME_COUNT));

    robot.kill();

    helper.assertTrue(robot.inventory().isEmpty(), DUPLICATED);
    ItemStack damaged = droppedRobot(helper);
    helper.assertTrue(damaged.is(CubitItems.DAMAGED_GENERAL_ROBOT.get()), LOST);
    assertContentsHoldIron(helper, damaged.get(CubitDataComponents.ROBOT_INVENTORY.get()));
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theRepairKeepsTheItems(GameTestHelper helper) {
    ItemStack damaged = new ItemStack(CubitItems.DAMAGED_GENERAL_ROBOT.get());
    damaged.set(CubitDataComponents.ROBOT_INVENTORY.get(), contentsWithIron());
    CraftingInput input =
        CraftingInput.of(2, 1, List.of(damaged, new ItemStack(CubitItems.TOOLBOX.get())));

    ItemStack repaired =
        helper
            .getLevel()
            .getRecipeManager()
            .getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel())
            .map(holder -> holder.value().assemble(input, helper.getLevel().registryAccess()))
            .orElseThrow(() -> new IllegalStateException(NO_REPAIR));

    assertContentsHoldIron(helper, repaired.get(CubitDataComponents.ROBOT_INVENTORY.get()));
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void savingKeepsTheItems(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    robot.inventory().setItem(SOME_SLOT, new ItemStack(Items.IRON_INGOT, SOME_COUNT));

    assertHoldsIron(helper, reload(helper, robot));
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void whatDoesNotFitFallsOnTheGround(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(CubitDataComponents.ROBOT_INVENTORY.get(), contentsAboveTheSlots());
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item, FLOOR);

    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertValueEqual(robot.inventory().getContainerSize(), BASE_SLOTS, WRONG_CONTENT);
          helper.assertValueEqual(heldCount(robot) + droppedCount(helper), ABOVE_THE_SLOTS, LOST);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void theSurplusSurvivesASaveBeforeItFalls(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(CubitDataComponents.ROBOT_INVENTORY.get(), contentsAboveTheSlots());
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item, FLOOR);

    RobotEntity loaded = reload(helper, robot);
    helper.getLevel().addFreshEntity(loaded);
    robot.discard();

    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertValueEqual(heldCount(loaded) + droppedCount(helper), ABOVE_THE_SLOTS, LOST);
          helper.succeed();
        });
  }

  private static RobotMenu menuOf(RobotEntity robot, Player player) {
    return RobotScreenReadout.serverMenu(0, player.getInventory(), robot);
  }

  private static ServerPlayer playerNear(GameTestHelper helper) {
    ServerPlayer player = GameTestRobots.onlinePlayer(helper);
    Vec3 position = helper.absoluteVec(Vec3.atBottomCenterOf(FLOOR.above()));
    player.moveTo(position.x, position.y, position.z);
    return player;
  }

  private static RobotEntity reload(GameTestHelper helper, RobotEntity robot) {
    CompoundTag saved = robot.saveWithoutId(new CompoundTag());
    RobotEntity loaded = CubitEntities.GENERAL_ROBOT.get().create(helper.getLevel());
    loaded.load(saved);
    loaded.setUUID(UUID.randomUUID());
    return loaded;
  }

  private static ItemContainerContents contentsWithIron() {
    List<ItemStack> stacks = new ArrayList<>();
    for (int slot = 0; slot < SOME_SLOT; slot++) {
      stacks.add(ItemStack.EMPTY);
    }
    stacks.add(new ItemStack(Items.IRON_INGOT, SOME_COUNT));
    return ItemContainerContents.fromItems(stacks);
  }

  private static ItemContainerContents contentsAboveTheSlots() {
    List<ItemStack> stacks = new ArrayList<>();
    for (int slot = 0; slot < ABOVE_THE_SLOTS; slot++) {
      stacks.add(new ItemStack(Items.IRON_INGOT));
    }
    return ItemContainerContents.fromItems(stacks);
  }

  private static void assertHoldsIron(GameTestHelper helper, RobotEntity robot) {
    ItemStack stack = robot.inventory().getItem(SOME_SLOT);
    helper.assertTrue(stack.is(Items.IRON_INGOT), WRONG_CONTENT);
    helper.assertValueEqual(stack.getCount(), SOME_COUNT, WRONG_CONTENT);
  }

  private static void assertContentsHoldIron(
      GameTestHelper helper, ItemContainerContents contents) {
    helper.assertTrue(contents != null, LOST);
    helper.assertTrue(
        ItemStack.matches(
            contents.stream().toList().get(SOME_SLOT), new ItemStack(Items.IRON_INGOT, SOME_COUNT)),
        WRONG_CONTENT);
  }

  private static ItemStack collectedItem(Player player) {
    return player.getInventory().items.stream()
        .filter(stack -> stack.is(CubitItems.GENERAL_ROBOT.get()))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(LOST));
  }

  private static ItemStack droppedRobot(GameTestHelper helper) {
    return helper.getEntities(EntityType.ITEM).getFirst().getItem();
  }

  /** The pickup may take back what fits in a stack, so only the total proves nothing is lost. */
  private static int heldCount(RobotEntity robot) {
    int held = 0;
    for (int slot = 0; slot < robot.inventory().getContainerSize(); slot++) {
      held += robot.inventory().getItem(slot).getCount();
    }
    return held;
  }

  private static int droppedCount(GameTestHelper helper) {
    return helper.getEntities(EntityType.ITEM).stream()
        .mapToInt(entity -> entity.getItem().getCount())
        .sum();
  }
}
