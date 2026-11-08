package io.github.marcelofabianov.cubit.domain.tier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.marcelofabianov.cubit.domain.behavior.LightLevel;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.ConsumptionExtras;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.energy.RobotActivity;
import io.github.marcelofabianov.cubit.domain.energy.SolarOutput;
import org.junit.jupiter.api.Test;

class TierSpecConsumptionTest {
  private static final long CAPACITY = 300_000;
  private static final int ANY_SLOTS = 27;
  private static final int ANY_HEALTH = 25;
  private static final int ANY_RADIUS = 15;
  private static final TierSpec BASE = spec(15, 5, 5, 25, 15, 1);
  private static final TierSpec ULTIMATE = spec(30, 10, 10, 250, 30, 2);
  private static final double NONE = 0;
  private static final double ONE_BLOCK = 1;
  private static final double SLAB = 0.5;
  private static final long WALK = 15;
  private static final long WALK_LOADED = 20;
  private static final long WALK_LOADED_IN_WATER = 25;
  private static final long DRAIN = 25;
  private static final long WALK_AND_DRAIN = 40;
  private static final long WALK_AND_CLIMB = 30;
  private static final long WALK_AND_SLAB = 23;
  private static final long EVERYTHING = 65;
  private static final long ULTIMATE_EVERYTHING = 330;
  private static final long LESS_THAN_DRAIN = 10;
  private static final double BELOW_ZERO = -0.5;
  private static final long BASE_LAMP = 1;
  private static final long ULTIMATE_LAMP = 2;
  private static final long WALK_WITH_LAMP = 16;

  private static TierSpec spec(
      long walk, long full, long water, long negative, long climb, long lamp) {
    return new TierSpec(
        new Energy(CAPACITY),
        new Energy(walk),
        new Energy(1),
        ANY_SLOTS,
        ANY_HEALTH,
        ANY_RADIUS,
        1,
        new ConsumptionExtras(
            new Energy(full),
            new Energy(water),
            new Energy(negative),
            new Energy(climb),
            new Energy(lamp)),
        new LightLevel(0),
        new SolarOutput(Energy.ZERO, Energy.ZERO),
        new ScannerSpec(1, 1, 1, 1, Energy.ZERO, Energy.ZERO, 1));
  }

  private static RobotActivity walking(boolean full, boolean water, boolean negative, double up) {
    return new RobotActivity(true, full, water, negative, up, false);
  }

  private static RobotActivity still(boolean full, boolean water, boolean negative) {
    return new RobotActivity(false, full, water, negative, NONE, false);
  }

  private static void assertSpends(long expected, TierSpec spec, RobotActivity activity) {
    Battery full = new Battery(new Energy(CAPACITY), new Energy(CAPACITY));
    Battery after = spec.spendOneTick(full, PowerState.ACTIVE, activity);
    assertEquals(new Energy(CAPACITY - expected), after.charge());
  }

  @Test
  void standingStillSpendsNothing() {
    assertSpends(0, BASE, still(false, false, false));
  }

  @Test
  void loadAndWaterOnlyCostWhenWalking() {
    assertSpends(0, BASE, still(true, true, false));
  }

  @Test
  void walkingOnPlainGroundSpendsTheTierConsumption() {
    assertSpends(WALK, BASE, walking(false, false, false, NONE));
  }

  @Test
  void aFullInventoryAddsItsExtra() {
    assertSpends(WALK_LOADED, BASE, walking(true, false, false, NONE));
  }

  @Test
  void waterAddsItsExtra() {
    assertSpends(WALK_LOADED, BASE, walking(false, true, false, NONE));
  }

  @Test
  void aFullInventoryInWaterAddsBothExtras() {
    assertSpends(WALK_LOADED_IN_WATER, BASE, walking(true, true, false, NONE));
  }

  @Test
  void aNegativeConditionCostsEvenStandingStill() {
    assertSpends(DRAIN, BASE, still(false, false, true));
  }

  @Test
  void aNegativeConditionAddsToWalking() {
    assertSpends(WALK_AND_DRAIN, BASE, walking(false, false, true, NONE));
  }

  @Test
  void climbingOneBlockAddsItsExtra() {
    assertSpends(WALK_AND_CLIMB, BASE, walking(false, false, false, ONE_BLOCK));
  }

  @Test
  void climbingIsProportionalToTheHeightAndRoundedToWholeEnergy() {
    assertSpends(WALK_AND_SLAB, BASE, walking(false, false, false, SLAB));
  }

  @Test
  void climbingOnlyCostsWhenWalking() {
    assertSpends(0, BASE, new RobotActivity(false, false, false, false, ONE_BLOCK, false));
  }

  @Test
  void everyExtraAddsUp() {
    assertSpends(EVERYTHING, BASE, walking(true, true, true, ONE_BLOCK));
  }

  @Test
  void eachTierAppliesItsOwnNumbers() {
    assertSpends(ULTIMATE_EVERYTHING, ULTIMATE, walking(true, true, true, ONE_BLOCK));
  }

  @Test
  void aLitLampCostsEvenStandingStill() {
    assertSpends(BASE_LAMP, BASE, new RobotActivity(false, false, false, false, NONE, true));
  }

  @Test
  void aLitLampAddsToWalking() {
    assertSpends(WALK_WITH_LAMP, BASE, new RobotActivity(true, false, false, false, NONE, true));
  }

  @Test
  void eachTierPaysItsOwnLamp() {
    assertSpends(
        ULTIMATE_LAMP, ULTIMATE, new RobotActivity(false, false, false, false, NONE, true));
  }

  @Test
  void onlyAnActiveRobotPaysForTheLamp() {
    Battery full = new Battery(new Energy(CAPACITY), new Energy(CAPACITY));
    RobotActivity lit = new RobotActivity(false, false, false, false, NONE, true);
    for (PowerState state : PowerState.values()) {
      if (state != PowerState.ACTIVE) {
        assertEquals(full, BASE.spendOneTick(full, state, lit));
      }
    }
  }

  @Test
  void onlyAnActiveRobotSpends() {
    Battery full = new Battery(new Energy(CAPACITY), new Energy(CAPACITY));
    RobotActivity everything = walking(true, true, true, ONE_BLOCK);
    for (PowerState state : PowerState.values()) {
      if (state != PowerState.ACTIVE) {
        assertEquals(full, BASE.spendOneTick(full, state, everything));
      }
    }
  }

  @Test
  void theDrainCanEmptyTheBatteryWithoutGoingBelowZero() {
    Battery low = new Battery(new Energy(CAPACITY), new Energy(LESS_THAN_DRAIN));
    Battery after = BASE.spendOneTick(low, PowerState.ACTIVE, still(false, false, true));
    assertEquals(Energy.ZERO, after.charge());
  }

  @Test
  void rejectsAnInvalidClimbedHeight() {
    assertThrows(IllegalArgumentException.class, () -> walking(false, false, false, BELOW_ZERO));
    assertThrows(IllegalArgumentException.class, () -> walking(false, false, false, Double.NaN));
  }
}
