package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotUpgrade;
import io.github.marcelofabianov.cubit.item.TierLook;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.function.BiConsumer;

/** The modules and the upgrade notices, in Portuguese (answers 733 and 811). */
final class PortugueseUpgradeTexts {
  private static final String BASE = "Base";
  private static final String ADVANCED = "Avançado";
  private static final String ELITE = "Elite";
  private static final String ULTIMATE = "Supremo";
  private static final String ADVANCED_MODULE = "Módulo Avançado";
  private static final String ELITE_MODULE = "Módulo Elite";
  private static final String ULTIMATE_MODULE = "Módulo Supremo";
  private static final String ADVANCED_MODULE_TOOLTIP = "Exige um Cubit Base";
  private static final String ELITE_MODULE_TOOLTIP = "Exige um Cubit Avançado";
  private static final String ULTIMATE_MODULE_TOOLTIP = "Exige um Cubit Elite";
  private static final String UPGRADED = "Evoluí para %s!";
  private static final String NEEDS_TIER = "Preciso ser %s antes";
  private static final String ALREADY_TIER = "Já sou %s";

  private PortugueseUpgradeTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(TierLook.nameKey(RobotTier.BASE), BASE);
    add.accept(TierLook.nameKey(RobotTier.ADVANCED), ADVANCED);
    add.accept(TierLook.nameKey(RobotTier.ELITE), ELITE);
    add.accept(TierLook.nameKey(RobotTier.ULTIMATE), ULTIMATE);
    UpgradeTexts.module(
        add, CubitItems.ADVANCED_MODULE.get(), ADVANCED_MODULE, ADVANCED_MODULE_TOOLTIP);
    UpgradeTexts.module(add, CubitItems.ELITE_MODULE.get(), ELITE_MODULE, ELITE_MODULE_TOOLTIP);
    UpgradeTexts.module(
        add, CubitItems.ULTIMATE_MODULE.get(), ULTIMATE_MODULE, ULTIMATE_MODULE_TOOLTIP);
    add.accept(RobotUpgrade.UPGRADED_KEY, UPGRADED);
    add.accept(RobotUpgrade.NEEDS_TIER_KEY, NEEDS_TIER);
    add.accept(RobotUpgrade.ALREADY_TIER_KEY, ALREADY_TIER);
  }
}
