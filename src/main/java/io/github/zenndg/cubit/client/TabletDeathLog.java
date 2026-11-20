package io.github.zenndg.cubit.client;

import io.github.zenndg.cubit.network.DeathLogPayload;
import java.util.List;

/** The last death log the server sent, which the Deaths tab of the Tablet shows (answer 1111). */
final class TabletDeathLog {
  private static final DeathLogPayload EMPTY =
      new DeathLogPayload(List.of(), DeathLogPayload.NOT_GUIDING, DeathLogPayload.NO_GUIDE_TIER);

  private static DeathLogPayload current = EMPTY;

  private TabletDeathLog() {}

  static void show(DeathLogPayload log) {
    current = log;
  }

  static DeathLogPayload current() {
    return current;
  }
}
