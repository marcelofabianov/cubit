package io.github.marcelofabianov.cubit.domain.tier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class RobotTierTest {
  private static final String BASE_KEY = "base";
  private static final String ULTIMATE_KEY = "ultimate";
  private static final String UNKNOWN_KEY = "mythic";

  @Test
  void keyIsTheNameUsedInConfigAndSaves() {
    assertEquals(BASE_KEY, RobotTier.BASE.key());
    assertEquals(ULTIMATE_KEY, RobotTier.ULTIMATE.key());
  }

  @Test
  void findsATierByItsKey() {
    assertEquals(Optional.of(RobotTier.ULTIMATE), RobotTier.fromKey(ULTIMATE_KEY));
  }

  @Test
  void unknownKeyFindsNothing() {
    assertEquals(Optional.empty(), RobotTier.fromKey(UNKNOWN_KEY));
  }
}
