package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PowerBankUpgradeTest {
  private static final Energy BASE_CAPACITY = new Energy(210_000);
  private static final Energy ADVANCED_CAPACITY = new Energy(420_000);
  private static final Energy SMALL_CAPACITY = new Energy(100_000);
  private static final Energy CHARGE = new Energy(150_000);

  @Test
  void theChargeMovesToTheNextBank() {
    Battery next =
        PowerBankUpgrade.carry(new Battery(BASE_CAPACITY, CHARGE), ADVANCED_CAPACITY).orElseThrow();

    assertEquals(CHARGE, next.charge());
    assertEquals(ADVANCED_CAPACITY, next.capacity());
  }

  @Test
  void aChargeThatDoesNotFitRefusesTheRecipe() {
    assertTrue(
        PowerBankUpgrade.carry(new Battery(BASE_CAPACITY, CHARGE), SMALL_CAPACITY).isEmpty());
  }
}
