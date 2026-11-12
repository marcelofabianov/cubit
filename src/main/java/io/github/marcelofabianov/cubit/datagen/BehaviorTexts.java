package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.domain.robot.PickupMode;
import io.github.marcelofabianov.cubit.menu.BehaviorKeys;
import java.util.function.BiConsumer;

/** The behavior tab, in English, the base language (answer 888). */
final class BehaviorTexts {
  private static final String TAB = "Behavior";
  private static final String PICKUP = "Pickup";
  private static final String PICKUP_DETAIL = "Picks up items it touches";
  private static final String MAGNET = "Magnet";
  private static final String MAGNET_DETAIL = "Pulls items within %s blocks · %s FE per item";
  private static final String FILTER = "Pickup filter";
  private static final String WHITELIST = "Whitelist";
  private static final String BLACKLIST = "Blacklist";
  private static final String TAG = "Tag: %s";

  private BehaviorTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(BehaviorKeys.TAB, TAB);
    add.accept(BehaviorKeys.PICKUP, PICKUP);
    add.accept(BehaviorKeys.PICKUP_DETAIL, PICKUP_DETAIL);
    add.accept(BehaviorKeys.MAGNET, MAGNET);
    add.accept(BehaviorKeys.MAGNET_DETAIL, MAGNET_DETAIL);
    add.accept(BehaviorKeys.FILTER, FILTER);
    add.accept(BehaviorKeys.mode(PickupMode.WHITELIST), WHITELIST);
    add.accept(BehaviorKeys.mode(PickupMode.BLACKLIST), BLACKLIST);
    add.accept(BehaviorKeys.TAG, TAG);
  }
}
