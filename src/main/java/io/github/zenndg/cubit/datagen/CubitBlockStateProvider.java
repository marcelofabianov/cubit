package io.github.zenndg.cubit.datagen;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.block.DockStatusProperty;
import io.github.zenndg.cubit.block.DockTierProperty;
import io.github.zenndg.cubit.block.InductionDockBlock;
import io.github.zenndg.cubit.registry.CubitBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/** Points each state of the dock to its model. The models come from the concept board. */
final class CubitBlockStateProvider extends BlockStateProvider {
  private static final String UPPER_SUFFIX = "_upper";
  private static final String ULTIMATE_SUFFIX = "_ultimate";
  private static final String NO_SUFFIX = "";
  private static final String SOLAR_PANEL_SUFFIX = "_solar_panel";
  private static final String SOLAR_ITEM = "solar_induction_dock_item";
  private static final String PARTICLE = "particle";
  private static final String BLOCK_FOLDER = "block/";
  private static final String BUILTIN_ENTITY = "builtin/entity";
  // The display of a block item of the game, so the desk sits in the slot like any block.
  private static final float GUI_TILT = 30;
  private static final float GUI_TURN = 225;
  private static final float GUI_SCALE = 0.625f;
  private static final float GROUND_LIFT = 3;
  private static final float GROUND_SCALE = 0.25f;
  private static final float FIXED_SCALE = 0.5f;
  private static final float HAND_TILT = 75;
  private static final float HAND_TURN = 45;
  private static final float HAND_LIFT = 2.5f;
  private static final float HAND_SCALE = 0.375f;
  private static final float FIRST_PERSON_SCALE = 0.4f;
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
    engineeringDesk();
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

  /**
   * The desk is drawn by GeckoLib, from its left half, so its block model only gives the particles
   * of breaking, and its item is drawn by the same model (answer 1494).
   */
  private void engineeringDesk() {
    ModelFile particles =
        models()
            .getBuilder(CubitBlocks.ENGINEERING_DESK_ID)
            .texture(PARTICLE, modLoc(BLOCK_FOLDER + CubitBlocks.ENGINEERING_DESK_ID));
    getVariantBuilder(CubitBlocks.ENGINEERING_DESK.get())
        .forAllStates(state -> ConfiguredModel.builder().modelFile(particles).build());
    deskItem();
  }

  /** The item of the desk, drawn by GeckoLib, placed in each view like a block of the game. */
  private void deskItem() {
    itemModels()
        .getBuilder(CubitBlocks.ENGINEERING_DESK_ID)
        .parent(new ModelFile.UncheckedModelFile(BUILTIN_ENTITY))
        .transforms()
        .transform(ItemDisplayContext.GUI)
        .rotation(GUI_TILT, GUI_TURN, 0)
        .scale(GUI_SCALE)
        .end()
        .transform(ItemDisplayContext.GROUND)
        .translation(0, GROUND_LIFT, 0)
        .scale(GROUND_SCALE)
        .end()
        .transform(ItemDisplayContext.FIXED)
        .scale(FIXED_SCALE)
        .end()
        .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
        .rotation(HAND_TILT, HAND_TURN, 0)
        .translation(0, HAND_LIFT, 0)
        .scale(HAND_SCALE)
        .end()
        .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
        .rotation(0, HAND_TURN, 0)
        .scale(FIRST_PERSON_SCALE)
        .end()
        .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
        .rotation(0, GUI_TURN, 0)
        .scale(FIRST_PERSON_SCALE)
        .end();
  }
}
