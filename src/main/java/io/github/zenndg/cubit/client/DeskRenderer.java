package io.github.zenndg.cubit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.zenndg.cubit.block.EngineeringDeskBlockEntity;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

/** Draws the whole desk from its left half, turned by its facing, with the band of its tier. */
public final class DeskRenderer extends GeoBlockRenderer<EngineeringDeskBlockEntity> {
  public DeskRenderer(BlockEntityRendererProvider.Context context) {
    super(
        new DeskGeoModel<>() {
          @Override
          RobotTier tierOf(EngineeringDeskBlockEntity desk) {
            return desk.shownTier();
          }
        });
  }

  /** The desk is two blocks wide, with the lamp and the arm above it. */
  @Override
  public AABB getRenderBoundingBox(EngineeringDeskBlockEntity desk) {
    return new AABB(desk.getBlockPos()).inflate(1);
  }

  @Override
  public void renderRecursively(
      PoseStack poseStack,
      EngineeringDeskBlockEntity desk,
      GeoBone bone,
      RenderType renderType,
      MultiBufferSource bufferSource,
      VertexConsumer buffer,
      boolean isReRender,
      float partialTick,
      int packedLight,
      int packedOverlay,
      int colour) {
    super.renderRecursively(
        poseStack,
        desk,
        bone,
        renderType,
        bufferSource,
        buffer,
        isReRender,
        partialTick,
        packedLight,
        packedOverlay,
        DeskBand.colour(bone, desk.shownTier(), colour));
  }
}
