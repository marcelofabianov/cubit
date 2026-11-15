package io.github.marcelofabianov.cubit.domain.tier;

import io.github.marcelofabianov.cubit.domain.robot.RobotJob;
import java.util.EnumSet;
import java.util.Set;

/**
 * The jobs a tier accepts, read from allowed_jobs in the config, and what the tier does with each
 * card, as the Fisher and the Farmer (answers 1145, 1163, 1185 and 1226).
 */
public record JobSpec(Set<RobotJob> allowed, FisherSpec fisher, FarmerSpec farmer) {
  /** No jobs, as the Base and a tier built without job numbers, which only tests need. */
  public static final JobSpec NONE = new JobSpec(Set.of());

  public JobSpec {
    allowed = allowed.isEmpty() ? Set.of() : Set.copyOf(EnumSet.copyOf(allowed));
  }

  /** The jobs alone, with no numbers for the cards, which only tests and the Base need. */
  public JobSpec(Set<RobotJob> allowed) {
    this(allowed, FisherSpec.NONE, FarmerSpec.NONE);
  }

  public boolean accepts(RobotJob job) {
    return allowed.contains(job);
  }

  /** Whether the tier takes any card, so the card slot is not locked. */
  public boolean unlocked() {
    return !allowed.isEmpty();
  }
}
