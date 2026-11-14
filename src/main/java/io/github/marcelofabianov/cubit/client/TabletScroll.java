package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.network.CubitNetwork;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

/**
 * Shift and the wheel with the Tablet in hand switch its app instead of the slot of the hotbar
 * (answer 1006). The client only asks, and the server picks the app.
 */
@EventBusSubscriber(modid = CubitMod.MOD_ID, value = Dist.CLIENT)
public final class TabletScroll {
  private TabletScroll() {}

  @SubscribeEvent
  public static void onScroll(InputEvent.MouseScrollingEvent event) {
    Minecraft minecraft = Minecraft.getInstance();
    LocalPlayer player = minecraft.player;
    if (minecraft.screen != null
        || player == null
        || !player.isShiftKeyDown()
        || !holdsTablet(player)
        || event.getScrollDeltaY() == 0) {
      return;
    }
    event.setCanceled(true);
    CubitNetwork.switchTabletApp((int) -Math.signum(event.getScrollDeltaY()));
  }

  private static boolean holdsTablet(LocalPlayer player) {
    return player.getMainHandItem().is(CubitItems.TABLET.get())
        || player.getOffhandItem().is(CubitItems.TABLET.get());
  }
}
