package io.github.marcelofabianov.cubit.player;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * The Target app: the mob clicked, or the one in the aim up to the reach of the config, becomes the
 * target of Focus of the chosen Cubit, by the rules of a mob the owner hurt (answers 1043 and
 * 1047). The aim stops at the first block, so a mob behind a wall is not marked.
 */
final class TabletTargetApp {
  private static final double AIM_MARGIN = 1;

  private TabletTargetApp() {}

  static void click(ServerPlayer player, RobotEntity chosen, TabletUse.Click click) {
    Optional<LivingEntity> mob =
        click instanceof TabletUse.Living living && living.entity() instanceof LivingEntity hit
            ? Optional.of(hit)
            : aimed(player);
    if (mob.isEmpty()) {
      player.displayClientMessage(Component.translatable(TabletKeys.NO_MOB), true);
    } else if (!chosen.defense().markFocusTarget(mob.get())) {
      player.displayClientMessage(Component.translatable(TabletKeys.NOT_A_TARGET), true);
    }
  }

  private static Optional<LivingEntity> aimed(ServerPlayer player) {
    double reach = CubitMod.CONFIG.tabletTargetReachBlocks();
    Vec3 eye = player.getEyePosition();
    Vec3 look = player.getViewVector(1);
    Vec3 end = eye.add(look.scale(reach));
    HitResult wall =
        player
            .level()
            .clip(
                new ClipContext(
                    eye, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
    Vec3 limit = wall.getType() == HitResult.Type.MISS ? end : wall.getLocation();
    AABB area = player.getBoundingBox().expandTowards(look.scale(reach)).inflate(AIM_MARGIN);
    EntityHitResult hit =
        ProjectileUtil.getEntityHitResult(
            player, eye, limit, area, TabletTargetApp::aimable, eye.distanceToSqr(limit));
    return hit != null && hit.getEntity() instanceof LivingEntity mob
        ? Optional.of(mob)
        : Optional.empty();
  }

  private static boolean aimable(Entity entity) {
    return entity instanceof LivingEntity && entity.isAlive() && entity.isPickable();
  }
}
