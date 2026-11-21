package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.block.DockStatusProperty;
import io.github.zenndg.cubit.block.DockTierProperty;
import io.github.zenndg.cubit.block.InductionDockBlock;
import io.github.zenndg.cubit.registry.CubitBlocks;
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
  private static final String ULTIMATE_SUFFIX = "_ultimate";
  private static final String NO_SUFFIX = "";
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
          String body = bodyOf(state.getValue(InductionDockBlock.TIER));
          if (state.getValue(InductionDockBlock.HALF) == DoubleBlockHalf.UPPER) {
            return ConfiguredModel.builder().modelFile(model(UPPER_SUFFIX + body)).build();
          }
          int turn =
              ((int) state.getValue(InductionDockBlock.FACING).toYRot() + HALF_TURN_DEGREES)
                  % FULL_TURN_DEGREES;
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
