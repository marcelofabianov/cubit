package io.github.marcelofabianov.cubit.gametest;

import com.electronwill.nightconfig.core.CommentedConfig;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
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
  private static final String RECHARGE_KEY = "general_robot.base.recharge_per_tick";
  private static final String SLOTS_KEY = "general_robot.base.inventory_slots";
  private static final String HEALTH_KEY = "general_robot.base.max_health";
  private static final String RADIUS_KEY = "general_robot.base.call_radius_blocks";
  private static final String WAKE_CHARGE_KEY = "general.wake_charge_percent";
  private static final int WAKE_CHARGE = 5;
  private static final int INITIAL_CHARGE = 25;
  private static final int BATTERY = 300_000;
  private static final int CONSUMPTION = 10;
  private static final int RECHARGE = 500;
  private static final int SLOTS = 27;
  private static final int HEALTH = 25;
  private static final int RADIUS = 15;
  private static final int MAX_SLOTS = 54;
  private static final int TOO_MANY_SLOTS = 55;
  private static final String NOT_A_NUMBER = "many";
  private static final Map<String, Integer> DEFAULTS =
      Map.of(
          INITIAL_CHARGE_KEY, INITIAL_CHARGE,
          WAKE_CHARGE_KEY, WAKE_CHARGE,
          BATTERY_KEY, BATTERY,
          CONSUMPTION_KEY, CONSUMPTION,
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
          RADIUS);
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

    DEFAULTS.forEach(
        (key, value) -> helper.assertValueEqual(file.<Integer>get(key), value, WRONG_KEY));
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
