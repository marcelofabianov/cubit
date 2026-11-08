package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
  private static final int BLOCKS_IN_SPHERE = 515;
  private static final int CORNER = 4;
  private static final double JUST_BEYOND = 0.01;
  private static final String NOT_IN_AREA = "The offset is outside the area";

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
  void holdsOnlyTheBlocksOfTheSphere() {
    ScanArea area = new ScanArea(RADIUS);
    int held = 0;
    for (int index = 0; index < area.blocks(); index++) {
      if (area.holds(index)) {
        held++;
      }
    }

    assertEquals(BLOCKS_IN_SPHERE, held);
  }

  @Test
  void theSphereReachesTheRadiusInEachDirectionButNotTheCorner() {
    ScanArea area = new ScanArea(RADIUS);
    int ahead = indexOf(area, RADIUS, 0, 0);
    int below = indexOf(area, 0, -RADIUS, 0);
    int corner = indexOf(area, CORNER, CORNER, 0);

    assertTrue(area.holds(ahead));
    assertTrue(area.holds(below));
    assertFalse(area.holds(corner));
  }

  @Test
  void holdsADistanceUpToTheRadius() {
    ScanArea area = new ScanArea(RADIUS);

    assertTrue(area.holdsDistance(RADIUS * RADIUS));
    assertFalse(area.holdsDistance(RADIUS * RADIUS + JUST_BEYOND));
  }

  @Test
  void rejectsAnAreaWithoutARadius() {
    assertThrows(IllegalArgumentException.class, () -> new ScanArea(0));
  }

  private static int indexOf(ScanArea area, int x, int y, int z) {
    for (int index = 0; index < area.blocks(); index++) {
      if (area.xOf(index) == x && area.yOf(index) == y && area.zOf(index) == z) {
        return index;
      }
    }
    throw new IllegalArgumentException(NOT_IN_AREA);
  }
}
