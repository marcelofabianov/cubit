package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.client.ScreenPainter.Box;
import io.github.marcelofabianov.cubit.item.TierLook;
import io.github.marcelofabianov.cubit.menu.BehaviorKeys;
import io.github.marcelofabianov.cubit.menu.DefenseKeys;
import io.github.marcelofabianov.cubit.menu.PickupScreenValues;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;

/**
 * The behavior tab: the pickup and magnet switches and the ghost slots of the pickup filter
 * (answers 878 to 888). Without the Functions heading, so three rows of the filter fit above the
 * player inventory, which stays shown (answer 893).
 */
final class RobotBehaviorPanel {
  private static final int LEFT = 7;
  private static final int RIGHT = 165;
  private static final int LINE = 9;
  private static final int DETAIL_LINE = 7;
  private static final int FIRST_ROW = 18;
  private static final int ROW_HEIGHT = 18;
  private static final int ICON_X = 11;
  private static final int ICON_TOP = 1;
  private static final int NAME_X = 30;
  private static final int NAME_TOP = 3;
  private static final int DETAIL_TOP = 11;
  private static final int NAME_WIDTH = 106;
  private static final int TOGGLE_X = 139;
  private static final int TOGGLE_TOP = 4;
  private static final Box FILTER_TITLE = new Box(8, 57, 100, LINE);
  private static final int FILTER_RULE = 66;
  private static final int MODE_X = 115;
  private static final int MODE_Y = 55;
  private static final int MODE_WIDTH = 50;
  private static final int MODE_HEIGHT = 10;
  private static final int MODE_TEXT_INSET = 3;
  private static final int MARK_SIZE = 3;
  private static final int MARK_INSET = 14;
  private static final int MARK_COLOR = 0xFFF5C400;
  private static final int MAGNET_ROW = 1;
  private static final int[] BUTTONS = {RobotMenu.PICKUP_BUTTON, RobotMenu.MAGNET_BUTTON};

  private final ScreenPainter painter;
  private final RobotMenu menu;

  RobotBehaviorPanel(ScreenPainter painter, RobotMenu menu) {
    this.painter = painter;
    this.menu = menu;
  }

  private ContainerData values() {
    return menu.pickupValues();
  }

  /** In the coordinates of the window, behind the items of the ghost slots. */
  void renderFrames(GuiGraphics graphics, int left, int top) {
    int slots = PickupScreenValues.filterSlots(values());
    for (int slot = 0; slot < slots; slot++) {
      ScreenPainter.slot(
          graphics,
          left + RobotMenu.FIRST_SLOT_X - 1 + slot % RobotMenu.SLOTS_PER_ROW * RobotMenu.SLOT_SIZE,
          top + filterTop() - 1 + slot / RobotMenu.SLOTS_PER_ROW * RobotMenu.SLOT_SIZE);
    }
  }

  /** In the coordinates of the window, over the items, so the tag mark shows. */
  void render(GuiGraphics graphics) {
    renderRow(graphics, 0, BehaviorKeys.PICKUP, Component.translatable(BehaviorKeys.PICKUP_DETAIL));
    renderRow(graphics, MAGNET_ROW, BehaviorKeys.MAGNET, magnetDetail());
    Component title = Component.translatable(BehaviorKeys.FILTER).withStyle(ChatFormatting.BOLD);
    painter.text(graphics, title, FILTER_TITLE, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
    graphics.fill(LEFT, FILTER_RULE, RIGHT, FILTER_RULE + 1, ScreenPainter.SHADE);
    graphics.fill(LEFT, FILTER_RULE + 1, RIGHT, FILTER_RULE + 2, ScreenPainter.WHITE);
    renderMode(graphics);
    renderTagMarks(graphics);
  }

  private void renderRow(GuiGraphics graphics, int row, String name, Component detail) {
    int top = FIRST_ROW + row * ROW_HEIGHT;
    if (row % 2 == 0) {
      graphics.fill(LEFT, top, RIGHT, top + ROW_HEIGHT, ScreenPainter.ROW);
    }
    if (row == 0) {
      ScreenSprites.funnel(graphics, ICON_X, top + ICON_TOP);
    }
    Box nameBox = new Box(NAME_X, top + NAME_TOP, NAME_WIDTH, LINE);
    painter.text(
        graphics,
        Component.translatable(name),
        nameBox,
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    Box detailBox = new Box(NAME_X, top + DETAIL_TOP, NAME_WIDTH, DETAIL_LINE);
    painter.text(graphics, detail, detailBox, ScreenPainter.DETAIL, ScreenPainter.DETAIL_SCALE);
    boolean magnet = row == MAGNET_ROW;
    ScreenToggle.draw(
        graphics,
        painter,
        TOGGLE_X,
        top + TOGGLE_TOP,
        magnet ? PickupScreenValues.magnetOn(values()) : PickupScreenValues.pickupOn(values()),
        !magnet || PickupScreenValues.magnetAvailable(values()));
  }

  /** The numbers of the tier, or the tier that unlocks the magnet (answers 881 and 888). */
  private Component magnetDetail() {
    if (!PickupScreenValues.magnetUnlocked(values())) {
      return PickupScreenValues.magnetUnlockTier(values())
          .map(tier -> Component.translatable(DefenseKeys.UNLOCKS_AT, TierLook.name(tier)))
          .orElse(Component.empty());
    }
    return Component.translatable(
        BehaviorKeys.MAGNET_DETAIL,
        PickupScreenValues.radius(values()),
        PickupScreenValues.cost(values()).amount());
  }

  private void renderMode(GuiGraphics graphics) {
    ScreenPainter.sunken(graphics, MODE_X, MODE_Y, MODE_WIDTH, MODE_HEIGHT, ScreenPainter.WHITE);
    Component mode = Component.translatable(BehaviorKeys.mode(PickupScreenValues.mode(values())));
    painter.text(
        graphics,
        mode,
        new Box(MODE_X + MODE_TEXT_INSET, MODE_Y + 2, MODE_WIDTH - MODE_TEXT_INSET, DETAIL_LINE),
        ScreenPainter.TEXT,
        ScreenPainter.DETAIL_SCALE);
  }

  private void renderTagMarks(GuiGraphics graphics) {
    for (int slot = 0; slot < PickupScreenValues.filterSlots(values()); slot++) {
      if (tagAt(slot).isPresent()) {
        int x = RobotMenu.FIRST_SLOT_X + slot % RobotMenu.SLOTS_PER_ROW * RobotMenu.SLOT_SIZE;
        int y = filterTop() + slot / RobotMenu.SLOTS_PER_ROW * RobotMenu.SLOT_SIZE;
        graphics.fill(
            x + MARK_INSET - 1, y, x + MARK_INSET - 1 + MARK_SIZE, y + MARK_SIZE, MARK_COLOR);
      }
    }
  }

  /** The tag of a filter slot, from the item the server shows in it. */
  Optional<String> tagAt(int slot) {
    Slot shown = menu.slots.get(menu.filterFirstSlot() + slot);
    return PickupScreenValues.tagAt(values(), slot, shown.getItem());
  }

  /** The index of a filter slot, when the slot belongs to the filter. */
  OptionalInt filterSlotOf(Slot slot) {
    int index = slot.index - menu.filterFirstSlot();
    return index >= 0 && index < PickupScreenValues.MAX_FILTER_SLOTS
        ? OptionalInt.of(index)
        : OptionalInt.empty();
  }

  /** The switch or the mode button under the mouse, when it can change. */
  OptionalInt buttonAt(double x, double y) {
    if (x >= MODE_X && x < MODE_X + MODE_WIDTH && y >= MODE_Y && y < MODE_Y + MODE_HEIGHT) {
      return OptionalInt.of(RobotMenu.PICKUP_MODE_BUTTON);
    }
    for (int row = 0; row < BUTTONS.length; row++) {
      int top = FIRST_ROW + row * ROW_HEIGHT + TOGGLE_TOP;
      boolean available = row != MAGNET_ROW || PickupScreenValues.magnetAvailable(values());
      if (ScreenToggle.contains(TOGGLE_X, top, x, y) && available) {
        return OptionalInt.of(BUTTONS[row]);
      }
    }
    return OptionalInt.empty();
  }

  private static int filterTop() {
    return RobotMenu.PICKUP_FILTER_Y;
  }
}
