package io.github.marcelofabianov.cubit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.TrackFrames;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/**
 * Draws the tracks over the body with one picture for each frame, so each robot rolls at its own
 * pace. The pictures are transparent everywhere but on the tracks.
 */
final class RobotTrackLayer extends GeoRenderLayer<RobotEntity> {
  private static final String FRAME_PATH = "textures/entity/%s_tracks/frame_%d.png";
  private static final float WALK_UNITS_PER_BLOCK = 4f;
  private static final int NO_TINT = -1;

  private final ResourceLocation[] frames = new ResourceLocation[TrackFrames.COUNT];

  RobotTrackLayer(GeoRenderer<RobotEntity> renderer, RobotType robotType) {
    super(renderer);
    for (int frame = 0; frame < frames.length; frame++) {
      frames[frame] =
          ResourceLocation.fromNamespaceAndPath(
              CubitMod.MOD_ID, FRAME_PATH.formatted(robotType.id(), frame));
    }
  }

  @Override
  public void render(
      PoseStack poseStack,
      RobotEntity robot,
      BakedGeoModel model,
      RenderType bodyType,
      MultiBufferSource buffers,
      VertexConsumer bodyBuffer,
      float partialTick,
      int packedLight,
      int packedOverlay) {
    float blocksWalked = robot.walkAnimation.position(partialTick) / WALK_UNITS_PER_BLOCK;
    RenderType type = RenderType.entityTranslucent(frames[TrackFrames.frameAt(blocksWalked)]);
    getRenderer()
        .reRender(
            model,
            poseStack,
            buffers,
            robot,
            type,
            buffers.getBuffer(type),
            partialTick,
            packedLight,
            packedOverlay,
            NO_TINT);
  }
}
