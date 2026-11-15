package io.github.marcelofabianov.cubit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotJobLook;
import java.util.List;
import java.util.function.Function;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/**
 * The hat and the backpack of the Fisher, always with its card (answers 1196, 1197 and 1200). They
 * stay hidden in the body and in the other layers, and only this layer shows them, with a picture
 * that is transparent everywhere but on them. The hat covers the solar panel, which still charges
 * (answer 1201).
 */
final class RobotFisherLayer extends GeoRenderLayer<RobotEntity> {
  private static final String TEXTURE_PATH = "textures/entity/%s_fisher.png";
  private static final List<String> BONES = List.of("fisher_hat", "fisher_backpack");
  private static final int NO_TINT = -1;

  private final ResourceLocation texture;

  RobotFisherLayer(GeoRenderer<RobotEntity> renderer, RobotType robotType) {
    super(renderer);
    texture =
        ResourceLocation.fromNamespaceAndPath(
            CubitMod.MOD_ID, TEXTURE_PATH.formatted(robotType.id()));
  }

  /** Called with each frame of the model, before the body and every layer draw it. */
  static void hideIn(Function<String, GeoBone> bones) {
    setHidden(bones, true);
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
    if (!RobotJobLook.fisher(robot)) {
      return;
    }
    Function<String, GeoBone> bones = name -> model.getBone(name).orElse(null);
    setHidden(bones, false);
    RenderType type = RenderType.entityCutoutNoCull(texture);
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
    setHidden(bones, true);
  }

  private static void setHidden(Function<String, GeoBone> bones, boolean hidden) {
    for (String name : BONES) {
      GeoBone bone = bones.apply(name);
      if (bone != null) {
        bone.setHidden(hidden);
      }
    }
  }
}
