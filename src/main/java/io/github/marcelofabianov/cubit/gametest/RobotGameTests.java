package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Placing, collecting and saving only exist in the world, so they run in the real game. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class RobotGameTests {
  private static final String EMPTY_TEMPLATE = "empty";
  private static final BlockPos FIRST_FLOOR = new BlockPos(0, 0, 0);
  private static final BlockPos SECOND_FLOOR = new BlockPos(2, 0, 2);
  private static final long INITIAL_ENERGY_AMOUNT = 75_000;
  private static final long SAVED_ENERGY_AMOUNT = 1_234;
  private static final long CAPACITY_AMOUNT = 300_000;
  private static final long TOO_MUCH_ENERGY_AMOUNT = 999_999;
  private static final float SAVED_HEALTH = 7.5f;
  private static final float MAX_HEALTH = 25f;
  private static final int INITIAL_BAR_WIDTH = 3;
  private static final String INVALID_VALUE = "bogus";
  private static final String TIER_KEY = "Tier";
  private static final String POWER_STATE_KEY = "PowerState";
  private static final String WRONG_STATE = "Wrong robot state";
  private static final String WRONG_POWER = "Wrong power state";
  private static final String WRONG_LINK = "Wrong following robot";
  private static final String WRONG_OWNER = "Wrong robot owner";
  private static final String WRONG_COUNT = "Wrong robot count";
  private static final String WRONG_STACK = "Wrong item stack";
  private static final String WRONG_BAR = "Wrong battery bar";
  private static final String ITEM_NOT_SPENT = "Placing did not spend the item";
  private static final String ITEM_SPENT = "Placing without room spent the item";
  private static final String ITEM_FITS_CONTAINER = "The robot item fits inside containers";

  private RobotGameTests() {}

  @GameTest(template = EMPTY_TEMPLATE)
  public static void placingANewRobotMakesItFollowItsOwner(GameTestHelper helper) {
    Player player = helper.makeMockPlayer(GameType.SURVIVAL);
    ItemStack stack = new ItemStack(CubitItems.GENERAL_ROBOT.get());

    RobotEntity robot = placeRobot(helper, player, stack, FIRST_FLOOR);

    helper.assertTrue(stack.isEmpty(), ITEM_NOT_SPENT);
    helper.assertValueEqual(robot.ownerId(), Optional.of(player.getUUID()), WRONG_OWNER);
    helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_POWER);
    helper.assertValueEqual(robot.state(), newRobotState(), WRONG_STATE);
    helper.assertValueEqual(follower(player), Optional.of(robot.getUUID()), WRONG_LINK);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void placingEvenInCreativeSpendsTheItem(GameTestHelper helper) {
    Player player = helper.makeMockPlayer(GameType.CREATIVE);
    ItemStack stack = new ItemStack(CubitItems.GENERAL_ROBOT.get());

    placeRobot(helper, player, stack, FIRST_FLOOR);

    helper.assertTrue(stack.isEmpty(), ITEM_NOT_SPENT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void placingASecondRobotLeavesItSwitchedOff(GameTestHelper helper) {
    Player player = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity first = placeNewRobot(helper, player, FIRST_FLOOR);
    RobotEntity second = placeNewRobot(helper, player, SECOND_FLOOR);

    helper.assertValueEqual(second.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_POWER);
    helper.assertValueEqual(first.powerState(), PowerState.ACTIVE, WRONG_POWER);
    helper.assertValueEqual(follower(player), Optional.of(first.getUUID()), WRONG_LINK);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void turningOnARobotSwitchesOffTheOneThatFollowed(GameTestHelper helper) {
    Player player = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity first = placeNewRobot(helper, player, FIRST_FLOOR);
    RobotEntity second = placeNewRobot(helper, player, SECOND_FLOOR);

    second.interact(player, InteractionHand.MAIN_HAND);

    helper.assertValueEqual(second.powerState(), PowerState.ACTIVE, WRONG_POWER);
    helper.assertValueEqual(first.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_POWER);
    helper.assertValueEqual(follower(player), Optional.of(second.getUUID()), WRONG_LINK);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void collectingAndPlacingAgainPreservesTheState(GameTestHelper helper) {
    Player player = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotState saved =
        new RobotState(RobotTier.BASE, new Energy(SAVED_ENERGY_AMOUNT), SAVED_HEALTH);
    RobotEntity robot = placeRobot(helper, player, stackWith(saved), FIRST_FLOOR);

    player.setShiftKeyDown(true);
    robot.interact(player, InteractionHand.MAIN_HAND);
    player.setShiftKeyDown(false);

    helper.assertTrue(robot.isRemoved(), WRONG_COUNT);
    helper.assertValueEqual(follower(player), Optional.empty(), WRONG_LINK);
    ItemStack collected = player.getInventory().getItem(player.getInventory().selected);
    helper.assertValueEqual(
        collected.get(CubitDataComponents.ROBOT_STATE.get()), saved, WRONG_STACK);

    RobotEntity again = placeRobot(helper, player, collected, SECOND_FLOOR);
    helper.assertValueEqual(again.state(), saved, WRONG_STATE);
    helper.assertTrue(!again.getUUID().equals(robot.getUUID()), WRONG_STATE);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void onlyTheOwnerCanInteract(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    Player stranger = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity first = placeNewRobot(helper, owner, FIRST_FLOOR);
    RobotEntity second = placeNewRobot(helper, owner, SECOND_FLOOR);

    stranger.setShiftKeyDown(true);
    first.interact(stranger, InteractionHand.MAIN_HAND);
    stranger.setShiftKeyDown(false);
    second.interact(stranger, InteractionHand.MAIN_HAND);

    helper.assertTrue(!first.isRemoved(), WRONG_COUNT);
    helper.assertValueEqual(second.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_POWER);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void placingWithoutRoomKeepsTheItem(GameTestHelper helper) {
    Player player = helper.makeMockPlayer(GameType.SURVIVAL);
    ItemStack stack = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    helper.setBlock(FIRST_FLOOR.above(), Blocks.STONE);

    use(helper, player, stack, FIRST_FLOOR);

    helper.assertFalse(stack.isEmpty(), ITEM_SPENT);
    helper.assertValueEqual(robots(helper).size(), 0, WRONG_COUNT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void savingAndLoadingPreservesTheRobot(GameTestHelper helper) {
    Player player = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = placeNewRobot(helper, player, FIRST_FLOOR);

    RobotEntity loaded = reload(helper, robot.saveWithoutId(new CompoundTag()));

    helper.assertValueEqual(loaded.state(), robot.state(), WRONG_STATE);
    helper.assertValueEqual(loaded.ownerId(), robot.ownerId(), WRONG_OWNER);
    helper.assertValueEqual(loaded.powerState(), robot.powerState(), WRONG_POWER);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void invalidSavedDataUsesSafeValues(GameTestHelper helper) {
    Player player = helper.makeMockPlayer(GameType.SURVIVAL);
    CompoundTag saved = placeNewRobot(helper, player, FIRST_FLOOR).saveWithoutId(new CompoundTag());
    saved.putString(TIER_KEY, INVALID_VALUE);
    saved.putString(POWER_STATE_KEY, INVALID_VALUE);

    RobotEntity loaded = reload(helper, saved);

    helper.assertValueEqual(loaded.state().tier(), RobotTier.BASE, WRONG_STATE);
    helper.assertValueEqual(loaded.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_POWER);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void energyAboveTheCapacityIsLimitedWhenPlacing(GameTestHelper helper) {
    Player player = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotState saved =
        new RobotState(RobotTier.BASE, new Energy(TOO_MUCH_ENERGY_AMOUNT), SAVED_HEALTH);

    RobotEntity robot = placeRobot(helper, player, stackWith(saved), FIRST_FLOOR);

    helper.assertValueEqual(robot.state().energy(), new Energy(CAPACITY_AMOUNT), WRONG_STATE);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void itemFollowsTheStackRules(GameTestHelper helper) {
    ItemStack stack = new ItemStack(CubitItems.GENERAL_ROBOT.get());

    helper.assertValueEqual(stack.getMaxStackSize(), 1, WRONG_STACK);
    helper.assertFalse(stack.getItem().canFitInsideContainerItems(), ITEM_FITS_CONTAINER);
    helper.assertTrue(stack.isBarVisible(), WRONG_BAR);
    helper.assertValueEqual(stack.getBarWidth(), INITIAL_BAR_WIDTH, WRONG_BAR);
    helper.succeed();
  }

  private static RobotState newRobotState() {
    return new RobotState(RobotTier.BASE, new Energy(INITIAL_ENERGY_AMOUNT), MAX_HEALTH);
  }

  private static ItemStack stackWith(RobotState state) {
    ItemStack stack = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    stack.set(CubitDataComponents.ROBOT_STATE.get(), state);
    return stack;
  }

  private static RobotEntity placeNewRobot(GameTestHelper helper, Player player, BlockPos floor) {
    return placeRobot(helper, player, new ItemStack(CubitItems.GENERAL_ROBOT.get()), floor);
  }

  private static RobotEntity placeRobot(
      GameTestHelper helper, Player player, ItemStack stack, BlockPos floor) {
    List<RobotEntity> before = robots(helper);
    use(helper, player, stack, floor);
    return robots(helper).stream()
        .filter(robot -> !before.contains(robot))
        .findFirst()
        .orElseThrow();
  }

  private static void use(GameTestHelper helper, Player player, ItemStack stack, BlockPos floor) {
    helper.setBlock(floor, Blocks.STONE);
    BlockPos absolute = helper.absolutePos(floor);
    BlockHitResult hit =
        new BlockHitResult(Vec3.atCenterOf(absolute), Direction.UP, absolute, false);
    player.setItemInHand(InteractionHand.MAIN_HAND, stack);
    stack.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hit));
  }

  private static List<RobotEntity> robots(GameTestHelper helper) {
    return helper.getEntities(CubitEntities.GENERAL_ROBOT.get());
  }

  private static Optional<UUID> follower(Player player) {
    return player.getData(CubitAttachments.FOLLOWING_ROBOT).robot();
  }

  private static RobotEntity reload(GameTestHelper helper, CompoundTag saved) {
    RobotEntity loaded = CubitEntities.GENERAL_ROBOT.get().create(helper.getLevel());
    loaded.load(saved);
    return loaded;
  }
}
