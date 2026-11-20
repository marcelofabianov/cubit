package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.entity.FishingCatch;
import io.github.zenndg.cubit.item.JobCardItem;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * The cards the card slot takes and the junk the Fisher may leave out, as in the junk of the
 * fishing table of the game, so a pack can edit both (answers 1162 and 1207).
 */
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
    tag(JobCardItem.CARDS)
        .add(
            CubitItems.FISHER_CARD.get(),
            CubitItems.FARMER_CARD.get(),
            CubitItems.ORGANIZER_CARD.get());
    tag(FishingCatch.JUNK)
        .add(
            Items.LILY_PAD,
            Items.LEATHER_BOOTS,
            Items.LEATHER,
            Items.BONE,
            Items.POTION,
            Items.STRING,
            Items.FISHING_ROD,
            Items.BOWL,
            Items.STICK,
            Items.INK_SAC,
            Items.TRIPWIRE_HOOK,
            Items.ROTTEN_FLESH,
            Items.BAMBOO);
  }
}
