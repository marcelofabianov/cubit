package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.client.ScreenPainter.Box;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.item.TierLook;
import io.github.marcelofabianov.cubit.menu.BehaviorKeys;
import io.github.marcelofabianov.cubit.menu.DefenseKeys;
import io.github.marcelofabianov.cubit.menu.PickupScreenValues;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;

/**
 * The behavior tab: the pickup, magnet and fetch switches and the ghost slots of the pickup filter
 * (answers 878 to 888 and 938). Without the Functions heading and with rows of 12 px, so three rows
 * of the filter fit above the player inventory, which stays shown (answers 893, 898 and 940). The
 * detail of each switch shows in its tooltip.
 */
final class RobotBehaviorPanel {
  private static final int LEFT = 7;
  private static final int RIGHT = 165;
  private static final int LINE = 9;
  private static final int DETAIL_LINE = 7;
  private static final int FIRST_ROW = 18;
  private static final int ROW_HEIGHT = 12;
  private static final int ICON_X = 11;
  private static final float ICON_SCALE = 0.75f;
  private static final int NAME_X = 30;
  private static final int NAME_TOP = 2;
  private static final int NAME_WIDTH = 106;
  private static final int TOGGLE_X = 139;
  private static final int TOGGLE_TOP = 0;
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
  private static final int PICKUP_ROW = 0;
  private static final int MAGNET_ROW = 1;
  private static final int FETCH_ROW = 2;
  private static final int[] BUTTONS = {
    RobotMenu.PICKUP_BUTTON, RobotMenu.MAGNET_BUTTON, RobotMenu.FETCH_BUTTON
  };
  private static final String[] NAMES = {
    BehaviorKeys.PICKUP, BehaviorKeys.MAGNET, BehaviorKeys.FETCH
  };

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
    for (int row = 0; row < BUTTONS.length; row++) {
      renderRow(graphics, row);
    }
    Component title = Component.translatable(BehaviorKeys.FILTER).withStyle(ChatFormatting.BOLD);
    painter.text(graphics, title, FILTER_TITLE, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
    graphics.fill(LEFT, FILTER_RULE, RIGHT, FILTER_RULE + 1, ScreenPainter.SHADE);
    graphics.fill(LEFT, FILTER_RULE + 1, RIGHT, FILTER_RULE + 2, ScreenPainter.WHITE);
    renderMode(graphics);
    renderTagMarks(graphics);
  }

  /** The funnel shrinks to the height of the row, so the three rows fit (answer 940). */
  private void renderRow(GuiGraphics graphics, int row) {
    int top = FIRST_ROW + row * ROW_HEIGHT;
    if (row % 2 == 0) {
      graphics.fill(LEFT, top, RIGHT, top + ROW_HEIGHT, ScreenPainter.ROW);
    }
    if (row == PICKUP_ROW) {
      graphics.pose().pushPose();
      graphics.pose().translate(ICON_X, top, 0);
      graphics.pose().scale(ICON_SCALE, ICON_SCALE, 1);
      ScreenSprites.funnel(graphics, 0, 0);
      graphics.pose().popPose();
    }
    Box nameBox = new Box(NAME_X, top + NAME_TOP, NAME_WIDTH, LINE);
    painter.text(
        graphics,
        Component.translatable(NAMES[row]),
        nameBox,
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    ScreenToggle.draw(graphics, painter, TOGGLE_X, top + TOGGLE_TOP, on(row), available(row));
  }

  private boolean on(int row) {
    return switch (row) {
      case MAGNET_ROW -> PickupScreenValues.magnetOn(values());
      case FETCH_ROW -> PickupScreenValues.fetchOn(values());
      default -> PickupScreenValues.pickupOn(values());
    };
  }

  private boolean available(int row) {
    return switch (row) {
      case MAGNET_ROW -> PickupScreenValues.magnetAvailable(values());
      case FETCH_ROW -> PickupScreenValues.fetchAvailable(values());
      default -> true;
    };
  }

  /** The detail of the switch under the mouse, as its tooltip, by the mouse (answer 940). */
  boolean renderDetail(GuiGraphics graphics, Font font, int mouseX, int mouseY, int left, int top) {
    Optional<Component> detail = detailAt(mouseX - left, mouseY - top);
    detail.ifPresent(text -> graphics.renderTooltip(font, text, mouseX, mouseY));
    return detail.isPresent();
  }

  private Optional<Component> detailAt(double x, double y) {
    if (x < LEFT || x >= RIGHT || y < FIRST_ROW) {
      return Optional.empty();
    }
    int row = (int) ((y - FIRST_ROW) / ROW_HEIGHT);
    return switch (row) {
      case PICKUP_ROW -> Optional.of(Component.translatable(BehaviorKeys.PICKUP_DETAIL));
      case MAGNET_ROW -> Optional.of(magnetDetail());
      case FETCH_ROW -> Optional.of(fetchDetail());
      default -> Optional.empty();
    };
  }

  /** The numbers of the tier, or the tier that unlocks the magnet (answers 881 and 888). */
  private Component magnetDetail() {
    if (!PickupScreenValues.magnetUnlocked(values())) {
      return unlocksAt(PickupScreenValues.magnetUnlockTier(values()));
    }
    return Component.translatable(
        BehaviorKeys.MAGNET_DETAIL,
        PickupScreenValues.radius(values()),
        PickupScreenValues.cost(values()).amount());
  }

  /** The radius of the tier around the owner, or the tier that unlocks fetch (answer 943). */
  private Component fetchDetail() {
    int radius = PickupScreenValues.fetchRadius(values());
    if (radius == 0) {
      return unlocksAt(PickupScreenValues.fetchUnlockTier(values()));
    }
    return Component.translatable(BehaviorKeys.FETCH_DETAIL, radius);
  }

  private static Component unlocksAt(Optional<RobotTier> tier) {
    return tier.map(unlock ->
            (Component) Component.translatable(DefenseKeys.UNLOCKS_AT, TierLook.name(unlock)))
        .orElse(Component.empty());
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
      if (ScreenToggle.contains(TOGGLE_X, top, x, y) && available(row)) {
        return OptionalInt.of(BUTTONS[row]);
      }
    }
    return OptionalInt.empty();
  }

  private static int filterTop() {
    return RobotMenu.PICKUP_FILTER_Y;
  }
}
