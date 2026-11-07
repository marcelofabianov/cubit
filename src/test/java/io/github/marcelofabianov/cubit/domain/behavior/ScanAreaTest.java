package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ScanAreaTest {
  private static final int RADIUS = 5;
  private static final int BLOCKS_IN_RADIUS = 1331;
  private static final int LIMIT_RADIUS = 64;
  private static final int BLOCKS_IN_LIMIT_RADIUS = 2_146_689;
  private static final int SMALL_RADIUS = 1;
  private static final int BLOCKS_IN_SMALL_RADIUS = 27;

  @Test
  void countsTheBlocksOfTheCube() {
    assertEquals(BLOCKS_IN_RADIUS, new ScanArea(RADIUS).blocks());
    assertEquals(BLOCKS_IN_LIMIT_RADIUS, new ScanArea(LIMIT_RADIUS).blocks());
  }

  @Test
  void visitsEachBlockOfTheCubeOnce() {
    ScanArea area = new ScanArea(SMALL_RADIUS);
    Set<List<Integer>> visited = new HashSet<>();
    for (int index = 0; index < area.blocks(); index++) {
      visited.add(List.of(area.xOf(index), area.yOf(index), area.zOf(index)));
    }

    assertEquals(BLOCKS_IN_SMALL_RADIUS, visited.size());
    for (List<Integer> offset : visited) {
      for (int value : offset) {
        assertEquals(true, Math.abs(value) <= SMALL_RADIUS);
      }
    }
  }

  @Test
  void rejectsAnAreaWithoutARadius() {
    assertThrows(IllegalArgumentException.class, () -> new ScanArea(0));
  }
}
