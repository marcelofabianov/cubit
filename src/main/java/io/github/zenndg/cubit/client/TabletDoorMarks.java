package io.github.zenndg.cubit.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.robot.TabletApp;
import io.github.zenndg.cubit.item.TabletAppLook;
import io.github.zenndg.cubit.item.TabletItem;
import io.github.zenndg.cubit.network.DoorMarksPayload;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4fStack;

/**
 * The arrows over the doors the player marked, in the color of the Doors app, only while the Tablet
 * in hand has that app active, through blocks (answer 1371). The server sends them once a second.
 */
@EventBusSubscriber(modid = CubitMod.MOD_ID, value = Dist.CLIENT)
public final class TabletDoorMarks {
  private static final float ALPHA = 0.9f;
  private static final float SCALE = 1.0f;
  private static final double HALF = 0.5;

  private static List<BlockPos> doors = List.of();

  private TabletDoorMarks() {}

  static void show(DoorMarksPayload payload) {
    doors = payload.doors();
  }

  @SubscribeEvent
  public static void render(RenderLevelStageEvent event) {
    LocalPlayer player = Minecraft.getInstance().player;
    if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL
        || player == null
        || doors.isEmpty()
        || !holdsDoorsApp(player)) {
      return;
    }
    float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
    double ticks = player.level().getGameTime() + partialTick;
    Matrix4fStack view = RenderSystem.getModelViewStack();
    view.pushMatrix();
    view.mul(event.getModelViewMatrix());
    RenderSystem.applyModelViewMatrix();
    MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
    List<Vec3> tops = doors.stream().map(TabletDoorMarks::topOf).toList();
    MobMarker.Style style = MobMarker.Style.of(TabletAppLook.color(TabletApp.DOORS), SCALE);
    MobMarker.drawOver(buffers, event.getCamera(), tops, style, ticks, ALPHA);
    buffers.endBatch();
    view.popMatrix();
    RenderSystem.applyModelViewMatrix();
  }

  /** Over the upper half of a door, which is two blocks high. */
  private static Vec3 topOf(BlockPos pos) {
    return new Vec3(pos.getX() + HALF, pos.getY() + 2, pos.getZ() + HALF);
  }

  private static boolean holdsDoorsApp(LocalPlayer player) {
    return isDoorsTablet(player.getMainHandItem()) || isDoorsTablet(player.getOffhandItem());
  }

  private static boolean isDoorsTablet(ItemStack stack) {
    return stack.is(CubitItems.TABLET.get()) && TabletItem.appOf(stack) == TabletApp.DOORS;
  }
}
