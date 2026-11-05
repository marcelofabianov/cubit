package io.github.marcelofabianov.cubit.registry;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.InductionDockBlock;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Block entities and the capabilities they offer to other mods. */
public final class CubitBlockEntities {
  private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
      DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CubitMod.MOD_ID);

  public static final Supplier<BlockEntityType<InductionDockBlockEntity>> INDUCTION_DOCK =
      BLOCK_ENTITIES.register(
          CubitBlocks.INDUCTION_DOCK_ID,
          () ->
              BlockEntityType.Builder.of(
                      InductionDockBlockEntity::new, CubitBlocks.INDUCTION_DOCK.get())
                  .build(null));

  private CubitBlockEntities() {}

  public static void register(IEventBus modBus) {
    BLOCK_ENTITIES.register(modBus);
    modBus.addListener(CubitBlockEntities::registerCapabilities);
  }

  private static void registerCapabilities(RegisterCapabilitiesEvent event) {
    // Registered by block, because the upper part of the dock has no block entity of its own.
    event.registerBlock(
        Capabilities.EnergyStorage.BLOCK,
        (level, pos, state, entity, side) -> {
          InductionDockBlockEntity dock = InductionDockBlock.dockAt(level, pos, state);
          return dock == null ? null : dock.energyStorage();
        },
        CubitBlocks.INDUCTION_DOCK.get());
    event.registerBlock(
        Capabilities.ItemHandler.BLOCK,
        (level, pos, state, entity, side) -> {
          InductionDockBlockEntity dock = InductionDockBlock.dockAt(level, pos, state);
          return dock == null ? null : dock.itemHandler();
        },
        CubitBlocks.INDUCTION_DOCK.get());
  }
}
