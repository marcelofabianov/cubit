package io.github.zenndg.cubit.menu;

import io.github.zenndg.cubit.domain.robot.OrganizerMode;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * The Organizer as its screens show it: the line Mode of the Work tab and the orders of the Supply
 * tab (answers 1290, 1295 and 1303). Every change is checked against the owner (rule 20).
 */
public interface OrganizerControl {
  /** On the client, where the real values come from the server. */
  OrganizerControl NONE =
      new OrganizerControl() {
        @Override
        public Optional<OrganizerMode> organizerMode() {
          return Optional.empty();
        }

        @Override
        public boolean switchMode(Player player) {
          return false;
        }

        @Override
        public List<SupplyRow> rows() {
          return List.of();
        }

        @Override
        public int limit() {
          return 0;
        }

        @Override
        public boolean place(Player player, int index, ItemStack carried) {
          return false;
        }

        @Override
        public boolean step(Player player, int index, int direction, boolean big) {
          return false;
        }
      };

  /** The mode, only with the card of the Organizer. */
  Optional<OrganizerMode> organizerMode();

  boolean switchMode(Player player);

  /** The orders, one per marked destination, in the order of the marks. */
  List<SupplyRow> rows();

  /** How many marks the tier keeps. */
  int limit();

  /** The item of an order, from the cursor, or none with an empty hand. */
  boolean place(Player player, int index, ItemStack carried);

  /** − and + of an order, by 8 with Shift (answer 1300). */
  boolean step(Player player, int index, int direction, boolean big);
}
