package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.registry.CubitEntities;
import io.github.zenndg.cubit.registry.CubitItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

/** A thrown Recharge Splash. It breaks on the first block or entity it hits, as a splash potion. */
public final class RechargeSplashProjectile extends ThrowableItemProjectile {
  /** The gravity of the splash potion of the game, so it flies the same way. */
  private static final double GRAVITY = 0.05;

  public RechargeSplashProjectile(
      EntityType<? extends RechargeSplashProjectile> type, Level level) {
    super(type, level);
  }

  public RechargeSplashProjectile(Level level, LivingEntity thrower) {
    super(CubitEntities.RECHARGE_SPLASH.get(), thrower, level);
  }

  @Override
  protected Item getDefaultItem() {
    return CubitItems.RECHARGE_SPLASH.get();
  }

  @Override
  protected double getDefaultGravity() {
    return GRAVITY;
  }

  @Override
  protected void onHit(HitResult result) {
    super.onHit(result);
    if (level() instanceof ServerLevel level) {
      RechargeSplashImpact.at(level, this);
      discard();
    }
  }
}
