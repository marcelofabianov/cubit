package io.github.zenndg.cubit.block;

import io.github.zenndg.cubit.domain.tier.RobotTier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/** Both halves of a dock show its tier, which picks the band and the body (answer 1379). */
final class DockTierStates {
  private DockTierStates() {}

  static void show(Level level, BlockPos lower, RobotTier tier) {
    DockTierProperty property = DockTierProperty.of(tier);
    for (BlockPos pos : new BlockPos[] {lower, lower.above()}) {
      BlockState state = level.getBlockState(pos);
      if (state.getBlock() instanceof InductionDockBlock
          && state.getValue(InductionDockBlock.TIER) != property) {
        level.setBlock(pos, state.setValue(InductionDockBlock.TIER, property), Block.UPDATE_ALL);
      }
    }
  }
}
