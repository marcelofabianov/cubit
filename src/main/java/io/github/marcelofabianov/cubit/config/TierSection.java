package io.github.marcelofabianov.cubit.config;

import io.github.marcelofabianov.cubit.domain.behavior.LightLevel;
import io.github.marcelofabianov.cubit.domain.energy.ConsumptionExtras;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.SolarOutput;
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
  private static final int MAX_CALL_RADIUS = 128;

  private final ModConfigSpec.IntValue batteryCapacity;
  private final ModConfigSpec.IntValue movingConsumption;
  private final ModConfigSpec.IntValue fullInventoryExtra;
  private final ModConfigSpec.IntValue waterExtra;
  private final ModConfigSpec.IntValue negativeConditionExtra;
  private final ModConfigSpec.IntValue climbExtra;
  private final ModConfigSpec.IntValue lampConsumption;
  private final ModConfigSpec.IntValue lampLight;
  private final SolarKeys solar;
  private final ModConfigSpec.IntValue recharge;
  private final ModConfigSpec.IntValue inventorySlots;
  private final ModConfigSpec.IntValue maxHealth;
  private final ModConfigSpec.IntValue callRadius;
  private final ModConfigSpec.DoubleValue speedMultiplier;

  TierSection(ModConfigSpec.Builder builder, TierSpec defaults) {
    batteryCapacity = energy(builder, BATTERY_CAPACITY, defaults.batteryCapacity(), MIN_POSITIVE);
    movingConsumption =
        energy(builder, MOVING_CONSUMPTION, defaults.movingConsumptionPerTick(), MIN_CONSUMPTION);
    ConsumptionExtras extras = defaults.extras();
    fullInventoryExtra =
        energy(builder, FULL_INVENTORY_EXTRA, extras.fullInventoryPerTick(), MIN_CONSUMPTION);
    waterExtra = energy(builder, WATER_EXTRA, extras.waterPerTick(), MIN_CONSUMPTION);
    negativeConditionExtra =
        energy(
            builder, NEGATIVE_CONDITION_EXTRA, extras.negativeConditionPerTick(), MIN_CONSUMPTION);
    climbExtra = energy(builder, CLIMB_EXTRA, extras.climbPerBlock(), MIN_CONSUMPTION);
    lampConsumption = energy(builder, LAMP_CONSUMPTION, extras.lampPerTick(), MIN_CONSUMPTION);
    lampLight = light(builder, LAMP_LIGHT, defaults.lampLight());
    solar = new SolarKeys(builder, defaults.solar());
    recharge = energy(builder, RECHARGE, defaults.rechargePerTick(), MIN_POSITIVE);
    inventorySlots =
        count(builder, INVENTORY_SLOTS, defaults.inventorySlots(), MAX_INVENTORY_SLOTS);
    maxHealth =
        builder.defineInRange(MAX_HEALTH, defaults.maxHealth(), MIN_POSITIVE, MAX_HEALTH_LIMIT);
    callRadius = count(builder, CALL_RADIUS, defaults.callRadiusBlocks(), MAX_CALL_RADIUS);
    speedMultiplier =
        builder.defineInRange(
            SPEED_MULTIPLIER,
            defaults.speedMultiplier(),
            MIN_SPEED_MULTIPLIER,
            MAX_SPEED_MULTIPLIER);
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
        new ConsumptionExtras(
            new Energy(fullInventoryExtra.get()),
            new Energy(waterExtra.get()),
            new Energy(negativeConditionExtra.get()),
            new Energy(climbExtra.get()),
            new Energy(lampConsumption.get())),
        new LightLevel(lampLight.get()),
        solar.read());
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
}
