package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import org.junit.jupiter.api.Test;

class RobotStateEnergyTest {
  private static final float HEALTH = 20f;
  private static final Energy WAKE_CHARGE = new Energy(15_000);

  @Test
  void anotherChargeKeepsEverythingElse() {
    RobotState state =
        new RobotState(RobotTier.ELITE, Energy.ZERO, HEALTH)
            .withPickup(PickupSettings.INITIAL.toggledMagnet());

    RobotState charged = state.withEnergy(WAKE_CHARGE);

    assertEquals(WAKE_CHARGE, charged.energy());
    assertEquals(state.withEnergy(Energy.ZERO), state);
    assertEquals(state.pickup(), charged.pickup());
    assertEquals(state.tier(), charged.tier());
  }
}
