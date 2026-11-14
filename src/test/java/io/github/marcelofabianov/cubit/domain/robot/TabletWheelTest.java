package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TabletWheelTest {
  private static final double UP = 1;
  private static final double DOWN = -1;
  private static final double SMALL_UP = 0.1;
  private static final int NEXT = 1;
  private static final int PREVIOUS = -1;
  private static final int NONE = 0;

  @Test
  void theWheelDownGoesToTheNextApp() {
    assertEquals(NEXT, TabletWheel.direction(0, DOWN));
    assertEquals(PREVIOUS, TabletWheel.direction(0, UP));
  }

  @Test
  void onMacOsShiftTurnsTheWheelSidewaysAndItStillSwitches() {
    assertEquals(PREVIOUS, TabletWheel.direction(DOWN, 0));
    assertEquals(NEXT, TabletWheel.direction(UP, 0));
  }

  @Test
  void theVerticalPartWinsOnATrackpad() {
    assertEquals(PREVIOUS, TabletWheel.direction(DOWN, SMALL_UP));
  }

  @Test
  void aScrollThatDoesNotMoveSwitchesNothing() {
    assertEquals(NONE, TabletWheel.direction(0, 0));
  }
}
