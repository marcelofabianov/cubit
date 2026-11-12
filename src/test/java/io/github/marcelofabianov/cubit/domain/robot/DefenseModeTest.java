package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class DefenseModeTest {
  private static final String PEACEFUL_KEY = "peaceful";
  private static final String RETALIATE_KEY = "retaliate";
  private static final String AGGRESSIVE_KEY = "aggressive";
  private static final String UNKNOWN_KEY = "berserk";

  @Test
  void keyIsTheNameUsedInConfigAndSaves() {
    assertEquals(PEACEFUL_KEY, DefenseMode.PEACEFUL.key());
    assertEquals(RETALIATE_KEY, DefenseMode.RETALIATE.key());
    assertEquals(AGGRESSIVE_KEY, DefenseMode.AGGRESSIVE.key());
  }

  @Test
  void findsAModeByItsKey() {
    assertEquals(Optional.of(DefenseMode.AGGRESSIVE), DefenseMode.fromKey(AGGRESSIVE_KEY));
  }

  @Test
  void unknownKeyFindsNothing() {
    assertEquals(Optional.empty(), DefenseMode.fromKey(UNKNOWN_KEY));
  }

  @Test
  void aNewRobotIsPeaceful() {
    assertEquals(DefenseMode.PEACEFUL, DefenseMode.INITIAL);
  }
}
