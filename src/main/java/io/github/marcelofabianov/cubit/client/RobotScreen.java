package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.client.ScreenPainter.Box;
import io.github.marcelofabianov.cubit.client.ScreenPainter.Colors;
import io.github.marcelofabianov.cubit.domain.behavior.RobotScreenStatus;
import io.github.marcelofabianov.cubit.domain.robot.PickupEntry;
import io.github.marcelofabianov.cubit.menu.BehaviorKeys;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * The robot screen of the concept image, with three tabs: the inventory, the settings and the
 * defense. The tab is only a choice of the client, and the server never depends on it.
 */
final class RobotScreen extends AbstractContainerScreen<RobotMenu> {
  private static final int WIDTH = 176;
  private static final int BELOW_HOTBAR = 29;
  private static final int LINE = 9;
  private static final int DETAIL_LINE = 7;
  private static final int MARGIN = 8;
  private static final Box STATUS = new Box(100, 6, 68, LINE);

  /** Narrower in the settings, to leave room for the Power button (answer 861). */
  private static final Box SETTINGS_BATTERY = new Box(100, 6, 50, LINE);

  private static final int TITLE_Y = 6;
  private static final int TITLE_WIDTH = 90;
  private static final int BADGE_GAP = 4;
  private static final int BADGE_PADDING = 3;
  private static final int BADGE_HEIGHT = 9;
  private static final int BADGE_TOP = 5;
  private static final int BADGE_TEXT_TOP = 7;
  private static final int BADGE_COLOR = 0xFF8B8B8B;
  private static final int PORTRAIT_Y = 17;
  private static final Box HEALTH = new Box(50, 18, 96, LINE);
  private static final Box HEALTH_BAR = new Box(50, 26, 96, 7);
  private static final Box BATTERY = new Box(50, 37, 96, LINE);
  private static final Box BATTERY_BAR = new Box(50, 45, 96, 7);
  private static final Box RECHARGE = new Box(50, 55, 96, DETAIL_LINE);
  private static final int BOLT_X = 156;
  private static final int BOLT_Y = 20;
  private static final int REDSTONE_CENTER = 160;
  private static final int REDSTONE_Y = 54;
  private static final int ROBOT_LABEL_Y = RobotMenu.FIRST_SLOT_Y - LINE;
  private static final int PLAYER_LABEL_GAP = 10;

  private final ScreenPainter painter = new ScreenPainter();
  private RobotSettingsPanel settings;
  private RobotDefensePanel defense;
  private RobotBehaviorPanel behavior;
  private final InventoryScrollbar scrollbar;
  private final RobotScreenTabs tabs = new RobotScreenTabs();
  private final RobotExclusiveRow exclusive;

  RobotScreen(RobotMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    scrollbar = new InventoryScrollbar(menu);
    exclusive = new RobotExclusiveRow(painter, menu);
    imageWidth = WIDTH + (scrollbar.shown() ? InventoryScrollbar.EXTRA_WIDTH : 0);
    imageHeight = menu.playerInventoryY() + RobotMenu.HOTBAR_GAP + BELOW_HOTBAR;
  }

  @Override
  protected void init() {
    super.init();
    settings = new RobotSettingsPanel(painter, menu, oreIcon(menu.opening().filterIconTag()));
    defense = new RobotDefensePanel(painter, menu);
    behavior = new RobotBehaviorPanel(painter, menu);
    showOpenTab();
  }

  /** The first block of the tag stands for the ore, as the client knows the tags. */
  private static ItemStack oreIcon(String tag) {
    ResourceLocation id = ResourceLocation.tryParse(tag);
    if (tag.isEmpty() || id == null) {
      return ItemStack.EMPTY;
    }
    TagKey<Block> key = TagKey.create(Registries.BLOCK, id);
    return BuiltInRegistries.BLOCK
        .getTag(key)
        .flatMap(blocks -> blocks.stream().findFirst())
        .map(block -> new ItemStack(block.value()))
        .orElse(ItemStack.EMPTY);
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    painter.begin(font);
    super.render(graphics, mouseX, mouseY, partialTick);
    if (!renderTagTooltip(graphics, mouseX, mouseY)
        && !exclusive.renderTooltip(graphics, font, hoveredSlot, mouseX, mouseY)) {
      renderTooltip(graphics, mouseX, mouseY);
    }
    graphics.pose().pushPose();
    graphics.pose().translate(leftPos, topPos, 0);
    painter.cutTooltip(graphics, mouseX - leftPos, mouseY - topPos);
    graphics.pose().popPose();
    renderTabTooltip(graphics, mouseX, mouseY);
  }

  /** A filter slot that holds a tag shows the tag, not the item it stands for (answer 882). */
  private boolean renderTagTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
    if (tabs.open() != RobotScreenTabs.Tab.BEHAVIOR || hoveredSlot == null) {
      return false;
    }
    Optional<String> tag =
        behavior.filterSlotOf(hoveredSlot).stream().boxed().findFirst().flatMap(behavior::tagAt);
    tag.ifPresent(name ->
        graphics.renderTooltip(
            font,
            Component.translatable(BehaviorKeys.TAG, PickupEntry.TAG_MARK + name),
            mouseX,
            mouseY));
    return tag.isPresent();
  }

  private void showOpenTab() {
    menu.showTab(
        tabs.open() == RobotScreenTabs.Tab.INVENTORY, tabs.open() == RobotScreenTabs.Tab.BEHAVIOR);
  }

  private void renderTabTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
    if (tabs.open() == RobotScreenTabs.Tab.SETTINGS
        && settings.overPower(mouseX - leftPos, mouseY - topPos)) {
      graphics.renderComponentTooltip(
          font,
          List.of(
              Component.translatable(ScreenKeys.POWER),
              Component.translatable(ScreenKeys.POWER_DETAIL)),
          mouseX,
          mouseY);
      return;
    }
    boolean detail =
        switch (tabs.open()) {
          case BEHAVIOR -> behavior.renderDetail(graphics, font, mouseX, mouseY, leftPos, topPos);
          case SETTINGS -> settings.renderDetail(graphics, font, mouseX, mouseY, leftPos, topPos);
          default -> false;
        };
    if (detail) {
      return;
    }
    tabAt(mouseX, mouseY)
        .ifPresent(under ->
            graphics.renderTooltip(font, Component.translatable(under.title()), mouseX, mouseY));
  }

  @Override
  protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
    tabs.renderBehind(graphics, leftPos + tabLeft(), topPos);
    ScreenPainter.window(graphics, leftPos, topPos, imageWidth, imageHeight);
    tabs.renderFront(graphics, leftPos + tabLeft(), topPos);
    if (tabs.open() == RobotScreenTabs.Tab.BEHAVIOR) {
      behavior.renderFrames(graphics, leftPos, topPos);
      RobotSlotFrames.player(graphics, menu, leftPos, topPos);
    }
    if (tabs.open() != RobotScreenTabs.Tab.INVENTORY) {
      return;
    }
    scrollbar.render(graphics, leftPos, topPos);
    long tick = minecraft.player == null ? 0 : minecraft.player.tickCount;
    RobotPortrait.draw(
        graphics, leftPos + MARGIN, topPos + PORTRAIT_Y, menu.expression(), menu.tier(), tick);
    ScreenSprites.bolt(graphics, leftPos + BOLT_X, topPos + BOLT_Y);
    renderSlots(graphics);
  }

  private void renderSlots(GuiGraphics graphics) {
    RobotSlotFrames.robot(graphics, menu, leftPos, topPos);
    exclusive.renderFrames(graphics, leftPos, topPos);
    RobotSlotFrames.player(graphics, menu, leftPos, topPos);
  }

  @Override
  protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    Component heading =
        tabs.open() == RobotScreenTabs.Tab.INVENTORY
            ? title
            : Component.translatable(tabs.open().title());
    int titleEnd = renderTitle(graphics, heading.copy().withStyle(ChatFormatting.BOLD));
    renderBadge(graphics, titleEnd + BADGE_GAP);
    if (tabs.open() != RobotScreenTabs.Tab.INVENTORY) {
      painter.textRight(
          graphics,
          RobotTexts.batteryPercent(menu),
          tabs.open() == RobotScreenTabs.Tab.SETTINGS ? SETTINGS_BATTERY : STATUS,
          ScreenPainter.DETAIL,
          ScreenPainter.FULL_SCALE);
      switch (tabs.open()) {
        case SETTINGS -> settings.render(graphics);
        case DEFENSE -> defense.render(graphics);
        default -> renderBehavior(graphics);
      }
      return;
    }
    RobotScreenStatus status = menu.status();
    painter.status(graphics, RobotTexts.status(menu), STATUS, RobotTexts.statusColor(status));
    renderBars(graphics, status);
    renderInventoryLabels(graphics);
  }

  private void renderBehavior(GuiGraphics graphics) {
    behavior.render(graphics);
    painter.text(
        graphics,
        playerInventoryTitle,
        MARGIN,
        menu.playerInventoryY() - PLAYER_LABEL_GAP,
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
  }

  private int renderTitle(GuiGraphics graphics, Component heading) {
    Box box = new Box(MARGIN, TITLE_Y, TITLE_WIDTH, LINE);
    painter.text(graphics, heading, box, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
    return MARGIN + Math.min(painter.width(heading, ScreenPainter.FULL_SCALE), TITLE_WIDTH);
  }

  private void renderBadge(GuiGraphics graphics, int x) {
    Component tier = Component.translatable(ScreenKeys.tier(menu.tier()));
    int width = painter.width(tier, ScreenPainter.DETAIL_SCALE) + BADGE_PADDING * 2;
    graphics.fill(x, BADGE_TOP, x + width, BADGE_TOP + BADGE_HEIGHT, BADGE_COLOR);
    painter.text(
        graphics,
        tier,
        x + BADGE_PADDING,
        BADGE_TEXT_TOP,
        ScreenPainter.WHITE,
        ScreenPainter.DETAIL_SCALE);
  }

  /** The battery is red only with a critical battery, as in the images (answer 681). */
  private void renderBars(GuiGraphics graphics, RobotScreenStatus status) {
    Component health =
        Component.translatable(ScreenKeys.HEALTH_AMOUNT, menu.health(), menu.maxHealth());
    painter.pair(
        graphics, HEALTH, Component.translatable(ScreenKeys.HEALTH), health, Colors.TITLE_FIRST);
    ScreenPainter.bar(graphics, HEALTH_BAR, menu.health(), menu.maxHealth(), ScreenPainter.RED);
    painter.pair(
        graphics,
        BATTERY,
        Component.translatable(ScreenKeys.BATTERY),
        DockTexts.amount(menu.charge(), menu.capacity()),
        Colors.TITLE_FIRST);
    int color =
        status == RobotScreenStatus.CRITICAL_BATTERY ? ScreenPainter.RED : ScreenPainter.GREEN;
    ScreenPainter.bar(
        graphics, BATTERY_BAR, menu.charge().amount(), menu.capacity().amount(), color);
    painter.text(
        graphics,
        RobotTexts.recharge(menu),
        RECHARGE,
        ScreenPainter.DETAIL,
        ScreenPainter.DETAIL_SCALE);
  }

  private void renderInventoryLabels(GuiGraphics graphics) {
    Component redstone = Component.translatable(ScreenKeys.REDSTONE);
    int width = painter.width(redstone, ScreenPainter.DETAIL_SCALE);
    painter.text(
        graphics,
        redstone,
        REDSTONE_CENTER - width / 2,
        REDSTONE_Y,
        ScreenPainter.DETAIL,
        ScreenPainter.DETAIL_SCALE);
    exclusive.renderLabels(graphics);
    Component robotLabel = Component.translatable(ScreenKeys.ROBOT_INVENTORY, title);
    painter.text(
        graphics, robotLabel, MARGIN, ROBOT_LABEL_Y, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
    painter.text(
        graphics,
        playerInventoryTitle,
        MARGIN,
        menu.playerInventoryY() - PLAYER_LABEL_GAP,
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    Optional<RobotScreenTabs.Tab> clicked = tabAt(mouseX, mouseY);
    if (clicked.isPresent()) {
      tabs.open(clicked.get());
      showOpenTab();
      return true;
    }
    if (tabs.open() == RobotScreenTabs.Tab.INVENTORY) {
      OptionalInt row = scrollbar.press(mouseX - leftPos, mouseY - topPos);
      if (row.isPresent()) {
        scrollTo(row.getAsInt());
        return true;
      }
    }
    OptionalInt pressed = pressedButton(mouseX - leftPos, mouseY - topPos);
    if (pressed.isPresent()) {
      minecraft.gameMode.handleInventoryButtonClick(menu.containerId, pressed.getAsInt());
      return true;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseDragged(
      double mouseX, double mouseY, int button, double dragX, double dragY) {
    OptionalInt row = scrollbar.drag(mouseY - topPos);
    if (row.isPresent()) {
      scrollTo(row.getAsInt());
      return true;
    }
    return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    scrollbar.release();
    return super.mouseReleased(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
    if (tabs.open() == RobotScreenTabs.Tab.INVENTORY
        && scrollbar.overInventory(mouseX - leftPos, mouseY - topPos)) {
      scrollTo(menu.topRow() - (int) Math.signum(scrollY));
      return true;
    }
    return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
  }

  /** The client shows the rows at once, and the server, which decides, follows by the button. */
  private void scrollTo(int row) {
    int clamped = menu.window().clampTopRow(row);
    if (clamped != menu.topRow()) {
      menu.scrollTo(clamped);
      minecraft.gameMode.handleInventoryButtonClick(
          menu.containerId, RobotMenu.FIRST_SCROLL_BUTTON + clamped);
    }
  }

  private int tabLeft() {
    return imageWidth - RobotScreenTabs.INSET;
  }

  /** The button under the mouse in the open tab, as the server numbers them. */
  private OptionalInt pressedButton(double x, double y) {
    return switch (tabs.open()) {
      case SETTINGS -> settings.switchAt(x, y);
      case DEFENSE -> defense.modeAt(x, y);
      case BEHAVIOR -> behavior.buttonAt(x, y);
      case INVENTORY -> OptionalInt.empty();
    };
  }

  private Optional<RobotScreenTabs.Tab> tabAt(double mouseX, double mouseY) {
    return tabs.at(mouseX - leftPos - tabLeft(), mouseY - topPos);
  }

  /** The tabs stand outside the window, and a click there must not throw the item. */
  @Override
  protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top, int button) {
    return tabAt(mouseX, mouseY).isEmpty()
        && super.hasClickedOutside(mouseX, mouseY, left, top, button);
  }
}
