package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.behavior.HibernationReason;
import io.github.marcelofabianov.cubit.domain.robot.RobotJob;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import java.util.Optional;
import java.util.Set;
import net.minecraft.world.entity.player.Player;

/**
 * The job of the Cubit as the Work tab shows it: the card in the slot, why it hibernates away from
 * the Dock, the first tier that takes a card, and Resume (answers 1162, 1164 and 1167).
 */
public interface WorkControl {
  /** On the client, where the real values come from the server. */
  WorkControl NONE =
      new WorkControl() {
        @Override
        public Optional<RobotJob> job() {
          return Optional.empty();
        }

        @Override
        public Optional<HibernationReason> hibernation() {
          return Optional.empty();
        }

        @Override
        public Optional<RobotTier> cardUnlockTier() {
          return Optional.empty();
        }

        @Override
        public Set<RobotJob> allowedJobs() {
          return Set.of();
        }

        @Override
        public Optional<RobotTier> tierFor(RobotJob job) {
          return Optional.empty();
        }

        @Override
        public boolean resume(Player player) {
          return false;
        }

        @Override
        public Optional<RobotTier> fertilizerUnlockTier() {
          return Optional.empty();
        }

        @Override
        public boolean fertilizerUnlocked() {
          return false;
        }

        @Override
        public int unloadIntervalMinutes() {
          return 0;
        }

        @Override
        public boolean stepUnloadInterval(Player player, int direction) {
          return false;
        }
      };

  Optional<RobotJob> job();

  Optional<HibernationReason> hibernation();

  /** The first tier whose allowed_jobs is not empty, read from the config (answer 1162). */
  Optional<RobotTier> cardUnlockTier();

  /** The jobs the tier of this Cubit accepts now, for the card slot of the screen. */
  Set<RobotJob> allowedJobs();

  /** The first tier that accepts the job, for the hint of a card that does not fit (1162). */
  Optional<RobotTier> tierFor(RobotJob job);

  /** Wakes a Cubit hibernating away from the Dock, and says whether it did (answer 253). */
  boolean resume(Player player);

  /** The first tier whose Farmer takes fertilizer, read from the config (answer 1244). */
  Optional<RobotTier> fertilizerUnlockTier();

  /** Whether the tier of this Cubit takes fertilizer in its slot. */
  boolean fertilizerUnlocked();

  /** The unload interval that holds now, in minutes, with zero for only when full (1252). */
  int unloadIntervalMinutes();

  /** One step of the buttons − and + of the interval, only for the owner (rule 20). */
  boolean stepUnloadInterval(Player player, int direction);
}
