package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class EnergyTest {
  private static final Energy FIVE_THOUSAND = new Energy(5_000);
  private static final Energy TEN_THOUSAND = new Energy(10_000);
  private static final Energy FIFTEEN_THOUSAND = new Energy(15_000);
  private static final Energy ONE = new Energy(1);
  private static final Energy TWO = new Energy(2);

  @Test
  void rejectsNegativeAmount() {
    assertThrows(IllegalArgumentException.class, () -> new Energy(-1));
  }

  @Test
  void addsAmounts() {
    assertEquals(FIFTEEN_THOUSAND, FIVE_THOUSAND.plus(TEN_THOUSAND));
  }

  @Test
  void subtractsAmounts() {
    assertEquals(FIVE_THOUSAND, FIFTEEN_THOUSAND.minus(TEN_THOUSAND));
  }

  @Test
  void rejectsSubtractionBelowZero() {
    assertThrows(IllegalArgumentException.class, () -> ONE.minus(TWO));
  }

  @Test
  void returnsTheSmallerAmount() {
    assertEquals(ONE, ONE.min(TWO));
    assertEquals(ONE, TWO.min(ONE));
  }
}
