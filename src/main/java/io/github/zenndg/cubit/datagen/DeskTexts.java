package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.block.DeskFirstNotice;
import io.github.zenndg.cubit.block.DeskUpgrade;
import io.github.zenndg.cubit.domain.desk.DeskStatus;
import io.github.zenndg.cubit.item.EngineeringDeskItem;
import io.github.zenndg.cubit.menu.DeskScreenKeys;
import io.github.zenndg.cubit.registry.CubitBlocks;
import java.util.Map;
import java.util.function.BiConsumer;

/** The Engineering Desk, in English, the base language (answers 1424 to 1494). */
final class DeskTexts {
  private static final String NAME = "Engineering Desk";
  private static final String UPGRADED = "Engineering Desk upgraded to %s!";
  private static final String NEEDS_TIER = "The Engineering Desk needs to be %s first";
  private static final String ALREADY_TIER = "The Engineering Desk is already %s";
  private static final String FIRST = "Pick a recipe in the catalog and power the desk from below.";
  private static final String TOOLTIP_TIER = "Tier: %s";
  private static final String TOOLTIP_ENERGY = "Energy: %s FE";
  private static final String TOOLTIP_ORDER = "Order: %s / %s";
  private static final String TOOLTIP_ITEMS = "Keeps %s items";
  private static final String TITLE_TIER = "%s · %s";
  private static final String PROJECT = "Project";
  private static final String STORAGE = "Storage";
  private static final String INPUT = "Input";
  private static final String OUTPUT = "Output";
  private static final String STORED_ENERGY = "Stored energy";
  private static final String ORDER = "%s / %s";
  private static final String CANCEL = "Cancel";
  private static final String LOCKED = "Locked";
  private static final String CATALOG = "Catalog";
  private static final String NO_RECIPE = "Pick a recipe";
  private static final String COST = "%s FE per item";
  private static final String STATUS_IDLE = "Idle";
  private static final String STATUS_CRAFTING = "Crafting";
  private static final String STATUS_REDSTONE = "Redstone signal";
  private static final String STATUS_MISSING = "Missing materials";
  private static final String STATUS_REFUSED = "Recipe refused";
  private static final String STATUS_NO_ENERGY = "No energy";
  private static final String STATUS_OUTPUT_FULL = "Output full";
  private static final Map<DeskStatus, String> STATUS =
      Map.of(
          DeskStatus.IDLE, STATUS_IDLE,
          DeskStatus.CRAFTING, STATUS_CRAFTING,
          DeskStatus.REDSTONE, STATUS_REDSTONE,
          DeskStatus.MISSING_MATERIALS, STATUS_MISSING,
          DeskStatus.REFUSED, STATUS_REFUSED,
          DeskStatus.NO_ENERGY, STATUS_NO_ENERGY,
          DeskStatus.OUTPUT_FULL, STATUS_OUTPUT_FULL);

  private DeskTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(CubitBlocks.ENGINEERING_DESK.get().getDescriptionId(), NAME);
    add.accept(DeskUpgrade.UPGRADED_KEY, UPGRADED);
    add.accept(DeskUpgrade.NEEDS_TIER_KEY, NEEDS_TIER);
    add.accept(DeskUpgrade.ALREADY_TIER_KEY, ALREADY_TIER);
    add.accept(DeskFirstNotice.KEY, FIRST);
    add.accept(EngineeringDeskItem.TIER_KEY, TOOLTIP_TIER);
    add.accept(EngineeringDeskItem.ENERGY_KEY, TOOLTIP_ENERGY);
    add.accept(EngineeringDeskItem.ORDER_KEY, TOOLTIP_ORDER);
    add.accept(EngineeringDeskItem.ITEMS_KEY, TOOLTIP_ITEMS);
    addScreen(add);
  }

  private static void addScreen(BiConsumer<String, String> add) {
    add.accept(DeskScreenKeys.TITLE_TIER, TITLE_TIER);
    add.accept(DeskScreenKeys.PROJECT_TAB, PROJECT);
    add.accept(DeskScreenKeys.STORAGE_TAB, STORAGE);
    add.accept(DeskScreenKeys.INPUT, INPUT);
    add.accept(DeskScreenKeys.OUTPUT, OUTPUT);
    add.accept(DeskScreenKeys.STORED_ENERGY, STORED_ENERGY);
    add.accept(DeskScreenKeys.ORDER, ORDER);
    add.accept(DeskScreenKeys.CANCEL, CANCEL);
    add.accept(DeskScreenKeys.LOCKED, LOCKED);
    add.accept(DeskScreenKeys.CATALOG, CATALOG);
    add.accept(DeskScreenKeys.NO_RECIPE, NO_RECIPE);
    add.accept(DeskScreenKeys.COST, COST);
    STATUS.forEach((status, text) -> add.accept(DeskScreenKeys.status(status), text));
  }
}
