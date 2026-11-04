package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/** The game decides who drowns by this tag, and a robot does not breathe. */
final class CubitEntityTagsProvider extends EntityTypeTagsProvider {
  CubitEntityTagsProvider(
      PackOutput output,
      CompletableFuture<HolderLookup.Provider> lookup,
      ExistingFileHelper existingFiles) {
    super(output, lookup, CubitMod.MOD_ID, existingFiles);
  }

  @Override
  protected void addTags(HolderLookup.Provider lookup) {
    tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER).add(CubitEntities.GENERAL_ROBOT.get());
  }
}
