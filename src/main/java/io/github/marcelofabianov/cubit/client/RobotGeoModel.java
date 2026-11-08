package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.domain.behavior.RobotPose;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotFace;
import io.github.marcelofabianov.cubit.entity.RobotLooks;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

/** The model has no separate head: to look around, the whole body turns and leans over the neck. */
final class RobotGeoModel extends DefaultedEntityGeoModel<RobotEntity> {
  private static final String TURNING_BONE = "body";
  private static final float MAX_YAW_DEGREES = 45f;
  private static final String WHITE_PATH = "textures/entity/%s.png";
  private static final String COLOR_PATH = "textures/entity/%s_color/%s.png";

  private final Map<RobotColor, ResourceLocation> textures = new EnumMap<>(RobotColor.class);

  RobotGeoModel(ResourceLocation assetSubpath) {
    super(assetSubpath);
    for (RobotColor color : RobotColor.values()) {
      String path =
          color == RobotColor.WHITE
              ? WHITE_PATH.formatted(assetSubpath.getPath())
              : COLOR_PATH.formatted(assetSubpath.getPath(), color.key());
      textures.put(color, assetSubpath.withPath(path));
    }
  }

  /** Each color is a picture of its own, so the layers on top keep their colors. */
  @Override
  public ResourceLocation getTextureResource(RobotEntity robot, GeoRenderer<RobotEntity> renderer) {
    return textures.get(RobotLooks.color(robot));
  }

  @Override
  public void setCustomAnimations(
      RobotEntity robot, long instanceId, AnimationState<RobotEntity> state) {
    super.setCustomAnimations(robot, instanceId, state);
    GeoBone body = getAnimationProcessor().getBone(TURNING_BONE);
    EntityModelData data = state.getData(DataTickets.ENTITY_MODEL_DATA);
    if (body != null && data != null) {
      float yaw = Mth.clamp(data.netHeadYaw(), -MAX_YAW_DEGREES, MAX_YAW_DEGREES);
      body.setRotY(yaw * Mth.DEG_TO_RAD);
    }
    // The powered down animation leans the body by itself, and writing here would erase it.
    if (body != null
        && RobotPose.of(RobotFace.expression(robot), false) != RobotPose.POWERED_DOWN) {
      body.setRotX(RobotGaze.tiltDegrees(robot, state.getPartialTick()) * Mth.DEG_TO_RAD);
    }
  }
}
