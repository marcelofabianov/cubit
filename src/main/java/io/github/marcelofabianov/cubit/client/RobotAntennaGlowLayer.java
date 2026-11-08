package io.github.marcelofabianov.cubit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.LampPulse;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotLamp;
import io.github.marcelofabianov.cubit.entity.RobotScanners;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoVertex;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/**
 * Lights the tips of the antennas, with a soft halo that always faces the camera: white while the
 * lamp is lit, and red or blue while the scanners found something, even by day and with the lamp
 * off (answers 558 and 638). With neither, the layer draws nothing, so the tips keep their color.
 */
final class RobotAntennaGlowLayer extends GeoRenderLayer<RobotEntity> {
  private static final String GLOW_PATH = "textures/entity/%s_antenna/glow.png";
  private static final String HALO_PATH = "textures/entity/%s_antenna/halo.png";
  private static final Set<String> ANTENNAS = Set.of("left_antenna", "right_antenna");
  private static final int NO_TINT = -1;
  private static final float HALO_HALF_SIZE = 0.28f;
  private static final float HALO_STRENGTH = 0.65f;

  /** Spreads the robots over the pulse, so they do not all breathe at the same moment. */
  private static final int PULSE_SPREAD_TICKS = 17;

  private final RenderType glowType;
  private final RenderType haloType;

  RobotAntennaGlowLayer(GeoRenderer<RobotEntity> renderer, RobotType robotType) {
    super(renderer);
    glowType = RenderType.eyes(texture(GLOW_PATH, robotType));
    haloType = RenderType.eyes(texture(HALO_PATH, robotType));
  }

  static ResourceLocation texture(String path, RobotType robotType) {
    return ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, path.formatted(robotType.id()));
  }

  static ResourceLocation haloTexture(RobotType robotType) {
    return texture(HALO_PATH, robotType);
  }

  /** The scanners win over the lamp, so a found mob shows even at night. */
  private static Optional<ScanColor> scanColor(RobotEntity robot) {
    return ScanColor.of(RobotScanners.signal(robot));
  }

  private static boolean isGlowing(RobotEntity robot) {
    return !robot.isInvisible() && (RobotLamp.isLit(robot) || scanColor(robot).isPresent());
  }

  /** Spread per robot, so they do not all breathe at the same moment. */
  static float pulse(RobotEntity robot, float partialTick) {
    double tick = (double) robot.tickCount + partialTick + robot.getId() * PULSE_SPREAD_TICKS;
    return (float) LampPulse.at(tick);
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
    int tint = scanColor(robot).map(ScanColor::argb).orElse(NO_TINT);
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
            tint);
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
    float[] rgb = scanColor(robot).map(GlowHalo::rgbOf).orElse(GlowHalo.WHITE);
    float pulse = pulse(robot, partialTick);
    GlowHalo.draw(
        buffers.getBuffer(haloType), tip, HALO_HALF_SIZE * pulse, HALO_STRENGTH * pulse, rgb);
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
}
