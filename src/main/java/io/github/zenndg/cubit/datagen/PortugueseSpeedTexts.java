package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.menu.ScreenKeys;
import java.util.function.BiConsumer;

/** The speed row, in Portuguese, with the same keys as the English one (answer 1271). */
final class PortugueseSpeedTexts {
  private static final String SPEED = "Velocidade";
  private static final String RANGE = "Velocidade ao andar, de %s%% a 100%% do tier";
  private static final String COST = " · mais devagar gasta mais FE por caminho";
  private static final String DETAIL = RANGE + COST;
  private static final String VALUE = "%s%%";

  private PortugueseSpeedTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(ScreenKeys.SPEED, SPEED);
    add.accept(ScreenKeys.SPEED_DETAIL, DETAIL);
    add.accept(ScreenKeys.SPEED_VALUE, VALUE);
  }
}
