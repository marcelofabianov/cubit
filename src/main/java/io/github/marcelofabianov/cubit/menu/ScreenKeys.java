package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.dock.DockStatus;
import java.util.Locale;

/** The translation keys of the screens, shared by the screens and the language file. */
public final class ScreenKeys {
  private static final String PREFIX = "gui.cubit.";
  private static final String DOCK = PREFIX + "dock.";

  public static final String ENERGY_AMOUNT = PREFIX + "energy_amount";
  public static final String HEALTH = PREFIX + "health";
  public static final String HEALTH_AMOUNT = PREFIX + "health_amount";
  public static final String REDSTONE = PREFIX + "redstone";

  public static final String DOCK_STORED_ENERGY = DOCK + "stored_energy";
  public static final String DOCK_INPUT = DOCK + "input";
  public static final String DOCK_FULL = DOCK + "full";
  public static final String DOCK_NO_INPUT = DOCK + "no_input";
  public static final String DOCK_LIMIT = DOCK + "limit";
  public static final String DOCK_DOCKED = DOCK + "docked";
  public static final String DOCK_TAB_DOCK = DOCK + "tab_dock";
  public static final String DOCK_TAB_STORAGE = DOCK + "tab_storage";
  public static final String DOCK_ROBOT_BATTERY = DOCK + "robot_battery";
  public static final String DOCK_CHARGING_LINE = DOCK + "charging_line";
  public static final String DOCK_COMPLETE_LINE = DOCK + "complete_line";
  public static final String DOCK_NOT_CHARGING_LINE = DOCK + "not_charging_line";
  public static final String DOCK_HEAL_MINUTES = DOCK + "heal_minutes";
  public static final String DOCK_HEAL_UNDER_A_MINUTE = DOCK + "heal_under_a_minute";
  public static final String DOCK_HEAL_FULL = DOCK + "heal_full";
  public static final String DOCK_NO_ROBOT = DOCK + "no_robot";
  public static final String DOCK_READY = DOCK + "ready";
  public static final String DOCK_NEEDS_ENERGY = DOCK + "needs_energy";
  public static final String DOCK_PLACE_HINT = DOCK + "place_hint";
  public static final String DOCK_QUEUE = DOCK + "queue";
  public static final String DOCK_QUEUE_EMPTY = DOCK + "queue_empty";
  public static final String DOCK_QUEUE_ONE = DOCK + "queue_one";
  public static final String DOCK_QUEUE_MANY = DOCK + "queue_many";
  public static final String DOCK_OWNER = DOCK + "owner";
  public static final String DOCK_OWNER_UNKNOWN = DOCK + "owner_unknown";
  public static final String DOCK_CALL = DOCK + "call";

  private static final String DOCK_STATUS = DOCK + "status.";
  private static final String ROBOT = PREFIX + "robot.";
  private static final String ROBOT_STATUS = ROBOT + "status.";
  private static final String RECHARGE = ROBOT + "recharge.";
  private static final String SOLAR = ROBOT + "solar.";
  private static final String CAUSE = ROBOT + "cause.";
  private static final String TIER = ROBOT + "tier.";

  public static final String BATTERY = ROBOT + "battery";
  public static final String ROBOT_INVENTORY = ROBOT + "inventory";
  public static final String EXCLUSIVE_SLOTS = ROBOT + "exclusive_slots";
  public static final String TORCHES = ROBOT + "torches";
  public static final String TORCHES_DETAIL = ROBOT + "torches.detail";
  public static final String AUTO_RETURN = ROBOT + "auto_return";
  public static final String AUTO_RETURN_DETAIL = ROBOT + "auto_return.detail";
  public static final String UNLOAD = ROBOT + "unload";
  public static final String UNLOAD_DETAIL = ROBOT + "unload.detail";
  public static final String TAB_INVENTORY = ROBOT + "tab_inventory";
  public static final String TAB_SETTINGS = ROBOT + "tab_settings";
  public static final String SETTINGS = ROBOT + "settings";
  public static final String SETTINGS_BATTERY = ROBOT + "settings_battery";
  public static final String FUNCTIONS = ROBOT + "functions";
  public static final String LAMP = ROBOT + "lamp";
  public static final String LAMP_DETAIL = ROBOT + "lamp_detail";
  public static final String SCANNERS = ROBOT + "scanners";
  public static final String SCANNERS_DETAIL = ROBOT + "scanners_detail";
  public static final String CHATTER = ROBOT + "chatter";
  public static final String CHATTER_DETAIL = ROBOT + "chatter_detail";
  public static final String SOLAR_DETAIL = ROBOT + "solar_detail";
  public static final String POWER = ROBOT + "power";
  public static final String POWER_DETAIL = ROBOT + "power_detail";
  public static final String SWITCH_ON = ROBOT + "switch_on";
  public static final String SWITCH_OFF = ROBOT + "switch_off";
  public static final String SCANNER_FILTER = ROBOT + "scanner_filter";
  public static final String LOOKING_FOR = ROBOT + "looking_for";
  public static final String NO_FILTER = ROBOT + "no_filter";
  public static final String FILTER_HINT = ROBOT + "filter_hint";
  public static final String STATUS = ROBOT + "status";
  public static final String SOLAR_PANEL = ROBOT + "solar_panel";
  public static final String ENERGY_USE = ROBOT + "energy_use";
  public static final String ENERGY_USE_VALUE = ROBOT + "energy_use_value";
  public static final String IDLE = ROBOT + "idle";
  public static final String HIBERNATING = ROBOT + "hibernating";
  public static final String HEALTH_RECOVERS = ROBOT + "health_recovers";
  public static final String HEALTH_RECOVERING = ROBOT + "health_recovering";
  public static final String FOLLOWING = ROBOT + "following";
  public static final String FOLLOWING_YOU = ROBOT + "following_you";
  public static final String FOLLOWING_NOBODY = ROBOT + "following_nobody";

  private ScreenKeys() {}

  /** Off and no energy share one text, as in the images (answer 685). */
  public static String dockStatus(DockStatus status) {
    String name =
        switch (status) {
          case OFF, NO_ENERGY -> DockStatusName.NO_POWER;
          default -> status.name().toLowerCase(Locale.ROOT);
        };
    return DOCK_STATUS + name;
  }

  public static String robotStatus(Enum<?> status) {
    return ROBOT_STATUS + lower(status);
  }

  public static String recharge(Enum<?> source) {
    return RECHARGE + lower(source);
  }

  public static String solar(Enum<?> status) {
    return SOLAR + lower(status);
  }

  public static String cause(Enum<?> cause) {
    return CAUSE + lower(cause);
  }

  public static String tier(Enum<?> tier) {
    return TIER + lower(tier);
  }

  private static String lower(Enum<?> value) {
    return value.name().toLowerCase(Locale.ROOT);
  }

  /** The names of the dock status keys, for the language file. */
  public static final class DockStatusName {
    public static final String NO_POWER = "no_power";
    public static final String IDLE = "idle";
    public static final String CHARGING = "charging";
    public static final String COMPLETE = "complete";

    private DockStatusName() {}
  }
}
