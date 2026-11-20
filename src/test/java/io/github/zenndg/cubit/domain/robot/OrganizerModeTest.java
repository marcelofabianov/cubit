package io.github.zenndg.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class OrganizerModeTest {
  private static final String UNKNOWN = "sort";

  /** It starts organizing, and the key is a contract of the save (answers 1290 and 1301). */
  @Test
  void startsOrganizingAndReadsItsKeys() {
    assertEquals(OrganizerMode.ORGANIZE, OrganizerMode.INITIAL);
    assertEquals(
        Optional.of(OrganizerMode.SUPPLY), OrganizerMode.fromKey(OrganizerMode.SUPPLY.key()));
    assertTrue(OrganizerMode.fromKey(UNKNOWN).isEmpty());
  }

  /** The line Mode of the Work tab goes from one to the other. */
  @Test
  void theLineSwitchesBetweenTheTwo() {
    assertEquals(OrganizerMode.SUPPLY, OrganizerMode.ORGANIZE.next());
    assertEquals(OrganizerMode.ORGANIZE, OrganizerMode.SUPPLY.next());
  }
}
