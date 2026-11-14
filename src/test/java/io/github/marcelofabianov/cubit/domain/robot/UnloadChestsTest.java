package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class UnloadChestsTest {
  private static final String OVERWORLD = "minecraft:overworld";
  private static final String BLANK = " ";
  private static final UnloadChest FIRST = new UnloadChest(1L, OVERWORLD);
  private static final UnloadChest SECOND = new UnloadChest(2L, OVERWORLD);
  private static final UnloadChest THIRD = new UnloadChest(3L, OVERWORLD);
  private static final int TWO = 2;

  @Test
  void aClickAddsTheChestAfterTheOthers() {
    UnloadChests.Clicked clicked = new UnloadChests(List.of(FIRST)).clicked(SECOND, TWO);

    assertEquals(UnloadChests.Mark.ADDED, clicked.mark());
    assertEquals(List.of(FIRST, SECOND), clicked.chests().chests());
  }

  @Test
  void aClickOnAMarkedChestRemovesIt() {
    UnloadChests.Clicked clicked = new UnloadChests(List.of(FIRST, SECOND)).clicked(FIRST, TWO);

    assertEquals(UnloadChests.Mark.REMOVED, clicked.mark());
    assertEquals(List.of(SECOND), clicked.chests().chests());
  }

  @Test
  void theTierLimitsTheChests() {
    UnloadChests two = new UnloadChests(List.of(FIRST, SECOND));

    assertEquals(UnloadChests.Mark.FULL, two.clicked(THIRD, TWO).mark());
    assertEquals(List.of(FIRST), two.fitting(1).chests());
    assertTrue(two.fitting(0).isEmpty());
  }

  /** Shift and a click away from a marked chest take out the last one (answer 1099). */
  @Test
  void withoutLastTakesOutTheLastMarked() {
    UnloadChests two = new UnloadChests(List.of(FIRST, SECOND));

    assertEquals(List.of(FIRST), two.withoutLast().chests());
    assertTrue(UnloadChests.NONE.withoutLast().isEmpty());
  }

  @Test
  void aChestNeedsADimension() {
    assertThrows(IllegalArgumentException.class, () -> new UnloadChest(1L, BLANK));
    assertTrue(FIRST.isIn(OVERWORLD));
  }
}
