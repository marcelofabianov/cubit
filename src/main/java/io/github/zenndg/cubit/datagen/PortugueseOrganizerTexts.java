package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.domain.robot.OrganizerMode;
import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.domain.robot.TabletApp;
import io.github.zenndg.cubit.item.TabletItem;
import io.github.zenndg.cubit.menu.TabletKeys;
import io.github.zenndg.cubit.menu.WorkKeys;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.function.BiConsumer;

/** The Organizer, its Mark app and its Supply tab, in Portuguese, with the English keys. */
final class PortugueseOrganizerTexts {
  private static final String JOB = "Organizador";
  private static final String CARD = "Aprendizado de Máquina: Organizador";
  private static final String APP = "Marcar";
  private static final String ONLY_INVENTORY = "Marcar marca um bloco com inventário";
  private static final String MARKED = "Bloco marcado para %s · até %s · Shift desmarca";
  private static final String UNMARKED = "Bloco desmarcado para %s";
  private static final String NONE = "%s não tem bloco marcado";
  private static final String FULL = "%s já tem %s marcas · Shift e clique desmarca";
  private static final String NUMBER = "Marca %s";
  private static final String NO_ROOM = "Não há lugar para alguns itens";
  private static final String MODE = "Modo";
  private static final String MODE_DETAIL = "Organizar arruma a base · Abastecer enche as marcas";
  private static final String ORGANIZE = "Organizar";
  private static final String SUPPLY = "Abastecer";
  private static final String SUPPLY_TITLE = "Abastecer · %s de %s";
  private static final String SUPPLY_EMPTY = "Marque os destinos com o app Marcar";
  private static final String CHOOSE = "Clique com um item para escolher";
  private static final String CLEAR = " · mão vazia limpa";
  private static final String ITEM_DETAIL = CHOOSE + CLEAR;
  private static final String AMOUNT_DETAIL = "Até esta quantidade · Shift anda de 8 em 8";

  private PortugueseOrganizerTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(WorkKeys.job(RobotJob.ORGANIZER), JOB);
    add.accept(CubitItems.ORGANIZER_CARD.get().getDescriptionId(), CARD);
    add.accept(TabletItem.appKey(TabletApp.MARK), APP);
    add.accept(TabletKeys.MARK_ONLY_INVENTORY, ONLY_INVENTORY);
    add.accept(TabletKeys.MARK_MARKED, MARKED);
    add.accept(TabletKeys.MARK_UNMARKED, UNMARKED);
    add.accept(TabletKeys.MARK_NONE, NONE);
    add.accept(TabletKeys.MARK_FULL, FULL);
    add.accept(TabletKeys.MARK_NUMBER, NUMBER);
    add.accept(WorkKeys.ORGANIZER_NO_ROOM, NO_ROOM);
    add.accept(WorkKeys.ORGANIZER_MODE, MODE);
    add.accept(WorkKeys.ORGANIZER_MODE_DETAIL, MODE_DETAIL);
    add.accept(WorkKeys.organizerMode(OrganizerMode.ORGANIZE.key()), ORGANIZE);
    add.accept(WorkKeys.organizerMode(OrganizerMode.SUPPLY.key()), SUPPLY);
    add.accept(WorkKeys.SUPPLY_TAB, SUPPLY);
    add.accept(WorkKeys.SUPPLY_TITLE, SUPPLY_TITLE);
    add.accept(WorkKeys.SUPPLY_EMPTY, SUPPLY_EMPTY);
    add.accept(WorkKeys.SUPPLY_ITEM_DETAIL, ITEM_DETAIL);
    add.accept(WorkKeys.SUPPLY_AMOUNT_DETAIL, AMOUNT_DETAIL);
  }
}
