package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class InventoryFitTest {
  private static final String FIRST = "first";
  private static final String SECOND = "second";
  private static final String THIRD = "third";
  private static final List<String> ITEMS = List.of(FIRST, SECOND, THIRD);
  private static final int ROOM_FOR_TWO = 2;
  private static final int ROOM_FOR_ALL = 3;

  @Test
  void whatFitsIsKeptInOrderAndTheRestIsSurplus() {
    InventoryFit<String> fit = InventoryFit.of(ITEMS, ROOM_FOR_TWO);

    assertEquals(List.of(FIRST, SECOND), fit.kept());
    assertEquals(List.of(THIRD), fit.surplus());
  }

  @Test
  void withEnoughRoomNothingIsSurplus() {
    InventoryFit<String> fit = InventoryFit.of(ITEMS, ROOM_FOR_ALL);

    assertEquals(ITEMS, fit.kept());
    assertEquals(List.of(), fit.surplus());
  }

  @Test
  void withoutRoomEverythingIsSurplus() {
    InventoryFit<String> fit = InventoryFit.of(ITEMS, 0);

    assertEquals(List.of(), fit.kept());
    assertEquals(ITEMS, fit.surplus());
  }

  @Test
  void rejectsANegativeCapacity() {
    assertThrows(IllegalArgumentException.class, () -> InventoryFit.of(ITEMS, -1));
  }
}
