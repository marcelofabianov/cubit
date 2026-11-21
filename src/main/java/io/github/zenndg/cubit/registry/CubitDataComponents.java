package io.github.zenndg.cubit.registry;

import com.mojang.serialization.Codec;
import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.block.SavedDeskOrder;
import io.github.zenndg.cubit.domain.robot.RobotState;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.item.EnergySlotContents;
import io.github.zenndg.cubit.item.RobotStateComponent;
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
  private static final String TORCH_SLOT_ID = "torch_slot";
  private static final String CARD_SLOT_ID = "card_slot";
  private static final String FERTILIZER_SLOT_ID = "fertilizer_slot";
  private static final String DOCK_ENERGY_ID = "dock_energy";
  private static final String DOCK_STORAGE_ID = "dock_storage";
  private static final String DOCK_TIER_ID = "dock_tier";
  private static final String DESK_TIER_ID = "engineering_desk_tier";
  private static final String DESK_ENERGY_ID = "engineering_desk_energy";
  private static final String DESK_INPUT_ID = "engineering_desk_input";
  private static final String DESK_OUTPUT_ID = "engineering_desk_output";
  private static final String DESK_ORDER_ID = "engineering_desk_order";
  private static final String POWER_BANK_ENERGY_ID = "power_bank_energy";
  private static final String TABLET_APP_ID = "tablet_app";
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

  /** The torches in the torch slot of the robot, a contract with the saves (answer 981). */
  public static final Supplier<DataComponentType<ItemContainerContents>> TORCH_SLOT =
      COMPONENTS.registerComponentType(
          TORCH_SLOT_ID,
          builder ->
              builder
                  .persistent(ItemContainerContents.CODEC)
                  .networkSynchronized(ItemContainerContents.STREAM_CODEC));

  /** The card of the card slot, kept like the torches, also in the damaged item (answer 1162). */
  public static final Supplier<DataComponentType<ItemContainerContents>> CARD_SLOT =
      COMPONENTS.registerComponentType(
          CARD_SLOT_ID,
          builder ->
              builder
                  .persistent(ItemContainerContents.CODEC)
                  .networkSynchronized(ItemContainerContents.STREAM_CODEC));

  /** The fertilizer of the Farmer, kept like the card, also in the damaged item (1244, 1247). */
  public static final Supplier<DataComponentType<ItemContainerContents>> FERTILIZER_SLOT =
      COMPONENTS.registerComponentType(
          FERTILIZER_SLOT_ID,
          builder ->
              builder
                  .persistent(ItemContainerContents.CODEC)
                  .networkSynchronized(ItemContainerContents.STREAM_CODEC));

  public static final Supplier<DataComponentType<Long>> DOCK_ENERGY =
      COMPONENTS.registerComponentType(
          DOCK_ENERGY_ID,
          builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG));

  /** The tier of a dock, kept in its item when it breaks (answer 1373). */
  public static final Supplier<DataComponentType<RobotTier>> DOCK_TIER =
      COMPONENTS.registerComponentType(
          DOCK_TIER_ID,
          builder ->
              builder
                  .persistent(DockTierCodec.CODEC)
                  .networkSynchronized(DockTierCodec.STREAM_CODEC));

  /** The storage of a dock, kept in its item when it breaks (answers 954 and 958). */
  public static final Supplier<DataComponentType<ItemContainerContents>> DOCK_STORAGE =
      COMPONENTS.registerComponentType(
          DOCK_STORAGE_ID,
          builder ->
              builder
                  .persistent(ItemContainerContents.CODEC)
                  .networkSynchronized(ItemContainerContents.STREAM_CODEC));

  /** The tier of an Engineering Desk, kept in its item when it breaks (answer 1492). */
  public static final Supplier<DataComponentType<RobotTier>> DESK_TIER =
      COMPONENTS.registerComponentType(
          DESK_TIER_ID,
          builder ->
              builder
                  .persistent(DockTierCodec.CODEC)
                  .networkSynchronized(DockTierCodec.STREAM_CODEC));

  /** The energy of an Engineering Desk (answer 1492). */
  public static final Supplier<DataComponentType<Long>> DESK_ENERGY =
      COMPONENTS.registerComponentType(
          DESK_ENERGY_ID,
          builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG));

  /** The input inventory of an Engineering Desk (answer 1492). */
  public static final Supplier<DataComponentType<ItemContainerContents>> DESK_INPUT =
      COMPONENTS.registerComponentType(
          DESK_INPUT_ID,
          builder ->
              builder
                  .persistent(ItemContainerContents.CODEC)
                  .networkSynchronized(ItemContainerContents.STREAM_CODEC));

  /** The output inventory of an Engineering Desk (answer 1492). */
  public static final Supplier<DataComponentType<ItemContainerContents>> DESK_OUTPUT =
      COMPONENTS.registerComponentType(
          DESK_OUTPUT_ID,
          builder ->
              builder
                  .persistent(ItemContainerContents.CODEC)
                  .networkSynchronized(ItemContainerContents.STREAM_CODEC));

  /** The recipe and the order of an Engineering Desk (answer 1492). */
  public static final Supplier<DataComponentType<SavedDeskOrder>> DESK_ORDER =
      COMPONENTS.registerComponentType(
          DESK_ORDER_ID,
          builder ->
              builder
                  .persistent(SavedDeskOrder.CODEC)
                  .networkSynchronized(SavedDeskOrder.STREAM_CODEC));

  /** The charge of a Power Bank, a contract with the saves (answer 903). */
  /** The active app of the Tablet, by its key, a contract with the saves (answer 1026). */
  public static final Supplier<DataComponentType<String>> TABLET_APP =
      COMPONENTS.registerComponentType(
          TABLET_APP_ID,
          builder ->
              builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));

  public static final Supplier<DataComponentType<Long>> POWER_BANK_ENERGY =
      COMPONENTS.registerComponentType(
          POWER_BANK_ENERGY_ID,
          builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG));

  private CubitDataComponents() {}

  public static void register(IEventBus modBus) {
    COMPONENTS.register(modBus);
  }
}
