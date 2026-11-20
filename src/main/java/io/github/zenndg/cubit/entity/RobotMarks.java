package io.github.zenndg.cubit.entity;

import com.mojang.logging.LogUtils;
import io.github.zenndg.cubit.domain.ownership.Ownership;
import io.github.zenndg.cubit.domain.robot.OrganizerMark;
import io.github.zenndg.cubit.domain.robot.OrganizerMarks;
import io.github.zenndg.cubit.domain.robot.SupplyAmount;
import io.github.zenndg.cubit.domain.robot.UnloadChests;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

/**
 * The blocks marked with the Mark app, in order: the machines and the destinations of the
 * Organizer, each with its order, which only the owner changes (answers 1294 to 1297 and rule 20).
 * They live only in the save of the entity, under MarkedBlocks, so collecting the Cubit erases them
 * (answers 1301 and 1327).
 */
public final class RobotMarks {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String KEY = "MarkedBlocks";
  private static final String BLOCK_KEY = "Block";
  private static final String DIMENSION_KEY = "Dimension";
  private static final String FACE_KEY = "Face";
  private static final String ITEM_KEY = "Item";
  private static final String COUNT_KEY = "Count";
  private static final String INVALID_MARK = "Robot {} has an invalid mark: {}";

  private final RobotEntity robot;
  private List<OrganizerTarget> targets = List.of();

  RobotMarks(RobotEntity robot) {
    this.robot = robot;
  }

  public List<OrganizerTarget> targets() {
    return targets;
  }

  /** The limit of the tier, from the config (answer 1300). */
  public int limit() {
    return robot.spec().jobs().organizer().marks();
  }

  public boolean marks(BlockPos pos) {
    return domain().contains(pos.asLong(), RobotUnload.dimensionOf(robot.level()));
  }

  /** A click of the owner on a block with an inventory marks it with the face, or unmarks it. */
  public Optional<UnloadChests.Mark> click(Player player, BlockPos pos, Direction face) {
    if (!Ownership.owns(robot.ownerId(), player.getUUID())
        || OrganizerStock.at(robot.level(), pos, face).isEmpty()) {
      return Optional.empty();
    }
    OrganizerMark mark =
        new OrganizerMark(
            pos.asLong(), RobotUnload.dimensionOf(robot.level()), face.get3DDataValue());
    OrganizerMarks.Clicked clicked = domain().clicked(mark, limit());
    keep(clicked.marks());
    return Optional.of(clicked.mark());
  }

  /** Shift and a click: the clicked block, or the last one marked (as the Unload app). */
  public boolean unmark(Player player, Optional<BlockPos> clicked) {
    if (!Ownership.owns(robot.ownerId(), player.getUUID()) || targets.isEmpty()) {
      return false;
    }
    String dimension = RobotUnload.dimensionOf(robot.level());
    OrganizerMarks next =
        clicked
            .filter(pos -> domain().contains(pos.asLong(), dimension))
            .map(pos -> domain().clicked(new OrganizerMark(pos.asLong(), dimension, 0), 0).marks())
            .orElseGet(() -> domain().withoutLast());
    keep(next);
    return true;
  }

  /** The item of an order, from what the owner holds on the cursor; nothing clears it. */
  public boolean setWanted(Player player, int index, ItemStack item) {
    return change(player, index, target -> target.withWanted(item));
  }

  /** − and + of an order, by 8 with Shift (answer 1300). */
  public boolean stepAmount(Player player, int index, int direction, boolean big) {
    return change(player, index, target -> target.withAmount(target.amount().step(direction, big)));
  }

  private boolean change(Player player, int index, UnaryOperator<OrganizerTarget> how) {
    if (!Ownership.owns(robot.ownerId(), player.getUUID())
        || index < 0
        || index >= targets.size()) {
      return false;
    }
    List<OrganizerTarget> next = new ArrayList<>(targets);
    next.set(index, how.apply(next.get(index)));
    targets = List.copyOf(next);
    return true;
  }

  /** Keeps the orders of the marks that stayed, and a new mark gets a new order. */
  private void keep(OrganizerMarks marks) {
    List<OrganizerTarget> next = new ArrayList<>();
    for (OrganizerMark mark : marks.marks()) {
      next.add(
          targets.stream()
              .filter(old -> old.mark().samePlace(mark.block(), mark.dimension()))
              .findFirst()
              .orElseGet(() -> OrganizerTarget.of(mark)));
    }
    targets = List.copyOf(next);
  }

  private OrganizerMarks domain() {
    return new OrganizerMarks(targets.stream().map(OrganizerTarget::mark).toList());
  }

  void save(CompoundTag tag) {
    if (targets.isEmpty()) {
      return;
    }
    ListTag saved = new ListTag();
    for (OrganizerTarget target : targets) {
      CompoundTag entry = new CompoundTag();
      entry.putLong(BLOCK_KEY, target.mark().block());
      entry.putString(DIMENSION_KEY, target.mark().dimension());
      entry.putInt(FACE_KEY, target.mark().face());
      entry.putInt(COUNT_KEY, target.amount().count());
      if (target.hasOrder()) {
        entry.put(ITEM_KEY, target.wanted().save(robot.registryAccess()));
      }
      saved.add(entry);
    }
    tag.put(KEY, saved);
  }

  /** An invalid mark is left out and goes to the log, and a lower limit keeps the first (17). */
  void load(CompoundTag tag) {
    List<OrganizerTarget> loaded = new ArrayList<>();
    for (Tag entry : tag.getList(KEY, Tag.TAG_COMPOUND)) {
      read((CompoundTag) entry).ifPresent(loaded::add);
    }
    targets = List.copyOf(loaded.subList(0, Math.min(loaded.size(), Math.max(limit(), 0))));
  }

  private Optional<OrganizerTarget> read(CompoundTag entry) {
    try {
      OrganizerMark mark =
          new OrganizerMark(
              entry.getLong(BLOCK_KEY), entry.getString(DIMENSION_KEY), entry.getInt(FACE_KEY));
      ItemStack item =
          entry.contains(ITEM_KEY)
              ? ItemStack.parseOptional(robot.registryAccess(), entry.getCompound(ITEM_KEY))
              : ItemStack.EMPTY;
      return Optional.of(
          new OrganizerTarget(mark, item, new SupplyAmount(entry.getInt(COUNT_KEY))));
    } catch (IllegalArgumentException invalid) {
      LOGGER.warn(INVALID_MARK, robot.getUUID(), entry);
      return Optional.empty();
    }
  }
}
