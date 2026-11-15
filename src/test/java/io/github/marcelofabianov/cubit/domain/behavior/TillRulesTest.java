package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TillRulesTest {
  private static final boolean YES = true;
  private static final boolean NO = false;

  /** Grass by the water, with air above, is tilled (answer 1232). */
  @Test
  void tillsWhatTheHoeTurnsByTheWater() {
    assertTrue(TillRules.tills(YES, YES, YES, NO));
  }

  /** Far from the water the farmland would dry, so it is left alone. */
  @Test
  void leavesTheRestAlone() {
    assertFalse(TillRules.tills(NO, YES, YES, NO));
    assertFalse(TillRules.tills(YES, NO, YES, NO));
    assertFalse(TillRules.tills(YES, YES, NO, NO));
    assertFalse(TillRules.tills(YES, YES, YES, YES));
  }
}
