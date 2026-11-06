package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class ExpressionRulesTest {
  private static final int HOLD_TICKS = 3;
  private static final Energy CAPACITY = new Energy(1_000);
  private static final Energy AT_LIMIT = new Energy(100);
  private static final Energy ABOVE_LIMIT = new Energy(101);
  private static final Percentage CRITICAL = new Percentage(10);

  private static final RobotSituation ALL =
      new RobotSituation(true, true, true, true, true, true, true);
  private static final float MAX_HEALTH = 25f;
  private static final float AT_DYING_LIMIT = 2.5f;
  private static final float ABOVE_DYING_LIMIT = 2.6f;

  private static RobotExpression active(RobotSituation situation) {
    return ExpressionRules.decide(PowerState.ACTIVE, situation);
  }

  @Test
  void anInactiveRobotHasTheScreenOffWhateverElseHolds() {
    assertEquals(RobotExpression.OFF, ExpressionRules.decide(PowerState.INACTIVE_EXHAUSTED, ALL));
    assertEquals(
        RobotExpression.OFF, ExpressionRules.decide(PowerState.INACTIVE_SWITCHED_OFF, ALL));
  }

  @Test
  void aHibernatingRobotIsAlwaysCharging() {
    assertEquals(RobotExpression.CHARGING, ExpressionRules.decide(PowerState.HIBERNATING, ALL));
  }

  @Test
  void anActiveRobotFollowsThePriorityOrder() {
    assertEquals(RobotExpression.DYING, active(ALL));
    assertEquals(
        RobotExpression.HURT,
        active(new RobotSituation(false, true, true, true, true, true, true)));
    assertEquals(
        RobotExpression.WORRIED,
        active(new RobotSituation(false, false, true, true, true, true, true)));
    assertEquals(
        RobotExpression.ALERT,
        active(new RobotSituation(false, false, false, true, true, true, true)));
    assertEquals(
        RobotExpression.SURPRISED,
        active(new RobotSituation(false, false, false, false, true, true, true)));
    assertEquals(
        RobotExpression.SLEEPY,
        active(new RobotSituation(false, false, false, false, false, true, true)));
    assertEquals(
        RobotExpression.HAPPY,
        active(new RobotSituation(false, false, false, false, false, false, true)));
    assertEquals(
        RobotExpression.NORMAL,
        active(new RobotSituation(false, false, false, false, false, false, false)));
  }

  @Test
  void aRobotAtTheHealthLimitIsDyingAndAboveItIsNot() {
    assertTrue(ExpressionRules.isDying(AT_DYING_LIMIT, MAX_HEALTH, CRITICAL));
    assertFalse(ExpressionRules.isDying(ABOVE_DYING_LIMIT, MAX_HEALTH, CRITICAL));
  }

  @Test
  void theDamageExpressionsDoNotBeep() {
    assertFalse(ExpressionRules.beeps(RobotExpression.NORMAL, RobotExpression.HURT));
    assertFalse(ExpressionRules.beeps(RobotExpression.HURT, RobotExpression.DYING));
    assertFalse(ExpressionRules.beeps(RobotExpression.HURT, RobotExpression.NORMAL));
    assertFalse(ExpressionRules.beeps(RobotExpression.NORMAL, RobotExpression.WORRIED));
    assertFalse(ExpressionRules.beeps(RobotExpression.WORRIED, RobotExpression.NORMAL));
    assertFalse(ExpressionRules.beeps(RobotExpression.DYING, RobotExpression.NORMAL));
  }

  @Test
  void theBeepPlaysOnChangesToExpressionsWithoutTheirOwnSound() {
    assertTrue(ExpressionRules.beeps(RobotExpression.NORMAL, RobotExpression.HAPPY));
    assertTrue(ExpressionRules.beeps(RobotExpression.HAPPY, RobotExpression.NORMAL));
    assertTrue(ExpressionRules.beeps(RobotExpression.NORMAL, RobotExpression.SLEEPY));
    assertTrue(ExpressionRules.beeps(RobotExpression.ALERT, RobotExpression.NORMAL));
  }

  @Test
  void expressionsWithTheirOwnSoundDoNotBeep() {
    assertFalse(ExpressionRules.beeps(RobotExpression.NORMAL, RobotExpression.ALERT));
    assertFalse(ExpressionRules.beeps(RobotExpression.NORMAL, RobotExpression.SURPRISED));
    assertFalse(ExpressionRules.beeps(RobotExpression.NORMAL, RobotExpression.CHARGING));
    assertFalse(ExpressionRules.beeps(RobotExpression.NORMAL, RobotExpression.OFF));
  }

  @Test
  void turningOnPlacingAndLoadingDoNotBeep() {
    assertFalse(ExpressionRules.beeps(RobotExpression.OFF, RobotExpression.NORMAL));
    assertFalse(ExpressionRules.beeps(RobotExpression.OFF, RobotExpression.HAPPY));
    assertFalse(ExpressionRules.beeps(RobotExpression.CHARGING, RobotExpression.NORMAL));
  }

  @Test
  void anUnchangedExpressionDoesNotBeep() {
    assertFalse(ExpressionRules.beeps(RobotExpression.NORMAL, RobotExpression.NORMAL));
  }

  @Test
  void theExpressionTimerRunsForTheHoldTimeAndStops() {
    ExpressionTimer timer = ExpressionTimer.started(HOLD_TICKS);
    for (int tick = 0; tick < HOLD_TICKS; tick++) {
      assertTrue(timer.isRunning());
      timer = timer.tick();
    }
    assertFalse(timer.isRunning());
    assertFalse(timer.tick().isRunning());
  }

  @Test
  void aBatteryAtTheLimitIsCriticalAndAboveItIsNot() {
    assertTrue(new Battery(CAPACITY, AT_LIMIT).hasAtMost(CRITICAL));
    assertFalse(new Battery(CAPACITY, ABOVE_LIMIT).hasAtMost(CRITICAL));
  }
}
