package io.github.zenndg.cubit.registry;

import com.mojang.serialization.Codec;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/** The tier of a dock by its key, with an unknown key read as Base (rule 17 and answer 1373). */
final class DockTierCodec {
  static final Codec<RobotTier> CODEC = Codec.STRING.xmap(DockTierCodec::fromKey, RobotTier::key);
  static final StreamCodec<ByteBuf, RobotTier> STREAM_CODEC =
      ByteBufCodecs.STRING_UTF8.map(DockTierCodec::fromKey, RobotTier::key);

  private DockTierCodec() {}

  private static RobotTier fromKey(String key) {
    return RobotTier.fromKey(key).orElse(RobotTier.BASE);
  }
}
