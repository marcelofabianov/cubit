package io.github.zenndg.cubit;

import io.github.zenndg.cubit.config.CubitConfig;
import io.github.zenndg.cubit.network.CubitNetwork;
import io.github.zenndg.cubit.player.TabletSelection;
import io.github.zenndg.cubit.registry.CubitAttachments;
import io.github.zenndg.cubit.registry.CubitBlockEntities;
import io.github.zenndg.cubit.registry.CubitBlocks;
import io.github.zenndg.cubit.registry.CubitCreativeTabs;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import io.github.zenndg.cubit.registry.CubitEntities;
import io.github.zenndg.cubit.registry.CubitItems;
import io.github.zenndg.cubit.registry.CubitMenus;
import io.github.zenndg.cubit.registry.CubitRecipes;
import io.github.zenndg.cubit.registry.CubitSounds;
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
    CubitBlocks.register(modBus);
    CubitBlockEntities.register(modBus);
    CubitEntities.register(modBus);
    CubitItems.register(modBus);
    CubitMenus.register(modBus);
    CubitRecipes.register(modBus);
    CubitSounds.register(modBus);
    CubitCreativeTabs.register(modBus);
    modBus.addListener(CubitNetwork::register);
    CubitNetwork.setTabletRequests(TabletSelection.requests());
  }
}
