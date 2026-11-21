package io.github.zenndg.cubit.client;

import io.github.zenndg.cubit.block.DeskGrid;
import io.github.zenndg.cubit.domain.energy.EnergyFormat;
import io.github.zenndg.cubit.item.TierLook;
import io.github.zenndg.cubit.menu.DeskControl;
import io.github.zenndg.cubit.menu.DeskScreenKeys;
import io.github.zenndg.cubit.menu.EngineeringDeskMenu;
import io.github.zenndg.cubit.menu.ScreenKeys;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * The screen of the Engineering Desk, with the Project and Storage tabs (answers 1473 and 1493). It
 * only shows and asks: the server decides (rule 19). The blueprint look comes later (1493).
 */
final class EngineeringDeskScreen
    extends net.minecraft.client.gui.screens.inventory.AbstractContainerScreen<
        EngineeringDeskMenu> {
  private static final int HEIGHT = 230;
  private static final int MARGIN = 8;
  private static final int TITLE_Y = 6;
  private static final int LABEL_Y = 20;
  private static final int PROJECT_Y = 9;
  private static final int STORAGE_Y = 37;
  private static final int[] TAB_TOPS = {PROJECT_Y, STORAGE_Y};
  private static final int ITEM_SHIFT = -2;
  private static final int ARROW_X = 156;
  private static final int ARROW_Y = 50;
  private static final int ARROW_WIDTH = 22;
  private static final int ARROW_HEIGHT = 6;
  private static final int RESULT_X = 184;
  private static final int RESULT_Y = 44;
  private static final int ORDER_Y = 92;
  private static final int BUTTON_HEIGHT = 12;
  private static final int SMALL_BUTTON = 14;
  private static final int MINUS_X = 96;
  private static final int PLUS_X = 136;
  private static final int CANCEL_X = 154;
  private static final int CANCEL_WIDTH = 48;
  private static final int ORDER_TEXT_X = 123;
  private static final int ENERGY_LABEL_Y = 110;
  private static final int BAR_Y = 121;
  private static final int BAR_WIDTH = 204;
  private static final int BAR_HEIGHT = 6;
  private static final int INVENTORY_LABEL_Y = 135;
  private static final int PERCENT = 100;
  private static final int ENERGY_GAP = 4;
  private static final String MINUS_LABEL = "-";
  private static final String PLUS_LABEL = "+";

  private final DeskCatalog catalog = new DeskCatalog();
  private final DeskGridView grid = new DeskGridView();
  private boolean storageOpen;
  private FlatButton minus;
  private FlatButton plus;
  private FlatButton cancel;

  EngineeringDeskScreen(EngineeringDeskMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    imageWidth = EngineeringDeskMenu.WIDTH;
    imageHeight = HEIGHT;
    inventoryLabelX = EngineeringDeskMenu.SLOT_SIZE * 2 + MARGIN + MARGIN;
    inventoryLabelY = INVENTORY_LABEL_Y;
  }

  @Override
  protected void init() {
    super.init();
    minus = button(MINUS_X, SMALL_BUTTON, Component.literal(MINUS_LABEL), () -> order(false));
    plus = button(PLUS_X, SMALL_BUTTON, Component.literal(PLUS_LABEL), () -> order(true));
    cancel =
        button(
            CANCEL_X,
            CANCEL_WIDTH,
            Component.translatable(DeskScreenKeys.CANCEL),
            () -> send(DeskControl.CANCEL));
    showTab();
  }

  private FlatButton button(int x, int width, Component label, Runnable action) {
    return addRenderableWidget(
        new FlatButton(leftPos + x, topPos + ORDER_Y, width, BUTTON_HEIGHT, label, action));
  }

  private void order(boolean up) {
    boolean shift = hasShiftDown();
    send(
        up
            ? (shift ? DeskControl.PLUS_SHIFT : DeskControl.PLUS)
            : (shift ? DeskControl.MINUS_SHIFT : DeskControl.MINUS));
  }

  private void send(int id) {
    minecraft.gameMode.handleInventoryButtonClick(menu.containerId, id);
  }

  private void showTab() {
    menu.showStorage(storageOpen);
    minus.visible = !storageOpen;
    plus.visible = !storageOpen;
    cancel.visible = !storageOpen;
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    super.render(graphics, mouseX, mouseY, partialTick);
    renderTooltip(graphics, mouseX, mouseY);
    if (!storageOpen) {
      catalog
          .at(mouseX, mouseY, leftPos, topPos)
          .ifPresent(index -> catalog.tooltip(graphics, font, index, mouseX, mouseY));
    }
  }

  @Override
  protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
    int tabsLeft = leftPos + imageWidth - ScreenTabStrip.INSET;
    int openTop = storageOpen ? STORAGE_Y : PROJECT_Y;
    ScreenTabStrip.renderBehind(graphics, tabsLeft, topPos, TAB_TOPS, openTop);
    ScreenPainter.window(graphics, leftPos, topPos, imageWidth, imageHeight);
    ScreenTabStrip.renderOpen(graphics, tabsLeft, topPos, openTop);
    renderTabIcons(graphics, tabsLeft);
    DeskScreenFrames.player(graphics, leftPos, topPos);
    if (storageOpen) {
      DeskScreenFrames.storage(graphics, leftPos, topPos, menu.slots());
    } else {
      renderProject(graphics);
    }
  }

  private void renderTabIcons(GuiGraphics graphics, int tabsLeft) {
    int x = tabsLeft + ScreenTabStrip.ICON_X + ITEM_SHIFT;
    int y = topPos + ScreenTabStrip.ICON_Y + ITEM_SHIFT;
    graphics.renderItem(new ItemStack(CubitItems.ENGINEERING_DESK.get()), x, y + PROJECT_Y);
    graphics.renderItem(new ItemStack(Items.CHEST), x, y + STORAGE_Y);
  }

  private void renderProject(GuiGraphics graphics) {
    catalog.render(graphics, leftPos, topPos, menu.recipeIndex());
    long tick = minecraft.player == null ? 0 : minecraft.player.tickCount;
    grid.render(graphics, leftPos, topPos, chosenMarks(), menu.marks(), tick);
    ScreenPainter.bar(
        graphics,
        new ScreenPainter.Box(leftPos + ARROW_X, topPos + ARROW_Y, ARROW_WIDTH, ARROW_HEIGHT),
        menu.progressPercent(),
        PERCENT,
        ScreenPainter.STATUS_BLUE);
    ScreenPainter.slot(graphics, leftPos + RESULT_X, topPos + RESULT_Y);
    graphics.renderFakeItem(chosenResult(), leftPos + RESULT_X + 1, topPos + RESULT_Y + 1);
    ScreenPainter.bar(
        graphics,
        new ScreenPainter.Box(leftPos + MARGIN, topPos + BAR_Y, BAR_WIDTH, BAR_HEIGHT),
        menu.stock().amount(),
        Math.max(1, menu.capacity().amount()),
        ScreenPainter.ORANGE);
    ScreenPainter.slot(
        graphics,
        leftPos + EngineeringDeskMenu.ENERGY_SLOT_X - 1,
        topPos + EngineeringDeskMenu.ENERGY_SLOT_Y - 1);
  }

  private List<Ingredient> chosenMarks() {
    var all = catalog.recipes();
    int index = menu.recipeIndex();
    return index < 0 || index >= all.size() ? List.of() : DeskGrid.of(all.get(index).value());
  }

  private ItemStack chosenResult() {
    var all = catalog.recipes();
    int index = menu.recipeIndex();
    return index < 0 || index >= all.size()
        ? ItemStack.EMPTY
        : DeskCatalog.resultOf(all.get(index));
  }

  @Override
  protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    Component title =
        Component.translatable(DeskScreenKeys.TITLE_TIER, this.title, TierLook.name(menu.tier()));
    graphics.drawString(font, title, MARGIN, TITLE_Y, ScreenPainter.TEXT, false);
    Component status = Component.translatable(DeskScreenKeys.status(menu.status()));
    int statusX = imageWidth - MARGIN - font.width(status);
    graphics.drawString(font, status, statusX, TITLE_Y, ScreenPainter.STATUS_BLUE, false);
    graphics.drawString(
        font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, ScreenPainter.TEXT, false);
    if (storageOpen) {
      DeskScreenFrames.storageLabels(graphics, font, LABEL_Y);
    } else {
      renderProjectLabels(graphics);
    }
  }

  private void renderProjectLabels(GuiGraphics graphics) {
    Component catalogTitle = Component.translatable(DeskScreenKeys.CATALOG);
    graphics.drawString(font, catalogTitle, MARGIN, LABEL_Y, ScreenPainter.TEXT, false);
    Component order = Component.translatable(DeskScreenKeys.ORDER, menu.done(), menu.total());
    graphics.drawCenteredString(font, order, ORDER_TEXT_X, ORDER_Y + 2, ScreenPainter.WHITE);
    Component label = Component.translatable(DeskScreenKeys.STORED_ENERGY);
    graphics.drawString(font, label, MARGIN, ENERGY_LABEL_Y, ScreenPainter.TEXT, false);
    Component amount =
        Component.translatable(
            ScreenKeys.ENERGY_AMOUNT,
            EnergyFormat.compact(menu.stock()),
            EnergyFormat.compact(menu.capacity()));
    int amountX = MARGIN + font.width(label) + ENERGY_GAP;
    graphics.drawString(font, amount, amountX, ENERGY_LABEL_Y, ScreenPainter.DETAIL, false);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    double tabX = mouseX - (leftPos + imageWidth - ScreenTabStrip.INSET);
    double tabY = mouseY - topPos;
    if (ScreenTabStrip.contains(tabX, tabY, PROJECT_Y)
        || ScreenTabStrip.contains(tabX, tabY, STORAGE_Y)) {
      storageOpen = ScreenTabStrip.contains(tabX, tabY, STORAGE_Y);
      showTab();
      return true;
    }
    if (!storageOpen && clickedProject(mouseX, mouseY)) {
      return true;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  private boolean clickedProject(double mouseX, double mouseY) {
    var recipe = catalog.at(mouseX, mouseY, leftPos, topPos);
    if (recipe.isPresent()) {
      if (DeskCatalog.unlocked(catalog.recipes().get(recipe.get()))) {
        send(DeskControl.choose(recipe.get()));
      }
      return true;
    }
    var cell = grid.at(mouseX, mouseY, leftPos, topPos);
    if (cell.isPresent()) {
      menu.marks().set(cell.get(), menu.getCarried().copyWithCount(1));
      send(DeskControl.mark(cell.get()));
      return true;
    }
    return false;
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
    if (!storageOpen && catalog.at(mouseX, mouseY, leftPos, topPos).isPresent()) {
      catalog.scroll(scrollY);
      return true;
    }
    return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
  }
}
