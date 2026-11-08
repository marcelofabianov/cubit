package io.github.marcelofabianov.cubit.client;

import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import dev.lambdaurora.lambdynlights.api.entity.luminance.EntityLuminance;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSourceManager;
import io.github.marcelofabianov.cubit.CubitMod;
import net.minecraft.resources.ResourceLocation;

/** Named only as text in the mod metadata, so a dedicated server never loads this class. */
public final class CubitDynamicLights implements DynamicLightsInitializer {
  private static final String LUMINANCE_NAME = "robot";
  private static final String REMOVAL = "removal";

  static final EntityLuminance.Type ROBOT_LUMINANCE =
      EntityLuminance.Type.registerSimple(
          ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, LUMINANCE_NAME),
          RobotLuminance.INSTANCE);

  /** Version 4.8.11 still makes this method abstract, though it is marked for removal. */
  @SuppressWarnings(REMOVAL)
  @Override
  public void onInitializeDynamicLights(ItemLightSourceManager itemLightSources) {}
}
