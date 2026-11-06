package io.github.marcelofabianov.cubit.config;

import io.github.marcelofabianov.cubit.domain.behavior.FollowDecision;
import io.github.marcelofabianov.cubit.domain.energy.ConsumptionExtras;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.energy.PowerRules;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.domain.tier.TierCatalog;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import java.util.List;
import java.util.Map;
import net.neoforged.neoforge.common.ModConfigSpec;

/** The COMMON config. The keys and limits are documented in _docs/desenvolvimento/config.md. */
public final class CubitConfig {
  public static final RobotType GENERAL_ROBOT = new RobotType("general_robot");
  private static final String GENERAL_SECTION = "general";
  private static final int TIER_SECTION_DEPTH = 2;
  private static final long BASE_BATTERY = 300_000;
  private static final long BASE_CONSUMPTION = 15;
  private static final long BASE_FULL_INVENTORY_EXTRA = 5;
  private static final long BASE_WATER_EXTRA = 5;
  private static final long BASE_NEGATIVE_CONDITION_EXTRA = 25;
  private static final long BASE_CLIMB_EXTRA = 15;
  private static final long BASE_RECHARGE = 500;
  private static final int BASE_SLOTS = 27;
  private static final int BASE_HEALTH = 25;
  private static final int BASE_CALL_RADIUS = 15;
  private static final double BASE_SPEED_MULTIPLIER = 1;
  private static final TierSpec GENERAL_ROBOT_BASE =
      new TierSpec(
          new Energy(BASE_BATTERY),
          new Energy(BASE_CONSUMPTION),
          new Energy(BASE_RECHARGE),
          BASE_SLOTS,
          BASE_HEALTH,
          BASE_CALL_RADIUS,
          BASE_SPEED_MULTIPLIER,
          new ConsumptionExtras(
              new Energy(BASE_FULL_INVENTORY_EXTRA),
              new Energy(BASE_WATER_EXTRA),
              new Energy(BASE_NEGATIVE_CONDITION_EXTRA),
              new Energy(BASE_CLIMB_EXTRA)));

  private final ModConfigSpec spec;
  private final GeneralSection general;
  private final DamagedRobotSection damagedRobot;
  private final EnergySlotSection energySlot;
  private final LoadSection load;
  private final ExpressionSection expression;
  private final DockSection dock;
  private final TierCatalog tierCatalog;

  public CubitConfig() {
    ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    builder.push(GENERAL_SECTION);
    general = new GeneralSection(builder);
    damagedRobot = new DamagedRobotSection(builder);
    energySlot = new EnergySlotSection(builder);
    load = new LoadSection(builder);
    expression = new ExpressionSection(builder);
    dock = new DockSection(builder);
    builder.pop();

    builder.push(List.of(GENERAL_ROBOT.id(), RobotTier.BASE.key()));
    TierSection generalRobotBase = new TierSection(builder, GENERAL_ROBOT_BASE);
    builder.pop(TIER_SECTION_DEPTH);

    tierCatalog =
        new NeoForgeTierCatalog(Map.of(GENERAL_ROBOT, Map.of(RobotTier.BASE, generalRobotBase)));
    spec = builder.build();
  }

  public ModConfigSpec spec() {
    return spec;
  }

  public TierCatalog tierCatalog() {
    return tierCatalog;
  }

  public Percentage initialCharge() {
    return new Percentage(general.initialCharge.get());
  }

  public Percentage criticalCharge() {
    return new Percentage(expression.criticalCharge.get());
  }

  public int worriedExpressionTicks() {
    return expression.worriedTicks.get();
  }

  public int worriedRadiusBlocks() {
    return expression.worriedRadius.get();
  }

  public int dyingExpressionTicks() {
    return expression.dyingTicks.get();
  }

  public int hurtExpressionTicks() {
    return expression.hurtTicks.get();
  }

  public Percentage dyingHealth() {
    return new Percentage(expression.dyingHealth.get());
  }

  public int happyExpressionTicks() {
    return expression.happyTicks.get();
  }

  public PowerRules powerRules() {
    return new PowerRules(new Percentage(general.wakeCharge.get()));
  }

  /** The limits of the two keys do not overlap, so this is always a valid decision. */
  public FollowDecision followDecision() {
    return new FollowDecision(general.stopDistance.get(), general.teleportDistance.get());
  }

  public double baseMovementSpeed() {
    return general.baseSpeed.get();
  }

  public int followRepathTicks() {
    return general.repathTicks.get();
  }

  public double burningSpeedMultiplier() {
    return general.burningSpeed.get();
  }

  public double fullInventorySpeedMultiplier() {
    return load.fullInventorySpeed.get();
  }

  public int waterSearchRadiusBlocks() {
    return general.waterSearchRadius.get();
  }

  public int burningWanderRadiusBlocks() {
    return general.burningWanderRadius.get();
  }

  public int redstoneDustEnergy() {
    return energySlot.dustEnergy.get();
  }

  public int redstoneBlockEnergy() {
    return energySlot.blockEnergy.get();
  }

  public int dockEnergyCapacity() {
    return dock.capacity.get();
  }

  public int dockInputPerTick() {
    return dock.input.get();
  }

  public int dockFullHealTicks() {
    return dock.fullHealTicks.get();
  }

  public int dockCompleteHoldTicks() {
    return dock.completeHoldTicks.get();
  }

  public int dockStuckTicks() {
    return dock.stuckTicks.get();
  }

  public Percentage repairHealth() {
    return new Percentage(damagedRobot.repairHealth.get());
  }

  public int voidRescueRetryTicks() {
    return damagedRobot.voidRescueRetry.get();
  }
}
