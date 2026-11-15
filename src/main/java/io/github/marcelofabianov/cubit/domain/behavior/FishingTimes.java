package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * The time of one catch, as in the game: a wait drawn between its limits, less the Lure of the
 * tier, and then the bite, drawn between its own (answer 1184). The limits come from the config,
 * and the caller draws the numbers, so the domain stays free of randomness.
 */
public record FishingTimes(
    int waitMinTicks, int waitMaxTicks, int biteMinTicks, int biteMaxTicks, int lureTicksPerLevel) {
  private static final String INVALID_MESSAGE = "Invalid fishing times: ";

  public FishingTimes {
    if (waitMinTicks < 1
        || biteMinTicks < 1
        || waitMaxTicks < waitMinTicks
        || biteMaxTicks < biteMinTicks
        || lureTicksPerLevel < 0) {
      throw new IllegalArgumentException(INVALID_MESSAGE + waitMinTicks + waitMaxTicks);
    }
  }

  /**
   * The ticks until the bite, from a drawn wait and a drawn bite. The Lure may take the whole wait
   * away, as in the game, but never the bite.
   */
  public int ticksToBite(int drawnWait, int drawnBite, int lureLevel) {
    int wait = Math.clamp(drawnWait, waitMinTicks, waitMaxTicks);
    int lured = Math.max(0, wait - lureLevel * lureTicksPerLevel);
    return lured + Math.clamp(drawnBite, biteMinTicks, biteMaxTicks);
  }
}
