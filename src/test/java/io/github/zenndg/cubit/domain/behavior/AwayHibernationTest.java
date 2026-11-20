package io.github.zenndg.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class AwayHibernationTest {
  private static final boolean CARD = true;
  private static final boolean NO_CARD = false;
  private static final boolean IN_AREA = true;
  private static final boolean OUTSIDE = false;
  private static final boolean FULL = true;
  private static final boolean ROOM = false;
  private static final boolean CAN_UNLOAD = true;
  private static final boolean CANNOT_UNLOAD = false;
  private static final Optional<HibernationReason> NO_WATER =
      Optional.of(HibernationReason.NO_WATER);
  private static final Optional<HibernationReason> NONE = Optional.empty();

  /** With a card, in an area, full and with nowhere to unload, it hibernates (answer 1165). */
  @Test
  void aFullCubitWithACardAndNowhereToUnloadHibernates() {
    assertEquals(
        Optional.of(HibernationReason.INVENTORY_FULL),
        AwayHibernation.reason(CARD, IN_AREA, FULL, CANNOT_UNLOAD));
  }

  /** Without a card, following, with room or with a chest, it keeps going (answers 1052, 1088). */
  @Test
  void otherwiseItKeepsGoing() {
    assertEquals(Optional.empty(), AwayHibernation.reason(NO_CARD, IN_AREA, FULL, CANNOT_UNLOAD));
    assertEquals(Optional.empty(), AwayHibernation.reason(CARD, OUTSIDE, FULL, CANNOT_UNLOAD));
    assertEquals(Optional.empty(), AwayHibernation.reason(CARD, IN_AREA, ROOM, CANNOT_UNLOAD));
    assertEquals(Optional.empty(), AwayHibernation.reason(CARD, IN_AREA, FULL, CAN_UNLOAD));
  }

  /** The job may stop it too, as the Fisher with no water in its area (answer 1188). */
  @Test
  void theJobReasonStopsACubitWithACardInItsArea() {
    assertEquals(
        Optional.of(HibernationReason.NO_WATER),
        AwayHibernation.reason(CARD, IN_AREA, ROOM, CAN_UNLOAD, NO_WATER));
  }

  /** The full inventory comes first, because unloading is what Resume needs to fix first. */
  @Test
  void aFullInventoryComesBeforeTheJobReason() {
    assertEquals(
        Optional.of(HibernationReason.INVENTORY_FULL),
        AwayHibernation.reason(CARD, IN_AREA, FULL, CANNOT_UNLOAD, NO_WATER));
  }

  @Test
  void theJobReasonCountsOnlyWithACardInItsArea() {
    assertEquals(
        Optional.empty(), AwayHibernation.reason(NO_CARD, IN_AREA, ROOM, CAN_UNLOAD, NO_WATER));
    assertEquals(
        Optional.empty(), AwayHibernation.reason(CARD, OUTSIDE, ROOM, CAN_UNLOAD, NO_WATER));
    assertEquals(Optional.empty(), AwayHibernation.reason(CARD, IN_AREA, ROOM, CAN_UNLOAD, NONE));
  }
}
