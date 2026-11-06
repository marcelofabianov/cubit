package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class LightLevelTest {
  private static final int MAX_OF_THE_GAME = 15;
  private static final int ABOVE_THE_GAME = 16;
  private static final int BELOW_ZERO = -1;
  private static final int LOW = 4;
  private static final int HIGH = 10;

  @Test
  void acceptsTheWholeRangeOfTheGame() {
    assertDoesNotThrow(() -> new LightLevel(0));
    assertDoesNotThrow(() -> new LightLevel(MAX_OF_THE_GAME));
  }

  @Test
  void rejectsWhatTheGameDoesNotHave() {
    assertThrows(IllegalArgumentException.class, () -> new LightLevel(BELOW_ZERO));
    assertThrows(IllegalArgumentException.class, () -> new LightLevel(ABOVE_THE_GAME));
  }

  @Test
  void theBrightestOfTwoWins() {
    assertEquals(new LightLevel(HIGH), new LightLevel(LOW).max(new LightLevel(HIGH)));
    assertEquals(new LightLevel(HIGH), new LightLevel(HIGH).max(new LightLevel(LOW)));
  }
}
