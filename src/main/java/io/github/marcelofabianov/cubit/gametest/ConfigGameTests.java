package io.github.marcelofabianov.cubit.gametest;

import com.electronwill.nightconfig.core.CommentedConfig;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.domain.energy.ConsumptionExtras;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import java.util.Map;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Runs in the real game, because the config cannot be loaded outside of it. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ConfigGameTests {
  private static final String EMPTY_TEMPLATE = "empty";
  private static final RobotType GENERAL_ROBOT = new RobotType("general_robot");
  private static final RobotType UNKNOWN_ROBOT = new RobotType("unknown_robot");
  private static final String INITIAL_CHARGE_KEY = "general.initial_charge_percent";
  private static final String BATTERY_KEY = "general_robot.base.battery_capacity";
  private static final String CONSUMPTION_KEY = "general_robot.base.moving_consumption_per_tick";
  private static final String DOCK_CAPACITY_KEY = "general.dock_energy_capacity";
  private static final String DOCK_INPUT_KEY = "general.dock_input_per_tick";
  private static final String DOCK_HEAL_KEY = "general.dock_full_heal_ticks";
  private static final String DOCK_STUCK_KEY = "general.dock_stuck_ticks";
  private static final String DOCK_HOLD_KEY = "general.dock_complete_hold_ticks";
  private static final int DOCK_HOLD = 60;
  private static final int DOCK_CAPACITY = 100_000;
  private static final int DOCK_INPUT = 5_000;
  private static final int DOCK_HEAL = 1_200;
  private static final int DOCK_STUCK = 100;
  private static final String FULL_KEY = "general_robot.base.full_inventory_extra_per_tick";
  private static final String WATER_KEY = "general_robot.base.water_extra_per_tick";
  private static final String NEGATIVE_KEY = "general_robot.base.negative_condition_extra_per_tick";
  private static final String CLIMB_KEY = "general_robot.base.climb_extra_per_block";
  private static final String FULL_SPEED_KEY = "general.full_inventory_speed_multiplier";
  private static final String RECHARGE_KEY = "general_robot.base.recharge_per_tick";
  private static final String SLOTS_KEY = "general_robot.base.inventory_slots";
  private static final String HEALTH_KEY = "general_robot.base.max_health";
  private static final String RADIUS_KEY = "general_robot.base.call_radius_blocks";
  private static final String WAKE_CHARGE_KEY = "general.wake_charge_percent";
  private static final int WAKE_CHARGE = 5;
  private static final int INITIAL_CHARGE = 25;
  private static final int BATTERY = 300_000;
  private static final int CONSUMPTION = 15;
  private static final int FULL_INVENTORY_EXTRA = 5;
  private static final int WATER_EXTRA = 5;
  private static final int NEGATIVE_CONDITION_EXTRA = 25;
  private static final int CLIMB_EXTRA = 15;
  private static final double FULL_INVENTORY_SPEED = 0.75;
  private static final int RECHARGE = 500;
  private static final int SLOTS = 27;
  private static final int HEALTH = 25;
  private static final int RADIUS = 15;
  private static final int MAX_SLOTS = 54;
  private static final int TOO_MANY_SLOTS = 55;
  private static final String NOT_A_NUMBER = "many";
  private static final String REPATH_KEY = "general.follow_repath_ticks";
  private static final int REPATH = 10;
  private static final String STOP_KEY = "general.stop_distance_blocks";
  private static final int STOP = 3;
  private static final String TELEPORT_KEY = "general.teleport_distance_blocks";
  private static final int TELEPORT = 20;
  private static final String WATER_SEARCH_KEY = "general.water_search_radius_blocks";
  private static final int WATER_SEARCH = 8;
  private static final String WANDER_KEY = "general.burning_wander_radius_blocks";
  private static final int WANDER = 5;
  private static final String BURNING_SPEED_KEY = "general.burning_speed_multiplier";
  private static final double BURNING_SPEED = 1.25;
  private static final String BASE_SPEED_KEY = "general.base_movement_speed";
  private static final double BASE_SPEED = 0.3;
  private static final String SPEED_KEY = "general_robot.base.speed_multiplier";
  private static final double SPEED = 1;
  private static final Map<String, Integer> GENERAL_DEFAULTS =
      Map.of(
          INITIAL_CHARGE_KEY, INITIAL_CHARGE,
          WAKE_CHARGE_KEY, WAKE_CHARGE,
          REPATH_KEY, REPATH,
          STOP_KEY, STOP,
          TELEPORT_KEY, TELEPORT,
          WATER_SEARCH_KEY, WATER_SEARCH,
          WANDER_KEY, WANDER);
  private static final Map<String, Integer> TIER_DEFAULTS =
      Map.of(
          BATTERY_KEY, BATTERY,
          CONSUMPTION_KEY, CONSUMPTION,
          FULL_KEY, FULL_INVENTORY_EXTRA,
          WATER_KEY, WATER_EXTRA,
          NEGATIVE_KEY, NEGATIVE_CONDITION_EXTRA,
          CLIMB_KEY, CLIMB_EXTRA,
          RECHARGE_KEY, RECHARGE,
          SLOTS_KEY, SLOTS,
          HEALTH_KEY, HEALTH,
          RADIUS_KEY, RADIUS);
  private static final TierSpec BASE_SPEC =
      new TierSpec(
          new Energy(BATTERY),
          new Energy(CONSUMPTION),
          new Energy(RECHARGE),
          SLOTS,
          HEALTH,
          RADIUS,
          SPEED,
          new ConsumptionExtras(
              new Energy(FULL_INVENTORY_EXTRA),
              new Energy(WATER_EXTRA),
              new Energy(NEGATIVE_CONDITION_EXTRA),
              new Energy(CLIMB_EXTRA)));
  private static final String WRONG_SPEC = "Wrong tier numbers";
  private static final String WRONG_CHARGE = "Wrong initial charge";
  private static final String WRONG_KEY = "Wrong value for a config key";
  private static final String UNKNOWN_TYPE_ACCEPTED = "A robot type without config was accepted";

  private ConfigGameTests() {}

  @GameTest(template = EMPTY_TEMPLATE)
  public static void loadedConfigGivesTheNumbersOfTheTiersDocument(GameTestHelper helper) {
    TierSpec spec = CubitMod.CONFIG.tierCatalog().spec(GENERAL_ROBOT, RobotTier.BASE);

    helper.assertValueEqual(spec, BASE_SPEC, WRONG_SPEC);
    helper.assertValueEqual(
        CubitMod.CONFIG.initialCharge(), new Percentage(INITIAL_CHARGE), WRONG_CHARGE);
    helper.assertValueEqual(
        CubitMod.CONFIG.fullInventorySpeedMultiplier(), FULL_INVENTORY_SPEED, WRONG_SPEC);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void tierWithoutConfigUsesTheBaseTier(GameTestHelper helper) {
    TierSpec spec = CubitMod.CONFIG.tierCatalog().spec(GENERAL_ROBOT, RobotTier.ULTIMATE);

    helper.assertValueEqual(spec, BASE_SPEC, WRONG_SPEC);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void robotTypeWithoutConfigIsAProgrammingError(GameTestHelper helper) {
    try {
      CubitMod.CONFIG.tierCatalog().spec(UNKNOWN_ROBOT, RobotTier.BASE);
      helper.fail(UNKNOWN_TYPE_ACCEPTED);
    } catch (IllegalArgumentException expected) {
      helper.succeed();
    }
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void keysAreTheContractWithThePack(GameTestHelper helper) {
    CommentedConfig file = correctedFile();

    GENERAL_DEFAULTS.forEach(
        (key, value) -> helper.assertValueEqual(file.<Integer>get(key), value, WRONG_KEY));
    TIER_DEFAULTS.forEach(
        (key, value) -> helper.assertValueEqual(file.<Integer>get(key), value, WRONG_KEY));
    helper.assertValueEqual(file.<Double>get(BASE_SPEED_KEY), BASE_SPEED, WRONG_KEY);
    helper.assertValueEqual(file.<Double>get(BURNING_SPEED_KEY), BURNING_SPEED, WRONG_KEY);
    helper.assertValueEqual(file.<Double>get(SPEED_KEY), SPEED, WRONG_KEY);
    helper.assertValueEqual(file.<Integer>get(DOCK_CAPACITY_KEY), DOCK_CAPACITY, WRONG_KEY);
    helper.assertValueEqual(file.<Integer>get(DOCK_INPUT_KEY), DOCK_INPUT, WRONG_KEY);
    helper.assertValueEqual(file.<Integer>get(DOCK_HEAL_KEY), DOCK_HEAL, WRONG_KEY);
    helper.assertValueEqual(file.<Integer>get(DOCK_STUCK_KEY), DOCK_STUCK, WRONG_KEY);
    helper.assertValueEqual(file.<Integer>get(DOCK_HOLD_KEY), DOCK_HOLD, WRONG_KEY);
    helper.assertValueEqual(file.<Double>get(FULL_SPEED_KEY), FULL_INVENTORY_SPEED, WRONG_KEY);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void valueOutOfLimitsUsesTheNearestLimit(GameTestHelper helper) {
    CommentedConfig file = CommentedConfig.inMemory();
    file.set(SLOTS_KEY, TOO_MANY_SLOTS);

    new CubitConfig().spec().correct(file);

    helper.assertValueEqual(file.<Integer>get(SLOTS_KEY), MAX_SLOTS, WRONG_KEY);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void invalidValueGoesBackToTheDefault(GameTestHelper helper) {
    CommentedConfig file = CommentedConfig.inMemory();
    file.set(SLOTS_KEY, NOT_A_NUMBER);

    new CubitConfig().spec().correct(file);

    helper.assertValueEqual(file.<Integer>get(SLOTS_KEY), SLOTS, WRONG_KEY);
    helper.succeed();
  }

  private static CommentedConfig correctedFile() {
    CommentedConfig file = CommentedConfig.inMemory();
    new CubitConfig().spec().correct(file);
    return file;
  }
}
