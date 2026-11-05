package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.DockStatusProperty;
import io.github.marcelofabianov.cubit.block.InductionDockBlock;
import io.github.marcelofabianov.cubit.registry.CubitBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/** Points each state of the dock to its model. The models come from the concept board. */
final class CubitBlockStateProvider extends BlockStateProvider {
  private static final String BLOCK_FOLDER = "block/";
  private static final String UPPER_SUFFIX = "_upper";
  private static final String NAME_SEPARATOR = "_";
  private static final int HALF_TURN_DEGREES = 180;
  private static final int FULL_TURN_DEGREES = 360;

  CubitBlockStateProvider(PackOutput output, ExistingFileHelper existingFiles) {
    super(output, CubitMod.MOD_ID, existingFiles);
  }

  @Override
  protected void registerStatesAndModels() {
    Block dock = CubitBlocks.INDUCTION_DOCK.get();
    getVariantBuilder(dock)
        .forAllStates(state -> {
          if (state.getValue(InductionDockBlock.HALF) == DoubleBlockHalf.UPPER) {
            return ConfiguredModel.builder().modelFile(model(UPPER_SUFFIX)).build();
          }
          int turn =
              ((int) state.getValue(InductionDockBlock.FACING).toYRot() + HALF_TURN_DEGREES)
                  % FULL_TURN_DEGREES;
          return ConfiguredModel.builder()
              .modelFile(statusModel(state.getValue(InductionDockBlock.STATUS)))
              .rotationY(turn)
              .build();
        });
    simpleBlockItem(dock, statusModel(DockStatusProperty.IDLE));
  }

  private ModelFile statusModel(DockStatusProperty status) {
    return model(NAME_SEPARATOR + status.getSerializedName());
  }

  private ModelFile model(String suffix) {
    return models().getExistingFile(modLoc(BLOCK_FOLDER + CubitBlocks.INDUCTION_DOCK_ID + suffix));
  }
}
