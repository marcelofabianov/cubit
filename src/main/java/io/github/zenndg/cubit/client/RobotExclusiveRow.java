package io.github.zenndg.cubit.client;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.item.JobCardItem;
import io.github.zenndg.cubit.item.TierLook;
import io.github.zenndg.cubit.menu.DefenseKeys;
import io.github.zenndg.cubit.menu.RobotMenu;
import io.github.zenndg.cubit.menu.ScreenKeys;
import io.github.zenndg.cubit.menu.WorkKeys;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;

/**
 * The row of the exclusive slots, above the inventory of the robot: the torch slot, dimmed on a
 * tier without torches, after the energy slot on the Tablet, the card slot and, with the card of
 * the Farmer, its fertilizer slot (answers 980, 984, 1104, 1162 and 1244).
 */
final class RobotExclusiveRow {
  private static final int MARGIN = 8;
  private static final int LINE = 9;
  private static final int TITLE_Y = RobotMenu.EXCLUSIVE_SLOT_Y - LINE;
  private static final int SLOT_SIZE = 18;
  private static final int ICON_SIZE = 16;
  private static final String ICON_PATH = "textures/gui/slot/%s.png";
  private static final ResourceLocation ENERGY_ICON = icon("slot_energy");
  private static final ResourceLocation TORCHES_ICON = icon("slot_torches");
  private static final ResourceLocation CARD_ICON = icon("slot_card");
  private static final ResourceLocation FERTILIZER_ICON = icon("slot_fertilizer");

  private final ScreenPainter painter;
  private final RobotMenu menu;

  RobotExclusiveRow(ScreenPainter painter, RobotMenu menu) {
    this.painter = painter;
    this.menu = menu;
  }

  /** In the coordinates of the screen, behind the items. */
  void renderFrames(GuiGraphics graphics, int left, int top) {
    int x = left + menu.layout().torchSlotX() - 1;
    int y = top + RobotMenu.EXCLUSIVE_SLOT_Y - 1;
    ScreenPainter.slot(graphics, x, y);
    if (!unlocked()) {
      graphics.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, ScreenToggle.DISABLED);
    }
    int cardX = left + menu.layout().cardSlotX() - 1;
    ScreenPainter.slot(graphics, cardX, y);
    if (!menu.cardsUnlocked()) {
      graphics.fill(cardX, y, cardX + SLOT_SIZE, y + SLOT_SIZE, ScreenToggle.DISABLED);
    }
    if (menu.getSlot(menu.fertilizerSlotIndex()).isActive()) {
      int fertilizerX = left + menu.layout().fertilizerSlotX() - 1;
      ScreenPainter.slot(graphics, fertilizerX, y);
      if (!menu.fertilizerUnlocked()) {
        graphics.fill(
            fertilizerX, y, fertilizerX + SLOT_SIZE, y + SLOT_SIZE, ScreenToggle.DISABLED);
      }
    }
    renderIcons(graphics, left, top);
  }

  /**
   * The gray outline of what goes in each empty exclusive slot, as the armor slots of the game show
   * (answers 1256 and 1257), drawn over the frames and under the items.
   */
  private void renderIcons(GuiGraphics graphics, int left, int top) {
    icon(graphics, left, top, menu.energySlotIndex(), ENERGY_ICON);
    icon(graphics, left, top, menu.torchSlotIndex(), TORCHES_ICON);
    icon(graphics, left, top, menu.cardSlotIndex(), CARD_ICON);
    icon(graphics, left, top, menu.fertilizerSlotIndex(), FERTILIZER_ICON);
  }

  private void icon(GuiGraphics graphics, int left, int top, int index, ResourceLocation texture) {
    Slot slot = menu.getSlot(index);
    if (slot.isActive() && !slot.hasItem()) {
      graphics.blit(
          texture, left + slot.x, top + slot.y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
    }
  }

  /** In the coordinates of the window. */
  void renderLabels(GuiGraphics graphics) {
    Component title =
        Component.translatable(ScreenKeys.EXCLUSIVE_SLOTS).withStyle(ChatFormatting.BOLD);
    painter.text(graphics, title, MARGIN, TITLE_Y, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
  }

  /** An empty torch slot names itself, or the tier that unlocks it, as the energy slot does. */
  boolean renderTooltip(GuiGraphics graphics, Font font, Slot hovered, int mouseX, int mouseY) {
    if (hovered == null || hovered.hasItem()) {
      return false;
    }
    if (hovered.index == menu.energySlotIndex()) {
      graphics.renderTooltip(font, Component.translatable(ScreenKeys.REDSTONE), mouseX, mouseY);
      return true;
    }
    if (hovered.index == menu.cardSlotIndex()) {
      graphics.renderTooltip(font, cardHint(), mouseX, mouseY);
      return true;
    }
    if (hovered.index == menu.fertilizerSlotIndex()) {
      graphics.renderTooltip(font, fertilizerHint(), mouseX, mouseY);
      return true;
    }
    if (hovered.index != menu.torchSlotIndex()) {
      return false;
    }
    Component text =
        unlocked()
            ? Component.translatable(ScreenKeys.TORCHES)
            : menu.torchUnlockTier()
                .map(tier ->
                    (Component) Component.translatable(DefenseKeys.UNLOCKS_AT, TierLook.name(tier)))
                .orElse(Component.translatable(ScreenKeys.TORCHES));
    graphics.renderTooltip(font, text, mouseX, mouseY);
    return true;
  }

  /**
   * The empty card slot names itself, or the tier that unlocks it. With a card in hand that this
   * tier does not take, it says the tier the card asks for (answer 1162).
   */
  private Component cardHint() {
    if (!menu.cardsUnlocked()) {
      return unlocksAt(menu.cardUnlockTier(), DefenseKeys.UNLOCKS_AT);
    }
    Optional<RobotJob> held = JobCardItem.jobOf(menu.getCarried());
    if (held.isPresent() && !menu.acceptsJob(held.get())) {
      return unlocksAt(menu.tierFor(held.get()), WorkKeys.CARD_NEEDS_TIER);
    }
    return Component.translatable(WorkKeys.CARD_SLOT);
  }

  /** The empty fertilizer slot names itself, or the tier that unlocks it (answer 1244). */
  private Component fertilizerHint() {
    if (menu.fertilizerUnlocked()) {
      return Component.translatable(WorkKeys.FERTILIZER_SLOT);
    }
    return menu.fertilizerUnlockTier()
        .map(
            tier -> (Component) Component.translatable(DefenseKeys.UNLOCKS_AT, TierLook.name(tier)))
        .orElseGet(() -> Component.translatable(WorkKeys.FERTILIZER_SLOT));
  }

  private static Component unlocksAt(Optional<RobotTier> tier, String key) {
    return tier.map(found -> (Component) Component.translatable(key, TierLook.name(found)))
        .orElseGet(() -> Component.translatable(WorkKeys.CARD_SLOT));
  }

  private static ResourceLocation icon(String name) {
    return ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, ICON_PATH.formatted(name));
  }

  private boolean unlocked() {
    return menu.switchAvailable(RobotMenu.TORCHES_BUTTON);
  }
}
