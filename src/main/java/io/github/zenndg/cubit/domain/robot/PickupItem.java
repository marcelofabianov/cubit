package io.github.zenndg.cubit.domain.robot;

import java.util.Set;

/** What the filter reads of an item on the ground: its id and the ids of its tags. */
public record PickupItem(String id, Set<String> tags) {
  public PickupItem {
    tags = Set.copyOf(tags);
  }
}
