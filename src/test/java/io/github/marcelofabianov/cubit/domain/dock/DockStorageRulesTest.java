package io.github.marcelofabianov.cubit.domain.dock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import org.junit.jupiter.api.Test;

class DockStorageRulesTest {
  private static final Energy CAPACITY = new Energy(210_000);
  private static final Battery FULL = new Battery(CAPACITY, CAPACITY);
  private static final Battery HALF = new Battery(CAPACITY, new Energy(105_000));
  private static final Battery EMPTY = new Battery(CAPACITY, Energy.ZERO);
  private static final Optional<Battery> NO_BANK = Optional.empty();
  private static final boolean OCCUPIED = true;
  private static final boolean FREE = false;
  private static final boolean ROBOT_FULL = true;
  private static final boolean ROBOT_CHARGING = false;
  private static final int SECOND = 1;
  private static final int THIRD = 2;

  @Test
  void onlyTheToolboxThePowerBankAndTheRobotItemsAreStored() {
    assertTrue(DockStorageRules.accepts(DockStoredKind.TOOLBOX));
    assertTrue(DockStorageRules.accepts(DockStoredKind.POWER_BANK));
    assertTrue(DockStorageRules.accepts(DockStoredKind.ROBOT));
    assertFalse(DockStorageRules.accepts(DockStoredKind.OTHER));
  }

  @Test
  void banksChargeWithoutARobotOrWithAFullOne() {
    assertTrue(DockStorageRules.chargesBanks(FREE, ROBOT_CHARGING));
    assertTrue(DockStorageRules.chargesBanks(OCCUPIED, ROBOT_FULL));
    assertFalse(DockStorageRules.chargesBanks(OCCUPIED, ROBOT_CHARGING));
  }

  @Test
  void theFirstBankThatIsNotFullCharges() {
    List<Optional<Battery>> slots =
        List.of(Optional.of(FULL), NO_BANK, Optional.of(HALF), Optional.of(EMPTY));

    assertEquals(OptionalInt.of(THIRD), DockStorageRules.bankToCharge(slots));
  }

  @Test
  void anEmptyBankCountsAsNotFull() {
    assertEquals(
        OptionalInt.of(SECOND),
        DockStorageRules.bankToCharge(List.of(NO_BANK, Optional.of(EMPTY))));
  }

  @Test
  void withEveryBankFullNothingCharges() {
    assertEquals(
        OptionalInt.empty(), DockStorageRules.bankToCharge(List.of(Optional.of(FULL), NO_BANK)));
  }

  @Test
  void automationTakesTheToolboxTheRobotItemsAndOnlyAFullBank() {
    assertTrue(DockStorageRules.releases(DockStoredKind.TOOLBOX, false));
    assertTrue(DockStorageRules.releases(DockStoredKind.ROBOT, false));
    assertTrue(DockStorageRules.releases(DockStoredKind.POWER_BANK, true));
    assertFalse(DockStorageRules.releases(DockStoredKind.POWER_BANK, false));
    assertFalse(DockStorageRules.releases(DockStoredKind.OTHER, true));
  }
}
