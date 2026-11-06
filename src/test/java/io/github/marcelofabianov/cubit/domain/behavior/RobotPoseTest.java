package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RobotPoseTest {
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
  void theArmsStayUpWhileTheRobotShowsTheDamage() {
    assertTrue(RobotPose.raisesArms(RobotExpression.HURT));
    assertTrue(RobotPose.raisesArms(RobotExpression.DYING));
  }

  @Test
  void theArmsStayDownInAnyOtherExpression() {
    assertFalse(RobotPose.raisesArms(RobotExpression.NORMAL));
    assertFalse(RobotPose.raisesArms(RobotExpression.WORRIED));
    assertFalse(RobotPose.raisesArms(RobotExpression.OFF));
    assertFalse(RobotPose.raisesArms(RobotExpression.CHARGING));
  }

  @Test
  void theOtherExpressionsDoNotChangeThePose() {
    assertEquals(RobotPose.WALKING, RobotPose.of(RobotExpression.HURT, true));
    assertEquals(RobotPose.IDLE, RobotPose.of(RobotExpression.DYING, false));
  }
}
