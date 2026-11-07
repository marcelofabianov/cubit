package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MarkPulseTest {
  private static final double PRECISION = 1e-9;
  private static final double HALF_PERIOD = MarkPulse.PERIOD_TICKS / 2;
  private static final int SAMPLES = 120;

  @Test
  void startsStrongAndFadesAtHalfThePeriod() {
    assertEquals(MarkPulse.STRONGEST, MarkPulse.at(0), PRECISION);
    assertEquals(MarkPulse.WEAKEST, MarkPulse.at(HALF_PERIOD), PRECISION);
    assertEquals(MarkPulse.STRONGEST, MarkPulse.at(MarkPulse.PERIOD_TICKS), PRECISION);
  }

  @Test
  void neverGoesBeyondItsLimits() {
    for (int tick = 0; tick < SAMPLES; tick++) {
      double strength = MarkPulse.at(tick);
      assertTrue(strength >= MarkPulse.WEAKEST - PRECISION);
      assertTrue(strength <= MarkPulse.STRONGEST + PRECISION);
    }
  }
}
