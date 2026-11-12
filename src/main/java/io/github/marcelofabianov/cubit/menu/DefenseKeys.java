package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;

/** The translation keys of the defense tab. They are a contract with whoever translates the mod. */
public final class DefenseKeys {
  private static final String PREFIX = "gui.cubit.robot.defense.";
  private static final String DETAIL_SUFFIX = ".detail";

  public static final String TAB = PREFIX + "tab";
  public static final String MODES = PREFIX + "modes";
  public static final String UNLOCKS_AT = PREFIX + "unlocks_at";
  public static final String SHOCK = PREFIX + "shock";
  public static final String DAMAGE = PREFIX + "damage";
  public static final String RANGE = PREFIX + "range";
  public static final String RANGE_VALUE = PREFIX + "range_value";
  public static final String INTERVAL = PREFIX + "interval";
  public static final String INTERVAL_VALUE = PREFIX + "interval_value";
  public static final String COST = PREFIX + "cost";
  public static final String COST_VALUE = PREFIX + "cost_value";
  public static final String TARGETS = PREFIX + "targets";
  public static final String NEAREST = PREFIX + "nearest";
  public static final String ALL_IN_RANGE = PREFIX + "all_in_range";
  public static final String NO_SHOCK = PREFIX + "no_shock";

  private DefenseKeys() {}

  public static String mode(DefenseMode mode) {
    return PREFIX + mode.key();
  }

  public static String detail(DefenseMode mode) {
    return PREFIX + mode.key() + DETAIL_SUFFIX;
  }
}
