package io.github.marcelofabianov.cubit.block;

import com.mojang.logging.LogUtils;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.dock.DockQueue;
import io.github.marcelofabianov.cubit.domain.dock.DockStatus;
import io.github.marcelofabianov.cubit.domain.dock.DockStock;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.ownership.Ownership;
import io.github.marcelofabianov.cubit.item.EnergySlotItems;
import io.github.marcelofabianov.cubit.item.SavedEnergy;
import io.github.marcelofabianov.cubit.registry.CubitBlockEntities;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.slf4j.Logger;

/** The state of a dock: its owner, its energy and its slot. The rules live in the domain. */
public final class InductionDockBlockEntity extends BlockEntity {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String NO_OWNER_MESSAGE = "The dock at {} has no owner.";
  private static final String OWNER_KEY = "Owner";
  private static final String ENERGY_KEY = "Energy";
  private static final String SLOT_KEY = "Slot";
  private static final int SLOT = 0;
  private static final int SLOT_SIZE = 1;
  private static final int ONE_ITEM = 1;
  private static final double PLATE_HEIGHT = 3.0 / 16;

  /** 12 units away from the tower: exactly the width of a robot, and a little margin. */
  private static final double TOWARD_FRONT = 2.05 / 16;

  private static final long ROBOT_LOST_TICKS = 3;

  private final SimpleContainer slot =
      new SimpleContainer(SLOT_SIZE) {
        @Override
        public boolean canPlaceItem(int index, ItemStack stack) {
          return EnergySlotItems.accepts(stack);
        }

        @Override
        public void setChanged() {
          InductionDockBlockEntity.this.setChanged();
        }
      };
  private final IEnergyStorage energyStorage = new DockEnergyStorage(this);

  /** Automation may only put redstone in: the energy it becomes never comes back out. */
  private final IItemHandler itemHandler =
      new InvWrapper(slot) {
        @Override
        public ItemStack extractItem(int index, int amount, boolean simulate) {
          return ItemStack.EMPTY;
        }
      };

  private UUID owner;
  private UUID occupant;
  private long occupantSeen;
  private boolean robotFull;
  private DockQueue queue = DockQueue.EMPTY;
  private DockStock stock = emptyStock();
  private Energy inputLeft = inputPerTick();

  public InductionDockBlockEntity(BlockPos pos, BlockState state) {
    super(CubitBlockEntities.INDUCTION_DOCK.get(), pos, state);
  }

  private static DockStock emptyStock() {
    return DockStock.of(capacity(), Energy.ZERO);
  }

  private static Energy capacity() {
    return new Energy(CubitMod.CONFIG.dockEnergyCapacity());
  }

  private static Energy inputPerTick() {
    return new Energy(CubitMod.CONFIG.dockInputPerTick());
  }

  static void serverTick(
      Level level, BlockPos pos, BlockState state, InductionDockBlockEntity dock) {
    dock.inputLeft = inputPerTick();
    dock.useSlot();
    dock.showStatus(level, pos, state);
  }

  private void useSlot() {
    ItemStack stack = slot.getItem(SLOT);
    Energy itemEnergy = EnergySlotItems.energyOf(stack);
    if (stock.canTake(itemEnergy)) {
      stack.shrink(ONE_ITEM);
      stock = stock.take(itemEnergy);
      setChanged();
    }
  }

  private void showStatus(Level level, BlockPos pos, BlockState state) {
    DockStatus status =
        DockStatus.decide(stock.energy().isEmpty(), isOccupied(), isOccupied() && robotFull);
    DockStatusProperty shown = DockStatusProperty.of(status);
    if (state.getValue(InductionDockBlock.STATUS) != shown) {
      level.setBlock(pos, state.setValue(InductionDockBlock.STATUS, shown), Block.UPDATE_CLIENTS);
    }
  }

  public Optional<UUID> owner() {
    return Optional.ofNullable(owner);
  }

  public boolean isOwner(Player player) {
    return Ownership.owns(owner(), player.getUUID());
  }

  void setOwner(Player player) {
    owner = player.getUUID();
    setChanged();
    if (level != null) {
      level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }
  }

  /** A robot reports itself on every tick, so one that left, by any way, frees the dock. */
  public void reportRobot(UUID robot, boolean full) {
    occupant = robot;
    robotFull = full;
    occupantSeen = level.getGameTime();
  }

  public boolean isOccupied() {
    return occupant != null
        && level != null
        && level.getGameTime() - occupantSeen < ROBOT_LOST_TICKS;
  }

  Optional<UUID> occupant() {
    return isOccupied() ? Optional.of(occupant) : Optional.empty();
  }

  public void reportWaiting(UUID robot) {
    queue = queue.report(robot, level.getGameTime());
  }

  /** Nobody waiting also counts: the robot asking is then the next one. */
  public boolean isNextInLine(UUID robot) {
    return queue.first(level.getGameTime()).filter(first -> !first.equals(robot)).isEmpty();
  }

  public Battery charge(Battery robot, Energy rate) {
    DockStock.Transfer transfer = stock.charge(robot, rate);
    stock = transfer.stock();
    setChanged();
    return transfer.robot();
  }

  /** Where a robot stands, between the front of the plate and the tower. */
  public Vec3 robotSpot() {
    Direction front = getBlockState().getValue(InductionDockBlock.FACING);
    return Vec3.atBottomCenterOf(worldPosition)
        .add(front.getStepX() * TOWARD_FRONT, PLATE_HEIGHT, front.getStepZ() * TOWARD_FRONT);
  }

  public Energy energy() {
    return stock.energy().charge();
  }

  public Container slot() {
    return slot;
  }

  public IEnergyStorage energyStorage() {
    return energyStorage;
  }

  public IItemHandler itemHandler() {
    return itemHandler;
  }

  public int energyLevel(int steps) {
    return stock.energy().level(steps);
  }

  boolean insert(ItemStack stack) {
    return EnergySlotItems.insert(slot, stack);
  }

  Energy receive(Energy offered, boolean simulate) {
    DockStock.Received received = stock.receive(offered, inputLeft);
    if (!simulate) {
      stock = received.stock();
      inputLeft = inputLeft.minus(received.accepted());
      setChanged();
    }
    return received.accepted();
  }

  long capacityAmount() {
    return stock.energy().capacity().amount();
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    if (owner != null) {
      tag.putUUID(OWNER_KEY, owner);
    }
    tag.putLong(ENERGY_KEY, energy().amount());
    ItemStack stack = slot.getItem(SLOT);
    if (!stack.isEmpty()) {
      tag.put(SLOT_KEY, stack.save(registries));
    }
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    owner = tag.hasUUID(OWNER_KEY) ? tag.getUUID(OWNER_KEY) : null;
    if (owner == null && (level == null || !level.isClientSide())) {
      LOGGER.warn(NO_OWNER_MESSAGE, worldPosition);
    }
    stock = DockStock.of(capacity(), SavedEnergy.of(tag.getLong(ENERGY_KEY)));
    slot.setItem(
        SLOT,
        tag.contains(SLOT_KEY)
            ? ItemStack.parse(registries, tag.getCompound(SLOT_KEY)).orElse(ItemStack.EMPTY)
            : ItemStack.EMPTY);
  }

  @Override
  protected void applyImplicitComponents(DataComponentInput input) {
    super.applyImplicitComponents(input);
    long saved = input.getOrDefault(CubitDataComponents.DOCK_ENERGY.get(), 0L);
    stock = DockStock.of(capacity(), SavedEnergy.of(saved));
  }

  @Override
  protected void collectImplicitComponents(DataComponentMap.Builder components) {
    super.collectImplicitComponents(components);
    components.set(CubitDataComponents.DOCK_ENERGY.get(), energy().amount());
  }

  /** Only the owner travels to the client, because it decides who can break the block. */
  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    CompoundTag tag = new CompoundTag();
    if (owner != null) {
      tag.putUUID(OWNER_KEY, owner);
    }
    return tag;
  }

  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}
