package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.domain.behavior.RobotPose;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

/** Plays the animation of the current pose, named after it in the animation file of the robot. */
final class RobotAnimations {
  private static final String CONTROLLER = "pose";
  private static final int TRANSITION_TICKS = 5;
  private static final String ARMS_CONTROLLER = "arms";
  private static final String ARMS_RAISED = "arms_raised";
  private static final RawAnimation RAISED_ARMS = RawAnimation.begin().thenPlayAndHold(ARMS_RAISED);
  private static final String FISHING = "fishing";
  private static final RawAnimation FISHING_ARM = RawAnimation.begin().thenPlayAndHold(FISHING);
  private static final Map<RobotPose, RawAnimation> ANIMATIONS = new EnumMap<>(RobotPose.class);

  static {
    for (RobotPose pose : RobotPose.values()) {
      String name = pose.name().toLowerCase(Locale.ROOT);
      ANIMATIONS.put(
          pose,
          pose == RobotPose.POWERED_DOWN
              ? RawAnimation.begin().thenPlayAndHold(name)
              : RawAnimation.begin().thenLoop(name));
    }
  }

  private RobotAnimations() {}

  /** The arms come last, because the last controller decides a bone that two of them move. */
  static void register(RobotEntity robot, AnimatableManager.ControllerRegistrar controllers) {
    controllers.add(
        new AnimationController<>(
            robot,
            CONTROLLER,
            TRANSITION_TICKS,
            state ->
                state.setAndContinue(
                    ANIMATIONS.get(RobotPose.of(RobotFace.expression(robot), state.isMoving())))));
    controllers.add(
        new AnimationController<>(
            robot, ARMS_CONTROLLER, TRANSITION_TICKS, state -> arms(robot, state)));
  }

  /**
   * The raised arms of a feeling come first, and then the arm with the tool of the job (answers
   * 1199 and 1229).
   */
  private static PlayState arms(RobotEntity robot, AnimationState<RobotEntity> state) {
    if (RobotFace.armsRaised(robot)) {
      return state.setAndContinue(RAISED_ARMS);
    }
    return RobotJobLook.toolInClaw(robot) ? state.setAndContinue(FISHING_ARM) : PlayState.STOP;
  }
}
