package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.domain.robot.WorkArea;
import java.util.Optional;
import net.minecraft.core.BlockPos;

/**
 * A job done by errands, one at a time: it plans the next one, the Cubit walks to its block and
 * does it, then rests, as the Organizer does (answer 1290).
 */
interface JobErrands {
  /** The card of the job, active in its area. */
  boolean active();

  boolean resting();

  /** Takes the next errand, when there is none and it is time to look. */
  void plan(WorkArea area);

  /** Where the current errand is, if any. */
  Optional<BlockPos> target();

  /** Does the errand at its block. */
  void act();

  /** A place it cannot reach is left until the next look. */
  void giveUp();
}
