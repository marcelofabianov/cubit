package io.github.marcelofabianov.cubit.domain.energy;

import java.util.ArrayList;
import java.util.List;

/** Why the robot spent energy in a tick, in the order the screen lists them. */
public enum EnergyUseCause {
  WALKING,
  FULL_INVENTORY,
  WATER,
  CLIMBING,
  DANGER,
  LAMP;

  /** Load, water and climbing only cost while walking, as in {@link ConsumptionExtras}. */
  public static List<EnergyUseCause> of(RobotActivity activity) {
    List<EnergyUseCause> causes = new ArrayList<>();
    if (activity.walking()) {
      causes.add(WALKING);
      addIf(causes, activity.fullInventory(), FULL_INVENTORY);
      addIf(causes, activity.inWater(), WATER);
      addIf(causes, activity.climbedBlocks() > 0, CLIMBING);
    }
    addIf(causes, activity.negativeCondition(), DANGER);
    addIf(causes, activity.lampLit(), LAMP);
    return List.copyOf(causes);
  }

  private static void addIf(List<EnergyUseCause> causes, boolean present, EnergyUseCause cause) {
    if (present) {
      causes.add(cause);
    }
  }
}
