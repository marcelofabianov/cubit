package io.github.marcelofabianov.cubit.compat.jade;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.InductionDockBlock;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.energy.EnergyReading;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/**
 * The stock and the status of the screen of an Induction Dock in Jade (answer 662). The stock is
 * only on the server, and the status is in the block state the client already has.
 */
enum DockJadeProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
  INSTANCE;

  private static final String STOCK = "Stock";
  private static final String CAPACITY = "Capacity";

  @Override
  public void appendServerData(CompoundTag data, BlockAccessor accessor) {
    if (accessor.getBlockEntity() instanceof InductionDockBlockEntity dock) {
      data.putLong(STOCK, dock.energy().amount());
      data.putLong(CAPACITY, CubitMod.CONFIG.dockEnergyCapacity());
    }
  }

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
    CompoundTag data = accessor.getServerData();
    if (data.contains(STOCK) && data.contains(CAPACITY)) {
      tooltip.add(
          JadeEnergyText.stock(EnergyReading.capped(data.getLong(STOCK), data.getLong(CAPACITY))));
    }
    BlockState state = accessor.getBlockState();
    if (state.hasProperty(InductionDockBlock.STATUS)) {
      String status = state.getValue(InductionDockBlock.STATUS).getSerializedName();
      tooltip.add(Component.translatable(JadeKeys.dockStatus(status)));
    }
  }

  @Override
  public ResourceLocation getUid() {
    return JadeKeys.DOCK_UID;
  }
}
