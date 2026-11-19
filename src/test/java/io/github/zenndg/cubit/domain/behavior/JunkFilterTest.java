package io.github.zenndg.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class JunkFilterTest {
  private static final boolean KEEP_JUNK = true;
  private static final boolean DROP_JUNK = false;
  private static final boolean JUNK = true;
  private static final boolean NOT_JUNK = false;
  private static final boolean ENCHANTED = true;
  private static final boolean PLAIN = false;

  /** Keep junk starts on and keeps everything (answer 1191). */
  @Test
  void withKeepJunkOnEverythingStays() {
    assertTrue(JunkFilter.keeps(KEEP_JUNK, JUNK, PLAIN));
  }

  @Test
  void withKeepJunkOffTheJunkGoes() {
    assertFalse(JunkFilter.keeps(DROP_JUNK, JUNK, PLAIN));
    assertTrue(JunkFilter.keeps(DROP_JUNK, NOT_JUNK, PLAIN));
  }

  /** The enchanted rod of the treasure is never junk (answer 1207). */
  @Test
  void anEnchantedItemIsNeverJunk() {
    assertTrue(JunkFilter.keeps(DROP_JUNK, JUNK, ENCHANTED));
  }
}
