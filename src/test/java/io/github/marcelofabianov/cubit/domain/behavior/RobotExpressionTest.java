package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RobotExpressionTest {
  @Test
  void theScreenSwitchedOffDoesNotGlow() {
    assertFalse(RobotExpression.OFF.glows());
  }

  @Test
  void aScreenThatIsOnGlows() {
    assertTrue(RobotExpression.NORMAL.glows());
    assertTrue(RobotExpression.SLEEPY.glows());
    assertTrue(RobotExpression.CHARGING.glows());
  }
}
