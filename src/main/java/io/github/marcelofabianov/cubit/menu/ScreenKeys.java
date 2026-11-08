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

  /** The names of the dock status keys, for the language file. */
  public static final class DockStatusName {
    public static final String NO_POWER = "no_power";
    public static final String IDLE = "idle";
    public static final String CHARGING = "charging";
    public static final String COMPLETE = "complete";

    private DockStatusName() {}
  }
}
