package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class RetaliationMemoryTest {
  private static final UUID ZOMBIE = new UUID(1, 1);
  private static final UUID SKELETON = new UUID(2, 2);
  private static final long NOW = 1_000;
  private static final int MEMORY_TICKS = 600;
  private static final long STILL_REMEMBERED = NOW + MEMORY_TICKS - 1;
  private static final long FORGOTTEN = NOW + MEMORY_TICKS;
  private static final long LATER = NOW + 300;

  @Test
  void anEmptyMemoryRemembersNobody() {
    assertFalse(RetaliationMemory.EMPTY.remembers(ZOMBIE, NOW));
  }

  @Test
  void theAttackerIsRememberedForTheMemoryTime() {
    RetaliationMemory memory = RetaliationMemory.EMPTY.hurtBy(ZOMBIE, NOW, MEMORY_TICKS);

    assertTrue(memory.remembers(ZOMBIE, STILL_REMEMBERED));
    assertFalse(memory.remembers(ZOMBIE, FORGOTTEN));
  }

  @Test
  void aNewAttackerTakesThePlace() {
    RetaliationMemory memory =
        RetaliationMemory.EMPTY
            .hurtBy(ZOMBIE, NOW, MEMORY_TICKS)
            .hurtBy(SKELETON, LATER, MEMORY_TICKS);

    assertTrue(memory.remembers(SKELETON, LATER));
    assertFalse(memory.remembers(ZOMBIE, LATER));
  }

  @Test
  void aNewHitRenewsTheTime() {
    RetaliationMemory memory =
        RetaliationMemory.EMPTY
            .hurtBy(ZOMBIE, NOW, MEMORY_TICKS)
            .hurtBy(ZOMBIE, LATER, MEMORY_TICKS);

    assertTrue(memory.remembers(ZOMBIE, FORGOTTEN));
  }

  @Test
  void forgettingClearsTheAttacker() {
    RetaliationMemory memory = RetaliationMemory.EMPTY.hurtBy(ZOMBIE, NOW, MEMORY_TICKS);

    assertFalse(memory.forget().remembers(ZOMBIE, NOW));
  }

  @Test
  void theMemoryTimeMustBePositive() {
    assertThrows(
        IllegalArgumentException.class, () -> RetaliationMemory.EMPTY.hurtBy(ZOMBIE, NOW, 0));
  }
}
