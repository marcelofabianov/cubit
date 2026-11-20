package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.behavior.CropRipeness;
import io.github.zenndg.cubit.domain.behavior.TillRules;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;

/**
 * How the Farmer reads a block of its area: a ripe plant of any mod, a tall plant to cut, a fruit
 * to pick, ground to till by the water or empty farmland to plant (answers 1219, 1222 and 1232).
 * The caller checks the chunk first (rule 22).
 */
public final class FarmPlants {
  private static final String CROPS_PATH = "farmer_crops";
  private static final String IGNORED_PATH = "farmer_ignored";
  private static final String AGE = "age";

  /** The water that keeps farmland wet, as FarmBlock.isNearWater reads it. */
  private static final int WATER_REACH = 4;

  /** Plants the Farmer harvests besides those that grow by age, which a pack may edit. */
  public static final TagKey<Block> CROPS = tag(CROPS_PATH);

  /** Plants the Farmer never harvests, such as saplings and bamboo. */
  public static final TagKey<Block> IGNORED = tag(IGNORED_PATH);

  private FarmPlants() {}

  private static TagKey<Block> tag(String path) {
    return TagKey.create(
        Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, path));
  }

  /** What the Farmer would do at this block, if anything. */
  static Optional<FarmAction> actionAt(Level level, BlockPos pos, boolean special) {
    BlockState state = level.getBlockState(pos);
    if (ripe(state)) {
      return Optional.of(FarmAction.HARVEST);
    }
    if (special && (tall(level, pos, state) || fruit(level, pos))) {
      return Optional.of(tall(level, pos, state) ? FarmAction.TALL : FarmAction.FRUIT);
    }
    if (state.getBlock() instanceof FarmBlock && level.isEmptyBlock(pos.above())) {
      return Optional.of(FarmAction.PLANT);
    }
    return tillable(level, pos, state) ? Optional.of(FarmAction.TILL) : Optional.empty();
  }

  static boolean ripe(BlockState state) {
    Optional<IntegerProperty> age = ageOf(state);
    OptionalInt value =
        age.map(property -> OptionalInt.of(state.getValue(property))).orElse(OptionalInt.empty());
    int max = age.map(FarmPlants::maxOf).orElse(0);
    boolean plant = state.getBlock() instanceof BushBlock;
    return CropRipeness.ripe(plant, value, max, state.is(CROPS), state.is(IGNORED));
  }

  /** A cane or a cactus one block over its base, which stays. */
  static boolean tall(Level level, BlockPos pos, BlockState state) {
    Block block = state.getBlock();
    return (block instanceof SugarCaneBlock || block instanceof CactusBlock)
        && level.getBlockState(pos.below()).is(block)
        && !level.getBlockState(pos.below(2)).is(block);
  }

  /** A pumpkin or a melon, or any fruit, that a grown stem points at. */
  static boolean fruit(Level level, BlockPos pos) {
    for (Direction side : Direction.Plane.HORIZONTAL) {
      BlockState stem = level.getBlockState(pos.relative(side));
      if (stem.getBlock() instanceof AttachedStemBlock
          && stem.getValue(AttachedStemBlock.FACING) == side.getOpposite()) {
        return true;
      }
    }
    return false;
  }

  static boolean tillable(Level level, BlockPos pos, BlockState state) {
    BlockState tilled = tilledState(level, pos, state, true);
    boolean makesFarmland = tilled != null && tilled.getBlock() instanceof FarmBlock;
    return TillRules.tills(
        makesFarmland,
        level.isEmptyBlock(pos.above()),
        makesFarmland && nearWater(level, pos),
        false);
  }

  /** What the hoe of the game turns the block into, with no player, as HoeItem does. */
  static BlockState tilledState(Level level, BlockPos pos, BlockState state, boolean simulate) {
    BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false);
    UseOnContext context =
        new UseOnContext(
            level, null, InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_HOE), hit);
    return state.getToolModifiedState(context, ItemAbilities.HOE_TILL, simulate);
  }

  /** As FarmBlock.isNearWater, only over loaded blocks (rule 22). */
  private static boolean nearWater(Level level, BlockPos pos) {
    for (BlockPos near :
        BlockPos.betweenClosed(
            pos.offset(-WATER_REACH, 0, -WATER_REACH), pos.offset(WATER_REACH, 1, WATER_REACH))) {
      if (level.isLoaded(near) && level.getFluidState(near).is(FluidTags.WATER)) {
        return true;
      }
    }
    return false;
  }

  private static Optional<IntegerProperty> ageOf(BlockState state) {
    for (Property<?> property : state.getProperties()) {
      if (property instanceof IntegerProperty integer && AGE.equals(property.getName())) {
        return Optional.of(integer);
      }
    }
    return Optional.empty();
  }

  private static int maxOf(IntegerProperty property) {
    return property.getPossibleValues().stream().mapToInt(Integer::intValue).max().orElse(0);
  }
}
