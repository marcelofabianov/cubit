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
  }
}
