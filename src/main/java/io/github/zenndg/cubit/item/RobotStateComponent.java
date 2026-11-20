package io.github.zenndg.cubit.item;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.robot.DefenseMode;
import io.github.zenndg.cubit.domain.robot.OreFilter;
import io.github.zenndg.cubit.domain.robot.OrganizerMode;
import io.github.zenndg.cubit.domain.robot.PickupFilter;
import io.github.zenndg.cubit.domain.robot.PickupSettings;
import io.github.zenndg.cubit.domain.robot.RobotColor;
import io.github.zenndg.cubit.domain.robot.RobotState;
import io.github.zenndg.cubit.domain.robot.SpeedChoice;
import io.github.zenndg.cubit.domain.robot.UnloadInterval;
import io.github.zenndg.cubit.domain.robot.WorkSettings;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.slf4j.Logger;

/**
 * Saves a {@link RobotState} in an item. Decoding never fails, because a failure would destroy the
 * whole item stack, and the robot with it. Invalid values become safe ones instead.
 */
public final class RobotStateComponent {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String TIER_FIELD = "tier";
  private static final String ENERGY_FIELD = "energy";
  private static final String HEALTH_FIELD = "health";
  private static final String LAMP_FIELD = "lamp_enabled";
  private static final boolean LAMP_DEFAULT = true;
  private static final String COLOR_FIELD = "color";
  private static final String SCANNERS_FIELD = "scanners_enabled";
  private static final boolean SCANNERS_DEFAULT = true;
  private static final String ORE_FILTER_FIELD = "ore_filter";
  private static final String CHATTER_FIELD = "chatter_enabled";
  private static final boolean CHATTER_DEFAULT = true;
  private static final String DEFENSE_FIELD = "defense_mode";
  private static final String SOLAR_FIELD = "solar_enabled";
  private static final boolean SOLAR_DEFAULT = true;
  private static final boolean TORCHES_DEFAULT = true;
  private static final String PICKUP_FIELD = "pickup_enabled";
  private static final String MAGNET_FIELD = "magnet_enabled";
  private static final String PICKUP_FILTER_FIELD = "pickup_filter";
  private static final String FETCH_FIELD = "fetch_enabled";
  private static final String TORCHES_FIELD = "torches_enabled";
  private static final String AUTO_RETURN_FIELD = "auto_return_enabled";
  private static final boolean AUTO_RETURN_DEFAULT = true;
  private static final String UNLOAD_FIELD = "unload_enabled";
  private static final boolean UNLOAD_DEFAULT = true;
  private static final String DEATH_POINT_FIELD = "death_point_enabled";
  private static final boolean DEATH_POINT_DEFAULT = true;
  private static final String KEEP_JUNK_FIELD = "keep_junk_enabled";
  private static final boolean KEEP_JUNK_DEFAULT = WorkSettings.INITIAL.keepJunk();
  private static final String UNLOAD_INTERVAL_FIELD = "unload_interval_minutes";
  private static final String SPEED_FIELD = "speed_percent";
  private static final String ORGANIZER_MODE_FIELD = "organizer_mode";
  private static final String UNKNOWN_MODE_MESSAGE = "Unknown Organizer mode {}. Using organize.";
  private static final int LOWEST_SPEED = 1;
  private static final String INVALID_SPEED_MESSAGE = "Invalid speed percentage {}. Using {}.";
  private static final String UNKNOWN_DEFENSE_MESSAGE = "Unknown defense mode {}. Using peaceful.";
  private static final String UNKNOWN_COLOR_MESSAGE = "Unknown robot color {}. Using white.";
  private static final String UNKNOWN_TIER_MESSAGE = "Unknown robot tier {}. Using the base tier.";

  private static final Codec<RobotTier> TIER_CODEC =
      Codec.STRING.xmap(RobotStateComponent::tierFromKey, RobotTier::key);
  private static final Codec<RobotColor> COLOR_CODEC =
      Codec.STRING.xmap(RobotStateComponent::colorFromKey, RobotColor::key);
  private static final Codec<DefenseMode> DEFENSE_CODEC =
      Codec.STRING.xmap(RobotStateComponent::defenseFromKey, DefenseMode::key);
  private static final Codec<Energy> ENERGY_CODEC =
      Codec.LONG.xmap(SavedEnergy::of, Energy::amount);
  private static final Codec<OrganizerMode> MODE_CODEC =
      Codec.STRING.xmap(RobotStateComponent::modeFromKey, OrganizerMode::key);
  private static final Codec<SpeedChoice> SPEED_CODEC =
      Codec.INT.xmap(RobotStateComponent::safeSpeed, SpeedChoice::percent);
  private static final Codec<Float> HEALTH_CODEC =
      Codec.FLOAT.xmap(RobotStateComponent::safeHealth, health -> health);

  private static final Codec<OreFilter> ORE_FILTER_CODEC =
      Codec.STRING.listOf().xmap(SavedOreFilter::of, SavedOreFilter::tagsOf);

  /**
   * The switches added last, in one part of the codec, which holds at most 16 fields. They stay
   * flat in the saved component, so the names are the same as before.
   */
  private record LateSwitches(
      boolean torches,
      boolean autoReturn,
      boolean unload,
      boolean deathPoint,
      boolean keepJunk,
      Optional<Integer> unloadInterval,
      SpeedChoice speed,
      OrganizerMode organizerMode) {}

  private static final MapCodec<LateSwitches> LATE_SWITCHES =
      RecordCodecBuilder.mapCodec(instance ->
          instance
              .group(
                  Codec.BOOL
                      .optionalFieldOf(TORCHES_FIELD, TORCHES_DEFAULT)
                      .forGetter(LateSwitches::torches),
                  Codec.BOOL
                      .optionalFieldOf(AUTO_RETURN_FIELD, AUTO_RETURN_DEFAULT)
                      .forGetter(LateSwitches::autoReturn),
                  Codec.BOOL
                      .optionalFieldOf(UNLOAD_FIELD, UNLOAD_DEFAULT)
                      .forGetter(LateSwitches::unload),
                  Codec.BOOL
                      .optionalFieldOf(DEATH_POINT_FIELD, DEATH_POINT_DEFAULT)
                      .forGetter(LateSwitches::deathPoint),
                  Codec.BOOL
                      .optionalFieldOf(KEEP_JUNK_FIELD, KEEP_JUNK_DEFAULT)
                      .forGetter(LateSwitches::keepJunk),
                  Codec.INT
                      .optionalFieldOf(UNLOAD_INTERVAL_FIELD)
                      .forGetter(LateSwitches::unloadInterval),
                  SPEED_CODEC
                      .optionalFieldOf(SPEED_FIELD, SpeedChoice.FULL)
                      .forGetter(LateSwitches::speed),
                  MODE_CODEC
                      .optionalFieldOf(ORGANIZER_MODE_FIELD, OrganizerMode.INITIAL)
                      .forGetter(LateSwitches::organizerMode))
              .apply(instance, LateSwitches::new));

  public static final Codec<RobotState> CODEC =
      RecordCodecBuilder.create(instance ->
          instance
              .group(
                  TIER_CODEC.fieldOf(TIER_FIELD).forGetter(RobotState::tier),
                  ENERGY_CODEC.fieldOf(ENERGY_FIELD).forGetter(RobotState::energy),
                  HEALTH_CODEC.fieldOf(HEALTH_FIELD).forGetter(RobotState::health),
                  Codec.BOOL
                      .optionalFieldOf(LAMP_FIELD, LAMP_DEFAULT)
                      .forGetter(RobotState::lampOn),
                  COLOR_CODEC
                      .optionalFieldOf(COLOR_FIELD, RobotColor.WHITE)
                      .forGetter(RobotState::color),
                  Codec.BOOL
                      .optionalFieldOf(SCANNERS_FIELD, SCANNERS_DEFAULT)
                      .forGetter(RobotState::scannersOn),
                  ORE_FILTER_CODEC
                      .optionalFieldOf(ORE_FILTER_FIELD, OreFilter.EMPTY)
                      .forGetter(RobotState::oreFilter),
                  Codec.BOOL
                      .optionalFieldOf(CHATTER_FIELD, CHATTER_DEFAULT)
                      .forGetter(RobotState::chatterOn),
                  DEFENSE_CODEC
                      .optionalFieldOf(DEFENSE_FIELD, DefenseMode.INITIAL)
                      .forGetter(RobotState::defenseMode),
                  Codec.BOOL
                      .optionalFieldOf(SOLAR_FIELD, SOLAR_DEFAULT)
                      .forGetter(RobotState::solarOn),
                  Codec.BOOL
                      .optionalFieldOf(PICKUP_FIELD, PickupSettings.INITIAL.pickupOn())
                      .forGetter(state -> state.pickup().pickupOn()),
                  Codec.BOOL
                      .optionalFieldOf(MAGNET_FIELD, PickupSettings.INITIAL.magnetOn())
                      .forGetter(state -> state.pickup().magnetOn()),
                  SavedPickupFilter.CODEC
                      .optionalFieldOf(PICKUP_FILTER_FIELD, PickupFilter.EMPTY)
                      .forGetter(state -> state.pickup().filter()),
                  Codec.BOOL
                      .optionalFieldOf(FETCH_FIELD, PickupSettings.INITIAL.fetchOn())
                      .forGetter(state -> state.pickup().fetchOn()),
                  LATE_SWITCHES.forGetter(state ->
                      new LateSwitches(
                          state.torchesOn(),
                          state.autoReturnOn(),
                          state.unloadOn(),
                          state.deathPointOn(),
                          state.workSettings().keepJunk(),
                          savedInterval(state.workSettings().unloadInterval()),
                          state.speed(),
                          state.workSettings().organizerMode())))
              .apply(
                  instance,
                  (tier,
                      energy,
                      health,
                      lamp,
                      color,
                      scanners,
                      oreFilter,
                      chatter,
                      defense,
                      solar,
                      pickup,
                      magnet,
                      pickupFilter,
                      fetch,
                      late) ->
                      new RobotState(
                          tier,
                          energy,
                          health,
                          lamp,
                          color,
                          scanners,
                          oreFilter,
                          chatter,
                          defense,
                          solar,
                          new PickupSettings(pickup, magnet, fetch, pickupFilter),
                          late.torches(),
                          late.autoReturn(),
                          late.unload(),
                          late.deathPoint(),
                          new WorkSettings(
                              late.keepJunk(),
                              intervalOf(late.unloadInterval()),
                              late.organizerMode()),
                          late.speed())));

  public static final StreamCodec<ByteBuf, RobotState> STREAM_CODEC =
      ByteBufCodecs.fromCodec(CODEC);

  private RobotStateComponent() {}

  private static RobotTier tierFromKey(String key) {
    return RobotTier.fromKey(key)
        .orElseGet(() -> {
          LOGGER.warn(UNKNOWN_TIER_MESSAGE, key);
          return RobotTier.BASE;
        });
  }

  private static RobotColor colorFromKey(String key) {
    return RobotColor.fromKey(key)
        .orElseGet(() -> {
          LOGGER.warn(UNKNOWN_COLOR_MESSAGE, key);
          return RobotColor.WHITE;
        });
  }

  private static OrganizerMode modeFromKey(String key) {
    return OrganizerMode.fromKey(key)
        .orElseGet(() -> {
          LOGGER.warn(UNKNOWN_MODE_MESSAGE, key);
          return OrganizerMode.INITIAL;
        });
  }

  private static DefenseMode defenseFromKey(String key) {
    return DefenseMode.fromKey(key)
        .orElseGet(() -> {
          LOGGER.warn(UNKNOWN_DEFENSE_MESSAGE, key);
          return DefenseMode.INITIAL;
        });
  }

  /** Only a chosen interval is saved, so one nobody chose keeps following the config (1254). */
  private static Optional<Integer> savedInterval(UnloadInterval interval) {
    return interval.chosen() ? Optional.of(interval.minutes()) : Optional.empty();
  }

  /** A negative number in the save is no choice at all, and the config holds (rule 17). */
  private static UnloadInterval intervalOf(Optional<Integer> saved) {
    return saved
        .filter(minutes -> minutes >= 0)
        .map(UnloadInterval::new)
        .orElse(UnloadInterval.CONFIG_DEFAULT);
  }

  /** Out of the range, the nearest end holds, and the config fits it when placed (rule 17). */
  private static SpeedChoice safeSpeed(int percent) {
    int safe = Math.clamp(percent, LOWEST_SPEED, SpeedChoice.MAX_PERCENT);
    if (safe != percent) {
      LOGGER.warn(INVALID_SPEED_MESSAGE, percent, safe);
    }
    return new SpeedChoice(safe);
  }

  private static float safeHealth(float health) {
    return Float.isNaN(health) ? 0 : Math.max(health, 0);
  }
}
