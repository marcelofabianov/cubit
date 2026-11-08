package io.github.marcelofabianov.cubit.domain.ownership;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OwnerProtectionTest {
  private static final Optional<UUID> OWNER = Optional.of(new UUID(1, 1));
  private static final Optional<UUID> STRANGER = Optional.of(new UUID(2, 2));

  @Test
  void damageCreditedToTheOwnerIsBlocked() {
    assertTrue(OwnerProtection.blocksDamage(OWNER, OWNER));
  }

  @Test
  void damageFromSomeoneElseIsNotBlocked() {
    assertFalse(OwnerProtection.blocksDamage(OWNER, STRANGER));
  }

  @Test
  void damageWithoutAnAttackerIsNotBlocked() {
    assertFalse(OwnerProtection.blocksDamage(OWNER, Optional.empty()));
  }

  @Test
  void aRobotWithoutOwnerHasNoProtection() {
    assertFalse(OwnerProtection.blocksDamage(Optional.empty(), Optional.empty()));
    assertFalse(OwnerProtection.blocksDamage(Optional.empty(), STRANGER));
  }
}
