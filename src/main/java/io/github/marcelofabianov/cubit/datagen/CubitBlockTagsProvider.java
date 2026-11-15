package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.entity.FarmPlants;
import io.github.marcelofabianov.cubit.registry.CubitBlocks;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * The block tags of the mod: the Dock, and the plants the Farmer adds or leaves out, which a pack
 * may edit (answer 1222). The stems give fruit, and the berry bush would be broken.
 */
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
    tag(FarmPlants.CROPS);
    tag(FarmPlants.IGNORED)
        .addTag(BlockTags.SAPLINGS)
        .add(
            Blocks.BAMBOO,
            Blocks.BAMBOO_SAPLING,
            Blocks.MELON_STEM,
            Blocks.PUMPKIN_STEM,
            Blocks.ATTACHED_MELON_STEM,
            Blocks.ATTACHED_PUMPKIN_STEM,
            Blocks.SWEET_BERRY_BUSH);
  }
}
