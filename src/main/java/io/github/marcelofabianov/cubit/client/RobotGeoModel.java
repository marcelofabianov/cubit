package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.domain.behavior.RobotPose;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotFace;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

/** The model has no separate head: to look around, the whole body turns and leans over the neck. */
final class RobotGeoModel extends DefaultedEntityGeoModel<RobotEntity> {
  private static final String TURNING_BONE = "body";
  private static final float MAX_YAW_DEGREES = 45f;

  RobotGeoModel(ResourceLocation assetSubpath) {
    super(assetSubpath);
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
