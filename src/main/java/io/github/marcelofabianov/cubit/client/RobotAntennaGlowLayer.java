package io.github.marcelofabianov.cubit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.LampPulse;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotLamp;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoVertex;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/**
 * Lights the tips of the antennas while the lamp is lit, with a soft halo that always faces the
 * camera. Without the lamp the layer draws nothing, so the tips keep their own color.
 */
final class RobotAntennaGlowLayer extends GeoRenderLayer<RobotEntity> {
  private static final String GLOW_PATH = "textures/entity/%s_antenna/glow.png";
  private static final String HALO_PATH = "textures/entity/%s_antenna/halo.png";
  private static final Set<String> ANTENNAS = Set.of("left_antenna", "right_antenna");
  private static final int NO_TINT = -1;
  private static final int MAX_COLOR = 255;
  private static final float HALO_HALF_SIZE = 0.28f;
  private static final float HALO_STRENGTH = 0.65f;
  private static final float TEXTURE_END = 1f;
  private static final float NORMAL_UP = 1f;

  /** Spreads the robots over the pulse, so they do not all breathe at the same moment. */
  private static final int PULSE_SPREAD_TICKS = 17;

  private final RenderType glowType;
  private final RenderType haloType;

  RobotAntennaGlowLayer(GeoRenderer<RobotEntity> renderer, RobotType robotType) {
    super(renderer);
    glowType = RenderType.eyes(texture(GLOW_PATH, robotType));
    haloType = RenderType.eyes(texture(HALO_PATH, robotType));
  }

  private static ResourceLocation texture(String path, RobotType robotType) {
    return ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, path.formatted(robotType.id()));
  }

  private static boolean isGlowing(RobotEntity robot) {
    return !robot.isInvisible() && RobotLamp.isLit(robot);
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
    if (!isGlowing(robot)) {
      return;
    }
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
            NO_TINT);
  }

  /** Called while the pose is that of the bone, so the halo follows the swing of the antenna. */
  @Override
  public void renderForBone(
      PoseStack poseStack,
      RobotEntity robot,
      GeoBone bone,
      RenderType bodyType,
      MultiBufferSource buffers,
      VertexConsumer bodyBuffer,
      float partialTick,
      int packedLight,
      int packedOverlay) {
    if (!ANTENNAS.contains(bone.getName()) || !isGlowing(robot)) {
      return;
    }
    Vector3f tip = poseStack.last().pose().transformPosition(tipOf(bone));
    double tick = (double) robot.tickCount + partialTick + robot.getId() * PULSE_SPREAD_TICKS;
    drawHalo(buffers.getBuffer(haloType), tip, (float) LampPulse.at(tick));
  }

  /** The tip is the last cube of an antenna in the model. */
  private static Vector3f tipOf(GeoBone bone) {
    GeoCube tip = bone.getCubes().getLast();
    Vector3f sum = new Vector3f();
    int vertices = 0;
    for (GeoQuad quad : tip.quads()) {
      for (GeoVertex vertex : quad.vertices()) {
        sum.add(vertex.position());
        vertices++;
      }
    }
    return sum.div(vertices);
  }

  private static void drawHalo(VertexConsumer buffer, Vector3f center, float pulse) {
    float half = HALO_HALF_SIZE * pulse;
    Matrix4f facing =
        new Matrix4f()
            .translation(center)
            .rotate(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
    int color = Math.round(MAX_COLOR * HALO_STRENGTH * pulse);
    vertex(buffer, facing, -half, -half, 0, TEXTURE_END, color);
    vertex(buffer, facing, half, -half, TEXTURE_END, TEXTURE_END, color);
    vertex(buffer, facing, half, half, TEXTURE_END, 0, color);
    vertex(buffer, facing, -half, half, 0, 0, color);
  }

  private static void vertex(
      VertexConsumer buffer, Matrix4f facing, float x, float y, float u, float v, int color) {
    buffer
        .addVertex(facing, x, y, 0)
        .setColor(color, color, color, MAX_COLOR)
        .setUv(u, v)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(LightTexture.FULL_BRIGHT)
        .setNormal(0, NORMAL_UP, 0);
  }
}
