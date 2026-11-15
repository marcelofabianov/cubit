package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.domain.behavior.HibernationReason;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.AreaCorner;
import io.github.marcelofabianov.cubit.domain.robot.RobotJob;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.robot.WorkArea;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotFollowCall;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The base of the jobs: the card slot, the jobs of each tier and the hibernation away from the
 * Dock, with Resume, Follow and the card taken out (answers 1161 to 1175).
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class JobBaseGameTests {
  private static final String ARENA = "arena";
  private static final String EMPTY_TEMPLATE = "empty";
  private static final int TIMEOUT = 400;
  private static final int FLOOR_Y = 1;
  private static final BlockPos ROBOT_FLOOR = new BlockPos(4, FLOOR_Y, 2);
  private static final BlockPos FIRST_CORNER = new BlockPos(14, FLOOR_Y, 0);
  private static final BlockPos SECOND_CORNER = new BlockPos(20, FLOOR_Y, 4);
  private static final Energy CHARGE = new Energy(300_000);
  private static final float HEALTH = 50f;
  private static final int STACK = 64;
  private static final int CARD_SLOT = 0;
  private static final String POWER_STATE_KEY = "PowerState";
  private static final String REASON_KEY = "HibernationReason";
  private static final String UNKNOWN_REASON = "no_lava";
  private static final String WRONG_JOBS = "Wrong jobs for the tier";
  private static final String WRONG_SLOT = "The card slot took the wrong item";
  private static final String NOT_KEPT = "The card was not kept in the item";
  private static final String NOT_ASLEEP = "The Cubit should hibernate away from the Dock";
  private static final String STILL_ASLEEP = "The Cubit should be awake";
  private static final String WRONG_REASON = "Wrong reason to hibernate";

  private JobBaseGameTests() {}

  /** Base none, then each tier adds its jobs (answers 1145 and 1163). */
  @GameTest(template = EMPTY_TEMPLATE)
  public static void eachTierReadsItsJobsFromTheConfig(GameTestHelper helper) {
    helper.assertTrue(jobsOf(RobotTier.BASE).isEmpty(), WRONG_JOBS);
    helper.assertValueEqual(
        jobsOf(RobotTier.ADVANCED),
        Set.of(RobotJob.FISHER, RobotJob.FARMER, RobotJob.RANCHER),
        WRONG_JOBS);
    helper.assertTrue(jobsOf(RobotTier.ELITE).contains(RobotJob.MINER), WRONG_JOBS);
    helper.assertValueEqual(
        jobsOf(RobotTier.ULTIMATE).size(), RobotJob.values().length, WRONG_JOBS);
    helper.succeed();
  }

  /** Only a card of a job the tier takes, and nothing on Base (answer 1162). */
  @GameTest(template = ARENA)
  public static void theCardSlotTakesOnlyACardTheTierAccepts(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    Container base = robot(helper, owner, RobotTier.BASE).workArea().work().slot();
    Container advanced = robot(helper, owner, RobotTier.ADVANCED).workArea().work().slot();

    helper.assertFalse(base.canPlaceItem(CARD_SLOT, card()), WRONG_SLOT);
    helper.assertTrue(advanced.canPlaceItem(CARD_SLOT, card()), WRONG_SLOT);
    helper.assertFalse(advanced.canPlaceItem(CARD_SLOT, new ItemStack(Items.STONE)), WRONG_SLOT);
    helper.succeed();
  }

  /** Picking the Cubit up keeps the card in the item (answers 191 and 1162). */
  @GameTest(template = ARENA)
  public static void theCardStaysInTheItem(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = robot(helper, owner, RobotTier.ADVANCED);
    robot.workArea().work().slot().setItem(CARD_SLOT, card());

    ItemStack item = GameTestRobots.collect(owner, robot);

    ItemContainerContents kept = item.get(CubitDataComponents.CARD_SLOT.get());
    helper.assertTrue(
        kept != null && kept.stream().anyMatch(stack -> stack.is(CubitItems.FISHER_CARD.get())),
        NOT_KEPT);
    helper.succeed();
  }

  /** With a card, in its area, full and with no chest, it falls asleep (answer 1165). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aFullCubitWithACardHibernatesAway(GameTestHelper helper) {
    RobotEntity robot = fullWorker(helper, GameTestRobots.onlinePlayer(helper));

    helper.succeedWhen(() -> {
      helper.assertValueEqual(robot.powerState(), PowerState.HIBERNATING_AWAY, NOT_ASLEEP);
      helper.assertValueEqual(
          robot.workArea().work().hibernation(),
          Optional.of(HibernationReason.INVENTORY_FULL),
          WRONG_REASON);
    });
  }

  /** Resume with room wakes it, and with the reason still there it sleeps again (answer 253). */
  @GameTest(template = ARENA)
  public static void resumeWakesOnlyWhenTheReasonIsGone(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity full = asleep(helper, fullWorker(helper, owner));
    RobotEntity empty = asleep(helper, worker(helper, owner));

    helper.assertTrue(full.workArea().work().resume(owner), NOT_ASLEEP);
    helper.assertTrue(empty.workArea().work().resume(owner), NOT_ASLEEP);

    helper.assertValueEqual(full.powerState(), PowerState.HIBERNATING_AWAY, NOT_ASLEEP);
    helper.assertValueEqual(empty.powerState(), PowerState.ACTIVE, STILL_ASLEEP);
    helper.succeed();
  }

  /** An unknown reason in the save is the full inventory (answer 1175 and rule 17). */
  @GameTest(template = ARENA)
  public static void anUnknownReasonReadsAsTheFullInventory(GameTestHelper helper) {
    RobotEntity robot = worker(helper, GameTestRobots.onlinePlayer(helper));
    CompoundTag saved = robot.saveWithoutId(new CompoundTag());
    saved.putString(POWER_STATE_KEY, PowerState.HIBERNATING_AWAY.name());
    saved.putString(REASON_KEY, UNKNOWN_REASON);

    RobotEntity loaded = GameTestRobots.loadRobot(helper, saved);

    helper.assertValueEqual(
        loaded.workArea().work().hibernation(),
        Optional.of(HibernationReason.INVENTORY_FULL),
        WRONG_REASON);
    helper.succeed();
  }

  /** Follow wakes the Cubit and takes it out of its area (answer 1170). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void followWakesAHibernatingCubit(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = fullWorker(helper, owner);

    helper
        .startSequence()
        .thenWaitUntil(() -> helper.assertTrue(isAsleep(robot), NOT_ASLEEP))
        .thenExecute(() -> helper.assertTrue(new RobotFollowCall(robot).call(owner), NOT_ASLEEP))
        .thenExecute(
            () -> helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, STILL_ASLEEP))
        .thenSucceed();
  }

  /** Without the card there is no job, so the sleep ends (answer 1171). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void takingTheCardOutWakesIt(GameTestHelper helper) {
    RobotEntity robot = fullWorker(helper, GameTestRobots.onlinePlayer(helper));

    helper
        .startSequence()
        .thenWaitUntil(() -> helper.assertTrue(isAsleep(robot), NOT_ASLEEP))
        .thenExecute(() -> robot.workArea().work().slot().removeItemNoUpdate(CARD_SLOT))
        .thenWaitUntil(
            () -> helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, STILL_ASLEEP))
        .thenSucceed();
  }

  private static Set<RobotJob> jobsOf(RobotTier tier) {
    return CubitMod.CONFIG.tierCatalog().spec(CubitConfig.GENERAL_ROBOT, tier).jobs().allowed();
  }

  private static RobotEntity robot(GameTestHelper helper, ServerPlayer owner, RobotTier tier) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(CubitDataComponents.ROBOT_STATE.get(), new RobotState(tier, CHARGE, HEALTH));
    return GameTestRobots.placeRobot(helper, owner, item, ROBOT_FLOOR);
  }

  /** An Advanced Cubit with the Fisher card, in its area, with an empty inventory. */
  private static RobotEntity worker(GameTestHelper helper, ServerPlayer owner) {
    RobotEntity robot = robot(helper, owner, RobotTier.ADVANCED);
    robot.workArea().work().slot().setItem(CARD_SLOT, card());
    robot.workArea().assign(area(helper, robot), owner);
    return robot;
  }

  /** The same worker with every slot taken and no marked chest. */
  private static RobotEntity fullWorker(GameTestHelper helper, ServerPlayer owner) {
    RobotEntity robot = worker(helper, owner);
    Container inventory = robot.inventory();
    for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
      inventory.setItem(slot, new ItemStack(Items.COBBLESTONE, STACK));
    }
    return robot;
  }

  /** The robot read back from a save that hibernates away from the Dock. */
  private static RobotEntity asleep(GameTestHelper helper, RobotEntity robot) {
    CompoundTag saved = robot.saveWithoutId(new CompoundTag());
    saved.putString(POWER_STATE_KEY, PowerState.HIBERNATING_AWAY.name());
    return GameTestRobots.loadRobot(helper, saved);
  }

  private static boolean isAsleep(RobotEntity robot) {
    return robot.powerState() == PowerState.HIBERNATING_AWAY;
  }

  private static WorkArea area(GameTestHelper helper, RobotEntity robot) {
    BlockPos first = helper.absolutePos(FIRST_CORNER);
    BlockPos second = helper.absolutePos(SECOND_CORNER);
    return WorkArea.between(
        new AreaCorner(first.getX(), first.getY(), first.getZ()),
        new AreaCorner(second.getX(), second.getY(), second.getZ()),
        robot.level().dimension().location().toString());
  }

  private static ItemStack card() {
    return new ItemStack(CubitItems.FISHER_CARD.get());
  }
}
