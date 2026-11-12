package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.registry.CubitDamageTypes;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

/** The damage type of the shock, a data file like the vanilla ones (answer 831). */
final class CubitDamageTypeProvider extends DatapackBuiltinEntriesProvider {
  /** The hunger a hit costs, the same as most vanilla damage. */
  private static final float EXHAUSTION = 0.1f;

  private static final RegistrySetBuilder ENTRIES =
      new RegistrySetBuilder()
          .add(
              Registries.DAMAGE_TYPE,
              context ->
                  context.register(
                      CubitDamageTypes.SHOCK,
                      new DamageType(CubitDamageTypes.SHOCK_MESSAGE_ID, EXHAUSTION)));

  CubitDamageTypeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries, ENTRIES, Set.of(CubitMod.MOD_ID));
  }
}
