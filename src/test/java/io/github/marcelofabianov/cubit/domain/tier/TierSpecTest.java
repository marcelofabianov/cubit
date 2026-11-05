package io.github.marcelofabianov.cubit.domain.tier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.ConsumptionExtras;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.energy.RobotActivity;
import java.util.List;
import org.junit.jupiter.api.Test;

class TierSpecTest {
  private static final ConsumptionExtras NO_EXTRAS =
      new ConsumptionExtras(Energy.ZERO, Energy.ZERO, Energy.ZERO, Energy.ZERO);
  private static final RobotActivity WALKING = new RobotActivity(true, false, false, false, 0);
  private static final int ANY_SLOTS = 27;
  private static final int ANY_HEALTH = 25;
  private static final int ANY_RADIUS = 15;
  private static final Energy BASE_CAPACITY = new Energy(300_000);
  private static final Energy BASE_MOVING = new Energy(10);
  private static final Energy BASE_RECHARGE = new Energy(500);
  private static final TierSpec BASE = spec(BASE_CAPACITY, BASE_MOVING, BASE_RECHARGE);
  private static final TierSpec ULTIMATE =
      spec(new Energy(8_000_000), new Energy(20), new Energy(5_000));
  private static final Percentage EMPTY = new Percentage(0);
  private static final Percentage HALF = new Percentage(50);
  private static final Percentage CRAFT_CHARGE = new Percentage(25);
  private static final Percentage FULL = new Percentage(100);
  private static final Energy BASE_CHARGE_AFTER_CRAFT = new Energy(75_000);
  private static final Energy BASE_AFTER_MOVING = new Energy(299_990);
  private static final Energy LESS_THAN_MOVING = new Energy(4);
  private static final Energy ULTIMATE_AFTER_MOVING = new Energy(3_999_980);
  private static final Energy ULTIMATE_AFTER_RECHARGE = new Energy(4_005_000);
  private static final Energy ONE = new Energy(1);
  private static final double ULTIMATE_SPEED = 2.0;

  private static TierSpec spec(Energy capacity, Energy moving, Energy recharge) {
    return new TierSpec(
        capacity, moving, recharge, ANY_SLOTS, ANY_HEALTH, ANY_RADIUS, 1, NO_EXTRAS);
  }

  @Test
  void newBatteryStartsWithTheGivenChargeOfItsCapacity() {
    assertEquals(BASE_CHARGE_AFTER_CRAFT, BASE.newBattery(CRAFT_CHARGE).charge());
    assertEquals(BASE_CAPACITY, BASE.newBattery(CRAFT_CHARGE).capacity());
  }

  @Test
  void movingForOneTickSpendsTheTierConsumption() {
    assertEquals(
        BASE_AFTER_MOVING,
        BASE.spendOneTick(BASE.newBattery(FULL), PowerState.ACTIVE, WALKING).charge());
  }

  @Test
  void movingWithLessThanTheConsumptionEmptiesTheBattery() {
    Battery battery = new Battery(BASE_CAPACITY, LESS_THAN_MOVING);
    assertEquals(Energy.ZERO, BASE.spendOneTick(battery, PowerState.ACTIVE, WALKING).charge());
  }

  @Test
  void rechargingForOneTickReceivesTheTierRate() {
    assertEquals(BASE_RECHARGE, BASE.rechargeOneTick(BASE.newBattery(EMPTY)).charge());
  }

  @Test
  void differentTiersApplyTheirOwnNumbers() {
    Battery battery = ULTIMATE.newBattery(HALF);
    assertEquals(
        ULTIMATE_AFTER_MOVING, ULTIMATE.spendOneTick(battery, PowerState.ACTIVE, WALKING).charge());
    assertEquals(ULTIMATE_AFTER_RECHARGE, ULTIMATE.rechargeOneTick(battery).charge());
  }

  @Test
  void rejectsZeroValuesThatWouldBreakTheRobot() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new TierSpec(Energy.ZERO, ONE, ONE, 1, 1, 1, 1, NO_EXTRAS));
    assertThrows(
        IllegalArgumentException.class,
        () -> new TierSpec(ONE, ONE, Energy.ZERO, 1, 1, 1, 1, NO_EXTRAS));
    assertThrows(
        IllegalArgumentException.class, () -> new TierSpec(ONE, ONE, ONE, 0, 1, 1, 1, NO_EXTRAS));
    assertThrows(
        IllegalArgumentException.class, () -> new TierSpec(ONE, ONE, ONE, 1, 0, 1, 1, NO_EXTRAS));
    assertThrows(
        IllegalArgumentException.class, () -> new TierSpec(ONE, ONE, ONE, 1, 1, 0, 1, NO_EXTRAS));
  }

  @Test
  void rejectsSpeedThatWouldStopTheRobot() {
    assertThrows(IllegalArgumentException.class, () -> speedOf(0));
    assertThrows(IllegalArgumentException.class, () -> speedOf(Double.NaN));
  }

  @Test
  void keepsTheSpeedMultiplierOfTheTier() {
    assertEquals(ULTIMATE_SPEED, speedOf(ULTIMATE_SPEED).speedMultiplier());
  }

  private static TierSpec speedOf(double multiplier) {
    return new TierSpec(ONE, ONE, ONE, 1, 1, 1, multiplier, NO_EXTRAS);
  }

  @Test
  void hasFourTiersInEvolutionOrder() {
    assertEquals(
        List.of(RobotTier.BASE, RobotTier.ADVANCED, RobotTier.ELITE, RobotTier.ULTIMATE),
        List.of(RobotTier.values()));
  }
}
