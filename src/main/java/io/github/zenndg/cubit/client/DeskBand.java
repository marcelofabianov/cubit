package io.github.zenndg.cubit.client;

import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.item.TierLook;
import net.minecraft.util.FastColor;
import software.bernie.geckolib.cache.object.GeoBone;

/**
 * The band on the edge of the desk takes the color of the tier: grey, blue, purple, gold (1439).
 */
final class DeskBand {
  static final String BONE = "tier_band";

  private DeskBand() {}

  static int colour(GeoBone bone, RobotTier tier, int colour) {
    return BONE.equals(bone.getName())
        ? FastColor.ARGB32.multiply(colour, FastColor.ARGB32.opaque(TierLook.color(tier)))
        : colour;
  }
}
