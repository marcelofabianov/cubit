package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.robot.PickupMode;

/**
 * The translation keys of the behavior tab. They are a contract with whoever translates the mod.
 */
public final class BehaviorKeys {
  private static final String PREFIX = "gui.cubit.robot.behavior.";

  public static final String TAB = PREFIX + "tab";
  public static final String PICKUP = PREFIX + "pickup";
  public static final String PICKUP_DETAIL = PREFIX + "pickup.detail";
  public static final String MAGNET = PREFIX + "magnet";
  public static final String MAGNET_DETAIL = PREFIX + "magnet.detail";
  public static final String FILTER = PREFIX + "filter";
  public static final String TAG = PREFIX + "tag";

  private BehaviorKeys() {}

  public static String mode(PickupMode mode) {
    return PREFIX + mode.key();
  }
}
