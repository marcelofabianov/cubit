package io.github.zenndg.cubit.config;

import com.mojang.logging.LogUtils;
import io.github.zenndg.cubit.domain.tier.DeathPointLevel;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.slf4j.Logger;

/**
 * What one tier does with the death point. The key name is a contract, and an invalid value is the
 * safest one, locked, logged once (answer 1129 and rule 17).
 */
final class DeathPointKey {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String KEY = "death_point";
  private static final String INVALID_LEVEL = "Invalid death_point {}, so it is locked";

  private final ModConfigSpec.ConfigValue<String> level;
  private final AtomicBoolean warned = new AtomicBoolean();

  DeathPointKey(ModConfigSpec.Builder builder, DeathPointLevel defaults) {
    level = builder.define(KEY, defaults.key());
  }

  DeathPointLevel read() {
    String key = level.get();
    Optional<DeathPointLevel> read = DeathPointLevel.fromKey(key);
    if (read.isEmpty() && warned.compareAndSet(false, true)) {
      LOGGER.warn(INVALID_LEVEL, key);
    }
    return read.orElse(DeathPointLevel.LOCKED);
  }
}
