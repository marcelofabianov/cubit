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
  private static final String APP_AREA = "Area";
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
  private static final String BACK_TO_OPEN = "The chosen Cubit is not in Focus · back to Open";
  private static final String NO_MOB = "No mob in sight";
  private static final String NOT_A_TARGET = "This cannot be a Focus target";
  private static final String DOCK_ONLY_YOURS = "The Dock app marks one of your Docks";
  private static final String DOCK_NO_CUBIT = "Choose a Cubit within reach first";
  private static final String DOCK_MARKED = "Preferred Dock of %s";
  private static final String DOCK_UNMARKED = "%s has no preferred Dock";
  private static final String GO_TO_DOCK = "Go to Dock";
  private static final String GO_TO_DOCK_DETAIL = "Sends the Cubit to hibernate at its Dock";
  private static final String DOCK_SWITCHED_OFF = "The Cubit is switched off";
  private static final String DOCK_HIBERNATING = "The Cubit is already at a Dock";
  private static final String AREA_PICK_CUBIT = "Right-click one of your Cubits first";
  private static final String AREA_ONLY_YOURS = "The Area app marks an area for a Cubit of yours";
  private static final String AREA_LOCKED = "%s has no area mode in its tier";
  private static final String AREA_FIRST_CORNER = "Area · %s · right-click the first corner";
  private static final String AREA_FIRST_HINT = "Area · right-click the first corner";
  private static final String AREA_SECOND_CORNER = "First corner %s %s %s · click the other one";
  private static final String AREA_SECOND_HINT = "Area · right-click the other corner";
  private static final String AREA_TOO_BIG = "%s × %s × %s is too big · up to %s wide and %s high";
  private static final String AREA_OTHER_DIMENSION = "The area must be in the dimension of %s";
  private static final String AREA_OUT_OF_REACH = "Too far from %s · up to %s blocks away";
  private static final String AREA_SET = "%s works in %s × %s × %s";
  private static final String AREA_CANCELLED = "Area marking cancelled";
  private static final String AREA_CUBIT_GONE = "The Cubit is gone · right-click a Cubit again";
  private static final String AREA_SIZE = "%s × %s × %s";
  private static final String AREA_LINE = "Area";
  private static final String AREA_NONE = "None · mark one with the Area app";
  private static final String AREA_SUMMARY = "%s × %s × %s · %s %s %s to %s %s %s";
  private static final String FOLLOW = "Follow";
  private static final String FOLLOW_DETAIL = "Follows you · the one following now switches off";
  private static final String FOLLOW_ALREADY = "Already following you";
  private static final String FOLLOW_NO_CHARGE = "Without charge to follow you";
  private static final String FOLLOW_OTHER_DIMENSION = "In another dimension";
  private static final String DOCK_OUT_OF_REACH = "No free Dock within reach";
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
    add.accept(TabletItem.appKey(TabletApp.AREA), APP_AREA);
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
    addApps(add);
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

  /** The apps Target and Dock and Go to Dock (answers 1041 to 1047). */
  private static void addApps(BiConsumer<String, String> add) {
    add.accept(TabletKeys.BACK_TO_OPEN, BACK_TO_OPEN);
    add.accept(TabletKeys.NO_MOB, NO_MOB);
    add.accept(TabletKeys.NOT_A_TARGET, NOT_A_TARGET);
    add.accept(TabletKeys.DOCK_ONLY_YOURS, DOCK_ONLY_YOURS);
    add.accept(TabletKeys.DOCK_NO_CUBIT, DOCK_NO_CUBIT);
    add.accept(TabletKeys.DOCK_MARKED, DOCK_MARKED);
    add.accept(TabletKeys.DOCK_UNMARKED, DOCK_UNMARKED);
    add.accept(TabletKeys.GO_TO_DOCK, GO_TO_DOCK);
    add.accept(TabletKeys.GO_TO_DOCK_DETAIL, GO_TO_DOCK_DETAIL);
    add.accept(TabletKeys.DOCK_SWITCHED_OFF, DOCK_SWITCHED_OFF);
    add.accept(TabletKeys.DOCK_HIBERNATING, DOCK_HIBERNATING);
    add.accept(TabletKeys.DOCK_OUT_OF_REACH, DOCK_OUT_OF_REACH);
    addFollow(add);
    addArea(add);
  }

  /** The Area app (answers 1051 to 1060). */
  private static void addArea(BiConsumer<String, String> add) {
    add.accept(TabletKeys.AREA_PICK_CUBIT, AREA_PICK_CUBIT);
    add.accept(TabletKeys.AREA_ONLY_YOURS, AREA_ONLY_YOURS);
    add.accept(TabletKeys.AREA_LOCKED, AREA_LOCKED);
    add.accept(TabletKeys.AREA_FIRST_CORNER, AREA_FIRST_CORNER);
    add.accept(TabletKeys.AREA_FIRST_HINT, AREA_FIRST_HINT);
    add.accept(TabletKeys.AREA_SECOND_CORNER, AREA_SECOND_CORNER);
    add.accept(TabletKeys.AREA_SECOND_HINT, AREA_SECOND_HINT);
    add.accept(TabletKeys.AREA_TOO_BIG, AREA_TOO_BIG);
    add.accept(TabletKeys.AREA_OTHER_DIMENSION, AREA_OTHER_DIMENSION);
    add.accept(TabletKeys.AREA_OUT_OF_REACH, AREA_OUT_OF_REACH);
    add.accept(TabletKeys.AREA_SET, AREA_SET);
    add.accept(TabletKeys.AREA_CANCELLED, AREA_CANCELLED);
    add.accept(TabletKeys.AREA_CUBIT_GONE, AREA_CUBIT_GONE);
    add.accept(TabletKeys.AREA_SIZE, AREA_SIZE);
    add.accept(TabletKeys.AREA_LINE, AREA_LINE);
    add.accept(TabletKeys.AREA_NONE, AREA_NONE);
    add.accept(TabletKeys.AREA_SUMMARY, AREA_SUMMARY);
  }

  /** `Follow` of the header (answers 1073 to 1075). */
  private static void addFollow(BiConsumer<String, String> add) {
    add.accept(TabletKeys.FOLLOW, FOLLOW);
    add.accept(TabletKeys.FOLLOW_DETAIL, FOLLOW_DETAIL);
    add.accept(TabletKeys.FOLLOW_ALREADY, FOLLOW_ALREADY);
    add.accept(TabletKeys.FOLLOW_NO_CHARGE, FOLLOW_NO_CHARGE);
    add.accept(TabletKeys.FOLLOW_OTHER_DIMENSION, FOLLOW_OTHER_DIMENSION);
  }
}
