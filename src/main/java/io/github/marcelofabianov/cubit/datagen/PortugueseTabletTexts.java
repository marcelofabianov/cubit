package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.domain.robot.TabletApp;
import io.github.marcelofabianov.cubit.item.TabletItem;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.function.BiConsumer;

/** The Tablet, in Brazilian Portuguese (answers 1006 and 1026). */
final class PortugueseTabletTexts {
  private static final String NAME = "Tablet do Cubit";
  private static final String WITH_APP = "%s · %s";
  private static final String APP_OPEN = "Abrir";
  private static final String APP_TARGET = "Alvo";
  private static final String APP_DOCK = "Dock";
  private static final String TITLE = "Cubits";
  private static final String SEARCH = "Buscar";
  private static final String EMPTY = "Nenhum Cubit colocado";
  private static final String PLACE = "%s, %s, %s · %s";
  private static final String DISTANCE = "%s m";
  private static final String LAST_SEEN = "visto há %s min";
  private static final String CHOSEN = "Escolhido: %s";
  private static final String OVERWORLD = "Superfície";
  private static final String NETHER = "Nether";
  private static final String END = "End";
  private static final String BACK_TO_OPEN = "Cubit escolhido fora do Focus · de volta ao Abrir";
  private static final String NO_MOB = "Nenhum mob na mira";
  private static final String NOT_A_TARGET = "Isto não pode ser alvo do Focus";
  private static final String DOCK_ONLY_YOURS = "O app Dock marca um Dock seu";
  private static final String DOCK_NO_CUBIT = "Escolha antes um Cubit ao alcance";
  private static final String DOCK_MARKED = "Dock preferido de %s";
  private static final String DOCK_UNMARKED = "%s está sem Dock preferido";
  private static final String GO_TO_DOCK = "Ir para o Dock";
  private static final String GO_TO_DOCK_DETAIL = "Manda o Cubit hibernar no Dock";
  private static final String DOCK_SWITCHED_OFF = "O Cubit está desligado";
  private static final String DOCK_HIBERNATING = "O Cubit já está num Dock";
  private static final String DOCK_OUT_OF_REACH = "Nenhum Dock livre ao alcance";
  private static final String BACK = "Voltar";
  private static final String RENAME = "Renomear";
  private static final String RENAME_TITLE = "Renomear o Cubit";
  private static final String CONFIRM = "Confirmar";
  private static final String CANCEL = "Cancelar";
  private static final String TURN_ON = "Ligar";
  private static final String SWITCH_OFF = "Desligar";
  private static final String TURN_ON_DETAIL = "Liga o Cubit, e ele passa a seguir você";
  private static final String POWER_UNAVAILABLE = "Sem carga para ligar";
  private static final String PLACE_TITLE = "Lugar";
  private static final String COORDINATES = "%s, %s, %s";
  private static final String OUT_OF_REACH = "Fora de alcance · visto há %s min";
  private static final String OUT_OF_REACH_DETAIL = "O chunk deste Cubit não está carregado";
  private static final String TAB_RESOURCES = "Recursos";
  private static final String TAB_FILTERS = "Filtros";

  private PortugueseTabletTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(CubitItems.TABLET.get().getDescriptionId(), NAME);
    add.accept(TabletKeys.WITH_APP, WITH_APP);
    add.accept(TabletItem.appKey(TabletApp.OPEN), APP_OPEN);
    add.accept(TabletItem.appKey(TabletApp.TARGET), APP_TARGET);
    add.accept(TabletItem.appKey(TabletApp.DOCK), APP_DOCK);
    add.accept(TabletKeys.TITLE, TITLE);
    add.accept(TabletKeys.SEARCH, SEARCH);
    add.accept(TabletKeys.EMPTY, EMPTY);
    add.accept(TabletKeys.PLACE, PLACE);
    add.accept(TabletKeys.DISTANCE, DISTANCE);
    add.accept(TabletKeys.LAST_SEEN, LAST_SEEN);
    add.accept(TabletKeys.CHOSEN, CHOSEN);
    add.accept(TabletKeys.OVERWORLD, OVERWORLD);
    add.accept(TabletKeys.NETHER, NETHER);
    add.accept(TabletKeys.END, END);
    addScreen(add);
    addApps(add);
  }

  /** The screen of a Cubit on the Tablet (answers 1033 to 1037). */
  private static void addScreen(BiConsumer<String, String> add) {
    add.accept(TabletKeys.BACK, BACK);
    add.accept(TabletKeys.RENAME, RENAME);
    add.accept(TabletKeys.RENAME_TITLE, RENAME_TITLE);
    add.accept(TabletKeys.CONFIRM, CONFIRM);
    add.accept(TabletKeys.CANCEL, CANCEL);
    add.accept(TabletKeys.TURN_ON, TURN_ON);
    add.accept(TabletKeys.SWITCH_OFF, SWITCH_OFF);
    add.accept(TabletKeys.TURN_ON_DETAIL, TURN_ON_DETAIL);
    add.accept(TabletKeys.POWER_UNAVAILABLE, POWER_UNAVAILABLE);
    add.accept(TabletKeys.PLACE_TITLE, PLACE_TITLE);
    add.accept(TabletKeys.COORDINATES, COORDINATES);
    add.accept(TabletKeys.OUT_OF_REACH, OUT_OF_REACH);
    add.accept(TabletKeys.OUT_OF_REACH_DETAIL, OUT_OF_REACH_DETAIL);
    add.accept(TabletKeys.TAB_RESOURCES, TAB_RESOURCES);
    add.accept(TabletKeys.TAB_FILTERS, TAB_FILTERS);
  }

  /** The apps Target and Dock and Go to Dock (answers 1041 to 1047). */
  private static void addApps(BiConsumer<String, String> add) {
    add.accept(TabletKeys.BACK_TO_OPEN, BACK_TO_OPEN);
    add.accept(TabletKeys.NO_MOB, NO_MOB);
    add.accept(TabletKeys.NOT_A_TARGET, NOT_A_TARGET);
    add.accept(TabletKeys.DOCK_ONLY_YOURS, DOCK_ONLY_YOURS);
    add.accept(TabletKeys.DOCK_NO_CUBIT, DOCK_NO_CUBIT);
    add.accept(TabletKeys.DOCK_MARKED, DOCK_MARKED);
    add.accept(TabletKeys.DOCK_UNMARKED, DOCK_UNMARKED);
    add.accept(TabletKeys.GO_TO_DOCK, GO_TO_DOCK);
    add.accept(TabletKeys.GO_TO_DOCK_DETAIL, GO_TO_DOCK_DETAIL);
    add.accept(TabletKeys.DOCK_SWITCHED_OFF, DOCK_SWITCHED_OFF);
    add.accept(TabletKeys.DOCK_HIBERNATING, DOCK_HIBERNATING);
    add.accept(TabletKeys.DOCK_OUT_OF_REACH, DOCK_OUT_OF_REACH);
  }
}
