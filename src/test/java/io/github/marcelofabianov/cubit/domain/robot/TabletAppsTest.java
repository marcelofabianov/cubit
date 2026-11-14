package io.github.marcelofabianov.cubit.domain.robot;

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
        List.of(TabletApp.OPEN, TabletApp.TARGET, TabletApp.DOCK), WITH_TARGET.available());
    assertEquals(List.of(TabletApp.OPEN, TabletApp.DOCK), WITHOUT_TARGET.available());
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
    assertEquals(TabletApp.OPEN, WITH_TARGET.next(TabletApp.DOCK, FORWARD));
    assertEquals(TabletApp.DOCK, WITH_TARGET.next(TabletApp.OPEN, BACKWARD));
    assertEquals(TabletApp.DOCK, WITHOUT_TARGET.next(TabletApp.OPEN, FORWARD));
  }

  @Test
  void theWheelStartsFromOpenWhenTheActiveAppStoppedMakingSense() {
    assertEquals(TabletApp.DOCK, WITHOUT_TARGET.next(TabletApp.TARGET, FORWARD));
    assertEquals(TabletApp.OPEN, WITHOUT_TARGET.next(TabletApp.TARGET, STILL));
  }
}
