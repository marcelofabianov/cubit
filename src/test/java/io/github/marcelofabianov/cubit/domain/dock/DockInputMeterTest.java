package io.github.marcelofabianov.cubit.domain.dock;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import org.junit.jupiter.api.Test;

class DockInputMeterTest {
  private static final Energy STEADY = new Energy(2_400);
  private static final Energy BURST = new Energy(48_000);
  private static final long AVERAGE_OF_ONE_BURST = 2_400;

  private static void recordSecond(DockInputMeter meter, Energy each) {
    for (int tick = 0; tick < DockInputMeter.TICKS_PER_SECOND; tick++) {
      meter.record(each);
    }
  }

  @Test
  void aNewMeterShowsNothing() {
    assertEquals(Energy.ZERO, new DockInputMeter().perTick());
  }

  @Test
  void aSteadyInputShowsItsRate() {
    DockInputMeter meter = new DockInputMeter();
    recordSecond(meter, STEADY);
    assertEquals(STEADY, meter.perTick());
  }

  @Test
  void aBurstIsSpreadOverTheSecond() {
    DockInputMeter meter = new DockInputMeter();
    meter.record(BURST);
    assertEquals(new Energy(AVERAGE_OF_ONE_BURST), meter.perTick());
  }

  @Test
  void anInputOlderThanASecondIsForgotten() {
    DockInputMeter meter = new DockInputMeter();
    meter.record(BURST);
    recordSecond(meter, Energy.ZERO);
    assertEquals(Energy.ZERO, meter.perTick());
  }
}
