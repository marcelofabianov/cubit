package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.dock.DockGoal;
import net.minecraft.world.entity.player.Player;

/** `Go to Dock` of the Tablet. The server decides whether the Cubit goes (answer 1041). */
public interface DockGoalControl {
  /** On the client, where the real state comes from the server. */
  DockGoalControl NONE =
      new DockGoalControl() {
        @Override
        public DockGoal goal() {
          return DockGoal.NO_DOCK;
        }

        @Override
        public boolean go(Player player) {
          return false;
        }
      };

  /** Whether the Cubit can go now, or why not. */
  DockGoal goal();

  /** False when the player is not the owner or the Cubit cannot go. */
  boolean go(Player player);
}
