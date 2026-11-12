package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.domain.robot.OreFilter;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.registry.CubitDamageTypes;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The shock and the defense modes in the world (answers 822 to 834). Each test with mobs runs in a
 * batch of its own, because a shock reaches the arena of the test beside it.
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ShockGameTests {
  private static final String ARENA = "arena";
  private static final String EMPTY = "empty";
  private static final String SHOCK_AGGRESSIVE_BATCH = "shock_aggressive";
  private static final String SHOCK_PEACEFUL_BATCH = "shock_peaceful";
  private static final String SHOCK_ANIMAL_BATCH = "shock_animal";
  private static final String SHOCK_RETALIATE_BATCH = "shock_retaliate";
  private static final String SHOCK_AREA_BATCH = "shock_area";
  private static final String SHOCK_NO_CHARGE_BATCH = "shock_no_charge";
  private static final String SHOCK_WALL_BATCH = "shock_wall";
  private static final String WRONG_MODE = "Wrong defense mode";
  private static final String NOT_SHOCKED = "The mob should have been shocked";
  private static final String SHOCKED = "The mob should not have been shocked";
  private static final String WRONG_DAMAGE_TYPE = "The damage should be the shock of the Cubit";
  private static final String WRONG_CHARGE = "The shock should cost the energy of one shock";
  private static final String CHOSEN = "The choice should have been refused";
  private static final String NOT_CHOSEN = "The choice should have been accepted";

  private static final int TIMEOUT = 200;
  private static final int WAIT_TICKS = 60;
  private static final float HEALTH = 25f;
  private static final float RETALIATED_HIT = 1f;
  private static final Energy CHARGE = new Energy(100_000);
  private static final Energy ELITE_COST = new Energy(2_000);
  private static final Energy ULTIMATE_COST = new Energy(1_500);
  private static final Energy BELOW_COST = new Energy(1_999);
  private static final int ROOF_Y = 5;
  private static final int ROOF_WIDTH = 8;
  private static final int ROOF_DEPTH = 5;
  private static final int FLOOR_Y = 1;
  private static final int STAND_Y = 2;
  private static final int MIDDLE_Z = 2;
  private static final int ROBOT_X = 1;
  private static final BlockPos FLOOR = new BlockPos(ROBOT_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos NEAR = new BlockPos(ROBOT_X + 2, STAND_Y, MIDDLE_Z);
  private static final BlockPos BEYOND = new BlockPos(ROBOT_X + 3, STAND_Y, MIDDLE_Z + 1);
  private static final BlockPos WALL = new BlockPos(ROBOT_X + 1, STAND_Y, MIDDLE_Z);

  /** Next to the robot, as a mob that just hit it, so the knockback keeps it in range. */
  private static final BlockPos ADJACENT = WALL;

  private ShockGameTests() {}

  @GameTest(template = EMPTY)
  public static void aNewRobotIsPeacefulAndKeepsItsModeAsAnItem(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item(RobotTier.ELITE), FLOOR);
    helper.assertValueEqual(robot.defense().mode(), DefenseMode.PEACEFUL, WRONG_MODE);

    robot.defense().choose(owner, DefenseMode.AGGRESSIVE);
    ItemStack collected = collect(owner, robot);

    RobotState saved = collected.get(CubitDataComponents.ROBOT_STATE.get());
    helper.assertValueEqual(saved.defenseMode(), DefenseMode.AGGRESSIVE, WRONG_MODE);
    RobotEntity again = GameTestRobots.placeRobot(helper, owner, collected, FLOOR);
    helper.assertValueEqual(again.defense().mode(), DefenseMode.AGGRESSIVE, WRONG_MODE);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void savingKeepsTheMode(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item(RobotTier.ADVANCED), FLOOR);
    robot.defense().choose(owner, DefenseMode.RETALIATE);

    RobotEntity loaded = GameTestRobots.loadRobot(helper, robot.saveWithoutId(new CompoundTag()));

    helper.assertValueEqual(loaded.defense().mode(), DefenseMode.RETALIATE, WRONG_MODE);
    helper.succeed();
  }

  @GameTest(template = EMPTY)
  public static void onlyTheOwnerChoosesAModeOfTheTier(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    Player stranger = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity base = GameTestRobots.placeRobot(helper, owner, item(RobotTier.BASE), FLOOR);
    RobotEntity advanced =
        GameTestRobots.placeRobot(helper, owner, item(RobotTier.ADVANCED), FLOOR.east());

    helper.assertFalse(base.defense().choose(owner, DefenseMode.RETALIATE), CHOSEN);
    helper.assertFalse(advanced.defense().choose(owner, DefenseMode.AGGRESSIVE), CHOSEN);
    helper.assertFalse(advanced.defense().choose(stranger, DefenseMode.RETALIATE), CHOSEN);
    helper.assertTrue(advanced.defense().choose(owner, DefenseMode.RETALIATE), NOT_CHOSEN);
    helper.assertValueEqual(advanced.defense().mode(), DefenseMode.RETALIATE, WRONG_MODE);
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = SHOCK_AGGRESSIVE_BATCH)
  public static void aggressiveShocksAHostileMobInRange(GameTestHelper helper) {
    Player owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item(RobotTier.ELITE), FLOOR);
    robot.defense().choose(owner, DefenseMode.AGGRESSIVE);
    Mob husk = still(helper.spawn(EntityType.HUSK, NEAR));

    helper.runAfterDelay(
        WAIT_TICKS,
        () -> {
          helper.assertTrue(husk.getHealth() < husk.getMaxHealth(), NOT_SHOCKED);
          DamageSource last = husk.getLastDamageSource();
          helper.assertTrue(last != null && last.is(CubitDamageTypes.SHOCK), WRONG_DAMAGE_TYPE);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = SHOCK_PEACEFUL_BATCH)
  public static void peacefulNeverShocks(GameTestHelper helper) {
    Player owner = ownerBeside(helper);
    GameTestRobots.placeRobot(helper, owner, item(RobotTier.ELITE), FLOOR);
    Mob husk = still(helper.spawn(EntityType.HUSK, NEAR));

    helper.runAfterDelay(
        WAIT_TICKS,
        () -> {
          helper.assertValueEqual(husk.getHealth(), husk.getMaxHealth(), SHOCKED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = SHOCK_ANIMAL_BATCH)
  public static void aggressiveNeverShocksAPeacefulMob(GameTestHelper helper) {
    Player owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item(RobotTier.ELITE), FLOOR);
    robot.defense().choose(owner, DefenseMode.AGGRESSIVE);
    Mob cow = still(helper.spawn(EntityType.COW, NEAR));

    helper.runAfterDelay(
        WAIT_TICKS,
        () -> {
          helper.assertValueEqual(cow.getHealth(), cow.getMaxHealth(), SHOCKED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = SHOCK_RETALIATE_BATCH)
  public static void retaliateOnlyShocksTheAttacker(GameTestHelper helper) {
    Player owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item(RobotTier.ADVANCED), FLOOR);
    robot.defense().choose(owner, DefenseMode.RETALIATE);
    Mob attacker = still(helper.spawn(EntityType.HUSK, ADJACENT));
    Mob bystander = still(helper.spawn(EntityType.HUSK, NEAR.south()));

    robot.hurt(robot.damageSources().mobAttack(attacker), RETALIATED_HIT);

    helper.runAfterDelay(
        WAIT_TICKS,
        () -> {
          helper.assertTrue(attacker.getHealth() < attacker.getMaxHealth(), NOT_SHOCKED);
          helper.assertValueEqual(bystander.getHealth(), bystander.getMaxHealth(), SHOCKED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = SHOCK_AREA_BATCH)
  public static void anUltimateShockHitsEveryTargetForOneCost(GameTestHelper helper) {
    Player owner = ownerBeside(helper);
    roof(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item(RobotTier.ULTIMATE), FLOOR);
    robot.defense().choose(owner, DefenseMode.AGGRESSIVE);
    Energy before = robot.state().energy();
    Mob first = still(helper.spawn(EntityType.HUSK, NEAR));
    Mob second = still(helper.spawn(EntityType.HUSK, BEYOND));

    helper.succeedWhen(() -> {
      helper.assertTrue(first.getHealth() < first.getMaxHealth(), NOT_SHOCKED);
      helper.assertTrue(second.getHealth() < second.getMaxHealth(), NOT_SHOCKED);
      long spent = before.amount() - robot.state().energy().amount();
      helper.assertTrue(
          spent >= ULTIMATE_COST.amount() && spent < ULTIMATE_COST.amount() * 2, WRONG_CHARGE);
    });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = SHOCK_NO_CHARGE_BATCH)
  public static void withoutChargeForTheCostThereIsNoShock(GameTestHelper helper) {
    Player owner = ownerBeside(helper);
    roof(helper);
    RobotEntity robot =
        GameTestRobots.placeRobot(helper, owner, item(RobotTier.ELITE, BELOW_COST), FLOOR);
    robot.defense().choose(owner, DefenseMode.AGGRESSIVE);
    Mob husk = still(helper.spawn(EntityType.HUSK, NEAR));

    helper.runAfterDelay(
        WAIT_TICKS,
        () -> {
          helper.assertValueEqual(husk.getHealth(), husk.getMaxHealth(), SHOCKED);
          helper.assertTrue(robot.state().energy().amount() < ELITE_COST.amount(), WRONG_CHARGE);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = SHOCK_WALL_BATCH)
  public static void aWallBlocksTheShock(GameTestHelper helper) {
    Player owner = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item(RobotTier.ELITE), FLOOR);
    robot.defense().choose(owner, DefenseMode.AGGRESSIVE);
    helper.setBlock(WALL, Blocks.STONE);
    helper.setBlock(WALL.above(), Blocks.STONE);
    Mob husk = still(helper.spawn(EntityType.HUSK, NEAR));

    helper.runAfterDelay(
        WAIT_TICKS,
        () -> {
          helper.assertValueEqual(husk.getHealth(), husk.getMaxHealth(), SHOCKED);
          helper.succeed();
        });
  }

  /** Without the sky, the solar panel gives nothing, and the charge only goes down. */
  private static void roof(GameTestHelper helper) {
    for (int x = 0; x < ROOF_WIDTH; x++) {
      for (int z = 0; z < ROOF_DEPTH; z++) {
        helper.setBlock(new BlockPos(x, ROOF_Y, z), Blocks.STONE);
      }
    }
  }

  /** Without its AI, the mob stays where the test put it and never attacks. */
  private static Mob still(Mob mob) {
    mob.setNoAi(true);
    return mob;
  }

  private static ItemStack item(RobotTier tier) {
    return item(tier, CHARGE);
  }

  private static ItemStack item(RobotTier tier, Energy energy) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(
        CubitDataComponents.ROBOT_STATE.get(),
        new RobotState(
            tier, energy, HEALTH, false, RobotColor.WHITE, false, OreFilter.EMPTY, false));
    return item;
  }

  /** On the side away from the mobs, so the owner never stands between them and the robot. */
  private static Player ownerBeside(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    Vec3 beside = helper.absoluteVec(Vec3.atBottomCenterOf(FLOOR.above().north()));
    owner.moveTo(beside.x, beside.y, beside.z);
    return owner;
  }

  private static ItemStack collect(Player owner, RobotEntity robot) {
    owner.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    owner.setShiftKeyDown(true);
    robot.interact(owner, InteractionHand.MAIN_HAND);
    owner.setShiftKeyDown(false);
    return owner.getInventory().getItem(0);
  }
}
