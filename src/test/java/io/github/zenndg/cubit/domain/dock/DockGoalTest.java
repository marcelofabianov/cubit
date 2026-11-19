package io.github.zenndg.cubit.domain.dock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.zenndg.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class DockGoalTest {
  @Test
  void anActiveCubitWithADockInReachGoes() {
    assertTrue(DockGoal.of(PowerState.ACTIVE, true).ready());
  }

  @Test
  void withoutADockInReachItSaysSo() {
    assertEquals(DockGoal.NO_DOCK, DockGoal.of(PowerState.ACTIVE, false));
  }

  @Test
  void anInactiveCubitDoesNotGo() {
    assertEquals(DockGoal.HIBERNATING, DockGoal.of(PowerState.HIBERNATING, true));
    assertEquals(DockGoal.SWITCHED_OFF, DockGoal.of(PowerState.INACTIVE_SWITCHED_OFF, true));
    assertEquals(DockGoal.SWITCHED_OFF, DockGoal.of(PowerState.INACTIVE_EXHAUSTED, true));
    assertFalse(DockGoal.of(PowerState.HIBERNATING, true).ready());
  }

  /** Go to Dock also wakes a Cubit hibernating away from the Dock (answer 1170). */
  @Test
  void aCubitHibernatingAwayGoesToo() {
    assertTrue(DockGoal.of(PowerState.HIBERNATING_AWAY, true).ready());
    assertEquals(DockGoal.NO_DOCK, DockGoal.of(PowerState.HIBERNATING_AWAY, false));
  }
}
