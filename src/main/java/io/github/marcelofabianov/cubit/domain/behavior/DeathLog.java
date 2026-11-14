package io.github.marcelofabianov.cubit.domain.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The deaths of a player, the newest first, up to a limit from the config. Past the limit the
 * oldest one leaves (answers 1111 and 1115). A row is found by its place and its time, so a stale
 * screen never removes another death.
 */
public record DeathLog(List<DeathRecord> deaths) {
  public static final DeathLog EMPTY = new DeathLog(List.of());

  private static final String LIMIT_MESSAGE = "The death log limit must be positive: ";

  public DeathLog {
    deaths = List.copyOf(deaths);
  }

  public DeathLog record(DeathRecord death, int limit) {
    requirePositive(limit);
    List<DeathRecord> next = new ArrayList<>(deaths.size() + 1);
    next.add(death);
    next.addAll(deaths);
    return new DeathLog(next).fittedTo(limit);
  }

  public DeathLog fittedTo(int limit) {
    requirePositive(limit);
    if (deaths.size() <= limit) {
      return this;
    }
    return new DeathLog(deaths.subList(0, limit));
  }

  public DeathLog remove(int index, long dayTime) {
    if (at(index, dayTime).isEmpty()) {
      return this;
    }
    List<DeathRecord> next = new ArrayList<>(deaths);
    next.remove(index);
    return new DeathLog(next);
  }

  public Optional<DeathRecord> at(int index, long dayTime) {
    if (index < 0 || index >= deaths.size()) {
      return Optional.empty();
    }
    DeathRecord death = deaths.get(index);
    return death.dayTime() == dayTime ? Optional.of(death) : Optional.empty();
  }

  public Optional<DeathRecord> latest() {
    return deaths.isEmpty() ? Optional.empty() : Optional.of(deaths.getFirst());
  }

  private static void requirePositive(int limit) {
    if (limit <= 0) {
      throw new IllegalArgumentException(LIMIT_MESSAGE + limit);
    }
  }
}
