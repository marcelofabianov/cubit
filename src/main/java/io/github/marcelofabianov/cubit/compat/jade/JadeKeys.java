package io.github.marcelofabianov.cubit.compat.jade;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import java.util.Locale;
import net.minecraft.resources.ResourceLocation;

/**
 * The names the Jade plugin shows and saves. It imports nothing of Jade, so the data generation
 * reads it without Jade, and only the plugin classes ever load Jade (answer 667).
 */
public final class JadeKeys {
  public static final String TIER = "jade.cubit.tier";
  public static final String BATTERY = "jade.cubit.battery";
  public static final String DOCK_ENERGY = "jade.cubit.dock_energy";
  private static final String TIER_NAME = "jade.cubit.tier.%s";
  private static final String STATE = "jade.cubit.state.%s";
  private static final String DOCK_STATUS = "jade.cubit.dock_status.%s";
  private static final String CONFIG = "config.jade.plugin_%s.%s";
  private static final String ROBOT_PATH = "robot";
  private static final String DOCK_PATH = "induction_dock";
  private static final String OUT_OF_BATTERY = "out_of_battery";
  private static final String SWITCHED_OFF = "switched_off";

  public static final ResourceLocation ROBOT_UID =
      ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, ROBOT_PATH);
  public static final ResourceLocation DOCK_UID =
      ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, DOCK_PATH);

  private JadeKeys() {}

  public static String tierName(RobotTier tier) {
    return TIER_NAME.formatted(tier.key());
  }

  /** The two inactive states read apart, because the player acts differently on each. */
  public static String state(PowerState state) {
    String name =
        switch (state) {
          case INACTIVE_EXHAUSTED -> OUT_OF_BATTERY;
          case INACTIVE_SWITCHED_OFF -> SWITCHED_OFF;
          case ACTIVE, HIBERNATING -> state.name().toLowerCase(Locale.ROOT);
        };
    return STATE.formatted(name);
  }

  public static String dockStatus(String serializedName) {
    return DOCK_STATUS.formatted(serializedName);
  }

  public static String config(ResourceLocation uid) {
    return CONFIG.formatted(uid.getNamespace(), uid.getPath());
  }
}
