package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.domain.robot.OrganizerMark;
import io.github.zenndg.cubit.domain.robot.SupplyAmount;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

/**
 * A block marked with the Mark app and, for a destination, its order: the item, with its data, and
 * the amount, which starts at a stack, with no item until the player chooses (answers 1294 and
 * 1304).
 */
public record OrganizerTarget(OrganizerMark mark, ItemStack wanted, SupplyAmount amount) {
  public OrganizerTarget {
    wanted = wanted.copyWithCount(1);
  }

  /** A new mark, with no item and a stack (answer 1304). */
  static OrganizerTarget of(OrganizerMark mark) {
    return new OrganizerTarget(mark, ItemStack.EMPTY, SupplyAmount.MAX);
  }

  BlockPos pos() {
    return BlockPos.of(mark.block());
  }

  /** The face clicked when marking, the one it supplies through (answer 1296). */
  Direction face() {
    return Direction.from3DDataValue(mark.face());
  }

  boolean hasOrder() {
    return !wanted.isEmpty();
  }

  OrganizerTarget withWanted(ItemStack item) {
    return new OrganizerTarget(mark, item, amount);
  }

  OrganizerTarget withAmount(SupplyAmount next) {
    return new OrganizerTarget(mark, wanted, next);
  }
}
