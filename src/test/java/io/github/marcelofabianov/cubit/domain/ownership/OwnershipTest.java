package io.github.marcelofabianov.cubit.domain.ownership;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OwnershipTest {
  private static final UUID OWNER = new UUID(1, 1);
  private static final UUID STRANGER = new UUID(2, 2);

  @Test
  void theOwnerOwns() {
    assertTrue(Ownership.owns(Optional.of(OWNER), OWNER));
  }

  @Test
  void aStrangerDoesNotOwn() {
    assertFalse(Ownership.owns(Optional.of(OWNER), STRANGER));
  }

  @Test
  void withoutAnOwnerNobodyOwns() {
    assertFalse(Ownership.owns(Optional.empty(), OWNER));
  }

  @Test
  void twoThingsOfTheSameOwnerMatch() {
    assertTrue(Ownership.sameOwner(Optional.of(OWNER), Optional.of(OWNER)));
  }

  @Test
  void twoThingsOfDifferentOwnersDoNotMatch() {
    assertFalse(Ownership.sameOwner(Optional.of(OWNER), Optional.of(STRANGER)));
  }

  @Test
  void twoThingsWithoutAnOwnerDoNotMatch() {
    assertFalse(Ownership.sameOwner(Optional.empty(), Optional.empty()));
  }

  @Test
  void oneThingWithoutAnOwnerDoesNotMatch() {
    assertFalse(Ownership.sameOwner(Optional.empty(), Optional.of(OWNER)));
    assertFalse(Ownership.sameOwner(Optional.of(OWNER), Optional.empty()));
  }
}
