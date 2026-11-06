package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.behavior.ContinuousSound;
import io.github.marcelofabianov.cubit.domain.behavior.RobotSoundRules;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.registry.CubitSounds;
import java.util.EnumSet;
import java.util.Set;

/**
 * Plays the sounds of a robot. Short sounds go out from the server. Loops run on the client, which
 * only learns what is playing from the synced data.
 */
public final class RobotAudio {
  private RobotAudio() {}

  public static Set<ContinuousSound> playing(RobotEntity robot) {
    byte flags = robot.getEntityData().get(RobotSyncedData.PLAYING_SOUNDS);
    Set<ContinuousSound> sounds = EnumSet.noneOf(ContinuousSound.class);
    for (ContinuousSound sound : ContinuousSound.values()) {
      if ((flags & flagOf(sound)) != 0) {
        sounds.add(sound);
      }
    }
    return sounds;
  }

  static void update(RobotEntity robot, boolean walking, Energy chargeBefore) {
    boolean charging = RobotSoundRules.isCharging(chargeBefore, robot.battery().charge());
    int flags = 0;
    for (ContinuousSound sound :
        RobotSoundRules.playing(robot.powerState(), walking, robot.isOnFire(), charging)) {
      flags |= flagOf(sound);
    }
    robot.getEntityData().set(RobotSyncedData.PLAYING_SOUNDS, (byte) flags);
  }

  static void onStateChange(RobotEntity robot, PowerState from, PowerState to) {
    RobotSoundRules.powerSound(from, to).ifPresent(sound -> robot.playSound(CubitSounds.of(sound)));
  }

  static void onPlaced(RobotEntity robot) {
    robot.playSound(CubitSounds.PLACED.get());
  }

  private static int flagOf(ContinuousSound sound) {
    return 1 << sound.ordinal();
  }
}
