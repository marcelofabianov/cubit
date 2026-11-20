package io.github.zenndg.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class DeathLogTest {
  private static final String OVERWORLD = "minecraft:overworld";
  private static final String CAUSE = "{\"text\":\"Steve fell\"}";
  private static final int LIMIT = 3;
  private static final int SMALLER_LIMIT = 2;
  private static final int Y = 64;
  private static final DeathRecord FIRST = death(100L);
  private static final DeathRecord SECOND = death(200L);
  private static final DeathRecord THIRD = death(300L);
  private static final DeathRecord FOURTH = death(400L);

  private static DeathRecord death(long dayTime) {
    return new DeathRecord(dayTime, (int) dayTime, Y, -(int) dayTime, OVERWORLD, CAUSE);
  }

  @Test
  void theNewestDeathComesFirst() {
    DeathLog log = DeathLog.EMPTY.record(FIRST, LIMIT).record(SECOND, LIMIT);

    assertEquals(List.of(SECOND, FIRST), log.deaths());
    assertEquals(Optional.of(SECOND), log.latest());
  }

  /** Past the limit, the oldest one leaves (answer 1115). */
  @Test
  void theOldestLeavesPastTheLimit() {
    DeathLog log = DeathLog.EMPTY.record(FIRST, LIMIT).record(SECOND, LIMIT).record(THIRD, LIMIT);

    assertEquals(List.of(FOURTH, THIRD, SECOND), log.record(FOURTH, LIMIT).deaths());
  }

  @Test
  void aSmallerLimitDropsTheOldest() {
    DeathLog log = DeathLog.EMPTY.record(FIRST, LIMIT).record(SECOND, LIMIT).record(THIRD, LIMIT);

    assertEquals(List.of(THIRD, SECOND), log.fittedTo(SMALLER_LIMIT).deaths());
  }

  @Test
  void removingTakesOnlyTheMatchingRow() {
    DeathLog log = DeathLog.EMPTY.record(FIRST, LIMIT).record(SECOND, LIMIT);

    assertEquals(List.of(SECOND), log.remove(1, FIRST.dayTime()).deaths());
  }

  /** A stale screen must not remove another death that moved into the same row. */
  @Test
  void aStaleRemovalChangesNothing() {
    DeathLog log = DeathLog.EMPTY.record(FIRST, LIMIT).record(SECOND, LIMIT);

    assertSame(log, log.remove(0, FIRST.dayTime()));
    assertSame(log, log.remove(LIMIT, FIRST.dayTime()));
    assertSame(log, log.remove(-1, FIRST.dayTime()));
  }

  @Test
  void findsTheRowOfADeath() {
    DeathLog log = DeathLog.EMPTY.record(FIRST, LIMIT).record(SECOND, LIMIT);

    assertEquals(Optional.of(FIRST), log.at(1, FIRST.dayTime()));
    assertTrue(log.at(0, FIRST.dayTime()).isEmpty());
  }

  @Test
  void theLimitIsPositive() {
    assertThrows(IllegalArgumentException.class, () -> DeathLog.EMPTY.record(FIRST, 0));
  }

  @Test
  void anEmptyLogHasNoLatest() {
    assertTrue(DeathLog.EMPTY.latest().isEmpty());
  }
}
