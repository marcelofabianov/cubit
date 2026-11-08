package io.github.marcelofabianov.cubit.config;

import com.mojang.logging.LogUtils;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.domain.tier.TierCatalog;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;

/** Reads the robot numbers from the config. */
public final class NeoForgeTierCatalog implements TierCatalog {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String UNKNOWN_TYPE_MESSAGE = "Robot type without config: ";
  private static final String MISSING_TIER_MESSAGE = "No config for {} at {}. Using the base tier.";

  private final Map<RobotType, Map<RobotTier, TierSection>> sections;
  private final Set<RobotTier> warnedTiers = ConcurrentHashMap.newKeySet();

  NeoForgeTierCatalog(Map<RobotType, Map<RobotTier, TierSection>> sections) {
    this.sections = sections;
  }

  @Override
  public TierSpec spec(RobotType type, RobotTier tier) {
    Map<RobotTier, TierSection> tiers = sections.get(type);
    if (tiers == null) {
      throw new IllegalArgumentException(UNKNOWN_TYPE_MESSAGE + type.id());
    }
    TierSection section = tiers.get(tier);
    if (section != null) {
      return section.read();
    }
    // A saved robot may hold a tier the config does not have yet. It must not break the world.
    if (warnedTiers.add(tier)) {
      LOGGER.warn(MISSING_TIER_MESSAGE, type.id(), tier);
    }
    return tiers.get(RobotTier.BASE).read();
  }
}
