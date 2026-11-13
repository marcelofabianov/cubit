package io.github.marcelofabianov.cubit.menu;

import net.minecraft.world.entity.player.Player;

/** The Power button of the settings tab. The server decides whether the robot switches off. */
public interface PowerControl {
  /** On the client, where the real state comes from the server. */
  PowerControl NONE =
      new PowerControl() {
        @Override
        public boolean available() {
          return false;
        }

        @Override
        public boolean switchOff(Player player) {
          return false;
        }

        @Override
        public boolean turnOn(Player player) {
          return false;
        }
      };

  /** Only an active robot switches off. In the base it hibernates (answer 857). */
  boolean available();

  /** False when the player is not the owner or the robot is not active. */
  boolean switchOff(Player player);

  /**
   * The Power of the Tablet also turns on a switched-off robot with charge, as the click on it does
   * (answer 1033). False when the player is not the owner or the robot cannot turn on.
   */
  boolean turnOn(Player player);
}
