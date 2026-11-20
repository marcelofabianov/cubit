package io.github.zenndg.cubit.gametest;

import com.mojang.serialization.DataResult;
import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.config.CubitConfig;
import io.github.zenndg.cubit.domain.behavior.DeathGuideCall;
import io.github.zenndg.cubit.domain.behavior.DeathLog;
import io.github.zenndg.cubit.domain.behavior.DeathRecord;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.robot.RobotState;
import io.github.zenndg.cubit.domain.tier.DeathPointLevel;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.entity.RobotDeathGuide;
import io.github.zenndg.cubit.entity.RobotEntity;
import io.github.zenndg.cubit.network.DeathGuidePayload;
import io.github.zenndg.cubit.network.DeleteDeathPayload;
import io.github.zenndg.cubit.player.OwnerDeaths;
import io.github.zenndg.cubit.player.SavedDeathLog;
import io.github.zenndg.cubit.player.TabletDeaths;
import io.github.zenndg.cubit.player.TabletSelection;
import io.github.zenndg.cubit.registry.CubitAttachments;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The death point: every death of the player goes to the log, and from Elite on the Cubit that
 * follows walks the owner to the spot, which a trip to a Dock cancels (answers 1109 to 1129).
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DeathPointGameTests {
  private static final String ARENA = "arena";
  private static final String EMPTY_TEMPLATE = "empty";
  private static final int TIMEOUT = 400;
  private static final int FLOOR_Y = 1;
  private static final BlockPos ROBOT_FLOOR = new BlockPos(4, FLOOR_Y, 2);
  private static final BlockPos OWNER_SPOT = new BlockPos(3, FLOOR_Y + 1, 2);
  private static final BlockPos DEATH_SPOT = new BlockPos(11, FLOOR_Y + 1, 2);
  private static final Energy CHARGE = new Energy(300_000);
  private static final float HEALTH = 50f;
  private static final double ARRIVAL_BLOCKS = 3.5;
  private static final String CAUSE = "Steve fell";
  private static final int X = 10;
  private static final int Y = 64;
  private static final int Z = -20;
  private static final String NETHER = "minecraft:the_nether";
  private static final long FIRST_DAY_TIME = 1_000L;
  private static final String BAD_DIMENSION = " ";
  private static final String DIMENSION_FIELD = "Dimension";
  private static final String NOT_RECORDED = "The death was not recorded";
  private static final String WRONG_LOG = "The death log is wrong";
  private static final String WRONG_LEVEL = "Wrong death point for the tier";
  private static final String WRONG_CALL = "The guide gave the wrong answer";
  private static final String NOT_GUIDED = "The Cubit did not guide the owner to the spot";
  private static final String STILL_GUIDING = "The guide should be over";
  private static final String GUIDED = "The request should not start the guide";
  private static final String NOT_KEPT = "The death point switch was not kept";
  private static final String WRONG_DEFAULT = "The death point switch should start on";

  private DeathPointGameTests() {}

  /** Base locked, Advanced marks, Elite and Ultimate guide (answers 1117 and 1129). */
  @GameTest(template = EMPTY_TEMPLATE)
  public static void eachTierReadsItsDeathPointFromTheConfig(GameTestHelper helper) {
    helper.assertValueEqual(levelOf(RobotTier.BASE), DeathPointLevel.LOCKED, WRONG_LEVEL);
    helper.assertValueEqual(levelOf(RobotTier.ADVANCED), DeathPointLevel.MARK, WRONG_LEVEL);
    helper.assertValueEqual(levelOf(RobotTier.ELITE), DeathPointLevel.GUIDE, WRONG_LEVEL);
    helper.assertValueEqual(levelOf(RobotTier.ULTIMATE), DeathPointLevel.GUIDE, WRONG_LEVEL);
    helper.succeed();
  }

  /**
   * A death goes to the log with the time of the world, and past the limit the oldest one leaves.
   * The clock of the world is never changed here, since other tests read it (answer 1115).
   */
  @GameTest(template = EMPTY_TEMPLATE)
  public static void everyDeathGoesToTheLogUpToTheLimit(GameTestHelper helper) {
    ServerPlayer player = GameTestRobots.onlinePlayer(helper);
    int limit = CubitMod.CONFIG.deathLogSize();
    DeathLog full = DeathLog.EMPTY;
    for (int death = 0; death < limit; death++) {
      full = full.record(new DeathRecord(death, X, Y, Z, DeathRecord.OVERWORLD, CAUSE), limit);
    }
    player.setData(CubitAttachments.DEATH_LOG, full);

    OwnerDeaths.record(player, Component.literal(CAUSE));

    DeathLog log = player.getData(CubitAttachments.DEATH_LOG);
    DeathRecord newest = log.latest().orElseThrow();
    helper.assertValueEqual(log.deaths().size(), limit, NOT_RECORDED);
    helper.assertValueEqual(newest.dayTime(), helper.getLevel().getDayTime(), WRONG_LOG);
    helper.assertValueEqual(newest.dimension(), DeathRecord.OVERWORLD, WRONG_LOG);
    helper.assertFalse(log.deaths().contains(full.deaths().getLast()), WRONG_LOG);
    helper.succeed();
  }

  /** The saved log reads back, and a death that cannot be read is skipped (rule 17). */
  @GameTest(template = EMPTY_TEMPLATE)
  public static void theLogIsSavedAndABadDeathIsSkipped(GameTestHelper helper) {
    DeathRecord death = new DeathRecord(FIRST_DAY_TIME, X, Y, Z, DeathRecord.OVERWORLD, CAUSE);
    DeathLog log = DeathLog.EMPTY.record(death, CubitMod.CONFIG.deathLogSize());
    Tag saved = SavedDeathLog.CODEC.encodeStart(NbtOps.INSTANCE, log).getOrThrow();
    ListTag withBad = ((ListTag) saved).copy();
    CompoundTag bad = withBad.getCompound(0).copy();
    bad.putString(DIMENSION_FIELD, BAD_DIMENSION);
    withBad.add(bad);

    DataResult<DeathLog> read = SavedDeathLog.CODEC.parse(NbtOps.INSTANCE, withBad);

    helper.assertValueEqual(read.getOrThrow(), log, WRONG_LOG);
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void anEliteCubitWalksTheOwnerToTheSpot(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.ELITE);
    DeathRecord death = deathAt(helper, DEATH_SPOT);

    helper.assertTrue(robot.docking().deathGuide().start(death), WRONG_CALL);

    helper.succeedWhen(() -> {
      helper.assertTrue(near(helper, robot, DEATH_SPOT), NOT_GUIDED);
      helper.assertTrue(robot.docking().deathGuide().guiding().isEmpty(), STILL_GUIDING);
    });
  }

  /** Only a tier that guides, with the switch on and in the Overworld (answers 1117 and 1118). */
  @GameTest(template = ARENA)
  public static void theGuideSaysWhyItCannotStart(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper);
    RobotDeathGuide advanced =
        chosenRobot(helper, owner, RobotTier.ADVANCED).docking().deathGuide();
    DeathRecord here = deathAt(helper, DEATH_SPOT);
    DeathRecord nether = new DeathRecord(FIRST_DAY_TIME, 0, 0, 0, NETHER, CAUSE);

    helper.assertValueEqual(advanced.callFor(here), DeathGuideCall.LOCKED, WRONG_CALL);
    helper.assertFalse(advanced.start(here), GUIDED);
    RobotDeathGuide elite = chosenRobot(helper, owner, RobotTier.ELITE).docking().deathGuide();
    helper.assertValueEqual(elite.callFor(nether), DeathGuideCall.DEATH_ELSEWHERE, WRONG_CALL);
    elite.toggle();
    helper.assertValueEqual(elite.callFor(here), DeathGuideCall.SWITCHED_OFF, WRONG_CALL);
    helper.succeed();
  }

  /** A trip to a Dock, as the auto return starts, wins and cancels the guide (answer 1121). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aTripToADockCancelsTheGuide(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.ELITE);
    robot.docking().deathGuide().start(deathAt(helper, DEATH_SPOT));

    robot.callToDock(helper.absolutePos(OWNER_SPOT));

    helper.succeedWhen(
        () -> helper.assertTrue(robot.docking().deathGuide().guiding().isEmpty(), STILL_GUIDING));
  }

  /**
   * The request names the Cubit, so a request for another one does nothing, and a stale row never
   * removes another death (answer 1111 and rule 20).
   */
  @GameTest(template = ARENA)
  public static void theTabletRequestsAreCheckedOnTheServer(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.ELITE);
    DeathRecord death = deathAt(helper, DEATH_SPOT);
    owner.setData(CubitAttachments.DEATH_LOG, DeathLog.EMPTY.record(death, 1));

    TabletDeaths.guide(owner, new DeathGuidePayload(UUID.randomUUID(), 0, death.dayTime(), true));
    helper.assertTrue(robot.docking().deathGuide().guiding().isEmpty(), GUIDED);
    TabletDeaths.delete(owner, new DeleteDeathPayload(0, death.dayTime() + 1));
    helper.assertValueEqual(
        owner.getData(CubitAttachments.DEATH_LOG).deaths().size(), 1, WRONG_LOG);
    TabletDeaths.guide(owner, new DeathGuidePayload(robot.getUUID(), 0, death.dayTime(), true));
    helper.assertTrue(robot.docking().deathGuide().guiding().isPresent(), NOT_GUIDED);
    TabletDeaths.delete(owner, new DeleteDeathPayload(0, death.dayTime()));
    helper.assertTrue(owner.getData(CubitAttachments.DEATH_LOG).deaths().isEmpty(), WRONG_LOG);
    helper.succeed();
  }

  /** The switch starts on and stays in the item (answer 1118). */
  @GameTest(template = ARENA)
  public static void theSwitchStartsOnAndStaysInTheItem(GameTestHelper helper) {
    ServerPlayer owner = ownerAt(helper);
    RobotEntity robot = chosenRobot(helper, owner, RobotTier.ADVANCED);
    helper.assertTrue(robot.docking().deathGuide().isOn(), WRONG_DEFAULT);

    robot.docking().deathGuide().toggle();
    ItemStack item = GameTestRobots.collect(owner, robot);

    RobotState kept = item.get(CubitDataComponents.ROBOT_STATE.get());
    helper.assertFalse(kept.deathPointOn(), NOT_KEPT);
    helper.succeed();
  }

  private static DeathPointLevel levelOf(RobotTier tier) {
    return CubitMod.CONFIG.tierCatalog().spec(CubitConfig.GENERAL_ROBOT, tier).deathPoint();
  }

  private static ServerPlayer ownerAt(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    BlockPos spot = helper.absolutePos(OWNER_SPOT);
    owner.moveTo(spot.getX(), spot.getY(), spot.getZ());
    return owner;
  }

  private static RobotEntity chosenRobot(
      GameTestHelper helper, ServerPlayer owner, RobotTier tier) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(CubitDataComponents.ROBOT_STATE.get(), new RobotState(tier, CHARGE, HEALTH));
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item, ROBOT_FLOOR);
    TabletSelection.choose(owner, robot.getUUID());
    return robot;
  }

  private static DeathRecord deathAt(GameTestHelper helper, BlockPos relative) {
    BlockPos spot = helper.absolutePos(relative);
    return new DeathRecord(
        FIRST_DAY_TIME, spot.getX(), spot.getY(), spot.getZ(), DeathRecord.OVERWORLD, CAUSE);
  }

  private static boolean near(GameTestHelper helper, RobotEntity robot, BlockPos relative) {
    BlockPos spot = helper.absolutePos(relative);
    return Math.sqrt(spot.distToCenterSqr(robot.position())) <= ARRIVAL_BLOCKS;
  }
}
