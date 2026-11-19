package io.github.zenndg.cubit.domain.robot;

import io.github.zenndg.cubit.domain.energy.Battery;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.energy.Percentage;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.domain.tier.TierSpec;

/**
 * What a robot keeps when it becomes an item. The config may change while a robot is stored, so the
 * numbers are only valid after being fitted to a {@link TierSpec}.
 */
public record RobotState(
    RobotTier tier,
    Energy energy,
    float health,
    boolean lampOn,
    RobotColor color,
    boolean scannersOn,
    OreFilter oreFilter,
    boolean chatterOn,
    DefenseMode defenseMode,
    boolean solarOn,
    PickupSettings pickup,
    boolean torchesOn,
    boolean autoReturnOn,
    boolean unloadOn,
    boolean deathPointOn,
    WorkSettings workSettings,
    SpeedChoice speed) {
  private static final String INVALID_HEALTH_MESSAGE = "Invalid robot health: ";
  private static final float MIN_PLACED_HEALTH = 1f;

  public RobotState {
    if (Float.isNaN(health) || health < 0) {
      throw new IllegalArgumentException(INVALID_HEALTH_MESSAGE + health);
    }
  }

  /**
   * The switches that came later start on, the ones of the jobs as at first (1191), and the speed
   * whole (1266), also for what was saved before they existed.
   */
  public RobotState(
      RobotTier tier,
      Energy energy,
      float health,
      boolean lampOn,
      RobotColor color,
      boolean scannersOn,
      OreFilter oreFilter,
      boolean chatterOn,
      DefenseMode defenseMode,
      boolean solarOn,
      PickupSettings pickup,
      boolean torchesOn) {
    this(
        tier,
        energy,
        health,
        lampOn,
        color,
        scannersOn,
        oreFilter,
        chatterOn,
        defenseMode,
        solarOn,
        pickup,
        torchesOn,
        true,
        true,
        true,
        WorkSettings.INITIAL,
        SpeedChoice.FULL);
  }

  /** Pickup starts with its initial settings, also for what was saved before it existed. */
  public RobotState(
      RobotTier tier,
      Energy energy,
      float health,
      boolean lampOn,
      RobotColor color,
      boolean scannersOn,
      OreFilter oreFilter,
      boolean chatterOn,
      DefenseMode defenseMode,
      boolean solarOn) {
    this(
        tier,
        energy,
        health,
        lampOn,
        color,
        scannersOn,
        oreFilter,
        chatterOn,
        defenseMode,
        solarOn,
        PickupSettings.INITIAL,
        true);
  }

  /** The solar panel starts switched on, also for what was saved before its switch existed. */
  public RobotState(
      RobotTier tier,
      Energy energy,
      float health,
      boolean lampOn,
      RobotColor color,
      boolean scannersOn,
      OreFilter oreFilter,
      boolean chatterOn,
      DefenseMode defenseMode) {
    this(tier, energy, health, lampOn, color, scannersOn, oreFilter, chatterOn, defenseMode, true);
  }

  /** The robot starts peaceful, also for what was saved before the modes existed. */
  public RobotState(
      RobotTier tier,
      Energy energy,
      float health,
      boolean lampOn,
      RobotColor color,
      boolean scannersOn,
      OreFilter oreFilter,
      boolean chatterOn) {
    this(
        tier, energy, health, lampOn, color, scannersOn, oreFilter, chatterOn, DefenseMode.INITIAL);
  }

  /** The lamp starts switched on, also for what was saved before the lamp existed. */
  public RobotState(RobotTier tier, Energy energy, float health) {
    this(tier, energy, health, true);
  }

  /** The shell starts white, also for what was saved before the colors existed. */
  public RobotState(RobotTier tier, Energy energy, float health, boolean lampOn) {
    this(tier, energy, health, lampOn, RobotColor.WHITE);
  }

  /** The scanners start on and looking for nothing in particular, also for older saves. */
  public RobotState(RobotTier tier, Energy energy, float health, boolean lampOn, RobotColor color) {
    this(tier, energy, health, lampOn, color, true, OreFilter.EMPTY);
  }

  /** The chatter starts on, also for what was saved before it existed. */
  public RobotState(
      RobotTier tier,
      Energy energy,
      float health,
      boolean lampOn,
      RobotColor color,
      boolean scannersOn,
      OreFilter oreFilter) {
    this(tier, energy, health, lampOn, color, scannersOn, oreFilter, true);
  }

  public static RobotState initial(RobotTier tier, TierSpec spec, Percentage initialCharge) {
    return new RobotState(tier, spec.newBattery(initialCharge).charge(), spec.maxHealth());
  }

  /** What a robot keeps when it dies: the tier and the looks, without energy or health. */
  public RobotState damaged() {
    return new RobotState(
        tier,
        Energy.ZERO,
        0f,
        lampOn,
        color,
        scannersOn,
        oreFilter,
        chatterOn,
        defenseMode,
        solarOn,
        pickup,
        torchesOn,
        autoReturnOn,
        unloadOn,
        deathPointOn,
        workSettings,
        speed);
  }

  public RobotState repaired(TierSpec spec, Percentage health) {
    return new RobotState(
        tier,
        Energy.ZERO,
        (float) spec.maxHealth() * health.value() / Percentage.MAX,
        lampOn,
        color,
        scannersOn,
        oreFilter,
        chatterOn,
        defenseMode,
        solarOn,
        pickup,
        torchesOn,
        autoReturnOn,
        unloadOn,
        deathPointOn,
        workSettings,
        speed);
  }

  /**
   * The same robot with another charge, as the recharge in the crafting grid gives (answer 181).
   */
  public RobotState withEnergy(Energy next) {
    return new RobotState(
        tier,
        next,
        health,
        lampOn,
        color,
        scannersOn,
        oreFilter,
        chatterOn,
        defenseMode,
        solarOn,
        pickup,
        torchesOn,
        autoReturnOn,
        unloadOn,
        deathPointOn,
        workSettings,
        speed);
  }

  public RobotState withPickup(PickupSettings next) {
    return new RobotState(
        tier,
        energy,
        health,
        lampOn,
        color,
        scannersOn,
        oreFilter,
        chatterOn,
        defenseMode,
        solarOn,
        next,
        torchesOn,
        autoReturnOn,
        unloadOn,
        deathPointOn,
        workSettings,
        speed);
  }

  public RobotState withTorches(boolean on) {
    return new RobotState(
        tier,
        energy,
        health,
        lampOn,
        color,
        scannersOn,
        oreFilter,
        chatterOn,
        defenseMode,
        solarOn,
        pickup,
        on,
        autoReturnOn,
        unloadOn,
        deathPointOn,
        workSettings,
        speed);
  }

  public RobotState withAutoReturn(boolean on) {
    return new RobotState(
        tier,
        energy,
        health,
        lampOn,
        color,
        scannersOn,
        oreFilter,
        chatterOn,
        defenseMode,
        solarOn,
        pickup,
        torchesOn,
        on,
        unloadOn,
        deathPointOn,
        workSettings,
        speed);
  }

  public RobotState withUnload(boolean on) {
    return new RobotState(
        tier,
        energy,
        health,
        lampOn,
        color,
        scannersOn,
        oreFilter,
        chatterOn,
        defenseMode,
        solarOn,
        pickup,
        torchesOn,
        autoReturnOn,
        on,
        deathPointOn,
        workSettings,
        speed);
  }

  public RobotState withDeathPoint(boolean on) {
    return new RobotState(
        tier,
        energy,
        health,
        lampOn,
        color,
        scannersOn,
        oreFilter,
        chatterOn,
        defenseMode,
        solarOn,
        pickup,
        torchesOn,
        autoReturnOn,
        unloadOn,
        on,
        workSettings,
        speed);
  }

  public RobotState withWorkSettings(WorkSettings on) {
    return new RobotState(
        tier,
        energy,
        health,
        lampOn,
        color,
        scannersOn,
        oreFilter,
        chatterOn,
        defenseMode,
        solarOn,
        pickup,
        torchesOn,
        autoReturnOn,
        unloadOn,
        deathPointOn,
        on,
        speed);
  }

  /** The same robot walking at another percentage of its tier (answer 1266). */
  public RobotState withSpeed(SpeedChoice next) {
    return new RobotState(
        tier,
        energy,
        health,
        lampOn,
        color,
        scannersOn,
        oreFilter,
        chatterOn,
        defenseMode,
        solarOn,
        pickup,
        torchesOn,
        autoReturnOn,
        unloadOn,
        deathPointOn,
        workSettings,
        next);
  }

  public Battery batteryFor(TierSpec spec) {
    return new Battery(spec.batteryCapacity(), energy.min(spec.batteryCapacity()));
  }

  /** Never below the minimum: a robot placed without health would die immediately. */
  public float healthFor(TierSpec spec) {
    return Math.clamp(health, MIN_PLACED_HEALTH, (float) spec.maxHealth());
  }
}
