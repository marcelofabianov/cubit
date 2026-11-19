package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.menu.DeathKeys;
import io.github.zenndg.cubit.menu.ScreenKeys;
import java.util.function.BiConsumer;

/** The death point, in Portuguese, with the keys of the English texts (answer 712). */
final class PortugueseDeathTexts {
  private static final String SWITCH = "Local de morte";
  private static final String SWITCH_DETAIL = "Mostra onde você morreu e guia até lá";
  private static final String FELL = "Você morreu em %s, %s, %s · %s";
  private static final String ARRIVED = "Foi aqui que você morreu";
  private static final String STUCK = "Não acho um caminho até lá";
  private static final String TAB_TITLE = "Mortes";
  private static final String EMPTY = "Nenhuma morte ainda";
  private static final String WHEN = "Quando";
  private static final String WHERE = "Onde e causa";
  private static final String DAY_TIME = "Dia %s, %s";
  private static final String COORDINATES = "%s, %s, %s";
  private static final String GUIDE = "Guiar";
  private static final String CANCEL = "Cancelar";
  private static final String DELETE = "Excluir";
  private static final String GUIDE_DETAIL = "O Cubit vai na frente e leva você até lá";
  private static final String CANCEL_DETAIL = "O Cubit para de guiar e segue você";
  private static final String DELETE_DETAIL = "Tira esta morte da lista";
  private static final String SWITCHED_OFF = "A chave Local de morte está desligada";
  private static final String DEATH_ELSEWHERE = "O Cubit só guia na Superfície";
  private static final String CUBIT_ELSEWHERE = "O Cubit precisa estar na Superfície";
  private static final String NOT_FOLLOWING = "Só o Cubit que segue você guia · use o Seguir antes";
  private static final String OVERWORLD = "Superfície";
  private static final String NETHER = "Nether";
  private static final String END = "O End";

  private PortugueseDeathTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(ScreenKeys.DEATH_POINT, SWITCH);
    add.accept(ScreenKeys.DEATH_POINT_DETAIL, SWITCH_DETAIL);
    add.accept(DeathKeys.NOTICE_FELL, FELL);
    add.accept(DeathKeys.NOTICE_ARRIVED, ARRIVED);
    add.accept(DeathKeys.NOTICE_STUCK, STUCK);
    add.accept(DeathKeys.TAB_TITLE, TAB_TITLE);
    add.accept(DeathKeys.EMPTY, EMPTY);
    add.accept(DeathKeys.WHEN, WHEN);
    add.accept(DeathKeys.WHERE, WHERE);
    add.accept(DeathKeys.DAY_TIME, DAY_TIME);
    add.accept(DeathKeys.COORDINATES, COORDINATES);
    add.accept(DeathKeys.GUIDE, GUIDE);
    add.accept(DeathKeys.CANCEL, CANCEL);
    add.accept(DeathKeys.DELETE, DELETE);
    add.accept(DeathKeys.GUIDE_DETAIL, GUIDE_DETAIL);
    add.accept(DeathKeys.CANCEL_DETAIL, CANCEL_DETAIL);
    add.accept(DeathKeys.DELETE_DETAIL, DELETE_DETAIL);
    add.accept(DeathKeys.SWITCHED_OFF, SWITCHED_OFF);
    add.accept(DeathKeys.DEATH_ELSEWHERE, DEATH_ELSEWHERE);
    add.accept(DeathKeys.CUBIT_ELSEWHERE, CUBIT_ELSEWHERE);
    add.accept(DeathKeys.NOT_FOLLOWING, NOT_FOLLOWING);
    add.accept(DeathKeys.OVERWORLD, OVERWORLD);
    add.accept(DeathKeys.NETHER, NETHER);
    add.accept(DeathKeys.END, END);
  }
}
