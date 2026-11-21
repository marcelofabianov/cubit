package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.block.DeskFirstNotice;
import io.github.zenndg.cubit.block.DeskUpgrade;
import io.github.zenndg.cubit.domain.desk.DeskStatus;
import io.github.zenndg.cubit.item.EngineeringDeskItem;
import io.github.zenndg.cubit.menu.DeskScreenKeys;
import io.github.zenndg.cubit.registry.CubitBlocks;
import java.util.Map;
import java.util.function.BiConsumer;

/** The Engineering Desk, in Portuguese, with the same keys as English (answer 1458). */
final class PortugueseDeskTexts {
  private static final String NAME = "Mesa de Engenharia";
  private static final String UPGRADED = "Mesa de Engenharia evoluída para %s!";
  private static final String NEEDS_TIER = "A Mesa de Engenharia precisa ser %s antes";
  private static final String ALREADY_TIER = "A Mesa de Engenharia já é %s";
  private static final String FIRST = "Escolha uma receita no catálogo e dê energia por baixo.";
  private static final String TOOLTIP_TIER = "Tier: %s";
  private static final String TOOLTIP_ENERGY = "Energia: %s FE";
  private static final String TOOLTIP_ORDER = "Pedido: %s / %s";
  private static final String TOOLTIP_ITEMS = "Guarda %s itens";
  private static final String TITLE_TIER = "%s · %s";
  private static final String PROJECT = "Projeto";
  private static final String STORAGE = "Estoque";
  private static final String INPUT = "Entrada";
  private static final String OUTPUT = "Saída";
  private static final String STORED_ENERGY = "Energia armazenada";
  private static final String ORDER = "%s / %s";
  private static final String CANCEL = "Cancelar";
  private static final String LOCKED = "Bloqueada";
  private static final String CATALOG = "Catálogo";
  private static final String NO_RECIPE = "Escolha uma receita";
  private static final String COST = "%s FE por item";
  private static final String STATUS_IDLE = "Parada";
  private static final String STATUS_CRAFTING = "Criando";
  private static final String STATUS_REDSTONE = "Sinal de redstone";
  private static final String STATUS_MISSING = "Faltam materiais";
  private static final String STATUS_REFUSED = "Receita recusada";
  private static final String STATUS_NO_ENERGY = "Sem energia";
  private static final String STATUS_OUTPUT_FULL = "Saída cheia";
  private static final Map<DeskStatus, String> STATUS =
      Map.of(
          DeskStatus.IDLE, STATUS_IDLE,
          DeskStatus.CRAFTING, STATUS_CRAFTING,
          DeskStatus.REDSTONE, STATUS_REDSTONE,
          DeskStatus.MISSING_MATERIALS, STATUS_MISSING,
          DeskStatus.REFUSED, STATUS_REFUSED,
          DeskStatus.NO_ENERGY, STATUS_NO_ENERGY,
          DeskStatus.OUTPUT_FULL, STATUS_OUTPUT_FULL);

  private PortugueseDeskTexts() {}

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
