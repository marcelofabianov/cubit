package io.github.marcelofabianov.cubit.domain.tier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class UnloadSpecTest {
  private static final int REACH = 16;

  @Test
  void aTierWithoutNumbersMarksNoChest() {
    assertEquals(0, UnloadSpec.NONE.chests());
  }

  @Test
  void theNumbersCannotBeNegative() {
    assertThrows(IllegalArgumentException.class, () -> new UnloadSpec(-1, REACH));
  }
}
