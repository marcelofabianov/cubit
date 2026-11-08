package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ScanSignalTest {
  private static final int BEYOND = 99;

  @Test
  void aMobWinsOverAnOre() {
    assertEquals(ScanSignal.MOB, ScanSignal.of(true, true));
    assertEquals(ScanSignal.MOB, ScanSignal.of(true, false));
  }

  @Test
  void anOreShowsWithoutAMob() {
    assertEquals(ScanSignal.ORE, ScanSignal.of(false, true));
  }

  @Test
  void nothingFoundShowsNothing() {
    assertEquals(ScanSignal.NONE, ScanSignal.of(false, false));
  }

  @Test
  void anUnknownNumberShowsNothing() {
    assertEquals(ScanSignal.ORE, ScanSignal.fromOrdinal(ScanSignal.ORE.ordinal()));
    assertEquals(ScanSignal.NONE, ScanSignal.fromOrdinal(-1));
    assertEquals(ScanSignal.NONE, ScanSignal.fromOrdinal(BEYOND));
  }
}
