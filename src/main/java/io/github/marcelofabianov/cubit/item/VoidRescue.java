package io.github.marcelofabianov.cubit.item;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.VoidRescueTarget;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

/** Brings a damaged robot back from the void. The domain chooses where, and this moves it. */
final class VoidRescue {
  private static final float NO_ROTATION = 0f;
  private static final int BELOW_THE_WORLD = 1;

  private VoidRescue() {}

  /** Returns true when the item must skip the rest of its tick, to wait where it is. */
  static boolean rescue(ServerLevel level, ItemEntity entity, ItemStack stack) {
    if (entity.isNoGravity() && level.getGameTime() % CubitMod.CONFIG.voidRescueRetryTicks() != 0) {
      return true;
    }
    MinecraftServer server = level.getServer();
    GlobalPos safe = stack.get(CubitDataComponents.LAST_SAFE_POSITION.get());
    ServerPlayer owner = onlineOwner(server, stack);
    ServerLevel overworld = server.overworld();
    BlockPos spawn = overworld.getSharedSpawnPos();
    VoidRescueTarget target =
        VoidRescueTarget.decide(usable(level, safe), owner != null, overworld.isLoaded(spawn));
    return switch (target) {
      case SAFE_POSITION -> moveInside(entity, safe.pos());
      case OWNER -> deliver(entity, stack, owner);
      case WORLD_SPAWN -> sendToSpawn(level, entity, overworld, spawn);
      case WAIT -> waitBelowTheWorld(level, entity);
    };
  }

  private static boolean usable(ServerLevel level, GlobalPos safe) {
    return safe != null && safe.dimension() == level.dimension() && level.isLoaded(safe.pos());
  }

  private static ServerPlayer onlineOwner(MinecraftServer server, ItemStack stack) {
    UUID owner = stack.get(CubitDataComponents.ROBOT_OWNER.get());
    return owner == null ? null : server.getPlayerList().getPlayer(owner);
  }

  private static boolean moveInside(ItemEntity entity, BlockPos target) {
    Vec3 position = Vec3.atBottomCenterOf(target);
    entity.setNoGravity(false);
    entity.setDeltaMovement(Vec3.ZERO);
    entity.teleportTo(position.x, position.y, position.z);
    return false;
  }

  private static boolean deliver(ItemEntity entity, ItemStack stack, ServerPlayer owner) {
    // Drops the item at the owner's feet when the inventory is full, so it is never lost.
    owner.getInventory().placeItemBackInInventory(stack.copy());
    entity.discard();
    return true;
  }

  private static boolean sendToSpawn(
      ServerLevel level, ItemEntity entity, ServerLevel overworld, BlockPos spawn) {
    if (level == overworld) {
      return moveInside(entity, spawn);
    }
    entity.setNoGravity(false);
    entity.changeDimension(
        new DimensionTransition(
            overworld,
            Vec3.atBottomCenterOf(spawn),
            Vec3.ZERO,
            NO_ROTATION,
            NO_ROTATION,
            DimensionTransition.DO_NOTHING));
    return true;
  }

  private static boolean waitBelowTheWorld(ServerLevel level, ItemEntity entity) {
    entity.setNoGravity(true);
    entity.setDeltaMovement(Vec3.ZERO);
    entity.teleportTo(entity.getX(), level.getMinBuildHeight() - BELOW_THE_WORLD, entity.getZ());
    return true;
  }
}
