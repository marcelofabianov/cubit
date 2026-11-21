package io.github.zenndg.cubit.registry;

import com.mojang.serialization.MapCodec;
import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.item.PowerBankUpgradeRecipe;
import io.github.zenndg.cubit.item.RobotRechargeRecipe;
import io.github.zenndg.cubit.item.RobotRepairRecipe;
import io.github.zenndg.cubit.item.SolarDockRecipe;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Recipe types. The IDs are a contract with the pack, so they cannot change without a migration.
 */
public final class CubitRecipes {
  private static final String ROBOT_REPAIR_ID = "robot_repair";
  private static final String ROBOT_RECHARGE_ID = "robot_recharge";
  private static final String POWER_BANK_UPGRADE_ID = "power_bank_upgrade";
  private static final String SOLAR_DOCK_ASSEMBLY_ID = "solar_dock_assembly";
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

  /** Waking a stored robot with a Power Bank in the grid (answer 910). */
  public static final Supplier<RecipeSerializer<RobotRechargeRecipe>> ROBOT_RECHARGE =
      SERIALIZERS.register(
          ROBOT_RECHARGE_ID,
          () -> serializer(RobotRechargeRecipe.CODEC, RobotRechargeRecipe.STREAM_CODEC));

  /** The chained Power Bank, which keeps its charge (answer 910). */
  public static final Supplier<RecipeSerializer<PowerBankUpgradeRecipe>> POWER_BANK_UPGRADE =
      SERIALIZERS.register(
          POWER_BANK_UPGRADE_ID,
          () -> serializer(PowerBankUpgradeRecipe.CODEC, PowerBankUpgradeRecipe.STREAM_CODEC));

  /** The Dock Solar Panel on an Induction Dock, which keeps its state (answer 1386). */
  public static final Supplier<RecipeSerializer<SolarDockRecipe>> SOLAR_DOCK_ASSEMBLY =
      SERIALIZERS.register(
          SOLAR_DOCK_ASSEMBLY_ID,
          () -> serializer(SolarDockRecipe.CODEC, SolarDockRecipe.STREAM_CODEC));

  private CubitRecipes() {}

  private static <T extends Recipe<?>> RecipeSerializer<T> serializer(
      MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
    return new RecipeSerializer<>() {
      @Override
      public MapCodec<T> codec() {
        return codec;
      }

      @Override
      public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return streamCodec;
      }
    };
  }

  public static void register(IEventBus modBus) {
    SERIALIZERS.register(modBus);
  }
}
