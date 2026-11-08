package io.github.marcelofabianov.cubit.domain.energy;

/** What each situation adds to the energy a robot spends. The numbers come from the config. */
public record ConsumptionExtras(
    Energy fullInventoryPerTick,
    Energy waterPerTick,
    Energy negativeConditionPerTick,
    Energy climbPerBlock,
    Energy lampPerTick) {

  /** Load, water and climbing only cost while walking. The rest costs even standing still. */
  public Energy of(RobotActivity activity) {
    Energy total = activity.negativeCondition() ? negativeConditionPerTick : Energy.ZERO;
    if (activity.lampLit()) {
      total = total.plus(lampPerTick);
    }
    if (!activity.walking()) {
      return total;
    }
    if (activity.fullInventory()) {
      total = total.plus(fullInventoryPerTick);
    }
    if (activity.inWater()) {
      total = total.plus(waterPerTick);
    }
    return total.plus(climbing(activity.climbedBlocks()));
  }

  /** Energy is always whole (AGENTS.md, rule 13), so a partial block is rounded. */
  private Energy climbing(double blocks) {
    return new Energy(Math.round(climbPerBlock.amount() * blocks));
  }
}
