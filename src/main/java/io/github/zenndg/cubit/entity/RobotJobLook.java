package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.domain.robot.RobotJob;
import java.util.Optional;

/**
 * What the client knows of the job, only for the looks: the card in the slot and whether the tool
 * of the job is in the claw (answers 1197, 1199 and 1229). The rules stay on the server.
 */
public final class RobotJobLook {
  private RobotJobLook() {}

  public static Optional<RobotJob> job(RobotEntity robot) {
    int shown = robot.getEntityData().get(RobotSyncedData.JOB) - 1;
    RobotJob[] jobs = RobotJob.values();
    return shown >= 0 && shown < jobs.length ? Optional.of(jobs[shown]) : Optional.empty();
  }

  public static boolean fisher(RobotEntity robot) {
    return job(robot).filter(RobotJob.FISHER::equals).isPresent();
  }

  public static boolean farmer(RobotEntity robot) {
    return job(robot).filter(RobotJob.FARMER::equals).isPresent();
  }

  public static boolean organizer(RobotEntity robot) {
    return job(robot).filter(RobotJob.ORGANIZER::equals).isPresent();
  }

  public static boolean toolInClaw(RobotEntity robot) {
    return robot.getEntityData().get(RobotSyncedData.TOOL_IN_CLAW);
  }

  /** Set on the server: the tool of the job goes to the claw while the Cubit works. */
  static void showTool(RobotEntity robot, boolean inClaw) {
    robot.getEntityData().set(RobotSyncedData.TOOL_IN_CLAW, inClaw);
  }
}
