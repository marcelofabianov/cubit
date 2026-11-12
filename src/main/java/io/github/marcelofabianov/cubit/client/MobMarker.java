package io.github.marcelofabianov.cubit.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * An arrow over the head of a mob, pointing down at it, facing the camera and drawn through blocks:
 * red for each hostile mob the scanner found, and yellow and bigger for the target of Focus
 * (answers 646, 648, 649 and 853). The numbers are only visual, so they stay here and not in the
 * config.
 */
final class MobMarker {
  private static final String TYPE_NAME = "cubit_mob_marker";
  private static final int BUFFER_SIZE = 256;
  private static final float HALF_WIDTH = 0.22f;
  private static final float HALF_HEIGHT = 0.18f;
  private static final float GAP_ABOVE_HEAD = 0.45f;
  private static final float BOB_BLOCKS = 0.08f;
  private static final double BOB_PERIOD_TICKS = 40;
  private static final double FULL_TURN = 2 * Math.PI;
  private static final float TARGET_SCALE = 1.5f;

  /** Filled triangles that ignore depth, so the arrow shows through blocks. */
  static final RenderType THROUGH_BLOCKS =
      RenderType.create(
          TYPE_NAME,
          DefaultVertexFormat.POSITION_COLOR,
          VertexFormat.Mode.TRIANGLES,
          BUFFER_SIZE,
          RenderType.CompositeState.builder()
              .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
              .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
              .setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
              .setWriteMaskState(RenderStateShard.COLOR_WRITE)
              .setCullState(RenderStateShard.NO_CULL)
              .createCompositeState(false));

  private MobMarker() {}

  /** The look of an arrow: red for what the scanner found, and yellow, bigger, for the target. */
  record Style(ScanColor color, float scale) {
    static final Style SCANNED = new Style(ScanColor.MOB, 1f);
    static final Style TARGET = new Style(ScanColor.FOCUS, TARGET_SCALE);
  }

  static void draw(
      MultiBufferSource.BufferSource buffers,
      Camera camera,
      List<Entity> mobs,
      Style style,
      float partialTick,
      double ticks,
      float alpha) {
    VertexConsumer triangles = buffers.getBuffer(THROUGH_BLOCKS);
    Quaternionf facing = camera.rotation();
    float bob = (float) (BOB_BLOCKS * Math.sin(FULL_TURN * ticks / BOB_PERIOD_TICKS));
    for (Entity mob : mobs) {
      Vec3 feet = mob.getPosition(partialTick).subtract(camera.getPosition());
      Vector3f tip = new Vector3f((float) feet.x, (float) feet.y, (float) feet.z);
      tip.add(0, mob.getBbHeight() + GAP_ABOVE_HEAD + bob, 0);
      arrow(triangles, facing, tip, style, alpha);
    }
    buffers.endBatch(THROUGH_BLOCKS);
  }

  /** Two upper corners and the point, which sits just over the head. */
  private static void arrow(
      VertexConsumer triangles, Quaternionf facing, Vector3f point, Style style, float alpha) {
    float width = HALF_WIDTH * style.scale();
    float height = HALF_HEIGHT * style.scale();
    Vector3f center = new Vector3f(0, height, 0).rotate(facing).add(point);
    corner(triangles, facing, center, new Vector3f(-width, height, 0), style.color(), alpha);
    corner(triangles, facing, center, new Vector3f(width, height, 0), style.color(), alpha);
    corner(triangles, facing, center, new Vector3f(0, -height, 0), style.color(), alpha);
  }

  private static void corner(
      VertexConsumer triangles,
      Quaternionf facing,
      Vector3f center,
      Vector3f offset,
      ScanColor color,
      float alpha) {
    Vector3f at = offset.rotate(facing).add(center);
    triangles.addVertex(at.x, at.y, at.z).setColor(color.red, color.green, color.blue, alpha);
  }
}
