package io.github.marcelofabianov.cubit.config;

import io.github.marcelofabianov.cubit.domain.behavior.LightLevel;
import io.github.marcelofabianov.cubit.domain.energy.ConsumptionExtras;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.SolarOutput;
import io.github.marcelofabianov.cubit.domain.tier.ScannerSpec;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

/** The config keys of one robot type at one tier. The key names are a contract with the pack. */
final class TierSection {
  private static final String BATTERY_CAPACITY = "battery_capacity";
  private static final String MOVING_CONSUMPTION = "moving_consumption_per_tick";
  private static final String FULL_INVENTORY_EXTRA = "full_inventory_extra_per_tick";
  private static final String WATER_EXTRA = "water_extra_per_tick";
  private static final String NEGATIVE_CONDITION_EXTRA = "negative_condition_extra_per_tick";
  private static final String CLIMB_EXTRA = "climb_extra_per_block";
  private static final String LAMP_CONSUMPTION = "lamp_consumption_per_tick";
  private static final String LAMP_LIGHT = "lamp_light_level";
  private static final int MIN_LIGHT = 0;
  private static final String SOLAR_DAY = "solar_day_per_tick";
  private static final String SOLAR_NIGHT = "solar_night_per_tick";
  private static final String RECHARGE = "recharge_per_tick";
  private static final String INVENTORY_SLOTS = "inventory_slots";
  private static final String MAX_HEALTH = "max_health";
  private static final String CALL_RADIUS = "call_radius_blocks";
  private static final String SPEED_MULTIPLIER = "speed_multiplier";
  private static final double MIN_SPEED_MULTIPLIER = 0.1;
  private static final double MAX_SPEED_MULTIPLIER = 4;
  private static final int MIN_POSITIVE = 1;
  private static final int MIN_CONSUMPTION = 0;
  private static final int MAX_ENERGY = Integer.MAX_VALUE;
  private static final int MAX_INVENTORY_SLOTS = 54;
  private static final int MAX_HEALTH_LIMIT = 1024;
  private static final String ORE_RADIUS = "ore_scan_radius_blocks";
  private static final String MOB_RADIUS = "mob_scan_radius_blocks";
  private static final String ORE_INTERVAL = "ore_scan_interval_ticks";
  private static final String MOB_INTERVAL = "mob_scan_interval_ticks";
  private static final String ORE_COST = "ore_scan_cost";
  private static final String MOB_COST = "mob_scan_cost";
  private static final String ORE_FILTER_SLOTS = "ore_filter_slots";
  private static final int MAX_SCAN_RADIUS = 64;
  private static final int MAX_SCAN_INTERVAL = 72000;
  private static final int MAX_ORE_FILTER_SLOTS = 64;

  private final ModConfigSpec.IntValue batteryCapacity;
  private final ModConfigSpec.IntValue movingConsumption;
  private final ExtraKeys extras;
  private final ModConfigSpec.IntValue lampLight;
  private final SolarKeys solar;
  private final ScannerKeys scanners;
  private final ModConfigSpec.IntValue recharge;
  private final ModConfigSpec.IntValue inventorySlots;
  private final ModConfigSpec.IntValue maxHealth;
  private final ModConfigSpec.IntValue callRadius;
  private final ModConfigSpec.DoubleValue speedMultiplier;
  private final ShockKeys shock;

  TierSection(ModConfigSpec.Builder builder, TierSpec defaults) {
    batteryCapacity = energy(builder, BATTERY_CAPACITY, defaults.batteryCapacity(), MIN_POSITIVE);
    movingConsumption =
        energy(builder, MOVING_CONSUMPTION, defaults.movingConsumptionPerTick(), MIN_CONSUMPTION);
    extras = new ExtraKeys(builder, defaults.extras());
    lampLight = light(builder, LAMP_LIGHT, defaults.lampLight());
    solar = new SolarKeys(builder, defaults.solar());
    scanners = new ScannerKeys(builder, defaults.scanners());
    recharge = energy(builder, RECHARGE, defaults.rechargePerTick(), MIN_POSITIVE);
    inventorySlots =
        count(builder, INVENTORY_SLOTS, defaults.inventorySlots(), MAX_INVENTORY_SLOTS);
    maxHealth =
        builder.defineInRange(MAX_HEALTH, defaults.maxHealth(), MIN_POSITIVE, MAX_HEALTH_LIMIT);
    callRadius =
        count(builder, CALL_RADIUS, defaults.callRadiusBlocks(), TierSpec.MAX_CALL_RADIUS_BLOCKS);
    speedMultiplier =
        builder.defineInRange(
            SPEED_MULTIPLIER,
            defaults.speedMultiplier(),
            MIN_SPEED_MULTIPLIER,
            MAX_SPEED_MULTIPLIER);
    shock = new ShockKeys(builder, defaults.shock());
  }

  TierSpec read() {
    return new TierSpec(
        new Energy(batteryCapacity.get()),
        new Energy(movingConsumption.get()),
        new Energy(recharge.get()),
        inventorySlots.get(),
        maxHealth.get(),
        callRadius.get(),
        speedMultiplier.get(),
        extras.read(),
        new LightLevel(lampLight.get()),
        solar.read(),
        scanners.read(),
        shock.read());
  }

  private static ModConfigSpec.IntValue count(
      ModConfigSpec.Builder builder, String key, int defaultValue, int max) {
    return builder.defineInRange(key, defaultValue, MIN_POSITIVE, max);
  }

  private static ModConfigSpec.IntValue light(
      ModConfigSpec.Builder builder, String key, LightLevel defaultValue) {
    return builder.defineInRange(key, defaultValue.value(), MIN_LIGHT, LightLevel.MAX);
  }

  private static ModConfigSpec.IntValue energy(
      ModConfigSpec.Builder builder, String key, Energy defaultValue, int min) {
    return builder.defineInRange(key, Math.toIntExact(defaultValue.amount()), min, MAX_ENERGY);
  }

  /** The extra consumption of one tier, in the order the keys have always had in the file. */
  private static final class ExtraKeys {
    private final ModConfigSpec.IntValue fullInventory;
    private final ModConfigSpec.IntValue water;
    private final ModConfigSpec.IntValue negativeCondition;
    private final ModConfigSpec.IntValue climb;
    private final ModConfigSpec.IntValue lamp;

    ExtraKeys(ModConfigSpec.Builder builder, ConsumptionExtras defaults) {
      fullInventory =
          energy(builder, FULL_INVENTORY_EXTRA, defaults.fullInventoryPerTick(), MIN_CONSUMPTION);
      water = energy(builder, WATER_EXTRA, defaults.waterPerTick(), MIN_CONSUMPTION);
      negativeCondition =
          energy(
              builder,
              NEGATIVE_CONDITION_EXTRA,
              defaults.negativeConditionPerTick(),
              MIN_CONSUMPTION);
      climb = energy(builder, CLIMB_EXTRA, defaults.climbPerBlock(), MIN_CONSUMPTION);
      lamp = energy(builder, LAMP_CONSUMPTION, defaults.lampPerTick(), MIN_CONSUMPTION);
    }

    ConsumptionExtras read() {
      return new ConsumptionExtras(
          new Energy(fullInventory.get()),
          new Energy(water.get()),
          new Energy(negativeCondition.get()),
          new Energy(climb.get()),
          new Energy(lamp.get()));
    }
  }

  /** The panel of one tier. A class of its own keeps the constructor above within the limit. */
  private static final class SolarKeys {
    private final ModConfigSpec.IntValue day;
    private final ModConfigSpec.IntValue night;

    SolarKeys(ModConfigSpec.Builder builder, SolarOutput defaults) {
      day = energy(builder, SOLAR_DAY, defaults.dayPerTick(), MIN_CONSUMPTION);
      night = energy(builder, SOLAR_NIGHT, defaults.nightPerTick(), MIN_CONSUMPTION);
    }

    SolarOutput read() {
      return new SolarOutput(new Energy(day.get()), new Energy(night.get()));
    }
  }

  /** The scanners of one tier. A class of its own keeps the constructor above within the limit. */
  private static final class ScannerKeys {
    private final ModConfigSpec.IntValue oreRadius;
    private final ModConfigSpec.IntValue mobRadius;
    private final ModConfigSpec.IntValue oreInterval;
    private final ModConfigSpec.IntValue mobInterval;
    private final ModConfigSpec.IntValue oreCost;
    private final ModConfigSpec.IntValue mobCost;
    private final ModConfigSpec.IntValue oreFilterSlots;

    ScannerKeys(ModConfigSpec.Builder builder, ScannerSpec defaults) {
      oreRadius = count(builder, ORE_RADIUS, defaults.oreRadiusBlocks(), MAX_SCAN_RADIUS);
      mobRadius = count(builder, MOB_RADIUS, defaults.mobRadiusBlocks(), MAX_SCAN_RADIUS);
      oreInterval = count(builder, ORE_INTERVAL, defaults.oreIntervalTicks(), MAX_SCAN_INTERVAL);
      mobInterval = count(builder, MOB_INTERVAL, defaults.mobIntervalTicks(), MAX_SCAN_INTERVAL);
      oreCost = energy(builder, ORE_COST, defaults.oreCost(), MIN_CONSUMPTION);
      mobCost = energy(builder, MOB_COST, defaults.mobCost(), MIN_CONSUMPTION);
      oreFilterSlots =
          count(builder, ORE_FILTER_SLOTS, defaults.oreFilterSlots(), MAX_ORE_FILTER_SLOTS);
    }

    ScannerSpec read() {
      return new ScannerSpec(
          oreRadius.get(),
          mobRadius.get(),
          oreInterval.get(),
          mobInterval.get(),
          new Energy(oreCost.get()),
          new Energy(mobCost.get()),
          oreFilterSlots.get());
    }
  }
}
