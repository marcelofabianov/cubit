package io.github.marcelofabianov.cubit.client;

import dev.lambdaurora.lambdynlights.api.entity.luminance.EntityLuminance;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSourceManager;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotLamp;
import net.minecraft.world.entity.Entity;

/** Gives the dynamic light the level the server decided. An invisible robot gives no light. */
final class RobotLuminance implements EntityLuminance {
  static final RobotLuminance INSTANCE = new RobotLuminance();
  private static final int NO_LIGHT = 0;

  private RobotLuminance() {}

  @Override
  public Type type() {
    return CubitDynamicLights.ROBOT_LUMINANCE;
  }

  @Override
  public int getLuminance(ItemLightSourceManager itemLightSources, Entity entity) {
    if (entity.isInvisible() || !(entity instanceof RobotEntity robot)) {
      return NO_LIGHT;
    }
    return RobotLamp.lightLevel(robot);
  }
}
