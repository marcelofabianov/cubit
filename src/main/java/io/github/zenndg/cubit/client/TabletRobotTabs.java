package io.github.zenndg.cubit.client;

import io.github.zenndg.cubit.menu.DeathKeys;
import io.github.zenndg.cubit.menu.DefenseKeys;
import io.github.zenndg.cubit.menu.ScreenKeys;
import io.github.zenndg.cubit.menu.TabletKeys;
import io.github.zenndg.cubit.menu.WorkKeys;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

/**
 * The tabs of the screen of a Cubit on the Tablet, on the right of the window, from the top, with
 * the icons of the tabs of the screen of the Cubit and the Deaths tab (answers 1022, 1037 and
 * 1119). Each tab says where its part of the screen of the Cubit starts, so the slots of the menu
 * line up under the header.
 */
final class TabletRobotTabs {
  static final int INSET = ScreenTabStrip.INSET;
  private static final int RESOURCES_Y = 9;
  private static final int DEFENSE_Y = 37;
  private static final int FILTERS_Y = 65;
  private static final int INVENTORY_Y = 93;
  private static final int DEATHS_Y = 121;
  private static final int WORK_Y = 149;
  private static final int SUPPLY_Y = 177;
  private static final int SUPPLY_TOP = 44;
  private static final int CHEST_X = 6;
  private static final int CHEST_Y = 6;
  private static final int DEFENSE_TOP = 14;
  private static final int FILTERS_TOP = RobotBehaviorPanel.filterHeadingY() - 4;
  private static final int INVENTORY_TOP = 26;
  private static final int[] TOPS = {
    RESOURCES_Y, DEFENSE_Y, FILTERS_Y, INVENTORY_Y, DEATHS_Y, WORK_Y
  };
  private static final int[] TOPS_WITH_SUPPLY = {
    RESOURCES_Y, DEFENSE_Y, FILTERS_Y, INVENTORY_Y, DEATHS_Y, WORK_Y, SUPPLY_Y
  };

  private final BooleanSupplier supplyShown;
  private Tab open = Tab.RESOURCES;

  /** The Supply tab only shows with the card of the Organizer (answer 1303). */
  TabletRobotTabs(BooleanSupplier supplyShown) {
    this.supplyShown = supplyShown;
  }

  /** The tabs, from the top, and the line of the screen of the Cubit each one starts at. */
  enum Tab {
    RESOURCES(RESOURCES_Y, TabletKeys.TAB_RESOURCES, 0),
    DEFENSE(DEFENSE_Y, DefenseKeys.TAB, DEFENSE_TOP),
    FILTERS(FILTERS_Y, TabletKeys.TAB_FILTERS, FILTERS_TOP),
    INVENTORY(INVENTORY_Y, ScreenKeys.TAB_INVENTORY, INVENTORY_TOP),
    DEATHS(DEATHS_Y, DeathKeys.TAB_TITLE, 0),
    WORK(WORK_Y, WorkKeys.TAB, 0),
    SUPPLY(SUPPLY_Y, WorkKeys.SUPPLY_TAB, SUPPLY_TOP);

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
    ScreenTabStrip.renderBehind(
        graphics, left, top, supplyShown.getAsBoolean() ? TOPS_WITH_SUPPLY : TOPS, open.y);
  }

  void renderFront(GuiGraphics graphics, int left, int top) {
    ScreenTabStrip.renderOpen(graphics, left, top, open.y);
    int iconX = left + ScreenTabStrip.ICON_X;
    ScreenSprites.gear(graphics, iconX, top + RESOURCES_Y + ScreenTabStrip.ICON_Y);
    ScreenSprites.shield(graphics, iconX, top + DEFENSE_Y + ScreenTabStrip.ICON_Y);
    ScreenSprites.funnel(graphics, iconX, top + FILTERS_Y + ScreenTabStrip.ICON_Y);
    ScreenSprites.chest(graphics, left + CHEST_X, top + INVENTORY_Y + CHEST_Y);
    ScreenSprites.deathsTab(graphics, iconX, top + DEATHS_Y + ScreenTabStrip.ICON_Y);
    ScreenSprites.workTab(graphics, iconX, top + WORK_Y + ScreenTabStrip.ICON_Y);
    if (supplyShown.getAsBoolean()) {
      graphics.renderItem(
          new ItemStack(CubitItems.ORGANIZER_CARD.get()),
          iconX,
          top + SUPPLY_Y + ScreenTabStrip.ICON_Y);
    }
  }

  /** The tab under a point given from the left of the tabs and the top of the window. */
  Optional<Tab> at(double x, double y) {
    return Arrays.stream(Tab.values())
        .filter(each -> each != Tab.SUPPLY || supplyShown.getAsBoolean())
        .filter(each -> ScreenTabStrip.contains(x, y, each.y))
        .findFirst();
  }
}
