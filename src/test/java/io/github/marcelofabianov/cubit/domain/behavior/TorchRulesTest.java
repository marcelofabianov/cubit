package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.tier.TorchSpec;
import org.junit.jupiter.api.Test;

class TorchRulesTest {
  private static final Energy CAPACITY = new Energy(600_000);
  private static final Energy COST = new Energy(20);
  private static final TorchSpec ADVANCED = new TorchSpec(COST, new LightLevel(1));
  private static final TorchSpec BASE = TorchSpec.NONE;
  private static final Battery CHARGED = new Battery(CAPACITY, new Energy(1_000));
  private static final Battery SHORT = new Battery(CAPACITY, new Energy(19));
  private static final LightLevel DARK = new LightLevel(0);
  private static final LightLevel LIT = new LightLevel(1);
  private static final LightLevel NIGHT = new LightLevel(4);
  private static final LightLevel DAY = new LightLevel(15);
  private static final LightLevel LAMP_ON_BELOW = new LightLevel(8);
  private static final boolean ON = true;
  private static final boolean OFF = false;
  private static final boolean SKY = true;
  private static final boolean CAVE = false;

  @Test
  void anActiveRobotWithTorchesItsSwitchAndChargePlaces() {
    assertTrue(TorchRules.places(PowerState.ACTIVE, ON, ADVANCED, ON, CHARGED));
  }

  @Test
  void placingNeedsEveryCondition() {
    assertFalse(TorchRules.places(PowerState.HIBERNATING, ON, ADVANCED, ON, CHARGED));
    assertFalse(TorchRules.places(PowerState.ACTIVE, OFF, ADVANCED, ON, CHARGED));
    assertFalse(TorchRules.places(PowerState.ACTIVE, ON, BASE, ON, CHARGED));
    assertFalse(TorchRules.places(PowerState.ACTIVE, ON, ADVANCED, OFF, CHARGED));
    assertFalse(TorchRules.places(PowerState.ACTIVE, ON, ADVANCED, ON, SHORT));
  }

  @Test
  void aDarkCaveIsDangerousAtAnyTime() {
    assertTrue(TorchRules.dangerous(DARK, ADVANCED, CAVE, DAY, LAMP_ON_BELOW));
    assertFalse(TorchRules.dangerous(LIT, ADVANCED, CAVE, DAY, LAMP_ON_BELOW));
  }

  @Test
  void underTheSkyOnlyWhenTheLampWouldLight() {
    assertFalse(TorchRules.dangerous(DARK, ADVANCED, SKY, DAY, LAMP_ON_BELOW));
    assertTrue(TorchRules.dangerous(DARK, ADVANCED, SKY, NIGHT, LAMP_ON_BELOW));
  }

  @Test
  void placingChargesTheCostOfOneTorch() {
    assertEquals(CHARGED.charge().minus(COST), TorchRules.pay(CHARGED, ADVANCED).charge());
  }
}
