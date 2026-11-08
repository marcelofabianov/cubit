package io.github.marcelofabianov.cubit.domain.tier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import org.junit.jupiter.api.Test;

class ScannerSpecTest {
  @Test
  void acceptsAScanThatCostsNothing() {
    assertDoesNotThrow(() -> new ScannerSpec(1, 1, 1, 1, Energy.ZERO, Energy.ZERO, 1));
  }

  @Test
  void rejectsNumbersThatWouldBreakAScan() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new ScannerSpec(0, 1, 1, 1, Energy.ZERO, Energy.ZERO, 1));
    assertThrows(
        IllegalArgumentException.class,
        () -> new ScannerSpec(1, 0, 1, 1, Energy.ZERO, Energy.ZERO, 1));
    assertThrows(
        IllegalArgumentException.class,
        () -> new ScannerSpec(1, 1, 0, 1, Energy.ZERO, Energy.ZERO, 1));
    assertThrows(
        IllegalArgumentException.class,
        () -> new ScannerSpec(1, 1, 1, 0, Energy.ZERO, Energy.ZERO, 1));
    assertThrows(
        IllegalArgumentException.class,
        () -> new ScannerSpec(1, 1, 1, 1, Energy.ZERO, Energy.ZERO, 0));
  }
}
