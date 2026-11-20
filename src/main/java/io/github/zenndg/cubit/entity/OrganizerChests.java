package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.domain.behavior.ChestStock;
import io.github.zenndg.cubit.domain.behavior.StorageChoice;
import io.github.zenndg.cubit.domain.robot.UnloadChest;
import io.github.zenndg.cubit.domain.robot.WorkArea;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * The chests of the Unload app that the Organizer uses: only the loaded ones inside its area, in
 * the order of the marks (answers 1287 and 1291).
 */
final class OrganizerChests {
  private OrganizerChests() {}

  /** The chests it may use now, with their handlers. */
  static List<Chest> inside(RobotEntity robot, WorkArea area) {
    String dimension = RobotUnload.dimensionOf(robot.level());
    List<Chest> found = new ArrayList<>();
    for (UnloadChest chest : robot.docking().unload().chests().chests()) {
      BlockPos pos = BlockPos.of(chest.block());
      if (chest.isIn(dimension) && area.contains(pos.getX(), pos.getY(), pos.getZ())) {
        robot.docking().unload().handlerAt(pos).ifPresent(h -> found.add(new Chest(pos, h)));
      }
    }
    return found;
  }

  /** Where this stack goes, by the rule of the chest with the most, or nowhere (1292). */
  static Optional<BlockPos> chestFor(List<Chest> chests, ItemStack stack) {
    List<ChestStock> stocks =
        chests.stream()
            .map(chest ->
                new ChestStock(
                    OrganizerStock.count(chest.handler(), stack),
                    OrganizerStock.fits(chest.handler(), stack)))
            .toList();
    OptionalInt index = StorageChoice.chestFor(stocks);
    return index.isPresent() ? Optional.of(chests.get(index.getAsInt()).pos()) : Optional.empty();
  }

  /** The first chest that holds the item of an order, with its data (answer 1294). */
  static Optional<BlockPos> chestWith(List<Chest> chests, ItemStack wanted) {
    return chests.stream()
        .filter(chest -> OrganizerStock.count(chest.handler(), wanted) > 0)
        .map(Chest::pos)
        .findFirst();
  }

  record Chest(BlockPos pos, IItemHandler handler) {}
}
