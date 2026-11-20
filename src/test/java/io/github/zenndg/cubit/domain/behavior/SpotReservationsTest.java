package io.github.zenndg.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class SpotReservationsTest {
  private static final String OVERWORLD = "minecraft:overworld";
  private static final String NETHER = "minecraft:the_nether";
  private static final long STAND = 7L;
  private static final long OTHER_STAND = 8L;
  private static final UUID FIRST = new UUID(1, 1);
  private static final UUID SECOND = new UUID(2, 2);

  /** Each Fisher holds its own stand, and another one is not given it (answer 1189). */
  @Test
  void aHeldStandIsNotGivenToAnother() {
    SpotReservations reservations = new SpotReservations();
    assertTrue(reservations.reserve(OVERWORLD, STAND, FIRST));
    assertFalse(reservations.reserve(OVERWORLD, STAND, SECOND));
    assertFalse(reservations.freeFor(OVERWORLD, STAND, SECOND));
    assertTrue(reservations.freeFor(OVERWORLD, STAND, FIRST));
  }

  @Test
  void takingAnotherStandLetsGoOfTheFirst() {
    SpotReservations reservations = new SpotReservations();
    reservations.reserve(OVERWORLD, STAND, FIRST);
    reservations.reserve(OVERWORLD, OTHER_STAND, FIRST);
    assertTrue(reservations.reserve(OVERWORLD, STAND, SECOND));
  }

  @Test
  void releasingFreesTheStand() {
    SpotReservations reservations = new SpotReservations();
    reservations.reserve(OVERWORLD, STAND, FIRST);
    reservations.release(FIRST);
    assertTrue(reservations.freeFor(OVERWORLD, STAND, SECOND));
    reservations.release(SECOND);
  }

  @Test
  void theSameBlockInAnotherDimensionIsAnotherStand() {
    SpotReservations reservations = new SpotReservations();
    reservations.reserve(OVERWORLD, STAND, FIRST);
    assertTrue(reservations.reserve(NETHER, STAND, SECOND));
  }
}
