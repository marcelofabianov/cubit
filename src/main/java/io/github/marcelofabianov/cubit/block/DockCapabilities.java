package io.github.marcelofabianov.cubit.block;

import io.github.marcelofabianov.cubit.CubitMod;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.EntityCapability;

/** The capabilities through which a dock reads what it does not own. */
public final class DockCapabilities {
  private static final String DOCKED_ROBOT_ID = "docked_robot";

  public static final EntityCapability<DockedRobot, Void> DOCKED_ROBOT =
      EntityCapability.createVoid(
          ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, DOCKED_ROBOT_ID),
          DockedRobot.class);

  private DockCapabilities() {}
}
