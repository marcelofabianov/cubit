package io.github.marcelofabianov.cubit.config;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

/** The config keys of one robot type at one tier. The key names are a contract with the pack. */
final class TierSection {
  private static final String BATTERY_CAPACITY = "battery_capacity";
  private static final String MOVING_CONSUMPTION = "moving_consumption_per_tick";
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
  private final ModConfigSpec.IntValue recharge;
  private final ModConfigSpec.IntValue inventorySlots;
  private final ModConfigSpec.IntValue maxHealth;
  private final ModConfigSpec.IntValue callRadius;
  private final ModConfigSpec.DoubleValue speedMultiplier;

  TierSection(ModConfigSpec.Builder builder, TierSpec defaults) {
    batteryCapacity = energy(builder, BATTERY_CAPACITY, defaults.batteryCapacity(), MIN_POSITIVE);
    movingConsumption =
        energy(builder, MOVING_CONSUMPTION, defaults.movingConsumptionPerTick(), MIN_CONSUMPTION);
    recharge = energy(builder, RECHARGE, defaults.rechargePerTick(), MIN_POSITIVE);
    inventorySlots =
        builder.defineInRange(
            INVENTORY_SLOTS, defaults.inventorySlots(), MIN_POSITIVE, MAX_INVENTORY_SLOTS);
    maxHealth =
        builder.defineInRange(MAX_HEALTH, defaults.maxHealth(), MIN_POSITIVE, MAX_HEALTH_LIMIT);
    callRadius =
        builder.defineInRange(
            CALL_RADIUS, defaults.callRadiusBlocks(), MIN_POSITIVE, MAX_CALL_RADIUS);
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
        speedMultiplier.get());
  }

  private static ModConfigSpec.IntValue energy(
      ModConfigSpec.Builder builder, String key, Energy defaultValue, int min) {
    return builder.defineInRange(key, Math.toIntExact(defaultValue.amount()), min, MAX_ENERGY);
  }
}
