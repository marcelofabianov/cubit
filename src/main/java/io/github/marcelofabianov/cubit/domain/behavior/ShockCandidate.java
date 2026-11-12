package io.github.marcelofabianov.cubit.domain.behavior;

/** What a living thing near the robot is, for the shock. The game layer classifies it. */
public enum ShockCandidate {
  OWNER,
  PLAYER,
  ROBOT,
  PEACEFUL_MOB,
  NEUTRAL_MOB,
  HOSTILE_MOB
}
