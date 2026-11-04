package io.github.marcelofabianov.cubit.domain.tier;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;

/** The numbers of one robot type at one tier. They come from the config, never from the code. */
public record TierSpec(
    Energy batteryCapacity,
    Energy movingConsumptionPerTick,
    Energy rechargePerTick,
    int inventorySlots,
    int maxHealth,
    int callRadiusBlocks) {

  private static final String BATTERY_CAPACITY = "batteryCapacity";
  private static final String RECHARGE_PER_TICK = "rechargePerTick";
  private static final String INVENTORY_SLOTS = "inventorySlots";
  private static final String MAX_HEALTH = "maxHealth";
  private static final String CALL_RADIUS_BLOCKS = "callRadiusBlocks";
  private static final String NOT_POSITIVE_MESSAGE = " must be positive: ";

  public TierSpec {
    requirePositive(batteryCapacity.amount(), BATTERY_CAPACITY);
    requirePositive(rechargePerTick.amount(), RECHARGE_PER_TICK);
    requirePositive(inventorySlots, INVENTORY_SLOTS);
    requirePositive(maxHealth, MAX_HEALTH);
    requirePositive(callRadiusBlocks, CALL_RADIUS_BLOCKS);
  }

  public Battery newBattery(Percentage initialCharge) {
    return Battery.withCharge(batteryCapacity, initialCharge);
  }

  public Battery moveOneTick(Battery battery) {
    return battery.consume(movingConsumptionPerTick).battery();
  }

  public Battery rechargeOneTick(Battery battery) {
    return battery.recharge(rechargePerTick).battery();
  }

  private static void requirePositive(long value, String name) {
    if (value <= 0) {
      throw new IllegalArgumentException(name + NOT_POSITIVE_MESSAGE + value);
    }
  }
}
