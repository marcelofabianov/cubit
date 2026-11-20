package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.domain.robot.OrganizerMode;
import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.domain.robot.TabletApp;
import io.github.zenndg.cubit.item.TabletItem;
import io.github.zenndg.cubit.menu.TabletKeys;
import io.github.zenndg.cubit.menu.WorkKeys;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.function.BiConsumer;

/** The Organizer, its Mark app and its Supply tab, in English, the base language (1285 to 1304). */
final class OrganizerTexts {
  private static final String JOB = "Organizer";
  private static final String CARD = "Machine Learning: Organizer";
  private static final String APP = "Mark";
  private static final String ONLY_INVENTORY = "Mark marks a block with an inventory";
  private static final String MARKED = "Block marked for %s · up to %s · Shift to unmark";
  private static final String UNMARKED = "Block unmarked for %s";
  private static final String NONE = "%s has no marked block";
  private static final String FULL = "%s has %s marks · Shift and a click unmarks";
  private static final String NUMBER = "Mark %s";
  private static final String NO_ROOM = "No room for some items";
  private static final String MODE = "Mode";
  private static final String MODE_DETAIL = "Organize keeps the base tidy · Supply fills the marks";
  private static final String ORGANIZE = "Organize";
  private static final String SUPPLY = "Supply";
  private static final String SUPPLY_TITLE = "Supply · %s of %s";
  private static final String SUPPLY_EMPTY = "Mark destinations with the Mark app";
  private static final String CHOOSE = "Click holding an item to choose it";
  private static final String CLEAR = " · empty hand clears";
  private static final String ITEM_DETAIL = CHOOSE + CLEAR;
  private static final String AMOUNT_DETAIL = "Up to this many · Shift goes 8 at a time";

  private OrganizerTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(WorkKeys.job(RobotJob.ORGANIZER), JOB);
    add.accept(CubitItems.ORGANIZER_CARD.get().getDescriptionId(), CARD);
    add.accept(TabletItem.appKey(TabletApp.MARK), APP);
    add.accept(TabletKeys.MARK_ONLY_INVENTORY, ONLY_INVENTORY);
    add.accept(TabletKeys.MARK_MARKED, MARKED);
    add.accept(TabletKeys.MARK_UNMARKED, UNMARKED);
    add.accept(TabletKeys.MARK_NONE, NONE);
    add.accept(TabletKeys.MARK_FULL, FULL);
    add.accept(TabletKeys.MARK_NUMBER, NUMBER);
    add.accept(WorkKeys.ORGANIZER_NO_ROOM, NO_ROOM);
    add.accept(WorkKeys.ORGANIZER_MODE, MODE);
    add.accept(WorkKeys.ORGANIZER_MODE_DETAIL, MODE_DETAIL);
    add.accept(WorkKeys.organizerMode(OrganizerMode.ORGANIZE.key()), ORGANIZE);
    add.accept(WorkKeys.organizerMode(OrganizerMode.SUPPLY.key()), SUPPLY);
    add.accept(WorkKeys.SUPPLY_TAB, SUPPLY);
    add.accept(WorkKeys.SUPPLY_TITLE, SUPPLY_TITLE);
    add.accept(WorkKeys.SUPPLY_EMPTY, SUPPLY_EMPTY);
    add.accept(WorkKeys.SUPPLY_ITEM_DETAIL, ITEM_DETAIL);
    add.accept(WorkKeys.SUPPLY_AMOUNT_DETAIL, AMOUNT_DETAIL);
  }
}
