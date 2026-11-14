package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.item.RobotItem;
import io.github.marcelofabianov.cubit.item.TierLook;
import io.github.marcelofabianov.cubit.network.CubitNetwork;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import io.github.marcelofabianov.cubit.registry.CubitMenus;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

/** Client-only setup. Common code never references this package, or a dedicated server breaks. */
@EventBusSubscriber(modid = CubitMod.MOD_ID, value = Dist.CLIENT)
public final class CubitClient {
  private static final String NOTICE_LAYER = "notice";
  private static final int COLLAR_LAYER = 1;
  private static final int NO_TINT = -1;

  private CubitClient() {}

  @SubscribeEvent
  public static void setup(FMLClientSetupEvent event) {
    CubitNetwork.setNoticeDisplay(RobotNoticeOverlay::show);
    CubitNetwork.setMarksDisplay(ScanMarks::showOres, ScanMarks::showMobs);
    CubitNetwork.setFocusMarkDisplay(ScanMarks::showFocus);
    CubitNetwork.setTabletDisplays(TabletSelectionScreen::show, TabletArrow::setChosen);
    CubitNetwork.setAreaDisplay(TabletAreaMarks::show);
    event.enqueueWork(() ->
        ItemProperties.register(
            CubitItems.GENERAL_ROBOT.get(),
            RobotItem.COLOR_PROPERTY,
            (stack, level, entity, seed) -> RobotItem.iconColor(stack)));
  }

  /** Above the message of the game, because the height of the bars is only known by then. */
  @SubscribeEvent
  public static void registerGuiLayers(RegisterGuiLayersEvent event) {
    event.registerAbove(
        VanillaGuiLayers.OVERLAY_MESSAGE,
        ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, NOTICE_LAYER),
        RobotNoticeOverlay::render);
  }

  /** Only the collar layer is tinted, with the color of the tier saved in the item. */
  @SubscribeEvent
  public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
    event.register(
        (stack, layer) ->
            layer == COLLAR_LAYER
                ? FastColor.ARGB32.opaque(TierLook.color(TierLook.tierOf(stack)))
                : NO_TINT,
        CubitItems.GENERAL_ROBOT.get(),
        CubitItems.DAMAGED_GENERAL_ROBOT.get());
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
    event.register(CubitMenus.TABLET_ROBOT.get(), TabletRobotScreen::new);
  }
}
