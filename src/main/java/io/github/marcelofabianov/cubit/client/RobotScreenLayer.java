package io.github.marcelofabianov.cubit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.BlinkCycle;
import io.github.marcelofabianov.cubit.domain.behavior.BlinkFrame;
import io.github.marcelofabianov.cubit.domain.behavior.GazeDirection;
import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotFace;
import io.github.marcelofabianov.cubit.entity.RobotLamp;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/**
 * Draws the screen over the body, at a higher resolution, so the face stays close to the concept.
 * The screen glows in the dark, except when it is off, which also greys the antennas. While the
 * lamp is lit, a soft glow around what lights up follows the same picture, so it blinks with it.
 */
final class RobotScreenLayer extends GeoRenderLayer<RobotEntity> {
  private static final String SCREEN_PATH = "textures/entity/%s_screen/%s.png";
  private static final String GLOW_PATH = "textures/entity/%s_screen_glow/%s.png";
  private static final String EYES_NAME = "normal_%s_%s";

  /** Spreads the robots over the cycle, so they do not all blink at the same moment. */
  private static final int BLINK_SPREAD_TICKS = 37;

  private static final int NO_TINT = -1;

  private final Map<RobotExpression, ResourceLocation> textures =
      new EnumMap<>(RobotExpression.class);
  private final Map<GazeDirection, Map<BlinkFrame, ResourceLocation>> eyes =
      new EnumMap<>(GazeDirection.class);
  private final Map<ResourceLocation, ResourceLocation> glows = new HashMap<>();

  RobotScreenLayer(GeoRenderer<RobotEntity> renderer, RobotType robotType) {
    super(renderer);
    for (RobotExpression expression : RobotExpression.values()) {
      textures.put(expression, screen(robotType, lower(expression)));
    }
    for (GazeDirection gaze : GazeDirection.values()) {
      Map<BlinkFrame, ResourceLocation> frames = new EnumMap<>(BlinkFrame.class);
      for (BlinkFrame frame : BlinkFrame.values()) {
        frames.put(frame, screen(robotType, EYES_NAME.formatted(lower(gaze), lower(frame))));
      }
      eyes.put(gaze, frames);
    }
    eyes.get(GazeDirection.LEVEL).put(BlinkFrame.OPEN, textures.get(RobotExpression.NORMAL));
  }

  private ResourceLocation screen(RobotType robotType, String name) {
    ResourceLocation screen = location(SCREEN_PATH, robotType, name);
    glows.put(screen, location(GLOW_PATH, robotType, name));
    return screen;
  }

  private static ResourceLocation location(String path, RobotType robotType, String name) {
    return ResourceLocation.fromNamespaceAndPath(
        CubitMod.MOD_ID, path.formatted(robotType.id(), name));
  }

  private static String lower(Enum<?> value) {
    return value.name().toLowerCase(Locale.ROOT);
  }

  /** Only the normal expression blinks and looks around. */
  private ResourceLocation textureOf(RobotEntity robot, RobotExpression expression) {
    if (expression != RobotExpression.NORMAL) {
      return textures.get(expression);
    }
    long tick = (long) robot.tickCount + (long) robot.getId() * BLINK_SPREAD_TICKS;
    return eyes.get(RobotGaze.of(robot)).get(BlinkCycle.frameAt(tick));
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
    RobotExpression expression = RobotFace.expression(robot);
    ResourceLocation texture = textureOf(robot, expression);
    boolean glows = expression.glows();
    RenderType type =
        glows
            ? RenderType.entityTranslucentEmissive(texture)
            : RenderType.entityTranslucent(texture);
    int light = glows ? LightTexture.FULL_BRIGHT : packedLight;
    getRenderer()
        .reRender(
            model,
            poseStack,
            buffers,
            robot,
            type,
            buffers.getBuffer(type),
            partialTick,
            light,
            packedOverlay,
            NO_TINT);
    if (glows && RobotLamp.isLit(robot)) {
      renderGlow(poseStack, robot, model, buffers, partialTick, packedOverlay, texture);
    }
  }

  private void renderGlow(
      PoseStack poseStack,
      RobotEntity robot,
      BakedGeoModel model,
      MultiBufferSource buffers,
      float partialTick,
      int packedOverlay,
      ResourceLocation screen) {
    RenderType type = RenderType.eyes(this.glows.get(screen));
    getRenderer()
        .reRender(
            model,
            poseStack,
            buffers,
            robot,
            type,
            buffers.getBuffer(type),
            partialTick,
            LightTexture.FULL_BRIGHT,
            packedOverlay,
            NO_TINT);
  }
}
