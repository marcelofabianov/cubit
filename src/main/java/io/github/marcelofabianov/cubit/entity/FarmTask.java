package io.github.marcelofabianov.cubit.entity;

import net.minecraft.core.BlockPos;

/** One thing the Farmer found to do, at a block of its area. */
record FarmTask(BlockPos pos, FarmAction action) {}
