package io.github.marcelofabianov.cubit.domain.tier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import org.junit.jupiter.api.Test;

class FisherSpecTest {
  private static final Energy COST = new Energy(300);
  private static final int LEVEL_III = 3;
  private static final int TOO_HIGH = 4;
  private static final int NEGATIVE = -1;
  private static final int NONE = 0;
  private static final Energy CAPACITY = new Energy(1_000);
  private static final Energy LITTLE = new Energy(100);

  /** The Ultimate has Lure III and Luck of the Sea III (answer 1185). */
  @Test
  void keepsTheLevelsOfTheGame() {
    FisherSpec ultimate = new FisherSpec(LEVEL_III, LEVEL_III, COST);
    assertEquals(LEVEL_III, ultimate.lureLevel());
    assertEquals(COST, ultimate.catchCost());
  }

  @Test
  void rejectsLevelsTheGameDoesNotHave() {
    assertThrows(IllegalArgumentException.class, () -> new FisherSpec(TOO_HIGH, NONE, COST));
    assertThrows(IllegalArgumentException.class, () -> new FisherSpec(NONE, NEGATIVE, COST));
  }

  @Test
  void noneFishesWithNothing() {
    assertEquals(NONE, FisherSpec.NONE.lureLevel());
    assertEquals(Energy.ZERO, FisherSpec.NONE.catchCost());
  }

  /** 300 FE a fish (answer 1190), and a battery with less ends empty, never below. */
  @Test
  void eachCatchCostsItsEnergy() {
    FisherSpec spec = new FisherSpec(NONE, NONE, COST);
    Battery full = new Battery(CAPACITY, CAPACITY);
    assertEquals(CAPACITY.minus(COST), spec.payForCatch(full).charge());
    assertEquals(Energy.ZERO, spec.payForCatch(new Battery(CAPACITY, LITTLE)).charge());
  }
}
