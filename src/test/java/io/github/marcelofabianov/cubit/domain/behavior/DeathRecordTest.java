package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DeathRecordTest {
  private static final String OVERWORLD = "minecraft:overworld";
  private static final String NETHER = "minecraft:the_nether";
  private static final String BLANK = " ";
  private static final String CAUSE = "{\"text\":\"Steve fell\"}";
  private static final long DAY_TIME = 1_000L;
  private static final int X = 10;
  private static final int Y = 64;
  private static final int Z = -20;

  @Test
  void knowsWhetherItHappenedInTheOverworld() {
    assertTrue(new DeathRecord(DAY_TIME, X, Y, Z, OVERWORLD, CAUSE).inOverworld());
    assertFalse(new DeathRecord(DAY_TIME, X, Y, Z, NETHER, CAUSE).inOverworld());
  }

  @Test
  void rejectsWhatCannotBeADeath() {
    assertThrows(
        IllegalArgumentException.class, () -> new DeathRecord(-1L, X, Y, Z, OVERWORLD, CAUSE));
    assertThrows(
        IllegalArgumentException.class, () -> new DeathRecord(DAY_TIME, X, Y, Z, BLANK, CAUSE));
    assertThrows(
        IllegalArgumentException.class, () -> new DeathRecord(DAY_TIME, X, Y, Z, OVERWORLD, null));
  }
}
