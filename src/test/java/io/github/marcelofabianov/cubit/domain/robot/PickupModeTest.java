package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PickupModeTest {
  private static final String WHITELIST_KEY = "whitelist";
  private static final String UNKNOWN_KEY = "greylist";

  @Test
  void theKeyIsTheSavedName() {
    assertEquals(WHITELIST_KEY, PickupMode.WHITELIST.key());
    assertEquals(PickupMode.WHITELIST, PickupMode.fromKeyOrInitial(WHITELIST_KEY));
  }

  @Test
  void anUnknownKeyFallsBackToTheBlacklist() {
    assertEquals(PickupMode.BLACKLIST, PickupMode.fromKeyOrInitial(UNKNOWN_KEY));
    assertEquals(PickupMode.BLACKLIST, PickupMode.INITIAL);
  }
}
