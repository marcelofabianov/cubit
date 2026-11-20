package io.github.zenndg.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class OpenWaterTest {
  private static final WaterCell WATER = WaterCell.INSIDE_WATER;
  private static final WaterCell AIR = WaterCell.ABOVE_WATER;
  private static final WaterCell SHORE = WaterCell.INVALID;

  @Test
  void aLayerOfOneKindKeepsIt() {
    assertEquals(WATER, OpenWater.layer(List.of(WATER, WATER, WATER)));
    assertEquals(AIR, OpenWater.layer(List.of(AIR, AIR)));
  }

  /** The shore inside the 5 by 5 breaks the layer, so a point by the edge is never open. */
  @Test
  void aMixedOrEmptyLayerIsBroken() {
    assertEquals(SHORE, OpenWater.layer(List.of(WATER, SHORE, WATER)));
    assertEquals(SHORE, OpenWater.layer(List.of(WATER, AIR)));
    assertEquals(SHORE, OpenWater.layer(List.of()));
  }

  /** Two layers of water under two of air, as in a lake two blocks deep (answer 1183). */
  @Test
  void waterUnderAirIsOpen() {
    assertTrue(OpenWater.isOpen(List.of(WATER, WATER, AIR, AIR)));
    assertTrue(OpenWater.isOpen(List.of(WATER, WATER, WATER, WATER)));
  }

  @Test
  void aBrokenLayerOrWaterOverAirIsNotOpen() {
    assertFalse(OpenWater.isOpen(List.of(WATER, SHORE, AIR, AIR)));
    assertFalse(OpenWater.isOpen(List.of(WATER, AIR, WATER, AIR)));
    assertFalse(OpenWater.isOpen(List.of(AIR, AIR, AIR, AIR)));
    assertFalse(OpenWater.isOpen(List.of()));
  }
}
