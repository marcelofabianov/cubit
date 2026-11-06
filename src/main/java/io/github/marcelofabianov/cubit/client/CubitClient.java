package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.network.CubitNetwork;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import io.github.marcelofabianov.cubit.registry.CubitMenus;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

/** Client-only setup. Common code never references this package, or a dedicated server breaks. */
@EventBusSubscriber(modid = CubitMod.MOD_ID, value = Dist.CLIENT)
public final class CubitClient {
  private static final String NOTICE_LAYER = "notice";

  private CubitClient() {}

  @SubscribeEvent
  public static void setup(FMLClientSetupEvent event) {
    CubitNetwork.setNoticeDisplay(RobotNoticeOverlay::show);
  }

  /** Above the message of the game, because the height of the bars is only known by then. */
  @SubscribeEvent
  public static void registerGuiLayers(RegisterGuiLayersEvent event) {
    event.registerAbove(
        VanillaGuiLayers.OVERLAY_MESSAGE,
        ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, NOTICE_LAYER),
        RobotNoticeOverlay::render);
  }

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
