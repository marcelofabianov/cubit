package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class FetchMemoryTest {
  private static final UUID ITEM = new UUID(0, 1);
  private static final UUID OTHER = new UUID(0, 2);
  private static final long NOW = 500;
  private static final int IGNORE = 200;

  @Test
  void anIgnoredItemIsIgnoredUntilTheTimeEnds() {
    FetchMemory memory = FetchMemory.EMPTY.ignore(ITEM, NOW, IGNORE);

    assertTrue(memory.ignores(ITEM, NOW));
    assertTrue(memory.ignores(ITEM, NOW + IGNORE - 1));
    assertFalse(memory.ignores(ITEM, NOW + IGNORE));
  }

  @Test
  void otherItemsAreNotIgnored() {
    assertFalse(FetchMemory.EMPTY.ignore(ITEM, NOW, IGNORE).ignores(OTHER, NOW));
  }

  @Test
  void ignoringAnotherItemForgetsTheOnesWhoseTimeEnded() {
    FetchMemory memory =
        FetchMemory.EMPTY.ignore(ITEM, NOW, IGNORE).ignore(OTHER, NOW + IGNORE, IGNORE);

    assertFalse(memory.ignoredUntil().containsKey(ITEM));
    assertTrue(memory.ignores(OTHER, NOW + IGNORE));
  }

  @Test
  void theIgnoreTimeMustBePositive() {
    assertThrows(IllegalArgumentException.class, () -> FetchMemory.EMPTY.ignore(ITEM, NOW, 0));
  }
}
