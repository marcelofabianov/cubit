package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.domain.robot.OreFilter;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Focus in the world: only the mob the owner hurt takes the shock, and without a target the robot
 * defends like Retaliate (answers 843 to 853). Each test with mobs runs in a batch of its own.
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class FocusGameTests {
  private static final String ARENA = "arena";
  private static final String EMPTY = "empty";
  private static final String TARGET_BATCH = "focus_target";
  private static final String NO_TARGET_BATCH = "focus_no_target";
  private static final String PEACEFUL_BATCH = "focus_peaceful";
  private static final String SWITCH_BATCH = "focus_switch";
  private static final String DEATH_BATCH = "focus_death";
  private static final String MODE_BATCH = "focus_mode";
  private static final String NOT_SHOCKED = "The target should have been shocked";
  private static final String SHOCKED = "Only the target should be shocked";
  private static final String WRONG_TARGET = "Wrong target of Focus";
  private static final String NOT_ALLOWED = "Focus should unlock at Advanced";
  private static final String ALLOWED = "Focus should be locked at Base";

  private static final int TIMEOUT = 200;
  private static final int WAIT_TICKS = 60;
  private static final int SETTLE_TICKS = 3;
  private static final float HEALTH = 25f;
  private static final float OWNER_HIT = 1f;
  private static final Energy CHARGE = new Energy(100_000);
  private static final int FLOOR_Y = 1;
  private static final int STAND_Y = 2;
  private static final int MIDDLE_Z = 2;
  private static final int ROBOT_X = 1;
  private static final BlockPos FLOOR = new BlockPos(ROBOT_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos NEAR = new BlockPos(ROBOT_X + 2, STAND_Y, MIDDLE_Z);
  private static final BlockPos BESIDE = NEAR.south();

  /** Next to the robot, as a mob that just hit it, so the knockback keeps it in range. */
  private static final BlockPos ADJACENT = new BlockPos(ROBOT_X + 1, STAND_Y, MIDDLE_Z);

  private FocusGameTests() {}

  @GameTest(template = EMPTY)
  public static void focusUnlocksAtAdvanced(GameTestHelper helper) {
    helper.assertTrue(
        CubitMod.CONFIG
            .tierCatalog()
            .spec(CubitConfig.GENERAL_ROBOT, RobotTier.ADVANCED)
            .shock()
            .allows(DefenseMode.FOCUS),
        NOT_ALLOWED);
    helper.assertFalse(
        CubitMod.CONFIG
            .tierCatalog()
            .spec(CubitConfig.GENERAL_ROBOT, RobotTier.BASE)
            .shock()
            .allows(DefenseMode.FOCUS),
        ALLOWED);
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = TARGET_BATCH)
  public static void focusOnlyShocksTheMobTheOwnerHurt(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = focusedRobot(helper, owner);
    Mob target = still(helper.spawn(EntityType.HUSK, NEAR));
    Mob bystander = still(helper.spawn(EntityType.HUSK, BESIDE));

    hitByOwner(owner, target);

    helper.runAfterDelay(
        WAIT_TICKS,
        () -> {
          helper.assertTrue(robot.defense().focusTarget().is(target.getUUID()), WRONG_TARGET);
          helper.assertTrue(target.getHealth() < target.getMaxHealth() - OWNER_HIT, NOT_SHOCKED);
          helper.assertValueEqual(bystander.getHealth(), bystander.getMaxHealth(), SHOCKED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = NO_TARGET_BATCH)
  public static void withoutATargetFocusDefendsLikeRetaliate(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = focusedRobot(helper, owner);
    Mob attacker = still(helper.spawn(EntityType.HUSK, ADJACENT));

    robot.hurt(robot.damageSources().mobAttack(attacker), OWNER_HIT);

    helper.runAfterDelay(
        WAIT_TICKS,
        () -> {
          helper.assertFalse(robot.defense().focusTarget().hasTarget(), WRONG_TARGET);
          helper.assertTrue(attacker.getHealth() < attacker.getMaxHealth(), NOT_SHOCKED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = PEACEFUL_BATCH)
  public static void aPeacefulMobHurtByTheOwnerNeverBecomesTheTarget(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = focusedRobot(helper, owner);
    Mob cow = still(helper.spawn(EntityType.COW, NEAR));

    hitByOwner(owner, cow);

    helper.runAfterDelay(
        WAIT_TICKS,
        () -> {
          helper.assertFalse(robot.defense().focusTarget().hasTarget(), WRONG_TARGET);
          helper.assertValueEqual(cow.getHealth(), cow.getMaxHealth() - OWNER_HIT, SHOCKED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = SWITCH_BATCH)
  public static void aNewMobHurtByTheOwnerTakesThePlace(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = focusedRobot(helper, owner);
    Mob first = still(helper.spawn(EntityType.HUSK, NEAR));
    Mob second = still(helper.spawn(EntityType.HUSK, BESIDE));

    hitByOwner(owner, first);
    hitByOwner(owner, second);

    helper.assertTrue(robot.defense().focusTarget().is(second.getUUID()), WRONG_TARGET);
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = DEATH_BATCH)
  public static void theTargetEndsWhenItDies(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = focusedRobot(helper, owner);
    Mob target = still(helper.spawn(EntityType.HUSK, NEAR));
    hitByOwner(owner, target);

    target.kill();

    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertFalse(robot.defense().focusTarget().hasTarget(), WRONG_TARGET);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = MODE_BATCH)
  public static void changingTheModeForgetsTheTarget(GameTestHelper helper) {
    ServerPlayer owner = ownerBeside(helper);
    RobotEntity robot = focusedRobot(helper, owner);
    Mob target = still(helper.spawn(EntityType.HUSK, NEAR));
    hitByOwner(owner, target);

    robot.defense().choose(owner, DefenseMode.RETALIATE);

    helper.assertFalse(robot.defense().focusTarget().hasTarget(), WRONG_TARGET);
    helper.succeed();
  }

  private static RobotEntity focusedRobot(GameTestHelper helper, ServerPlayer owner) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(
        CubitDataComponents.ROBOT_STATE.get(),
        new RobotState(
            RobotTier.ADVANCED,
            CHARGE,
            HEALTH,
            false,
            RobotColor.WHITE,
            false,
            OreFilter.EMPTY,
            false));
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item, FLOOR);
    robot.defense().choose(owner, DefenseMode.FOCUS);
    return robot;
  }

  /** Any damage the owner causes marks the target, as a hit does (answer 852). */
  private static void hitByOwner(ServerPlayer owner, LivingEntity mob) {
    mob.hurt(owner.damageSources().playerAttack(owner), OWNER_HIT);
  }

  /** Without its AI, the mob stays where the test put it and never attacks. */
  private static Mob still(Mob mob) {
    mob.setNoAi(true);
    return mob;
  }

  /** On the side away from the mobs, so the owner never stands between them and the robot. */
  private static ServerPlayer ownerBeside(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    Vec3 beside = helper.absoluteVec(Vec3.atBottomCenterOf(FLOOR.above().north()));
    owner.moveTo(beside.x, beside.y, beside.z);
    return owner;
  }
}
