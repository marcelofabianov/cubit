package io.github.marcelofabianov.cubit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.BatteryIndicator;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotFace;
import io.github.marcelofabianov.cubit.entity.RobotLooks;
import java.util.EnumMap;
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
 * Draws the battery on the back, at a higher resolution, so it stays close to the concept. The
 * frame takes the light of the world, and only the fill glows, in two pictures for that reason.
 */
final class RobotBatteryLayer extends GeoRenderLayer<RobotEntity> {
  private static final String BATTERY_PATH = "textures/entity/%s_battery/%s.png";
  private static final String FRAME_NAME = "frame";
  private static final int NO_TINT = -1;

  private final ResourceLocation frame;
  private final Map<BatteryIndicator, ResourceLocation> fills =
      new EnumMap<>(BatteryIndicator.class);

  RobotBatteryLayer(GeoRenderer<RobotEntity> renderer, RobotType robotType) {
    super(renderer);
    frame = battery(robotType, FRAME_NAME);
    for (BatteryIndicator indicator : BatteryIndicator.values()) {
      if (indicator != BatteryIndicator.EMPTY) {
        fills.put(indicator, battery(robotType, indicator.name().toLowerCase(Locale.ROOT)));
      }
    }
  }

  private static ResourceLocation battery(RobotType robotType, String name) {
    return ResourceLocation.fromNamespaceAndPath(
        CubitMod.MOD_ID, BATTERY_PATH.formatted(robotType.id(), name));
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
    draw(
        poseStack,
        robot,
        model,
        buffers,
        RenderType.entityTranslucent(frame),
        partialTick,
        packedLight,
        packedOverlay);
    BatteryIndicator indicator = RobotLooks.batteryIndicator(robot);
    if (indicator == BatteryIndicator.EMPTY) {
      return;
    }
    ResourceLocation fill = fills.get(indicator);
    boolean glows = RobotFace.expression(robot).glows();
    RenderType type =
        glows ? RenderType.entityTranslucentEmissive(fill) : RenderType.entityTranslucent(fill);
    int light = glows ? LightTexture.FULL_BRIGHT : packedLight;
    draw(poseStack, robot, model, buffers, type, partialTick, light, packedOverlay);
  }

  private void draw(
      PoseStack poseStack,
      RobotEntity robot,
      BakedGeoModel model,
      MultiBufferSource buffers,
      RenderType type,
      float partialTick,
      int light,
      int packedOverlay) {
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
  }
}
