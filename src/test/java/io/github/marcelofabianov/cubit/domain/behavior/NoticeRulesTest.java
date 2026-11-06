package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class NoticeRulesTest {
  @Test
  void anActiveRobotSpeaks() {
    assertTrue(NoticeRules.speaks(PowerState.ACTIVE));
  }

  @Test
  void aRobotSwitchedOffOrOnADockDoesNotSpeak() {
    assertFalse(NoticeRules.speaks(PowerState.HIBERNATING));
    assertFalse(NoticeRules.speaks(PowerState.INACTIVE_EXHAUSTED));
    assertFalse(NoticeRules.speaks(PowerState.INACTIVE_SWITCHED_OFF));
  }
}
