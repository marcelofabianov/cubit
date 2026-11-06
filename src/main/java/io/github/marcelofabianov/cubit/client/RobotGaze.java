package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.BodyTilt;
import io.github.marcelofabianov.cubit.domain.behavior.GazeDirection;
import io.github.marcelofabianov.cubit.domain.behavior.GazeTracker;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotFace;
import java.util.Map;
import java.util.WeakHashMap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

/** Keeps where each robot looks. It is only visual, so the server does not know about it. */
@EventBusSubscriber(modid = CubitMod.MOD_ID, value = Dist.CLIENT)
public final class RobotGaze {
  private static final Map<RobotEntity, GazeTracker> TRACKERS = new WeakHashMap<>();
  private static final Map<RobotEntity, BodyTilt> TILTS = new WeakHashMap<>();

  private RobotGaze() {}

  @SubscribeEvent
  public static void onEntityTick(EntityTickEvent.Post event) {
    if (event.getEntity() instanceof RobotEntity robot && robot.level().isClientSide()) {
      GazeTracker tracker = TRACKERS.getOrDefault(robot, GazeTracker.LEVEL);
      TRACKERS.put(robot, tracker.next(robot.getY() - robot.yo));
      BodyTilt tilt = TILTS.getOrDefault(robot, BodyTilt.STRAIGHT);
      TILTS.put(robot, tilt.next(RobotFace.ownerLevel(robot)));
    }
  }

  static float tiltDegrees(RobotEntity robot, float partialTick) {
    return TILTS.getOrDefault(robot, BodyTilt.STRAIGHT).at(partialTick);
  }

  static GazeDirection of(RobotEntity robot) {
    GazeDirection own = TRACKERS.getOrDefault(robot, GazeTracker.LEVEL).direction();
    return own.or(RobotFace.ownerGaze(robot));
  }
}
