package io.github.marcelofabianov.cubit.domain.tier;

import io.github.marcelofabianov.cubit.domain.behavior.LightLevel;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.ConsumptionExtras;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.energy.RobotActivity;
import io.github.marcelofabianov.cubit.domain.energy.SolarOutput;

/** The numbers of one robot type at one tier. They come from the config, never from the code. */
public record TierSpec(
    Energy batteryCapacity,
    Energy movingConsumptionPerTick,
    Energy rechargePerTick,
    int inventorySlots,
    int maxHealth,
    int callRadiusBlocks,
    double speedMultiplier,
    ConsumptionExtras extras,
    LightLevel lampLight,
    SolarOutput solar,
    ScannerSpec scanners,
    ShockSpec shock,
    PickupSpec pickup,
    Energy powerBankCapacity,
    TorchSpec torches,
    Percentage returnCharge,
    AreaSpec area,
    UnloadSpec unload) {

  /** The largest radius the config accepts, which also limits the search for a robot to call. */
  public static final int MAX_CALL_RADIUS_BLOCKS = 128;

  private static final Percentage NO_RETURN_CHARGE = new Percentage(0);
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

  /** A tier without unload numbers marks no chest, which only tests need. */
  public TierSpec(
      Energy batteryCapacity,
      Energy movingConsumptionPerTick,
      Energy rechargePerTick,
      int inventorySlots,
      int maxHealth,
      int callRadiusBlocks,
      double speedMultiplier,
      ConsumptionExtras extras,
      LightLevel lampLight,
      SolarOutput solar,
      ScannerSpec scanners,
      ShockSpec shock,
      PickupSpec pickup,
      Energy powerBankCapacity,
      TorchSpec torches,
      Percentage returnCharge,
      AreaSpec area) {
    this(
        batteryCapacity,
        movingConsumptionPerTick,
        rechargePerTick,
        inventorySlots,
        maxHealth,
        callRadiusBlocks,
        speedMultiplier,
        extras,
        lampLight,
        solar,
        scanners,
        shock,
        pickup,
        powerBankCapacity,
        torches,
        returnCharge,
        area,
        UnloadSpec.NONE);
  }

  /** A tier without area numbers has no area mode, which only tests need. */
  public TierSpec(
      Energy batteryCapacity,
      Energy movingConsumptionPerTick,
      Energy rechargePerTick,
      int inventorySlots,
      int maxHealth,
      int callRadiusBlocks,
      double speedMultiplier,
      ConsumptionExtras extras,
      LightLevel lampLight,
      SolarOutput solar,
      ScannerSpec scanners,
      ShockSpec shock,
      PickupSpec pickup,
      Energy powerBankCapacity,
      TorchSpec torches,
      Percentage returnCharge) {
    this(
        batteryCapacity,
        movingConsumptionPerTick,
        rechargePerTick,
        inventorySlots,
        maxHealth,
        callRadiusBlocks,
        speedMultiplier,
        extras,
        lampLight,
        solar,
        scanners,
        shock,
        pickup,
        powerBankCapacity,
        torches,
        returnCharge,
        AreaSpec.NONE);
  }

  /** A tier without a return charge only goes back to a Dock when empty, which only tests need. */
  public TierSpec(
      Energy batteryCapacity,
      Energy movingConsumptionPerTick,
      Energy rechargePerTick,
      int inventorySlots,
      int maxHealth,
      int callRadiusBlocks,
      double speedMultiplier,
      ConsumptionExtras extras,
      LightLevel lampLight,
      SolarOutput solar,
      ScannerSpec scanners,
      ShockSpec shock,
      PickupSpec pickup,
      Energy powerBankCapacity,
      TorchSpec torches) {
    this(
        batteryCapacity,
        movingConsumptionPerTick,
        rechargePerTick,
        inventorySlots,
        maxHealth,
        callRadiusBlocks,
        speedMultiplier,
        extras,
        lampLight,
        solar,
        scanners,
        shock,
        pickup,
        powerBankCapacity,
        torches,
        NO_RETURN_CHARGE);
  }

  /** A tier without torch numbers has its torches locked, which only tests need. */
  public TierSpec(
      Energy batteryCapacity,
      Energy movingConsumptionPerTick,
      Energy rechargePerTick,
      int inventorySlots,
      int maxHealth,
      int callRadiusBlocks,
      double speedMultiplier,
      ConsumptionExtras extras,
      LightLevel lampLight,
      SolarOutput solar,
      ScannerSpec scanners,
      ShockSpec shock,
      PickupSpec pickup,
      Energy powerBankCapacity) {
    this(
        batteryCapacity,
        movingConsumptionPerTick,
        rechargePerTick,
        inventorySlots,
        maxHealth,
        callRadiusBlocks,
        speedMultiplier,
        extras,
        lampLight,
        solar,
        scanners,
        shock,
        pickup,
        powerBankCapacity,
        TorchSpec.NONE);
  }

  /** A tier without a Power Bank number has none, which only tests need. */
  public TierSpec(
      Energy batteryCapacity,
      Energy movingConsumptionPerTick,
      Energy rechargePerTick,
      int inventorySlots,
      int maxHealth,
      int callRadiusBlocks,
      double speedMultiplier,
      ConsumptionExtras extras,
      LightLevel lampLight,
      SolarOutput solar,
      ScannerSpec scanners,
      ShockSpec shock,
      PickupSpec pickup) {
    this(
        batteryCapacity,
        movingConsumptionPerTick,
        rechargePerTick,
        inventorySlots,
        maxHealth,
        callRadiusBlocks,
        speedMultiplier,
        extras,
        lampLight,
        solar,
        scanners,
        shock,
        pickup,
        Energy.ZERO);
  }

  /** A tier without pickup numbers has no magnet and no filter slots. */
  public TierSpec(
      Energy batteryCapacity,
      Energy movingConsumptionPerTick,
      Energy rechargePerTick,
      int inventorySlots,
      int maxHealth,
      int callRadiusBlocks,
      double speedMultiplier,
      ConsumptionExtras extras,
      LightLevel lampLight,
      SolarOutput solar,
      ScannerSpec scanners,
      ShockSpec shock) {
    this(
        batteryCapacity,
        movingConsumptionPerTick,
        rechargePerTick,
        inventorySlots,
        maxHealth,
        callRadiusBlocks,
        speedMultiplier,
        extras,
        lampLight,
        solar,
        scanners,
        shock,
        PickupSpec.NONE);
  }

  /** A tier without shock numbers only has Peaceful, like Base. */
  public TierSpec(
      Energy batteryCapacity,
      Energy movingConsumptionPerTick,
      Energy rechargePerTick,
      int inventorySlots,
      int maxHealth,
      int callRadiusBlocks,
      double speedMultiplier,
      ConsumptionExtras extras,
      LightLevel lampLight,
      SolarOutput solar,
      ScannerSpec scanners) {
    this(
        batteryCapacity,
        movingConsumptionPerTick,
        rechargePerTick,
        inventorySlots,
        maxHealth,
        callRadiusBlocks,
        speedMultiplier,
        extras,
        lampLight,
        solar,
        scanners,
        ShockSpec.NONE);
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
