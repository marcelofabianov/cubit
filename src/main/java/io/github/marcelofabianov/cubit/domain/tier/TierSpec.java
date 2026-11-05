package io.github.marcelofabianov.cubit.domain.tier;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.ConsumptionExtras;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.energy.RobotActivity;

/** The numbers of one robot type at one tier. They come from the config, never from the code. */
public record TierSpec(
    Energy batteryCapacity,
    Energy movingConsumptionPerTick,
    Energy rechargePerTick,
    int inventorySlots,
    int maxHealth,
    int callRadiusBlocks,
    double speedMultiplier,
    ConsumptionExtras extras) {

  private static final String BATTERY_CAPACITY = "batteryCapacity";
  private static final String RECHARGE_PER_TICK = "rechargePerTick";
  private static final String INVENTORY_SLOTS = "inventorySlots";
  private static final String MAX_HEALTH = "maxHealth";
  private static final String CALL_RADIUS_BLOCKS = "callRadiusBlocks";
  private static final String SPEED_MULTIPLIER = "speedMultiplier";
  private static final String NOT_POSITIVE_MESSAGE = " must be positive: ";

  public TierSpec {
    requirePositive(batteryCapacity.amount(), BATTERY_CAPACITY);
    requirePositive(rechargePerTick.amount(), RECHARGE_PER_TICK);
    requirePositive(inventorySlots, INVENTORY_SLOTS);
    requirePositive(maxHealth, MAX_HEALTH);
    requirePositive(callRadiusBlocks, CALL_RADIUS_BLOCKS);
    if (!(speedMultiplier > 0)) {
      throw new IllegalArgumentException(SPEED_MULTIPLIER + NOT_POSITIVE_MESSAGE + speedMultiplier);
    }
  }

  public Battery newBattery(Percentage initialCharge) {
    return Battery.withCharge(batteryCapacity, initialCharge);
  }

  /** Only an active robot spends, and it may spend down to an empty battery. */
  public Battery spendOneTick(Battery battery, PowerState state, RobotActivity activity) {
    if (state != PowerState.ACTIVE) {
      return battery;
    }
    Energy walking = activity.walking() ? movingConsumptionPerTick : Energy.ZERO;
    return battery.consume(walking.plus(extras.of(activity))).battery();
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
