package io.github.zenndg.cubit.entity;

import net.minecraft.core.BlockPos;

/**
 * One errand of the Organizer: where it goes and what it does there. The target is the index of a
 * mark, or the id of an item on the ground, and is unused by the other kinds.
 */
record OrganizerTask(Kind kind, BlockPos pos, int target) {
  enum Kind {
    /** Puts what it carries into a chest, by the rule of the chest with the most. */
    STORE,
    /** Takes the output of a marked machine through its bottom face. */
    TAKE_OUTPUT,
    /** Picks up an item on the ground of the area. */
    PICK_UP,
    /** Takes the item of an order from a chest of the Unload app. */
    FETCH,
    /** Puts the item of an order into its destination, through the marked face. */
    DELIVER
  }

  static final int NO_TARGET = -1;
}
