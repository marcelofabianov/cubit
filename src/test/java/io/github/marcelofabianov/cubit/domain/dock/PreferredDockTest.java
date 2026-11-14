package io.github.marcelofabianov.cubit.domain.dock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class PreferredDockTest {
  private static final String OVERWORLD = "minecraft:overworld";
  private static final String NETHER = "minecraft:the_nether";
  private static final String NO_DIMENSION = "";
  private static final long FIRST_BLOCK = 10L;
  private static final long SECOND_BLOCK = 20L;
  private static final PreferredDock FIRST = new PreferredDock(FIRST_BLOCK, OVERWORLD);
  private static final PreferredDock SECOND = new PreferredDock(SECOND_BLOCK, OVERWORLD);

  @Test
  void aClickMarksTheDock() {
    assertEquals(Optional.of(FIRST), PreferredDock.clicked(Optional.empty(), FIRST));
    assertEquals(Optional.of(SECOND), PreferredDock.clicked(Optional.of(FIRST), SECOND));
  }

  @Test
  void aClickOnTheMarkedDockRemovesTheMark() {
    assertTrue(PreferredDock.clicked(Optional.of(FIRST), FIRST).isEmpty());
  }

  @Test
  void theDimensionCounts() {
    assertTrue(FIRST.isIn(OVERWORLD));
    assertFalse(FIRST.isIn(NETHER));
    PreferredDock elsewhere = new PreferredDock(FIRST_BLOCK, NETHER);
    assertEquals(Optional.of(elsewhere), PreferredDock.clicked(Optional.of(FIRST), elsewhere));
  }

  @Test
  void aDockNeedsADimension() {
    assertThrows(
        IllegalArgumentException.class, () -> new PreferredDock(FIRST_BLOCK, NO_DIMENSION));
  }
}
