package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class PaintRulesTest {
  @Test
  void anotherColorPaintsTheRobot() {
    assertTrue(PaintRules.paints(RobotColor.WHITE, RobotColor.RED));
  }

  @Test
  void theSameColorDoesNothing() {
    assertFalse(PaintRules.paints(RobotColor.RED, RobotColor.RED));
  }

  @Test
  void whitePaintsBackToTheOriginalLook() {
    assertTrue(PaintRules.paints(RobotColor.RED, RobotColor.WHITE));
  }

  @Test
  void onlyAnActiveRobotReacts() {
    assertTrue(PaintRules.reacts(PowerState.ACTIVE));
    assertFalse(PaintRules.reacts(PowerState.HIBERNATING));
    assertFalse(PaintRules.reacts(PowerState.INACTIVE_EXHAUSTED));
    assertFalse(PaintRules.reacts(PowerState.INACTIVE_SWITCHED_OFF));
  }
}
