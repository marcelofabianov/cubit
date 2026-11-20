package io.github.zenndg.cubit.domain.behavior;

import java.util.List;
import java.util.OptionalInt;

/**
 * Where the Organizer puts an item: the chest with room that holds the most of the same item, the
 * first on a tie, or, with none, the first chest with room, in the order of the marks (answers 208,
 * 212 and 1291). No room anywhere means it keeps the item (answer 1292).
 */
public final class StorageChoice {
  private static final int NONE = -1;

  private StorageChoice() {}

  /** The index of the chest, in the order of the marks, or none. */
  public static OptionalInt chestFor(List<ChestStock> chests) {
    int best = NONE;
    int firstWithRoom = NONE;
    for (int index = 0; index < chests.size(); index++) {
      ChestStock chest = chests.get(index);
      if (!chest.room()) {
        continue;
      }
      if (firstWithRoom == NONE) {
        firstWithRoom = index;
      }
      if (chest.sameItems() > 0
          && (best == NONE || chest.sameItems() > chests.get(best).sameItems())) {
        best = index;
      }
    }
    int chosen = best != NONE ? best : firstWithRoom;
    return chosen == NONE ? OptionalInt.empty() : OptionalInt.of(chosen);
  }
}
