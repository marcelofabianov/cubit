package io.github.marcelofabianov.cubit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.SolarPulse;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotSolar;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/**
 * Draws a slow pulse on the solar cell while the panel fills the battery. The picture is black
 * everywhere but on the cell, and adding black changes nothing.
 */
final class RobotSolarPanelLayer extends GeoRenderLayer<RobotEntity> {
  private static final String GLOW_PATH = "textures/entity/%s_solar/glow.png";
  private static final int MAX_COLOR = 255;
  private static final int ALPHA_SHIFT = 24;
  private static final int RED_SHIFT = 16;
  private static final int GREEN_SHIFT = 8;

  /** Spreads the robots over the pulse, so they do not all breathe at the same moment. */
  private static final int PULSE_SPREAD_TICKS = 23;

  private final RenderType glowType;

  RobotSolarPanelLayer(GeoRenderer<RobotEntity> renderer, RobotType robotType) {
    super(renderer);
    glowType = RenderType.eyes(texture(GLOW_PATH, robotType));
  }

  private static ResourceLocation texture(String path, RobotType robotType) {
    return ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, path.formatted(robotType.id()));
  }

  private static int gray(double strength) {
    int value = (int) Math.round(MAX_COLOR * strength);
    return MAX_COLOR << ALPHA_SHIFT | value << RED_SHIFT | value << GREEN_SHIFT | value;
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
    if (RobotSolar.isCharging(robot)) {
      double tick = (double) robot.tickCount + partialTick + robot.getId() * PULSE_SPREAD_TICKS;
      getRenderer()
          .reRender(
              model,
              poseStack,
              buffers,
              robot,
              glowType,
              buffers.getBuffer(glowType),
              partialTick,
              LightTexture.FULL_BRIGHT,
              packedOverlay,
              gray(SolarPulse.at(tick)));
    }
  }
}
