package io.github.zenndg.cubit.block;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.config.CubitConfig;
import io.github.zenndg.cubit.item.PowerBankItem;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/**
 * The order of the catalog of the desk: the order in which the recipes unlock, from the Cubit and
 * its parts to the chains of the Power Banks and the repair (answers 1498 and 1499). A recipe out
 * of this list, as one a pack added, comes last, in the order of its ID.
 */
final class DeskCatalogOrder {
  private static final String FROM_CASING = "_from_casing";
  private static final String RECHARGE = "recharge_";
  private static final String REPAIR = "repair_";

  /** By the item each recipe makes, which is also the name of the recipe. */
  private static final List<Supplier<Item>> BY_ITEM =
      List.of(
          CubitItems.GENERAL_ROBOT_CASING,
          CubitItems.GENERAL_ROBOT_VISOR,
          CubitItems.GENERAL_ROBOT_TREADS,
          CubitItems.GENERAL_ROBOT_CORE,
          CubitItems.GENERAL_ROBOT,
          CubitItems.TOOLBOX,
          CubitItems.INDUCTION_DOCK,
          CubitItems.TABLET,
          CubitItems.POWER_BANK_CASING,
          CubitItems.BASE_POWER_CELL,
          CubitItems.ADVANCED_MODULE,
          CubitItems.FISHER_CARD,
          CubitItems.FARMER_CARD,
          CubitItems.ORGANIZER_CARD,
          CubitItems.COOK_CARD,
          CubitItems.RECHARGE_SPLASH,
          CubitItems.ELITE_MODULE,
          CubitItems.ULTIMATE_MODULE,
          CubitItems.BASE_POWER_BANK,
          CubitItems.ADVANCED_POWER_CELL,
          CubitItems.ADVANCED_POWER_BANK,
          CubitItems.ELITE_POWER_CELL,
          CubitItems.ELITE_POWER_BANK,
          CubitItems.ULTIMATE_POWER_CELL,
          CubitItems.ULTIMATE_POWER_BANK,
          CubitItems.DOCK_SOLAR_PANEL,
          CubitItems.SOLAR_INDUCTION_DOCK);

  private static List<String> order;

  private DeskCatalogOrder() {}

  /** The place of a recipe in the catalog. One out of the list goes after all of them. */
  static int rank(ResourceLocation id) {
    List<String> paths = order();
    int index = CubitMod.MOD_ID.equals(id.getNamespace()) ? paths.indexOf(id.getPath()) : -1;
    return index < 0 ? paths.size() : index;
  }

  /** Read once the items exist. Each bank made from its casing comes right after its upgrade. */
  private static synchronized List<String> order() {
    if (order == null) {
      List<String> paths = new ArrayList<>();
      for (Supplier<Item> item : BY_ITEM) {
        String path = BuiltInRegistries.ITEM.getKey(item.get()).getPath();
        paths.add(path);
        if (item.get() instanceof PowerBankItem) {
          paths.add(path + FROM_CASING);
        }
      }
      paths.add(RECHARGE + CubitConfig.GENERAL_ROBOT.id());
      paths.add(REPAIR + CubitConfig.GENERAL_ROBOT.id());
      order = List.copyOf(paths);
    }
    return order;
  }
}
