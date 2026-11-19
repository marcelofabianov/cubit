package io.github.zenndg.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FishingPaceTest {
  private static final boolean RAIN = true;
  private static final boolean DRY = false;
  private static final boolean SKY = true;
  private static final boolean ROOF = false;
  private static final float LUCKY = 0.1F;
  private static final float UNLUCKY = 0.9F;
  private static final int NONE = 0;
  private static final int ONE = 1;
  private static final int TWO = 2;

  @Test
  void underTheSkyWithoutRainEachTickCountsOne() {
    assertEquals(ONE, FishingPace.ticks(DRY, SKY, LUCKY, LUCKY));
  }

  /** The rain may count one more, as in the game (answer 1184). */
  @Test
  void theRainMayCountOneMore() {
    assertEquals(TWO, FishingPace.ticks(RAIN, SKY, LUCKY, LUCKY));
    assertEquals(ONE, FishingPace.ticks(RAIN, SKY, UNLUCKY, LUCKY));
  }

  @Test
  void withoutSkyATickMayCountNone() {
    assertEquals(NONE, FishingPace.ticks(DRY, ROOF, LUCKY, LUCKY));
    assertEquals(ONE, FishingPace.ticks(DRY, ROOF, LUCKY, UNLUCKY));
  }
}
