package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.entity.RechargeSplashImpact;
import io.github.zenndg.cubit.item.RechargeSplashItem;
import io.github.zenndg.cubit.registry.CubitEntities;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.function.BiConsumer;

/** The Recharge Splash, in Portuguese (answers 1400 and 1419). */
final class PortugueseRechargeSplashTexts {
  private static final String NAME = "Mistura de Recarga";
  private static final String TIP = "Cura e recarrega seus Cubits em %s s, uma mistura por vez";
  private static final String STILL_RECHARGING = "O Cubit ainda está recarregando: aguarde %s s";

  private PortugueseRechargeSplashTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(CubitItems.RECHARGE_SPLASH.get().getDescriptionId(), NAME);
    add.accept(CubitEntities.RECHARGE_SPLASH.get().getDescriptionId(), NAME);
    add.accept(RechargeSplashItem.TOOLTIP_KEY, TIP);
    add.accept(RechargeSplashImpact.STILL_RECHARGING_KEY, STILL_RECHARGING);
  }
}
