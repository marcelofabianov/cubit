package io.github.zenndg.cubit.client;

import io.github.zenndg.cubit.menu.ScreenKeys;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.Arrays;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

/**
 * The Dock and Storage tabs on the right of the dock screen, like the ones of the robot screen, and
 * the Solar tab of a Solar Dock, with the icon of the panel (answers 956, 957, 960 and 1395). The
 * open tab is only a choice of the client.
 */
final class DockScreenTabs {
  private static final int DOCK_Y = 9;
  private static final int STORAGE_Y = 37;
  private static final int SOLAR_Y = 65;

  /** The item is 16 pixels, and the icons of the tabs are smaller, so it sits a little up. */
  private static final int ITEM_SHIFT = -2;

  private static final int[] TOPS = {DOCK_Y, STORAGE_Y};
  private static final int[] SOLAR_TOPS = {DOCK_Y, STORAGE_Y, SOLAR_Y};

  private final boolean solar;
  private Tab open = Tab.DOCK;

  DockScreenTabs(boolean solar) {
    this.solar = solar;
  }

  /** The tabs, from the top. */
  enum Tab {
    DOCK(DOCK_Y, ScreenKeys.DOCK_TAB_DOCK),
    STORAGE(STORAGE_Y, ScreenKeys.DOCK_TAB_STORAGE),
    SOLAR(SOLAR_Y, ScreenKeys.DOCK_TAB_SOLAR);

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
    ScreenTabStrip.renderBehind(graphics, left, top, solar ? SOLAR_TOPS : TOPS, open.y);
  }

  void renderFront(GuiGraphics graphics, int left, int top) {
    ScreenTabStrip.renderOpen(graphics, left, top, open.y);
    int iconX = left + ScreenTabStrip.ICON_X;
    ScreenSprites.dockTab(graphics, iconX, top + DOCK_Y + ScreenTabStrip.ICON_Y);
    ScreenSprites.storageTab(graphics, iconX, top + STORAGE_Y + ScreenTabStrip.ICON_Y);
    if (solar) {
      graphics.renderItem(
          new ItemStack(CubitItems.DOCK_SOLAR_PANEL.get()),
          iconX + ITEM_SHIFT,
          top + SOLAR_Y + ScreenTabStrip.ICON_Y + ITEM_SHIFT);
    }
  }

  /** The tab under a point given from the left of the tabs and the top of the window. */
  Optional<Tab> at(double x, double y) {
    return Arrays.stream(Tab.values())
        .filter(each -> solar || each != Tab.SOLAR)
        .filter(each -> ScreenTabStrip.contains(x, y, each.y))
        .findFirst();
  }
}
