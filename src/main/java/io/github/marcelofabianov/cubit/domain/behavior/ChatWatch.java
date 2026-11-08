package io.github.marcelofabianov.cubit.domain.behavior;

import java.util.Optional;

/**
 * Finds what changed between two readings of the world. The robot speaks when something changes, so
 * each comment has a reason, and the draw only chooses the phrase.
 */
public record ChatWatch(Period period, boolean raining, boolean thundering, boolean cold) {
  private static final long TICKS_PER_DAY = 24_000;
  private static final long MID_MORNING_AT = 2_600;
  private static final long DUSK_AT = 12_000;
  private static final long DAWN_AT = 23_000;

  /** The parts of a day, each one with a comment when it begins. */
  public enum Period {
    DAWN,
    DAY,
    DUSK
  }

  public static ChatWatch first(WeatherReading reading) {
    return new ChatWatch(
        periodOf(reading.dayTime()), reading.raining(), reading.thundering(), reading.cold());
  }

  public Step next(WeatherReading now) {
    return new Step(topicAfter(now), first(now));
  }

  private Optional<ChatTopic> topicAfter(WeatherReading now) {
    if (now.cold() && !cold) {
      return Optional.of(ChatTopic.COLD);
    }
    return now.skyVisible() ? underTheSky(now) : Optional.empty();
  }

  private Optional<ChatTopic> underTheSky(WeatherReading now) {
    if (now.thundering() && !thundering) {
      return Optional.of(ChatTopic.STORM);
    }
    if (now.raining() && !raining) {
      return Optional.of(ChatTopic.RAIN);
    }
    Period current = periodOf(now.dayTime());
    if (current != period) {
      return beginning(current, now);
    }
    boolean rainStopped = raining && !now.raining();
    return rainStopped && current != Period.DUSK
        ? Optional.of(ChatTopic.NICE_DAY)
        : Optional.empty();
  }

  private static Optional<ChatTopic> beginning(Period period, WeatherReading now) {
    return switch (period) {
      case DAWN -> Optional.of(ChatTopic.DAWN);
      case DUSK -> Optional.of(ChatTopic.DUSK);
      case DAY -> now.raining() ? Optional.empty() : Optional.of(ChatTopic.NICE_DAY);
    };
  }

  private static Period periodOf(long dayTime) {
    long time = Math.floorMod(dayTime, TICKS_PER_DAY);
    if (time >= DAWN_AT || time < MID_MORNING_AT) {
      return Period.DAWN;
    }
    return time < DUSK_AT ? Period.DAY : Period.DUSK;
  }

  /** What to comment on, if anything, and what to compare the next reading with. */
  public record Step(Optional<ChatTopic> topic, ChatWatch watch) {}
}
