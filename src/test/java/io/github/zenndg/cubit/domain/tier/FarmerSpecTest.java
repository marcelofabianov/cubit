package io.github.zenndg.cubit.domain.tier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.zenndg.cubit.domain.energy.Battery;
import io.github.zenndg.cubit.domain.energy.Energy;
import org.junit.jupiter.api.Test;

class FarmerSpecTest {
  private static final Energy COST = new Energy(50);
  private static final Energy CAPACITY = new Energy(1_000);
  private static final Energy LITTLE = new Energy(10);
  private static final int PAUSE = 20;
  private static final int NO_PAUSE = 0;
  private static final int FORTUNE_II = 2;
  private static final int TOO_MUCH = 4;
  private static final boolean ON = true;
  private static final boolean OFF = false;

  /** 50 FE an action on the Advanced (answer 1226), never below an empty battery. */
  @Test
  void eachActionCostsItsEnergy() {
    FarmerSpec spec = new FarmerSpec(COST, PAUSE, OFF, OFF, 0);
    assertEquals(CAPACITY.minus(COST), spec.payForAction(new Battery(CAPACITY, CAPACITY)).charge());
    assertEquals(Energy.ZERO, spec.payForAction(new Battery(CAPACITY, LITTLE)).charge());
  }

  @Test
  void rejectsNumbersTheGameDoesNotHave() {
    assertThrows(IllegalArgumentException.class, () -> new FarmerSpec(COST, NO_PAUSE, OFF, OFF, 0));
    assertThrows(
        IllegalArgumentException.class, () -> new FarmerSpec(COST, PAUSE, ON, ON, TOO_MUCH));
  }

  @Test
  void theUltimateKeepsFortuneII() {
    assertEquals(FORTUNE_II, new FarmerSpec(COST, PAUSE, ON, ON, FORTUNE_II).fortune());
  }
}
