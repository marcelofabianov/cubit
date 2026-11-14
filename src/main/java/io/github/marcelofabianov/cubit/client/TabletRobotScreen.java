package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.domain.robot.RobotName;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import io.github.marcelofabianov.cubit.menu.TabletRobotMenu;
import io.github.marcelofabianov.cubit.network.CubitNetwork;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * The screen of a Cubit on the Tablet: a big window with the header fixed on top and the tabs
 * Features, Defense, Filters and Inventory below (answers 1022, 1035 and 1037). Each tab shows a
 * part of the screen of the Cubit, so the slots of the menu line up with it under the header.
 */
final class TabletRobotScreen extends AbstractContainerScreen<TabletRobotMenu> {
  private static final int WIDTH = TabletRobotHeader.WIDTH;
  private static final int MARGIN = TabletRobotHeader.MARGIN;

  /** The panels draw from x 7, so the content lines up with the margin of the header. */
  private static final int CONTENT_X = 1;

  private static final int BOTTOM = 6;

  /** The screen of the Cubit is as wide as the Base one, plus the columns of its tier. */
  private static final int ROBOT_SCREEN_WIDTH = 176;

  private static final int LINE = 9;
  private static final int PLAYER_LABEL_GAP = 10;
  private static final int ROBOT_LABEL_Y = RobotMenu.FIRST_SLOT_Y - LINE;
  private static final String NO_NAME = "";

  private final ScreenPainter painter = new ScreenPainter();
  private final ScreenPainter headerPainter = new ScreenPainter();
  private final TabletRobotTabs tabs = new TabletRobotTabs();
  private final TabletRobotHeader header;
  private final TabletHeaderButtons buttons;
  private final RobotExclusiveRow exclusive;
  private TabletResourcesPanel resources;
  private RobotDefensePanel defense;
  private RobotBehaviorPanel behavior;
  private TabletRenameDialog dialog;
  private Component shownName;
  private int windowLeft;
  private int windowTop;

  TabletRobotScreen(TabletRobotMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    header = new TabletRobotHeader(headerPainter, menu);
    buttons = new TabletHeaderButtons(menu);
    exclusive = new RobotExclusiveRow(painter, menu);
    shownName = title;
    imageWidth = Math.max(WIDTH, CONTENT_X + ROBOT_SCREEN_WIDTH + menu.extraWidth());
    imageHeight =
        TabletRobotHeader.HEIGHT
            + menu.playerInventoryY()
            + RobotMenu.HOTBAR_GAP
            + RobotMenu.SLOT_SIZE
            - TabletRobotTabs.Tab.FILTERS.panelTop()
            + BOTTOM;
  }

  @Override
  protected void init() {
    super.init();
    windowLeft = leftPos;
    windowTop = topPos;
    int right = imageWidth - MARGIN - CONTENT_X;
    resources = new TabletResourcesPanel(painter, menu, right);
    defense = new RobotDefensePanel(painter, menu, right);
    behavior = new RobotBehaviorPanel(painter, menu, right);
    dialog = new TabletRenameDialog(font, width, height, this::rename);
    addButtons();
    showOpenTab();
  }

  private void addButtons() {
    buttons.add(
        this::addRenderableWidget,
        windowLeft,
        windowTop,
        new TabletHeaderButtons.Actions(
            this::back,
            button -> minecraft.gameMode.handleInventoryButtonClick(menu.containerId, button),
            () -> dialog.open(typedName())));
  }

  /** The part of the screen of the Cubit that the open tab shows starts under the header. */
  private void showOpenTab() {
    TabletRobotTabs.Tab open = tabs.open();
    leftPos = windowLeft + CONTENT_X;
    topPos = windowTop + TabletRobotHeader.HEIGHT - open.panelTop();
    menu.showTabletTab(open == TabletRobotTabs.Tab.INVENTORY, open == TabletRobotTabs.Tab.FILTERS);
  }

  /** The menu closes on the server first, then the list comes back. */
  private void back() {
    minecraft.player.closeContainer();
    CubitNetwork.requestRobotList();
  }

  /** The server checks and names. The header follows at once, with the same rule. */
  private void rename(String typed) {
    CubitNetwork.renameRobot(menu.tablet().robot(), typed);
    RobotName.typed(typed)
        .ifPresent(
            name -> shownName = name.removes() ? defaultName() : Component.literal(name.value()));
  }

  private static Component defaultName() {
    return Component.translatable(CubitEntities.GENERAL_ROBOT.get().getDescriptionId());
  }

  /** A Cubit without a name opens the field empty, so Confirm does not name it Cubit. */
  private String typedName() {
    String shown = shownName.getString();
    return shown.equals(defaultName().getString()) ? NO_NAME : shown;
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    painter.begin(font);
    headerPainter.begin(font);
    buttons.update(!dialog.isOpen());
    super.render(graphics, mouseX, mouseY, partialTick);
    if (dialog.isOpen()) {
      dialog.render(graphics, width, height, mouseX, mouseY, partialTick);
      return;
    }
    if (tabs.open() != TabletRobotTabs.Tab.FILTERS
        || !behavior.renderTagTooltip(graphics, font, hoveredSlot, mouseX, mouseY)) {
      renderTooltip(graphics, mouseX, mouseY);
    }
    renderCutTooltips(graphics, mouseX, mouseY);
    renderDetail(graphics, mouseX, mouseY);
  }

  private void renderCutTooltips(GuiGraphics graphics, int mouseX, int mouseY) {
    graphics.pose().pushPose();
    graphics.pose().translate(windowLeft, windowTop, 0);
    headerPainter.cutTooltip(graphics, mouseX - windowLeft, mouseY - windowTop);
    graphics.pose().popPose();
    graphics.pose().pushPose();
    graphics.pose().translate(leftPos, topPos, 0);
    painter.cutTooltip(graphics, mouseX - leftPos, mouseY - topPos);
    graphics.pose().popPose();
  }

  private void renderDetail(GuiGraphics graphics, int mouseX, int mouseY) {
    boolean shown =
        switch (tabs.open()) {
          case RESOURCES ->
              mouseY >= windowTop + TabletRobotHeader.HEIGHT
                  && resources.renderDetail(graphics, font, mouseX, mouseY, leftPos, topPos);
          case INVENTORY -> exclusive.renderTooltip(graphics, font, hoveredSlot, mouseX, mouseY);
          default -> false;
        };
    if (!shown) {
      tabAt(mouseX, mouseY)
          .ifPresent(under ->
              graphics.renderTooltip(font, Component.translatable(under.title()), mouseX, mouseY));
    }
  }

  @Override
  protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
    tabs.renderBehind(graphics, windowLeft + tabLeft(), windowTop);
    ScreenPainter.window(graphics, windowLeft, windowTop, imageWidth, imageHeight);
    tabs.renderFront(graphics, windowLeft + tabLeft(), windowTop);
    long tick = minecraft.player == null ? 0 : minecraft.player.tickCount;
    graphics.pose().pushPose();
    graphics.pose().translate(windowLeft, windowTop, 0);
    header.render(graphics, shownName, tick, imageWidth);
    graphics.pose().popPose();
    switch (tabs.open()) {
      case FILTERS -> {
        behavior.renderFrames(graphics, leftPos, topPos);
        RobotSlotFrames.player(graphics, menu, leftPos, topPos);
      }
      case INVENTORY -> {
        RobotSlotFrames.robot(graphics, menu, leftPos, topPos);
        exclusive.renderFrames(graphics, leftPos, topPos);
      }
      default -> {}
    }
  }

  /** In the coordinates of the screen of the Cubit, over the items. */
  @Override
  protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    switch (tabs.open()) {
      case RESOURCES -> resources.render(graphics);
      case DEFENSE -> defense.render(graphics);
      case FILTERS -> {
        behavior.renderFilter(graphics);
        label(graphics, playerInventoryTitle, menu.playerInventoryY() - PLAYER_LABEL_GAP);
      }
      case INVENTORY -> renderInventoryLabels(graphics);
    }
  }

  private void renderInventoryLabels(GuiGraphics graphics) {
    exclusive.renderLabels(graphics);
    label(graphics, Component.translatable(ScreenKeys.ROBOT_INVENTORY, shownName), ROBOT_LABEL_Y);
  }

  private void label(GuiGraphics graphics, Component text, int y) {
    painter.text(graphics, text, MARGIN, y, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (dialog.isOpen()) {
      return dialog.mouseClicked(mouseX, mouseY, button);
    }
    Optional<TabletRobotTabs.Tab> clicked = tabAt(mouseX, mouseY);
    if (clicked.isPresent()) {
      tabs.open(clicked.get());
      showOpenTab();
      return true;
    }
    OptionalInt pressed = pressedButton(mouseX, mouseY);
    if (pressed.isPresent()) {
      minecraft.gameMode.handleInventoryButtonClick(menu.containerId, pressed.getAsInt());
      return true;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  /** The button under the mouse in the open tab, below the header, as the server numbers them. */
  private OptionalInt pressedButton(double mouseX, double mouseY) {
    if (mouseY < windowTop + TabletRobotHeader.HEIGHT) {
      return OptionalInt.empty();
    }
    double x = mouseX - leftPos;
    double y = mouseY - topPos;
    return switch (tabs.open()) {
      case RESOURCES -> resources.switchAt(x, y);
      case DEFENSE -> defense.modeAt(x, y);
      case FILTERS -> behavior.modeAt(x, y);
      case INVENTORY -> OptionalInt.empty();
    };
  }

  @Override
  public boolean keyPressed(int key, int scanCode, int modifiers) {
    return dialog.isOpen()
        ? dialog.keyPressed(key, scanCode, modifiers)
        : super.keyPressed(key, scanCode, modifiers);
  }

  @Override
  public boolean charTyped(char typed, int modifiers) {
    return dialog.isOpen() ? dialog.charTyped(typed, modifiers) : super.charTyped(typed, modifiers);
  }

  private int tabLeft() {
    return imageWidth - TabletRobotTabs.INSET;
  }

  private Optional<TabletRobotTabs.Tab> tabAt(double mouseX, double mouseY) {
    return tabs.at(mouseX - windowLeft - tabLeft(), mouseY - windowTop);
  }

  /** The window, not the part of the tab, decides what is outside, and the tabs are inside. */
  @Override
  protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top, int button) {
    boolean outside =
        mouseX < windowLeft
            || mouseY < windowTop
            || mouseX >= windowLeft + imageWidth
            || mouseY >= windowTop + imageHeight;
    return outside && tabAt(mouseX, mouseY).isEmpty();
  }
}
