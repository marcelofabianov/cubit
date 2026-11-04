package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class EnergyTest {
  @Test
  void rejectsNegativeAmount() {
    assertThrows(IllegalArgumentException.class, () -> new Energy(-1));
  }

  @Test
  void addsAmounts() {
    assertEquals(new Energy(15_000), new Energy(5_000).plus(new Energy(10_000)));
  }

  @Test
  void subtractsAmounts() {
    assertEquals(new Energy(5_000), new Energy(15_000).minus(new Energy(10_000)));
  }

  @Test
  void rejectsSubtractionBelowZero() {
    assertThrows(IllegalArgumentException.class, () -> new Energy(1).minus(new Energy(2)));
  }

  @Test
  void returnsTheSmallerAmount() {
    assertEquals(new Energy(3), new Energy(3).min(new Energy(7)));
    assertEquals(new Energy(3), new Energy(7).min(new Energy(3)));
  }
}
