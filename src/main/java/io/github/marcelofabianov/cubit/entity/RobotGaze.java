package io.github.marcelofabianov.cubit.entity;

import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

/**
 * Where the head of the robot turns: to the target of its shock until it is eliminated and to its
 * last spot for a while after, or else to what the goal looks at (answers 987 to 995). The body
 * follows the head on its own only while the robot stands still, as in the game, so walking never
 * turns it away from its owner.
 */
final class RobotGaze {
  private static final float LOOK_YAW_SPEED = 10f;

  private RobotGaze() {}

  static void look(RobotEntity robot, Entity otherwise) {
    Optional<Vec3> spot = robot.defense().lookSpot();
    if (spot.isPresent()) {
      Vec3 at = spot.get();
      robot.getLookControl().setLookAt(at.x, at.y, at.z, LOOK_YAW_SPEED, robot.getMaxHeadXRot());
    } else if (otherwise != null) {
      robot.getLookControl().setLookAt(otherwise, LOOK_YAW_SPEED, robot.getMaxHeadXRot());
    }
  }

  static boolean atShockTarget(RobotEntity robot) {
    return robot.defense().lookSpot().isPresent();
  }
}
