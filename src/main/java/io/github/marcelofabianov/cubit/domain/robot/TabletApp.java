package io.github.marcelofabianov.cubit.domain.robot;

import java.util.Locale;

/**
 * The apps of the Tablet, one active at a time (answers 999 and 1006). The key is the name in the
 * component of the item, a contract with the saves (answer 1026).
 */
public enum TabletApp {
  OPEN,
  TARGET,
  DOCK;

  public String key() {
    return name().toLowerCase(Locale.ROOT);
  }

  /** A missing or unknown key falls back to Open, which always exists. */
  public static TabletApp fromKeyOrOpen(String key) {
    for (TabletApp app : values()) {
      if (app.key().equals(key)) {
        return app;
      }
    }
    return OPEN;
  }
}
