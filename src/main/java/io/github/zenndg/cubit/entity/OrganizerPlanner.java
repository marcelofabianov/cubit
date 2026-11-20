package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.domain.robot.OrganizerMode;
import io.github.zenndg.cubit.domain.robot.WorkArea;
import io.github.zenndg.cubit.menu.WorkKeys;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * Chooses what the Organizer does next and moves the items of the chests, the orders and the
 * destinations. It only looks after an action or once per scan interval (rule 21).
 */
final class OrganizerPlanner {
  private static final String ITEMS = "#items";
  private static final double EDGE = 1;

  private final RobotEntity robot;
  private final RobotOrganizing organizing;
  private long nextLook = Long.MIN_VALUE;
  private boolean warnedNoRoom;

  OrganizerPlanner(RobotEntity robot, RobotOrganizing organizing) {
    this.robot = robot;
    this.organizing = organizing;
  }

  /** After an action, the next look is right away. */
  void lookNow() {
    nextLook = Long.MIN_VALUE;
  }

  /** The orders first in supply, then storing what it carries, then gathering in organize. */
  Optional<OrganizerTask> next(WorkArea area) {
    long now = robot.level().getGameTime();
    if (now < nextLook) {
      return Optional.empty();
    }
    List<OrganizerChests.Chest> chests = OrganizerChests.inside(robot, area);
    Optional<OrganizerTask> found =
        organizing.mode() == OrganizerMode.SUPPLY
            ? OrganizerOrders.next(robot, area, chests)
            : Optional.empty();
    if (found.isEmpty()) {
      found = nextStore(chests);
    }
    if (found.isEmpty() && organizing.mode() == OrganizerMode.ORGANIZE) {
      found = nextGather(area);
    }
    if (found.isEmpty()) {
      nextLook = now + organizing.spec().scanIntervalTicks();
    }
    return found;
  }

  /** The chest for the first stack it carries that has a place, or a warning once (1292). */
  private Optional<OrganizerTask> nextStore(List<OrganizerChests.Chest> chests) {
    boolean homeless = false;
    for (int slot = 0; slot < robot.inventory().getContainerSize(); slot++) {
      ItemStack stack = robot.inventory().getItem(slot);
      if (stack.isEmpty() || OrganizerOrders.wanted(robot, stack)) {
        continue;
      }
      Optional<BlockPos> chest = OrganizerChests.chestFor(chests, stack);
      if (chest.isPresent()) {
        return Optional.of(new OrganizerTask(OrganizerTask.Kind.STORE, chest.get(), slot));
      }
      homeless = true;
    }
    warnOnce(homeless);
    return Optional.empty();
  }

  /** The nearest free marked machine that gives something out, or item on the ground. */
  private Optional<OrganizerTask> nextGather(WorkArea area) {
    if (robot.robotInventory().isFull()) {
      return Optional.empty();
    }
    String dimension = RobotUnload.dimensionOf(robot.level());
    Optional<OrganizerTask> machine =
        robot.workArea().work().marks().targets().stream()
            .map(OrganizerTarget::pos)
            .filter(pos -> area.contains(pos.getX(), pos.getY(), pos.getZ()))
            .filter(pos -> RobotOrganizing.SPOTS.freeFor(dimension, pos.asLong(), robot.getUUID()))
            .filter(pos ->
                OrganizerStock.at(robot.level(), pos, Direction.DOWN)
                    .filter(OrganizerStock::givesOut)
                    .isPresent())
            .min(Comparator.comparingDouble(pos -> robot.blockPosition().distSqr(pos)))
            .filter(pos -> RobotOrganizing.SPOTS.reserve(dimension, pos.asLong(), robot.getUUID()))
            .map(pos ->
                new OrganizerTask(OrganizerTask.Kind.TAKE_OUTPUT, pos, OrganizerTask.NO_TARGET));
    return machine.or(() -> nextItem(area, dimension + ITEMS));
  }

  private Optional<OrganizerTask> nextItem(WorkArea area, String key) {
    AABB box =
        new AABB(
            area.min().x(),
            area.min().y(),
            area.min().z(),
            area.max().x() + EDGE,
            area.max().y() + EDGE,
            area.max().z() + EDGE);
    return robot
        .level()
        .getEntitiesOfClass(ItemEntity.class, box, item -> !item.hasPickUpDelay())
        .stream()
        .filter(item -> RobotOrganizing.SPOTS.freeFor(key, item.getId(), robot.getUUID()))
        .min(Comparator.comparingDouble(robot::distanceToSqr))
        .filter(item -> RobotOrganizing.SPOTS.reserve(key, item.getId(), robot.getUUID()))
        .map(item ->
            new OrganizerTask(OrganizerTask.Kind.PICK_UP, item.blockPosition(), item.getId()));
  }

  /** Puts one stack it carries into the chest, if the chest is still the right one. */
  int store(BlockPos chest, IItemHandler carried) {
    Optional<IItemHandler> handler = robot.docking().unload().handlerAt(chest);
    if (handler.isEmpty()) {
      return 0;
    }
    WorkArea area = robot.workArea().here().orElse(null);
    List<OrganizerChests.Chest> chests =
        area == null ? List.of() : OrganizerChests.inside(robot, area);
    for (int slot = 0; slot < carried.getSlots(); slot++) {
      ItemStack stack = carried.getStackInSlot(slot);
      if (!stack.isEmpty()
          && !OrganizerOrders.wanted(robot, stack)
          && OrganizerChests.chestFor(chests, stack).filter(chest::equals).isPresent()) {
        return OrganizerStock.move(
            carried,
            handler.get(),
            seen -> ItemStack.isSameItemSameComponents(seen, stack),
            stack.getMaxStackSize());
      }
    }
    return 0;
  }

  int fetch(OrganizerTask task, IItemHandler carried) {
    return OrganizerOrders.fetch(robot, task, carried);
  }

  int deliver(OrganizerTask task, IItemHandler carried) {
    return OrganizerOrders.deliver(robot, task, carried);
  }

  /** Once, while some item has no place, and again only after every item had one (1292). */
  private void warnOnce(boolean homeless) {
    if (!homeless) {
      warnedNoRoom = false;
      return;
    }
    if (!warnedNoRoom && RobotInteractions.onlineOwner(robot) instanceof ServerPlayer owner) {
      Component text = Component.translatable(WorkKeys.ORGANIZER_NO_ROOM);
      io.github.zenndg.cubit.network.CubitNetwork.sendNotice(
          owner, Component.translatable(RobotNotices.FORMAT_KEY, text));
    }
    warnedNoRoom = true;
  }
}
