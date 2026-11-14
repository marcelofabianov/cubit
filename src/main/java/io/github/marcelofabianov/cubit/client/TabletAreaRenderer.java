package io.github.marcelofabianov.cubit.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4fStack;

/**
 * Draws the box of the Area app through blocks, after the level, as the boxes of the scanners
 * (answer 1056). While marking, the action bar shows the size, only when it changes, so the texts
 * of the server still show.
 */
@EventBusSubscriber(modid = CubitMod.MOD_ID, value = Dist.CLIENT)
public final class TabletAreaRenderer {
  private static final float FITS_RED = 0.3f;
  private static final float FITS_GREEN = 0.9f;
  private static final float FITS_BLUE = 0.35f;
  private static final float TOO_BIG_RED = 0.95f;
  private static final float TOO_BIG_GREEN = 0.25f;
  private static final float TOO_BIG_BLUE = 0.25f;
  private static final float AREA_RED = 0.6f;
  private static final float AREA_GREEN = 0.4f;
  private static final float AREA_BLUE = 0.95f;
  private static final float ALPHA = 0.9f;

  private static TabletAreaMarks.Size lastSize;

  private TabletAreaRenderer() {}

  @SubscribeEvent
  public static void render(RenderLevelStageEvent event) {
    if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) {
      return;
    }
    Optional<TabletAreaMarks.Shown> shown = TabletAreaMarks.shown();
    if (shown.isEmpty()) {
      lastSize = null;
      return;
    }
    Matrix4fStack view = RenderSystem.getModelViewStack();
    view.pushMatrix();
    view.mul(event.getModelViewMatrix());
    RenderSystem.applyModelViewMatrix();
    draw(event.getCamera().getPosition(), shown.get());
    view.popMatrix();
    RenderSystem.applyModelViewMatrix();
    sayTheSize(shown.get());
  }

  private static void draw(Vec3 camera, TabletAreaMarks.Shown shown) {
    MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
    VertexConsumer lines = buffers.getBuffer(ScanMarkRenderer.THROUGH_BLOCKS);
    PoseStack pose = new PoseStack();
    pose.translate(-camera.x, -camera.y, -camera.z);
    if (!shown.marking()) {
      LevelRenderer.renderLineBox(pose, lines, shown.box(), AREA_RED, AREA_GREEN, AREA_BLUE, ALPHA);
    } else if (shown.fits()) {
      LevelRenderer.renderLineBox(pose, lines, shown.box(), FITS_RED, FITS_GREEN, FITS_BLUE, ALPHA);
    } else {
      LevelRenderer.renderLineBox(
          pose, lines, shown.box(), TOO_BIG_RED, TOO_BIG_GREEN, TOO_BIG_BLUE, ALPHA);
    }
    buffers.endBatch(ScanMarkRenderer.THROUGH_BLOCKS);
  }

  private static void sayTheSize(TabletAreaMarks.Shown shown) {
    TabletAreaMarks.Size size = shown.size();
    if (!shown.marking() || size.equals(lastSize)) {
      return;
    }
    lastSize = size;
    Component text =
        Component.translatable(TabletKeys.AREA_SIZE, size.width(), size.height(), size.depth())
            .withStyle(shown.fits() ? ChatFormatting.GREEN : ChatFormatting.RED);
    Minecraft.getInstance().gui.setOverlayMessage(text, false);
  }
}
