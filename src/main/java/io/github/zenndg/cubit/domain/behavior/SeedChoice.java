package io.github.zenndg.cubit.domain.behavior;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Which seed the Farmer plants on empty farmland: the one of a neighbor plant, so the beds keep
 * together, and else the one it has the most of. Seeds are keys, such as an item id, so the domain
 * stays free of the game (answers 1233 and 1236).
 */
public final class SeedChoice {
  private SeedChoice() {}

  public static Optional<String> pick(Map<String, Integer> seeds, Collection<String> neighbors) {
    for (String neighbor : neighbors) {
      if (seeds.getOrDefault(neighbor, 0) > 0) {
        return Optional.of(neighbor);
      }
    }
    return seeds.entrySet().stream()
        .filter(entry -> entry.getValue() > 0)
        .max(
            Map.Entry.<String, Integer>comparingByValue().thenComparing(Map.Entry.comparingByKey()))
        .map(Map.Entry::getKey);
  }
}
