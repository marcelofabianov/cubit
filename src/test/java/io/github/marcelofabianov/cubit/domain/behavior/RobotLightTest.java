package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RobotLightTest {
  private static final LightLevel SCREEN = new LightLevel(4);
  private static final LightLevel BASE_LAMP = new LightLevel(10);
  private static final LightLevel ADVANCED_LAMP = new LightLevel(12);
  private static final LightLevel ELITE_LAMP = new LightLevel(14);
  private static final LightLevel ULTIMATE_LAMP = new LightLevel(15);
  private static final LightLevel WEAK_LAMP = new LightLevel(2);
  private static final LightLevel NO_LIGHT = new LightLevel(0);

  @Test
  void theLitLampWinsOverTheScreenInEveryTier() {
    for (LightLevel lamp : new LightLevel[] {BASE_LAMP, ADVANCED_LAMP, ELITE_LAMP, ULTIMATE_LAMP}) {
      assertEquals(lamp, RobotLight.level(true, SCREEN, true, lamp));
    }
  }

  @Test
  void aWeakLampDoesNotDimTheScreen() {
    assertEquals(SCREEN, RobotLight.level(true, SCREEN, true, WEAK_LAMP));
  }

  @Test
  void withTheLampOutOnlyTheScreenLights() {
    assertEquals(SCREEN, RobotLight.level(true, SCREEN, false, BASE_LAMP));
  }

  @Test
  void aDarkScreenMeansNoLightAtAll() {
    assertEquals(NO_LIGHT, RobotLight.level(false, SCREEN, false, BASE_LAMP));
    assertEquals(NO_LIGHT, RobotLight.level(false, SCREEN, true, BASE_LAMP));
  }
}
