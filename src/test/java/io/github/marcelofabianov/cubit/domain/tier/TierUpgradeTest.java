package io.github.marcelofabianov.cubit.domain.tier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TierUpgradeTest {
  @Test
  void aModuleOfTheNextTierUpgrades() {
    assertEquals(TierUpgrade.UPGRADES, TierUpgrade.of(RobotTier.BASE, RobotTier.ADVANCED));
    assertEquals(TierUpgrade.UPGRADES, TierUpgrade.of(RobotTier.ADVANCED, RobotTier.ELITE));
    assertEquals(TierUpgrade.UPGRADES, TierUpgrade.of(RobotTier.ELITE, RobotTier.ULTIMATE));
  }

  @Test
  void aModuleThatSkipsATierNeedsThePreviousOneFirst() {
    assertEquals(TierUpgrade.NEEDS_PREVIOUS_TIER, TierUpgrade.of(RobotTier.BASE, RobotTier.ELITE));
    assertEquals(
        TierUpgrade.NEEDS_PREVIOUS_TIER, TierUpgrade.of(RobotTier.BASE, RobotTier.ULTIMATE));
    assertEquals(
        TierUpgrade.NEEDS_PREVIOUS_TIER, TierUpgrade.of(RobotTier.ADVANCED, RobotTier.ULTIMATE));
  }

  @Test
  void aModuleOfTheSameTierFindsTheRobotAlreadyThere() {
    for (RobotTier tier : RobotTier.values()) {
      assertEquals(TierUpgrade.ALREADY_THERE, TierUpgrade.of(tier, tier));
    }
  }

  @Test
  void aModuleOfALowerTierFindsTheRobotAlreadyThere() {
    assertEquals(TierUpgrade.ALREADY_THERE, TierUpgrade.of(RobotTier.ELITE, RobotTier.ADVANCED));
    assertEquals(TierUpgrade.ALREADY_THERE, TierUpgrade.of(RobotTier.ULTIMATE, RobotTier.ELITE));
  }

  @Test
  void onlyAnUpgradeConsumesTheModule() {
    assertEquals(true, TierUpgrade.UPGRADES.consumesModule());
    assertEquals(false, TierUpgrade.NEEDS_PREVIOUS_TIER.consumesModule());
    assertEquals(false, TierUpgrade.ALREADY_THERE.consumesModule());
  }
}
