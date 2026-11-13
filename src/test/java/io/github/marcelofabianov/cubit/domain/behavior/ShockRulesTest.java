package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.domain.tier.ShockSpec;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ShockRulesTest {
  private static final int DAMAGE = 2;
  private static final int RANGE = 3;
  private static final int INTERVAL = 60;
  private static final Energy COST = new Energy(2_000);
  private static final Energy CAPACITY = new Energy(600_000);
  private static final Battery CHARGED = new Battery(CAPACITY, new Energy(10_000));
  private static final Battery ALMOST_EMPTY = new Battery(CAPACITY, new Energy(1_999));
  private static final ShockSpec ONE_TARGET =
      new ShockSpec(DAMAGE, RANGE, INTERVAL, COST, false, Set.of(DefenseMode.RETALIATE));
  private static final ShockSpec EVERY_TARGET =
      new ShockSpec(DAMAGE, RANGE, INTERVAL, COST, true, Set.of(DefenseMode.RETALIATE));
  private static final long LAST_SHOCK = 100;
  private static final long TOO_SOON = LAST_SHOCK + INTERVAL - 1;
  private static final long READY = LAST_SHOCK + INTERVAL;
  private static final String NEAR = "near";
  private static final String MIDDLE = "middle";
  private static final String FAR = "far";

  @Test
  void anActiveRobotShocksWhenTheIntervalPassed() {
    assertTrue(
        ShockRules.ready(
            PowerState.ACTIVE, DefenseMode.RETALIATE, ONE_TARGET, CHARGED, LAST_SHOCK, READY));
    assertFalse(
        ShockRules.ready(
            PowerState.ACTIVE, DefenseMode.RETALIATE, ONE_TARGET, CHARGED, LAST_SHOCK, TOO_SOON));
  }

  @Test
  void onlyAnActiveRobotShocks() {
    for (PowerState state : PowerState.values()) {
      boolean ready =
          ShockRules.ready(state, DefenseMode.RETALIATE, ONE_TARGET, CHARGED, LAST_SHOCK, READY);
      assertEquals(state == PowerState.ACTIVE, ready);
    }
  }

  @Test
  void withoutChargeForTheCostThereIsNoShock() {
    assertFalse(
        ShockRules.ready(
            PowerState.ACTIVE, DefenseMode.RETALIATE, ONE_TARGET, ALMOST_EMPTY, LAST_SHOCK, READY));
  }

  @Test
  void peacefulOrAModeTheTierLacksNeverShocks() {
    assertFalse(
        ShockRules.ready(
            PowerState.ACTIVE, DefenseMode.PEACEFUL, ONE_TARGET, CHARGED, LAST_SHOCK, READY));
    assertFalse(
        ShockRules.ready(
            PowerState.ACTIVE, DefenseMode.AGGRESSIVE, ONE_TARGET, CHARGED, LAST_SHOCK, READY));
  }

  @Test
  void aTierWithoutShockNeverShocks() {
    assertFalse(
        ShockRules.ready(
            PowerState.ACTIVE, DefenseMode.RETALIATE, ShockSpec.NONE, CHARGED, LAST_SHOCK, READY));
  }

  @Test
  void oneTargetIsTheNearest() {
    assertEquals(List.of(NEAR), ShockRules.targets(ONE_TARGET, List.of(NEAR, MIDDLE, FAR)));
  }

  @Test
  void anAreaShockHitsEveryTarget() {
    assertEquals(
        List.of(NEAR, MIDDLE, FAR), ShockRules.targets(EVERY_TARGET, List.of(NEAR, MIDDLE, FAR)));
  }

  @Test
  void noTargetsGiveNoTargets() {
    assertEquals(List.of(), ShockRules.targets(ONE_TARGET, List.<String>of()));
  }

  @Test
  void aStrikeKeepsTheRobotEngagedUntilTheNextOneIsDue() {
    long until = ShockRules.engagedUntil(LAST_SHOCK, ONE_TARGET);

    assertTrue(ShockRules.engaged(until, READY));
    assertFalse(ShockRules.engaged(until, READY + 1));
    assertFalse(ShockRules.engaged(0, 0));
  }
}
