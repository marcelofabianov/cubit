package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class ScanRulesTest {
  private static final boolean ON = true;
  private static final boolean OFF = false;
  private static final boolean FOLLOWS = true;
  private static final boolean ALONE = false;
  private static final int INTERVAL = 200;
  private static final int JUST_BEFORE = 199;
  private static final int SECOND_TIME = 400;

  @Test
  void anActiveRobotThatFollowsScans() {
    assertTrue(ScanRules.runs(ON, PowerState.ACTIVE, FOLLOWS));
  }

  @Test
  void theSwitchStopsIt() {
    assertFalse(ScanRules.runs(OFF, PowerState.ACTIVE, FOLLOWS));
  }

  @Test
  void aRobotThatDoesNotFollowDoesNotScan() {
    assertFalse(ScanRules.runs(ON, PowerState.ACTIVE, ALONE));
  }

  @Test
  void onlyTheActiveStateScans() {
    assertFalse(ScanRules.runs(ON, PowerState.HIBERNATING, FOLLOWS));
    assertFalse(ScanRules.runs(ON, PowerState.INACTIVE_EXHAUSTED, FOLLOWS));
    assertFalse(ScanRules.runs(ON, PowerState.INACTIVE_SWITCHED_OFF, FOLLOWS));
  }

  @Test
  void aScanIsDueEachInterval() {
    assertTrue(ScanRules.isDue(INTERVAL, INTERVAL));
    assertTrue(ScanRules.isDue(SECOND_TIME, INTERVAL));
    assertFalse(ScanRules.isDue(JUST_BEFORE, INTERVAL));
  }

  @Test
  void placingARobotDoesNotScanAtOnce() {
    assertFalse(ScanRules.isDue(0, INTERVAL));
  }

  @Test
  void anEnemyIsHostile() {
    assertTrue(ScanRules.isHostile(true, false));
  }

  @Test
  void aNeutralMobIsHostileOnlyWhenItTargetsTheOwner() {
    assertTrue(ScanRules.isHostile(false, true));
    assertFalse(ScanRules.isHostile(false, false));
  }
}
