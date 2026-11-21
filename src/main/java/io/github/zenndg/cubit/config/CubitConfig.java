package io.github.zenndg.cubit.config;

import com.mojang.logging.LogUtils;
import io.github.zenndg.cubit.domain.behavior.DeathGuideSpec;
import io.github.zenndg.cubit.domain.behavior.FishingTimes;
import io.github.zenndg.cubit.domain.behavior.FollowDecision;
import io.github.zenndg.cubit.domain.behavior.LampPolicy;
import io.github.zenndg.cubit.domain.behavior.LightLevel;
import io.github.zenndg.cubit.domain.dock.DockCatalog;
import io.github.zenndg.cubit.domain.energy.Percentage;
import io.github.zenndg.cubit.domain.energy.PowerRules;
import io.github.zenndg.cubit.domain.robot.OreKind;
import io.github.zenndg.cubit.domain.robot.OreSignals;
import io.github.zenndg.cubit.domain.robot.RobotType;
import io.github.zenndg.cubit.domain.robot.SpeedSteps;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.domain.tier.TierCatalog;
import java.util.ArrayList;
import java.util.EnumMap;
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
  private static final String REVERSED_FISHING = "Fishing limits are reversed; using the minimum";
  private static final String INVALID_RARE_TAG = "Ignoring an invalid tag in rare_ore_tags: {}";
  private static final String GENERAL_SECTION = "general";
  private static final int TIER_SECTION_DEPTH = 2;
  private static final int TICKS_PER_SECOND = 20;
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
  private final DefenseSection defense;
  private final PickupSection pickup;
  private final TorchSection torches;
  private final DeathPointSection deathPoint;
  private final FisherSection fisher;
  private final UnloadSection unload;
  private final SpeedControlSection speedControl;
  private final DoorSection doors;
  private final AtomicBoolean warnedRareTags = new AtomicBoolean();
  private final AtomicBoolean warnedLamp = new AtomicBoolean();
  private final AtomicBoolean warnedFishing = new AtomicBoolean();
  private final NeoForgeTierCatalog tierCatalog;

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
    defense = new DefenseSection(builder);
    pickup = new PickupSection(builder);
    torches = new TorchSection(builder);
    deathPoint = new DeathPointSection(builder);
    fisher = new FisherSection(builder);
    unload = new UnloadSection(builder);
    speedControl = new SpeedControlSection(builder);
    doors = new DoorSection(builder);
    builder.pop();

    tierCatalog = new NeoForgeTierCatalog(Map.of(GENERAL_ROBOT, tierSections(builder)));
    spec = builder.build();
  }

  /** In the order of the tiers, so the file keeps the base section first, as it always had. */
  private static Map<RobotTier, TierSection> tierSections(ModConfigSpec.Builder builder) {
    Map<RobotTier, TierSection> sections = new EnumMap<>(RobotTier.class);
    for (RobotTier tier : RobotTier.values()) {
      builder.push(List.of(GENERAL_ROBOT.id(), tier.key()));
      sections.put(tier, new TierSection(builder, TierDefaults.of(tier), DockDefaults.of(tier)));
      builder.pop(TIER_SECTION_DEPTH);
    }
    return sections;
  }

  public ModConfigSpec spec() {
    return spec;
  }

  public int retaliateMemoryTicks() {
    return defense.retaliateMemory.get();
  }

  public int shockLookAfterTicks() {
    return defense.shockLook.get();
  }

  public int focusForgetDistanceBlocks() {
    return defense.focusForget.get();
  }

  public int tabletTargetReachBlocks() {
    return defense.tabletTargetReach.get();
  }

  public int deathLogSize() {
    return deathPoint.logSize.get();
  }

  public DeathGuideSpec deathGuideSpec() {
    return new DeathGuideSpec(
        deathPoint.arrival.get(),
        deathPoint.lead.get(),
        deathPoint.stuckSeconds.get() * TICKS_PER_SECOND);
  }

  public int torchCheckTicks() {
    return torches.check.get();
  }

  public int torchScanRadiusBlocks() {
    return torches.scanRadius.get();
  }

  public int torchReachBlocks() {
    return torches.reach.get();
  }

  public int pickupCheckTicks() {
    return pickup.pickupCheck.get();
  }

  public int magnetCheckTicks() {
    return pickup.magnetCheck.get();
  }

  public int fetchCheckTicks() {
    return pickup.fetchCheck.get();
  }

  public int fetchGiveUpTicks() {
    return pickup.fetchGiveUp.get();
  }

  public int fetchIgnoreTicks() {
    return pickup.fetchIgnore.get();
  }

  public TierCatalog tierCatalog() {
    return tierCatalog;
  }

  /** The numbers of the Induction Dock at each tier (answer 1374). */
  public DockCatalog dockCatalog() {
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

  /** The keys of the chatter. */
  public ChatterSection chatter() {
    return chatter;
  }

  public int surprisedExpressionTicks() {
    return scanner.surprisedTicks.get();
  }

  public int scanMarkTicks() {
    return scanner.markTicks.get();
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

  /** A maximum under its minimum must not break the game: it becomes the minimum, logged once. */
  public FishingTimes fishingTimes() {
    int waitMin = fisher.waitMin.get();
    int biteMin = fisher.biteMin.get();
    int waitMax = fisher.waitMax.get();
    int biteMax = fisher.biteMax.get();
    if ((waitMax < waitMin || biteMax < biteMin) && warnedFishing.compareAndSet(false, true)) {
      LOGGER.warn(REVERSED_FISHING);
    }
    return new FishingTimes(
        waitMin,
        Math.max(waitMin, waitMax),
        biteMin,
        Math.max(biteMin, biteMax),
        fisher.lureTicks.get());
  }

  public int fisherCastReachBlocks() {
    return fisher.castReach.get();
  }

  public int unloadIntervalMaxMinutes() {
    return unload.maxMinutes.get();
  }

  /** A default over the max would never show in the Work tab, so it is kept under it. */
  public int unloadIntervalDefaultMinutes() {
    return Math.min(unload.defaultMinutes.get(), unloadIntervalMaxMinutes());
  }

  /** The steps of the speed buttons, valid by the ranges of the two keys (answer 1267). */
  public SpeedSteps speedSteps() {
    return new SpeedSteps(speedControl.minPercent.get(), speedControl.stepPercent.get());
  }

  public int farmerScanIntervalTicks() {
    return fisher.farmerScan.get();
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

  /** The charge a robot needs to wake, also for the recharge in the crafting grid (answer 181). */
  public Percentage wakeCharge() {
    return new Percentage(general.wakeCharge.get());
  }

  public PowerRules powerRules() {
    return new PowerRules(wakeCharge());
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

  /** The keys of the Induction Dock. */
  public DockSection dock() {
    return dock;
  }

  /** The keys of the marked doors (answers 1368 and 1369). */
  public DoorSection doors() {
    return doors;
  }

  public Percentage repairHealth() {
    return new Percentage(damagedRobot.repairHealth.get());
  }

  public int voidRescueRetryTicks() {
    return damagedRobot.voidRescueRetry.get();
  }
}
