package io.github.zenndg.cubit.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

/**
 * One errand of the Cook: where it goes, what it does there and, for raw food or fuel, the item it
 * wants, or none for any (answers 1317 to 1320).
 */
record CookTask(Kind kind, BlockPos pos, ItemStack like) {
  enum Kind {
    /** Takes raw food from a chest of the Unload app. */
    FETCH_RAW,
    /** Takes fuel of the tag from a chest of the Unload app. */
    FETCH_FUEL,
    /** Puts raw food into the oven, through its top face. */
    FEED,
    /** Puts fuel into the oven, through its side face. */
    FUEL,
    /** Takes the cooked food out of the oven, through its bottom face. */
    TAKE
  }

  CookTask {
    like = like.copyWithCount(1);
  }
}
