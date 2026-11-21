package io.github.zenndg.cubit.block;

import com.mojang.logging.LogUtils;
import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.desk.DeskOrder;
import io.github.zenndg.cubit.domain.desk.DeskProgress;
import io.github.zenndg.cubit.domain.desk.DeskSpec;
import io.github.zenndg.cubit.domain.desk.DeskStatus;
import io.github.zenndg.cubit.domain.desk.DeskStock;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.ownership.Ownership;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.item.EnergySlotItems;
import io.github.zenndg.cubit.item.SavedEnergy;
import io.github.zenndg.cubit.registry.CubitBlockEntities;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * The state of an Engineering Desk: its owner, tier, energy, inventories and order. It lives in the
 * left half, and the rules live in the domain.
 */
public final class EngineeringDeskBlockEntity extends BlockEntity implements GeoBlockEntity {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String OWNER_KEY = "Owner";
  private static final String ENERGY_KEY = "Energy";
  private static final String SLOT_KEY = "Slot";
  private static final String INPUT_KEY = "Input";
  private static final String OUTPUT_KEY = "Output";
  private static final String ORDER_KEY = "Order";
  private static final String PROGRESS_KEY = "Progress";
  private static final String GONE_RECIPE = "The desk at {} had the recipe {}, which is gone.";
  private static final int ENERGY_SLOT = 0;

  private final DeskTier tier = new DeskTier();
  private final SimpleContainer input = container(DeskSpec.MAX_SLOTS);
  private final SimpleContainer output = container(DeskSpec.MAX_SLOTS);
  private final SimpleContainer energySlot = container(1);
  private final DeskEnergyStorage energyStorage = new DeskEnergyStorage(this);
  private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

  private UUID owner;
  private DeskStock stock = DeskStock.of(spec().capacity(), Energy.ZERO);
  private ResourceLocation recipe;
  private DeskOrder order = DeskOrder.NONE;
  private DeskProgress progress = DeskProgress.NONE;
  private DeskStatus status = DeskStatus.IDLE;
  private Energy inputLeft = Energy.ZERO;
  private int signal;

  public EngineeringDeskBlockEntity(BlockPos pos, BlockState state) {
    super(CubitBlockEntities.ENGINEERING_DESK.get(), pos, state);
  }

  private SimpleContainer container(int size) {
    return new SimpleContainer(size) {
      @Override
      public void setChanged() {
        EngineeringDeskBlockEntity.this.setChanged();
      }
    };
  }

  static void serverTick(
      Level level, BlockPos pos, BlockState state, EngineeringDeskBlockEntity desk) {
    desk.inputLeft = CubitMod.CONFIG.engineeringDesk().inputPerTick();
    desk.useEnergySlot();
    desk.status = DeskWork.tick(desk, level, EngineeringDeskBlock.hasSignal(level, pos, state));
    desk.showWork(level, pos, state);
  }

  private void showWork(Level level, BlockPos pos, BlockState state) {
    int now = order.comparatorSignal();
    if (now != signal) {
      signal = now;
      level.updateNeighbourForOutputSignal(pos, state.getBlock());
    }
  }

  /** Redstone becomes energy whole, from the slot or from below (answers 1425 and 1447). */
  private void useEnergySlot() {
    ItemStack stack = energySlot.getItem(ENERGY_SLOT);
    Energy itemEnergy = EnergySlotItems.energyOf(stack);
    if (stock.canTake(itemEnergy)) {
      stack.shrink(1);
      stock = stock.take(itemEnergy);
      setChanged();
    }
  }

  public DeskSpec spec() {
    return tier.spec();
  }

  public RobotTier tier() {
    return tier.tier();
  }

  /** The tier the client shows, read from the block state that both halves carry. */
  public RobotTier shownTier() {
    return getBlockState().getValue(EngineeringDeskBlock.TIER).tier();
  }

  public int slots() {
    return spec().storageSlots();
  }

  public Container input() {
    return input;
  }

  public Container output() {
    return output;
  }

  public Container energySlot() {
    return energySlot;
  }

  DeskEnergyStorage energyStorage() {
    return energyStorage;
  }

  public Energy energy() {
    return stock.charge();
  }

  public Energy capacity() {
    return stock.capacity();
  }

  public DeskOrder order() {
    return order;
  }

  public DeskStatus status() {
    return status;
  }

  public int progressPercent() {
    return progress.percent(spec().craftTicks());
  }

  public Optional<ResourceLocation> recipeId() {
    return Optional.ofNullable(recipe);
  }

  /** The chosen recipe, while it exists. One a pack removed ends the order (rule 17). */
  Optional<RecipeHolder<CraftingRecipe>> recipe() {
    if (recipe == null || level == null) {
      return Optional.empty();
    }
    Optional<RecipeHolder<CraftingRecipe>> found =
        DeskRecipes.find(level.getRecipeManager(), recipe);
    if (found.isEmpty()) {
      LOGGER.warn(GONE_RECIPE, worldPosition, recipe);
      choose(null);
    }
    return found;
  }

  boolean canPayCraft() {
    return stock.canPay(CubitMod.CONFIG.engineeringDesk().craftEnergy());
  }

  /** One tick of the craft. True when it is ready to end. */
  boolean advance() {
    progress = progress.advance();
    setChanged();
    return progress.isReady(spec().craftTicks());
  }

  void crafted() {
    stock = stock.pay(CubitMod.CONFIG.engineeringDesk().craftEnergy());
    order = order.crafted();
    progress = DeskProgress.NONE;
    setChanged();
  }

  /** A new recipe starts an order of one, and none clears it (answers 1442 and 1444). */
  public void choose(ResourceLocation chosen) {
    recipe = chosen;
    order = chosen == null ? DeskOrder.NONE : DeskOrder.start();
    progress = DeskProgress.NONE;
    setChanged();
  }

  /** The minus and plus buttons. Without an order, plus starts one for the chosen recipe. */
  public void adjustOrder(int delta) {
    if (recipe == null || (!order.isActive() && delta <= 0)) {
      return;
    }
    order = order.adjust(delta, spec().maxOrder());
    if (!order.isActive()) {
      progress = DeskProgress.NONE;
    }
    setChanged();
  }

  public void cancelOrder() {
    order = DeskOrder.NONE;
    progress = DeskProgress.NONE;
    setChanged();
  }

  void upgradeTo(RobotTier next) {
    tier.set(next);
    stock = stock.withCapacity(spec().capacity());
    order = order.limitedTo(spec().maxOrder());
    setChanged();
    if (level != null) {
      EngineeringDeskBlock.showTier(level, worldPosition, getBlockState(), next);
    }
  }

  Energy receive(Energy offered, boolean simulate) {
    DeskStock.Received received = stock.receive(offered, inputLeft);
    if (!simulate) {
      stock = received.stock();
      inputLeft = inputLeft.minus(received.accepted());
      setChanged();
    }
    return received.accepted();
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

  public int signal() {
    return order.comparatorSignal();
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    if (owner != null) {
      tag.putUUID(OWNER_KEY, owner);
    }
    tag.putLong(ENERGY_KEY, energy().amount());
    tier.save(tag);
    DeskSaves.saveItems(tag, INPUT_KEY, input, registries);
    DeskSaves.saveItems(tag, OUTPUT_KEY, output, registries);
    DeskSaves.saveItems(tag, SLOT_KEY, energySlot, registries);
    DeskSaves.saveOrder(tag, ORDER_KEY, savedOrder());
    tag.putInt(PROGRESS_KEY, progress.ticks());
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    owner = tag.hasUUID(OWNER_KEY) ? tag.getUUID(OWNER_KEY) : null;
    tier.load(tag, worldPosition);
    stock = DeskStock.of(spec().capacity(), SavedEnergy.of(tag.getLong(ENERGY_KEY)));
    DeskSaves.loadItems(tag, INPUT_KEY, input, registries);
    DeskSaves.loadItems(tag, OUTPUT_KEY, output, registries);
    DeskSaves.loadItems(tag, SLOT_KEY, energySlot, registries);
    useOrder(DeskSaves.loadOrder(tag, ORDER_KEY, worldPosition));
    progress = new DeskProgress(Math.max(0, tag.getInt(PROGRESS_KEY)));
  }

  private Optional<SavedDeskOrder> savedOrder() {
    return recipeId().map(id -> new SavedDeskOrder(id, order.total(), order.done()));
  }

  private void useOrder(Optional<SavedDeskOrder> saved) {
    recipe = saved.map(SavedDeskOrder::recipe).orElse(null);
    order = saved.map(SavedDeskOrder::order).orElse(DeskOrder.NONE).limitedTo(spec().maxOrder());
  }

  @Override
  protected void applyImplicitComponents(DataComponentInput components) {
    super.applyImplicitComponents(components);
    tier.set(components.getOrDefault(CubitDataComponents.DESK_TIER.get(), RobotTier.BASE));
    long saved = components.getOrDefault(CubitDataComponents.DESK_ENERGY.get(), 0L);
    stock = DeskStock.of(spec().capacity(), SavedEnergy.of(saved));
    DeskSaves.apply(components.get(CubitDataComponents.DESK_INPUT.get()), input);
    DeskSaves.apply(components.get(CubitDataComponents.DESK_OUTPUT.get()), output);
    useOrder(Optional.ofNullable(components.get(CubitDataComponents.DESK_ORDER.get())));
  }

  @Override
  protected void collectImplicitComponents(DataComponentMap.Builder components) {
    super.collectImplicitComponents(components);
    components.set(CubitDataComponents.DESK_TIER.get(), tier());
    components.set(CubitDataComponents.DESK_ENERGY.get(), energy().amount());
    components.set(CubitDataComponents.DESK_INPUT.get(), DeskSaves.contents(input));
    components.set(CubitDataComponents.DESK_OUTPUT.get(), DeskSaves.contents(output));
    savedOrder().ifPresent(saved -> components.set(CubitDataComponents.DESK_ORDER.get(), saved));
  }

  /** Only the owner and the tier travel to the client: the owner decides who breaks the block. */
  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    CompoundTag tag = new CompoundTag();
    if (owner != null) {
      tag.putUUID(OWNER_KEY, owner);
    }
    tier.save(tag);
    return tag;
  }

  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    // The animations come with the second part of the desk (answer 1494).
  }

  @Override
  public AnimatableInstanceCache getAnimatableInstanceCache() {
    return animationCache;
  }
}
