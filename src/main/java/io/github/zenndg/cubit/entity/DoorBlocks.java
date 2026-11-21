package io.github.zenndg.cubit.entity;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * The doors, trapdoors and fence gates a Cubit opens, by the tags of the game, so the ones of other
 * mods count too (answer 1365). The Cubit moves only the door, without a redstone signal that would
 * reach other blocks (answer 1371).
 */
public final class DoorBlocks {
  private static final int SPARKS = 6;
  private static final double SPREAD = 0.3;
  private static final double SPARK_SPEED = 0.05;
  private static final double HALF = 0.5;
  private static final float VOLUME = 1.0f;
  private static final float PITCH = 1.0f;

  private DoorBlocks() {}

  public static boolean isDoor(BlockState state) {
    return (state.is(BlockTags.DOORS)
            || state.is(BlockTags.TRAPDOORS)
            || state.is(BlockTags.FENCE_GATES))
        && state.hasProperty(BlockStateProperties.OPEN);
  }

  /** The block that keeps the mark: the lower half of a door, or the block itself. */
  public static Optional<BlockPos> markedBlock(Level level, BlockPos pos) {
    BlockState state = level.getBlockState(pos);
    if (!isDoor(state)) {
      return Optional.empty();
    }
    boolean upper =
        state.hasProperty(DoorBlock.HALF)
            && state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER;
    return Optional.of(upper ? pos.below() : pos);
  }

  static boolean isOpen(BlockState state) {
    return state.getValue(BlockStateProperties.OPEN);
  }

  /** Opens or shuts the door with the sound of the game, and sparks when it opens. */
  static void setOpen(ServerLevel level, BlockPos pos, boolean open, Entity robot) {
    BlockState state = level.getBlockState(pos);
    if (!isDoor(state) || isOpen(state) == open) {
      return;
    }
    if (state.getBlock() instanceof DoorBlock door) {
      door.setOpen(robot, level, state, pos, open);
    } else {
      level.setBlock(pos, state.setValue(BlockStateProperties.OPEN, open), Block.UPDATE_CLIENTS);
      level.playSound(null, pos, soundOf(state, open), SoundSource.BLOCKS, VOLUME, PITCH);
      level.gameEvent(robot, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
    }
    if (open) {
      level.sendParticles(
          ParticleTypes.ELECTRIC_SPARK,
          pos.getX() + HALF,
          pos.getY() + HALF,
          pos.getZ() + HALF,
          SPARKS,
          SPREAD,
          SPREAD,
          SPREAD,
          SPARK_SPEED);
    }
  }

  private static SoundEvent soundOf(BlockState state, boolean open) {
    if (state.is(BlockTags.FENCE_GATES)) {
      return open ? SoundEvents.FENCE_GATE_OPEN : SoundEvents.FENCE_GATE_CLOSE;
    }
    if (state.is(Blocks.IRON_TRAPDOOR)) {
      return open ? SoundEvents.IRON_TRAPDOOR_OPEN : SoundEvents.IRON_TRAPDOOR_CLOSE;
    }
    return open ? SoundEvents.WOODEN_TRAPDOOR_OPEN : SoundEvents.WOODEN_TRAPDOOR_CLOSE;
  }
}
