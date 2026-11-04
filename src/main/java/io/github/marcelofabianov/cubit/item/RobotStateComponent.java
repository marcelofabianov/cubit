package io.github.marcelofabianov.cubit.item;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
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
  private static final String UNKNOWN_TIER_MESSAGE = "Unknown robot tier {}. Using the base tier.";

  private static final Codec<RobotTier> TIER_CODEC =
      Codec.STRING.xmap(RobotStateComponent::tierFromKey, RobotTier::key);
  private static final Codec<Energy> ENERGY_CODEC =
      Codec.LONG.xmap(amount -> new Energy(Math.max(amount, 0)), Energy::amount);
  private static final Codec<Float> HEALTH_CODEC =
      Codec.FLOAT.xmap(RobotStateComponent::safeHealth, health -> health);

  public static final Codec<RobotState> CODEC =
      RecordCodecBuilder.create(instance ->
          instance
              .group(
                  TIER_CODEC.fieldOf(TIER_FIELD).forGetter(RobotState::tier),
                  ENERGY_CODEC.fieldOf(ENERGY_FIELD).forGetter(RobotState::energy),
                  HEALTH_CODEC.fieldOf(HEALTH_FIELD).forGetter(RobotState::health))
              .apply(instance, RobotState::new));

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

  private static float safeHealth(float health) {
    return Float.isNaN(health) ? 0 : Math.max(health, 0);
  }
}
