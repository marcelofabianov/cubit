package io.github.marcelofabianov.cubit.domain.behavior;

/** The eyes stay open for 5 s and blink in about 0.25 s. Constants, because this is only visual. */
public final class BlinkCycle {
  private static final int OPEN_TICKS = 100;
  private static final int HALF_TICKS = 2;
  private static final int CLOSED_TICKS = 1;
  private static final int CYCLE_TICKS = OPEN_TICKS + HALF_TICKS + CLOSED_TICKS + HALF_TICKS;

  private BlinkCycle() {}

  public static BlinkFrame frameAt(long tick) {
    int position = (int) Math.floorMod(tick, (long) CYCLE_TICKS);
    if (position < OPEN_TICKS) {
      return BlinkFrame.OPEN;
    }
    int intoBlink = position - OPEN_TICKS;
    return intoBlink >= HALF_TICKS && intoBlink < HALF_TICKS + CLOSED_TICKS
        ? BlinkFrame.CLOSED
        : BlinkFrame.HALF;
  }
}
