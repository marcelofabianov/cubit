package io.github.zenndg.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class HibernationReasonTest {
  private static final String UNKNOWN_KEY = "no_lava";
  private static final String NO_WATER_KEY = "no_water";

  /** The keys are a save contract (answers 1165, 1172 and 1188). */
  @Test
  void theKeysRoundTrip() {
    for (HibernationReason reason : HibernationReason.values()) {
      assertEquals(Optional.of(reason), HibernationReason.fromKey(reason.key()));
    }
  }

  @Test
  void theFisherReasonKeepsItsKey() {
    assertEquals(Optional.of(HibernationReason.NO_WATER), HibernationReason.fromKey(NO_WATER_KEY));
  }

  @Test
  void anUnknownKeyIsNoReason() {
    assertEquals(Optional.empty(), HibernationReason.fromKey(UNKNOWN_KEY));
  }
}
