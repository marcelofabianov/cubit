package io.github.zenndg.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RechargeSplashReceptionTest {
  private static final UUID OWNER = new UUID(1, 1);
  private static final UUID SOMEONE_ELSE = new UUID(2, 2);
  private static final RechargeSplashDose RECEIVING = new RechargeSplashDose(new Energy(750), 1, 1);

  @Test
  void theRobotOfTheThrowerInTheRadiusStartsReceiving() {
    assertEquals(
        RechargeSplashReception.STARTS,
        RechargeSplashReception.of(Optional.of(OWNER), OWNER, true, RechargeSplashDose.NONE));
  }

  @Test
  void aRobotStillReceivingGetsNothingAndTheThrowerIsTold() {
    assertEquals(
        RechargeSplashReception.STILL_RECHARGING,
        RechargeSplashReception.of(Optional.of(OWNER), OWNER, true, RECEIVING));
  }

  @Test
  void anotherOwnerAnOwnerlessRobotOrOneOutOfTheRadiusIsIgnored() {
    assertEquals(
        RechargeSplashReception.IGNORED,
        RechargeSplashReception.of(
            Optional.of(SOMEONE_ELSE), OWNER, true, RechargeSplashDose.NONE));
    assertEquals(
        RechargeSplashReception.IGNORED,
        RechargeSplashReception.of(Optional.empty(), OWNER, true, RechargeSplashDose.NONE));
    assertEquals(
        RechargeSplashReception.IGNORED,
        RechargeSplashReception.of(Optional.of(OWNER), OWNER, false, RechargeSplashDose.NONE));
  }
}
