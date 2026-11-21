package io.github.zenndg.cubit.config;

import com.mojang.logging.LogUtils;
import io.github.zenndg.cubit.domain.desk.DeskCatalog;
import io.github.zenndg.cubit.domain.desk.DeskSpec;
import io.github.zenndg.cubit.domain.dock.DockCatalog;
import io.github.zenndg.cubit.domain.dock.DockSpec;
import io.github.zenndg.cubit.domain.robot.RobotType;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.domain.tier.TierCatalog;
import io.github.zenndg.cubit.domain.tier.TierSpec;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;

/**
 * Reads the robot numbers, and the ones of the Induction Dock and of the Engineering Desk of each
 * tier, from the config.
 */
public final class NeoForgeTierCatalog implements TierCatalog, DockCatalog, DeskCatalog {
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

  /** The dock keys live in the sections of the Cubit, so a dock follows the tiers of the robot. */
  @Override
  public DockSpec dock(RobotTier tier) {
    Map<RobotTier, TierSection> tiers = sections.get(CubitConfig.GENERAL_ROBOT);
    TierSection section = tiers.getOrDefault(tier, tiers.get(RobotTier.BASE));
    return section.readDock();
  }

  /** The desk keys also live in the sections of the Cubit, so a desk follows the same tiers. */
  @Override
  public DeskSpec desk(RobotTier tier) {
    Map<RobotTier, TierSection> tiers = sections.get(CubitConfig.GENERAL_ROBOT);
    TierSection section = tiers.getOrDefault(tier, tiers.get(RobotTier.BASE));
    return section.readDesk();
  }
}
