package io.github.zenndg.cubit.domain.tier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.zenndg.cubit.domain.energy.Battery;
import io.github.zenndg.cubit.domain.energy.Energy;
import org.junit.jupiter.api.Test;

class CookSpecTest {
  private static final Energy CAPACITY = new Energy(1_000);
  private static final Energy COST = new Energy(50);
  private static final Energy LITTLE = new Energy(10);
  private static final int OVENS = 4;
  private static final int PAUSE = 12;
  private static final int SCAN = 100;
  private static final int NONE = 0;
  private static final int NEGATIVE = -1;

  /** Each stack moved costs its energy, down to an empty battery and never more (rule 13). */
  @Test
  void eachStackMovedCostsItsEnergy() {
    CookSpec spec = new CookSpec(OVENS, COST, PAUSE, SCAN);
    assertEquals(CAPACITY.minus(COST), spec.payForAction(new Battery(CAPACITY, CAPACITY)).charge());
    assertEquals(Energy.ZERO, spec.payForAction(new Battery(CAPACITY, LITTLE)).charge());
  }

  @Test
  void rejectsNumbersThatCannotWork() {
    assertThrows(IllegalArgumentException.class, () -> new CookSpec(NEGATIVE, COST, PAUSE, SCAN));
    assertThrows(IllegalArgumentException.class, () -> new CookSpec(OVENS, COST, NONE, SCAN));
    assertThrows(IllegalArgumentException.class, () -> new CookSpec(OVENS, COST, PAUSE, NONE));
  }
}
