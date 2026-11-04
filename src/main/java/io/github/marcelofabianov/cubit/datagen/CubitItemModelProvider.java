package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

final class CubitItemModelProvider extends ItemModelProvider {
  CubitItemModelProvider(PackOutput output, ExistingFileHelper existingFiles) {
    super(output, CubitMod.MOD_ID, existingFiles);
  }

  @Override
  protected void registerModels() {
    basicItem(CubitItems.GENERAL_ROBOT.get());
  }
}
