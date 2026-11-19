package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.domain.behavior.HibernationReason;
import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.menu.WorkKeys;
import io.github.zenndg.cubit.registry.CubitItems;
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
  private static final String NO_WATER = "Sem água na área";
  private static final String NOTICE_NO_WATER = "Não acho água para pescar aqui";
  private static final String KEEP_JUNK = "Guardar lixo";
  private static final String KEEP_JUNK_DETAIL = "Desligada · o lixo, como botas, é descartado";
  private static final String FISHER = "Pescador";
  private static final String FISHER_CARD = "Aprendizado de Máquina: Pescador";
  private static final String FARMER = "Fazendeiro";
  private static final String FARMER_CARD = "Aprendizado de Máquina: Fazendeiro";
  private static final String FERTILIZER_SLOT = "Fertilizante";
  private static final String UNLOAD_INTERVAL = "Descarregar a cada";
  private static final String UNLOAD_INTERVAL_DETAIL = "Descarrega depois disso, mesmo sem encher";
  private static final String UNLOAD_MINUTES = "%s min";
  private static final String ONLY_WHEN_FULL = "Só quando cheio";

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
    add.accept(WorkKeys.reason(HibernationReason.NO_WATER), NO_WATER);
    add.accept(WorkKeys.notice(HibernationReason.NO_WATER), NOTICE_NO_WATER);
    add.accept(WorkKeys.KEEP_JUNK, KEEP_JUNK);
    add.accept(WorkKeys.KEEP_JUNK_DETAIL, KEEP_JUNK_DETAIL);
    add.accept(WorkKeys.job(RobotJob.FISHER), FISHER);
    add.accept(CubitItems.FISHER_CARD.get().getDescriptionId(), FISHER_CARD);
    add.accept(WorkKeys.job(RobotJob.FARMER), FARMER);
    add.accept(CubitItems.FARMER_CARD.get().getDescriptionId(), FARMER_CARD);
    add.accept(WorkKeys.FERTILIZER_SLOT, FERTILIZER_SLOT);
    add.accept(WorkKeys.UNLOAD_INTERVAL, UNLOAD_INTERVAL);
    add.accept(WorkKeys.UNLOAD_INTERVAL_DETAIL, UNLOAD_INTERVAL_DETAIL);
    add.accept(WorkKeys.UNLOAD_MINUTES, UNLOAD_MINUTES);
    add.accept(WorkKeys.ONLY_WHEN_FULL, ONLY_WHEN_FULL);
  }
}
