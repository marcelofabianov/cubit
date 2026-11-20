package io.github.zenndg.cubit.client;

import io.github.zenndg.cubit.menu.BehaviorKeys;
import io.github.zenndg.cubit.menu.DefenseKeys;
import io.github.zenndg.cubit.menu.ScreenKeys;
import io.github.zenndg.cubit.menu.WorkKeys;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

/**
 * The tabs on the right of the robot screen, from the top. The open tab is only a choice of the
 * client, and the server never depends on it.
 */
final class RobotScreenTabs {
  static final int INSET = ScreenTabStrip.INSET;
  private static final int INVENTORY_Y = 9;
  private static final int SETTINGS_Y = 37;
  private static final int DEFENSE_Y = 65;
  private static final int BEHAVIOR_Y = 93;
  private static final int WORK_Y = 121;
  private static final int SUPPLY_Y = 149;
  private static final int CHEST_X = 6;
  private static final int CHEST_Y = 6;
  private static final int[] TOPS = {INVENTORY_Y, SETTINGS_Y, DEFENSE_Y, BEHAVIOR_Y, WORK_Y};
  private static final int[] TOPS_WITH_SUPPLY = {
    INVENTORY_Y, SETTINGS_Y, DEFENSE_Y, BEHAVIOR_Y, WORK_Y, SUPPLY_Y
  };

  private final BooleanSupplier supplyShown;
  private Tab open = Tab.INVENTORY;

  /** The Supply tab only shows with the card of the Organizer (answer 1303). */
  RobotScreenTabs(BooleanSupplier supplyShown) {
    this.supplyShown = supplyShown;
  }

  /** The tabs, from the top. */
  enum Tab {
    INVENTORY(INVENTORY_Y, ScreenKeys.TAB_INVENTORY),
    SETTINGS(SETTINGS_Y, ScreenKeys.TAB_SETTINGS),
    DEFENSE(DEFENSE_Y, DefenseKeys.TAB),
    BEHAVIOR(BEHAVIOR_Y, BehaviorKeys.TAB),
    WORK(WORK_Y, WorkKeys.TAB),
    SUPPLY(SUPPLY_Y, WorkKeys.SUPPLY_TAB);

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

  /** Behind the window, so the window border runs under the closed tabs. */
  void renderBehind(GuiGraphics graphics, int left, int top) {
    ScreenTabStrip.renderBehind(
        graphics, left, top, supplyShown.getAsBoolean() ? TOPS_WITH_SUPPLY : TOPS, open.y);
  }

  /** The open tab covers the border of the window, so both look like one piece. */
  void renderFront(GuiGraphics graphics, int left, int top) {
    ScreenTabStrip.renderOpen(graphics, left, top, open.y);
    int iconX = left + ScreenTabStrip.ICON_X;
    ScreenSprites.chest(graphics, left + CHEST_X, top + INVENTORY_Y + CHEST_Y);
    ScreenSprites.gear(graphics, iconX, top + SETTINGS_Y + ScreenTabStrip.ICON_Y);
    ScreenSprites.shield(graphics, iconX, top + DEFENSE_Y + ScreenTabStrip.ICON_Y);
    ScreenSprites.funnel(graphics, iconX, top + BEHAVIOR_Y + ScreenTabStrip.ICON_Y);
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
