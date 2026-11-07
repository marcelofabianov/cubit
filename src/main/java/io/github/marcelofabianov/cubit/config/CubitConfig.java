package io.github.marcelofabianov.cubit.config;

import com.mojang.logging.LogUtils;
import io.github.marcelofabianov.cubit.domain.behavior.FollowDecision;
import io.github.marcelofabianov.cubit.domain.behavior.LampPolicy;
import io.github.marcelofabianov.cubit.domain.behavior.LightLevel;
import io.github.marcelofabianov.cubit.domain.energy.ConsumptionExtras;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.energy.PowerRules;
import io.github.marcelofabianov.cubit.domain.energy.SolarOutput;
import io.github.marcelofabianov.cubit.domain.robot.OreKind;
import io.github.marcelofabianov.cubit.domain.robot.OreSignals;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.domain.tier.ScannerSpec;
import io.github.marcelofabianov.cubit.domain.tier.TierCatalog;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.slf4j.Logger;

/** The COMMON config. The keys and limits are documented in _docs/desenvolvimento/config.md. */
public final class CubitConfig {
  public static final RobotType GENERAL_ROBOT = new RobotType("general_robot");
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String REVERSED_LAMP = "The lamp limits are reversed: off {}, on {}";
  private static final String INVALID_RARE_TAG = "Ignoring an invalid tag in rare_ore_tags: {}";
  private static final String GENERAL_SECTION = "general";
  private static final int TIER_SECTION_DEPTH = 2;
  private static final long BASE_BATTERY = 300_000;
  private static final long BASE_CONSUMPTION = 15;
  private static final long BASE_FULL_INVENTORY_EXTRA = 5;
  private static final long BASE_WATER_EXTRA = 5;
  private static final long BASE_NEGATIVE_CONDITION_EXTRA = 25;
  private static final long BASE_CLIMB_EXTRA = 15;
  private static final long BASE_LAMP_CONSUMPTION = 1;
  private static final int BASE_LAMP_LIGHT = 10;
  private static final long BASE_SOLAR_DAY = 5;
  private static final long BASE_RECHARGE = 500;
  private static final int BASE_SLOTS = 27;
  private static final int BASE_HEALTH = 25;
  private static final int BASE_CALL_RADIUS = 15;
  private static final double BASE_SPEED_MULTIPLIER = 1;
  private static final int BASE_SCAN_RADIUS = 5;
  private static final int BASE_ORE_SCAN_TICKS = 700;
  private static final int BASE_MOB_SCAN_TICKS = 200;
  private static final long BASE_ORE_SCAN_COST = 350;
  private static final long BASE_MOB_SCAN_COST = 100;
  private static final int BASE_ORE_FILTER_SLOTS = 1;
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
              new Energy(BASE_CLIMB_EXTRA),
              new Energy(BASE_LAMP_CONSUMPTION)),
          new LightLevel(BASE_LAMP_LIGHT),
          new SolarOutput(new Energy(BASE_SOLAR_DAY), Energy.ZERO),
          new ScannerSpec(
              BASE_SCAN_RADIUS,
              BASE_SCAN_RADIUS,
              BASE_ORE_SCAN_TICKS,
              BASE_MOB_SCAN_TICKS,
              new Energy(BASE_ORE_SCAN_COST),
              new Energy(BASE_MOB_SCAN_COST),
              BASE_ORE_FILTER_SLOTS));

  private final ModConfigSpec spec;
  private final GeneralSection general;
  private final DamagedRobotSection damagedRobot;
  private final EnergySlotSection energySlot;
  private final LoadSection load;
  private final ExpressionSection expression;
  private final NoticeSection notice;
  private final DockSection dock;
  private final LampSection lamp;
  private final SolarSection solar;
  private final ScannerSection scanner;
  private final ChatterSection chatter;
  private final AtomicBoolean warnedRareTags = new AtomicBoolean();
  private final AtomicBoolean warnedLamp = new AtomicBoolean();
  private final TierCatalog tierCatalog;

  public CubitConfig() {
    ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    builder.push(GENERAL_SECTION);
    general = new GeneralSection(builder);
    damagedRobot = new DamagedRobotSection(builder);
    energySlot = new EnergySlotSection(builder);
    load = new LoadSection(builder);
    expression = new ExpressionSection(builder);
    notice = new NoticeSection(builder);
    dock = new DockSection(builder);
    lamp = new LampSection(builder);
    solar = new SolarSection(builder);
    scanner = new ScannerSection(builder);
    chatter = new ChatterSection(builder);
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

  public LightLevel screenLight() {
    return new LightLevel(lamp.screenLight.get());
  }

  /** An invalid tag in the file must not break the game, so it only goes to the log once. */
  public OreSignals oreSignals() {
    List<OreKind> kinds = new ArrayList<>();
    for (String tag : scanner.rareTags.get()) {
      Optional<OreKind> kind = OreKind.parse(tag);
      kind.ifPresent(kinds::add);
      if (kind.isEmpty() && warnedRareTags.compareAndSet(false, true)) {
        LOGGER.warn(INVALID_RARE_TAG, tag);
      }
    }
    return new OreSignals(kinds);
  }

  public int chatterRepeatTicks() {
    return chatter.repeatTicks.get();
  }

  public int chatterLeftBehindBlocks() {
    return chatter.leftBehindBlocks.get();
  }

  public int chatterLeftBehindRepeatTicks() {
    return chatter.leftBehindRepeatTicks.get();
  }

  public int chatterBiomeRepeatTicks() {
    return chatter.biomeRepeatTicks.get();
  }

  public int chatterAnimalRepeatTicks() {
    return chatter.animalRepeatTicks.get();
  }

  public int chatterAnimalChancePercent() {
    return chatter.animalChancePercent.get();
  }

  public int chatterCheckTicks() {
    return chatter.checkTicks.get();
  }

  public int surprisedExpressionTicks() {
    return scanner.surprisedTicks.get();
  }

  public int scanBlocksPerTick() {
    return scanner.blocksPerTick.get();
  }

  public int solarCheckTicks() {
    return solar.checkTicks.get();
  }

  public int lampCheckTicks() {
    return lamp.checkTicks.get();
  }

  /** A reversed pair in the file must not break the game, so it only goes to the log once. */
  public LampPolicy lampPolicy() {
    int onBelow = lamp.onBelow.get();
    int offFrom = lamp.offFrom.get();
    if (offFrom < onBelow && warnedLamp.compareAndSet(false, true)) {
      LOGGER.warn(REVERSED_LAMP, offFrom, onBelow);
    }
    return LampPolicy.fitting(new LightLevel(onBelow), new LightLevel(offFrom));
  }

  public int noticeRepeatTicks() {
    return notice.repeatTicks.get();
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

  public int maxDropBlocks() {
    return general.maxDrop.get();
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
