package io.github.zenndg.cubit.domain.robot;

import java.util.ArrayList;
import java.util.List;

/**
 * The apps of the Tablet that make sense for the chosen Cubit: Open, Dock, Area and Unload always,
 * Target only with the chosen Cubit loaded and in Focus, and Mark only with the card of the
 * Organizer. Shift and the wheel go only through these, and an app that stops making sense goes
 * back to Open (answers 1006, 1044 and 1297).
 */
public record TabletApps(boolean targetAvailable, boolean markAvailable) {
  /** Without the Organizer, as before the Mark app existed. */
  public TabletApps(boolean targetAvailable) {
    this(targetAvailable, false);
  }

  public List<TabletApp> available() {
    List<TabletApp> apps = new ArrayList<>();
    for (TabletApp app : TabletApp.values()) {
      if ((app != TabletApp.TARGET || targetAvailable)
          && (app != TabletApp.MARK || markAvailable)) {
        apps.add(app);
      }
    }
    return apps;
  }

  /** The app the click uses: the active one, or Open when the active one stopped making sense. */
  public TabletApp effective(TabletApp active) {
    return available().contains(active) ? active : TabletApp.OPEN;
  }

  /** The next app in the direction of the wheel, going around, from the app the click would use. */
  public TabletApp next(TabletApp active, int direction) {
    List<TabletApp> apps = available();
    int index = apps.indexOf(effective(active));
    int step = Integer.signum(direction);
    return apps.get(Math.floorMod(index + step, apps.size()));
  }
}
