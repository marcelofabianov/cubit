package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.domain.behavior.ContinuousSound;
import io.github.marcelofabianov.cubit.entity.RobotAudio;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.registry.CubitSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

/** A looping sound that follows its robot and stops by itself when its condition ends. */
final class RobotLoopSound extends AbstractTickableSoundInstance {
  /** A step can skip a tick, and stopping right away would restart the sound over and over. */
  private static final int STOP_DELAY_TICKS = 5;

  private static final float NORMAL_SPEED = 0.15f;
  private static final float MIN_SPEED_RATIO = 0.5f;
  private static final float MAX_SPEED_RATIO = 1.25f;
  private static final float MIN_PITCH = 0.8f;
  private static final float PITCH_PER_RATIO = 0.2f;

  private final RobotEntity robot;
  private final ContinuousSound sound;
  private int ticksWithoutCondition;

  RobotLoopSound(RobotEntity robot, ContinuousSound sound) {
    super(CubitSounds.of(sound), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
    this.robot = robot;
    this.sound = sound;
    looping = true;
    delay = 0;
    followRobot();
  }

  /** Without this, a sound started with the volume at zero would be dropped and never come back. */
  @Override
  public boolean canStartSilent() {
    return true;
  }

  @Override
  public void tick() {
    ticksWithoutCondition =
        RobotAudio.playing(robot).contains(sound) ? 0 : ticksWithoutCondition + 1;
    if (robot.isRemoved() || ticksWithoutCondition > STOP_DELAY_TICKS) {
      stop();
      return;
    }
    followRobot();
    if (sound == ContinuousSound.TRACKS) {
      followSpeed();
    }
  }

  private void followRobot() {
    x = robot.getX();
    y = robot.getY();
    z = robot.getZ();
  }

  private void followSpeed() {
    float speed = Mth.sqrt((float) robot.getDeltaMovement().horizontalDistanceSqr());
    float moved = (float) Math.hypot(robot.getX() - robot.xo, robot.getZ() - robot.zo);
    float ratio =
        Mth.clamp(Math.max(speed, moved) / NORMAL_SPEED, MIN_SPEED_RATIO, MAX_SPEED_RATIO);
    volume = Math.min(ratio, 1);
    pitch = MIN_PITCH + PITCH_PER_RATIO * ratio;
  }
}
