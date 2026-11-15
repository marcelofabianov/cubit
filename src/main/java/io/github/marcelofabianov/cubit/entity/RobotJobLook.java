package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.robot.RobotJob;
import java.util.Optional;

/**
 * What the client knows of the job, only for the looks: the card in the slot and whether the Fisher
 * is fishing at its stand (answers 1197 and 1199). The rules stay on the server.
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

  public static boolean fishing(RobotEntity robot) {
    return robot.getEntityData().get(RobotSyncedData.FISHING);
  }
}
