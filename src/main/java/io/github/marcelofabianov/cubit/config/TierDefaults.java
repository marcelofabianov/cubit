package io.github.marcelofabianov.cubit.config;

import io.github.marcelofabianov.cubit.domain.behavior.LightLevel;
import io.github.marcelofabianov.cubit.domain.energy.ConsumptionExtras;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.energy.SolarOutput;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.domain.tier.AreaSpec;
import io.github.marcelofabianov.cubit.domain.tier.PickupSpec;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.domain.tier.ScannerSpec;
import io.github.marcelofabianov.cubit.domain.tier.ShockSpec;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import io.github.marcelofabianov.cubit.domain.tier.TorchSpec;
import java.util.Set;

/**
 * The default numbers of the Cubit at each tier, from _docs/tiers.md. The pack can change them in
 * the config, so they are only the starting point.
 */
final class TierDefaults {
  private static final long BASE_BATTERY = 300_000;
  private static final long BASE_CONSUMPTION = 15;
  private static final long BASE_FULL_INVENTORY_EXTRA = 5;
  private static final long BASE_WATER_EXTRA = 5;
  private static final long BASE_NEGATIVE_CONDITION_EXTRA = 25;
  private static final long BASE_CLIMB_EXTRA = 15;
  private static final long BASE_LAMP_CONSUMPTION = 1;
  private static final int BASE_LAMP_LIGHT = 10;
  private static final long BASE_SOLAR_DAY = 5;
  private static final long BASE_SOLAR_NIGHT = 0;
  private static final long BASE_RECHARGE = 500;
  private static final int BASE_SLOTS = 27;
  private static final int BASE_HEALTH = 25;
  private static final int BASE_CALL_RADIUS = 15;
  private static final double BASE_SPEED_MULTIPLIER = 1.0;
  private static final int BASE_ORE_SCAN_RADIUS = 5;
  private static final int BASE_MOB_SCAN_RADIUS = 8;
  private static final int BASE_ORE_SCAN_TICKS = 700;
  private static final int BASE_MOB_SCAN_TICKS = 200;
  private static final long BASE_ORE_SCAN_COST = 50;
  private static final long BASE_MOB_SCAN_COST = 50;
  private static final int BASE_ORE_FILTER_SLOTS = 1;
  private static final long ADVANCED_BATTERY = 600_000;
  private static final long ADVANCED_CONSUMPTION = 18;
  private static final long ADVANCED_FULL_INVENTORY_EXTRA = 6;
  private static final long ADVANCED_WATER_EXTRA = 6;
  private static final long ADVANCED_NEGATIVE_CONDITION_EXTRA = 50;
  private static final long ADVANCED_CLIMB_EXTRA = 18;
  private static final long ADVANCED_LAMP_CONSUMPTION = 1;
  private static final int ADVANCED_LAMP_LIGHT = 12;
  private static final long ADVANCED_SOLAR_DAY = 10;
  private static final long ADVANCED_SOLAR_NIGHT = 0;
  private static final long ADVANCED_RECHARGE = 1_000;
  private static final int ADVANCED_SLOTS = 36;
  private static final int ADVANCED_HEALTH = 50;
  private static final int ADVANCED_CALL_RADIUS = 24;
  private static final double ADVANCED_SPEED_MULTIPLIER = 1.25;
  private static final int ADVANCED_ORE_SCAN_RADIUS = 8;
  private static final int ADVANCED_MOB_SCAN_RADIUS = 14;
  private static final int ADVANCED_ORE_SCAN_TICKS = 400;
  private static final int ADVANCED_MOB_SCAN_TICKS = 160;
  private static final long ADVANCED_ORE_SCAN_COST = 100;
  private static final long ADVANCED_MOB_SCAN_COST = 100;
  private static final int ADVANCED_ORE_FILTER_SLOTS = 3;
  private static final long ELITE_BATTERY = 3_000_000;
  private static final long ELITE_CONSUMPTION = 22;
  private static final long ELITE_FULL_INVENTORY_EXTRA = 7;
  private static final long ELITE_WATER_EXTRA = 7;
  private static final long ELITE_NEGATIVE_CONDITION_EXTRA = 125;
  private static final long ELITE_CLIMB_EXTRA = 22;
  private static final long ELITE_LAMP_CONSUMPTION = 2;
  private static final int ELITE_LAMP_LIGHT = 14;
  private static final long ELITE_SOLAR_DAY = 12;
  private static final long ELITE_SOLAR_NIGHT = 3;
  private static final long ELITE_RECHARGE = 2_500;
  private static final int ELITE_SLOTS = 45;
  private static final int ELITE_HEALTH = 100;
  private static final int ELITE_CALL_RADIUS = 32;
  private static final double ELITE_SPEED_MULTIPLIER = 1.5;
  private static final int ELITE_ORE_SCAN_RADIUS = 12;
  private static final int ELITE_MOB_SCAN_RADIUS = 20;
  private static final int ELITE_ORE_SCAN_TICKS = 300;
  private static final int ELITE_MOB_SCAN_TICKS = 120;
  private static final long ELITE_ORE_SCAN_COST = 150;
  private static final long ELITE_MOB_SCAN_COST = 150;
  private static final int ELITE_ORE_FILTER_SLOTS = 6;
  private static final long ULTIMATE_BATTERY = 8_000_000;
  private static final long ULTIMATE_CONSUMPTION = 30;
  private static final long ULTIMATE_FULL_INVENTORY_EXTRA = 10;
  private static final long ULTIMATE_WATER_EXTRA = 10;
  private static final long ULTIMATE_NEGATIVE_CONDITION_EXTRA = 250;
  private static final long ULTIMATE_CLIMB_EXTRA = 30;
  private static final long ULTIMATE_LAMP_CONSUMPTION = 2;
  private static final int ULTIMATE_LAMP_LIGHT = 15;
  private static final long ULTIMATE_SOLAR_DAY = 16;
  private static final long ULTIMATE_SOLAR_NIGHT = 4;
  private static final long ULTIMATE_RECHARGE = 5_000;
  private static final int ULTIMATE_SLOTS = 54;
  private static final int ULTIMATE_HEALTH = 200;
  private static final int ULTIMATE_CALL_RADIUS = 48;
  private static final double ULTIMATE_SPEED_MULTIPLIER = 2.0;
  private static final int ULTIMATE_ORE_SCAN_RADIUS = 20;
  private static final int ULTIMATE_MOB_SCAN_RADIUS = 25;
  private static final int ULTIMATE_ORE_SCAN_TICKS = 100;
  private static final int ULTIMATE_MOB_SCAN_TICKS = 80;
  private static final long ULTIMATE_ORE_SCAN_COST = 300;
  private static final long ULTIMATE_MOB_SCAN_COST = 300;
  private static final int ULTIMATE_ORE_FILTER_SLOTS = 64;
  private static final int BASE_SHOCK_DAMAGE = 0;
  private static final int BASE_SHOCK_RANGE = 0;
  private static final int BASE_SHOCK_TICKS = 60;
  private static final long BASE_SHOCK_COST = 0;
  private static final ShockSpec BASE_SHOCK =
      new ShockSpec(
          BASE_SHOCK_DAMAGE,
          BASE_SHOCK_RANGE,
          BASE_SHOCK_TICKS,
          new Energy(BASE_SHOCK_COST),
          false,
          Set.of());
  private static final int ADVANCED_SHOCK_DAMAGE = 2;
  private static final int ADVANCED_SHOCK_RANGE = 3;
  private static final int ADVANCED_SHOCK_TICKS = 60;
  private static final long ADVANCED_SHOCK_COST = 2_000;
  private static final ShockSpec ADVANCED_SHOCK =
      new ShockSpec(
          ADVANCED_SHOCK_DAMAGE,
          ADVANCED_SHOCK_RANGE,
          ADVANCED_SHOCK_TICKS,
          new Energy(ADVANCED_SHOCK_COST),
          false,
          Set.of(DefenseMode.RETALIATE, DefenseMode.FOCUS));
  private static final int ELITE_SHOCK_DAMAGE = 4;
  private static final int ELITE_SHOCK_RANGE = 5;
  private static final int ELITE_SHOCK_TICKS = 40;
  private static final long ELITE_SHOCK_COST = 2_000;
  private static final ShockSpec ELITE_SHOCK =
      new ShockSpec(
          ELITE_SHOCK_DAMAGE,
          ELITE_SHOCK_RANGE,
          ELITE_SHOCK_TICKS,
          new Energy(ELITE_SHOCK_COST),
          false,
          Set.of(DefenseMode.RETALIATE, DefenseMode.AGGRESSIVE, DefenseMode.FOCUS));
  private static final int ULTIMATE_SHOCK_DAMAGE = 6;
  private static final int ULTIMATE_SHOCK_RANGE = 8;
  private static final int ULTIMATE_SHOCK_TICKS = 20;
  private static final long ULTIMATE_SHOCK_COST = 1_500;
  private static final ShockSpec ULTIMATE_SHOCK =
      new ShockSpec(
          ULTIMATE_SHOCK_DAMAGE,
          ULTIMATE_SHOCK_RANGE,
          ULTIMATE_SHOCK_TICKS,
          new Energy(ULTIMATE_SHOCK_COST),
          true,
          Set.of(DefenseMode.RETALIATE, DefenseMode.AGGRESSIVE, DefenseMode.FOCUS));
  private static final PickupSpec BASE_PICKUP = new PickupSpec(0, Energy.ZERO, 3, false, 4);
  private static final PickupSpec ADVANCED_PICKUP = new PickupSpec(3, new Energy(20), 9, false, 6);
  private static final PickupSpec ELITE_PICKUP = new PickupSpec(5, new Energy(30), 18, true, 8);
  private static final PickupSpec ULTIMATE_PICKUP = new PickupSpec(8, new Energy(60), 27, true, 12);
  private static final Energy TORCH_COST = new Energy(20);
  private static final Percentage RETURN_CHARGE = new Percentage(10);
  private static final AreaSpec BASE_AREA = AreaSpec.NONE;
  private static final AreaSpec ADVANCED_AREA = new AreaSpec(16, 8);
  private static final AreaSpec ELITE_AREA = new AreaSpec(32, 16);
  private static final AreaSpec ULTIMATE_AREA = new AreaSpec(64, 32);
  private static final TorchSpec BASE_TORCHES = new TorchSpec(TORCH_COST, new LightLevel(0));
  private static final TorchSpec ADVANCED_TORCHES = new TorchSpec(TORCH_COST, new LightLevel(1));
  private static final TorchSpec ELITE_TORCHES = new TorchSpec(TORCH_COST, new LightLevel(1));
  private static final TorchSpec ULTIMATE_TORCHES = new TorchSpec(TORCH_COST, new LightLevel(1));
  private static final long BASE_POWER_BANK = 210_000;
  private static final long ADVANCED_POWER_BANK = 420_000;
  private static final long ELITE_POWER_BANK = 2_100_000;
  private static final long ULTIMATE_POWER_BANK = 5_600_000;
  private static final TierSpec BASE_SPEC =
      new TierSpec(
          new Energy(BASE_BATTERY),
          new Energy(BASE_CONSUMPTION),
          new Energy(BASE_RECHARGE),
          BASE_SLOTS,
          BASE_HEALTH,
          BASE_CALL_RADIUS,
          BASE_SPEED_MULTIPLIER,
          new ConsumptionExtras(
              new Energy(BASE_FULL_INVENTORY_EXTRA),
              new Energy(BASE_WATER_EXTRA),
              new Energy(BASE_NEGATIVE_CONDITION_EXTRA),
              new Energy(BASE_CLIMB_EXTRA),
              new Energy(BASE_LAMP_CONSUMPTION)),
          new LightLevel(BASE_LAMP_LIGHT),
          new SolarOutput(new Energy(BASE_SOLAR_DAY), new Energy(BASE_SOLAR_NIGHT)),
          new ScannerSpec(
              BASE_ORE_SCAN_RADIUS,
              BASE_MOB_SCAN_RADIUS,
              BASE_ORE_SCAN_TICKS,
              BASE_MOB_SCAN_TICKS,
              new Energy(BASE_ORE_SCAN_COST),
              new Energy(BASE_MOB_SCAN_COST),
              BASE_ORE_FILTER_SLOTS),
          BASE_SHOCK,
          BASE_PICKUP,
          new Energy(BASE_POWER_BANK),
          BASE_TORCHES,
          RETURN_CHARGE,
          BASE_AREA);
  private static final TierSpec ADVANCED_SPEC =
      new TierSpec(
          new Energy(ADVANCED_BATTERY),
          new Energy(ADVANCED_CONSUMPTION),
          new Energy(ADVANCED_RECHARGE),
          ADVANCED_SLOTS,
          ADVANCED_HEALTH,
          ADVANCED_CALL_RADIUS,
          ADVANCED_SPEED_MULTIPLIER,
          new ConsumptionExtras(
              new Energy(ADVANCED_FULL_INVENTORY_EXTRA),
              new Energy(ADVANCED_WATER_EXTRA),
              new Energy(ADVANCED_NEGATIVE_CONDITION_EXTRA),
              new Energy(ADVANCED_CLIMB_EXTRA),
              new Energy(ADVANCED_LAMP_CONSUMPTION)),
          new LightLevel(ADVANCED_LAMP_LIGHT),
          new SolarOutput(new Energy(ADVANCED_SOLAR_DAY), new Energy(ADVANCED_SOLAR_NIGHT)),
          new ScannerSpec(
              ADVANCED_ORE_SCAN_RADIUS,
              ADVANCED_MOB_SCAN_RADIUS,
              ADVANCED_ORE_SCAN_TICKS,
              ADVANCED_MOB_SCAN_TICKS,
              new Energy(ADVANCED_ORE_SCAN_COST),
              new Energy(ADVANCED_MOB_SCAN_COST),
              ADVANCED_ORE_FILTER_SLOTS),
          ADVANCED_SHOCK,
          ADVANCED_PICKUP,
          new Energy(ADVANCED_POWER_BANK),
          ADVANCED_TORCHES,
          RETURN_CHARGE,
          ADVANCED_AREA);
  private static final TierSpec ELITE_SPEC =
      new TierSpec(
          new Energy(ELITE_BATTERY),
          new Energy(ELITE_CONSUMPTION),
          new Energy(ELITE_RECHARGE),
          ELITE_SLOTS,
          ELITE_HEALTH,
          ELITE_CALL_RADIUS,
          ELITE_SPEED_MULTIPLIER,
          new ConsumptionExtras(
              new Energy(ELITE_FULL_INVENTORY_EXTRA),
              new Energy(ELITE_WATER_EXTRA),
              new Energy(ELITE_NEGATIVE_CONDITION_EXTRA),
              new Energy(ELITE_CLIMB_EXTRA),
              new Energy(ELITE_LAMP_CONSUMPTION)),
          new LightLevel(ELITE_LAMP_LIGHT),
          new SolarOutput(new Energy(ELITE_SOLAR_DAY), new Energy(ELITE_SOLAR_NIGHT)),
          new ScannerSpec(
              ELITE_ORE_SCAN_RADIUS,
              ELITE_MOB_SCAN_RADIUS,
              ELITE_ORE_SCAN_TICKS,
              ELITE_MOB_SCAN_TICKS,
              new Energy(ELITE_ORE_SCAN_COST),
              new Energy(ELITE_MOB_SCAN_COST),
              ELITE_ORE_FILTER_SLOTS),
          ELITE_SHOCK,
          ELITE_PICKUP,
          new Energy(ELITE_POWER_BANK),
          ELITE_TORCHES,
          RETURN_CHARGE,
          ELITE_AREA);
  private static final TierSpec ULTIMATE_SPEC =
      new TierSpec(
          new Energy(ULTIMATE_BATTERY),
          new Energy(ULTIMATE_CONSUMPTION),
          new Energy(ULTIMATE_RECHARGE),
          ULTIMATE_SLOTS,
          ULTIMATE_HEALTH,
          ULTIMATE_CALL_RADIUS,
          ULTIMATE_SPEED_MULTIPLIER,
          new ConsumptionExtras(
              new Energy(ULTIMATE_FULL_INVENTORY_EXTRA),
              new Energy(ULTIMATE_WATER_EXTRA),
              new Energy(ULTIMATE_NEGATIVE_CONDITION_EXTRA),
              new Energy(ULTIMATE_CLIMB_EXTRA),
              new Energy(ULTIMATE_LAMP_CONSUMPTION)),
          new LightLevel(ULTIMATE_LAMP_LIGHT),
          new SolarOutput(new Energy(ULTIMATE_SOLAR_DAY), new Energy(ULTIMATE_SOLAR_NIGHT)),
          new ScannerSpec(
              ULTIMATE_ORE_SCAN_RADIUS,
              ULTIMATE_MOB_SCAN_RADIUS,
              ULTIMATE_ORE_SCAN_TICKS,
              ULTIMATE_MOB_SCAN_TICKS,
              new Energy(ULTIMATE_ORE_SCAN_COST),
              new Energy(ULTIMATE_MOB_SCAN_COST),
              ULTIMATE_ORE_FILTER_SLOTS),
          ULTIMATE_SHOCK,
          ULTIMATE_PICKUP,
          new Energy(ULTIMATE_POWER_BANK),
          ULTIMATE_TORCHES,
          RETURN_CHARGE,
          ULTIMATE_AREA);

  private TierDefaults() {}

  static TierSpec of(RobotTier tier) {
    return switch (tier) {
      case BASE -> BASE_SPEC;
      case ADVANCED -> ADVANCED_SPEC;
      case ELITE -> ELITE_SPEC;
      case ULTIMATE -> ULTIMATE_SPEC;
    };
  }
}
