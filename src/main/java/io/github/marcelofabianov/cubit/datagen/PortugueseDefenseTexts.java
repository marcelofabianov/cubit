package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.menu.DefenseKeys;
import io.github.marcelofabianov.cubit.registry.CubitDamageTypes;
import io.github.marcelofabianov.cubit.registry.CubitSounds;
import java.util.function.BiConsumer;

/** The defense tab, the shock and its sounds, in Portuguese (answers 733 and 833). */
final class PortugueseDefenseTexts {
  private static final String TAB = "Defesa";
  private static final String MODES = "Modos";
  private static final String PEACEFUL = "Pacífico";
  private static final String PEACEFUL_DETAIL = "Nunca ataca";
  private static final String RETALIATE = "Contra-atacar";
  private static final String RETALIATE_DETAIL = "Dá choque em quem ferir você ou o Cubit";
  private static final String AGGRESSIVE = "Agressivo";
  private static final String AGGRESSIVE_DETAIL = "Dá choque em mobs hostis no alcance";
  private static final String UNLOCKS_AT = "Libera no %s";
  private static final String SHOCK = "Choque";
  private static final String DAMAGE = "Dano";
  private static final String RANGE = "Alcance";
  private static final String RANGE_VALUE = "%s blocos";
  private static final String INTERVAL = "Intervalo";
  private static final String INTERVAL_VALUE = "A cada %s s";
  private static final String COST = "Custo";
  private static final String COST_VALUE = "%s FE por choque";
  private static final String TARGETS = "Alvos";
  private static final String NEAREST = "O mais próximo";
  private static final String ALL_IN_RANGE = "Todos no alcance";
  private static final String NO_SHOCK = "Nenhum tier tem choque";
  private static final String DEATH = "%1$s levou um choque de %2$s";
  private static final String SHOCK_SUBTITLE = "Cubit dá um choque";
  private static final String AGGRESSIVE_SUBTITLE = "Cubit fica agressivo";

  private PortugueseDefenseTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(DefenseKeys.TAB, TAB);
    add.accept(DefenseKeys.MODES, MODES);
    add.accept(DefenseKeys.mode(DefenseMode.PEACEFUL), PEACEFUL);
    add.accept(DefenseKeys.detail(DefenseMode.PEACEFUL), PEACEFUL_DETAIL);
    add.accept(DefenseKeys.mode(DefenseMode.RETALIATE), RETALIATE);
    add.accept(DefenseKeys.detail(DefenseMode.RETALIATE), RETALIATE_DETAIL);
    add.accept(DefenseKeys.mode(DefenseMode.AGGRESSIVE), AGGRESSIVE);
    add.accept(DefenseKeys.detail(DefenseMode.AGGRESSIVE), AGGRESSIVE_DETAIL);
    add.accept(DefenseKeys.UNLOCKS_AT, UNLOCKS_AT);
    add.accept(DefenseKeys.SHOCK, SHOCK);
    add.accept(DefenseKeys.DAMAGE, DAMAGE);
    add.accept(DefenseKeys.RANGE, RANGE);
    add.accept(DefenseKeys.RANGE_VALUE, RANGE_VALUE);
    add.accept(DefenseKeys.INTERVAL, INTERVAL);
    add.accept(DefenseKeys.INTERVAL_VALUE, INTERVAL_VALUE);
    add.accept(DefenseKeys.COST, COST);
    add.accept(DefenseKeys.COST_VALUE, COST_VALUE);
    add.accept(DefenseKeys.TARGETS, TARGETS);
    add.accept(DefenseKeys.NEAREST, NEAREST);
    add.accept(DefenseKeys.ALL_IN_RANGE, ALL_IN_RANGE);
    add.accept(DefenseKeys.NO_SHOCK, NO_SHOCK);
    add.accept(DefenseTexts.DEATH_PREFIX + CubitDamageTypes.SHOCK_MESSAGE_ID, DEATH);
    add.accept(CubitSoundProvider.subtitleKey(CubitSounds.SHOCK_ATTACK_NAME), SHOCK_SUBTITLE);
    add.accept(
        CubitSoundProvider.subtitleKey(CubitSounds.AGGRESSIVE_MODE_NAME), AGGRESSIVE_SUBTITLE);
  }
}
