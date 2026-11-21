package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.entity.RechargeSplashImpact;
import io.github.zenndg.cubit.item.RechargeSplashItem;
import io.github.zenndg.cubit.registry.CubitEntities;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.function.BiConsumer;

/** The Recharge Splash, in English, the base language (answers 1400 and 1419). */
final class RechargeSplashTexts {
  private static final String NAME = "Recharge Splash";
  private static final String TIP = "Heals and recharges your Cubits over %s s, one at a time";
  private static final String STILL_RECHARGING = "Cubit is still recharging: wait %s s";

  private RechargeSplashTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(CubitItems.RECHARGE_SPLASH.get().getDescriptionId(), NAME);
    add.accept(CubitEntities.RECHARGE_SPLASH.get().getDescriptionId(), NAME);
    add.accept(RechargeSplashItem.TOOLTIP_KEY, TIP);
    add.accept(RechargeSplashImpact.STILL_RECHARGING_KEY, STILL_RECHARGING);
  }
}
