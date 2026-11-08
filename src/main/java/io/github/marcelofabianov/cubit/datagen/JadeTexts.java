package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.compat.jade.JadeKeys;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import java.util.function.BiConsumer;

/** What the Jade plugin shows, in English (answers 660 and 662). */
final class JadeTexts {
  private static final String TIER = "Tier: %s";
  private static final String BATTERY = "Battery: %s%% (%s / %s FE)";
  private static final String DOCK_ENERGY = "Energy: %s / %s FE";
  private static final String BASE = "Base";
  private static final String ADVANCED = "Advanced";
  private static final String ELITE = "Elite";
  private static final String ULTIMATE = "Ultimate";
  private static final String ACTIVE = "State: Active";
  private static final String OUT_OF_BATTERY = "State: Out of battery";
  private static final String SWITCHED_OFF = "State: Switched off";
  private static final String HIBERNATING = "State: Hibernating";
  private static final String DOCK_OFF = "Status: Off";
  private static final String DOCK_IDLE = "Status: Idle";
  private static final String DOCK_CHARGING = "Status: Charging";
  private static final String DOCK_COMPLETE = "Status: Complete";
  private static final String DOCK_NO_ENERGY = "Status: No energy";
  private static final String OFF_NAME = "off";
  private static final String IDLE_NAME = "idle";
  private static final String CHARGING_NAME = "charging";
  private static final String COMPLETE_NAME = "complete";
  private static final String NO_ENERGY_NAME = "no_energy";
  private static final String ROBOT_OPTION = "Cubit";
  private static final String DOCK_OPTION = "Induction Dock";

  private JadeTexts() {}

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
