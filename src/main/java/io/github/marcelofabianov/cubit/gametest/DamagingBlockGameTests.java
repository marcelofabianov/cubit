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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** A robot leaves the blocks that hurt it, under the same conditions that hurt a player. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DamagingBlockGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 200;
  private static final int LEAVE_TICKS = 50;
  private static final int STAY_TICKS = 40;
  private static final int SAMPLE_TICKS = 50;
  private static final int FOLLOW_TICKS = 90;
  private static final int FLOOR_Y = 1;
  private static final int STAND_Y = 2;
  private static final int START_X = 4;
  private static final int MIDDLE_Z = 2;
  private static final int NEXT_Z = 4;
  private static final int EFFECT_TICKS = 100;
  private static final double FAR_X = 18.5;
  private static final double FOLLOWED_BLOCKS = 3;
  private static final double NO_MOVEMENT = 0.1;
  private static final double INTO_THE_CACTUS = 0.3;
  private static final double CENTER = 0.5;
  private static final float HEALTH = 25f;
  private static final long WALK_AND_DRAIN = 40;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos STAND = FLOOR.above();
  private static final BlockPos BESIDE = STAND.east();
  private static final BlockPos BESIDE_FLOOR = FLOOR.east();
  private static final Energy PLENTY = new Energy(100_000);
  private static final String STAYED = "The robot stayed on the damaging block";
  private static final String MOVED = "The robot moved without being hurt by a block";
  private static final String WRONG_POWER = "Wrong power state";
  private static final String CHARGED_FOR_NEIGHBOUR = "A block next to the robot cost energy";
  private static final String DID_NOT_FOLLOW = "The robot did not go back to following";
  private static final String WRONG_COST = "Wrong energy spent per tick: ";

  private DamagingBlockGameTests() {}

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotLeavesMagma(GameTestHelper helper) {
    RobotEntity robot = standingRobot(helper);
    helper.setBlock(FLOOR, Blocks.MAGMA_BLOCK);
    assertLeaves(helper, robot, robot.position());
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotLeavesAWitherRose(GameTestHelper helper) {
    RobotEntity robot = standingRobot(helper);
    helper.setBlock(FLOOR, Blocks.DIRT);
    helper.setBlock(STAND, Blocks.WITHER_ROSE);
    assertLeaves(helper, robot, robot.position());
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotLeavesACactusItTouches(GameTestHelper helper) {
    RobotEntity robot = standingRobot(helper);
    // Sand falls without support, and the cactus breaks without the sand.
    helper.setBlock(BESIDE_FLOOR.below(), Blocks.STONE);
    helper.setBlock(BESIDE_FLOOR, Blocks.SAND);
    helper.setBlock(BESIDE, Blocks.CACTUS);
    Vec3 pressed = robot.position().add(INTO_THE_CACTUS, 0, 0);
    robot.moveTo(pressed.x, pressed.y, pressed.z);
    helper.runAfterDelay(STAY_TICKS, () -> helper.assertBlockPresent(Blocks.CACTUS, BESIDE));
    assertLeaves(helper, robot, pressed);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aBlockOnlyNextToTheRobotDoesNotCount(GameTestHelper helper) {
    RobotEntity robot = standingRobot(helper);
    helper.setBlock(BESIDE_FLOOR, Blocks.MAGMA_BLOCK);
    helper.runAfterDelay(
        STAY_TICKS,
        () -> helper.assertValueEqual(robot.state().energy(), PLENTY, CHARGED_FOR_NEIGHBOUR));
    assertStays(helper, robot);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotSwitchedOffStaysOnMagma(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    placeWith(helper, owner, new BlockPos(START_X, FLOOR_Y, NEXT_Z));
    RobotEntity robot = placeWith(helper, owner, FLOOR);
    helper.assertValueEqual(robot.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_POWER);
    helper.setBlock(FLOOR, Blocks.MAGMA_BLOCK);
    assertStays(helper, robot);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void magmaCostsTheExtraEnergy(GameTestHelper helper) {
    RobotEntity robot = standingRobot(helper);
    helper.setBlock(FLOOR, Blocks.MAGMA_BLOCK);
    assertHighestCost(helper, robot, WALK_AND_DRAIN);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void magmaAndAnEffectChargeOnlyOneExtra(GameTestHelper helper) {
    RobotEntity robot = standingRobot(helper);
    helper.setBlock(FLOOR, Blocks.MAGMA_BLOCK);
    robot.addEffect(new MobEffectInstance(MobEffects.POISON, EFFECT_TICKS));
    assertHighestCost(helper, robot, WALK_AND_DRAIN);
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotGoesBackToFollowingAfterLeaving(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = placeWith(helper, owner, FLOOR);
    Vec3 start = robot.position();
    helper.setBlock(FLOOR, Blocks.MAGMA_BLOCK);
    Vec3 far = helper.absoluteVec(new Vec3(FAR_X, STAND_Y, MIDDLE_Z + CENTER));
    owner.moveTo(far.x, far.y, far.z);
    helper.runAfterDelay(
        FOLLOW_TICKS,
        () -> {
          helper.assertTrue(robot.getX() - start.x > FOLLOWED_BLOCKS, DID_NOT_FOLLOW);
          helper.succeed();
        });
  }

  private static void assertLeaves(GameTestHelper helper, RobotEntity robot, Vec3 start) {
    helper.runAfterDelay(
        LEAVE_TICKS,
        () -> {
          helper.assertTrue(robot.position().distanceTo(start) > NO_MOVEMENT, STAYED);
          helper.assertFalse(
              helper.getLevel().getBlockState(robot.getOnPos()).is(Blocks.MAGMA_BLOCK), STAYED);
          helper.succeed();
        });
  }

  private static void assertStays(GameTestHelper helper, RobotEntity robot) {
    Vec3 start = robot.position();
    helper.runAfterDelay(
        STAY_TICKS,
        () -> {
          helper.assertTrue(robot.position().distanceTo(start) < NO_MOVEMENT, MOVED);
          helper.succeed();
        });
  }

  private static void assertHighestCost(GameTestHelper helper, RobotEntity robot, long expected) {
    List<Long> costs = new ArrayList<>();
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
    helper.runAfterDelay(
        SAMPLE_TICKS + 1,
        () -> {
          long highest = costs.stream().mapToLong(Long::longValue).max().orElse(0);
          helper.assertTrue(highest == expected, WRONG_COST + costs);
          helper.succeed();
        });
  }

  private static RobotEntity standingRobot(GameTestHelper helper) {
    return placeWith(helper, helper.makeMockPlayer(GameType.SURVIVAL), FLOOR);
  }

  private static RobotEntity placeWith(GameTestHelper helper, Player owner, BlockPos floor) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(
        CubitDataComponents.ROBOT_STATE.get(),
        new RobotState(RobotTier.BASE, PLENTY, HEALTH, false));
    return GameTestRobots.placeRobot(helper, owner, item, floor);
  }
}
