package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class RobotColorTest {
  private static final int COLORS = 16;
  private static final String LIGHT_BLUE_KEY = "light_blue";
  private static final String UNKNOWN_KEY = "plaid";

  @Test
  void thereIsOneColorForEachDyeOfTheGame() {
    assertEquals(COLORS, RobotColor.values().length);
  }

  @Test
  void keysFindTheirColors() {
    for (RobotColor color : RobotColor.values()) {
      assertEquals(Optional.of(color), RobotColor.fromKey(color.key()));
    }
  }

  @Test
  void keysFollowTheNamesOfTheDyes() {
    assertEquals(LIGHT_BLUE_KEY, RobotColor.LIGHT_BLUE.key());
  }

  @Test
  void anUnknownKeyFindsNothing() {
    assertTrue(RobotColor.fromKey(UNKNOWN_KEY).isEmpty());
  }
}
