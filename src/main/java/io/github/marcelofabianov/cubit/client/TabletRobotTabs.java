package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.menu.DefenseKeys;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import java.util.Arrays;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;

/**
 * The tabs of the screen of a Cubit on the Tablet, on the right of the window, from the top, with
 * the icons of the tabs of the screen of the Cubit (answers 1022 and 1037). Each tab says where its
 * part of the screen of the Cubit starts, so the slots of the menu line up under the header.
 */
final class TabletRobotTabs {
  static final int INSET = ScreenTabStrip.INSET;
  private static final int RESOURCES_Y = 9;
  private static final int DEFENSE_Y = 37;
  private static final int FILTERS_Y = 65;
  private static final int INVENTORY_Y = 93;
  private static final int CHEST_X = 6;
  private static final int CHEST_Y = 6;
  private static final int DEFENSE_TOP = 14;
  private static final int FILTERS_TOP = RobotBehaviorPanel.filterHeadingY() - 4;
  private static final int INVENTORY_TOP = 26;
  private static final int[] TOPS = {RESOURCES_Y, DEFENSE_Y, FILTERS_Y, INVENTORY_Y};

  private Tab open = Tab.RESOURCES;

  /** The tabs, from the top, and the line of the screen of the Cubit each one starts at. */
  enum Tab {
    RESOURCES(RESOURCES_Y, TabletKeys.TAB_RESOURCES, 0),
    DEFENSE(DEFENSE_Y, DefenseKeys.TAB, DEFENSE_TOP),
    FILTERS(FILTERS_Y, TabletKeys.TAB_FILTERS, FILTERS_TOP),
    INVENTORY(INVENTORY_Y, ScreenKeys.TAB_INVENTORY, INVENTORY_TOP);

    private final int y;
    private final String name;
    private final int panelTop;

    Tab(int y, String name, int panelTop) {
      this.y = y;
      this.name = name;
      this.panelTop = panelTop;
    }

    String title() {
      return name;
    }

    /** The line of the screen of the Cubit that shows at the top of the content. */
    int panelTop() {
      return panelTop;
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
    ScreenSprites.gear(graphics, iconX, top + RESOURCES_Y + ScreenTabStrip.ICON_Y);
    ScreenSprites.shield(graphics, iconX, top + DEFENSE_Y + ScreenTabStrip.ICON_Y);
    ScreenSprites.funnel(graphics, iconX, top + FILTERS_Y + ScreenTabStrip.ICON_Y);
    ScreenSprites.chest(graphics, left + CHEST_X, top + INVENTORY_Y + CHEST_Y);
  }

  /** The tab under a point given from the left of the tabs and the top of the window. */
  Optional<Tab> at(double x, double y) {
    return Arrays.stream(Tab.values())
        .filter(each -> ScreenTabStrip.contains(x, y, each.y))
        .findFirst();
  }
}
