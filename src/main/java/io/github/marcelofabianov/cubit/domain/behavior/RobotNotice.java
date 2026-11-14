package io.github.marcelofabianov.cubit.domain.behavior;

/** The short texts a robot shows its owner. Each one waits its own interval to repeat. */
public enum RobotNotice {
  CRITICAL_BATTERY,
  HURT,
  OWNER_HURT,
  NEED_RECHARGE,
  ITEMS_UNLOADED,
  NO_ROOM
}
