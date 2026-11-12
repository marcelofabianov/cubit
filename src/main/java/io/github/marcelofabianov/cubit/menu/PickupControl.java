package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.robot.PickupSettings;
import io.github.marcelofabianov.cubit.domain.tier.PickupSpec;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** The pickup of the robot as its screen sees it. The server decides every change. */
public interface PickupControl {
  /** On the client, where the real values come from the server. */
  PickupControl NONE =
      new PickupControl() {
        @Override
        public PickupSettings settings() {
          return PickupSettings.INITIAL;
        }

        @Override
        public PickupSpec spec() {
          return PickupSpec.NONE;
        }

        @Override
        public Optional<RobotTier> magnetUnlockTier() {
          return Optional.empty();
        }

        @Override
        public boolean press(Player player, PickupButton button) {
          return false;
        }

        @Override
        public boolean place(Player player, int slot, ItemStack carried) {
          return false;
        }

        @Override
        public boolean cycle(Player player, int slot) {
          return false;
        }
      };

  PickupSettings settings();

  PickupSpec spec();

  /** The first tier with a magnet, read from the config, for the locked switch (answer 881). */
  Optional<RobotTier> magnetUnlockTier();

  /** False when the player is not the owner or the button cannot change anything now. */
  boolean press(Player player, PickupButton button);

  /** A stack copies its item to the slot, and an empty cursor clears it (answer 880). */
  boolean place(Player player, int slot, ItemStack carried);

  /** From the item through its tags and back, only on a tier with tags (answer 882). */
  boolean cycle(Player player, int slot);

  /** The buttons of the behavior tab. */
  enum PickupButton {
    PICKUP,
    MAGNET,
    MODE
  }
}
