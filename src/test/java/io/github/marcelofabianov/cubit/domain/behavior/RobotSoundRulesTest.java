package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RobotSoundRulesTest {
  private static final Energy LOW = new Energy(10);
  private static final Energy HIGH = new Energy(20);

  @Test
  void anInactiveRobotThatBecomesActivePlaysPowerOn() {
    assertEquals(
        Optional.of(PowerSound.POWER_ON),
        RobotSoundRules.powerSound(PowerState.INACTIVE_SWITCHED_OFF, PowerState.ACTIVE));
    assertEquals(
        Optional.of(PowerSound.POWER_ON),
        RobotSoundRules.powerSound(PowerState.INACTIVE_EXHAUSTED, PowerState.ACTIVE));
  }

  @Test
  void leavingTheBaseSwitchedOnPlaysPowerOn() {
    assertEquals(
        Optional.of(PowerSound.POWER_ON),
        RobotSoundRules.powerSound(PowerState.HIBERNATING, PowerState.ACTIVE));
  }

  @Test
  void anActiveRobotThatBecomesInactivePlaysPowerOff() {
    assertEquals(
        Optional.of(PowerSound.POWER_OFF),
        RobotSoundRules.powerSound(PowerState.ACTIVE, PowerState.INACTIVE_EXHAUSTED));
    assertEquals(
        Optional.of(PowerSound.POWER_OFF),
        RobotSoundRules.powerSound(PowerState.ACTIVE, PowerState.INACTIVE_SWITCHED_OFF));
  }

  @Test
  void enteringTheBasePlaysNoPowerSound() {
    assertEquals(
        Optional.empty(), RobotSoundRules.powerSound(PowerState.ACTIVE, PowerState.HIBERNATING));
    assertEquals(
        Optional.empty(),
        RobotSoundRules.powerSound(PowerState.INACTIVE_EXHAUSTED, PowerState.HIBERNATING));
  }

  @Test
  void leavingTheBaseSwitchedOffPlaysNoPowerSound() {
    assertEquals(
        Optional.empty(),
        RobotSoundRules.powerSound(PowerState.HIBERNATING, PowerState.INACTIVE_SWITCHED_OFF));
  }

  @Test
  void anUnchangedStatePlaysNothing() {
    assertEquals(
        Optional.empty(), RobotSoundRules.powerSound(PowerState.ACTIVE, PowerState.ACTIVE));
  }

  @Test
  void tracksPlayOnlyForAnActiveRobotThatWalks() {
    assertEquals(
        Set.of(ContinuousSound.TRACKS),
        RobotSoundRules.playing(PowerState.ACTIVE, true, false, false));
    assertEquals(Set.of(), RobotSoundRules.playing(PowerState.ACTIVE, false, false, false));
    assertEquals(
        Set.of(), RobotSoundRules.playing(PowerState.INACTIVE_SWITCHED_OFF, true, false, false));
  }

  @Test
  void shortCircuitPlaysWhileBurningInAnyState() {
    assertEquals(
        Set.of(ContinuousSound.SHORT_CIRCUIT),
        RobotSoundRules.playing(PowerState.INACTIVE_SWITCHED_OFF, false, true, false));
    assertEquals(
        Set.of(ContinuousSound.TRACKS, ContinuousSound.SHORT_CIRCUIT),
        RobotSoundRules.playing(PowerState.ACTIVE, true, true, false));
  }

  @Test
  void rechargingPlaysInAnyStateWhileCharging() {
    assertEquals(
        Set.of(ContinuousSound.RECHARGING),
        RobotSoundRules.playing(PowerState.HIBERNATING, false, false, true));
    assertEquals(
        Set.of(ContinuousSound.RECHARGING),
        RobotSoundRules.playing(PowerState.INACTIVE_EXHAUSTED, false, false, true));
  }

  @Test
  void onlyARisingChargeCountsAsCharging() {
    assertTrue(RobotSoundRules.isCharging(LOW, HIGH));
    assertFalse(RobotSoundRules.isCharging(HIGH, HIGH));
    assertFalse(RobotSoundRules.isCharging(HIGH, LOW));
  }
}
