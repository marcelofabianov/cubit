package io.github.zenndg.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.OptionalInt;
import org.junit.jupiter.api.Test;

class CropRipenessTest {
  private static final int MAX_AGE = 7;
  private static final OptionalInt GROWN = OptionalInt.of(MAX_AGE);
  private static final OptionalInt YOUNG = OptionalInt.of(3);
  private static final OptionalInt NO_AGE = OptionalInt.empty();
  private static final boolean PLANT = true;
  private static final boolean NOT_PLANT = false;
  private static final boolean ADDED = true;
  private static final boolean NOT_ADDED = false;
  private static final boolean IGNORED = true;
  private static final boolean KEPT = false;

  /** Any plant at its last age, also from another mod (answer 1222). */
  @Test
  void aPlantAtItsLastAgeIsRipe() {
    assertTrue(CropRipeness.ripe(PLANT, GROWN, MAX_AGE, NOT_ADDED, KEPT));
    assertFalse(CropRipeness.ripe(PLANT, YOUNG, MAX_AGE, NOT_ADDED, KEPT));
  }

  /** The ignored tag wins, as for the saplings and the bamboo. */
  @Test
  void anIgnoredPlantIsNeverRipe() {
    assertFalse(CropRipeness.ripe(PLANT, GROWN, MAX_AGE, ADDED, IGNORED));
  }

  @Test
  void theAddedTagBringsAPlantWithoutAgeOrOfAnotherKind() {
    assertTrue(CropRipeness.ripe(NOT_PLANT, NO_AGE, 0, ADDED, KEPT));
    assertTrue(CropRipeness.ripe(NOT_PLANT, GROWN, MAX_AGE, ADDED, KEPT));
    assertFalse(CropRipeness.ripe(NOT_PLANT, GROWN, MAX_AGE, NOT_ADDED, KEPT));
    assertFalse(CropRipeness.ripe(PLANT, NO_AGE, 0, NOT_ADDED, KEPT));
  }
}
