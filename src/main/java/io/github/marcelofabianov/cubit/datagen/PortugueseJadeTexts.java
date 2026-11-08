package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.compat.jade.JadeKeys;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import java.util.function.BiConsumer;

/** What the Jade plugin shows, in Portuguese (answers 712 and 733). */
final class PortugueseJadeTexts {
  private static final String TIER = "Tier: %s";
  private static final String BATTERY = "Bateria: %s%% (%s / %s FE)";
  private static final String DOCK_ENERGY = "Energia: %s / %s FE";
  private static final String BASE = "Base";
  private static final String ADVANCED = "Avançado";
  private static final String ELITE = "Elite";
  private static final String ULTIMATE = "Supremo";
  private static final String ACTIVE = "Estado: Ativo";
  private static final String OUT_OF_BATTERY = "Estado: Sem bateria";
  private static final String SWITCHED_OFF = "Estado: Desligado";
  private static final String HIBERNATING = "Estado: Hibernando";
  private static final String DOCK_OFF = "Estado: Sem energia";
  private static final String DOCK_IDLE = "Estado: Ociosa";
  private static final String DOCK_CHARGING = "Estado: Carregando";
  private static final String DOCK_COMPLETE = "Estado: Completa";
  private static final String DOCK_NO_ENERGY = DOCK_OFF;
  private static final String OFF_NAME = "off";
  private static final String IDLE_NAME = "idle";
  private static final String CHARGING_NAME = "charging";
  private static final String COMPLETE_NAME = "complete";
  private static final String NO_ENERGY_NAME = "no_energy";
  private static final String ROBOT_OPTION = "Cubit";
  private static final String DOCK_OPTION = "Base de Indução";

  private PortugueseJadeTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(JadeKeys.TIER, TIER);
    add.accept(JadeKeys.BATTERY, BATTERY);
    add.accept(JadeKeys.DOCK_ENERGY, DOCK_ENERGY);
    add.accept(JadeKeys.tierName(RobotTier.BASE), BASE);
    add.accept(JadeKeys.tierName(RobotTier.ADVANCED), ADVANCED);
    add.accept(JadeKeys.tierName(RobotTier.ELITE), ELITE);
    add.accept(JadeKeys.tierName(RobotTier.ULTIMATE), ULTIMATE);
    add.accept(JadeKeys.state(PowerState.ACTIVE), ACTIVE);
    add.accept(JadeKeys.state(PowerState.INACTIVE_EXHAUSTED), OUT_OF_BATTERY);
    add.accept(JadeKeys.state(PowerState.INACTIVE_SWITCHED_OFF), SWITCHED_OFF);
    add.accept(JadeKeys.state(PowerState.HIBERNATING), HIBERNATING);
    add.accept(JadeKeys.dockStatus(OFF_NAME), DOCK_OFF);
    add.accept(JadeKeys.dockStatus(IDLE_NAME), DOCK_IDLE);
    add.accept(JadeKeys.dockStatus(CHARGING_NAME), DOCK_CHARGING);
    add.accept(JadeKeys.dockStatus(COMPLETE_NAME), DOCK_COMPLETE);
    add.accept(JadeKeys.dockStatus(NO_ENERGY_NAME), DOCK_NO_ENERGY);
    add.accept(JadeKeys.config(JadeKeys.ROBOT_UID), ROBOT_OPTION);
    add.accept(JadeKeys.config(JadeKeys.DOCK_UID), DOCK_OPTION);
  }
}
