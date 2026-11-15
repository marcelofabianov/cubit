package io.github.marcelofabianov.cubit.domain.tier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.robot.RobotJob;
import java.util.EnumSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class JobSpecTest {
  private static final JobSpec ADVANCED =
      new JobSpec(EnumSet.of(RobotJob.FISHER, RobotJob.FARMER, RobotJob.RANCHER));

  /** Each tier accepts its own jobs, as allowed_jobs says (answers 1145 and 1163). */
  @Test
  void aTierAcceptsOnlyItsJobs() {
    assertTrue(ADVANCED.accepts(RobotJob.FISHER));
    assertFalse(ADVANCED.accepts(RobotJob.MINER));
  }

  @Test
  void aTierWithoutJobsTakesNoCard() {
    assertFalse(JobSpec.NONE.unlocked());
    assertFalse(new JobSpec(Set.of()).accepts(RobotJob.FISHER));
    assertTrue(ADVANCED.unlocked());
  }
}
