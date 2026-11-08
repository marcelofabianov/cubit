package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class OreFilterTest {
  private static final OreKind DIAMOND = new OreKind("c:ores/diamond");
  private static final OreKind GOLD = new OreKind("c:ores/gold");
  private static final OreKind COAL = new OreKind("c:ores/coal");
  private static final int ONE_SLOT = 1;
  private static final int TWO_SLOTS = 2;

  @Test
  void startsEmpty() {
    assertFalse(OreFilter.EMPTY.contains(DIAMOND));
  }

  @Test
  void clickingAddsAKind() {
    assertTrue(OreFilter.EMPTY.toggled(DIAMOND, ONE_SLOT).contains(DIAMOND));
  }

  @Test
  void clickingTheSameKindRemovesIt() {
    assertEquals(
        OreFilter.EMPTY, OreFilter.EMPTY.toggled(DIAMOND, ONE_SLOT).toggled(DIAMOND, ONE_SLOT));
  }

  @Test
  void aFullFilterDropsTheOldestKind() {
    OreFilter filter = new OreFilter(List.of(DIAMOND, GOLD)).toggled(COAL, TWO_SLOTS);

    assertEquals(List.of(GOLD, COAL), filter.kinds());
  }

  @Test
  void aSmallerConfigKeepsTheNewestKinds() {
    assertEquals(List.of(GOLD), new OreFilter(List.of(DIAMOND, GOLD)).fitting(ONE_SLOT).kinds());
    assertEquals(
        List.of(DIAMOND, GOLD), new OreFilter(List.of(DIAMOND, GOLD)).fitting(TWO_SLOTS).kinds());
  }

  @Test
  void repeatedKindsCountOnce() {
    assertEquals(List.of(DIAMOND), new OreFilter(List.of(DIAMOND, DIAMOND)).kinds());
  }
}
