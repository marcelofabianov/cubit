package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotUpgrade;
import io.github.marcelofabianov.cubit.item.ModuleItem;
import io.github.marcelofabianov.cubit.item.TierLook;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.function.BiConsumer;
import net.minecraft.world.item.Item;

/** The modules and the upgrade notices, in English, the base language (answers 808 and 811). */
final class UpgradeTexts {
  private static final String BASE = "Base";
  private static final String ADVANCED = "Advanced";
  private static final String ELITE = "Elite";
  private static final String ULTIMATE = "Ultimate";
  private static final String ADVANCED_MODULE = "Advanced Module";
  private static final String ELITE_MODULE = "Elite Module";
  private static final String ULTIMATE_MODULE = "Ultimate Module";
  private static final String ADVANCED_MODULE_TOOLTIP = "Requires a Base Cubit";
  private static final String ELITE_MODULE_TOOLTIP = "Requires an Advanced Cubit";
  private static final String ULTIMATE_MODULE_TOOLTIP = "Requires an Elite Cubit";
  private static final String UPGRADED = "Upgraded to %s!";
  private static final String NEEDS_TIER = "I need to be %s first";
  private static final String ALREADY_TIER = "I'm already %s";

  private UpgradeTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(TierLook.nameKey(RobotTier.BASE), BASE);
    add.accept(TierLook.nameKey(RobotTier.ADVANCED), ADVANCED);
    add.accept(TierLook.nameKey(RobotTier.ELITE), ELITE);
    add.accept(TierLook.nameKey(RobotTier.ULTIMATE), ULTIMATE);
    module(add, CubitItems.ADVANCED_MODULE.get(), ADVANCED_MODULE, ADVANCED_MODULE_TOOLTIP);
    module(add, CubitItems.ELITE_MODULE.get(), ELITE_MODULE, ELITE_MODULE_TOOLTIP);
    module(add, CubitItems.ULTIMATE_MODULE.get(), ULTIMATE_MODULE, ULTIMATE_MODULE_TOOLTIP);
    add.accept(RobotUpgrade.UPGRADED_KEY, UPGRADED);
    add.accept(RobotUpgrade.NEEDS_TIER_KEY, NEEDS_TIER);
    add.accept(RobotUpgrade.ALREADY_TIER_KEY, ALREADY_TIER);
  }

  static void module(BiConsumer<String, String> add, Item item, String name, String tooltip) {
    add.accept(item.getDescriptionId(), name);
    add.accept(((ModuleItem) item).tooltipKey(), tooltip);
  }
}
