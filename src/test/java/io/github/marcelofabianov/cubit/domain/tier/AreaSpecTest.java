package io.github.marcelofabianov.cubit.domain.tier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AreaSpecTest {
  private static final int SIDE = 16;
  private static final int HEIGHT = 8;

  @Test
  void aTierWithoutSideOrHeightHasNoAreaMode() {
    assertFalse(AreaSpec.NONE.unlocked());
    assertFalse(new AreaSpec(SIDE, 0).unlocked());
    assertTrue(new AreaSpec(SIDE, HEIGHT).unlocked());
  }

  @Test
  void anAreaCannotBeNegative() {
    assertThrows(IllegalArgumentException.class, () -> new AreaSpec(-1, HEIGHT));
  }
}
