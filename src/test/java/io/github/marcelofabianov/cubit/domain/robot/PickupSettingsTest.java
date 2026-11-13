package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import java.util.List;
import org.junit.jupiter.api.Test;

class PickupSettingsTest {
  private static final String DIRT_ID = "minecraft:dirt";
  private static final PickupFilter DIRT_ONLY =
      new PickupFilter(PickupMode.WHITELIST, List.of(PickupEntry.item(DIRT_ID)));
  private static final float HEALTH = 20f;

  @Test
  void everySwitchStartsOnWithAnEmptyBlacklist() {
    assertTrue(PickupSettings.INITIAL.pickupOn());
    assertTrue(PickupSettings.INITIAL.magnetOn());
    assertTrue(PickupSettings.INITIAL.fetchOn());
    assertEquals(PickupFilter.EMPTY, PickupSettings.INITIAL.filter());
  }

  @Test
  void eachSwitchTogglesOnItsOwn() {
    PickupSettings noPickup = PickupSettings.INITIAL.toggledPickup();
    PickupSettings noMagnet = PickupSettings.INITIAL.toggledMagnet();
    PickupSettings noFetch = PickupSettings.INITIAL.toggledFetch();

    assertFalse(noPickup.pickupOn());
    assertTrue(noPickup.magnetOn());
    assertTrue(noPickup.fetchOn());
    assertTrue(noMagnet.pickupOn());
    assertFalse(noMagnet.magnetOn());
    assertTrue(noMagnet.fetchOn());
    assertTrue(noFetch.pickupOn());
    assertTrue(noFetch.magnetOn());
    assertFalse(noFetch.fetchOn());
  }

  @Test
  void theFilterIsReplacedKeepingTheSwitches() {
    PickupSettings changed =
        PickupSettings.INITIAL.toggledMagnet().toggledFetch().withFilter(DIRT_ONLY);

    assertEquals(DIRT_ONLY, changed.filter());
    assertFalse(changed.magnetOn());
    assertFalse(changed.fetchOn());
  }

  @Test
  void aRobotSavedBeforePickupExistedGetsTheInitialSettings() {
    RobotState older = new RobotState(RobotTier.BASE, Energy.ZERO, HEALTH);

    assertEquals(PickupSettings.INITIAL, older.pickup());
  }

  @Test
  void dyingAndRepairingKeepThePickupSettings() {
    PickupSettings custom = PickupSettings.INITIAL.withFilter(DIRT_ONLY);
    RobotState state = new RobotState(RobotTier.BASE, Energy.ZERO, HEALTH).withPickup(custom);

    assertEquals(custom, state.damaged().pickup());
  }
}
