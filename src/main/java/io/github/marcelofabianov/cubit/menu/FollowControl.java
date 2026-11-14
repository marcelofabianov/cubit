package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.robot.FollowCall;
import net.minecraft.world.entity.player.Player;

/** `Follow` of the Tablet. The server decides whether the Cubit comes (answers 1073 to 1075). */
public interface FollowControl {
  /** On the client, where the real state comes from the server. */
  FollowControl NONE =
      new FollowControl() {
        @Override
        public FollowCall goal() {
          return FollowCall.FOLLOWING;
        }

        @Override
        public boolean call(Player player) {
          return false;
        }
      };

  /** Whether the Cubit can follow now, or why not. */
  FollowCall goal();

  /** False when the player is not the owner or the Cubit cannot follow. */
  boolean call(Player player);
}
