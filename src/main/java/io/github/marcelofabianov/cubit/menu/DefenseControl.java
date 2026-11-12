package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import net.minecraft.world.entity.player.Player;

/** The defense mode of the robot as its screen sees it. The server decides every change. */
public interface DefenseControl {
  /** On the client, where the real mode comes from the server. */
  DefenseControl NONE =
      new DefenseControl() {
        @Override
        public DefenseMode mode() {
          return DefenseMode.INITIAL;
        }

        @Override
        public boolean choose(Player player, DefenseMode mode) {
          return false;
        }
      };

  DefenseMode mode();

  /** False when the player is not the owner or the tier does not allow the mode. */
  boolean choose(Player player, DefenseMode mode);
}
