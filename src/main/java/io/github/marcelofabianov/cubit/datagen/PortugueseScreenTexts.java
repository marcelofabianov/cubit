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

/** What the screens of the Cubit and the dock show, in Portuguese (answers 712 and 733). */
final class PortugueseScreenTexts {
  private static final String ENERGY_AMOUNT = "%s / %s FE";
  private static final String HEALTH = "Vida";
  private static final String HEALTH_AMOUNT = "%s / %s";
  private static final String REDSTONE = "Redstone";
  private static final String NO_POWER = "Sem energia";
  private static final String IDLE = "Ociosa";
  private static final String CHARGING = "Carregando";
  private static final String COMPLETE = "Completa";
  private static final String STORED_ENERGY = "Energia guardada";
  private static final String INPUT = "Entrada +%s FE/t";
  private static final String FULL = "Cheia";
  private static final String NO_INPUT = "Sem entrada";
  private static final String LIMIT = "Cabos ou redstone · até %s FE/t";
  private static final String DOCKED = "Cubit na base";
  private static final String ROBOT_BATTERY = "Cubit · Bateria";
  private static final String CHARGING_LINE = "+%s FE/t · %s";
  private static final String COMPLETE_LINE = "Bateria cheia · %s";
  private static final String NOT_CHARGING_LINE = "Sem carregar · %s";
  private static final String HEAL_MINUTES = "vida cheia em cerca de %s min";
  private static final String HEAL_UNDER_A_MINUTE = "vida cheia em menos de 1 min";
  private static final String HEAL_FULL = "vida cheia";
  private static final String NO_ROBOT = "Nenhum Cubit na base";
  private static final String READY = "Pronta para carregar.";
  private static final String NEEDS_ENERGY = "Precisa de energia antes de carregar.";
  private static final String PLACE_HINT = "Um Cubit também pode ser colocado em cima da base.";
  private static final String QUEUE = "Fila";
  private static final String QUEUE_EMPTY = "Vazia";
  private static final String QUEUE_ONE = "1 Cubit esperando";
  private static final String QUEUE_MANY = "%s Cubits esperando";
  private static final String OWNER = "Dono";
  private static final String OWNER_UNKNOWN = "Desconhecido";
  private static final String CALL = "Chamar o Cubit mais próximo (%s blocos)";

  private static final String BATTERY = "Bateria";
  private static final String ROBOT_INVENTORY = "Inventário de %s";
  private static final String TAB_INVENTORY = "Inventário";
  private static final String DOCK_TAB_DOCK = "Dock";
  private static final String DOCK_TAB_STORAGE = "Armazenamento";
  private static final String TAB_SETTINGS = "Configurações";
  private static final String SETTINGS_BATTERY = "Bateria %s%%";
  private static final String FUNCTIONS = "Funções";
  private static final String LAMP = "Lanterna";
  private static final String LAMP_DETAIL = "Acende abaixo do nível %s · %s FE/t";
  private static final String SCANNERS = "Scanners";
  private static final String SCANNERS_DETAIL = "Minérios %s · mobs %s · %s FE por varredura";
  private static final String CHATTER = "Falas";
  private static final String CHATTER_DETAIL = "Tempo, biomas e reações";
  private static final String SWITCH_ON = "LIG";
  private static final String SWITCH_OFF = "DESL";
  private static final String SCANNER_FILTER = "Filtro do scanner";
  private static final String LOOKING_FOR = "Procurando %s";
  private static final String NO_FILTER = "Sem filtro · só minérios raros";
  private static final String FILTER_HINT = "Clique no Cubit com um minério na mão para mudar.";
  private static final String STATUS = "Estado";
  private static final String SOLAR_PANEL = "Painel solar";
  private static final String ENERGY_USE = "Consumo";
  private static final String ENERGY_USE_VALUE = "%s FE/t · %s";
  private static final String IDLE_USE = "parado";
  private static final String HIBERNATING_USE = "hibernando";
  private static final String HEALTH_RECOVERS = "%s / %s · recupera na base";
  private static final String HEALTH_RECOVERING = "%s / %s · recuperando";
  private static final String FOLLOWING = "Seguindo";
  private static final String FOLLOWING_YOU = "Você";
  private static final String FOLLOWING_NOBODY = "Ninguém";
  private static final String SOLAR_DETAIL = "Carrega no sol · %s FE/t";
  private static final String POWER = "Energia";
  private static final String POWER_DETAIL = "Desliga o Cubit";
  private static final String SOLAR_SWITCHED_OFF_TEXT = "Desligado";
  private static final String STATUS_CRITICAL_BATTERY_TEXT = "Bateria crítica";
  private static final String STATUS_FOLLOWING_TEXT = "Seguindo";
  private static final String STATUS_ACTIVE_TEXT = "Ativo";
  private static final String STATUS_HIBERNATING_TEXT = "Hibernando · Base";
  private static final String STATUS_OFF_TEXT = "Desligado";
  private static final String RECHARGE_DOCK_TEXT = "Base +%s FE/t";
  private static final String RECHARGE_DOCK_NO_POWER_TEXT = "Base · sem energia";
  private static final String RECHARGE_DOCK_FULL_TEXT = "Base · cheio";
  private static final String RECHARGE_SLOT_TEXT = "Slot +%s FE/t";
  private static final String RECHARGE_SOLAR_TEXT = "Solar +%s FE/t";
  private static final String RECHARGE_NONE_TEXT = "Sem carregar";
  private static final String SOLAR_CHARGING_TEXT = "Carregando · %s FE/t";
  private static final String SOLAR_NO_SKY_TEXT = "Sem céu · sem carregar";
  private static final String SOLAR_NIGHT_TEXT = "Noite · sem carregar";
  private static final String SOLAR_NO_SUN_TEXT = "Sem sol aqui · sem carregar";
  private static final String SOLAR_FULL_TEXT = "Bateria cheia";
  private static final String SOLAR_OFF_TEXT = "Desligado · hibernando";
  private static final String CAUSE_WALKING_TEXT = "andando";
  private static final String CAUSE_FULL_INVENTORY_TEXT = "inventário cheio";
  private static final String CAUSE_WATER_TEXT = "água";
  private static final String CAUSE_CLIMBING_TEXT = "subindo";
  private static final String CAUSE_DANGER_TEXT = "perigo";
  private static final String CAUSE_LAMP_TEXT = "lanterna";
  private static final String TIER_BASE_TEXT = "BASE";
  private static final String TIER_ADVANCED_TEXT = "AVANÇADO";
  private static final String TIER_ELITE_TEXT = "ELITE";
  private static final String TIER_ULTIMATE_TEXT = "SUPREMO";
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

  private PortugueseScreenTexts() {}

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
