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
 * Draws the boxes of the scanners through blocks, without a mixin and without touching the mob
 * (answers 557 and 594). After the level, so it works in every graphics mode, even fabulous, where
 * the other stages draw into targets of their own. The numbers are only visual, so they stay here.
 */
@EventBusSubscriber(modid = CubitMod.MOD_ID, value = Dist.CLIENT)
public final class ScanMarkRenderer {
  private static final String TYPE_NAME = "cubit_scan_mark";
  private static final int BUFFER_SIZE = 1536;
  private static final double LINE_WIDTH = 2.0;
  private static final float ORE_ALPHA = 0.3f;
  private static final float MOB_ALPHA = 0.5f;
  private static final float ORE_RED = 0.55f;
  private static final float ORE_GREEN = 0.8f;
  private static final float ORE_BLUE = 1f;
  private static final float MOB_RED = 1f;
  private static final float MOB_GREEN = 0.45f;
  private static final float MOB_BLUE = 0.45f;

  /** Lines that ignore depth, so the box shows through blocks. */
  private static final RenderType THROUGH_BLOCKS =
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
    if (ores.isEmpty() && mobs.isEmpty()) {
      return;
    }
    float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
    float pulse = (float) MarkPulse.at(level.getGameTime() + partialTick);
    Matrix4fStack view = RenderSystem.getModelViewStack();
    view.pushMatrix();
    view.mul(event.getModelViewMatrix());
    RenderSystem.applyModelViewMatrix();
    draw(event.getCamera().getPosition(), ores, mobs, partialTick, pulse);
    view.popMatrix();
    RenderSystem.applyModelViewMatrix();
  }

  private static void draw(
      Vec3 camera, List<BlockPos> ores, List<Entity> mobs, float partialTick, float pulse) {
    MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
    VertexConsumer lines = buffers.getBuffer(THROUGH_BLOCKS);
    PoseStack pose = new PoseStack();
    pose.translate(-camera.x, -camera.y, -camera.z);
    for (BlockPos pos : ores) {
      LevelRenderer.renderLineBox(
          pose, lines, new AABB(pos), ORE_RED, ORE_GREEN, ORE_BLUE, ORE_ALPHA * pulse);
    }
    for (Entity mob : mobs) {
      AABB box = mob.getBoundingBox().move(mob.getPosition(partialTick).subtract(mob.position()));
      LevelRenderer.renderLineBox(
          pose, lines, box, MOB_RED, MOB_GREEN, MOB_BLUE, MOB_ALPHA * pulse);
    }
    buffers.endBatch(THROUGH_BLOCKS);
  }
}
