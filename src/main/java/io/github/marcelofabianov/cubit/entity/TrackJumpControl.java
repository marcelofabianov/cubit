package io.github.marcelofabianov.cubit.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.JumpControl;

/** Tracks do not jump: the robot only climbs one block at a time, by its step height. */
final class TrackJumpControl extends JumpControl {
  TrackJumpControl(Mob mob) {
    super(mob);
  }

  @Override
  public void jump() {}
}
