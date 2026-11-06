package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Reads the energy spent tick by tick, so each situation shows its exact price (tiers.md). */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ConsumptionGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 200;
  private static final int SAMPLE_TICKS = 60;
  private static final int SETTLE_TICKS = 5;
  private static final int FLOOR_Y = 1;
  private static final int STAND_Y = 2;
  private static final int START_X = 1;
  private static final int MIDDLE_Z = 2;
  private static final int ARENA_LENGTH = 23;
  private static final int ARENA_WIDTH = 5;
  private static final int STEP_X = 6;
  private static final double FAR_X = 13.5;
  private static final float HEALTH = 25f;
  private static final int EFFECT_TICKS = 100;
  private static final int FIRE_TICKS = 100;
  private static final long WALK = 15;
  private static final long WALK_LOADED = 20;
  private static final long WALK_AND_CLIMB = 30;
  private static final long DRAIN = 25;
  private static final double BASE_SPEED = 0.33;
  private static final double LOADED_SPEED = 0.2475;
  private static final double SPEED_TOLERANCE = 1.0E-6;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final Energy PLENTY = new Energy(100_000);
  private static final String WRONG_COST = "Wrong energy spent per tick: ";
  private static final String WRONG_SPEED = "Wrong movement speed";
  private static final String NOT_ACTIVE = "The robot should be active";

  private ConsumptionGameTests() {}

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void walkingOnPlainGroundCostsTheBaseConsumption(GameTestHelper helper) {
    RobotEntity robot = walkingRobot(helper);
    assertHighestCost(helper, robot, WALK);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void walkingWithAFullInventoryCostsMore(GameTestHelper helper) {
    RobotEntity robot = walkingRobot(helper);
    fillInventory(robot);
    assertHighestCost(helper, robot, WALK_LOADED);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void anInventoryWithAFreeSlotIsNotFull(GameTestHelper helper) {
    RobotEntity robot = walkingRobot(helper);
    fillInventory(robot);
    robot.inventory().setItem(0, ItemStack.EMPTY);
    assertHighestCost(helper, robot, WALK);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void walkingInWaterCostsMore(GameTestHelper helper) {
    fillArenaWithWater(helper);
    RobotEntity robot = walkingRobot(helper);
    assertHighestCost(helper, robot, WALK_LOADED);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void climbingABlockCostsMoreOnceAndGoingDownReturnsNothing(GameTestHelper helper) {
    for (int z = 0; z < ARENA_WIDTH; z++) {
      helper.setBlock(new BlockPos(STEP_X, STAND_Y, z), Blocks.STONE);
    }
    RobotEntity robot = walkingRobot(helper);
    List<Long> costs = new ArrayList<>();
    sampleCosts(
        helper,
        robot,
        costs,
        () -> {
          long highest = costs.stream().mapToLong(Long::longValue).max().orElse(0);
          long climbs = costs.stream().filter(cost -> cost == WALK_AND_CLIMB).count();
          helper.assertTrue(highest == WALK_AND_CLIMB && climbs == 1, WRONG_COST + costs);
          helper.assertTrue(
              costs.stream().allMatch(cost -> cost == 0 || cost == WALK || cost == WALK_AND_CLIMB),
              WRONG_COST + costs);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aHarmfulEffectDrainsEvenStandingStill(GameTestHelper helper) {
    RobotEntity robot = standingRobot(helper);
    robot.addEffect(new MobEffectInstance(MobEffects.POISON, EFFECT_TICKS));
    assertEveryCost(helper, robot, DRAIN);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void severalConditionsDrainOnlyOnce(GameTestHelper helper) {
    RobotEntity robot = standingRobot(helper);
    robot.addEffect(new MobEffectInstance(MobEffects.POISON, EFFECT_TICKS));
    robot.addEffect(new MobEffectInstance(MobEffects.WITHER, EFFECT_TICKS));
    assertEveryCost(helper, robot, DRAIN);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aHarmlessEffectDoesNotDrain(GameTestHelper helper) {
    RobotEntity robot = standingRobot(helper);
    robot.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, EFFECT_TICKS));
    assertEveryCost(helper, robot, 0);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void fireDrainsAtLeastTheExtra(GameTestHelper helper) {
    RobotEntity robot = standingRobot(helper);
    robot.igniteForTicks(FIRE_TICKS);
    List<Long> costs = new ArrayList<>();
    sampleCosts(
        helper,
        robot,
        costs,
        () -> {
          helper.assertTrue(costs.stream().allMatch(cost -> cost >= DRAIN), WRONG_COST + costs);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotSwitchedOffDoesNotDrain(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    standingRobotOf(helper, owner);
    RobotEntity robot = placeWith(helper, owner, new BlockPos(START_X, FLOOR_Y, MIDDLE_Z + 2));
    helper.assertValueEqual(robot.powerState(), PowerState.INACTIVE_SWITCHED_OFF, NOT_ACTIVE);
    robot.addEffect(new MobEffectInstance(MobEffects.POISON, EFFECT_TICKS));
    assertEveryCost(helper, robot, 0);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aFullInventorySlowsTheRobot(GameTestHelper helper) {
    RobotEntity robot = standingRobot(helper);
    fillInventory(robot);
    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          assertSpeed(helper, robot, LOADED_SPEED);
          robot.inventory().setItem(0, ItemStack.EMPTY);
          helper.runAfterDelay(
              SETTLE_TICKS,
              () -> {
                assertSpeed(helper, robot, BASE_SPEED);
                helper.succeed();
              });
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aFullInventoryDoesNotSlowARobotOnFire(GameTestHelper helper) {
    RobotEntity robot = standingRobot(helper);
    fillInventory(robot);
    robot.igniteForTicks(FIRE_TICKS);
    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          assertSpeed(helper, robot, BASE_SPEED);
          helper.succeed();
        });
  }

  private static void assertSpeed(GameTestHelper helper, RobotEntity robot, double expected) {
    double speed = robot.getAttributeValue(Attributes.MOVEMENT_SPEED);
    helper.assertTrue(Math.abs(speed - expected) < SPEED_TOLERANCE, WRONG_SPEED);
  }

  private static void assertHighestCost(GameTestHelper helper, RobotEntity robot, long expected) {
    List<Long> costs = new ArrayList<>();
    sampleCosts(
        helper,
        robot,
        costs,
        () -> {
          long highest = costs.stream().mapToLong(Long::longValue).max().orElse(0);
          helper.assertTrue(highest == expected, WRONG_COST + costs);
          helper.succeed();
        });
  }

  private static void assertEveryCost(GameTestHelper helper, RobotEntity robot, long expected) {
    List<Long> costs = new ArrayList<>();
    sampleCosts(
        helper,
        robot,
        costs,
        () -> {
          helper.assertTrue(costs.stream().allMatch(cost -> cost == expected), WRONG_COST + costs);
          helper.succeed();
        });
  }

  /** The first reading only sets the start, so each entry is the cost of exactly one tick. */
  private static void sampleCosts(
      GameTestHelper helper, RobotEntity robot, List<Long> costs, Runnable finish) {
    long[] last = {-1};
    for (int tick = 1; tick <= SAMPLE_TICKS; tick++) {
      helper.runAfterDelay(
          tick,
          () -> {
            long now = robot.state().energy().amount();
            if (last[0] >= 0) {
              costs.add(last[0] - now);
            }
            last[0] = now;
          });
    }
    helper.runAfterDelay(SAMPLE_TICKS + 1, finish::run);
  }

  private static void fillInventory(RobotEntity robot) {
    for (int slot = 0; slot < robot.inventory().getContainerSize(); slot++) {
      robot.inventory().setItem(slot, new ItemStack(Items.COBBLESTONE));
    }
  }

  private static void fillArenaWithWater(GameTestHelper helper) {
    for (int x = 0; x < ARENA_LENGTH; x++) {
      for (int z = 0; z < ARENA_WIDTH; z++) {
        helper.setBlock(new BlockPos(x, STAND_Y, z), Blocks.WATER);
      }
    }
  }

  private static RobotEntity walkingRobot(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeWith(helper, owner, FLOOR);
    Vec3 far = helper.absoluteVec(new Vec3(FAR_X, STAND_Y, MIDDLE_Z));
    owner.moveTo(far.x, far.y, far.z);
    return robot;
  }

  private static RobotEntity standingRobot(GameTestHelper helper) {
    return standingRobotOf(helper, helper.makeMockPlayer(GameType.SURVIVAL));
  }

  private static RobotEntity standingRobotOf(GameTestHelper helper, Player owner) {
    return placeWith(helper, owner, FLOOR);
  }

  private static RobotEntity placeWith(GameTestHelper helper, Player owner, BlockPos floor) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(
        CubitDataComponents.ROBOT_STATE.get(),
        new RobotState(RobotTier.BASE, PLENTY, HEALTH, false));
    return GameTestRobots.placeRobot(helper, owner, item, floor);
  }
}
