package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class FishingTimesTest {
  private static final int WAIT_MIN = 100;
  private static final int WAIT_MAX = 600;
  private static final int BITE_MIN = 20;
  private static final int BITE_MAX = 80;
  private static final int LURE_TICKS = 100;
  private static final FishingTimes GAME =
      new FishingTimes(WAIT_MIN, WAIT_MAX, BITE_MIN, BITE_MAX, LURE_TICKS);
  private static final int NO_LURE = 0;
  private static final int LURE_II = 2;
  private static final int LURE_III = 3;
  private static final int DRAWN_WAIT = 350;
  private static final int DRAWN_BITE = 50;
  private static final int TOO_LONG = 9000;
  private static final int TOO_SHORT = 1;
  private static final int NONE = 0;

  /** The wait and the bite of the game, as drawn (answer 1184). */
  @Test
  void withoutLureItIsTheWaitAndTheBite() {
    assertEquals(DRAWN_WAIT + DRAWN_BITE, GAME.ticksToBite(DRAWN_WAIT, DRAWN_BITE, NO_LURE));
  }

  @Test
  void eachLevelOfLureTakesItsTicksFromTheWait() {
    int expected = DRAWN_WAIT - LURE_II * LURE_TICKS + DRAWN_BITE;
    assertEquals(expected, GAME.ticksToBite(DRAWN_WAIT, DRAWN_BITE, LURE_II));
  }

  /** As in the game, the Lure may take the whole wait away, but never the bite. */
  @Test
  void theLureNeverTakesTheBite() {
    assertEquals(BITE_MIN, GAME.ticksToBite(WAIT_MIN, BITE_MIN, LURE_III));
  }

  @Test
  void aNumberDrawnOutsideTheLimitsIsKeptInside() {
    assertEquals(WAIT_MAX + BITE_MAX, GAME.ticksToBite(TOO_LONG, TOO_LONG, NO_LURE));
    assertEquals(WAIT_MIN + BITE_MIN, GAME.ticksToBite(TOO_SHORT, TOO_SHORT, NO_LURE));
  }

  @Test
  void limitsOutOfOrderAreRejected() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new FishingTimes(WAIT_MAX, WAIT_MIN, BITE_MIN, BITE_MAX, LURE_TICKS));
    assertThrows(
        IllegalArgumentException.class,
        () -> new FishingTimes(WAIT_MIN, WAIT_MAX, BITE_MAX, BITE_MIN, LURE_TICKS));
    assertThrows(
        IllegalArgumentException.class,
        () -> new FishingTimes(NONE, WAIT_MAX, BITE_MIN, BITE_MAX, LURE_TICKS));
  }
}
