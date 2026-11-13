package io.github.marcelofabianov.cubit.domain.behavior;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The items the robot gave up on, each ignored until a tick (answer 942). The caller gives the
 * current tick. Ignoring a new item forgets the ones whose time ended, so the memory stays small.
 */
public record FetchMemory(Map<UUID, Long> ignoredUntil) {
  public static final FetchMemory EMPTY = new FetchMemory(Map.of());

  private static final String IGNORE_MESSAGE = "The fetch ignore time must be positive: ";

  public FetchMemory {
    ignoredUntil = Map.copyOf(ignoredUntil);
  }

  public FetchMemory ignore(UUID item, long nowTick, int ignoreTicks) {
    if (ignoreTicks < 1) {
      throw new IllegalArgumentException(IGNORE_MESSAGE + ignoreTicks);
    }
    Map<UUID, Long> next = new HashMap<>();
    ignoredUntil.forEach((id, until) -> {
      if (nowTick < until) {
        next.put(id, until);
      }
    });
    next.put(item, nowTick + ignoreTicks);
    return new FetchMemory(next);
  }

  public boolean ignores(UUID item, long nowTick) {
    Long until = ignoredUntil.get(item);
    return until != null && nowTick < until;
  }
}
