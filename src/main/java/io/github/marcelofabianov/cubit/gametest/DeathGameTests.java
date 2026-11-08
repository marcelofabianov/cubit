package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.player.DeadRobotLinks;
import io.github.marcelofabianov.cubit.player.FollowingRobotLink;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** A dead robot becomes an item, frees the following link and is safe from its owner. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DeathGameTests {
  private static final String ARENA = "arena";
  private static final int FLOOR_Y = 1;
  private static final int START_X = 1;
  private static final int MIDDLE_Z = 2;
  private static final int LAST_Z = 4;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos SECOND_FLOOR = new BlockPos(START_X, FLOOR_Y, LAST_Z);
  private static final int BURNING_DELAY = 40;
  private static final float HUGE_DAMAGE = 1000f;
  private static final float SMALL_DAMAGE = 1f;
  private static final float LONG_FIRE_SECONDS = 60f;
  private static final String LAST_SAFE_POSITION_KEY = "LastSafePosition";
  private static final String NO_ITEM = "The robot left no damaged item";
  private static final String WRONG_STATE = "Wrong damaged state";
  private static final String WRONG_OWNER = "Wrong saved owner";
  private static final String WRONG_SAFE_POSITION = "Wrong last safe position";
  private static final String LINK_NOT_FREED = "The following link was not freed";
  private static final String LINK_LOST = "The following link of another robot was lost";
  private static final String OWNER_HURT = "The owner hurt the robot";
  private static final String NOT_HURT = "The damage did not reach the robot";

  private DeathGameTests() {}

  @GameTest(template = ARENA)
  public static void deathLeavesADamagedRobotWithTheState(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    BlockPos placed = robot.blockPosition();

    robot.kill();

    ItemStack stack = droppedItem(helper).getItem();
    helper.assertTrue(stack.is(CubitItems.DAMAGED_GENERAL_ROBOT.get()), NO_ITEM);
    helper.assertValueEqual(
        stack.get(CubitDataComponents.ROBOT_STATE.get()),
        new RobotState(RobotTier.BASE, Energy.ZERO, 0f),
        WRONG_STATE);
    helper.assertValueEqual(
        stack.get(CubitDataComponents.ROBOT_OWNER.get()), owner.getUUID(), WRONG_OWNER);
    helper.assertValueEqual(
        stack.get(CubitDataComponents.LAST_SAFE_POSITION.get()),
        GlobalPos.of(helper.getLevel().dimension(), placed),
        WRONG_SAFE_POSITION);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void deathLeavesTheItemEvenWithoutMobLoot(GameTestHelper helper) {
    GameRules.BooleanValue mobLoot =
        helper.getLevel().getGameRules().getRule(GameRules.RULE_DOMOBLOOT);
    boolean before = mobLoot.get();
    mobLoot.set(false, helper.getLevel().getServer());
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    GameTestRobots.placeNewRobot(helper, owner, FLOOR).kill();
    mobLoot.set(before, helper.getLevel().getServer());

    helper.assertTrue(
        droppedItem(helper).getItem().is(CubitItems.DAMAGED_GENERAL_ROBOT.get()), NO_ITEM);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aBurningRobotDoesNotCountAsSafe(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    BlockPos placed = robot.blockPosition();
    robot.igniteForSeconds(LONG_FIRE_SECONDS);

    helper.runAfterDelay(
        BURNING_DELAY,
        () -> {
          helper.assertValueEqual(
              robot.lastSafePosition(), Optional.of(placed), WRONG_SAFE_POSITION);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void theLastSafePositionSurvivesASave(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    CompoundTag saved = robot.saveWithoutId(new CompoundTag());
    helper.assertTrue(saved.contains(LAST_SAFE_POSITION_KEY), WRONG_SAFE_POSITION);

    RobotEntity loaded = CubitEntities.GENERAL_ROBOT.get().create(helper.getLevel());
    loaded.load(saved);

    helper.assertValueEqual(
        loaded.lastSafePosition(), robot.lastSafePosition(), WRONG_SAFE_POSITION);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void deathFreesTheLinkOfAnOnlineOwner(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    robot.kill();

    helper.assertValueEqual(follower(owner), Optional.empty(), LINK_NOT_FREED);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void deathFreesTheLinkOfAnOfflineOwnerWhenTheyJoin(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    robot.kill();
    helper.assertValueEqual(follower(owner), Optional.of(robot.getUUID()), LINK_NOT_FREED);

    DeadRobotLinks.releaseFor(owner, helper.getLevel().getServer());

    helper.assertValueEqual(follower(owner), Optional.empty(), LINK_NOT_FREED);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void joiningKeepsTheLinkOfAnotherRobot(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity first = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    first.kill();
    owner.setData(CubitAttachments.FOLLOWING_ROBOT, FollowingRobotLink.NOBODY);
    RobotEntity second = GameTestRobots.placeNewRobot(helper, owner, SECOND_FLOOR);

    DeadRobotLinks.releaseFor(owner, helper.getLevel().getServer());

    helper.assertValueEqual(follower(owner), Optional.of(second.getUUID()), LINK_LOST);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theOwnerCannotHurtTheRobot(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    DamageSources sources = helper.getLevel().damageSources();
    float health = robot.getHealth();

    helper.assertFalse(robot.hurt(sources.playerAttack(owner), HUGE_DAMAGE), OWNER_HURT);
    helper.assertFalse(robot.hurt(sources.thrown(owner, owner), HUGE_DAMAGE), OWNER_HURT);
    helper.assertValueEqual(robot.getHealth(), health, OWNER_HURT);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void otherDamageStillHurtsTheRobot(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    Player stranger = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    DamageSources sources = helper.getLevel().damageSources();

    helper.assertTrue(robot.hurt(sources.playerAttack(stranger), SMALL_DAMAGE), NOT_HURT);
    robot.invulnerableTime = 0;
    helper.assertTrue(robot.hurt(sources.onFire(), SMALL_DAMAGE), NOT_HURT);
    helper.succeed();
  }

  private static ItemEntity droppedItem(GameTestHelper helper) {
    return helper.getEntities(EntityType.ITEM).stream()
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(NO_ITEM));
  }

  private static Optional<UUID> follower(Player player) {
    return player.getData(CubitAttachments.FOLLOWING_ROBOT).robot();
  }
}
