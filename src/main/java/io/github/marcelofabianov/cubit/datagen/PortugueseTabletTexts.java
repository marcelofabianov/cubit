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
  private static final String APP_AREA = "Área";
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
  private static final String AREA_PICK_CUBIT = "Clique com o botão direito num Cubit seu primeiro";
  private static final String AREA_ONLY_YOURS = "O app Área marca a área de um Cubit seu";
  private static final String AREA_LOCKED = "%s não tem modo área no tier dele";
  private static final String AREA_FIRST_CORNER = "Área · %s · clique no primeiro canto";
  private static final String AREA_FIRST_HINT = "Área · clique no primeiro canto";
  private static final String AREA_SECOND_CORNER = "Primeiro canto %s %s %s · clique no outro";
  private static final String AREA_SECOND_HINT = "Área · clique no outro canto";
  private static final String AREA_TOO_BIG = "%s × %s × %s é grande · até %s de lado e %s de alto";
  private static final String AREA_OTHER_DIMENSION = "A área precisa estar na dimensão de %s";
  private static final String AREA_OUT_OF_REACH = "Longe demais de %s · até %s blocos";
  private static final String AREA_SET = "%s trabalha em %s × %s × %s";
  private static final String AREA_CANCELLED = "Marcação da área cancelada";
  private static final String AREA_CUBIT_GONE = "O Cubit sumiu · clique num Cubit de novo";
  private static final String AREA_SIZE = "%s × %s × %s";
  private static final String AREA_LINE = "Área";
  private static final String AREA_NONE = "Nenhuma · marque com o app Área";
  private static final String AREA_SUMMARY = "%s × %s × %s · de %s %s %s a %s %s %s";
  private static final String FOLLOW = "Seguir";
  private static final String FOLLOW_DETAIL = "Segue você · o que seguia antes desliga";
  private static final String FOLLOW_ALREADY = "Já está seguindo você";
  private static final String FOLLOW_NO_CHARGE = "Sem carga para seguir você";
  private static final String FOLLOW_OTHER_DIMENSION = "Em outra dimensão";
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
    add.accept(TabletItem.appKey(TabletApp.AREA), APP_AREA);
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
    addFollow(add);
    addArea(add);
  }

  /** The Area app (answers 1051 to 1060). */
  private static void addArea(BiConsumer<String, String> add) {
    add.accept(TabletKeys.AREA_PICK_CUBIT, AREA_PICK_CUBIT);
    add.accept(TabletKeys.AREA_ONLY_YOURS, AREA_ONLY_YOURS);
    add.accept(TabletKeys.AREA_LOCKED, AREA_LOCKED);
    add.accept(TabletKeys.AREA_FIRST_CORNER, AREA_FIRST_CORNER);
    add.accept(TabletKeys.AREA_FIRST_HINT, AREA_FIRST_HINT);
    add.accept(TabletKeys.AREA_SECOND_CORNER, AREA_SECOND_CORNER);
    add.accept(TabletKeys.AREA_SECOND_HINT, AREA_SECOND_HINT);
    add.accept(TabletKeys.AREA_TOO_BIG, AREA_TOO_BIG);
    add.accept(TabletKeys.AREA_OTHER_DIMENSION, AREA_OTHER_DIMENSION);
    add.accept(TabletKeys.AREA_OUT_OF_REACH, AREA_OUT_OF_REACH);
    add.accept(TabletKeys.AREA_SET, AREA_SET);
    add.accept(TabletKeys.AREA_CANCELLED, AREA_CANCELLED);
    add.accept(TabletKeys.AREA_CUBIT_GONE, AREA_CUBIT_GONE);
    add.accept(TabletKeys.AREA_SIZE, AREA_SIZE);
    add.accept(TabletKeys.AREA_LINE, AREA_LINE);
    add.accept(TabletKeys.AREA_NONE, AREA_NONE);
    add.accept(TabletKeys.AREA_SUMMARY, AREA_SUMMARY);
  }

  /** `Follow` of the header (answers 1073 to 1075). */
  private static void addFollow(BiConsumer<String, String> add) {
    add.accept(TabletKeys.FOLLOW, FOLLOW);
    add.accept(TabletKeys.FOLLOW_DETAIL, FOLLOW_DETAIL);
    add.accept(TabletKeys.FOLLOW_ALREADY, FOLLOW_ALREADY);
    add.accept(TabletKeys.FOLLOW_NO_CHARGE, FOLLOW_NO_CHARGE);
    add.accept(TabletKeys.FOLLOW_OTHER_DIMENSION, FOLLOW_OTHER_DIMENSION);
  }
}
