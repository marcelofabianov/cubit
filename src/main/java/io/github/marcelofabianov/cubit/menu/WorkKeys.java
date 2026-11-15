package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.behavior.HibernationReason;
import io.github.marcelofabianov.cubit.domain.robot.RobotJob;

/**
 * The translation keys of the base of the jobs: the Work tab, the states, Resume and the text of
 * the Cubit when it hibernates away from the Dock (answers 1167, 1168, 1172 and 1174). They are a
 * contract with whoever translates the mod.
 */
public final class WorkKeys {
  private static final String PREFIX = "gui.cubit.work.";
  private static final String JOB = PREFIX + "job.%s";
  private static final String REASON = PREFIX + "reason.%s";

  public static final String TAB = PREFIX + "tab";
  public static final String NO_JOB = PREFIX + "no_job";
  public static final String RESUME = PREFIX + "resume";
  public static final String RESUME_DETAIL = PREFIX + "resume_detail";
  public static final String RESUME_ONLY_ASLEEP = PREFIX + "resume_only_asleep";
  public static final String UNLOAD_RESUME_FIRST = PREFIX + "unload_resume_first";
  public static final String CARD_SLOT = PREFIX + "card_slot";
  public static final String CARD_NEEDS_TIER = PREFIX + "card_needs_tier";
  public static final String NOTICE_FULL = "notice.cubit.work_inventory_full";

  private WorkKeys() {}

  /** Why the Cubit hibernates, as `Inventory full` / `Inventário cheio`. */
  public static String reason(HibernationReason reason) {
    return REASON.formatted(reason.key());
  }

  /** The name of a job, as `Fisher` / `Pescador`. */
  public static String job(RobotJob job) {
    return JOB.formatted(job.key());
  }
}
