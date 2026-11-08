package io.github.marcelofabianov.cubit.domain.energy;

/** What the indicator on the back of the robot shows: nothing, or a level, normal or critical. */
public enum BatteryIndicator {
  EMPTY,
  NORMAL_1,
  NORMAL_2,
  NORMAL_3,
  NORMAL_4,
  CRITICAL_1,
  CRITICAL_2,
  CRITICAL_3,
  CRITICAL_4;

  private static final int LEVELS = 4;

  /** The critical limit comes from the config, so it may cover any level, but never the empty. */
  public static BatteryIndicator of(Battery battery, Percentage critical) {
    int level = battery.level(LEVELS);
    if (level == 0) {
      return EMPTY;
    }
    BatteryIndicator first = battery.hasAtMost(critical) ? CRITICAL_1 : NORMAL_1;
    return values()[first.ordinal() + level - 1];
  }
}
