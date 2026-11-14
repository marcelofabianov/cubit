package io.github.marcelofabianov.cubit.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.MarkPulse;
import java.util.List;
import java.util.OptionalDouble;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4fStack;

/**
 * Draws what the scanners found through blocks, without a mixin and without touching the mob: a box
 * around each ore and an arrow over each mob (answers 557, 594 and 646). After the level, so it
 * works in every graphics mode, even fabulous, where the other stages draw into targets of their
 * own. The numbers are only visual, so they stay here.
 */
@EventBusSubscriber(modid = CubitMod.MOD_ID, value = Dist.CLIENT)
public final class ScanMarkRenderer {
  private static final String TYPE_NAME = "cubit_scan_mark";
  private static final int BUFFER_SIZE = 1536;
  private static final double LINE_WIDTH = 2.0;
  private static final float ORE_ALPHA = 0.3f;
  private static final float MOB_ALPHA = 0.5f;

  /** Lines that ignore depth, so the box shows through blocks. */
  static final RenderType THROUGH_BLOCKS =
      RenderType.create(
          TYPE_NAME,
          DefaultVertexFormat.POSITION_COLOR_NORMAL,
          VertexFormat.Mode.LINES,
          BUFFER_SIZE,
          RenderType.CompositeState.builder()
              .setShaderState(RenderStateShard.RENDERTYPE_LINES_SHADER)
              .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(LINE_WIDTH)))
              .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
              .setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
              .setWriteMaskState(RenderStateShard.COLOR_WRITE)
              .setCullState(RenderStateShard.NO_CULL)
              .createCompositeState(false));

  private ScanMarkRenderer() {}

  @SubscribeEvent
  public static void render(RenderLevelStageEvent event) {
    if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) {
      return;
    }
    ClientLevel level = ScanMarks.currentLevel();
    if (level == null) {
      return;
    }
    List<BlockPos> ores = ScanMarks.ores(level);
    List<Entity> mobs = ScanMarks.mobs(level);
    Entity target = ScanMarks.focusTarget(level);
    if (ores.isEmpty() && mobs.isEmpty() && target == null) {
      return;
    }
    float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
    double ticks = level.getGameTime() + partialTick;
    float pulse = (float) MarkPulse.at(ticks);
    Matrix4fStack view = RenderSystem.getModelViewStack();
    view.pushMatrix();
    view.mul(event.getModelViewMatrix());
    RenderSystem.applyModelViewMatrix();
    MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
    drawOres(buffers, event.getCamera().getPosition(), ores, pulse);
    drawMobs(buffers, event.getCamera(), new Marked(mobs, target), partialTick, ticks, pulse);
    view.popMatrix();
    RenderSystem.applyModelViewMatrix();
  }

  /** The red arrows of the scanner, then the yellow one of the target of Focus. */
  private static void drawMobs(
      MultiBufferSource.BufferSource buffers,
      Camera camera,
      Marked marked,
      float partialTick,
      double ticks,
      float pulse) {
    float alpha = MOB_ALPHA * pulse;
    MobMarker.draw(
        buffers, camera, marked.mobs(), MobMarker.Style.SCANNED, partialTick, ticks, alpha);
    if (marked.target() != null) {
      MobMarker.draw(
          buffers,
          camera,
          List.of(marked.target()),
          MobMarker.Style.TARGET,
          partialTick,
          ticks,
          alpha);
    }
  }

  private record Marked(List<Entity> mobs, Entity target) {}

  private static void drawOres(
      MultiBufferSource.BufferSource buffers, Vec3 camera, List<BlockPos> ores, float pulse) {
    VertexConsumer lines = buffers.getBuffer(THROUGH_BLOCKS);
    PoseStack pose = new PoseStack();
    pose.translate(-camera.x, -camera.y, -camera.z);
    for (BlockPos pos : ores) {
      box(pose, lines, new AABB(pos), ScanColor.ORE, ORE_ALPHA * pulse);
    }
    buffers.endBatch(THROUGH_BLOCKS);
  }

  private static void box(
      PoseStack pose, VertexConsumer lines, AABB box, ScanColor color, float alpha) {
    LevelRenderer.renderLineBox(pose, lines, box, color.red, color.green, color.blue, alpha);
  }
}
