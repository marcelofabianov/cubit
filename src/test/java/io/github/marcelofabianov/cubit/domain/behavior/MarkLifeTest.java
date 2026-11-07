package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MarkLifeTest {
  private static final long NOW = 1_000;
  private static final int HOLD_TICKS = 200;

  @Test
  void lastsForTheHoldTicks() {
    MarkLife life = MarkLife.from(NOW, HOLD_TICKS);

    assertEquals(NOW + HOLD_TICKS, life.untilTick());
    assertTrue(life.lastsAt(NOW));
    assertTrue(life.lastsAt(NOW + HOLD_TICKS - 1));
    assertFalse(life.lastsAt(NOW + HOLD_TICKS));
  }

  @Test
  void aBoxWithoutTimeIsNeverDrawn() {
    assertFalse(MarkLife.from(NOW, 0).lastsAt(NOW));
  }

  @Test
  void rejectsANegativeTime() {
    assertThrows(IllegalArgumentException.class, () -> MarkLife.from(NOW, -1));
  }
}
