package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * Until when a box around something a scanner found stays drawn, counted in game ticks (answers 599
 * and 613). The world can end it sooner, when the ore is mined or the mob leaves the range.
 */
public record MarkLife(long untilTick) {
  private static final String NEGATIVE_MESSAGE = "A box cannot last a negative time: ";

  public static MarkLife from(long nowTick, int holdTicks) {
    if (holdTicks < 0) {
      throw new IllegalArgumentException(NEGATIVE_MESSAGE + holdTicks);
    }
    return new MarkLife(nowTick + holdTicks);
  }

  public boolean lastsAt(long nowTick) {
    return nowTick < untilTick;
  }
}
