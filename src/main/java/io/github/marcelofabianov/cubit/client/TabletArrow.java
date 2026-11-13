package io.github.marcelofabianov.cubit.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotLooks;
import io.github.marcelofabianov.cubit.item.TierLook;
import io.github.marcelofabianov.cubit.network.ChosenRobotPayload;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4fStack;

/**
 * The arrow over the Cubit chosen in the Tablet, only for the player who holds the Tablet, in the
 * color of its collar, bobbing and drawn through blocks (answers 1010, 1012 and 1013). Out of the
 * render range or in another dimension, there is no arrow (answer 1014).
 */
@EventBusSubscriber(modid = CubitMod.MOD_ID, value = Dist.CLIENT)
public final class TabletArrow {
  private static final float ALPHA = 0.9f;
  private static final float SCALE = 1.2f;

  private static Optional<UUID> chosen = Optional.empty();

  private TabletArrow() {}

  static void setChosen(ChosenRobotPayload payload) {
    chosen = payload.robot();
  }

  @SubscribeEvent
  public static void render(RenderLevelStageEvent event) {
    Minecraft minecraft = Minecraft.getInstance();
    LocalPlayer player = minecraft.player;
    ClientLevel level = minecraft.level;
    if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL
        || player == null
        || level == null
        || chosen.isEmpty()
        || !holdsTablet(player)) {
      return;
    }
    robotIn(level).ifPresent(robot -> draw(event, robot));
  }

  private static boolean holdsTablet(LocalPlayer player) {
    return player.getMainHandItem().is(CubitItems.TABLET.get())
        || player.getOffhandItem().is(CubitItems.TABLET.get());
  }

  private static Optional<RobotEntity> robotIn(ClientLevel level) {
    for (Entity entity : level.entitiesForRendering()) {
      if (entity instanceof RobotEntity robot && chosen.get().equals(robot.getUUID())) {
        return Optional.of(robot);
      }
    }
    return Optional.empty();
  }

  private static void draw(RenderLevelStageEvent event, RobotEntity robot) {
    float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
    double ticks = robot.level().getGameTime() + partialTick;
    Matrix4fStack view = RenderSystem.getModelViewStack();
    view.pushMatrix();
    view.mul(event.getModelViewMatrix());
    RenderSystem.applyModelViewMatrix();
    MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
    MobMarker.Style style = MobMarker.Style.of(TierLook.color(RobotLooks.tier(robot)), SCALE);
    MobMarker.draw(buffers, event.getCamera(), List.of(robot), style, partialTick, ticks, ALPHA);
    view.popMatrix();
    RenderSystem.applyModelViewMatrix();
  }
}
