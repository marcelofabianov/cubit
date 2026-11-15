package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class RobotJobTest {
  private static final String FISHER_KEY = "fisher";
  private static final String MECHANIC_KEY = "mechanic";
  private static final String UNKNOWN_KEY = "artisan";

  /** The keys are a config contract (answers 1163 and 1172). */
  @Test
  void theKeysRoundTrip() {
    for (RobotJob job : RobotJob.values()) {
      assertEquals(Optional.of(job), RobotJob.fromKey(job.key()));
    }
    assertEquals(FISHER_KEY, RobotJob.FISHER.key());
    assertEquals(MECHANIC_KEY, RobotJob.MECHANIC.key());
  }

  @Test
  void anUnknownKeyIsNoJob() {
    assertEquals(Optional.empty(), RobotJob.fromKey(UNKNOWN_KEY));
  }
}
