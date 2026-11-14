package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.domain.dock.DockGoal;
import io.github.marcelofabianov.cubit.domain.robot.FollowCall;
import io.github.marcelofabianov.cubit.domain.robot.UnloadCall;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import io.github.marcelofabianov.cubit.menu.TabletRobotMenu;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.UnaryOperator;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

/**
 * The buttons of the header of the Tablet: Back in the corner, and Power, Rename, Go to Dock,
 * Unload and Follow in a row under the portrait and the bars, from the margin (answers 1020, 1033,
 * 1035, 1073 and 1089). A button that cannot act now is disabled, with the reason in its tooltip.
 */
final class TabletHeaderButtons {
  static final int BUTTON_WIDTH = 53;
  static final int BUTTON_GAP = 4;
  private static final int MARGIN = TabletRobotHeader.MARGIN;
  private static final int BACK_Y = 5;
  private static final int BACK_WIDTH = 36;
  private static final int BACK_HEIGHT = 12;
  private static final int UNLOAD_PLACE = 3;
  private static final int FOLLOW_PLACE = 4;

  private final TabletRobotMenu menu;
  private FlatButton back;
  private FlatButton power;
  private FlatButton rename;
  private FlatButton dock;
  private FlatButton unload;
  private FlatButton follow;

  TabletHeaderButtons(TabletRobotMenu menu) {
    this.menu = menu;
  }

  /** What each button does, given by the screen. */
  record Actions(Runnable back, IntConsumer press, Runnable rename) {}

  void add(UnaryOperator<FlatButton> add, int windowLeft, int windowTop, Actions actions) {
    back = add.apply(backButton(windowLeft, windowTop, actions.back()));
    Runnable powerPress = pressing(actions, TabletRobotMenu.POWER_TOGGLE_BUTTON);
    power = add.apply(rowButton(windowLeft, windowTop, 0, Component.empty(), powerPress));
    rename =
        add.apply(rowButton(windowLeft, windowTop, 1, bold(TabletKeys.RENAME), actions.rename()));
    Runnable dockPress = pressing(actions, TabletRobotMenu.GO_TO_DOCK_BUTTON);
    dock = add.apply(rowButton(windowLeft, windowTop, 2, bold(TabletKeys.GO_TO_DOCK), dockPress));
    Runnable unloadPress = pressing(actions, TabletRobotMenu.UNLOAD_NOW_BUTTON);
    unload = add.apply(unloadButton(windowLeft, windowTop, unloadPress));
    Runnable followPress = pressing(actions, TabletRobotMenu.FOLLOW_BUTTON);
    follow = add.apply(followButton(windowLeft, windowTop, followPress));
  }

  private static Runnable pressing(Actions actions, int button) {
    return () -> actions.press().accept(button);
  }

  /** Unload, before Follow, as the offline screen also has it (answer 1089). */
  static FlatButton unloadButton(int windowLeft, int windowTop, Runnable action) {
    return rowButton(windowLeft, windowTop, UNLOAD_PLACE, bold(TabletKeys.UNLOAD_NOW), action);
  }

  /** Follow, the last of the row, as the offline screen also has it. */
  static FlatButton followButton(int windowLeft, int windowTop, Runnable action) {
    return rowButton(windowLeft, windowTop, FOLLOW_PLACE, bold(TabletKeys.FOLLOW), action);
  }

  /** A button of the row under the bars, by its place in the row. */
  static FlatButton rowButton(
      int windowLeft, int windowTop, int place, Component message, Runnable action) {
    return new FlatButton(
        windowLeft + MARGIN + place * (BUTTON_WIDTH + BUTTON_GAP),
        windowTop + TabletRobotHeader.BUTTON_Y,
        BUTTON_WIDTH,
        TabletRobotHeader.BUTTON_HEIGHT,
        message,
        action);
  }

  /** Back, in the upper left corner of the window, as the offline screen also has it. */
  static FlatButton backButton(int windowLeft, int windowTop, Runnable action) {
    FlatButton button =
        new FlatButton(
            windowLeft + MARGIN,
            windowTop + BACK_Y,
            BACK_WIDTH,
            BACK_HEIGHT,
            bold(TabletKeys.BACK),
            action);
    button.setMuted(true);
    return button;
  }

  static Component bold(String key) {
    return Component.translatable(key).withStyle(ChatFormatting.BOLD);
  }

  /** Read again on every frame, from the values of the server. Hidden behind the Rename dialog. */
  void update(boolean visible) {
    updatePower();
    updateDock();
    updateFollow();
    updateUnload();
    for (FlatButton button : List.of(back, power, rename, dock, unload, follow)) {
      button.visible = visible;
    }
  }

  /** Switch off an active Cubit, turn on one with charge, or say why neither (answer 1033). */
  private void updatePower() {
    boolean off = menu.powerAvailable();
    boolean on = !off && menu.canTurnOn();
    power.setMessage(bold(off ? TabletKeys.SWITCH_OFF : TabletKeys.TURN_ON));
    String detail =
        off
            ? ScreenKeys.POWER_DETAIL
            : on ? TabletKeys.TURN_ON_DETAIL : TabletKeys.POWER_UNAVAILABLE;
    enable(power, off || on, detail);
  }

  private void updateDock() {
    DockGoal goal = menu.dockGoal();
    String detail =
        switch (goal) {
          case READY -> TabletKeys.GO_TO_DOCK_DETAIL;
          case SWITCHED_OFF -> TabletKeys.DOCK_SWITCHED_OFF;
          case HIBERNATING -> TabletKeys.DOCK_HIBERNATING;
          case NO_DOCK -> TabletKeys.DOCK_OUT_OF_REACH;
        };
    enable(dock, goal.ready(), detail);
  }

  private void updateUnload() {
    UnloadCall call = menu.unloadCall();
    String detail =
        switch (call) {
          case READY -> TabletKeys.UNLOAD_NOW_DETAIL;
          case NO_CHEST -> TabletKeys.UNLOAD_NO_CHEST;
          case EMPTY -> TabletKeys.UNLOAD_EMPTY;
          case SWITCHED_OFF -> TabletKeys.DOCK_SWITCHED_OFF;
          case HIBERNATING -> TabletKeys.DOCK_HIBERNATING;
        };
    enable(unload, call.ready(), detail);
  }

  private void updateFollow() {
    FollowCall call = menu.followCall();
    String detail =
        switch (call) {
          case READY -> TabletKeys.FOLLOW_DETAIL;
          case FOLLOWING -> TabletKeys.FOLLOW_ALREADY;
          case NO_CHARGE -> TabletKeys.FOLLOW_NO_CHARGE;
          case OTHER_DIMENSION -> TabletKeys.FOLLOW_OTHER_DIMENSION;
        };
    enable(follow, call.ready(), detail);
  }

  private static void enable(FlatButton button, boolean active, String detail) {
    button.active = active;
    button.setMuted(!active);
    button.setTooltip(Tooltip.create(Component.translatable(detail)));
  }
}
