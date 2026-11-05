package io.github.marcelofabianov.cubit;

import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import io.github.marcelofabianov.cubit.registry.CubitCreativeTabs;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import io.github.marcelofabianov.cubit.registry.CubitRecipes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(CubitMod.MOD_ID)
public final class CubitMod {
  public static final String MOD_ID = "cubit";
  public static final CubitConfig CONFIG = new CubitConfig();

  public CubitMod(IEventBus modBus, ModContainer container) {
    container.registerConfig(ModConfig.Type.COMMON, CONFIG.spec());
    CubitDataComponents.register(modBus);
    CubitAttachments.register(modBus);
    CubitEntities.register(modBus);
    CubitItems.register(modBus);
    CubitRecipes.register(modBus);
    CubitCreativeTabs.register(modBus);
  }
}
