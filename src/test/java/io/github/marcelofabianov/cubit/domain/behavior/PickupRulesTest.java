package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.tier.PickupSpec;
import org.junit.jupiter.api.Test;

class PickupRulesTest {
  private static final Energy CAPACITY = new Energy(600_000);
  private static final Energy COST = new Energy(20);
  private static final int RADIUS = 3;
  private static final int SLOTS = 9;
  private static final PickupSpec ADVANCED = new PickupSpec(RADIUS, COST, SLOTS, false);
  private static final PickupSpec BASE = new PickupSpec(0, Energy.ZERO, SLOTS, false);
  private static final Battery CHARGED = new Battery(CAPACITY, new Energy(1_000));
  private static final Battery SHORT = new Battery(CAPACITY, new Energy(19));
  private static final boolean ON = true;
  private static final boolean OFF = false;

  @Test
  void onlyAnActiveRobotWithPickupOnCollects() {
    for (PowerState state : PowerState.values()) {
      assertEquals(state == PowerState.ACTIVE, PickupRules.collects(state, ON));
    }
    assertFalse(PickupRules.collects(PowerState.ACTIVE, OFF));
  }

  @Test
  void theMagnetPullsWhenEverythingAllows() {
    assertTrue(PickupRules.magnetPulls(PowerState.ACTIVE, ON, ON, ADVANCED, CHARGED));
  }

  @Test
  void theMagnetNeedsPickupItsSwitchAndAnUnlockedTier() {
    assertFalse(PickupRules.magnetPulls(PowerState.ACTIVE, OFF, ON, ADVANCED, CHARGED));
    assertFalse(PickupRules.magnetPulls(PowerState.ACTIVE, ON, OFF, ADVANCED, CHARGED));
    assertFalse(PickupRules.magnetPulls(PowerState.ACTIVE, ON, ON, BASE, CHARGED));
    assertFalse(PickupRules.magnetPulls(PowerState.HIBERNATING, ON, ON, ADVANCED, CHARGED));
  }

  @Test
  void theMagnetNeedsTheWholeCost() {
    assertFalse(PickupRules.magnetPulls(PowerState.ACTIVE, ON, ON, ADVANCED, SHORT));
  }

  @Test
  void pullingChargesTheCostOfOneItem() {
    Battery after = PickupRules.payForPull(CHARGED, ADVANCED);

    assertEquals(CHARGED.charge().minus(COST), after.charge());
  }
}
