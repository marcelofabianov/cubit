package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.client.ScreenPainter.Box;
import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.behavior.RobotScreenStatus;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.menu.ScreenEnums;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import io.github.marcelofabianov.cubit.network.CubitNetwork;
import io.github.marcelofabianov.cubit.network.RobotListPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * The screen of a Cubit in an unloaded chunk: only the header of its last snapshot, without tabs,
 * and the notice of how old it is. The buttons stay disabled with the reason, and Back still works
 * (answer 1034). The client builds it from the list, so nothing goes to the server but Back.
 */
final class TabletOfflineScreen extends Screen {
  private static final int WIDTH = TabletRobotHeader.WIDTH;
  private static final int HEIGHT = TabletRobotHeader.HEIGHT + 24;
  private static final int MARGIN = TabletRobotHeader.MARGIN;
  private static final int LINE = 9;
  private static final int PORTRAIT_Y = 22;
  private static final Box STATUS = new Box(200, 8, 92, LINE);
  private static final Box BATTERY = new Box(TabletRobotHeader.TEXT_X, 22, 150, LINE);
  private static final Box NOTICE = new Box(MARGIN, TabletRobotHeader.HEIGHT + 4, 284, LINE);
  private static final long TICKS_PER_MINUTE = 1_200;

  private final ScreenPainter painter = new ScreenPainter();
  private final RobotListPayload.Entry entry;
  private int left;
  private int top;

  private TabletOfflineScreen(RobotListPayload.Entry entry) {
    super(Component.translatable(TabletKeys.TITLE));
    this.entry = entry;
  }

  static void show(RobotListPayload.Entry entry) {
    Minecraft.getInstance().setScreen(new TabletOfflineScreen(entry));
  }

  @Override
  protected void init() {
    left = (width - WIDTH) / 2;
    top = (height - HEIGHT) / 2;
    addRenderableWidget(TabletHeaderButtons.backButton(left, top, CubitNetwork::requestRobotList));
    disabled(0, TabletKeys.TURN_ON);
    disabled(1, TabletKeys.RENAME);
    disabled(2, TabletKeys.GO_TO_DOCK);
    mute(addRenderableWidget(TabletHeaderButtons.followButton(left, top, () -> {})));
  }

  private void disabled(int place, String key) {
    mute(
        addRenderableWidget(
            TabletHeaderButtons.rowButton(
                left, top, place, TabletHeaderButtons.bold(key), () -> {})));
  }

  private static void mute(FlatButton button) {
    button.active = false;
    button.setMuted(true);
    button.setTooltip(Tooltip.create(Component.translatable(TabletKeys.OUT_OF_REACH_DETAIL)));
  }

  @Override
  public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    renderTransparentBackground(graphics);
    ScreenPainter.window(graphics, left, top, WIDTH, HEIGHT);
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    painter.begin(font);
    super.render(graphics, mouseX, mouseY, partialTick);
    graphics.pose().pushPose();
    graphics.pose().translate(left, top, 0);
    renderHeader(graphics);
    painter.cutTooltip(graphics, mouseX - left, mouseY - top);
    graphics.pose().popPose();
  }

  /** The portrait, the name, the state, the battery and the place of the snapshot. */
  private void renderHeader(GuiGraphics graphics) {
    RobotTier tier = ScreenEnums.of(RobotTier.values(), entry.look().tier());
    RobotColor color = ScreenEnums.of(RobotColor.values(), entry.look().color());
    RobotExpression face = ScreenEnums.of(RobotExpression.values(), entry.look().expression());
    long tick = minecraft.player == null ? 0 : minecraft.player.tickCount;
    RobotPortrait.draw(
        graphics,
        MARGIN,
        PORTRAIT_Y,
        new RobotPortrait.Look(face, tier, RobotShell.argb(color)),
        tick);
    TabletRobotHeader.title(painter, graphics, TabletSelectionScreen.nameOf(entry), tier);
    renderReadings(graphics);
  }

  /** The state, the battery, the place and how old the snapshot is. */
  private void renderReadings(GuiGraphics graphics) {
    RobotScreenStatus status = ScreenEnums.of(RobotScreenStatus.values(), entry.status());
    painter.status(
        graphics,
        Component.translatable(ScreenKeys.robotStatus(status)),
        STATUS,
        RobotTexts.statusColor(status));
    painter.text(
        graphics,
        Component.translatable(ScreenKeys.SETTINGS_BATTERY, entry.batteryPercent()),
        BATTERY,
        ScreenPainter.DETAIL,
        ScreenPainter.FULL_SCALE);
    Component coordinates =
        Component.translatable(
            TabletKeys.COORDINATES, entry.pos().getX(), entry.pos().getY(), entry.pos().getZ());
    TabletRobotHeader.place(
        painter, graphics, coordinates, TabletDimensions.name(entry.dimension()));
    TabletRobotHeader.rule(graphics, WIDTH);
    long minutes = entry.where().ageTicks() / TICKS_PER_MINUTE;
    painter.text(
        graphics,
        Component.translatable(TabletKeys.OUT_OF_REACH, minutes),
        NOTICE,
        ScreenPainter.TEXT,
        ScreenPainter.FULL_SCALE);
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }
}
