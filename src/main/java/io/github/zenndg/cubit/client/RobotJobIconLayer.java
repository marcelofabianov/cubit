package io.github.zenndg.cubit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.behavior.RobotExpression;
import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.domain.robot.RobotType;
import io.github.zenndg.cubit.entity.RobotEntity;
import io.github.zenndg.cubit.entity.RobotFace;
import io.github.zenndg.cubit.entity.RobotJobLook;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/**
 * The small icon of the job in the left corner of the screen, over the face, glowing as the screen
 * does (answer 1202). It goes out with the screen, off or asleep. Each job will have its own: for
 * now the fish of the Fisher, the wheat of the Farmer and the box of the Organizer (1229, 1307).
 */
final class RobotJobIconLayer extends GeoRenderLayer<RobotEntity> {
  private static final String ICON_PATH = "textures/entity/%s_screen/job_%s.png";
  private static final int NO_TINT = -1;

  private final Map<RobotJob, ResourceLocation> icons = new EnumMap<>(RobotJob.class);

  RobotJobIconLayer(GeoRenderer<RobotEntity> renderer, RobotType robotType) {
    super(renderer);
    for (RobotJob job : List.of(RobotJob.FISHER, RobotJob.FARMER, RobotJob.ORGANIZER)) {
      icons.put(
          job,
          ResourceLocation.fromNamespaceAndPath(
              CubitMod.MOD_ID, ICON_PATH.formatted(robotType.id(), job.key())));
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
    Optional<ResourceLocation> icon = RobotJobLook.job(robot).map(icons::get);
    RobotExpression expression = RobotFace.expression(robot);
    if (icon.isEmpty() || !expression.glows() || expression == RobotExpression.SLEEPY) {
      return;
    }
    RenderType type = RenderType.entityTranslucentEmissive(icon.get());
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
