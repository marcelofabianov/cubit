package io.github.marcelofabianov.cubit.item;

import io.github.marcelofabianov.cubit.domain.robot.TabletApp;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

/**
 * The color of each app of the Tablet, in the action bar and in the strip on the item, so the
 * player sees at once which app is active (answers 1082 to 1084). Only visual, so it stays here.
 */
public final class TabletAppLook {
  private static final int OPEN_BLUE = 0x4FA3FF;
  private static final int TARGET_GREEN = 0x5BD15B;
  private static final int DOCK_RED = 0xE0504A;
  private static final int AREA_PURPLE = 0x9966F2;

  private TabletAppLook() {}

  /** In RGB, without alpha. */
  public static int color(TabletApp app) {
    return switch (app) {
      case OPEN -> OPEN_BLUE;
      case TARGET -> TARGET_GREEN;
      case DOCK -> DOCK_RED;
      case AREA -> AREA_PURPLE;
    };
  }

  /** `Cubit Tablet · Area`, bold, in the color of the app, for the action bar (answer 1082). */
  public static Component label(TabletApp app) {
    return Component.translatable(
            TabletKeys.WITH_APP,
            CubitItems.TABLET.get().getDescription(),
            Component.translatable(TabletItem.appKey(app)))
        .withStyle(ChatFormatting.BOLD)
        .withColor(color(app));
  }
}
