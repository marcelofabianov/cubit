package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.menu.TabletKeys;
import io.github.zenndg.cubit.menu.WorkKeys;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.function.BiConsumer;

/** The Cook, in English, the base language (answers 1314 to 1329). */
final class CookTexts {
  private static final String JOB = "Cook";
  private static final String CARD = "Machine Learning: Cook";
  private static final String ONLY_OVEN = "The Cook only marks a smoker or a furnace";

  private CookTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(WorkKeys.job(RobotJob.COOK), JOB);
    add.accept(CubitItems.COOK_CARD.get().getDescriptionId(), CARD);
    add.accept(TabletKeys.MARK_ONLY_OVEN, ONLY_OVEN);
  }
}
