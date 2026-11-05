package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/** Slows a robot with a full inventory. On fire it keeps its speed, because fleeing comes first. */
final class RobotLoadSpeed {
  private static final String MODIFIER_PATH = "full_inventory_speed";
  private static final ResourceLocation MODIFIER_ID =
      ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, MODIFIER_PATH);

  private RobotLoadSpeed() {}

  /** Not saved with the robot: the next tick applies it again from the inventory. */
  static void update(RobotEntity robot, boolean fullInventory) {
    AttributeInstance speed = robot.getAttribute(Attributes.MOVEMENT_SPEED);
    boolean slowed = fullInventory && !robot.isOnFire() && !robot.isInLava();
    if (slowed == speed.hasModifier(MODIFIER_ID)) {
      return;
    }
    if (slowed) {
      speed.addTransientModifier(
          new AttributeModifier(
              MODIFIER_ID,
              CubitMod.CONFIG.fullInventorySpeedMultiplier() - 1,
              AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    } else {
      speed.removeModifier(MODIFIER_ID);
    }
  }
}
