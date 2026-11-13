package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.block.InductionDockBlock;
import io.github.marcelofabianov.cubit.registry.CubitBlocks;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
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

/** The dock keeps its energy as an item, so breaking it never loses FE. */
final class CubitBlockLootProvider extends BlockLootSubProvider {
  private static final float ONE_ROLL = 1f;

  CubitBlockLootProvider(HolderLookup.Provider registries) {
    super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
  }

  @Override
  protected void generate() {
    Block dock = CubitBlocks.INDUCTION_DOCK.get();
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
                                    .include(CubitDataComponents.DOCK_STORAGE.get())))));
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    return List.of(CubitBlocks.INDUCTION_DOCK.get());
  }
}
