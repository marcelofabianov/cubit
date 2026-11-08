package io.github.marcelofabianov.cubit.domain.dock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DockRemovalTest {
  private static final UUID OWNER = new UUID(1, 1);
  private static final UUID STRANGER = new UUID(2, 2);

  @Test
  void theOwnerRemovesTheirDock() {
    assertTrue(DockRemoval.allows(false, false, Optional.of(OWNER), OWNER));
  }

  @Test
  void aStrangerDoesNotRemoveIt() {
    assertFalse(DockRemoval.allows(false, false, Optional.of(OWNER), STRANGER));
  }

  @Test
  void beingCreativeOrAnOperatorAloneIsNotEnough() {
    assertFalse(DockRemoval.allows(true, false, Optional.of(OWNER), STRANGER));
    assertFalse(DockRemoval.allows(false, true, Optional.of(OWNER), STRANGER));
  }

  @Test
  void anOperatorInCreativeRemovesADockOfSomeoneElse() {
    assertTrue(DockRemoval.allows(true, true, Optional.of(OWNER), STRANGER));
  }

  @Test
  void withoutAnOwnerOnlyAnOperatorInCreativeRemovesIt() {
    assertFalse(DockRemoval.allows(false, false, Optional.empty(), STRANGER));
    assertFalse(DockRemoval.allows(true, false, Optional.empty(), STRANGER));
    assertTrue(DockRemoval.allows(true, true, Optional.empty(), STRANGER));
  }
}
