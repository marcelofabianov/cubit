package io.github.zenndg.cubit.menu;

import io.github.zenndg.cubit.domain.behavior.HibernationReason;
import io.github.zenndg.cubit.domain.robot.RobotJob;

/**
 * The translation keys of the base of the jobs: the Work tab, the states, Resume and the text of
 * the Cubit when it hibernates away from the Dock, and Keep junk of the Fisher (answers 1167, 1168,
 * 1172, 1174, 1191 and 1195). They are a contract with whoever translates the mod.
 */
public final class WorkKeys {
  private static final String PREFIX = "gui.cubit.work.";
  private static final String MODE_PREFIX = PREFIX + "organizer_mode.";
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
  public static final String KEEP_JUNK = PREFIX + "keep_junk";
  public static final String KEEP_JUNK_DETAIL = PREFIX + "keep_junk_detail";
  public static final String FERTILIZER_SLOT = PREFIX + "fertilizer_slot";
  public static final String UNLOAD_INTERVAL = PREFIX + "unload_interval";
  public static final String UNLOAD_INTERVAL_DETAIL = PREFIX + "unload_interval_detail";
  public static final String UNLOAD_MINUTES = PREFIX + "unload_minutes";
  public static final String ONLY_WHEN_FULL = PREFIX + "only_when_full";
  public static final String ORGANIZER_NO_ROOM = PREFIX + "organizer_no_room";
  public static final String ORGANIZER_MODE = PREFIX + "organizer_mode";
  public static final String ORGANIZER_MODE_DETAIL = PREFIX + "organizer_mode_detail";
  public static final String SUPPLY_TAB = PREFIX + "supply_tab";
  public static final String SUPPLY_TITLE = PREFIX + "supply_title";
  public static final String SUPPLY_EMPTY = PREFIX + "supply_empty";
  public static final String SUPPLY_ITEM_DETAIL = PREFIX + "supply_item_detail";
  public static final String SUPPLY_AMOUNT_DETAIL = PREFIX + "supply_amount_detail";
  private static final String NOTICE = "notice.cubit.work_%s";
  public static final String NOTICE_FULL = NOTICE.formatted(HibernationReason.INVENTORY_FULL.key());

  private WorkKeys() {}

  /** Why the Cubit hibernates, as `Inventory full` / `Inventário cheio`. */
  public static String reason(HibernationReason reason) {
    return REASON.formatted(reason.key());
  }

  /** The text of the Cubit when it hibernates for a reason, as the full inventory (1174, 1195). */
  public static String notice(HibernationReason reason) {
    return NOTICE.formatted(reason.key());
  }

  /** The name of a job, as `Fisher` / `Pescador`. */
  public static String organizerMode(String key) {
    return MODE_PREFIX + key;
  }

  public static String job(RobotJob job) {
    return JOB.formatted(job.key());
  }
}
