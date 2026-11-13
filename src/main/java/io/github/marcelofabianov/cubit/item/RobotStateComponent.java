package io.github.marcelofabianov.cubit.item;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.domain.robot.OreFilter;
import io.github.marcelofabianov.cubit.domain.robot.PickupFilter;
import io.github.marcelofabianov.cubit.domain.robot.PickupSettings;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.netty.buffer.ByteBuf;
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
  private static final Codec<Float> HEALTH_CODEC =
      Codec.FLOAT.xmap(RobotStateComponent::safeHealth, health -> health);

  private static final Codec<OreFilter> ORE_FILTER_CODEC =
      Codec.STRING.listOf().xmap(SavedOreFilter::of, SavedOreFilter::tagsOf);

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
                  Codec.BOOL
                      .optionalFieldOf(TORCHES_FIELD, TORCHES_DEFAULT)
                      .forGetter(RobotState::torchesOn))
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
                      torches) ->
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
                          torches)));

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

  private static DefenseMode defenseFromKey(String key) {
    return DefenseMode.fromKey(key)
        .orElseGet(() -> {
          LOGGER.warn(UNKNOWN_DEFENSE_MESSAGE, key);
          return DefenseMode.INITIAL;
        });
  }

  private static float safeHealth(float health) {
    return Float.isNaN(health) ? 0 : Math.max(health, 0);
  }
}
