package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.menu.TabletKeys;
import io.github.zenndg.cubit.menu.WorkKeys;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.function.BiConsumer;

/** The Cook, in Portuguese, with the English keys. */
final class PortugueseCookTexts {
  private static final String JOB = "Cozinheiro";
  private static final String CARD = "Aprendizado de Máquina: Cozinheiro";
  private static final String ONLY_OVEN = "O Cozinheiro só marca fumeiro e fornalha";

  private PortugueseCookTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(WorkKeys.job(RobotJob.COOK), JOB);
    add.accept(CubitItems.COOK_CARD.get().getDescriptionId(), CARD);
    add.accept(TabletKeys.MARK_ONLY_OVEN, ONLY_OVEN);
  }
}
