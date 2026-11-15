package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.domain.behavior.HibernationReason;
import io.github.marcelofabianov.cubit.domain.robot.RobotJob;
import io.github.marcelofabianov.cubit.menu.WorkKeys;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.function.BiConsumer;

/** The base of the jobs, in Portuguese, with the keys of the English texts (answer 712). */
final class PortugueseWorkTexts {
  private static final String TAB = "Trabalho";
  private static final String NO_JOB = "Sem trabalho";
  private static final String RESUME = "Retomar";
  private static final String RESUME_DETAIL = "Acorda · ele dorme de novo se o motivo seguir";
  private static final String RESUME_ONLY_ASLEEP = "Só para um Cubit hibernando fora do Dock";
  private static final String UNLOAD_RESUME_FIRST = "Hibernando · use o Retomar antes";
  private static final String CARD_SLOT = "Card";
  private static final String CARD_NEEDS_TIER = "Pede o %s";
  private static final String NOTICE_FULL = "Estou cheio e não tenho onde descarregar";
  private static final String INVENTORY_FULL = "Inventário cheio";
  private static final String FISHER = "Pescador";
  private static final String FISHER_CARD = "Aprendizado de Máquina: Pescador";

  private PortugueseWorkTexts() {}

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
    add.accept(WorkKeys.job(RobotJob.FISHER), FISHER);
    add.accept(CubitItems.FISHER_CARD.get().getDescriptionId(), FISHER_CARD);
  }
}
