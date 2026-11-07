package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class OreSignalsTest {
  private static final OreKind DIAMOND = new OreKind("c:ores/diamond");
  private static final OreKind IRON = new OreKind("c:ores/iron");
  private static final OreKind COAL = new OreKind("c:ores/coal");
  private static final OreKind COPPER = new OreKind("c:ores/copper");
  private static final OreSignals SIGNALS = new OreSignals(List.of(DIAMOND, IRON));
  private static final OreFilter COAL_FILTER = new OreFilter(List.of(COAL));

  @Test
  void onlyTheListedKindsAreRare() {
    assertTrue(SIGNALS.isRare(DIAMOND));
    assertFalse(SIGNALS.isRare(COAL));
  }

  @Test
  void aRareOreSignalsWithoutBeingInTheFilter() {
    assertEquals(Optional.of(IRON), SIGNALS.lead(List.of(IRON), OreFilter.EMPTY));
  }

  @Test
  void aCommonOreOnlySignalsFromTheFilter() {
    assertEquals(Optional.of(COAL), SIGNALS.lead(List.of(COAL), COAL_FILTER));
    assertEquals(Optional.empty(), SIGNALS.lead(List.of(COAL), OreFilter.EMPTY));
    assertEquals(Optional.empty(), SIGNALS.lead(List.of(COPPER), COAL_FILTER));
  }

  @Test
  void theRarestLeads() {
    assertEquals(Optional.of(DIAMOND), SIGNALS.lead(List.of(IRON, DIAMOND), OreFilter.EMPTY));
  }

  @Test
  void aRareOreLeadsOverOneOnlyInTheFilter() {
    assertEquals(Optional.of(IRON), SIGNALS.lead(List.of(COAL, IRON), COAL_FILTER));
  }
}
