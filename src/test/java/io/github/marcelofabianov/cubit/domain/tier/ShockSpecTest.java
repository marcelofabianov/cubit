package io.github.marcelofabianov.cubit.domain.tier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ShockSpecTest {
  private static final int DAMAGE = 4;
  private static final int RANGE = 5;
  private static final int INTERVAL = 40;
  private static final Energy COST = new Energy(2_000);
  private static final int NEGATIVE = -1;

  @Test
  void peacefulIsAlwaysAllowed() {
    ShockSpec spec = new ShockSpec(DAMAGE, RANGE, INTERVAL, COST, false, Set.of());

    assertTrue(spec.allows(DefenseMode.PEACEFUL));
    assertFalse(spec.allows(DefenseMode.RETALIATE));
  }

  @Test
  void allowsTheModesOfTheTier() {
    ShockSpec spec =
        new ShockSpec(
            DAMAGE,
            RANGE,
            INTERVAL,
            COST,
            false,
            Set.of(DefenseMode.RETALIATE, DefenseMode.AGGRESSIVE));

    assertTrue(spec.allows(DefenseMode.RETALIATE));
    assertTrue(spec.allows(DefenseMode.AGGRESSIVE));
  }

  @Test
  void noneOnlyAllowsPeacefulAndNeverShocks() {
    assertEquals(Set.of(DefenseMode.PEACEFUL), ShockSpec.NONE.modes());
    assertFalse(ShockSpec.NONE.canShock());
  }

  @Test
  void aShockNeedsDamageAndRange() {
    assertTrue(new ShockSpec(DAMAGE, RANGE, INTERVAL, COST, false, Set.of()).canShock());
    assertFalse(new ShockSpec(0, RANGE, INTERVAL, COST, false, Set.of()).canShock());
    assertFalse(new ShockSpec(DAMAGE, 0, INTERVAL, COST, false, Set.of()).canShock());
  }

  @Test
  void rejectsInvalidNumbers() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new ShockSpec(NEGATIVE, RANGE, INTERVAL, COST, false, Set.of()));
    assertThrows(
        IllegalArgumentException.class,
        () -> new ShockSpec(DAMAGE, NEGATIVE, INTERVAL, COST, false, Set.of()));
    assertThrows(
        IllegalArgumentException.class,
        () -> new ShockSpec(DAMAGE, RANGE, 0, COST, false, Set.of()));
  }

  @Test
  void theModesCannotBeChangedFromOutside() {
    ShockSpec spec = new ShockSpec(DAMAGE, RANGE, INTERVAL, COST, false, Set.of());

    assertThrows(
        UnsupportedOperationException.class, () -> spec.modes().add(DefenseMode.AGGRESSIVE));
  }
}
