package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.domain.behavior.BlinkCycle;
import io.github.marcelofabianov.cubit.domain.behavior.BlinkFrame;
import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import java.util.Locale;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * The portrait of a robot on a screen: the face it shows now, cut from the same textures as the
 * model, so it blinks the same way, and the collar in the color of its tier (answer 690).
 */
final class RobotPortrait {
  static final int SIZE = 36;
  private static final String FACE_PATH = "textures/entity/%s_screen/%s.png";
  private static final String BLINK_NAME = "normal_level_%s";
  private static final int ATLAS = 1024;
  private static final int FACE_U = 248;
  private static final int FACE_V = 24;
  private static final int FACE_WIDTH = 144;
  private static final int FACE_HEIGHT = 88;
  private static final int SHOWN_FACE_X = 3;
  private static final int SHOWN_FACE_Y = 8;
  private static final int SHOWN_FACE_WIDTH = 30;
  private static final int SHOWN_FACE_HEIGHT = 18;
  private static final int COLLAR_X = 6;
  private static final int COLLAR_Y = 29;
  private static final int COLLAR_WIDTH = 14;
  private static final int LIGHT_X = 26;
  private static final int LIGHT_Y = 28;
  private static final int LIGHT_SIZE = 3;
  private static final int BASE_X = 9;
  private static final int BASE_Y = 32;
  private static final int BASE_WIDTH = 18;
  private static final int LIGHT_COLOR = 0xFFF2994A;
  private static final int BASE_COLOR = 0xFF9AA0A6;
  private static final int[] COLLAR_COLORS = {0xFF9AA0A6, 0xFF3B82F6, 0xFF8B5CF6, 0xFFF2B705};

  private RobotPortrait() {}

  static void draw(
      GuiGraphics graphics, int x, int y, RobotExpression expression, RobotTier tier, long tick) {
    draw(graphics, x, y, new Look(expression, tier, ScreenPainter.PORTRAIT), tick);
  }

  /** The face, the collar and the shell color behind them (answer 1011). */
  record Look(RobotExpression expression, RobotTier tier, int shell) {}

  static void draw(GuiGraphics graphics, int x, int y, Look look, long tick) {
    RobotExpression expression = look.expression();
    RobotTier tier = look.tier();
    ScreenPainter.sunken(graphics, x, y, SIZE, SIZE, look.shell());
    graphics.blit(
        face(expression, tick),
        x + SHOWN_FACE_X,
        y + SHOWN_FACE_Y,
        SHOWN_FACE_WIDTH,
        SHOWN_FACE_HEIGHT,
        FACE_U,
        FACE_V,
        FACE_WIDTH,
        FACE_HEIGHT,
        ATLAS,
        ATLAS);
    int collar = COLLAR_COLORS[tier.ordinal() % COLLAR_COLORS.length];
    graphics.fill(
        x + COLLAR_X, y + COLLAR_Y, x + COLLAR_X + COLLAR_WIDTH, y + COLLAR_Y + 2, collar);
    graphics.fill(
        x + LIGHT_X, y + LIGHT_Y, x + LIGHT_X + LIGHT_SIZE, y + LIGHT_Y + LIGHT_SIZE, LIGHT_COLOR);
    graphics.fill(x + BASE_X, y + BASE_Y, x + BASE_X + BASE_WIDTH, y + BASE_Y + 2, BASE_COLOR);
  }

  /** Only the normal face blinks, as on the model. */
  private static ResourceLocation face(RobotExpression expression, long tick) {
    String name = lower(expression);
    if (expression == RobotExpression.NORMAL) {
      BlinkFrame frame = BlinkCycle.frameAt(tick);
      name = frame == BlinkFrame.OPEN ? name : BLINK_NAME.formatted(lower(frame));
    }
    return ResourceLocation.fromNamespaceAndPath(
        CubitMod.MOD_ID, FACE_PATH.formatted(CubitConfig.GENERAL_ROBOT.id(), name));
  }

  private static String lower(Enum<?> value) {
    return value.name().toLowerCase(Locale.ROOT);
  }
}
