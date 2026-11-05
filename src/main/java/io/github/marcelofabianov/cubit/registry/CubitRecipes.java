package io.github.marcelofabianov.cubit.registry;

import com.mojang.serialization.MapCodec;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.item.RobotRepairRecipe;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Recipe types. The IDs are a contract with the pack, so they cannot change without a migration.
 */
public final class CubitRecipes {
  private static final String ROBOT_REPAIR_ID = "robot_repair";
  private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
      DeferredRegister.create(Registries.RECIPE_SERIALIZER, CubitMod.MOD_ID);

  public static final Supplier<RecipeSerializer<RobotRepairRecipe>> ROBOT_REPAIR =
      SERIALIZERS.register(
          ROBOT_REPAIR_ID,
          () ->
              new RecipeSerializer<>() {
                @Override
                public MapCodec<RobotRepairRecipe> codec() {
                  return RobotRepairRecipe.CODEC;
                }

                @Override
                public StreamCodec<RegistryFriendlyByteBuf, RobotRepairRecipe> streamCodec() {
                  return RobotRepairRecipe.STREAM_CODEC;
                }
              });

  private CubitRecipes() {}

  public static void register(IEventBus modBus) {
    SERIALIZERS.register(modBus);
  }
}
