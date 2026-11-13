package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.domain.robot.PickupMode;
import io.github.marcelofabianov.cubit.menu.BehaviorKeys;
import java.util.function.BiConsumer;

/** The behavior tab, in Portuguese (answer 888). */
final class PortugueseBehaviorTexts {
  private static final String TAB = "Comportamento";
  private static final String PICKUP = "Coleta";
  private static final String PICKUP_DETAIL = "Pega os itens que toca";
  private static final String MAGNET = "Ímã";
  private static final String MAGNET_DETAIL = "Puxa itens a até %s blocos · %s FE por item";
  private static final String FETCH = "Buscar";
  private static final String FETCH_DETAIL = "Busca itens a até %s blocos de você";
  private static final String FILTER = "Filtro de coleta";
  private static final String WHITELIST = "Lista branca";
  private static final String BLACKLIST = "Lista negra";
  private static final String TAG = "Tag: %s";

  private PortugueseBehaviorTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(BehaviorKeys.TAB, TAB);
    add.accept(BehaviorKeys.PICKUP, PICKUP);
    add.accept(BehaviorKeys.PICKUP_DETAIL, PICKUP_DETAIL);
    add.accept(BehaviorKeys.MAGNET, MAGNET);
    add.accept(BehaviorKeys.MAGNET_DETAIL, MAGNET_DETAIL);
    add.accept(BehaviorKeys.FETCH, FETCH);
    add.accept(BehaviorKeys.FETCH_DETAIL, FETCH_DETAIL);
    add.accept(BehaviorKeys.FILTER, FILTER);
    add.accept(BehaviorKeys.mode(PickupMode.WHITELIST), WHITELIST);
    add.accept(BehaviorKeys.mode(PickupMode.BLACKLIST), BLACKLIST);
    add.accept(BehaviorKeys.TAG, TAG);
  }
}
