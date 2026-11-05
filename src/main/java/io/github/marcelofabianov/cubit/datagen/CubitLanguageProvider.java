package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.registry.CubitBlocks;
import io.github.marcelofabianov.cubit.registry.CubitCreativeTabs;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

/** The only language file of the mod: everything the player reads is in English. */
final class CubitLanguageProvider extends LanguageProvider {
  private static final String LOCALE = "en_us";
  private static final String ROBOT_NAME = "Cubit";
  private static final String DAMAGED_ROBOT_NAME = "Damaged Cubit";
  private static final String TOOLBOX_NAME = "Toolbox";
  private static final String DOCK_NAME = "Induction Dock";

  CubitLanguageProvider(PackOutput output) {
    super(output, CubitMod.MOD_ID, LOCALE);
  }

  @Override
  protected void addTranslations() {
    add(CubitItems.GENERAL_ROBOT.get(), ROBOT_NAME);
    add(CubitEntities.GENERAL_ROBOT.get(), ROBOT_NAME);
    add(CubitCreativeTabs.MAIN_TITLE_KEY, ROBOT_NAME);
    add(CubitItems.DAMAGED_GENERAL_ROBOT.get(), DAMAGED_ROBOT_NAME);
    add(CubitItems.TOOLBOX.get(), TOOLBOX_NAME);
    add(CubitBlocks.INDUCTION_DOCK.get(), DOCK_NAME);
  }
}
