package io.github.zenndg.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.OptionalInt;
import org.junit.jupiter.api.Test;

class StorageChoiceTest {
  private static final int FEW = 3;
  private static final int MANY = 40;
  private static final int SECOND = 1;
  private static final int THIRD = 2;
  private static final ChestStock EMPTY_WITH_ROOM = new ChestStock(0, true);
  private static final ChestStock FEW_WITH_ROOM = new ChestStock(FEW, true);
  private static final ChestStock MANY_WITH_ROOM = new ChestStock(MANY, true);
  private static final ChestStock MANY_BUT_FULL = new ChestStock(MANY, false);
  private static final ChestStock FULL = new ChestStock(0, false);

  /** The chest with the most of the same item wins (answer 208). */
  @Test
  void theChestWithTheMostOfTheItemWins() {
    assertEquals(
        OptionalInt.of(THIRD),
        StorageChoice.chestFor(List.of(EMPTY_WITH_ROOM, FEW_WITH_ROOM, MANY_WITH_ROOM)));
  }

  /** A chest with the item but no room does not count. */
  @Test
  void aFullChestDoesNotCount() {
    assertEquals(
        OptionalInt.of(SECOND), StorageChoice.chestFor(List.of(MANY_BUT_FULL, FEW_WITH_ROOM)));
  }

  /** Without the item anywhere, the first chest with room, in the order of the marks (1291). */
  @Test
  void withoutTheItemTheFirstWithRoom() {
    assertEquals(OptionalInt.of(SECOND), StorageChoice.chestFor(List.of(FULL, EMPTY_WITH_ROOM)));
  }

  /** With no room anywhere, there is no chest, and the Cubit keeps the item (answer 1292). */
  @Test
  void noRoomMeansNoChest() {
    assertTrue(StorageChoice.chestFor(List.of(FULL, MANY_BUT_FULL)).isEmpty());
    assertTrue(StorageChoice.chestFor(List.of()).isEmpty());
  }

  /** A tie keeps the first in the order of the marks. */
  @Test
  void aTieKeepsTheFirst() {
    assertEquals(0, StorageChoice.chestFor(List.of(FEW_WITH_ROOM, FEW_WITH_ROOM)).getAsInt());
  }
}
