package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.domain.robot.TabletApp;
import io.github.marcelofabianov.cubit.item.TabletItem;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.function.BiConsumer;

/** The Tablet, in English, the base language (answers 1006 and 1026). */
final class TabletTexts {
  private static final String NAME = "Cubit Tablet";
  private static final String WITH_APP = "%s · %s";
  private static final String APP_OPEN = "Open";
  private static final String APP_TARGET = "Target";
  private static final String APP_DOCK = "Dock";
  private static final String TITLE = "Cubits";
  private static final String SEARCH = "Search";
  private static final String EMPTY = "No Cubits placed";
  private static final String PLACE = "%s, %s, %s · %s";
  private static final String DISTANCE = "%s m";
  private static final String LAST_SEEN = "seen %s min ago";
  private static final String CHOSEN = "Chosen: %s";
  private static final String OVERWORLD = "Overworld";
  private static final String NETHER = "Nether";
  private static final String END = "End";
  private static final String BACK = "Back";
  private static final String RENAME = "Rename";
  private static final String RENAME_TITLE = "Rename the Cubit";
  private static final String CONFIRM = "Confirm";
  private static final String CANCEL = "Cancel";
  private static final String TURN_ON = "Turn on";
  private static final String SWITCH_OFF = "Switch off";
  private static final String TURN_ON_DETAIL = "Turns the Cubit on, and it follows you";
  private static final String POWER_UNAVAILABLE = "No charge to turn on";
  private static final String PLACE_TITLE = "Place";
  private static final String COORDINATES = "%s, %s, %s";
  private static final String OUT_OF_REACH = "Out of reach · seen %s min ago";
  private static final String OUT_OF_REACH_DETAIL = "The chunk of this Cubit is not loaded";
  private static final String TAB_RESOURCES = "Features";
  private static final String TAB_FILTERS = "Filters";

  private TabletTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(CubitItems.TABLET.get().getDescriptionId(), NAME);
    add.accept(TabletKeys.WITH_APP, WITH_APP);
    add.accept(TabletItem.appKey(TabletApp.OPEN), APP_OPEN);
    add.accept(TabletItem.appKey(TabletApp.TARGET), APP_TARGET);
    add.accept(TabletItem.appKey(TabletApp.DOCK), APP_DOCK);
    add.accept(TabletKeys.TITLE, TITLE);
    add.accept(TabletKeys.SEARCH, SEARCH);
    add.accept(TabletKeys.EMPTY, EMPTY);
    add.accept(TabletKeys.PLACE, PLACE);
    add.accept(TabletKeys.DISTANCE, DISTANCE);
    add.accept(TabletKeys.LAST_SEEN, LAST_SEEN);
    add.accept(TabletKeys.CHOSEN, CHOSEN);
    add.accept(TabletKeys.OVERWORLD, OVERWORLD);
    add.accept(TabletKeys.NETHER, NETHER);
    add.accept(TabletKeys.END, END);
    addScreen(add);
  }

  /** The screen of a Cubit on the Tablet (answers 1033 to 1037). */
  private static void addScreen(BiConsumer<String, String> add) {
    add.accept(TabletKeys.BACK, BACK);
    add.accept(TabletKeys.RENAME, RENAME);
    add.accept(TabletKeys.RENAME_TITLE, RENAME_TITLE);
    add.accept(TabletKeys.CONFIRM, CONFIRM);
    add.accept(TabletKeys.CANCEL, CANCEL);
    add.accept(TabletKeys.TURN_ON, TURN_ON);
    add.accept(TabletKeys.SWITCH_OFF, SWITCH_OFF);
    add.accept(TabletKeys.TURN_ON_DETAIL, TURN_ON_DETAIL);
    add.accept(TabletKeys.POWER_UNAVAILABLE, POWER_UNAVAILABLE);
    add.accept(TabletKeys.PLACE_TITLE, PLACE_TITLE);
    add.accept(TabletKeys.COORDINATES, COORDINATES);
    add.accept(TabletKeys.OUT_OF_REACH, OUT_OF_REACH);
    add.accept(TabletKeys.OUT_OF_REACH_DETAIL, OUT_OF_REACH_DETAIL);
    add.accept(TabletKeys.TAB_RESOURCES, TAB_RESOURCES);
    add.accept(TabletKeys.TAB_FILTERS, TAB_FILTERS);
  }
}
