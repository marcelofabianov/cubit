package io.github.marcelofabianov.cubit.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * A soft glow that always faces the camera, without a hard edge. Drawn with a glowing render type,
 * a darker color shows as a weaker glow, so the strength scales the color.
 */
final class GlowHalo {
  private static final int MAX_COLOR = 255;
  private static final float TEXTURE_END = 1f;
  private static final float NORMAL_UP = 1f;

  /** Plain white, for the lamp. */
  static final float[] WHITE = {1f, 1f, 1f};

  private GlowHalo() {}

  static float[] rgbOf(ScanColor color) {
    return new float[] {color.red, color.green, color.blue};
  }

  /** The center is already where the camera sees it, as in a pose of the renderer. */
  static void draw(
      VertexConsumer buffer, Vector3f center, float halfSize, float strength, float[] rgb) {
    Matrix4f facing =
        new Matrix4f()
            .translation(center)
            .rotate(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
    int[] color = {channel(rgb[0], strength), channel(rgb[1], strength), channel(rgb[2], strength)};
    vertex(buffer, facing, -halfSize, -halfSize, 0, TEXTURE_END, color);
    vertex(buffer, facing, halfSize, -halfSize, TEXTURE_END, TEXTURE_END, color);
    vertex(buffer, facing, halfSize, halfSize, TEXTURE_END, 0, color);
    vertex(buffer, facing, -halfSize, halfSize, 0, 0, color);
  }

  private static int channel(float value, float strength) {
    return Math.round(MAX_COLOR * value * strength);
  }

  private static void vertex(
      VertexConsumer buffer, Matrix4f facing, float x, float y, float u, float v, int[] color) {
    buffer
        .addVertex(facing, x, y, 0)
        .setColor(color[0], color[1], color[2], MAX_COLOR)
        .setUv(u, v)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setLight(LightTexture.FULL_BRIGHT)
        .setNormal(0, NORMAL_UP, 0);
  }
}
