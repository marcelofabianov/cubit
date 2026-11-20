package io.github.zenndg.cubit.gametest;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.robot.RobotState;
import io.github.zenndg.cubit.domain.robot.SpeedChoice;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.entity.RobotEntity;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import io.github.zenndg.cubit.registry.CubitItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The speed control: the chosen percentage slows every walk, multiplies with the full inventory,
 * gives way in danger, answers only the owner and stays in the item and the save (answers 1259 and
 * 1266 to 1268).
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class SpeedGameTests {
  private static final String ARENA = "arena";
  private static final int SETTLE_TICKS = 5;
  private static final int FIRE_TICKS = 100;
  private static final BlockPos FLOOR = new BlockPos(4, 1, 2);
  private static final Energy PLENTY = new Energy(100_000);
  private static final float HEALTH = 25f;
  private static final int DOWN = -1;
  private static final int UP = 1;
  private static final int PRESSES_TO_EIGHTY = 2;
  private static final SpeedChoice EIGHTY = new SpeedChoice(80);
  private static final SpeedChoice SEVENTY = new SpeedChoice(70);
  private static final SpeedChoice OFF_THE_STEPS = new SpeedChoice(84);
  private static final double BASE_SPEED = 0.33;
  private static final double EIGHTY_SPEED = 0.264;
  private static final double EIGHTY_LOADED_SPEED = 0.198;
  private static final double SPEED_TOLERANCE = 1.0E-6;
  private static final String WRONG_SPEED = "Wrong movement speed";
  private static final String STRANGER_CHANGED = "Someone else changed the speed";
  private static final String NOT_KEPT = "The speed was not kept";
  private static final String NOT_FITTED = "The saved speed was not fitted to the steps";

  private SpeedGameTests() {}

  /** Two presses of − take the Cubit to 80% of the speed of its tier (answer 1259). */
  @GameTest(template = ARENA)
  public static void theChosenPercentageSlowsTheCubit(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = robot(helper, owner, state());
    pressDown(robot, owner);

    helper.runAfterDelay(SETTLE_TICKS, () -> assertSpeed(helper, robot, EIGHTY_SPEED));
  }

  /** The full inventory and the chosen speed multiply (answer 1268). */
  @GameTest(template = ARENA)
  public static void theFullInventoryMultipliesWithTheChoice(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = robot(helper, owner, state().withSpeed(EIGHTY));
    fillInventory(robot);

    helper.runAfterDelay(SETTLE_TICKS, () -> assertSpeed(helper, robot, EIGHTY_LOADED_SPEED));
  }

  /** On fire the Cubit flees at the whole speed of its tier (answer 1268). */
  @GameTest(template = ARENA)
  public static void onFireTheCubitFleesAtTheWholeSpeed(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = robot(helper, owner, state().withSpeed(EIGHTY));
    robot.igniteForTicks(FIRE_TICKS);

    helper.runAfterDelay(SETTLE_TICKS, () -> assertSpeed(helper, robot, BASE_SPEED));
  }

  /** Only the owner steps the speed (rule 20). */
  @GameTest(template = ARENA)
  public static void onlyTheOwnerChangesTheSpeed(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = robot(helper, owner, state());
    ServerPlayer stranger = GameTestRobots.onlinePlayer(helper);

    helper.assertFalse(robot.speed().step(stranger, DOWN), STRANGER_CHANGED);
    helper.assertValueEqual(robot.speed().choice(), SpeedChoice.FULL, STRANGER_CHANGED);
    helper.succeed();
  }

  /** + never goes above the whole speed of the tier (answer 1259). */
  @GameTest(template = ARENA)
  public static void plusStopsAtTheWholeSpeed(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = robot(helper, owner, state());
    robot.speed().step(owner, UP);

    helper.assertValueEqual(robot.speed().choice(), SpeedChoice.FULL, WRONG_SPEED);
    helper.succeed();
  }

  /** The choice stays in the save and in the item, and comes back when placed (1266). */
  @GameTest(template = ARENA)
  public static void theSpeedIsKeptInTheSaveAndInTheItem(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = robot(helper, owner, state().withSpeed(SEVENTY));

    CompoundTag saved = robot.saveWithoutId(new CompoundTag());
    RobotEntity loaded = GameTestRobots.loadRobot(helper, saved);
    helper.assertValueEqual(loaded.speed().choice(), SEVENTY, NOT_KEPT);
    ItemStack item = GameTestRobots.collect(owner, robot);
    helper.assertValueEqual(
        item.get(CubitDataComponents.ROBOT_STATE.get()).speed(), SEVENTY, NOT_KEPT);
    helper.succeed();
  }

  /** A value off the steps of the config goes to the nearest step when placed (answer 1266). */
  @GameTest(template = ARENA)
  public static void aSavedValueOffTheStepsIsFitted(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = robot(helper, owner, state().withSpeed(OFF_THE_STEPS));

    helper.assertValueEqual(robot.speed().choice(), EIGHTY, NOT_FITTED);
    helper.succeed();
  }

  private static void pressDown(RobotEntity robot, ServerPlayer owner) {
    for (int press = 0; press < PRESSES_TO_EIGHTY; press++) {
      robot.speed().step(owner, DOWN);
    }
  }

  private static RobotState state() {
    return new RobotState(RobotTier.BASE, PLENTY, HEALTH, false);
  }

  private static RobotEntity robot(GameTestHelper helper, ServerPlayer owner, RobotState state) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(CubitDataComponents.ROBOT_STATE.get(), state);
    return GameTestRobots.placeRobot(helper, owner, item, FLOOR);
  }

  private static void fillInventory(RobotEntity robot) {
    for (int slot = 0; slot < robot.inventory().getContainerSize(); slot++) {
      robot.inventory().setItem(slot, new ItemStack(Items.COBBLESTONE));
    }
  }

  private static void assertSpeed(GameTestHelper helper, RobotEntity robot, double expected) {
    double speed = robot.getAttributeValue(Attributes.MOVEMENT_SPEED);
    helper.assertTrue(Math.abs(speed - expected) < SPEED_TOLERANCE, WRONG_SPEED);
    helper.succeed();
  }
}
