package io.github.marcelofabianov.cubit.domain.dock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The robots waiting for a busy dock, in order of arrival. A robot reports itself on every tick, so
 * one that left the world, by any way, stops counting without anyone having to remove it.
 */
public record DockQueue(List<Waiting> waiting) {
  public static final DockQueue EMPTY = new DockQueue(List.of());
  private static final long TICKS_TO_FORGET = 2;

  public DockQueue {
    waiting = List.copyOf(waiting);
  }

  public DockQueue report(UUID robot, long now) {
    List<Waiting> kept = new ArrayList<>();
    boolean known = false;
    for (Waiting entry : waiting) {
      if (entry.robot().equals(robot)) {
        kept.add(new Waiting(robot, now));
        known = true;
      } else if (now - entry.lastSeen() < TICKS_TO_FORGET) {
        kept.add(entry);
      }
    }
    if (!known) {
      kept.add(new Waiting(robot, now));
    }
    return new DockQueue(kept);
  }

  public Optional<UUID> first(long now) {
    return waiting.stream()
        .filter(entry -> now - entry.lastSeen() < TICKS_TO_FORGET)
        .map(Waiting::robot)
        .findFirst();
  }

  /** One robot in the queue, and the last tick it reported itself. */
  public record Waiting(UUID robot, long lastSeen) {}
}
