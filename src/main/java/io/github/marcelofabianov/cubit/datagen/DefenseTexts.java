package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.entity.RobotFocus;
import io.github.marcelofabianov.cubit.menu.DefenseKeys;
import io.github.marcelofabianov.cubit.registry.CubitDamageTypes;
import io.github.marcelofabianov.cubit.registry.CubitSounds;
import java.util.function.BiConsumer;

/** The defense tab, the shock and its sounds, in English, the base language (answer 833). */
final class DefenseTexts {
  static final String DEATH_PREFIX = "death.attack.";
  private static final String TAB = "Defense";
  private static final String MODES = "Modes";
  private static final String PEACEFUL = "Peaceful";
  private static final String PEACEFUL_DETAIL = "Never attacks";
  private static final String RETALIATE = "Retaliate";
  private static final String RETALIATE_DETAIL = "Shocks whoever hurts you or the Cubit";
  private static final String AGGRESSIVE = "Aggressive";
  private static final String AGGRESSIVE_DETAIL = "Shocks hostile mobs in range";
  private static final String FOCUS = "Focus";
  private static final String FOCUS_DETAIL = "Shocks only the last mob you hit";
  private static final String FOCUS_TARGET = "Target: %s";
  private static final String UNLOCKS_AT = "Unlocks at %s";
  private static final String SHOCK = "Shock";
  private static final String DAMAGE = "Damage";
  private static final String RANGE = "Range";
  private static final String RANGE_VALUE = "%s blocks";
  private static final String INTERVAL = "Interval";
  private static final String INTERVAL_VALUE = "Every %s s";
  private static final String COST = "Cost";
  private static final String COST_VALUE = "%s FE per shock";
  private static final String TARGETS = "Targets";
  private static final String NEAREST = "The nearest";
  private static final String ALL_IN_RANGE = "All in range";
  private static final String NO_SHOCK = "No shock in any tier";
  private static final String DEATH = "%1$s was zapped by %2$s";
  private static final String SHOCK_SUBTITLE = "Cubit shocks";
  private static final String AGGRESSIVE_SUBTITLE = "Cubit turns aggressive";

  private DefenseTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(DefenseKeys.TAB, TAB);
    add.accept(DefenseKeys.MODES, MODES);
    add.accept(DefenseKeys.mode(DefenseMode.PEACEFUL), PEACEFUL);
    add.accept(DefenseKeys.detail(DefenseMode.PEACEFUL), PEACEFUL_DETAIL);
    add.accept(DefenseKeys.mode(DefenseMode.RETALIATE), RETALIATE);
    add.accept(DefenseKeys.detail(DefenseMode.RETALIATE), RETALIATE_DETAIL);
    add.accept(DefenseKeys.mode(DefenseMode.AGGRESSIVE), AGGRESSIVE);
    add.accept(DefenseKeys.detail(DefenseMode.AGGRESSIVE), AGGRESSIVE_DETAIL);
    add.accept(DefenseKeys.mode(DefenseMode.FOCUS), FOCUS);
    add.accept(DefenseKeys.detail(DefenseMode.FOCUS), FOCUS_DETAIL);
    add.accept(RobotFocus.TARGET_KEY, FOCUS_TARGET);
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
    add.accept(DEATH_PREFIX + CubitDamageTypes.SHOCK_MESSAGE_ID, DEATH);
    add.accept(CubitSoundProvider.subtitleKey(CubitSounds.SHOCK_ATTACK_NAME), SHOCK_SUBTITLE);
    add.accept(
        CubitSoundProvider.subtitleKey(CubitSounds.AGGRESSIVE_MODE_NAME), AGGRESSIVE_SUBTITLE);
  }
}
