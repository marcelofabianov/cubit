package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import java.util.Optional;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

/**
 * Tells Focus which mob the owner hurt, and tells Retaliate who hurt the robot or its owner. For
 * the owner, the robot that follows is the one that remembers, because it is the one by the owner's
 * side (answer 832).
 */
@EventBusSubscriber(modid = CubitMod.MOD_ID)
public final class DefenseWatch {
  private DefenseWatch() {}

  @SubscribeEvent
  public static void onDamage(LivingDamageEvent.Post event) {
    if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
      return;
    }
    if (attacker instanceof ServerPlayer owner && event.getEntity() != owner) {
      followerOf(owner).ifPresent(robot -> robot.defense().ownerHurt(event.getEntity()));
    }
    if (event.getEntity() instanceof RobotEntity robot) {
      robot.defense().hurtBy(attacker);
    } else if (event.getEntity() instanceof ServerPlayer owner && attacker != owner) {
      followerOf(owner).ifPresent(robot -> robot.defense().hurtBy(attacker));
    }
  }

  private static Optional<RobotEntity> followerOf(ServerPlayer owner) {
    return RobotEntity.follower(owner)
        .map(owner.serverLevel()::getEntity)
        .filter(RobotEntity.class::isInstance)
        .map(RobotEntity.class::cast);
  }
}
