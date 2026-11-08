package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.registry.CubitBlocks;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

final class CubitBlockTagsProvider extends BlockTagsProvider {
  CubitBlockTagsProvider(
      PackOutput output,
      CompletableFuture<HolderLookup.Provider> lookup,
      ExistingFileHelper existingFiles) {
    super(output, lookup, CubitMod.MOD_ID, existingFiles);
  }

  @Override
  protected void addTags(HolderLookup.Provider lookup) {
    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(CubitBlocks.INDUCTION_DOCK.get());
  }
}
