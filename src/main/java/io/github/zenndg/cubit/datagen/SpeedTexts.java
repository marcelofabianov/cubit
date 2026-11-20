package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.menu.ScreenKeys;
import java.util.function.BiConsumer;

/** The speed row, in English, the base language (answers 1269 and 1271). */
final class SpeedTexts {
  private static final String SPEED = "Speed";
  private static final String RANGE = "Walking speed, %s%% to 100%% of the tier";
  private static final String COST = " · slower costs more FE per trip";
  private static final String DETAIL = RANGE + COST;
  private static final String VALUE = "%s%%";

  private SpeedTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(ScreenKeys.SPEED, SPEED);
    add.accept(ScreenKeys.SPEED_DETAIL, DETAIL);
    add.accept(ScreenKeys.SPEED_VALUE, VALUE);
  }
}
