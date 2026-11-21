package io.github.zenndg.cubit.client;

import io.github.zenndg.cubit.client.ScreenPainter.Box;
import io.github.zenndg.cubit.client.ScreenPainter.Colors;
import io.github.zenndg.cubit.domain.dock.DockSunState;
import io.github.zenndg.cubit.domain.energy.EnergyFormat;
import io.github.zenndg.cubit.menu.InductionDockMenu;
import io.github.zenndg.cubit.menu.ScreenKeys;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * The Solar tab of a Solar Dock: whether the panel generates, why it stops, what it gives now and
 * at most at the tier of the dock (answers 1395 and 1396).
 */
final class DockSolarTab {
  private static final int LINE = 9;
  private static final int DETAIL_LINE = 7;
  private static final Box TITLE = new Box(8, 6, 92, LINE);
  private static final Box STATUS = new Box(100, 6, 68, LINE);
  private static final Box STATE = new Box(8, 22, 160, LINE);
  private static final Box REASON = new Box(8, 34, 160, LINE);
  private static final Box NOW = new Box(8, 46, 160, LINE);
  private static final Box MAX = new Box(8, 58, 160, LINE);
  private static final Box HINT = new Box(8, 74, 160, DETAIL_LINE);

  private DockSolarTab() {}

  static void render(ScreenPainter painter, GuiGraphics graphics, InductionDockMenu menu) {
    Component title =
        Component.translatable(ScreenKeys.DOCK_TAB_SOLAR).withStyle(ChatFormatting.BOLD);
    painter.text(graphics, title, TITLE, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
    DockSunState state = menu.sunState();
    boolean generating = state == DockSunState.GENERATING;
    int color = generating ? ScreenPainter.STATUS_GREEN : ScreenPainter.STATUS_ORANGE;
    Component sun = Component.translatable(ScreenKeys.dockSun(state));
    painter.status(graphics, sun, STATUS, color);
    painter.pair(
        graphics,
        STATE,
        Component.translatable(ScreenKeys.DOCK_SOLAR_STATUS),
        sun,
        Colors.TITLE_FIRST);
    if (!generating) {
      painter.pair(
          graphics,
          REASON,
          Component.translatable(ScreenKeys.DOCK_SOLAR_REASON),
          Component.translatable(ScreenKeys.dockSunReason(state)),
          Colors.TITLE_FIRST);
    }
    renderRates(painter, graphics, menu);
  }

  private static void renderRates(
      ScreenPainter painter, GuiGraphics graphics, InductionDockMenu menu) {
    painter.pair(
        graphics,
        NOW,
        Component.translatable(ScreenKeys.DOCK_SOLAR_NOW),
        Component.literal(EnergyFormat.rate(menu.solar())),
        Colors.TITLE_FIRST);
    painter.pair(
        graphics,
        MAX,
        Component.translatable(ScreenKeys.DOCK_SOLAR_MAX),
        Component.literal(EnergyFormat.rate(menu.solarMax())),
        Colors.TITLE_FIRST);
    Component hint = Component.translatable(ScreenKeys.DOCK_SOLAR_HINT);
    painter.text(graphics, hint, HINT, ScreenPainter.DETAIL, ScreenPainter.DETAIL_SCALE);
  }
}
