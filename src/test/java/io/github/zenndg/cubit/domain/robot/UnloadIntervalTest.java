package io.github.zenndg.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UnloadIntervalTest {
  private static final int CONFIG = 5;
  private static final int MAX = 30;
  private static final int UP = 1;
  private static final int DOWN = -1;
  private static final long FIVE_MINUTES = 6_000;
  private static final long ALMOST = FIVE_MINUTES - 1;
  private static final UnloadInterval ONE = new UnloadInterval(1);
  private static final UnloadInterval TOP = new UnloadInterval(MAX);
  private static final boolean ITEMS = true;
  private static final boolean EMPTY = false;
  private static final int TOO_LOW = -2;

  /** Nobody chose, so the default of the config holds (answer 1254). */
  @Test
  void anUnchosenIntervalFollowsTheConfig() {
    assertFalse(UnloadInterval.CONFIG_DEFAULT.chosen());
    assertEquals(CONFIG, UnloadInterval.CONFIG_DEFAULT.minutesOr(CONFIG));
    assertEquals(1, ONE.minutesOr(CONFIG));
  }

  /** The buttons go 1 minute a step, from only when full to the max (answer 1252). */
  @Test
  void theButtonsStepBetweenOnlyWhenFullAndTheMax() {
    assertEquals(
        new UnloadInterval(CONFIG + 1), UnloadInterval.CONFIG_DEFAULT.step(UP, CONFIG, MAX));
    assertEquals(UnloadInterval.ONLY_WHEN_FULL, ONE.step(DOWN, CONFIG, MAX));
    assertEquals(
        UnloadInterval.ONLY_WHEN_FULL, UnloadInterval.ONLY_WHEN_FULL.step(DOWN, CONFIG, MAX));
    assertEquals(TOP, TOP.step(UP, CONFIG, MAX));
  }

  /** It goes once the interval passed, with something to unload (answer 1250). */
  @Test
  void itIsDueOnceTheIntervalPassedWithSomethingToUnload() {
    assertTrue(UnloadInterval.CONFIG_DEFAULT.due(FIVE_MINUTES, ITEMS, CONFIG));
    assertFalse(UnloadInterval.CONFIG_DEFAULT.due(ALMOST, ITEMS, CONFIG));
    assertFalse(UnloadInterval.CONFIG_DEFAULT.due(FIVE_MINUTES, EMPTY, CONFIG));
  }

  @Test
  void onlyWhenFullIsNeverDueByTime() {
    assertFalse(UnloadInterval.ONLY_WHEN_FULL.due(Long.MAX_VALUE, ITEMS, CONFIG));
  }

  @Test
  void rejectsMinutesBelowTheDefault() {
    assertThrows(IllegalArgumentException.class, () -> new UnloadInterval(TOO_LOW));
  }
}
