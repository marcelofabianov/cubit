package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.item.PowerBankItem;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.function.BiConsumer;

/** The Power Bank, in English, the base language (answers 909, 917 and 921). */
final class PowerBankTexts {
  private static final String BASE = "Base Power Bank";
  private static final String ADVANCED = "Advanced Power Bank";
  private static final String ELITE = "Elite Power Bank";
  private static final String ULTIMATE = "Ultimate Power Bank";
  private static final String AMOUNT = "%s / %s FE";
  private static final String CHARGES_AT_DOCK = "Charges at the Induction Dock";

  private PowerBankTexts() {}

  static void addTo(BiConsumer<String, String> add) {
    add.accept(CubitItems.BASE_POWER_BANK.get().getDescriptionId(), BASE);
    add.accept(CubitItems.ADVANCED_POWER_BANK.get().getDescriptionId(), ADVANCED);
    add.accept(CubitItems.ELITE_POWER_BANK.get().getDescriptionId(), ELITE);
    add.accept(CubitItems.ULTIMATE_POWER_BANK.get().getDescriptionId(), ULTIMATE);
    add.accept(PowerBankItem.AMOUNT_KEY, AMOUNT);
    add.accept(PowerBankItem.CHARGES_AT_DOCK_KEY, CHARGES_AT_DOCK);
  }
}
