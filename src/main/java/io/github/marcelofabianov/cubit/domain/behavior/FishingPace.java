package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * How many ticks of the wait one tick counts, as in FishingHook.catchingFish: the rain may count
 * one more, and a place without sky may count none (answers 1184 and 1206). The caller draws the
 * two chances, so the domain stays free of randomness.
 */
public final class FishingPace {
  /** The chances of the game, kept as in FishingHook rather than in the config (answer 1206). */
  public static final float RAIN_CHANCE = 0.25F;

  public static final float NO_SKY_CHANCE = 0.5F;

  private static final int ONE_TICK = 1;

  private FishingPace() {}

  public static int ticks(boolean raining, boolean seesSky, float rainDraw, float skyDraw) {
    int ticks = ONE_TICK;
    if (rainDraw < RAIN_CHANCE && raining) {
      ticks++;
    }
    if (skyDraw < NO_SKY_CHANCE && !seesSky) {
      ticks--;
    }
    return ticks;
  }
}
