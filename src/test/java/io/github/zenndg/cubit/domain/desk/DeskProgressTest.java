package io.github.zenndg.cubit.domain.desk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DeskProgressTest {
  private static final int CRAFT_TICKS = 100;
  private static final int SHORTER = 30;
  private static final int HALF = 50;
  private static final int PERCENT = 100;

  private static DeskProgress after(int ticks) {
    DeskProgress progress = DeskProgress.NONE;
    for (int i = 0; i < ticks; i++) {
      progress = progress.advance();
    }
    return progress;
  }

  @Test
  void isNotReadyBeforeTheTimeOfTheTier() {
    assertFalse(after(CRAFT_TICKS - 1).isReady(CRAFT_TICKS));
  }

  @Test
  void isReadyAtTheTimeOfTheTier() {
    assertTrue(after(CRAFT_TICKS).isReady(CRAFT_TICKS));
  }

  @Test
  void aFasterTierFinishesWhatWasAlreadyPast() {
    assertTrue(after(HALF).isReady(SHORTER));
  }

  @Test
  void showsTheShareDone() {
    assertEquals(HALF, after(HALF).percent(CRAFT_TICKS));
    assertEquals(PERCENT, after(CRAFT_TICKS + 1).percent(CRAFT_TICKS));
  }

  @Test
  void rejectsNegativeTicks() {
    assertThrows(IllegalArgumentException.class, () -> new DeskProgress(-1));
  }
}
