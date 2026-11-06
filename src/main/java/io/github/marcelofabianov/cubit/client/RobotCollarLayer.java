package io.github.marcelofabianov.cubit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotLooks;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/**
 * Paints the collar with the color of the tier. The colors live in the pictures, one for each tier,
 * which are transparent everywhere but on the collar.
 */
final class RobotCollarLayer extends GeoRenderLayer<RobotEntity> {
  private static final String COLLAR_PATH = "textures/entity/%s_collar/%s.png";
  private static final int NO_TINT = -1;

  private final Map<RobotTier, ResourceLocation> textures = new EnumMap<>(RobotTier.class);

  RobotCollarLayer(GeoRenderer<RobotEntity> renderer, RobotType robotType) {
    super(renderer);
    for (RobotTier tier : RobotTier.values()) {
      textures.put(
          tier,
          ResourceLocation.fromNamespaceAndPath(
              CubitMod.MOD_ID, COLLAR_PATH.formatted(robotType.id(), tier.key())));
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
    RenderType type = RenderType.entityTranslucent(textures.get(RobotLooks.tier(robot)));
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
