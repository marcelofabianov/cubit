package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.block.DockUpgrade;
import io.github.zenndg.cubit.menu.ScreenKeys;
import java.util.function.BiConsumer;

/** The tiers of the Induction Dock, in Portuguese (answers 1377 and 1382). */
final class PortugueseDockTierTexts {
  private static final String TITLE_TIER = "%s · %s";
  private static final String UPGRADED = "Induction Dock evoluiu para %s!";
  private static final String NEEDS_TIER = "O Induction Dock precisa ser %s antes";
  private static final String ALREADY_TIER = "O Induction Dock já é %s";

  private PortugueseDockTierTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(ScreenKeys.DOCK_TITLE_TIER, TITLE_TIER);
    add.accept(DockUpgrade.UPGRADED_KEY, UPGRADED);
    add.accept(DockUpgrade.NEEDS_TIER_KEY, NEEDS_TIER);
    add.accept(DockUpgrade.ALREADY_TIER_KEY, ALREADY_TIER);
  }
}
