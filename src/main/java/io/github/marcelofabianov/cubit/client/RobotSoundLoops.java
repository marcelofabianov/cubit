package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.ContinuousSound;
import io.github.marcelofabianov.cubit.entity.RobotAudio;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import java.util.EnumMap;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

/** Starts the loops of each robot. Weak keys let the game forget a robot that left the world. */
@EventBusSubscriber(modid = CubitMod.MOD_ID, value = Dist.CLIENT)
public final class RobotSoundLoops {
  private static final Map<RobotEntity, Map<ContinuousSound, RobotLoopSound>> LOOPS =
      new WeakHashMap<>();

  private RobotSoundLoops() {}

  @SubscribeEvent
  public static void onEntityTick(EntityTickEvent.Post event) {
    if (event.getEntity() instanceof RobotEntity robot && robot.level().isClientSide()) {
      start(robot);
    }
  }

  private static void start(RobotEntity robot) {
    for (ContinuousSound sound : RobotAudio.playing(robot)) {
      Map<ContinuousSound, RobotLoopSound> loops =
          LOOPS.computeIfAbsent(robot, key -> new EnumMap<>(ContinuousSound.class));
      SoundManager manager = Minecraft.getInstance().getSoundManager();
      RobotLoopSound current = loops.get(sound);
      if (current == null || !manager.isActive(current)) {
        RobotLoopSound loop = new RobotLoopSound(robot, sound);
        loops.put(sound, loop);
        manager.play(loop);
      }
    }
  }
}
