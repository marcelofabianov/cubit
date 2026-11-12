package io.github.marcelofabianov.cubit.domain.behavior;

import java.util.Optional;
import java.util.UUID;

/**
 * The last mob that hurt the robot or its owner, until a moment. A new hit renews the time, and a
 * new attacker takes the place (answer 832). The caller gives the current tick.
 */
public record RetaliationMemory(Optional<UUID> attacker, long untilTick) {
  public static final RetaliationMemory EMPTY = new RetaliationMemory(Optional.empty(), 0);

  private static final String MEMORY_MESSAGE = "The retaliation memory must be positive: ";

  public RetaliationMemory hurtBy(UUID mob, long nowTick, int memoryTicks) {
    if (memoryTicks < 1) {
      throw new IllegalArgumentException(MEMORY_MESSAGE + memoryTicks);
    }
    return new RetaliationMemory(Optional.of(mob), nowTick + memoryTicks);
  }

  public boolean remembers(UUID mob, long nowTick) {
    return nowTick < untilTick && attacker.filter(mob::equals).isPresent();
  }

  /** The attacker died or left the dimension. */
  public RetaliationMemory forget() {
    return EMPTY;
  }
}
