package io.github.marcelofabianov.cubit.domain.tier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class DeathPointLevelTest {
  private static final String GUIDE_KEY = "guide";
  private static final String UNKNOWN_KEY = "teleport";

  @Test
  void eachLevelDoesWhatTheTierAllows() {
    assertFalse(DeathPointLevel.LOCKED.marks());
    assertTrue(DeathPointLevel.MARK.marks());
    assertFalse(DeathPointLevel.MARK.guides());
    assertTrue(DeathPointLevel.GUIDE.marks());
    assertTrue(DeathPointLevel.GUIDE.guides());
  }

  /** The config names are a contract (answer 1129). */
  @Test
  void theKeysRoundTrip() {
    for (DeathPointLevel level : DeathPointLevel.values()) {
      assertEquals(Optional.of(level), DeathPointLevel.fromKey(level.key()));
    }
    assertEquals(GUIDE_KEY, DeathPointLevel.GUIDE.key());
  }

  @Test
  void anUnknownKeyIsNoLevel() {
    assertEquals(Optional.empty(), DeathPointLevel.fromKey(UNKNOWN_KEY));
  }
}
