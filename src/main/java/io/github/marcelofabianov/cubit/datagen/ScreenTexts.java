package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.domain.behavior.RobotScreenStatus;
import io.github.marcelofabianov.cubit.domain.dock.DockStatus;
import io.github.marcelofabianov.cubit.domain.energy.EnergyUseCause;
import io.github.marcelofabianov.cubit.domain.energy.RechargeSource;
import io.github.marcelofabianov.cubit.domain.energy.SolarStatus;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import java.util.Map;
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

  private static final String BATTERY = "Battery";
  private static final String ROBOT_INVENTORY = "%s's inventory";
  private static final String TAB_INVENTORY = "Inventory";
  private static final String DOCK_TAB_DOCK = "Dock";
  private static final String DOCK_TAB_STORAGE = "Storage";
  private static final String EXCLUSIVE_SLOTS = "Exclusive slots";
  private static final String TORCHES = "Torches";
  private static final String TORCH_DETAIL = "Lights spots where mobs can spawn · %s FE per torch";
  private static final String TAB_SETTINGS = "Settings";
  private static final String SETTINGS_BATTERY = "Battery %s%%";
  private static final String FUNCTIONS = "Functions";
  private static final String LAMP = "Lamp";
  private static final String LAMP_DETAIL = "Lights up below level %s · %s FE/t";
  private static final String SCANNERS = "Scanners";
  private static final String SCANNERS_DETAIL = "Ores %s blocks · mobs %s · %s FE per scan";
  private static final String CHATTER = "Chatter";
  private static final String CHATTER_DETAIL = "Weather, biomes and reactions";
  private static final String SWITCH_ON = "ON";
  private static final String SWITCH_OFF = "OFF";
  private static final String SCANNER_FILTER = "Scanner filter";
  private static final String LOOKING_FOR = "Looking for %s";
  private static final String NO_FILTER = "No filter · rare ores only";
  private static final String FILTER_HINT = "Right-click the Cubit holding an ore to change it.";
  private static final String STATUS = "Status";
  private static final String SOLAR_PANEL = "Solar panel";
  private static final String ENERGY_USE = "Energy use";
  private static final String ENERGY_USE_VALUE = "%s FE/t · %s";
  private static final String IDLE_USE = "idle";
  private static final String HIBERNATING_USE = "hibernating";
  private static final String HEALTH_RECOVERS = "%s / %s · recovers at the Dock";
  private static final String HEALTH_RECOVERING = "%s / %s · recovering";
  private static final String FOLLOWING = "Following";
  private static final String FOLLOWING_YOU = "You";
  private static final String FOLLOWING_NOBODY = "Nobody";
  private static final String SOLAR_DETAIL = "Charges in the sun · %s FE/t";
  private static final String POWER = "Power";
  private static final String POWER_DETAIL = "Turns the Cubit off";
  private static final String SOLAR_SWITCHED_OFF_TEXT = "Off";
  private static final String STATUS_CRITICAL_BATTERY_TEXT = "Critical battery";
  private static final String STATUS_FOLLOWING_TEXT = "Following";
  private static final String STATUS_ACTIVE_TEXT = "Active";
  private static final String STATUS_HIBERNATING_TEXT = "Hibernating · Dock";
  private static final String STATUS_OFF_TEXT = "Off";
  private static final String RECHARGE_DOCK_TEXT = "Dock +%s FE/t";
  private static final String RECHARGE_DOCK_NO_POWER_TEXT = "Dock · no power";
  private static final String RECHARGE_DOCK_FULL_TEXT = "Dock · full";
  private static final String RECHARGE_SLOT_TEXT = "Slot +%s FE/t";
  private static final String RECHARGE_SOLAR_TEXT = "Solar +%s FE/t";
  private static final String RECHARGE_NONE_TEXT = "Not charging";
  private static final String SOLAR_CHARGING_TEXT = "Charging · %s FE/t";
  private static final String SOLAR_NO_SKY_TEXT = "No sky · not charging";
  private static final String SOLAR_NIGHT_TEXT = "Night · not charging";
  private static final String SOLAR_NO_SUN_TEXT = "No sun here · not charging";
  private static final String SOLAR_FULL_TEXT = "Battery full";
  private static final String SOLAR_OFF_TEXT = "Off · hibernating";
  private static final String CAUSE_WALKING_TEXT = "walking";
  private static final String CAUSE_FULL_INVENTORY_TEXT = "full inventory";
  private static final String CAUSE_WATER_TEXT = "water";
  private static final String CAUSE_CLIMBING_TEXT = "climbing";
  private static final String CAUSE_DANGER_TEXT = "danger";
  private static final String CAUSE_LAMP_TEXT = "lamp";
  private static final String TIER_BASE_TEXT = "BASE";
  private static final String TIER_ADVANCED_TEXT = "ADVANCED";
  private static final String TIER_ELITE_TEXT = "ELITE";
  private static final String TIER_ULTIMATE_TEXT = "ULTIMATE";
  private static final Map<Enum<?>, String> NAMED =
      Map.ofEntries(
          Map.entry(RobotScreenStatus.CRITICAL_BATTERY, STATUS_CRITICAL_BATTERY_TEXT),
          Map.entry(RobotScreenStatus.FOLLOWING, STATUS_FOLLOWING_TEXT),
          Map.entry(RobotScreenStatus.ACTIVE, STATUS_ACTIVE_TEXT),
          Map.entry(RobotScreenStatus.HIBERNATING, STATUS_HIBERNATING_TEXT),
          Map.entry(RobotScreenStatus.OFF, STATUS_OFF_TEXT));
  private static final Map<RechargeSource, String> RECHARGES =
      Map.of(
          RechargeSource.DOCK, RECHARGE_DOCK_TEXT,
          RechargeSource.DOCK_NO_POWER, RECHARGE_DOCK_NO_POWER_TEXT,
          RechargeSource.DOCK_FULL, RECHARGE_DOCK_FULL_TEXT,
          RechargeSource.SLOT, RECHARGE_SLOT_TEXT,
          RechargeSource.SOLAR, RECHARGE_SOLAR_TEXT,
          RechargeSource.NONE, RECHARGE_NONE_TEXT);
  private static final Map<SolarStatus, String> SOLARS =
      Map.of(
          SolarStatus.CHARGING, SOLAR_CHARGING_TEXT,
          SolarStatus.NO_SKY, SOLAR_NO_SKY_TEXT,
          SolarStatus.NIGHT, SOLAR_NIGHT_TEXT,
          SolarStatus.NO_SUN, SOLAR_NO_SUN_TEXT,
          SolarStatus.FULL, SOLAR_FULL_TEXT,
          SolarStatus.OFF, SOLAR_OFF_TEXT,
          SolarStatus.SWITCHED_OFF, SOLAR_SWITCHED_OFF_TEXT);
  private static final Map<EnergyUseCause, String> CAUSES =
      Map.of(
          EnergyUseCause.WALKING, CAUSE_WALKING_TEXT,
          EnergyUseCause.FULL_INVENTORY, CAUSE_FULL_INVENTORY_TEXT,
          EnergyUseCause.WATER, CAUSE_WATER_TEXT,
          EnergyUseCause.CLIMBING, CAUSE_CLIMBING_TEXT,
          EnergyUseCause.DANGER, CAUSE_DANGER_TEXT,
          EnergyUseCause.LAMP, CAUSE_LAMP_TEXT);
  private static final Map<RobotTier, String> TIERS =
      Map.of(
          RobotTier.BASE, TIER_BASE_TEXT,
          RobotTier.ADVANCED, TIER_ADVANCED_TEXT,
          RobotTier.ELITE, TIER_ELITE_TEXT,
          RobotTier.ULTIMATE, TIER_ULTIMATE_TEXT);

  private ScreenTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(ScreenKeys.ENERGY_AMOUNT, ENERGY_AMOUNT);
    add.accept(ScreenKeys.HEALTH, HEALTH);
    add.accept(ScreenKeys.HEALTH_AMOUNT, HEALTH_AMOUNT);
    add.accept(ScreenKeys.REDSTONE, REDSTONE);
    addDock(add);
    addRobot(add);
    addSettings(add);
    addStates(add);
  }

  private static void addRobot(BiConsumer<String, String> add) {
    add.accept(ScreenKeys.BATTERY, BATTERY);
    add.accept(ScreenKeys.ROBOT_INVENTORY, ROBOT_INVENTORY);
    add.accept(ScreenKeys.TAB_INVENTORY, TAB_INVENTORY);
    add.accept(ScreenKeys.TAB_SETTINGS, TAB_SETTINGS);
    add.accept(ScreenKeys.EXCLUSIVE_SLOTS, EXCLUSIVE_SLOTS);
    add.accept(ScreenKeys.TORCHES, TORCHES);
    add.accept(ScreenKeys.TORCHES_DETAIL, TORCH_DETAIL);
    add.accept(ScreenKeys.DOCK_TAB_DOCK, DOCK_TAB_DOCK);
    add.accept(ScreenKeys.DOCK_TAB_STORAGE, DOCK_TAB_STORAGE);
    add.accept(ScreenKeys.SETTINGS, TAB_SETTINGS);
    add.accept(ScreenKeys.SETTINGS_BATTERY, SETTINGS_BATTERY);
    add.accept(ScreenKeys.FUNCTIONS, FUNCTIONS);
    add.accept(ScreenKeys.LAMP, LAMP);
    add.accept(ScreenKeys.LAMP_DETAIL, LAMP_DETAIL);
    add.accept(ScreenKeys.SCANNERS, SCANNERS);
    add.accept(ScreenKeys.SCANNERS_DETAIL, SCANNERS_DETAIL);
    add.accept(ScreenKeys.CHATTER, CHATTER);
    add.accept(ScreenKeys.CHATTER_DETAIL, CHATTER_DETAIL);
    add.accept(ScreenKeys.SWITCH_ON, SWITCH_ON);
    add.accept(ScreenKeys.SWITCH_OFF, SWITCH_OFF);
  }

  private static void addSettings(BiConsumer<String, String> add) {
    add.accept(ScreenKeys.SCANNER_FILTER, SCANNER_FILTER);
    add.accept(ScreenKeys.LOOKING_FOR, LOOKING_FOR);
    add.accept(ScreenKeys.NO_FILTER, NO_FILTER);
    add.accept(ScreenKeys.FILTER_HINT, FILTER_HINT);
    add.accept(ScreenKeys.STATUS, STATUS);
    add.accept(ScreenKeys.SOLAR_PANEL, SOLAR_PANEL);
    add.accept(ScreenKeys.ENERGY_USE, ENERGY_USE);
    add.accept(ScreenKeys.ENERGY_USE_VALUE, ENERGY_USE_VALUE);
    add.accept(ScreenKeys.IDLE, IDLE_USE);
    add.accept(ScreenKeys.HIBERNATING, HIBERNATING_USE);
    add.accept(ScreenKeys.HEALTH_RECOVERS, HEALTH_RECOVERS);
    add.accept(ScreenKeys.HEALTH_RECOVERING, HEALTH_RECOVERING);
    add.accept(ScreenKeys.FOLLOWING, FOLLOWING);
    add.accept(ScreenKeys.FOLLOWING_YOU, FOLLOWING_YOU);
    add.accept(ScreenKeys.FOLLOWING_NOBODY, FOLLOWING_NOBODY);
    add.accept(ScreenKeys.SOLAR_DETAIL, SOLAR_DETAIL);
    add.accept(ScreenKeys.POWER, POWER);
    add.accept(ScreenKeys.POWER_DETAIL, POWER_DETAIL);
  }

  private static void addStates(BiConsumer<String, String> add) {
    NAMED.forEach((status, text) -> add.accept(ScreenKeys.robotStatus(status), text));
    RECHARGES.forEach((source, text) -> add.accept(ScreenKeys.recharge(source), text));
    SOLARS.forEach((status, text) -> add.accept(ScreenKeys.solar(status), text));
    CAUSES.forEach((cause, text) -> add.accept(ScreenKeys.cause(cause), text));
    TIERS.forEach((tier, text) -> add.accept(ScreenKeys.tier(tier), text));
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
