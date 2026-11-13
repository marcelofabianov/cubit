package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import java.util.Arrays;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;

/**
 * The Dock and Storage tabs on the right of the dock screen, like the ones of the robot screen
 * (answers 956, 957 and 960). The open tab is only a choice of the client.
 */
final class DockScreenTabs {
  private static final int DOCK_Y = 9;
  private static final int STORAGE_Y = 37;
  private static final int[] TOPS = {DOCK_Y, STORAGE_Y};

  private Tab open = Tab.DOCK;

  /** The tabs, from the top. */
  enum Tab {
    DOCK(DOCK_Y, ScreenKeys.DOCK_TAB_DOCK),
    STORAGE(STORAGE_Y, ScreenKeys.DOCK_TAB_STORAGE);

    private final int y;
    private final String name;

    Tab(int y, String name) {
      this.y = y;
      this.name = name;
    }

    String title() {
      return name;
    }
  }

  Tab open() {
    return open;
  }

  void open(Tab tab) {
    open = tab;
  }

  void renderBehind(GuiGraphics graphics, int left, int top) {
    ScreenTabStrip.renderBehind(graphics, left, top, TOPS, open.y);
  }

  void renderFront(GuiGraphics graphics, int left, int top) {
    ScreenTabStrip.renderOpen(graphics, left, top, open.y);
    int iconX = left + ScreenTabStrip.ICON_X;
    ScreenSprites.dockTab(graphics, iconX, top + DOCK_Y + ScreenTabStrip.ICON_Y);
    ScreenSprites.storageTab(graphics, iconX, top + STORAGE_Y + ScreenTabStrip.ICON_Y);
  }

  /** The tab under a point given from the left of the tabs and the top of the window. */
  Optional<Tab> at(double x, double y) {
    return Arrays.stream(Tab.values())
        .filter(each -> ScreenTabStrip.contains(x, y, each.y))
        .findFirst();
  }
}
