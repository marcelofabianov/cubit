package io.github.zenndg.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class TabletAppsTest {
  private static final TabletApps WITH_TARGET = new TabletApps(true);
  private static final TabletApps WITHOUT_TARGET = new TabletApps(false);
  private static final int FORWARD = 1;
  private static final int BACKWARD = -1;
  private static final int STILL = 0;

  @Test
  void targetOnlyShowsWithTheChosenCubitInFocus() {
    assertEquals(
        List.of(
            TabletApp.OPEN,
            TabletApp.TARGET,
            TabletApp.DOCK,
            TabletApp.AREA,
            TabletApp.UNLOAD,
            TabletApp.DOORS),
        WITH_TARGET.available());
    assertEquals(
        List.of(TabletApp.OPEN, TabletApp.DOCK, TabletApp.AREA, TabletApp.UNLOAD, TabletApp.DOORS),
        WITHOUT_TARGET.available());
  }

  @Test
  void anAppThatStoppedMakingSenseGoesBackToOpen() {
    assertEquals(TabletApp.OPEN, WITHOUT_TARGET.effective(TabletApp.TARGET));
    assertEquals(TabletApp.DOCK, WITHOUT_TARGET.effective(TabletApp.DOCK));
    assertEquals(TabletApp.TARGET, WITH_TARGET.effective(TabletApp.TARGET));
  }

  @Test
  void theWheelGoesAroundTheAvailableApps() {
    assertEquals(TabletApp.TARGET, WITH_TARGET.next(TabletApp.OPEN, FORWARD));
    assertEquals(TabletApp.AREA, WITH_TARGET.next(TabletApp.DOCK, FORWARD));
    assertEquals(TabletApp.UNLOAD, WITH_TARGET.next(TabletApp.AREA, FORWARD));
    assertEquals(TabletApp.DOORS, WITH_TARGET.next(TabletApp.UNLOAD, FORWARD));
    assertEquals(TabletApp.OPEN, WITH_TARGET.next(TabletApp.DOORS, FORWARD));
    assertEquals(TabletApp.DOORS, WITH_TARGET.next(TabletApp.OPEN, BACKWARD));
    assertEquals(TabletApp.DOCK, WITHOUT_TARGET.next(TabletApp.OPEN, FORWARD));
  }

  @Test
  void theWheelStartsFromOpenWhenTheActiveAppStoppedMakingSense() {
    assertEquals(TabletApp.DOCK, WITHOUT_TARGET.next(TabletApp.TARGET, FORWARD));
    assertEquals(TabletApp.OPEN, WITHOUT_TARGET.next(TabletApp.TARGET, STILL));
  }

  /** Mark only shows with the card of the Organizer, after Unload (answer 1297). */
  @Test
  void markOnlyWithTheOrganizer() {
    TabletApps organizer = new TabletApps(false, true);
    assertEquals(
        List.of(
            TabletApp.OPEN,
            TabletApp.DOCK,
            TabletApp.AREA,
            TabletApp.UNLOAD,
            TabletApp.MARK,
            TabletApp.DOORS),
        organizer.available());
    assertEquals(TabletApp.OPEN, WITHOUT_TARGET.effective(TabletApp.MARK));
    assertEquals(TabletApp.MARK, organizer.effective(TabletApp.MARK));
  }
}
