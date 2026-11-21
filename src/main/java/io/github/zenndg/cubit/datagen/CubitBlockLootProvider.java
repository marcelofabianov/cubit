package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.block.DeskPart;
import io.github.zenndg.cubit.block.EngineeringDeskBlock;
import io.github.zenndg.cubit.block.InductionDockBlock;
import io.github.zenndg.cubit.registry.CubitBlocks;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import java.util.List;
import java.util.Set;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

/** The dock and the desk keep their energy and items as an item, so breaking never loses any. */
final class CubitBlockLootProvider extends BlockLootSubProvider {
  private static final float ONE_ROLL = 1f;

  CubitBlockLootProvider(HolderLookup.Provider registries) {
    super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
  }

  @Override
  protected void generate() {
    dockLoot(CubitBlocks.INDUCTION_DOCK.get());
    dockLoot(CubitBlocks.SOLAR_INDUCTION_DOCK.get());
    deskLoot(CubitBlocks.ENGINEERING_DESK.get());
  }

  /** Only the lower half drops the dock, with its energy, storage and tier (answer 1373). */
  private void dockLoot(Block dock) {
    add(
        dock,
        LootTable.lootTable()
            .withPool(
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(ONE_ROLL))
                    .add(
                        LootItem.lootTableItem(dock)
                            .when(
                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(dock)
                                    .setProperties(
                                        StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(
                                                InductionDockBlock.HALF, DoubleBlockHalf.LOWER)))
                            .apply(
                                CopyComponentsFunction.copyComponents(
                                        CopyComponentsFunction.Source.BLOCK_ENTITY)
                                    .include(CubitDataComponents.DOCK_ENERGY.get())
                                    .include(CubitDataComponents.DOCK_STORAGE.get())
                                    .include(CubitDataComponents.DOCK_TIER.get())))));
  }

  /** Only the left half drops the desk, with its tier, energy, inventories and order (1432). */
  private void deskLoot(Block desk) {
    add(
        desk,
        LootTable.lootTable()
            .withPool(
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(ONE_ROLL))
                    .add(
                        LootItem.lootTableItem(desk)
                            .when(
                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(desk)
                                    .setProperties(
                                        StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(EngineeringDeskBlock.PART, DeskPart.LEFT)))
                            .apply(
                                CopyComponentsFunction.copyComponents(
                                        CopyComponentsFunction.Source.BLOCK_ENTITY)
                                    .include(CubitDataComponents.DESK_TIER.get())
                                    .include(CubitDataComponents.DESK_ENERGY.get())
                                    .include(CubitDataComponents.DESK_INPUT.get())
                                    .include(CubitDataComponents.DESK_OUTPUT.get())
                                    .include(CubitDataComponents.DESK_ORDER.get())))));
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    return List.of(
        CubitBlocks.INDUCTION_DOCK.get(),
        CubitBlocks.SOLAR_INDUCTION_DOCK.get(),
        CubitBlocks.ENGINEERING_DESK.get());
  }
}
