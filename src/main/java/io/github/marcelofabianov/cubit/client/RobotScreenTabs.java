package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.menu.BehaviorKeys;
import io.github.marcelofabianov.cubit.menu.DefenseKeys;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import java.util.Arrays;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;

/**
 * The tabs on the right of the robot screen, from the top. The open tab is only a choice of the
 * client, and the server never depends on it.
 */
final class RobotScreenTabs {
  static final int INSET = 3;
  private static final int WIDTH = 26;
  private static final int HEIGHT = 24;
  private static final int INVENTORY_Y = 9;
  private static final int SETTINGS_Y = 37;
  private static final int DEFENSE_Y = 65;
  private static final int BEHAVIOR_Y = 93;
  private static final int IDLE = 0xFFA9A9A9;
  private static final int CHEST_X = 6;
  private static final int CHEST_Y = 6;
  private static final int GEAR_X = 5;
  private static final int GEAR_Y = 4;
  private static final int SHIELD_X = 5;
  private static final int SHIELD_Y = 4;
  private static final int FUNNEL_X = 5;
  private static final int FUNNEL_Y = 4;

  private Tab open = Tab.INVENTORY;

  /** The tabs, from the top. */
  enum Tab {
    INVENTORY(INVENTORY_Y, ScreenKeys.TAB_INVENTORY),
    SETTINGS(SETTINGS_Y, ScreenKeys.TAB_SETTINGS),
    DEFENSE(DEFENSE_Y, DefenseKeys.TAB),
    BEHAVIOR(BEHAVIOR_Y, BehaviorKeys.TAB);

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
    for (Tab each : Tab.values()) {
      int y = top + each.y;
      graphics.fill(left, y, left + WIDTH, y + HEIGHT, ScreenPainter.EDGE);
      graphics.fill(
          left,
          y + 1,
          left + WIDTH - 1,
          y + HEIGHT - 1,
          each == open ? ScreenPainter.WINDOW : IDLE);
    }
  }

  /** The open tab covers the border of the window, so both look like one piece. */
  void renderFront(GuiGraphics graphics, int left, int top) {
    int y = top + open.y;
    graphics.fill(left, y + 1, left + WIDTH - 1, y + HEIGHT - 1, ScreenPainter.WINDOW);
    ScreenSprites.chest(graphics, left + CHEST_X, top + INVENTORY_Y + CHEST_Y);
    ScreenSprites.gear(graphics, left + GEAR_X, top + SETTINGS_Y + GEAR_Y);
    ScreenSprites.shield(graphics, left + SHIELD_X, top + DEFENSE_Y + SHIELD_Y);
    ScreenSprites.funnel(graphics, left + FUNNEL_X, top + BEHAVIOR_Y + FUNNEL_Y);
  }

  /** The tab under a point given from the left of the tabs and the top of the window. */
  Optional<Tab> at(double x, double y) {
    return Arrays.stream(Tab.values())
        .filter(each -> x >= 0 && x < WIDTH && y >= each.y && y < each.y + HEIGHT)
        .findFirst();
  }
}
