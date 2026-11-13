package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.client.ScreenPainter.Box;
import io.github.marcelofabianov.cubit.client.ScreenPainter.Colors;
import io.github.marcelofabianov.cubit.domain.dock.DockStatus;
import io.github.marcelofabianov.cubit.domain.energy.EnergyFormat;
import io.github.marcelofabianov.cubit.menu.InductionDockMenu;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * The dock screen of the concept image, in its three states and the two without a picture, in the
 * Dock tab, and the storage in the Storage tab (answers 956 and 960).
 */
final class InductionDockScreen extends AbstractContainerScreen<InductionDockMenu> {
  private static final int WIDTH = 176;
  private static final int HEIGHT = 262;
  private static final int MARGIN = 8;
  private static final int LINE = 9;
  private static final int DETAIL_LINE = 7;
  private static final Box TITLE = new Box(8, 6, 92, LINE);
  private static final Box STATUS = new Box(100, 6, 68, LINE);
  private static final int PICTURE_X = 8;
  private static final int TOWER_Y = 17;
  private static final int ROBOT_Y = 73;
  private static final Box STORED = new Box(50, 18, 96, LINE);
  private static final Box STOCK_BAR = new Box(50, 26, 96, 7);
  private static final Box INPUT = new Box(50, 36, 100, DETAIL_LINE);
  private static final Box LIMIT = new Box(50, 43, 100, DETAIL_LINE);
  private static final int BOLT_X = 156;
  private static final int BOLT_Y = 18;
  private static final int SLOT_FRAME_X = InductionDockMenu.SLOT_X - 1;
  private static final int SLOT_FRAME_Y = InductionDockMenu.SLOT_Y - 1;
  private static final int REDSTONE_CENTER = 160;
  private static final int REDSTONE_Y = 53;
  private static final int STORAGE_FRAME_Y = InductionDockMenu.STORAGE_Y - 1;
  private static final Box DOCKED = new Box(8, 60, 160, LINE);
  private static final int RULE_Y = 69;
  private static final Box SECTION = new Box(50, 74, 116, LINE);
  private static final Box BATTERY_BAR = new Box(50, 82, 116, 6);
  private static final Box HEALTH = new Box(50, 92, 116, LINE);
  private static final Box HEALTH_BAR = new Box(50, 100, 116, 6);
  private static final Box ROBOT_LINE = new Box(50, 109, 116, DETAIL_LINE);
  private static final Box NO_ROBOT = new Box(50, 78, 116, LINE);
  private static final Box NO_ROBOT_STATE = new Box(50, 89, 116, DETAIL_LINE);
  private static final Box NO_ROBOT_HINT = new Box(50, 97, 118, DETAIL_LINE);
  private static final int ROW_LEFT = 7;
  private static final int ROW_RIGHT = 169;
  private static final int QUEUE_TOP = 119;
  private static final int ROW_HEIGHT = 13;
  private static final Box QUEUE = new Box(11, 122, 154, LINE);
  private static final Box OWNER = new Box(11, 135, 154, LINE);
  private static final Box CALL = new Box(7, 149, 162, 16);
  private static final int INVENTORY_LABEL_Y = 169;
  private static final int ROWS = 3;
  private static final int COLUMNS = 9;
  private static final int SLOTS_FRAME_X = 7;
  private static final int HOTBAR_GAP = 58;

  private final ScreenPainter painter = new ScreenPainter();
  private final DockScreenTabs tabs = new DockScreenTabs();
  private FlatButton callButton;

  InductionDockScreen(InductionDockMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    imageWidth = WIDTH;
    imageHeight = HEIGHT;
    inventoryLabelX = MARGIN;
    inventoryLabelY = INVENTORY_LABEL_Y;
  }

  @Override
  protected void init() {
    super.init();
    Component label =
        Component.translatable(ScreenKeys.DOCK_CALL, menu.callRadius())
            .withStyle(ChatFormatting.BOLD);
    Box shown = CALL.moved(leftPos, topPos);
    callButton =
        addRenderableWidget(
            new FlatButton(shown.x(), shown.y(), shown.width(), shown.height(), label, this::call));
    showOpenTab();
  }

  private void showOpenTab() {
    boolean storage = tabs.open() == DockScreenTabs.Tab.STORAGE;
    menu.showStorage(storage);
    callButton.visible = !storage;
  }

  private void call() {
    minecraft.gameMode.handleInventoryButtonClick(menu.containerId, InductionDockMenu.CALL_BUTTON);
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    painter.begin(font);
    callButton.setMuted(noPower(menu.status()));
    super.render(graphics, mouseX, mouseY, partialTick);
    renderTooltip(graphics, mouseX, mouseY);
    graphics.pose().pushPose();
    graphics.pose().translate(leftPos, topPos, 0);
    painter.cutTooltip(graphics, mouseX - leftPos, mouseY - topPos);
    graphics.pose().popPose();
    tabAt(mouseX, mouseY)
        .ifPresent(under ->
            graphics.renderTooltip(font, Component.translatable(under.title()), mouseX, mouseY));
  }

  @Override
  protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
    tabs.renderBehind(graphics, leftPos + tabLeft(), topPos);
    ScreenPainter.window(graphics, leftPos, topPos, imageWidth, imageHeight);
    tabs.renderFront(graphics, leftPos + tabLeft(), topPos);
    renderSlots(graphics, leftPos, topPos);
    if (tabs.open() == DockScreenTabs.Tab.STORAGE) {
      renderStorageFrames(graphics, leftPos, topPos);
    } else {
      renderDockTab(graphics, leftPos, topPos);
    }
  }

  private void renderDockTab(GuiGraphics graphics, int left, int top) {
    ScreenPainter.sunken(
        graphics,
        left + PICTURE_X,
        top + TOWER_Y,
        RobotPortrait.SIZE,
        RobotPortrait.SIZE,
        ScreenPainter.PORTRAIT);
    ScreenSprites.tower(graphics, left + PICTURE_X + 1, top + TOWER_Y + 1, menu.status());
    ScreenSprites.bolt(graphics, left + BOLT_X, top + BOLT_Y);
    ScreenPainter.slot(graphics, left + SLOT_FRAME_X, top + SLOT_FRAME_Y);
    graphics.fill(
        left + MARGIN, top + RULE_Y, left + ROW_RIGHT - 1, top + RULE_Y + 1, ScreenPainter.SHADE);
    graphics.fill(
        left + ROW_LEFT,
        top + QUEUE_TOP,
        left + ROW_RIGHT,
        top + QUEUE_TOP + ROW_HEIGHT,
        ScreenPainter.ROW);
    renderPicture(graphics, left, top);
  }

  private void renderStorageFrames(GuiGraphics graphics, int left, int top) {
    for (int row = 0; row < ROWS; row++) {
      for (int column = 0; column < COLUMNS; column++) {
        ScreenPainter.slot(
            graphics,
            left + SLOTS_FRAME_X + column * ScreenPainter.SLOT_SIZE,
            top + STORAGE_FRAME_Y + row * ScreenPainter.SLOT_SIZE);
      }
    }
  }

  private void renderPicture(GuiGraphics graphics, int left, int top) {
    if (menu.hasRobot()) {
      long tick = minecraft.player == null ? 0 : minecraft.player.tickCount;
      RobotPortrait.draw(
          graphics,
          left + PICTURE_X,
          top + ROBOT_Y,
          menu.robotExpression(),
          menu.robotTier(),
          tick);
      return;
    }
    ScreenPainter.sunken(
        graphics,
        left + PICTURE_X,
        top + ROBOT_Y,
        RobotPortrait.SIZE,
        RobotPortrait.SIZE,
        ScreenPainter.WINDOW);
    ScreenSprites.emptyPlate(graphics, left + PICTURE_X + 1, top + ROBOT_Y + 1);
  }

  private void renderSlots(GuiGraphics graphics, int left, int top) {
    int firstRow = InductionDockMenu.PLAYER_INVENTORY_Y - 1;
    for (int row = 0; row < ROWS; row++) {
      for (int column = 0; column < COLUMNS; column++) {
        ScreenPainter.slot(
            graphics,
            left + SLOTS_FRAME_X + column * ScreenPainter.SLOT_SIZE,
            top + firstRow + row * ScreenPainter.SLOT_SIZE);
      }
    }
    for (int column = 0; column < COLUMNS; column++) {
      ScreenPainter.slot(
          graphics,
          left + SLOTS_FRAME_X + column * ScreenPainter.SLOT_SIZE,
          top + firstRow + HOTBAR_GAP);
    }
  }

  @Override
  protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    if (tabs.open() == DockScreenTabs.Tab.STORAGE) {
      Component storage =
          Component.translatable(ScreenKeys.DOCK_TAB_STORAGE).withStyle(ChatFormatting.BOLD);
      painter.text(graphics, storage, TITLE, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
    } else {
      renderDockLabels(graphics);
    }
    painter.text(
        graphics,
        playerInventoryTitle,
        inventoryLabelX,
        inventoryLabelY,
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
  }

  private void renderDockLabels(GuiGraphics graphics) {
    Component bold = title.copy().withStyle(ChatFormatting.BOLD);
    painter.text(graphics, bold, TITLE, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
    DockStatus status = menu.status();
    painter.status(
        graphics, Component.translatable(ScreenKeys.dockStatus(status)), STATUS, colorOf(status));
    renderStock(graphics);
    renderRedstoneLabel(graphics);
    painter.text(
        graphics,
        Component.translatable(ScreenKeys.DOCK_DOCKED).withStyle(ChatFormatting.BOLD),
        DOCKED,
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    if (menu.hasRobot()) {
      renderRobot(graphics);
    } else {
      renderNoRobot(graphics, status);
    }
    renderRows(graphics);
  }

  private void renderStock(GuiGraphics graphics) {
    painter.pair(
        graphics,
        STORED,
        Component.translatable(ScreenKeys.DOCK_STORED_ENERGY),
        DockTexts.amount(menu.stock(), menu.capacity()),
        Colors.TITLE_FIRST);
    ScreenPainter.bar(
        graphics, STOCK_BAR, menu.stock().amount(), menu.capacity().amount(), ScreenPainter.ORANGE);
    painter.text(
        graphics, DockTexts.input(menu), INPUT, ScreenPainter.DETAIL, ScreenPainter.DETAIL_SCALE);
    Component limit =
        Component.translatable(ScreenKeys.DOCK_LIMIT, EnergyFormat.rate(menu.inputLimit()));
    painter.text(graphics, limit, LIMIT, ScreenPainter.DETAIL, ScreenPainter.DETAIL_SCALE);
  }

  private void renderRedstoneLabel(GuiGraphics graphics) {
    Component label = Component.translatable(ScreenKeys.REDSTONE);
    int width = painter.width(label, ScreenPainter.DETAIL_SCALE);
    painter.text(
        graphics,
        label,
        REDSTONE_CENTER - width / 2,
        REDSTONE_Y,
        ScreenPainter.DETAIL,
        ScreenPainter.DETAIL_SCALE);
  }

  private void renderRobot(GuiGraphics graphics) {
    painter.pair(
        graphics,
        SECTION,
        Component.translatable(ScreenKeys.DOCK_ROBOT_BATTERY),
        DockTexts.amount(menu.robotCharge(), menu.robotCapacity()),
        Colors.TITLE_FIRST);
    ScreenPainter.bar(
        graphics,
        BATTERY_BAR,
        menu.robotCharge().amount(),
        menu.robotCapacity().amount(),
        ScreenPainter.GREEN);
    Component health =
        Component.translatable(ScreenKeys.HEALTH_AMOUNT, menu.robotHealth(), menu.robotMaxHealth());
    painter.pair(
        graphics, HEALTH, Component.translatable(ScreenKeys.HEALTH), health, Colors.TITLE_FIRST);
    ScreenPainter.bar(
        graphics, HEALTH_BAR, menu.robotHealth(), menu.robotMaxHealth(), ScreenPainter.RED);
    painter.text(
        graphics,
        DockTexts.robotLine(menu),
        ROBOT_LINE,
        ScreenPainter.DETAIL,
        ScreenPainter.DETAIL_SCALE);
  }

  private void renderNoRobot(GuiGraphics graphics, DockStatus status) {
    painter.text(
        graphics,
        Component.translatable(ScreenKeys.DOCK_NO_ROBOT).withStyle(ChatFormatting.BOLD),
        NO_ROBOT,
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    String state = noPower(status) ? ScreenKeys.DOCK_NEEDS_ENERGY : ScreenKeys.DOCK_READY;
    painter.text(
        graphics,
        Component.translatable(state),
        NO_ROBOT_STATE,
        ScreenPainter.DETAIL,
        ScreenPainter.DETAIL_SCALE);
    painter.text(
        graphics,
        Component.translatable(ScreenKeys.DOCK_PLACE_HINT),
        NO_ROBOT_HINT,
        ScreenPainter.DETAIL,
        ScreenPainter.DETAIL_SCALE);
  }

  private void renderRows(GuiGraphics graphics) {
    painter.pair(
        graphics,
        QUEUE,
        Component.translatable(ScreenKeys.DOCK_QUEUE),
        DockTexts.queue(menu.waiting()).withStyle(ChatFormatting.BOLD),
        Colors.VALUE_FIRST);
    painter.pair(
        graphics,
        OWNER,
        Component.translatable(ScreenKeys.DOCK_OWNER),
        DockTexts.owner(menu).withStyle(ChatFormatting.BOLD),
        Colors.VALUE_FIRST);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    Optional<DockScreenTabs.Tab> clicked = tabAt(mouseX, mouseY);
    if (clicked.isPresent()) {
      tabs.open(clicked.get());
      showOpenTab();
      return true;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  /** The tabs stand outside the window, and a click there must not throw the item. */
  @Override
  protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top, int button) {
    return tabAt(mouseX, mouseY).isEmpty()
        && super.hasClickedOutside(mouseX, mouseY, left, top, button);
  }

  private Optional<DockScreenTabs.Tab> tabAt(double mouseX, double mouseY) {
    return tabs.at(mouseX - leftPos - tabLeft(), mouseY - topPos);
  }

  private int tabLeft() {
    return imageWidth - ScreenTabStrip.INSET;
  }

  private static boolean noPower(DockStatus status) {
    return status == DockStatus.OFF || status == DockStatus.NO_ENERGY;
  }

  private static int colorOf(DockStatus status) {
    return switch (status) {
      case CHARGING, COMPLETE -> ScreenPainter.STATUS_GREEN;
      case IDLE -> ScreenPainter.STATUS_BLUE;
      case OFF, NO_ENERGY -> ScreenPainter.STATUS_ORANGE;
    };
  }
}
