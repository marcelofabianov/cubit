package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.item.EnergySlotContents;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The battery drains by walking, and redstone in the energy slot fills it without losing FE. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class EnergyGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 200;
  private static final int WALK_TICKS = 60;
  private static final int SETTLE_TICKS = 20;
  private static final int WAKE_TICKS = 45;
  private static final int FIRST_RECHARGE_TICKS = 3;
  private static final int FLOOR_Y = 1;
  private static final int START_X = 1;
  private static final int MIDDLE_Z = 2;
  private static final double FAR_X = 13.5;
  private static final double PUSH = 0.6;
  private static final double FAR_ENOUGH_TO_TELEPORT = 10;
  private static final double NO_MOVEMENT = 0.1;
  private static final float HEALTH = 25f;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final Energy PLENTY = new Energy(100_000);
  private static final Energy ALMOST_NOTHING = new Energy(15);
  private static final Energy SHORT_WALK = new Energy(120);
  private static final int FIRST_BLOCK_CHANGE_TICKS = 12;
  private static final int SECOND_BLOCK_CHANGE_TICKS = 13;
  private static final int EMPTY_BY_TICKS = 28;
  private static final int AFTER_DELAYED_ROUTE_TICKS = 30;
  private static final BlockPos BESIDE_ROUTE = new BlockPos(7, FLOOR_Y + 1, 4);
  private static final Energy WAKE_CHARGE = new Energy(15_000);
  private static final Energy FULL = new Energy(300_000);
  private static final int DUSTS_TO_WAKE = 3;
  private static final int SOME_DUSTS = 2;
  private static final int SLOT = 0;
  private static final long ONE_DUST_RESERVE = 5_000;
  private static final String DID_NOT_SPEND = "Walking did not spend energy";
  private static final String SPENT = "The robot spent energy without walking";
  private static final String WRONG_POWER = "Wrong power state";
  private static final String FOLLOWED = "The empty robot kept following";
  private static final String WRONG_BAR = "Wrong width of the item bar";
  private static final String NO_ROUTE = "The robot had no route when the block changed";
  private static final String LOST_LINK = "The empty robot lost the following link";
  private static final String NOT_CHARGED = "The redstone did not charge the battery";
  private static final String WASTED = "A full battery used redstone";
  private static final String HAND = "The redstone did not enter the slot";
  private static final String LOST = "The energy slot was lost";
  private static final String NO_REPAIR = "The repair recipe did not match";

  private EnergyGameTests() {}

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void walkingSpendsEnergy(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeWith(helper, owner, PLENTY);
    moveAway(helper, owner);

    helper.runAfterDelay(
        WALK_TICKS,
        () -> {
          helper.assertTrue(robot.state().energy().amount() < PLENTY.amount(), DID_NOT_SPEND);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void standingStillSpendsNothing(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = placeWith(helper, owner, PLENTY);

    helper.runAfterDelay(
        WALK_TICKS,
        () -> {
          helper.assertValueEqual(robot.state().energy(), PLENTY, SPENT);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void beingPushedSpendsNothing(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = placeWith(helper, owner, PLENTY);
    robot.setDeltaMovement(PUSH, 0, 0);

    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertValueEqual(robot.state().energy(), PLENTY, SPENT);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void anEmptyRobotStopsAndKeepsTheLink(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeWith(helper, owner, ALMOST_NOTHING);
    moveAway(helper, owner);

    helper.runAfterDelay(
        WALK_TICKS,
        () -> {
          helper.assertValueEqual(robot.state().energy(), Energy.ZERO, DID_NOT_SPEND);
          helper.assertValueEqual(robot.powerState(), PowerState.INACTIVE_EXHAUSTED, WRONG_POWER);
          helper.assertValueEqual(
              owner.getData(CubitAttachments.FOLLOWING_ROBOT).robot(),
              Optional.of(robot.getUUID()),
              LOST_LINK);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void anEmptyRobotDoesNotFollowNorTeleport(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeWith(helper, owner, ALMOST_NOTHING);
    moveAway(helper, owner);

    helper.runAfterDelay(
        WALK_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.INACTIVE_EXHAUSTED, WRONG_POWER);
          Vec3 stopped = robot.position();
          owner.moveTo(owner.getX() + FAR_ENOUGH_TO_TELEPORT, owner.getY(), owner.getZ());
          helper.runAfterDelay(
              SETTLE_TICKS,
              () -> {
                helper.assertTrue(robot.position().distanceTo(stopped) < NO_MOVEMENT, FOLLOWED);
                helper.succeed();
              });
        });
  }

  /** The game keeps a route update for later, and stopping must cancel it. */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aBlockChangeDoesNotRestartTheRouteOfAnEmptyRobot(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeWith(helper, owner, SHORT_WALK);
    moveAway(helper, owner);
    helper.runAfterDelay(
        FIRST_BLOCK_CHANGE_TICKS, () -> helper.setBlock(BESIDE_ROUTE, Blocks.STONE));
    helper.runAfterDelay(
        SECOND_BLOCK_CHANGE_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_POWER);
          helper.assertFalse(robot.getNavigation().isDone(), NO_ROUTE);
          helper.setBlock(BESIDE_ROUTE, Blocks.AIR);
        });
    helper.runAfterDelay(
        EMPTY_BY_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.INACTIVE_EXHAUSTED, WRONG_POWER);
          Vec3 stopped = robot.position();
          helper.runAfterDelay(
              AFTER_DELAYED_ROUTE_TICKS,
              () -> {
                helper.assertTrue(robot.position().distanceTo(stopped) < NO_MOVEMENT, FOLLOWED);
                helper.succeed();
              });
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void redstoneWakesAnEmptyRobot(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = placeWith(helper, owner, Energy.ZERO);
    helper.assertValueEqual(robot.powerState(), PowerState.INACTIVE_EXHAUSTED, WRONG_POWER);
    robot.energySlot().setItem(SLOT, new ItemStack(Items.REDSTONE, DUSTS_TO_WAKE));

    helper.runAfterDelay(
        WAKE_TICKS,
        () -> {
          helper.assertTrue(robot.state().energy().amount() >= WAKE_CHARGE.amount(), NOT_CHARGED);
          helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_POWER);
          helper.assertTrue(robot.energySlot().isEmpty(), NOT_CHARGED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aFullBatteryDoesNotUseRedstone(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = placeWith(helper, owner, FULL);
    robot.energySlot().setItem(SLOT, new ItemStack(Items.REDSTONE, SOME_DUSTS));

    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertValueEqual(robot.energySlot().getItem(SLOT).getCount(), SOME_DUSTS, WASTED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void clickingAnEmptyRobotWithRedstoneFillsTheSlot(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = placeWith(helper, owner, Energy.ZERO);
    owner.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.REDSTONE, SOME_DUSTS));

    robot.interact(owner, InteractionHand.MAIN_HAND);

    helper.assertValueEqual(robot.energySlot().getItem(SLOT).getCount(), SOME_DUSTS, HAND);
    helper.assertTrue(owner.getItemInHand(InteractionHand.MAIN_HAND).isEmpty(), HAND);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void collectingKeepsTheSlotAndTheReserve(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = placeWith(helper, owner, Energy.ZERO);
    robot.energySlot().setItem(SLOT, new ItemStack(Items.REDSTONE, SOME_DUSTS));

    helper.runAfterDelay(
        FIRST_RECHARGE_TICKS,
        () -> {
          long charged = robot.state().energy().amount();
          owner.setShiftKeyDown(true);
          robot.interact(owner, InteractionHand.MAIN_HAND);
          helper.assertTrue(robot.energySlot().isEmpty(), LOST);
          ItemStack item =
              owner.getInventory().items.stream()
                  .filter(stack -> stack.is(CubitItems.GENERAL_ROBOT.get()))
                  .findFirst()
                  .orElseThrow(() -> new IllegalStateException(LOST));
          EnergySlotContents saved = item.get(CubitDataComponents.ENERGY_SLOT.get());
          helper.assertTrue(saved != null, LOST);
          helper.assertValueEqual(saved.reserve() + charged, ONE_DUST_RESERVE, LOST);
          helper.assertValueEqual(
              saved.item().stream().findFirst().orElseThrow().getCount(), 1, LOST);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void deathAndRepairKeepTheSlot(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = placeWith(helper, owner, PLENTY);
    robot.energySlot().setItem(SLOT, new ItemStack(Items.REDSTONE, SOME_DUSTS));

    robot.kill();

    helper.assertTrue(robot.energySlot().isEmpty(), LOST);
    ItemStack damaged = helper.getEntities(EntityType.ITEM).getFirst().getItem();
    CraftingInput input =
        CraftingInput.of(2, 1, List.of(damaged, new ItemStack(CubitItems.TOOLBOX.get())));
    ItemStack repaired =
        helper
            .getLevel()
            .getRecipeManager()
            .getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel())
            .map(holder -> holder.value().assemble(input, helper.getLevel().registryAccess()))
            .orElseThrow(() -> new IllegalStateException(NO_REPAIR));
    EnergySlotContents saved = repaired.get(CubitDataComponents.ENERGY_SLOT.get());
    helper.assertTrue(saved != null, LOST);
    helper.assertValueEqual(
        saved.item().stream().findFirst().orElseThrow().getCount(), SOME_DUSTS, LOST);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void savingKeepsTheSlot(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = placeWith(helper, owner, PLENTY);
    robot.energySlot().setItem(SLOT, new ItemStack(Items.REDSTONE, SOME_DUSTS));
    CompoundTag saved = robot.saveWithoutId(new CompoundTag());

    RobotEntity loaded = CubitEntities.GENERAL_ROBOT.get().create(helper.getLevel());
    loaded.load(saved);

    helper.assertValueEqual(loaded.energySlot().getItem(SLOT).getCount(), SOME_DUSTS, LOST);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theItemBarIsEmptyOnlyWithoutEnergy(GameTestHelper helper) {
    helper.assertValueEqual(itemWith(ALMOST_NOTHING).getBarWidth(), 1, WRONG_BAR);
    helper.assertValueEqual(itemWith(Energy.ZERO).getBarWidth(), 0, WRONG_BAR);
    helper.succeed();
  }

  private static ItemStack itemWith(Energy energy) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(CubitDataComponents.ROBOT_STATE.get(), new RobotState(RobotTier.BASE, energy, HEALTH));
    return item;
  }

  private static RobotEntity placeWith(GameTestHelper helper, Player owner, Energy energy) {
    return GameTestRobots.placeRobot(helper, owner, itemWith(energy), FLOOR);
  }

  private static void moveAway(GameTestHelper helper, Player player) {
    Vec3 far = helper.absoluteVec(new Vec3(FAR_X, FLOOR_Y + 1, MIDDLE_Z));
    player.moveTo(far.x, far.y, far.z);
  }
}
