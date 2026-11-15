package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class HibernationReasonTest {
  private static final String UNKNOWN_KEY = "no_water";

  /** The keys are a save contract, and the base has one reason (answers 1165 and 1172). */
  @Test
  void theKeysRoundTrip() {
    for (HibernationReason reason : HibernationReason.values()) {
      assertEquals(Optional.of(reason), HibernationReason.fromKey(reason.key()));
    }
  }

  @Test
  void anUnknownKeyIsNoReason() {
    assertEquals(Optional.empty(), HibernationReason.fromKey(UNKNOWN_KEY));
  }
}
