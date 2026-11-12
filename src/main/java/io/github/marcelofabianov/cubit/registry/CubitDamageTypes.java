package io.github.marcelofabianov.cubit.registry;

import io.github.marcelofabianov.cubit.CubitMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

/** The damage of the robot's shock. The ID is part of the saves and of the death messages. */
public final class CubitDamageTypes {
  private static final String SHOCK_ID = "shock";
  public static final ResourceKey<DamageType> SHOCK =
      ResourceKey.create(
          Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, SHOCK_ID));

  /** The game builds the death message key as death.attack. followed by this id. */
  public static final String SHOCK_MESSAGE_ID = CubitMod.MOD_ID + "." + SHOCK_ID;

  private CubitDamageTypes() {}

  public static DamageSource shock(Entity robot) {
    return new DamageSource(
        robot
            .level()
            .registryAccess()
            .registryOrThrow(Registries.DAMAGE_TYPE)
            .getHolderOrThrow(SHOCK),
        robot);
  }
}
