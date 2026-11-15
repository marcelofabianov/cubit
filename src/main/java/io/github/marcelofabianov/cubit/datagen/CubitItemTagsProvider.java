package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.item.JobCardItem;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/** The cards the card slot takes, so a pack can add or remove one (answer 1162). */
final class CubitItemTagsProvider extends ItemTagsProvider {
  CubitItemTagsProvider(
      PackOutput output,
      CompletableFuture<HolderLookup.Provider> lookup,
      CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
      ExistingFileHelper existingFiles) {
    super(output, lookup, blockTags, CubitMod.MOD_ID, existingFiles);
  }

  @Override
  protected void addTags(HolderLookup.Provider lookup) {
    tag(JobCardItem.CARDS).add(CubitItems.FISHER_CARD.get());
  }
}
