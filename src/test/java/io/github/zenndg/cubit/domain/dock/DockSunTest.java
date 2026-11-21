package io.github.zenndg.cubit.domain.dock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DockSunTest {
  /** Only by day, under the open sky, without rain, where the world has a sun (answer 1384). */
  @Test
  void thePanelGeneratesOnlyUnderTheSun() {
    assertTrue(DockSun.generates(new DockSun(true, true, false, true)));
    assertFalse(DockSun.generates(new DockSun(false, true, false, true)));
    assertFalse(DockSun.generates(new DockSun(true, false, false, true)));
    assertFalse(DockSun.generates(new DockSun(true, true, true, true)));
    assertFalse(DockSun.generates(new DockSun(true, true, false, false)));
  }

  /** The Solar tab tells why the panel stops, the most lasting reason first (answer 1396). */
  @Test
  void theStateTellsWhyThePanelStops() {
    assertEquals(DockSunState.GENERATING, new DockSun(true, true, false, true).state());
    assertEquals(DockSunState.NO_SUN, new DockSun(true, true, false, false).state());
    assertEquals(DockSunState.COVERED, new DockSun(false, false, true, true).state());
    assertEquals(DockSunState.NIGHT, new DockSun(false, true, true, true).state());
    assertEquals(DockSunState.RAIN, new DockSun(true, true, true, true).state());
  }
}
