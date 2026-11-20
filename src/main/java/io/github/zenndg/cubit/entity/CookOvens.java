package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.CubitMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;

/**
 * What the Cook knows of the ovens of the game: a smoker or a furnace cooks food, and the blast
 * furnace, which is not a furnace of the game, does not (answer 1329). Raw food is what the smoker
 * cooks, also from other mods, so the Cook never smelts ore in a furnace (answer 1317).
 */
public final class CookOvens {
  private static final String FUELS_PATH = "cook_fuels";

  /** The fuels the Cook burns, which the pack may edit (answer 1319). */
  public static final TagKey<Item> FUELS =
      TagKey.create(
          Registries.ITEM, ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, FUELS_PATH));

  private CookOvens() {}

  /** Checks the chunk first: reading a block must never load one (AGENTS.md, rule 22). */
  static boolean isOven(Level level, BlockPos pos) {
    if (!level.hasChunkAt(pos)) {
      return false;
    }
    BlockEntity entity = level.getBlockEntity(pos);
    return entity instanceof SmokerBlockEntity || entity instanceof FurnaceBlockEntity;
  }

  static boolean isSmoker(Level level, BlockPos pos) {
    return level.hasChunkAt(pos) && level.getBlockEntity(pos) instanceof SmokerBlockEntity;
  }

  /** A fuel of the tag, the only kind the Cook takes from the chests (answer 1319). */
  static boolean isFuel(ItemStack stack) {
    return stack.is(FUELS);
  }

  /** An item the smoker of the game cooks, by its recipes, also from other mods. */
  static boolean isRaw(Level level, ItemStack stack) {
    return !stack.isEmpty()
        && level
            .getRecipeManager()
            .getRecipeFor(RecipeType.SMOKING, new SingleRecipeInput(stack), level)
            .isPresent();
  }
}
