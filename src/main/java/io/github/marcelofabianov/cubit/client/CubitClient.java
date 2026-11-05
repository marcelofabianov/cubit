package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import io.github.marcelofabianov.cubit.registry.CubitMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

/** Client-only setup. Common code never references this package, or a dedicated server breaks. */
@EventBusSubscriber(modid = CubitMod.MOD_ID, value = Dist.CLIENT)
public final class CubitClient {
  private CubitClient() {}

  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(
        CubitEntities.GENERAL_ROBOT.get(),
        context -> new RobotRenderer(context, CubitConfig.GENERAL_ROBOT));
  }

  @SubscribeEvent
  public static void registerScreens(RegisterMenuScreensEvent event) {
    event.register(CubitMenus.ROBOT.get(), RobotScreen::new);
    event.register(CubitMenus.INDUCTION_DOCK.get(), InductionDockScreen::new);
  }
}
