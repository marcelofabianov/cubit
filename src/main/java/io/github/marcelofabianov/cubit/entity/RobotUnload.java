package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.RobotNotice;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.ownership.Ownership;
import io.github.marcelofabianov.cubit.domain.robot.UnloadCall;
import io.github.marcelofabianov.cubit.domain.robot.UnloadChest;
import io.github.marcelofabianov.cubit.domain.robot.UnloadChests;
import io.github.marcelofabianov.cubit.menu.RobotSwitch;
import io.github.marcelofabianov.cubit.menu.UnloadControl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

/**
 * The chests of the Cubit, marked with the Unload app, its switch and `Unload` of the Tablet
 * (answers 1086 to 1095). Only a Cubit in an area unloads by itself, and only loaded chests in
 * reach count (rule 22). The trip itself is the {@link UnloadGoal}.
 */
public final class RobotUnload implements RobotSwitch, UnloadControl {
  private static final String KEY = "UnloadChests";
  private static final String BLOCK_KEY = "Block";
  private static final String DIMENSION_KEY = "Dimension";

  private final RobotEntity robot;
  private UnloadChests chests = UnloadChests.NONE;
  private boolean switchedOn = true;
  private boolean onTrip;
  private boolean byButton;
  private Optional<BlockPos> only = Optional.empty();
  private UnloadCall call = UnloadCall.NO_CHEST;
  private long nextCheck = Long.MIN_VALUE;

  RobotUnload(RobotEntity robot) {
    this.robot = robot;
  }

  public UnloadChests chests() {
    return chests;
  }

  /** Whether this block is one of the chests of the Cubit. */
  public boolean marks(BlockPos pos) {
    return chests.contains(new UnloadChest(pos.asLong(), dimensionOf(robot.level())));
  }

  /**
   * A click on a marked chest sends the Cubit to unload only there, as the button (answer 1098).
   * Gives the reason when it cannot go.
   */
  public UnloadCall unloadInto(Player player, BlockPos pos) {
    only = Optional.of(pos);
    nextCheck = Long.MIN_VALUE;
    UnloadCall now = goal();
    if (!Ownership.owns(robot.ownerId(), player.getUUID()) || !now.ready()) {
      only = Optional.empty();
      nextCheck = Long.MIN_VALUE;
      return now;
    }
    onTrip = true;
    byButton = true;
    return now;
  }

  /**
   * Shift and a click take out the clicked chest when it is marked, or else the last one marked,
   * from anywhere, so a far chest can give its place to a new one (answer 1099). False when there
   * was nothing to take out.
   */
  public boolean unmark(Player player, Optional<BlockPos> clicked) {
    if (!Ownership.owns(robot.ownerId(), player.getUUID()) || chests.isEmpty()) {
      return false;
    }
    Optional<UnloadChest> marked =
        clicked
            .map(pos -> new UnloadChest(pos.asLong(), dimensionOf(robot.level())))
            .filter(chests::contains);
    chests =
        marked.isPresent()
            ? chests.clicked(marked.get(), robot.spec().unload().chests()).chests()
            : chests.withoutLast();
    nextCheck = Long.MIN_VALUE;
    return true;
  }

  /** A click of the owner marks a block with an inventory, or removes a marked one (1086). */
  public Optional<UnloadChests.Mark> click(Player player, BlockPos pos) {
    if (!Ownership.owns(robot.ownerId(), player.getUUID()) || handlerAt(pos).isEmpty()) {
      return Optional.empty();
    }
    UnloadChest chest = new UnloadChest(pos.asLong(), dimensionOf(robot.level()));
    UnloadChests.Clicked clicked = chests.clicked(chest, robot.spec().unload().chests());
    chests = clicked.chests();
    nextCheck = Long.MIN_VALUE;
    return Optional.of(clicked.mark());
  }

  boolean switchedOn() {
    return switchedOn;
  }

  void setSwitchedOn(boolean switchedOn) {
    this.switchedOn = switchedOn;
  }

  @Override
  public boolean isOn() {
    return switchedOn;
  }

  @Override
  public boolean isAvailable() {
    return robot.powerState() != PowerState.HIBERNATING;
  }

  @Override
  public void toggle() {
    switchedOn = !switchedOn;
  }

  /** For the screen of the Tablet, read again once per interval of the dock search. */
  @Override
  public UnloadCall goal() {
    long now = robot.level().getGameTime();
    if (now >= nextCheck) {
      nextCheck = now + CubitMod.CONFIG.followRepathTicks();
      call = UnloadCall.of(robot.powerState(), isEmpty(), nextChest().isPresent());
    }
    return call;
  }

  /** Sends the Cubit now, checked again, only for the owner (answer 1089). */
  @Override
  public boolean unload(Player player) {
    nextCheck = Long.MIN_VALUE;
    if (!Ownership.owns(robot.ownerId(), player.getUUID()) || !goal().ready()) {
      return false;
    }
    onTrip = true;
    byButton = true;
    return true;
  }

  /** Whether the Cubit should go now: sent by the button, or full in its area (1087). */
  boolean wantsToGo() {
    if (onTrip) {
      return true;
    }
    boolean full = robot.robotInventory().isFull();
    if (UnloadCall.unloadsByItself(switchedOn, robot.workArea().working(), full, false)
        && nextChest().isPresent()) {
      onTrip = true;
      byButton = false;
    }
    return onTrip;
  }

  boolean onTrip() {
    return onTrip;
  }

  /** The trip ended. Only the Cubit that follows says so, which only the button sends (457). */
  void finish(boolean moved) {
    if (byButton) {
      robot.notices().show(moved ? RobotNotice.ITEMS_UNLOADED : RobotNotice.NO_ROOM);
    }
    onTrip = false;
    byButton = false;
    only = Optional.empty();
    nextCheck = Long.MIN_VALUE;
  }

  /** The first marked chest with room for something, loaded and in reach (answer 1088). */
  Optional<BlockPos> nextChest() {
    String dimension = dimensionOf(robot.level());
    double reach = robot.spec().unload().reachBlocks();
    for (UnloadChest chest : chests.chests()) {
      BlockPos pos = BlockPos.of(chest.block());
      boolean allowed = only.isEmpty() || only.get().equals(pos);
      if (allowed
          && chest.isIn(dimension)
          && pos.distToCenterSqr(robot.position()) <= reach * reach
          && handlerAt(pos).filter(this::hasRoom).isPresent()) {
        return Optional.of(pos);
      }
    }
    return Optional.empty();
  }

  /** Moves what fits into the chest, never the exclusive slots, which are not in this container. */
  boolean transferTo(BlockPos pos) {
    Optional<IItemHandler> handler = handlerAt(pos);
    if (handler.isEmpty()) {
      return false;
    }
    Container inventory = robot.inventory();
    boolean moved = false;
    for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
      ItemStack stack = inventory.getItem(slot);
      if (stack.isEmpty()) {
        continue;
      }
      ItemStack left = ItemHandlerHelper.insertItemStacked(handler.get(), stack.copy(), false);
      if (left.getCount() != stack.getCount()) {
        inventory.setItem(slot, left);
        moved = true;
      }
    }
    return moved;
  }

  /** The used and the total slots of a block with an inventory, for the arrow over it (1092). */
  public record Slots(int used, int total) {}

  public Optional<Slots> slotsAt(BlockPos pos) {
    return handlerAt(pos)
        .map(handler -> {
          int used = 0;
          for (int slot = 0; slot < handler.getSlots(); slot++) {
            used += handler.getStackInSlot(slot).isEmpty() ? 0 : 1;
          }
          return new Slots(used, handler.getSlots());
        });
  }

  boolean isEmpty() {
    return robot.inventory().isEmpty();
  }

  private boolean hasRoom(IItemHandler handler) {
    Container inventory = robot.inventory();
    for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
      ItemStack stack = inventory.getItem(slot);
      if (!stack.isEmpty()
          && ItemHandlerHelper.insertItemStacked(handler, stack.copy(), true).getCount()
              < stack.getCount()) {
        return true;
      }
    }
    return false;
  }

  /** Checks the chunk first: reading a block must never load one (AGENTS.md, rule 22). */
  Optional<IItemHandler> handlerAt(BlockPos pos) {
    Level level = robot.level();
    if (!level.hasChunkAt(pos)) {
      return Optional.empty();
    }
    return Optional.ofNullable(level.getCapability(Capabilities.ItemHandler.BLOCK, pos, null));
  }

  static String dimensionOf(Level level) {
    return level.dimension().location().toString();
  }

  void save(CompoundTag tag) {
    if (chests.isEmpty()) {
      return;
    }
    ListTag saved = new ListTag();
    for (UnloadChest chest : chests.chests()) {
      CompoundTag entry = new CompoundTag();
      entry.putLong(BLOCK_KEY, chest.block());
      entry.putString(DIMENSION_KEY, chest.dimension());
      saved.add(entry);
    }
    tag.put(KEY, saved);
  }

  /** A save without the key, or with broken entries, keeps only the good ones (rule 17). */
  void load(CompoundTag tag) {
    List<UnloadChest> loaded = new ArrayList<>();
    for (Tag entry : tag.getList(KEY, Tag.TAG_COMPOUND)) {
      CompoundTag saved = (CompoundTag) entry;
      String dimension = saved.getString(DIMENSION_KEY);
      if (saved.contains(BLOCK_KEY) && !dimension.isBlank()) {
        loaded.add(new UnloadChest(saved.getLong(BLOCK_KEY), dimension));
      }
    }
    chests = new UnloadChests(loaded).fitting(robot.spec().unload().chests());
  }
}
