package io.github.zenndg.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DoorMarksTest {
  private static final String OVERWORLD = "minecraft:overworld";
  private static final String NETHER = "minecraft:the_nether";
  private static final String BLANK = " ";
  private static final MarkedDoor FIRST = new MarkedDoor(1L, OVERWORLD);
  private static final MarkedDoor SECOND = new MarkedDoor(2L, OVERWORLD);
  private static final MarkedDoor IN_THE_NETHER = new MarkedDoor(3L, NETHER);
  private static final int TWO = 2;

  @Test
  void aClickMarksADoorAfterTheOthers() {
    DoorMarks.Clicked clicked = new DoorMarks(List.of(FIRST)).clicked(SECOND, TWO);

    assertEquals(DoorMarks.Mark.ADDED, clicked.mark());
    assertEquals(List.of(FIRST, SECOND), clicked.marks().doors());
  }

  @Test
  void aClickOnAMarkedDoorUnmarksIt() {
    DoorMarks.Clicked clicked = new DoorMarks(List.of(FIRST, SECOND)).clicked(FIRST, TWO);

    assertEquals(DoorMarks.Mark.REMOVED, clicked.mark());
    assertEquals(List.of(SECOND), clicked.marks().doors());
  }

  /** The limit counts the doors of every dimension (answer 1369). */
  @Test
  void theLimitCountsEveryDimension() {
    DoorMarks two = new DoorMarks(List.of(FIRST, IN_THE_NETHER));

    DoorMarks.Clicked clicked = two.clicked(SECOND, TWO);

    assertEquals(DoorMarks.Mark.FULL, clicked.mark());
    assertEquals(two, clicked.marks());
  }

  @Test
  void withoutLastAndWithoutTakeDoorsOut() {
    DoorMarks two = new DoorMarks(List.of(FIRST, SECOND));

    assertEquals(List.of(FIRST), two.withoutLast().doors());
    assertEquals(List.of(SECOND), two.without(FIRST).doors());
    assertTrue(DoorMarks.NONE.withoutLast().isEmpty());
  }

  @Test
  void theBlocksOfADimensionAreOnlyThoseMarkedThere() {
    DoorMarks marks = new DoorMarks(List.of(FIRST, IN_THE_NETHER));

    assertEquals(Set.of(FIRST.block()), marks.blocksIn(OVERWORLD));
    assertTrue(marks.contains(IN_THE_NETHER));
    assertFalse(marks.contains(SECOND));
  }

  @Test
  void aDoorNeedsADimension() {
    assertThrows(IllegalArgumentException.class, () -> new MarkedDoor(1L, BLANK));
  }
}
