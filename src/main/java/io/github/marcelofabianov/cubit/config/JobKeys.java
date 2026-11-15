package io.github.marcelofabianov.cubit.config;

import com.mojang.logging.LogUtils;
import io.github.marcelofabianov.cubit.domain.robot.RobotJob;
import io.github.marcelofabianov.cubit.domain.tier.JobSpec;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.slf4j.Logger;

/**
 * The jobs one tier accepts. The key name and the job names are a contract, and an unknown name is
 * skipped and logged once, as in defense_modes (answers 1163 and 1172).
 */
final class JobKeys {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String KEY = "allowed_jobs";
  private static final String INVALID_JOB = "Ignoring an invalid name in allowed_jobs: {}";
  private static final String EMPTY_JOB = "";

  private final ModConfigSpec.ConfigValue<List<? extends String>> jobs;
  private final AtomicBoolean warned = new AtomicBoolean();

  JobKeys(ModConfigSpec.Builder builder, JobSpec defaults) {
    jobs =
        builder.defineListAllowEmpty(
            KEY, keysOf(defaults.allowed()), () -> EMPTY_JOB, String.class::isInstance);
  }

  JobSpec read() {
    Set<RobotJob> allowed = EnumSet.noneOf(RobotJob.class);
    for (String key : jobs.get()) {
      Optional<RobotJob> job = RobotJob.fromKey(key);
      job.ifPresent(allowed::add);
      if (job.isEmpty() && warned.compareAndSet(false, true)) {
        LOGGER.warn(INVALID_JOB, key);
      }
    }
    return new JobSpec(allowed);
  }

  /** In the order of the jobs, so the file reads as the table of jobs per tier. */
  private static List<String> keysOf(Set<RobotJob> allowed) {
    return allowed.isEmpty()
        ? List.of()
        : EnumSet.copyOf(allowed).stream().map(RobotJob::key).toList();
  }
}
