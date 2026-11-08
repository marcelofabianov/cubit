package io.github.marcelofabianov.cubit.registry;

import com.mojang.serialization.Codec;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.item.EnergySlotContents;
import io.github.marcelofabianov.cubit.item.RobotStateComponent;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Item data. The IDs are part of the saves, so they cannot change without a migration. */
public final class CubitDataComponents {
  private static final String ROBOT_STATE_ID = "robot_state";
  private static final String LAST_SAFE_POSITION_ID = "last_safe_position";
  private static final String ROBOT_OWNER_ID = "robot_owner";
  private static final String ROBOT_INVENTORY_ID = "robot_inventory";
  private static final String ENERGY_SLOT_ID = "energy_slot";
  private static final String DOCK_ENERGY_ID = "dock_energy";
  private static final DeferredRegister.DataComponents COMPONENTS =
      DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CubitMod.MOD_ID);

  public static final Supplier<DataComponentType<RobotState>> ROBOT_STATE =
      COMPONENTS.registerComponentType(
          ROBOT_STATE_ID,
          builder ->
              builder
                  .persistent(RobotStateComponent.CODEC)
                  .networkSynchronized(RobotStateComponent.STREAM_CODEC));

  public static final Supplier<DataComponentType<GlobalPos>> LAST_SAFE_POSITION =
      COMPONENTS.registerComponentType(
          LAST_SAFE_POSITION_ID,
          builder ->
              builder.persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC));

  public static final Supplier<DataComponentType<UUID>> ROBOT_OWNER =
      COMPONENTS.registerComponentType(
          ROBOT_OWNER_ID,
          builder -> builder.persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC));

  public static final Supplier<DataComponentType<ItemContainerContents>> ROBOT_INVENTORY =
      COMPONENTS.registerComponentType(
          ROBOT_INVENTORY_ID,
          builder ->
              builder
                  .persistent(ItemContainerContents.CODEC)
                  .networkSynchronized(ItemContainerContents.STREAM_CODEC));

  public static final Supplier<DataComponentType<EnergySlotContents>> ENERGY_SLOT =
      COMPONENTS.registerComponentType(
          ENERGY_SLOT_ID,
          builder ->
              builder
                  .persistent(EnergySlotContents.CODEC)
                  .networkSynchronized(EnergySlotContents.STREAM_CODEC));

  public static final Supplier<DataComponentType<Long>> DOCK_ENERGY =
      COMPONENTS.registerComponentType(
          DOCK_ENERGY_ID,
          builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG));

  private CubitDataComponents() {}

  public static void register(IEventBus modBus) {
    COMPONENTS.register(modBus);
  }
}
