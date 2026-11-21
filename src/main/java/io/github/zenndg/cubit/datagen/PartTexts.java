package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.registry.CubitItems;
import java.util.function.BiConsumer;

/** The parts of the Cubit and of the Power Banks, in English, the base language (answer 1349). */
final class PartTexts {
  private static final String CASING = "Cubit Casing";
  private static final String VISOR = "Cubit Visor";
  private static final String TREADS = "Cubit Treads";
  private static final String CORE = "Cubit Neural Core";
  private static final String POWER_BANK_CASING = "Power Bank Casing";
  private static final String BASE_CELL = "Base Power Cell";
  private static final String ADVANCED_CELL = "Advanced Power Cell";
  private static final String ELITE_CELL = "Elite Power Cell";
  private static final String ULTIMATE_CELL = "Ultimate Power Cell";

  private PartTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(CubitItems.GENERAL_ROBOT_CASING.get().getDescriptionId(), CASING);
    add.accept(CubitItems.GENERAL_ROBOT_VISOR.get().getDescriptionId(), VISOR);
    add.accept(CubitItems.GENERAL_ROBOT_TREADS.get().getDescriptionId(), TREADS);
    add.accept(CubitItems.GENERAL_ROBOT_CORE.get().getDescriptionId(), CORE);
    add.accept(CubitItems.POWER_BANK_CASING.get().getDescriptionId(), POWER_BANK_CASING);
    add.accept(CubitItems.BASE_POWER_CELL.get().getDescriptionId(), BASE_CELL);
    add.accept(CubitItems.ADVANCED_POWER_CELL.get().getDescriptionId(), ADVANCED_CELL);
    add.accept(CubitItems.ELITE_POWER_CELL.get().getDescriptionId(), ELITE_CELL);
    add.accept(CubitItems.ULTIMATE_POWER_CELL.get().getDescriptionId(), ULTIMATE_CELL);
  }
}
