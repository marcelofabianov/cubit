package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class NoticeRepeatTest {
  private static final int INTERVAL = 3;

  @Test
  void aNoticeNeverShownCanBeShown() {
    assertTrue(NoticeRepeat.READY.canShow());
  }

  @Test
  void aNoticeJustShownWaitsForTheWholeInterval() {
    NoticeRepeat repeat = NoticeRepeat.READY.shown(INTERVAL);
    assertFalse(repeat.canShow());
    assertFalse(repeat.tick().canShow());
    assertFalse(repeat.tick().tick().canShow());
    assertTrue(repeat.tick().tick().tick().canShow());
  }

  @Test
  void waitingStopsAtReady() {
    assertEquals(NoticeRepeat.READY, NoticeRepeat.READY.tick());
  }
}
