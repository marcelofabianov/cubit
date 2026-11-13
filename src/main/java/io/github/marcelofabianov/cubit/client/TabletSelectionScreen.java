package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.client.ScreenPainter.Box;
import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.behavior.RobotScreenStatus;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.domain.robot.RobotListings;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.item.TierLook;
import io.github.marcelofabianov.cubit.menu.ScreenEnums;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import io.github.marcelofabianov.cubit.network.CubitNetwork;
import io.github.marcelofabianov.cubit.network.RobotListPayload;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;

/**
 * The selection screen of the Tablet: the Cubits of the player, already ordered by the server, with
 * a search by name once they do not fit (answers 1003 to 1005). A click chooses the Cubit, and the
 * server checks the choice.
 */
final class TabletSelectionScreen extends Screen {
  private static final int SCREEN_GAP = 30;
  private static final int MAX_WIDTH = 480;
  private static final int MIN_WIDTH = 300;
  private static final int MARGIN = 8;
  private static final int LINE = 9;
  private static final int TITLE_Y = 8;
  private static final int FIRST_ROW = 22;
  private static final int SEARCH_HEIGHT = 16;
  private static final int SEARCH_GAP = 6;
  private static final int ROW_HEIGHT = 44;
  private static final int BOTTOM = 8;
  private static final int PORTRAIT_X = 4;
  private static final int PORTRAIT_Y = 4;
  private static final int TEXT_X = MARGIN + PORTRAIT_X + RobotPortrait.SIZE + 10;
  private static final int NAME_Y = 6;
  private static final int BATTERY_Y = 19;
  private static final int PLACE_Y = 31;
  private static final int STATUS_WIDTH = 96;
  private static final int MAX_SEARCH = 50;
  private static final long TICKS_PER_MINUTE = 1_200;
  private static final int OPAQUE = 0xFF000000;
  private static final String SPACE = " ";
  private static final String SEPARATOR = " · ";

  private final ScreenPainter painter = new ScreenPainter();
  private final List<RobotListPayload.Entry> entries;
  private List<RobotListPayload.Entry> shown;
  private EditBox search;
  private int firstRow;
  private int left;
  private int top;
  private int windowWidth;
  private int windowHeight;
  private int rows;
  private boolean searchable;

  private TabletSelectionScreen(List<RobotListPayload.Entry> entries) {
    super(Component.translatable(TabletKeys.TITLE));
    this.entries = entries;
    this.shown = entries;
  }

  /** Opened by the list the server sends when the player uses the Tablet. */
  static void show(RobotListPayload payload) {
    Minecraft.getInstance().setScreen(new TabletSelectionScreen(payload.entries()));
  }

  private int rowsTop() {
    return FIRST_ROW + (searchable ? SEARCH_HEIGHT + SEARCH_GAP : 0);
  }

  /**
   * A big window, with a gap to the border of the game window, and as many rows as fit. The search
   * shows once the Cubits do not fit (answer 1005).
   */
  @Override
  protected void init() {
    windowWidth = Math.clamp(width - 2 * SCREEN_GAP, Math.min(MIN_WIDTH, width), MAX_WIDTH);
    windowHeight = Math.max(height - 2 * SCREEN_GAP, FIRST_ROW + ROW_HEIGHT + BOTTOM);
    left = (width - windowWidth) / 2;
    top = (height - windowHeight) / 2;
    searchable = entries.size() > (windowHeight - FIRST_ROW - BOTTOM) / ROW_HEIGHT;
    rows = Math.max((windowHeight - rowsTop() - BOTTOM) / ROW_HEIGHT, 1);
    if (searchable) {
      search =
          addRenderableWidget(
              new EditBox(
                  font,
                  left + MARGIN,
                  top + FIRST_ROW,
                  windowWidth - 2 * MARGIN,
                  SEARCH_HEIGHT,
                  Component.translatable(TabletKeys.SEARCH)));
      search.setMaxLength(MAX_SEARCH);
      search.setHint(Component.translatable(TabletKeys.SEARCH));
      search.setResponder(this::filter);
    }
  }

  private void filter(String text) {
    shown =
        entries.stream()
            .filter(entry -> RobotListings.matches(nameOf(entry).getString(), text))
            .toList();
    firstRow = 0;
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    painter.begin(font);
    super.render(graphics, mouseX, mouseY, partialTick);
    graphics.pose().pushPose();
    graphics.pose().translate(left, top, 0);
    Component heading = title.copy().withStyle(ChatFormatting.BOLD);
    painter.text(graphics, heading, MARGIN, TITLE_Y, ScreenPainter.TEXT, ScreenPainter.FULL_SCALE);
    if (shown.isEmpty()) {
      painter.text(
          graphics,
          Component.translatable(TabletKeys.EMPTY),
          MARGIN,
          rowsTop() + NAME_Y,
          ScreenPainter.DETAIL,
          ScreenPainter.FULL_SCALE);
    }
    for (int row = 0; row < rows && firstRow + row < shown.size(); row++) {
      renderRow(graphics, row, shown.get(firstRow + row));
    }
    graphics.pose().popPose();
  }

  /** The darkened game behind, then the window, so the window is never darkened itself. */
  @Override
  public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    renderTransparentBackground(graphics);
    ScreenPainter.window(graphics, left, top, windowWidth, windowHeight);
  }

  private void renderRow(GuiGraphics graphics, int row, RobotListPayload.Entry entry) {
    int y = rowsTop() + row * ROW_HEIGHT;
    if (row % 2 == 0) {
      graphics.fill(MARGIN, y, windowWidth - MARGIN, y + ROW_HEIGHT, ScreenPainter.ROW);
    }
    RobotTier tier = ScreenEnums.of(RobotTier.values(), entry.look().tier());
    long tick = minecraft.player == null ? 0 : minecraft.player.tickCount;
    RobotPortrait.draw(graphics, MARGIN + PORTRAIT_X, y + PORTRAIT_Y, lookOf(entry, tier), tick);
    Component name =
        nameOf(entry)
            .copy()
            .withStyle(ChatFormatting.BOLD)
            .append(SPACE)
            .append(TierLook.name(tier));
    painter.text(
        graphics,
        name,
        new Box(TEXT_X, y + NAME_Y, statusX() - TEXT_X - MARGIN, LINE),
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
    renderDetails(graphics, y, entry);
  }

  private int statusX() {
    return windowWidth - MARGIN - PORTRAIT_X - STATUS_WIDTH;
  }

  /** The battery and the place at full size, now that the window has room for them. */
  private void renderDetails(GuiGraphics graphics, int y, RobotListPayload.Entry entry) {
    RobotScreenStatus status = ScreenEnums.of(RobotScreenStatus.values(), entry.status());
    painter.status(
        graphics,
        Component.translatable(ScreenKeys.robotStatus(status)),
        new Box(statusX(), y + NAME_Y, STATUS_WIDTH, LINE),
        RobotTexts.statusColor(status));
    int textWidth = windowWidth - TEXT_X - MARGIN - PORTRAIT_X;
    painter.text(
        graphics,
        Component.translatable(ScreenKeys.SETTINGS_BATTERY, entry.batteryPercent()),
        new Box(TEXT_X, y + BATTERY_Y, textWidth, LINE),
        ScreenPainter.DETAIL,
        ScreenPainter.FULL_SCALE);
    painter.text(
        graphics,
        placeOf(entry),
        new Box(TEXT_X, y + PLACE_Y, textWidth, LINE),
        ScreenPainter.DETAIL,
        ScreenPainter.FULL_SCALE);
  }

  private static RobotPortrait.Look lookOf(RobotListPayload.Entry entry, RobotTier tier) {
    RobotColor color = ScreenEnums.of(RobotColor.values(), entry.look().color());
    int shell = OPAQUE | DyeColor.byName(color.key(), DyeColor.WHITE).getTextureDiffuseColor();
    RobotExpression face = ScreenEnums.of(RobotExpression.values(), entry.look().expression());
    return new RobotPortrait.Look(face, tier, shell);
  }

  private static Component nameOf(RobotListPayload.Entry entry) {
    return entry.name().isEmpty()
        ? Component.translatable(CubitEntities.GENERAL_ROBOT.get().getDescriptionId())
        : Component.literal(entry.name());
  }

  /** The place, the distance in the same dimension and, for a snapshot, how old it is. */
  private static Component placeOf(RobotListPayload.Entry entry) {
    Component place =
        Component.translatable(
            TabletKeys.PLACE,
            entry.pos().getX(),
            entry.pos().getY(),
            entry.pos().getZ(),
            TabletDimensions.name(entry.dimension()));
    if (entry.where().distance() > 0) {
      place =
          place
              .copy()
              .append(SEPARATOR)
              .append(
                  Component.translatable(
                      TabletKeys.DISTANCE, Math.round(entry.where().distance())));
    }
    if (!entry.where().loaded()) {
      long minutes = entry.where().ageTicks() / TICKS_PER_MINUTE;
      place =
          place
              .copy()
              .append(SEPARATOR)
              .append(Component.translatable(TabletKeys.LAST_SEEN, minutes));
    }
    return place;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    double x = mouseX - left;
    double y = mouseY - top - rowsTop();
    int row = (int) Math.floor(y / ROW_HEIGHT);
    if (x >= MARGIN
        && x < windowWidth - MARGIN
        && y >= 0
        && row < rows
        && firstRow + row < shown.size()) {
      CubitNetwork.chooseRobot(shown.get(firstRow + row).robot());
      onClose();
      return true;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
    int last = Math.max(shown.size() - rows, 0);
    firstRow = Math.clamp(firstRow - (int) Math.signum(scrollY), 0, last);
    return true;
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }
}
