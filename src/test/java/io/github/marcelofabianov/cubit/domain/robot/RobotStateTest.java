package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.behavior.LightLevel;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.ConsumptionExtras;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.energy.SolarOutput;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.domain.tier.ScannerSpec;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import java.util.List;
import org.junit.jupiter.api.Test;

class RobotStateTest {
  private static final Energy CAPACITY = new Energy(300_000);
  private static final Energy INITIAL_ENERGY = new Energy(75_000);
  private static final Energy SOME_ENERGY = new Energy(1_234);
  private static final Energy TOO_MUCH_ENERGY = new Energy(999_999);
  private static final int INITIAL_PERCENT = 25;
  private static final int MAX_HEALTH = 25;
  private static final int SLOTS = 27;
  private static final int RADIUS = 15;
  private static final float SOME_HEALTH = 7.5f;
  private static final float TOO_MUCH_HEALTH = 99f;
  private static final float MIN_PLACED_HEALTH = 1f;
  private static final int REPAIR_PERCENT = 25;
  private static final String DIAMOND_TAG = "c:ores/diamond";
  private static final float REPAIRED_HEALTH = 6.25f;
  private static final Energy CONSUMPTION = new Energy(10);
  private static final Energy RECHARGE = new Energy(500);
  private static final TierSpec SPEC =
      new TierSpec(
          CAPACITY,
          CONSUMPTION,
          RECHARGE,
          SLOTS,
          MAX_HEALTH,
          RADIUS,
          1,
          new ConsumptionExtras(Energy.ZERO, Energy.ZERO, Energy.ZERO, Energy.ZERO, Energy.ZERO),
          new LightLevel(0),
          new SolarOutput(Energy.ZERO, Energy.ZERO),
          new ScannerSpec(1, 1, 1, 1, Energy.ZERO, Energy.ZERO, 1));

  @Test
  void theTorchesStartOnAlsoForOlderSaves() {
    assertTrue(new RobotState(RobotTier.BASE, SOME_ENERGY, SOME_HEALTH).torchesOn());
  }

  @Test
  void dyingAndRepairingKeepTheTorchSwitch() {
    RobotState off =
        new RobotState(
            RobotTier.ADVANCED,
            SOME_ENERGY,
            SOME_HEALTH,
            true,
            RobotColor.WHITE,
            true,
            OreFilter.EMPTY,
            true,
            DefenseMode.INITIAL,
            true,
            PickupSettings.INITIAL,
            false);

    assertFalse(off.damaged().torchesOn());
    assertFalse(off.damaged().repaired(SPEC, new Percentage(INITIAL_PERCENT)).torchesOn());
    assertFalse(off.withEnergy(SOME_ENERGY).torchesOn());
  }

  @Test
  void theAutoReturnStartsOnAlsoForOlderSaves() {
    assertTrue(new RobotState(RobotTier.BASE, SOME_ENERGY, SOME_HEALTH).autoReturnOn());
  }

  @Test
  void dyingRepairingAndOtherSwitchesKeepTheAutoReturn() {
    RobotState off = new RobotState(RobotTier.BASE, SOME_ENERGY, SOME_HEALTH).withAutoReturn(false);

    assertFalse(off.damaged().autoReturnOn());
    assertFalse(off.damaged().repaired(SPEC, new Percentage(INITIAL_PERCENT)).autoReturnOn());
    assertFalse(off.withEnergy(SOME_ENERGY).autoReturnOn());
    assertFalse(off.withPickup(PickupSettings.INITIAL).withTorches(false).autoReturnOn());
  }

  @Test
  void aNewRobotIsWhite() {
    RobotState state = RobotState.initial(RobotTier.BASE, SPEC, new Percentage(INITIAL_PERCENT));

    assertEquals(RobotColor.WHITE, state.color());
  }

  @Test
  void dyingAndBeingRepairedKeepTheColor() {
    RobotState painted =
        new RobotState(RobotTier.BASE, SOME_ENERGY, SOME_HEALTH, true, RobotColor.RED);

    RobotState damaged = painted.damaged();
    RobotState repaired = damaged.repaired(SPEC, new Percentage(REPAIR_PERCENT));

    assertEquals(RobotColor.RED, damaged.color());
    assertEquals(RobotColor.RED, repaired.color());
  }

  @Test
  void newRobotHasTheInitialChargeAndFullHealth() {
    RobotState state = RobotState.initial(RobotTier.BASE, SPEC, new Percentage(INITIAL_PERCENT));

    assertEquals(new RobotState(RobotTier.BASE, INITIAL_ENERGY, MAX_HEALTH), state);
  }

  @Test
  void validStateIsKeptAsIs() {
    RobotState state = new RobotState(RobotTier.BASE, SOME_ENERGY, SOME_HEALTH);

    assertEquals(new Battery(CAPACITY, SOME_ENERGY), state.batteryFor(SPEC));
    assertEquals(SOME_HEALTH, state.healthFor(SPEC));
  }

  @Test
  void energyAboveTheCapacityUsesTheCapacity() {
    RobotState state = new RobotState(RobotTier.BASE, TOO_MUCH_ENERGY, SOME_HEALTH);

    assertEquals(new Battery(CAPACITY, CAPACITY), state.batteryFor(SPEC));
  }

  @Test
  void healthStaysBetweenOneAndTheMaximum() {
    assertEquals(
        (float) MAX_HEALTH,
        new RobotState(RobotTier.BASE, SOME_ENERGY, TOO_MUCH_HEALTH).healthFor(SPEC));
    assertEquals(
        MIN_PLACED_HEALTH, new RobotState(RobotTier.BASE, SOME_ENERGY, 0f).healthFor(SPEC));
  }

  @Test
  void damagedRobotKeepsTheTierWithoutEnergyOrHealth() {
    assertEquals(
        new RobotState(RobotTier.BASE, Energy.ZERO, 0f),
        new RobotState(RobotTier.BASE, SOME_ENERGY, SOME_HEALTH).damaged());
  }

  @Test
  void repairedRobotHasPartOfTheHealthAndAnEmptyBattery() {
    RobotState damaged = new RobotState(RobotTier.BASE, SOME_ENERGY, 0f);

    assertEquals(
        new RobotState(RobotTier.BASE, Energy.ZERO, REPAIRED_HEALTH),
        damaged.repaired(SPEC, new Percentage(REPAIR_PERCENT)));
  }

  @Test
  void rejectsHealthThatIsNotAValidNumber() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new RobotState(RobotTier.BASE, SOME_ENERGY, Float.NaN));
    assertThrows(
        IllegalArgumentException.class, () -> new RobotState(RobotTier.BASE, SOME_ENERGY, -1f));
  }

  @Test
  void theLampStartsSwitchedOn() {
    assertTrue(RobotState.initial(RobotTier.BASE, SPEC, new Percentage(INITIAL_PERCENT)).lampOn());
    assertTrue(new RobotState(RobotTier.BASE, SOME_ENERGY, SOME_HEALTH).lampOn());
  }

  @Test
  void dyingAndRepairingKeepTheLampSwitch() {
    RobotState off = new RobotState(RobotTier.BASE, SOME_ENERGY, SOME_HEALTH, false);

    assertFalse(off.damaged().lampOn());
    assertFalse(off.damaged().repaired(SPEC, new Percentage(REPAIR_PERCENT)).lampOn());
  }

  @Test
  void theScannersStartOnAndWithoutAFilter() {
    RobotState state = new RobotState(RobotTier.BASE, SOME_ENERGY, SOME_HEALTH);

    assertTrue(state.scannersOn());
    assertEquals(OreFilter.EMPTY, state.oreFilter());
  }

  @Test
  void dyingAndRepairingKeepTheScanners() {
    OreFilter filter = new OreFilter(List.of(new OreKind(DIAMOND_TAG)));
    RobotState state =
        new RobotState(
            RobotTier.BASE, SOME_ENERGY, SOME_HEALTH, true, RobotColor.WHITE, false, filter);
    RobotState repaired = state.damaged().repaired(SPEC, new Percentage(REPAIR_PERCENT));

    assertFalse(state.damaged().scannersOn());
    assertEquals(filter, state.damaged().oreFilter());
    assertFalse(repaired.scannersOn());
    assertEquals(filter, repaired.oreFilter());
  }

  @Test
  void aNewRobotIsPeaceful() {
    assertEquals(
        DefenseMode.PEACEFUL,
        new RobotState(RobotTier.BASE, SOME_ENERGY, SOME_HEALTH).defenseMode());
  }

  @Test
  void dyingAndRepairingKeepTheDefenseMode() {
    RobotState aggressive =
        new RobotState(
            RobotTier.ELITE,
            SOME_ENERGY,
            SOME_HEALTH,
            true,
            RobotColor.WHITE,
            true,
            OreFilter.EMPTY,
            true,
            DefenseMode.AGGRESSIVE);

    RobotState repaired = aggressive.damaged().repaired(SPEC, new Percentage(REPAIR_PERCENT));

    assertEquals(DefenseMode.AGGRESSIVE, aggressive.damaged().defenseMode());
    assertEquals(DefenseMode.AGGRESSIVE, repaired.defenseMode());
  }

  @Test
  void theSolarPanelStartsSwitchedOn() {
    assertTrue(new RobotState(RobotTier.BASE, SOME_ENERGY, SOME_HEALTH).solarOn());
  }

  @Test
  void dyingAndRepairingKeepTheSolarSwitch() {
    RobotState off =
        new RobotState(
            RobotTier.BASE,
            SOME_ENERGY,
            SOME_HEALTH,
            true,
            RobotColor.WHITE,
            true,
            OreFilter.EMPTY,
            true,
            DefenseMode.PEACEFUL,
            false);

    assertFalse(off.damaged().solarOn());
    assertFalse(off.damaged().repaired(SPEC, new Percentage(REPAIR_PERCENT)).solarOn());
  }
}
