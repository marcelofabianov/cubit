package io.github.marcelofabianov.cubit.registry;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.InductionDockBlock;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Blocks. The IDs are part of the saves, so they cannot change without a migration. */
public final class CubitBlocks {
  public static final String INDUCTION_DOCK_ID = "induction_dock";
  private static final float DOCK_HARDNESS = 3.5f;

  /** As strong as bedrock: no explosion breaks a block that only its owner may break. */
  private static final float DOCK_EXPLOSION_RESISTANCE = 3_600_000f;

  private static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(Registries.BLOCK, CubitMod.MOD_ID);

  public static final Supplier<Block> INDUCTION_DOCK =
      BLOCKS.register(
          INDUCTION_DOCK_ID,
          () ->
              new InductionDockBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.METAL)
                      .strength(DOCK_HARDNESS, DOCK_EXPLOSION_RESISTANCE)
                      .sound(SoundType.METAL)
                      .noOcclusion()));

  private CubitBlocks() {}

  public static void register(IEventBus modBus) {
    BLOCKS.register(modBus);
  }
}
