package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class ChatWatchTest {
  private static final long BEFORE_DAWN = 22_000;
  private static final long DAWN = 23_000;
  private static final long BEFORE_MID_MORNING = 2_500;
  private static final long MID_MORNING = 2_600;
  private static final long NOON = 6_000;
  private static final long DUSK = 12_000;
  private static final long NEXT_DAY_DAWN = 47_000;

  private static WeatherReading clear(long time) {
    return new WeatherReading(time, true, false, false, false);
  }

  private static WeatherReading rain(long time) {
    return new WeatherReading(time, true, true, false, false);
  }

  private static WeatherReading storm(long time) {
    return new WeatherReading(time, true, true, true, false);
  }

  private static WeatherReading cold(long time) {
    return new WeatherReading(time, false, false, false, true);
  }

  private static WeatherReading covered(long time) {
    return new WeatherReading(time, false, false, false, false);
  }

  private static Optional<ChatTopic> after(WeatherReading before, WeatherReading now) {
    return ChatWatch.first(before).next(now).topic();
  }

  @Test
  void nothingChangedSaysNothing() {
    assertEquals(Optional.empty(), after(clear(NOON), clear(NOON)));
  }

  @Test
  void theHourChangesTheTopic() {
    assertEquals(Optional.of(ChatTopic.DAWN), after(clear(BEFORE_DAWN), clear(DAWN)));
    assertEquals(Optional.of(ChatTopic.DUSK), after(clear(NOON), clear(DUSK)));
    assertEquals(
        Optional.of(ChatTopic.NICE_DAY), after(clear(BEFORE_MID_MORNING), clear(MID_MORNING)));
  }

  @Test
  void theHourRepeatsEveryDay() {
    assertEquals(Optional.of(ChatTopic.DAWN), after(clear(DUSK), clear(NEXT_DAY_DAWN)));
  }

  @Test
  void aRainyMorningIsNotANiceDay() {
    assertEquals(Optional.empty(), after(rain(BEFORE_MID_MORNING), rain(MID_MORNING)));
  }

  @Test
  void rainStartingAndStoppingAreTopics() {
    assertEquals(Optional.of(ChatTopic.RAIN), after(clear(NOON), rain(NOON)));
    assertEquals(Optional.of(ChatTopic.NICE_DAY), after(rain(NOON), clear(NOON)));
  }

  @Test
  void rainStoppingAtNightSaysNothing() {
    assertEquals(Optional.empty(), after(rain(DUSK), clear(DUSK)));
  }

  @Test
  void aStormWinsOverRain() {
    assertEquals(Optional.of(ChatTopic.STORM), after(clear(NOON), storm(NOON)));
    assertEquals(Optional.of(ChatTopic.STORM), after(rain(NOON), storm(NOON)));
  }

  @Test
  void aStormThatEndsIntoRainSaysNothing() {
    assertEquals(Optional.empty(), after(storm(NOON), rain(NOON)));
  }

  @Test
  void enteringTheColdCountsWithoutSky() {
    assertEquals(Optional.of(ChatTopic.COLD), after(covered(NOON), cold(NOON)));
    assertEquals(Optional.empty(), after(cold(NOON), cold(NOON)));
  }

  @Test
  void withoutSkyTheHourSaysNothing() {
    assertEquals(Optional.empty(), after(covered(BEFORE_DAWN), covered(DAWN)));
    assertEquals(Optional.empty(), after(covered(NOON), covered(DUSK)));
  }

  @Test
  void theNextReadingStartsFromTheNewOne() {
    ChatWatch.Step step = ChatWatch.first(clear(NOON)).next(rain(NOON));

    assertEquals(Optional.empty(), step.watch().next(rain(NOON)).topic());
  }
}
