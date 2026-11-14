package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.tier.DeathPointLevel;

/**
 * Whether the guide button of the Deaths tab can send the chosen Cubit, or why not: only a tier
 * that guides, with the switch on, a death in the Overworld and the Cubit that follows the owner
 * there (answers 1117, 1118 and 1120).
 */
public enum DeathGuideCall {
  READY,
  LOCKED,
  SWITCHED_OFF,
  DEATH_ELSEWHERE,
  CUBIT_ELSEWHERE,
  NOT_FOLLOWING;

  public static DeathGuideCall of(
      DeathPointLevel level,
      boolean switchedOn,
      boolean deathInOverworld,
      boolean cubitInOverworld,
      boolean following) {
    if (!level.guides()) {
      return LOCKED;
    }
    if (!switchedOn) {
      return SWITCHED_OFF;
    }
    if (!deathInOverworld) {
      return DEATH_ELSEWHERE;
    }
    if (!cubitInOverworld) {
      return CUBIT_ELSEWHERE;
    }
    return following ? READY : NOT_FOLLOWING;
  }

  public boolean ready() {
    return this == READY;
  }

  /** From Advanced on, with the switch on, the Cubit that follows tells where (answer 1111). */
  public static boolean tellsOnRespawn(
      DeathPointLevel level, boolean switchedOn, boolean following) {
    return level.marks() && switchedOn && following;
  }
}
