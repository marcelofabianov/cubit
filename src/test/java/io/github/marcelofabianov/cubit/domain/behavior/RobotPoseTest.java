package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RobotPoseTest {
  private static final int ARMS_RAISED_TICKS = 40;

  @Test
  void aRobotWithTheScreenOffIsPoweredDownEvenWhenPushed() {
    assertEquals(RobotPose.POWERED_DOWN, RobotPose.of(RobotExpression.OFF, false));
    assertEquals(RobotPose.POWERED_DOWN, RobotPose.of(RobotExpression.OFF, true));
  }

  @Test
  void aHibernatingRobotIsPoweredDown() {
    assertEquals(RobotPose.POWERED_DOWN, RobotPose.of(RobotExpression.CHARGING, false));
    assertEquals(RobotPose.POWERED_DOWN, RobotPose.of(RobotExpression.CHARGING, true));
  }

  @Test
  void anAwakeRobotWalksOnlyWhileMoving() {
    assertEquals(RobotPose.WALKING, RobotPose.of(RobotExpression.NORMAL, true));
    assertEquals(RobotPose.IDLE, RobotPose.of(RobotExpression.NORMAL, false));
  }

  @Test
  void theArmsGoUpRightAfterAHit() {
    assertTrue(RobotPose.raisesArms(RobotExpression.HURT, true));
    assertTrue(RobotPose.raisesArms(RobotExpression.DYING, true));
  }

  @Test
  void theArmsGoDownWhileTheDamageStillShows() {
    assertFalse(RobotPose.raisesArms(RobotExpression.HURT, false));
    assertFalse(RobotPose.raisesArms(RobotExpression.DYING, false));
  }

  @Test
  void theArmsStayUpForAboutTwoSeconds() {
    assertEquals(ARMS_RAISED_TICKS, RobotPose.ARMS_RAISED_TICKS);
  }

  @Test
  void theArmsStayDownInAnyOtherExpression() {
    assertFalse(RobotPose.raisesArms(RobotExpression.NORMAL, true));
    assertFalse(RobotPose.raisesArms(RobotExpression.WORRIED, true));
    assertFalse(RobotPose.raisesArms(RobotExpression.OFF, true));
    assertFalse(RobotPose.raisesArms(RobotExpression.CHARGING, true));
  }

  @Test
  void theOtherExpressionsDoNotChangeThePose() {
    assertEquals(RobotPose.WALKING, RobotPose.of(RobotExpression.HURT, true));
    assertEquals(RobotPose.IDLE, RobotPose.of(RobotExpression.DYING, false));
  }
}
