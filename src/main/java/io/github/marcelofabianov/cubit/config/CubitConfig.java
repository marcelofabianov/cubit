package io.github.marcelofabianov.cubit.config;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.domain.tier.TierCatalog;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.neoforged.neoforge.common.ModConfigSpec;

/** The COMMON config. The keys and limits are documented in _docs/desenvolvimento/config.md. */
public final class CubitConfig {
  private static final RobotType GENERAL_ROBOT = new RobotType("general_robot");
  private static final String GENERAL_SECTION = "general";
  private static final String INITIAL_CHARGE = "initial_charge_percent";
  private static final int INITIAL_CHARGE_DEFAULT = 25;
  private static final int PERCENT_MIN = 0;
  private static final int PERCENT_MAX = 100;
  private static final int TIER_SECTION_DEPTH = 2;
  private static final long BASE_BATTERY = 300_000;
  private static final long BASE_CONSUMPTION = 10;
  private static final long BASE_RECHARGE = 500;
  private static final int BASE_SLOTS = 27;
  private static final int BASE_HEALTH = 25;
  private static final int BASE_CALL_RADIUS = 15;
  private static final TierSpec GENERAL_ROBOT_BASE =
      new TierSpec(
          new Energy(BASE_BATTERY),
          new Energy(BASE_CONSUMPTION),
          new Energy(BASE_RECHARGE),
          BASE_SLOTS,
          BASE_HEALTH,
          BASE_CALL_RADIUS);

  private final ModConfigSpec spec;
  private final ModConfigSpec.IntValue initialCharge;
  private final TierCatalog tierCatalog;

  public CubitConfig() {
    ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    builder.push(GENERAL_SECTION);
    initialCharge =
        builder.defineInRange(INITIAL_CHARGE, INITIAL_CHARGE_DEFAULT, PERCENT_MIN, PERCENT_MAX);
    builder.pop();

    builder.push(List.of(GENERAL_ROBOT.id(), tierKey(RobotTier.BASE)));
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
    return new Percentage(initialCharge.get());
  }

  private static String tierKey(RobotTier tier) {
    return tier.name().toLowerCase(Locale.ROOT);
  }
}
