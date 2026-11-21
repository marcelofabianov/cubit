package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.domain.robot.TabletApp;
import io.github.zenndg.cubit.item.TabletItem;
import io.github.zenndg.cubit.menu.DoorKeys;
import java.util.function.BiConsumer;

/** The Doors app of the Tablet, in English, the base language (answers 1364 and 1369). */
final class DoorTexts {
  private static final String APP = "Doors";
  private static final String MARKED = "Door marked for your Cubits · up to %s · Shift to unmark";
  private static final String UNMARKED = "Door unmarked";
  private static final String NONE = "No marked door";
  private static final String FULL = "%s doors marked · Shift and a click unmarks";
  private static final String ONLY_DOOR = "Doors marks a door, a trapdoor or a fence gate";

  private DoorTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(TabletItem.appKey(TabletApp.DOORS), APP);
    add.accept(DoorKeys.MARKED, MARKED);
    add.accept(DoorKeys.UNMARKED, UNMARKED);
    add.accept(DoorKeys.NONE, NONE);
    add.accept(DoorKeys.FULL, FULL);
    add.accept(DoorKeys.ONLY_DOOR, ONLY_DOOR);
  }
}
