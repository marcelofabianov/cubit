package io.github.marcelofabianov.cubit.domain.dock;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DockQueueTest {
  private static final UUID FIRST = new UUID(0, 1);
  private static final UUID SECOND = new UUID(0, 2);
  private static final long START = 100;
  private static final long LONG_AFTER = 110;

  @Test
  void nobodyIsFirstInAnEmptyQueue() {
    assertEquals(Optional.empty(), DockQueue.EMPTY.first(START));
  }

  @Test
  void whoArrivesFirstEntersFirst() {
    DockQueue queue = DockQueue.EMPTY.report(FIRST, START).report(SECOND, START);
    assertEquals(Optional.of(FIRST), queue.first(START));
  }

  @Test
  void reportingAgainDoesNotLoseThePlace() {
    DockQueue queue =
        DockQueue.EMPTY.report(FIRST, START).report(SECOND, START).report(SECOND, START + 1);
    queue = queue.report(FIRST, START + 1);
    assertEquals(Optional.of(FIRST), queue.first(START + 1));
  }

  @Test
  void aRobotThatStoppedReportingLosesThePlace() {
    DockQueue queue =
        DockQueue.EMPTY.report(FIRST, START).report(SECOND, START).report(SECOND, LONG_AFTER);
    assertEquals(Optional.of(SECOND), queue.first(LONG_AFTER));
  }

  @Test
  void anAbandonedQueueIsEmpty() {
    assertEquals(Optional.empty(), DockQueue.EMPTY.report(FIRST, START).first(LONG_AFTER));
  }
}
