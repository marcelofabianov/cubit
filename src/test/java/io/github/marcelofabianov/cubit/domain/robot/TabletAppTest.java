package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TabletAppTest {
  private static final String UNKNOWN = "teleport";

  @Test
  void theKeyGoesBackToItsApp() {
    for (TabletApp app : TabletApp.values()) {
      assertEquals(app, TabletApp.fromKeyOrOpen(app.key()));
    }
  }

  @Test
  void anUnknownKeyOpens() {
    assertEquals(TabletApp.OPEN, TabletApp.fromKeyOrOpen(UNKNOWN));
  }
}
