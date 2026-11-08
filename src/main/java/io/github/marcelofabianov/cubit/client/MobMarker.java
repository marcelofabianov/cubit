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
 * A red arrow over the head of each hostile mob the scanner found, pointing down at it, facing the
 * camera and drawn through blocks (answers 646, 648 and 649). The numbers are only visual, so they
 * stay here and not in the config.
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

  static void draw(
      MultiBufferSource.BufferSource buffers,
      Camera camera,
      List<Entity> mobs,
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
      arrow(triangles, facing, tip, alpha);
    }
    buffers.endBatch(THROUGH_BLOCKS);
  }

  /** Two upper corners and the point, which sits just over the head. */
  private static void arrow(
      VertexConsumer triangles, Quaternionf facing, Vector3f point, float alpha) {
    Vector3f center = new Vector3f(0, HALF_HEIGHT, 0).rotate(facing).add(point);
    corner(triangles, facing, center, -HALF_WIDTH, HALF_HEIGHT, alpha);
    corner(triangles, facing, center, HALF_WIDTH, HALF_HEIGHT, alpha);
    corner(triangles, facing, center, 0, -HALF_HEIGHT, alpha);
  }

  private static void corner(
      VertexConsumer triangles,
      Quaternionf facing,
      Vector3f center,
      float x,
      float y,
      float alpha) {
    Vector3f at = new Vector3f(x, y, 0).rotate(facing).add(center);
    ScanColor color = ScanColor.MOB;
    triangles.addVertex(at.x, at.y, at.z).setColor(color.red, color.green, color.blue, alpha);
  }
}
