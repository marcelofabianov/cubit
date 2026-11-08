package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.domain.dock.DockStatus;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import java.util.function.BiConsumer;

/** What the screens of the Cubit and the dock show, in English (telas.md, answers 676 to 693). */
final class ScreenTexts {
  private static final String ENERGY_AMOUNT = "%s / %s FE";
  private static final String HEALTH = "Health";
  private static final String HEALTH_AMOUNT = "%s / %s";
  private static final String REDSTONE = "Redstone";
  private static final String NO_POWER = "No power";
  private static final String IDLE = "Idle";
  private static final String CHARGING = "Charging";
  private static final String COMPLETE = "Complete";
  private static final String STORED_ENERGY = "Stored energy";
  private static final String INPUT = "Input +%s FE/t";
  private static final String FULL = "Full";
  private static final String NO_INPUT = "No input";
  private static final String LIMIT = "Cables or redstone · up to %s FE/t";
  private static final String DOCKED = "Docked Cubit";
  private static final String ROBOT_BATTERY = "Cubit · Battery";
  private static final String CHARGING_LINE = "+%s FE/t · %s";
  private static final String COMPLETE_LINE = "Battery full · %s";
  private static final String NOT_CHARGING_LINE = "Not charging · %s";
  private static final String HEAL_MINUTES = "full health in about %s min";
  private static final String HEAL_UNDER_A_MINUTE = "full health in under 1 min";
  private static final String HEAL_FULL = "health full";
  private static final String NO_ROBOT = "No Cubit docked";
  private static final String READY = "Ready to charge.";
  private static final String NEEDS_ENERGY = "Needs energy before it can charge.";
  private static final String PLACE_HINT = "A Cubit can also be placed on top of the Dock.";
  private static final String QUEUE = "Queue";
  private static final String QUEUE_EMPTY = "Empty";
  private static final String QUEUE_ONE = "1 Cubit waiting";
  private static final String QUEUE_MANY = "%s Cubits waiting";
  private static final String OWNER = "Owner";
  private static final String OWNER_UNKNOWN = "Unknown";
  private static final String CALL = "Call the nearest Cubit (%s blocks)";

  private ScreenTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(ScreenKeys.ENERGY_AMOUNT, ENERGY_AMOUNT);
    add.accept(ScreenKeys.HEALTH, HEALTH);
    add.accept(ScreenKeys.HEALTH_AMOUNT, HEALTH_AMOUNT);
    add.accept(ScreenKeys.REDSTONE, REDSTONE);
    addDock(add);
  }

  private static void addDock(BiConsumer<String, String> add) {
    add.accept(ScreenKeys.dockStatus(DockStatus.OFF), NO_POWER);
    add.accept(ScreenKeys.dockStatus(DockStatus.IDLE), IDLE);
    add.accept(ScreenKeys.dockStatus(DockStatus.CHARGING), CHARGING);
    add.accept(ScreenKeys.dockStatus(DockStatus.COMPLETE), COMPLETE);
    add.accept(ScreenKeys.DOCK_STORED_ENERGY, STORED_ENERGY);
    add.accept(ScreenKeys.DOCK_INPUT, INPUT);
    add.accept(ScreenKeys.DOCK_FULL, FULL);
    add.accept(ScreenKeys.DOCK_NO_INPUT, NO_INPUT);
    add.accept(ScreenKeys.DOCK_LIMIT, LIMIT);
    add.accept(ScreenKeys.DOCK_DOCKED, DOCKED);
    add.accept(ScreenKeys.DOCK_ROBOT_BATTERY, ROBOT_BATTERY);
    addDockedRobot(add);
    add.accept(ScreenKeys.DOCK_NO_ROBOT, NO_ROBOT);
    add.accept(ScreenKeys.DOCK_READY, READY);
    add.accept(ScreenKeys.DOCK_NEEDS_ENERGY, NEEDS_ENERGY);
    add.accept(ScreenKeys.DOCK_PLACE_HINT, PLACE_HINT);
    add.accept(ScreenKeys.DOCK_QUEUE, QUEUE);
    add.accept(ScreenKeys.DOCK_QUEUE_EMPTY, QUEUE_EMPTY);
    add.accept(ScreenKeys.DOCK_QUEUE_ONE, QUEUE_ONE);
    add.accept(ScreenKeys.DOCK_QUEUE_MANY, QUEUE_MANY);
    add.accept(ScreenKeys.DOCK_OWNER, OWNER);
    add.accept(ScreenKeys.DOCK_OWNER_UNKNOWN, OWNER_UNKNOWN);
    add.accept(ScreenKeys.DOCK_CALL, CALL);
  }

  private static void addDockedRobot(BiConsumer<String, String> add) {
    add.accept(ScreenKeys.DOCK_CHARGING_LINE, CHARGING_LINE);
    add.accept(ScreenKeys.DOCK_COMPLETE_LINE, COMPLETE_LINE);
    add.accept(ScreenKeys.DOCK_NOT_CHARGING_LINE, NOT_CHARGING_LINE);
    add.accept(ScreenKeys.DOCK_HEAL_MINUTES, HEAL_MINUTES);
    add.accept(ScreenKeys.DOCK_HEAL_UNDER_A_MINUTE, HEAL_UNDER_A_MINUTE);
    add.accept(ScreenKeys.DOCK_HEAL_FULL, HEAL_FULL);
  }
}
