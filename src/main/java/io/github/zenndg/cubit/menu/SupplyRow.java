package io.github.zenndg.cubit.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

/**
 * One order of the Supply tab: the destination, the face it is filled through, the item, how many.
 */
public record SupplyRow(BlockPos pos, int face, ItemStack wanted, int amount) {}
