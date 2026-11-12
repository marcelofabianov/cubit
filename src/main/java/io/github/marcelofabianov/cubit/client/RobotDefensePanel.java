package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.client.ScreenPainter.Box;
import io.github.marcelofabianov.cubit.client.ScreenPainter.Colors;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.item.TierLook;
import io.github.marcelofabianov.cubit.menu.DefenseKeys;
import io.github.marcelofabianov.cubit.menu.DefenseOpening;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * The defense tab of the robot screen: the three modes, with the current one marked and the locked
 * ones dimmed, and the numbers of the shock of the tier (answers 829 and 833).
 */
final class RobotDefensePanel {
  private static final int LEFT = 7;
  private static final int RIGHT = 165;
  private static final int LINE = 9;
  private static final int DETAIL_LINE = 7;
  private static final Box MODES_TITLE = new Box(8, 18, 150, LINE);
  private static final int MODES_RULE = 27;
  private static final int FIRST_MODE = 31;
  private static final int MODE_HEIGHT = 20;
  private static final int MARK_X = 12;
  private static final int MARK_TOP = 5;
  private static final int MARK_SIZE = 10;
  private static final int MARK_DOT = 3;
  private static final int NAME_X = 30;
  private static final int NAME_TOP = 3;
  private static final int DETAIL_TOP = 12;
  private static final int NAME_WIDTH = 132;
  private static final Box SHOCK_TITLE = new Box(8, 96, 150, LINE);
  private static final int SHOCK_RULE = 105;
  private static final int FIRST_ROW = 109;
  private static final int ROW_HEIGHT = 14;
  private static final int ROW_TEXT_TOP = 3;
  private static final int ROW_LABEL_X = 10;
  private static final int ROW_WIDTH = 155;
  private static final int CURRENT = 0xFF4CAF50;
  private static final int DISABLED = 0x88C6C6C6;
  private static final double TICKS_PER_SECOND = 20;
  private static final String SECONDS_PATTERN = "0.#";
  private static final int DAMAGE_ROW = 0;
  private static final int RANGE_ROW = 1;
  private static final int INTERVAL_ROW = 2;
  private static final int COST_ROW = 3;
  private static final int TARGETS_ROW = 4;

  private final ScreenPainter painter;
  private final RobotMenu menu;

  RobotDefensePanel(ScreenPainter painter, RobotMenu menu) {
    this.painter = painter;
    this.menu = menu;
  }

  /** Drawn in the coordinates of the window. */
  void render(GuiGraphics graphics) {
    heading(graphics, DefenseKeys.MODES, MODES_TITLE, MODES_RULE);
    for (DefenseMode mode : DefenseMode.values()) {
      renderMode(graphics, mode, FIRST_MODE + mode.ordinal() * MODE_HEIGHT);
    }
    heading(graphics, DefenseKeys.SHOCK, SHOCK_TITLE, SHOCK_RULE);
    renderShock(graphics);
  }

  /** The button of the mode under the mouse, when the tier allows it and it is not the current. */
  OptionalInt modeAt(double x, double y) {
    for (DefenseMode mode : DefenseMode.values()) {
      int top = FIRST_MODE + mode.ordinal() * MODE_HEIGHT;
      boolean inside = x >= LEFT && x < RIGHT && y >= top && y < top + MODE_HEIGHT;
      if (inside && defense().allows(mode) && mode != menu.defenseMode()) {
        return OptionalInt.of(RobotMenu.FIRST_DEFENSE_BUTTON + mode.ordinal());
      }
    }
    return OptionalInt.empty();
  }

  private DefenseOpening defense() {
    return menu.opening().defense();
  }

  private void heading(GuiGraphics graphics, String key, Box box, int rule) {
    Component title = Component.translatable(key).withStyle(ChatFormatting.BOLD);
    painter.text(graphics, title, box, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
    graphics.fill(LEFT, rule, RIGHT, rule + 1, ScreenPainter.SHADE);
    graphics.fill(LEFT, rule + 1, RIGHT, rule + 2, ScreenPainter.WHITE);
  }

  private void renderMode(GuiGraphics graphics, DefenseMode mode, int top) {
    if (mode.ordinal() % 2 == 0) {
      graphics.fill(LEFT, top, RIGHT, top + MODE_HEIGHT, ScreenPainter.ROW);
    }
    boolean current = mode == menu.defenseMode();
    renderMark(graphics, top, current);
    Component name = Component.translatable(DefenseKeys.mode(mode));
    painter.text(
        graphics,
        current ? name.copy().withStyle(ChatFormatting.BOLD) : name,
        new Box(NAME_X, top + NAME_TOP, NAME_WIDTH, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    painter.text(
        graphics,
        detailOf(mode),
        new Box(NAME_X, top + DETAIL_TOP, NAME_WIDTH, DETAIL_LINE),
        ScreenPainter.DETAIL,
        ScreenPainter.DETAIL_SCALE);
    if (!defense().allows(mode)) {
      graphics.fill(LEFT, top, RIGHT, top + MODE_HEIGHT, DISABLED);
    }
  }

  /** A round choice, as the game has none: a sunken square, with a green dot on the current. */
  private static void renderMark(GuiGraphics graphics, int top, boolean current) {
    ScreenPainter.sunken(
        graphics, MARK_X, top + MARK_TOP, MARK_SIZE, MARK_SIZE, ScreenPainter.WHITE);
    if (current) {
      graphics.fill(
          MARK_X + MARK_DOT,
          top + MARK_TOP + MARK_DOT,
          MARK_X + MARK_SIZE - MARK_DOT,
          top + MARK_TOP + MARK_SIZE - MARK_DOT,
          CURRENT);
    }
  }

  /** A locked mode says which tier unlocks it, instead of what it does. */
  private Component detailOf(DefenseMode mode) {
    if (defense().allows(mode)) {
      return Component.translatable(DefenseKeys.detail(mode));
    }
    return unlocks(defense().unlockTier(mode));
  }

  private static Component unlocks(Optional<RobotTier> tier) {
    return tier.map(at -> Component.translatable(DefenseKeys.UNLOCKS_AT, TierLook.name(at)))
        .orElse(Component.translatable(DefenseKeys.NO_SHOCK));
  }

  private void renderShock(GuiGraphics graphics) {
    DefenseOpening shock = defense();
    if (!shock.canShock()) {
      row(
          graphics,
          DAMAGE_ROW,
          DefenseKeys.SHOCK,
          unlocks(shock.unlockTier(DefenseMode.RETALIATE)));
      return;
    }
    row(
        graphics,
        DAMAGE_ROW,
        DefenseKeys.DAMAGE,
        Component.literal(String.valueOf(shock.damage())));
    row(
        graphics,
        RANGE_ROW,
        DefenseKeys.RANGE,
        Component.translatable(DefenseKeys.RANGE_VALUE, shock.rangeBlocks()));
    renderTiming(graphics, shock);
  }

  /** How often, how much energy and how many targets. */
  private void renderTiming(GuiGraphics graphics, DefenseOpening shock) {
    row(
        graphics,
        INTERVAL_ROW,
        DefenseKeys.INTERVAL,
        Component.translatable(DefenseKeys.INTERVAL_VALUE, seconds(shock.intervalTicks())));
    row(
        graphics,
        COST_ROW,
        DefenseKeys.COST,
        Component.translatable(DefenseKeys.COST_VALUE, shock.cost()));
    row(
        graphics,
        TARGETS_ROW,
        DefenseKeys.TARGETS,
        Component.translatable(
            shock.hitsAllInRange() ? DefenseKeys.ALL_IN_RANGE : DefenseKeys.NEAREST));
  }

  private static String seconds(int ticks) {
    return new DecimalFormat(SECONDS_PATTERN).format(ticks / TICKS_PER_SECOND);
  }

  private void row(GuiGraphics graphics, int row, String label, Component value) {
    int top = FIRST_ROW + row * ROW_HEIGHT;
    if (row % 2 == 0) {
      graphics.fill(LEFT, top, RIGHT, top + ROW_HEIGHT, ScreenPainter.ROW);
    }
    Box box = new Box(ROW_LABEL_X, top + ROW_TEXT_TOP, ROW_WIDTH - ROW_LABEL_X, LINE);
    painter.pair(
        graphics,
        box,
        Component.translatable(label),
        value.copy().withStyle(ChatFormatting.BOLD),
        Colors.VALUE_FIRST);
  }
}
