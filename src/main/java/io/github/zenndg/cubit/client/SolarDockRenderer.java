package io.github.zenndg.cubit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.block.InductionDockBlock;
import io.github.zenndg.cubit.block.InductionDockBlockEntity;
import io.github.zenndg.cubit.domain.behavior.SolarPulse;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

/**
 * The slow pulse of the cells of a Solar Dock while its panel generates, the same as the panel of
 * the Cubit (answer 1392). It lights the face of the panel on top of the tower, which the model
 * tilts toward the front.
 */
final class SolarDockRenderer implements BlockEntityRenderer<InductionDockBlockEntity> {
  private static final String PANEL_PATH = "textures/block/induction_dock_solar_panel.png";
  private static final ResourceLocation PANEL =
      ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PANEL_PATH);
  private static final float PIXEL = 1f / 16;
  private static final float HALF = 0.5f;
  private static final float TILT_DEGREES = -22.5f;
  private static final float PIVOT_X = 8f;
  private static final float PIVOT_Y = 30.5f;
  private static final float PIVOT_Z = 13.5f;
  private static final float FACE_Y = 31.02f;
  private static final float LEFT = 1.5f;
  private static final float RIGHT = 14.5f;
  private static final float BACK = 9.5f;
  private static final float FRONT = 17.5f;
  private static final int MAX_COLOR = 255;
  private static final int ALPHA_SHIFT = 24;
  private static final int RED_SHIFT = 16;
  private static final int GREEN_SHIFT = 8;

  /** Spreads the docks over the pulse, so they do not all breathe at the same moment. */
  private static final int PULSE_SPREAD_TICKS = 23;

  SolarDockRenderer(BlockEntityRendererProvider.Context context) {}

  @Override
  public void render(
      InductionDockBlockEntity dock,
      float partialTick,
      PoseStack pose,
      MultiBufferSource buffers,
      int packedLight,
      int packedOverlay) {
    if (!dock.isSolar() || !dock.solar().sunlit() || dock.getLevel() == null) {
      return;
    }
    double ticks =
        dock.getLevel().getGameTime()
            + partialTick
            + Math.floorMod(dock.getBlockPos().asLong(), PULSE_SPREAD_TICKS);
    int color = gray(SolarPulse.at(ticks));
    int turn = turnOf(dock);
    pose.pushPose();
    pose.translate(HALF, 0, HALF);
    pose.mulPose(Axis.YP.rotationDegrees(-turn));
    pose.translate(-HALF, 0, -HALF);
    pose.scale(PIXEL, PIXEL, PIXEL);
    pose.translate(PIVOT_X, PIVOT_Y, PIVOT_Z);
    pose.mulPose(Axis.XP.rotationDegrees(TILT_DEGREES));
    pose.translate(-PIVOT_X, -PIVOT_Y, -PIVOT_Z);
    face(pose, buffers.getBuffer(RenderType.eyes(PANEL)), color);
    pose.popPose();
  }

  /** The same turn as the model of the dock in its block state. */
  private static int turnOf(InductionDockBlockEntity dock) {
    return InductionDockBlock.turnOf(dock.getBlockState().getValue(InductionDockBlock.FACING));
  }

  private static void face(PoseStack pose, VertexConsumer buffer, int color) {
    PoseStack.Pose last = pose.last();
    Matrix4f matrix = last.pose();
    vertex(buffer, last, matrix, LEFT, BACK, 0, 0, color);
    vertex(buffer, last, matrix, LEFT, FRONT, 0, 1, color);
    vertex(buffer, last, matrix, RIGHT, FRONT, 1, 1, color);
    vertex(buffer, last, matrix, RIGHT, BACK, 1, 0, color);
  }

  private static void vertex(
      VertexConsumer buffer,
      PoseStack.Pose last,
      Matrix4f matrix,
      float x,
      float z,
      float u,
      float v,
      int color) {
    buffer
        .addVertex(matrix, x, FACE_Y, z)
        .setColor(color)
        .setUv(u, v)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(LightTexture.FULL_BRIGHT)
        .setNormal(last, 0, 1, 0);
  }

  private static int gray(double strength) {
    int value = (int) Math.round(MAX_COLOR * strength);
    return MAX_COLOR << ALPHA_SHIFT | value << RED_SHIFT | value << GREEN_SHIFT | value;
  }
}
