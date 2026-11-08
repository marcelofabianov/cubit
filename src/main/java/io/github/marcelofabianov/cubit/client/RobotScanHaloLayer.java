package io.github.marcelofabianov.cubit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotScanners;
import java.util.Optional;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/**
 * A soft glow behind the body, in the color of what the scanners found (answers 558 and 641). It
 * sits at the middle of the body and faces the camera, so the body covers its center and only the
 * rim shows around the robot. Only visual: the real light stays white.
 */
final class RobotScanHaloLayer extends GeoRenderLayer<RobotEntity> {
  /** How far the glow reaches, by the height of the robot. */
  private static final float HALF_SIZE_BY_HEIGHT = 0.8f;

  private static final float STRENGTH = 0.35f;
  private static final float HALF = 0.5f;

  private final RenderType haloType;

  RobotScanHaloLayer(GeoRenderer<RobotEntity> renderer, RobotType robotType) {
    super(renderer);
    haloType = RenderType.eyes(RobotAntennaGlowLayer.haloTexture(robotType));
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
    Optional<ScanColor> color = ScanColor.of(RobotScanners.signal(robot));
    if (robot.isInvisible() || color.isEmpty()) {
      return;
    }
    float height = robot.getBbHeight();
    Vector3f middle = poseStack.last().pose().transformPosition(new Vector3f(0, height * HALF, 0));
    float pulse = RobotAntennaGlowLayer.pulse(robot, partialTick);
    GlowHalo.draw(
        buffers.getBuffer(haloType),
        middle,
        height * HALF_SIZE_BY_HEIGHT,
        STRENGTH * pulse,
        GlowHalo.rgbOf(color.get()));
  }
}
