package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.domain.robot.TabletApp;
import io.github.zenndg.cubit.item.TabletItem;
import io.github.zenndg.cubit.menu.DoorKeys;
import java.util.function.BiConsumer;

/** The Doors app of the Tablet, in Portuguese (answers 1364 and 1369). */
final class PortugueseDoorTexts {
  private static final String APP = "Portas";
  private static final String MARKED = "Porta marcada para os Cubits · até %s · Shift desmarca";
  private static final String UNMARKED = "Porta desmarcada";
  private static final String NONE = "Nenhuma porta marcada";
  private static final String FULL = "%s portas marcadas · Shift e um clique desmarcam";
  private static final String ONLY_DOOR = "Portas marca porta, alçapão ou portão de cerca";

  private PortugueseDoorTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(TabletItem.appKey(TabletApp.DOORS), APP);
    add.accept(DoorKeys.MARKED, MARKED);
    add.accept(DoorKeys.UNMARKED, UNMARKED);
    add.accept(DoorKeys.NONE, NONE);
    add.accept(DoorKeys.FULL, FULL);
    add.accept(DoorKeys.ONLY_DOOR, ONLY_DOOR);
  }
}
