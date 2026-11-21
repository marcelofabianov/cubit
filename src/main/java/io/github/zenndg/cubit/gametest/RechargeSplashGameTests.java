package io.github.zenndg.cubit.gametest;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.behavior.RobotExpression;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.energy.PowerState;
import io.github.zenndg.cubit.domain.robot.RobotState;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.entity.RechargeSplashProjectile;
import io.github.zenndg.cubit.entity.RobotEntity;
import io.github.zenndg.cubit.entity.RobotFace;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import io.github.zenndg.cubit.registry.CubitItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The Recharge Splash thrown in the world: who receives, how much, how gradually and in which state
 * (energia.md, Recharge Splash, with the Base numbers of tiers.md).
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class RechargeSplashGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 300;
  private static final int MIDWAY_TICKS = 20;
  private static final int AFTER_TICKS = 90;
  private static final int SECOND_THROW_TICKS = 15;
  private static final int FLOOR_Y = 1;
  private static final int MIDDLE_Z = 2;
  private static final double DROP_HEIGHT = 2.5;
  private static final double CENTER = 0.5;
  private static final double FALL_SPEED = -0.5;
  private static final BlockPos IMPACT = new BlockPos(2, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos NEAR = new BlockPos(4, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos FAR = new BlockPos(12, FLOOR_Y, MIDDLE_Z);
  private static final Vec3 PIG_SPOT = new Vec3(3.5, 2, 1.5);
  private static final float HEALTH = 10f;
  private static final float HEALED = 15f;
  private static final float PIG_HEALTH = 5f;
  private static final double HEALTH_TOLERANCE = 1.0E-3;
  private static final Energy START = new Energy(100_000);
  private static final Energy ONE_DOSE = new Energy(145_000);
  private static final Energy TWO_DOSES = new Energy(190_000);
  private static final Energy DOSE = new Energy(45_000);
  private static final String WRONG_ENERGY = "Wrong energy after the splash";
  private static final String WRONG_HEALTH = "Wrong health after the splash: ";
  private static final String NOT_GRADUAL = "The splash should give its dose gradually: ";
  private static final String NOT_HAPPY = "The robot should be happy while it receives";
  private static final String WRONG_STATE = "Wrong power state after the splash";

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theRobotOfTheThrowerReceivesGradually(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = robotOf(helper, owner, IMPACT, START);
    throwAt(helper, owner, IMPACT);
    helper.runAfterDelay(
        MIDWAY_TICKS,
        () -> {
          long charge = robot.state().energy().amount();
          helper.assertTrue(
              charge > START.amount() && charge < ONE_DOSE.amount(), NOT_GRADUAL + charge);
          helper.assertValueEqual(RobotFace.expression(robot), RobotExpression.HAPPY, NOT_HAPPY);
        });
    helper.runAfterDelay(
        AFTER_TICKS,
        () -> {
          helper.assertValueEqual(robot.state().energy(), ONE_DOSE, WRONG_ENERGY);
          helper.assertTrue(
              Math.abs(robot.getHealth() - HEALED) < HEALTH_TOLERANCE,
              WRONG_HEALTH + robot.getHealth());
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void otherOwnersFarRobotsAndMobsGetNothing(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity far = robotOf(helper, owner, FAR, START);
    RobotEntity other = robotOf(helper, helper.makeMockPlayer(GameType.SURVIVAL), NEAR, START);
    Pig pig = helper.spawn(EntityType.PIG, PIG_SPOT);
    pig.setHealth(PIG_HEALTH);
    throwAt(helper, owner, IMPACT);
    helper.runAfterDelay(
        AFTER_TICKS,
        () -> {
          helper.assertValueEqual(far.state().energy(), START, WRONG_ENERGY);
          helper.assertValueEqual(other.state().energy(), START, WRONG_ENERGY);
          helper.assertValueEqual(pig.getHealth(), PIG_HEALTH, WRONG_HEALTH);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aSecondSplashWhileReceivingGivesNothing(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = robotOf(helper, owner, IMPACT, START);
    throwAt(helper, owner, IMPACT);
    helper.runAfterDelay(SECOND_THROW_TICKS, () -> throwAt(helper, owner, IMPACT));
    helper.runAfterDelay(
        AFTER_TICKS + SECOND_THROW_TICKS,
        () -> {
          helper.assertValueEqual(robot.state().energy(), ONE_DOSE, WRONG_ENERGY);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aSplashAfterTheDoseEndsGivesAgain(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = robotOf(helper, owner, IMPACT, START);
    throwAt(helper, owner, IMPACT);
    helper.runAfterDelay(AFTER_TICKS, () -> throwAt(helper, owner, IMPACT));
    helper.runAfterDelay(
        AFTER_TICKS + AFTER_TICKS,
        () -> {
          helper.assertValueEqual(robot.state().energy(), TWO_DOSES, WRONG_ENERGY);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotWithoutChargeWakes(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = robotOf(helper, owner, IMPACT, Energy.ZERO);
    helper.assertValueEqual(robot.powerState(), PowerState.INACTIVE_EXHAUSTED, WRONG_STATE);
    throwAt(helper, owner, IMPACT);
    helper.runAfterDelay(
        AFTER_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_STATE);
          helper.assertValueEqual(robot.state().energy(), DOSE, WRONG_ENERGY);
          helper.succeed();
        });
  }

  /** The second robot of an owner is placed switched off, as in the consumption tests. */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aSwitchedOffRobotReceivesAndStaysOff(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    robotOf(helper, owner, FAR, START);
    RobotEntity robot = robotOf(helper, owner, IMPACT, START);
    helper.assertValueEqual(robot.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_STATE);
    throwAt(helper, owner, IMPACT);
    helper.runAfterDelay(
        AFTER_TICKS,
        () -> {
          helper.assertValueEqual(
              robot.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_STATE);
          helper.assertValueEqual(robot.state().energy(), ONE_DOSE, WRONG_ENERGY);
          helper.succeed();
        });
  }

  private static RobotEntity robotOf(
      GameTestHelper helper, Player owner, BlockPos floor, Energy energy) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(
        CubitDataComponents.ROBOT_STATE.get(),
        new RobotState(RobotTier.BASE, energy, HEALTH, false));
    return GameTestRobots.placeRobot(helper, owner, item, floor);
  }

  /** Dropped straight down over the floor block, so it breaks there a few ticks later. */
  private static void throwAt(GameTestHelper helper, Player thrower, BlockPos floor) {
    RechargeSplashProjectile flask = new RechargeSplashProjectile(helper.getLevel(), thrower);
    Vec3 at =
        helper.absoluteVec(
            new Vec3(floor.getX() + CENTER, floor.getY() + DROP_HEIGHT, floor.getZ() + CENTER));
    flask.setPos(at);
    flask.setDeltaMovement(0, FALL_SPEED, 0);
    helper.getLevel().addFreshEntity(flask);
  }
}
