package io.github.zenndg.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class OrganizerMarksTest {
  private static final String OVERWORLD = "minecraft:overworld";
  private static final String NETHER = "minecraft:the_nether";
  private static final String BLANK = " ";
  private static final int UP = 1;
  private static final int NORTH = 2;
  private static final int LIMIT = 2;
  private static final int TOO_HIGH_FACE = 6;
  private static final OrganizerMark FIRST = new OrganizerMark(1L, OVERWORLD, UP);
  private static final OrganizerMark SECOND = new OrganizerMark(2L, OVERWORLD, UP);
  private static final OrganizerMark THIRD = new OrganizerMark(3L, OVERWORLD, UP);

  /** A click on a new block adds it while the tier allows, in the order of the marks (1297). */
  @Test
  void aClickAddsUpToTheLimitOfTheTier() {
    OrganizerMarks two =
        OrganizerMarks.NONE.clicked(FIRST, LIMIT).marks().clicked(SECOND, LIMIT).marks();
    assertEquals(List.of(FIRST, SECOND), two.marks());
    OrganizerMarks.Clicked full = two.clicked(THIRD, LIMIT);
    assertEquals(UnloadChests.Mark.FULL, full.mark());
    assertEquals(two, full.marks());
  }

  /** A click on a marked block takes it out, whatever face was clicked. */
  @Test
  void aClickOnAMarkedBlockTakesItOut() {
    OrganizerMarks one = OrganizerMarks.NONE.clicked(FIRST, LIMIT).marks();
    OrganizerMarks.Clicked again = one.clicked(new OrganizerMark(1L, OVERWORLD, NORTH), LIMIT);
    assertEquals(UnloadChests.Mark.REMOVED, again.mark());
    assertTrue(again.marks().isEmpty());
  }

  /** Shift and a click away take the last one out, and a lower limit keeps the first ones. */
  @Test
  void theLastOneGoesAndALowerLimitKeepsTheFirst() {
    OrganizerMarks two = new OrganizerMarks(List.of(FIRST, SECOND));
    assertEquals(List.of(FIRST), two.withoutLast().marks());
    assertEquals(List.of(FIRST), two.fitting(1).marks());
    assertTrue(OrganizerMarks.NONE.withoutLast().isEmpty());
  }

  /** The same block in another dimension is another block. */
  @Test
  void theDimensionIsPartOfThePlace() {
    OrganizerMarks one = new OrganizerMarks(List.of(FIRST));
    assertTrue(one.contains(1L, OVERWORLD));
    assertFalse(one.contains(1L, NETHER));
  }

  @Test
  void rejectsAFaceTheGameDoesNotHave() {
    assertThrows(
        IllegalArgumentException.class, () -> new OrganizerMark(1L, OVERWORLD, TOO_HIGH_FACE));
    assertThrows(IllegalArgumentException.class, () -> new OrganizerMark(1L, BLANK, UP));
  }
}
