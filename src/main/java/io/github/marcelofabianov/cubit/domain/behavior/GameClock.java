package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * The day and the clock of the world, as the game counts them: the world starts at 06:00 of the
 * first day, and a day has 24000 ticks (answer 1116).
 */
public record GameClock(long day, int hour, int minute) {
  private static final long TICKS_PER_DAY = 24_000L;
  private static final long TICKS_PER_HOUR = 1_000L;
  private static final int HOURS_PER_DAY = 24;
  private static final int MINUTES_PER_HOUR = 60;
  private static final int DAWN_HOUR = 6;
  private static final String NEGATIVE_MESSAGE = "The world time cannot be negative: ";

  public static GameClock of(long dayTime) {
    if (dayTime < 0) {
      throw new IllegalArgumentException(NEGATIVE_MESSAGE + dayTime);
    }
    long inDay = dayTime % TICKS_PER_DAY;
    int hour = (int) ((inDay / TICKS_PER_HOUR + DAWN_HOUR) % HOURS_PER_DAY);
    int minute = (int) (inDay % TICKS_PER_HOUR * MINUTES_PER_HOUR / TICKS_PER_HOUR);
    return new GameClock(dayTime / TICKS_PER_DAY + 1, hour, minute);
  }
}
