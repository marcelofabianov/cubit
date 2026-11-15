package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.domain.behavior.HibernationReason;
import io.github.marcelofabianov.cubit.domain.robot.RobotJob;
import io.github.marcelofabianov.cubit.menu.WorkKeys;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.function.BiConsumer;

/** The base of the jobs, in English, the base language (answers 1167, 1168, 1172 and 1174). */
final class WorkTexts {
  private static final String TAB = "Work";
  private static final String NO_JOB = "No job";
  private static final String RESUME = "Resume";
  private static final String RESUME_DETAIL = "Wakes it · it sleeps again if the reason stays";
  private static final String RESUME_ONLY_ASLEEP = "Only while it hibernates away from a Dock";
  private static final String UNLOAD_RESUME_FIRST = "Hibernating · use Resume first";
  private static final String CARD_SLOT = "Card";
  private static final String CARD_NEEDS_TIER = "Needs %s";
  private static final String NOTICE_FULL = "I'm full and have nowhere to unload";
  private static final String INVENTORY_FULL = "Inventory full";
  private static final String NO_WATER = "No water in the area";
  private static final String NOTICE_NO_WATER = "I can't find water to fish here";
  private static final String KEEP_JUNK = "Keep junk";
  private static final String KEEP_JUNK_DETAIL = "Off · junk such as boots is thrown away";
  private static final String FISHER = "Fisher";
  private static final String FISHER_CARD = "Machine Learning: Fisher";

  private WorkTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(WorkKeys.TAB, TAB);
    add.accept(WorkKeys.NO_JOB, NO_JOB);
    add.accept(WorkKeys.RESUME, RESUME);
    add.accept(WorkKeys.RESUME_DETAIL, RESUME_DETAIL);
    add.accept(WorkKeys.RESUME_ONLY_ASLEEP, RESUME_ONLY_ASLEEP);
    add.accept(WorkKeys.UNLOAD_RESUME_FIRST, UNLOAD_RESUME_FIRST);
    add.accept(WorkKeys.CARD_SLOT, CARD_SLOT);
    add.accept(WorkKeys.CARD_NEEDS_TIER, CARD_NEEDS_TIER);
    add.accept(WorkKeys.NOTICE_FULL, NOTICE_FULL);
    add.accept(WorkKeys.reason(HibernationReason.INVENTORY_FULL), INVENTORY_FULL);
    add.accept(WorkKeys.reason(HibernationReason.NO_WATER), NO_WATER);
    add.accept(WorkKeys.notice(HibernationReason.NO_WATER), NOTICE_NO_WATER);
    add.accept(WorkKeys.KEEP_JUNK, KEEP_JUNK);
    add.accept(WorkKeys.KEEP_JUNK_DETAIL, KEEP_JUNK_DETAIL);
    add.accept(WorkKeys.job(RobotJob.FISHER), FISHER);
    add.accept(CubitItems.FISHER_CARD.get().getDescriptionId(), FISHER_CARD);
  }
}
