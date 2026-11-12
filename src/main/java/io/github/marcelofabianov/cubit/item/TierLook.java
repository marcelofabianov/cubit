package io.github.marcelofabianov.cubit.item;

import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/** How a tier looks outside the robot: the color of its collar and its name in the texts. */
public final class TierLook {
  private static final int BASE_COLOR = 0x9AA0A6;
  private static final int ADVANCED_COLOR = 0x3B82F6;
  private static final int ELITE_COLOR = 0x8B5CF6;
  private static final int ULTIMATE_COLOR = 0xF2B705;
  private static final String NAME_KEY = "tier.cubit.%s";

  private TierLook() {}

  /** The colors of the collar pictures in textures/entity/general_robot_collar. */
  public static int color(RobotTier tier) {
    return switch (tier) {
      case BASE -> BASE_COLOR;
      case ADVANCED -> ADVANCED_COLOR;
      case ELITE -> ELITE_COLOR;
      case ULTIMATE -> ULTIMATE_COLOR;
    };
  }

  public static String nameKey(RobotTier tier) {
    return NAME_KEY.formatted(tier.key());
  }

  public static Component name(RobotTier tier) {
    return Component.translatable(nameKey(tier));
  }

  /** The tier of a robot item or a damaged one. Without a saved state, it is a new robot. */
  public static RobotTier tierOf(ItemStack stack) {
    RobotState saved = stack.get(CubitDataComponents.ROBOT_STATE.get());
    return saved == null ? RobotTier.BASE : saved.tier();
  }
}
