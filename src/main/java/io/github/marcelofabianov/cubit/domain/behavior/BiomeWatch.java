package io.github.marcelofabianov.cubit.domain.behavior;

import java.util.Optional;

/**
 * Finds when the robot enters another group of biomes. A biome without a group, such as a river,
 * changes nothing, so crossing one does not repeat the comment on the forest around it.
 */
public record BiomeWatch(boolean known, Optional<ChatTopic> group) {
  public static final BiomeWatch UNKNOWN = new BiomeWatch(false, Optional.empty());

  public Step next(Optional<ChatTopic> now) {
    if (now.isEmpty()) {
      return new Step(Optional.empty(), new BiomeWatch(true, group));
    }
    boolean entered = known && group.isPresent() && !group.equals(now);
    return new Step(entered ? now : Optional.empty(), new BiomeWatch(true, now));
  }

  /** What to comment on, if anything, and what to compare the next reading with. */
  public record Step(Optional<ChatTopic> topic, BiomeWatch watch) {}
}
