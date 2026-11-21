package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.block.DockStatusProperty;
import io.github.zenndg.cubit.block.DockTierProperty;
import io.github.zenndg.cubit.block.InductionDockBlock;
import io.github.zenndg.cubit.registry.CubitBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/** Points each state of the dock to its model. The models come from the concept board. */
final class CubitBlockStateProvider extends BlockStateProvider {
  private static final String BLOCK_FOLDER = "block/";
  private static final String UPPER_SUFFIX = "_upper";
  private static final String ULTIMATE_SUFFIX = "_ultimate";
  private static final String NO_SUFFIX = "";
  private static final String SOLAR_PANEL_SUFFIX = "_solar_panel";
  private static final String SOLAR_ITEM = "solar_induction_dock_item";
  private static final String NAME_SEPARATOR = "_";

  CubitBlockStateProvider(PackOutput output, ExistingFileHelper existingFiles) {
    super(output, CubitMod.MOD_ID, existingFiles);
  }

  @Override
  protected void registerStatesAndModels() {
    Block dock = CubitBlocks.INDUCTION_DOCK.get();
    getVariantBuilder(dock)
        .forAllStates(state -> {
          String body = bodyOf(state.getValue(InductionDockBlock.TIER));
          if (state.getValue(InductionDockBlock.HALF) == DoubleBlockHalf.UPPER) {
            return ConfiguredModel.builder().modelFile(model(UPPER_SUFFIX + body)).build();
          }
          int turn = InductionDockBlock.turnOf(state.getValue(InductionDockBlock.FACING));
          return ConfiguredModel.builder()
              .modelFile(statusModel(state.getValue(InductionDockBlock.STATUS), body))
              .rotationY(turn)
              .build();
        });
    itemModels()
        .getBuilder(CubitBlocks.INDUCTION_DOCK_ID)
        .parent(statusModel(DockStatusProperty.IDLE, NO_SUFFIX))
        .override()
        .predicate(InductionDockBlock.ULTIMATE_PROPERTY, 1)
        .model(statusModel(DockStatusProperty.IDLE, ULTIMATE_SUFFIX))
        .end();
    solarDock();
  }

  /**
   * The Solar Dock is the dock and its panel, a part of its own turned with the dock (answer 1384).
   */
  private void solarDock() {
    MultiPartBlockStateBuilder parts = getMultipartBuilder(CubitBlocks.SOLAR_INDUCTION_DOCK.get());
    for (Direction facing : Direction.Plane.HORIZONTAL) {
      solarLowerParts(parts, facing);
    }
    for (DockTierProperty tier : DockTierProperty.values()) {
      parts
          .part()
          .modelFile(model(UPPER_SUFFIX + bodyOf(tier)))
          .addModel()
          .condition(InductionDockBlock.HALF, DoubleBlockHalf.UPPER)
          .condition(InductionDockBlock.TIER, tier)
          .end();
    }
    itemModels()
        .getBuilder(CubitBlocks.SOLAR_INDUCTION_DOCK_ID)
        .parent(models().getExistingFile(modLoc(BLOCK_FOLDER + SOLAR_ITEM)))
        .override()
        .predicate(InductionDockBlock.ULTIMATE_PROPERTY, 1)
        .model(models().getExistingFile(modLoc(BLOCK_FOLDER + SOLAR_ITEM + ULTIMATE_SUFFIX)))
        .end();
  }

  /** The lower half: the dock by status and tier, and the panel, turned with the facing. */
  private void solarLowerParts(MultiPartBlockStateBuilder parts, Direction facing) {
    int turn = InductionDockBlock.turnOf(facing);
    for (DockStatusProperty status : DockStatusProperty.values()) {
      for (DockTierProperty tier : DockTierProperty.values()) {
        parts
            .part()
            .modelFile(statusModel(status, bodyOf(tier)))
            .rotationY(turn)
            .addModel()
            .condition(InductionDockBlock.FACING, facing)
            .condition(InductionDockBlock.HALF, DoubleBlockHalf.LOWER)
            .condition(InductionDockBlock.STATUS, status)
            .condition(InductionDockBlock.TIER, tier)
            .end();
      }
    }
    parts
        .part()
        .modelFile(model(SOLAR_PANEL_SUFFIX))
        .rotationY(turn)
        .addModel()
        .condition(InductionDockBlock.FACING, facing)
        .condition(InductionDockBlock.HALF, DoubleBlockHalf.LOWER)
        .end();
  }

  /** The Ultimate has a netherite body, in its own models (answer 1380). */
  private static String bodyOf(DockTierProperty tier) {
    return tier == DockTierProperty.ULTIMATE ? ULTIMATE_SUFFIX : NO_SUFFIX;
  }

  private ModelFile statusModel(DockStatusProperty status, String body) {
    return model(NAME_SEPARATOR + status.getSerializedName() + body);
  }

  private ModelFile model(String suffix) {
    return models().getExistingFile(modLoc(BLOCK_FOLDER + CubitBlocks.INDUCTION_DOCK_ID + suffix));
  }
}
