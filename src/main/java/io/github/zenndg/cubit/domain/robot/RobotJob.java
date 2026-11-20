package io.github.zenndg.cubit.domain.robot;

import java.util.Locale;
import java.util.Optional;

/**
 * The jobs a card of Machine Learning gives the Cubit, one per card (answers 192, 1145 and 1146).
 * The keys are a config contract, in the lists of the jobs each tier accepts (answer 1163).
 */
public enum RobotJob {
  FISHER,
  FARMER,
  RANCHER,
  ORGANIZER,
  COOK,
  DEFENDER,
  MINER,
  BUILDER,
  MECHANIC,
  ALCHEMIST;

  public String key() {
    return name().toLowerCase(Locale.ROOT);
  }

  public static Optional<RobotJob> fromKey(String key) {
    for (RobotJob job : values()) {
      if (job.key().equals(key)) {
        return Optional.of(job);
      }
    }
    return Optional.empty();
  }
}
