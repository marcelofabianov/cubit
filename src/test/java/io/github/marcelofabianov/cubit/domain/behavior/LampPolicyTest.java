package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class LampPolicyTest {
  private static final LightLevel SEVEN = new LightLevel(7);
  private static final LightLevel EIGHT = new LightLevel(8);
  private static final LightLevel NINE = new LightLevel(9);
  private static final LightLevel TEN = new LightLevel(10);
  private static final LightLevel DARK = new LightLevel(0);
  private static final LampPolicy POLICY = new LampPolicy(EIGHT, TEN);

  private static boolean staysLit(LightLevel ambient, boolean wasLit) {
    return POLICY.staysLit(true, PowerState.ACTIVE, ambient, wasLit);
  }

  @Test
  void turnsOnBelowTheLowerLimit() {
    assertTrue(staysLit(SEVEN, false));
  }

  @Test
  void doesNotTurnOnAtTheLowerLimit() {
    assertFalse(staysLit(EIGHT, false));
    assertFalse(staysLit(NINE, false));
  }

  @Test
  void keepsItsStateBetweenTheLimits() {
    assertTrue(staysLit(EIGHT, true));
    assertTrue(staysLit(NINE, true));
  }

  @Test
  void turnsOffAtTheUpperLimit() {
    assertFalse(staysLit(TEN, true));
  }

  @Test
  void neverLightsWithTheSwitchOff() {
    assertFalse(POLICY.staysLit(false, PowerState.ACTIVE, DARK, true));
  }

  @Test
  void onlyAnActiveRobotLights() {
    for (PowerState state : PowerState.values()) {
      if (state != PowerState.ACTIVE) {
        assertFalse(POLICY.staysLit(true, state, DARK, true));
      }
    }
  }

  @Test
  void onlyAllowsTheLampWithTheSwitchOnAndAnActiveRobot() {
    assertTrue(LampPolicy.allows(true, PowerState.ACTIVE));
    assertFalse(LampPolicy.allows(false, PowerState.ACTIVE));
    assertFalse(LampPolicy.allows(true, PowerState.HIBERNATING));
    assertFalse(LampPolicy.allows(true, PowerState.INACTIVE_EXHAUSTED));
    assertFalse(LampPolicy.allows(true, PowerState.INACTIVE_SWITCHED_OFF));
  }

  @Test
  void equalLimitsAreValid() {
    assertDoesNotThrow(() -> new LampPolicy(EIGHT, EIGHT));
  }

  @Test
  void rejectsAnUpperLimitBelowTheLowerOne() {
    assertThrows(IllegalArgumentException.class, () -> new LampPolicy(TEN, EIGHT));
  }

  @Test
  void aReversedConfigBecomesAValidPolicy() {
    LampPolicy fitted = LampPolicy.fitting(TEN, EIGHT);

    assertTrue(fitted.staysLit(true, PowerState.ACTIVE, NINE, false));
    assertFalse(fitted.staysLit(true, PowerState.ACTIVE, TEN, true));
  }

  @Test
  void aValidConfigIsKeptAsIs() {
    assertFalse(LampPolicy.fitting(EIGHT, TEN).staysLit(true, PowerState.ACTIVE, NINE, false));
    assertTrue(LampPolicy.fitting(EIGHT, TEN).staysLit(true, PowerState.ACTIVE, NINE, true));
  }
}
