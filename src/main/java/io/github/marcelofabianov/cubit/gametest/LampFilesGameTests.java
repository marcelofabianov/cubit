package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.BlinkFrame;
import io.github.marcelofabianov.cubit.domain.behavior.GazeDirection;
import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.locale.Language;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** What the lamp needs on the client: the server only checks that the files were packed. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class LampFilesGameTests {
  private static final String ARENA = "arena";
  private static final String ENTITY = "/assets/cubit/textures/entity/general_robot";
  private static final String LIGHT = "/assets/cubit/dynamiclights/entity/general_robot.json";
  private static final String TIP_GLOW = ENTITY + "_antenna/glow.png";
  private static final String HALO = ENTITY + "_antenna/halo.png";
  private static final String SCREEN_GLOW = ENTITY + "_screen_glow/%s.png";
  private static final String EYES_NAME = "normal_%s_%s";
  private static final String MISSING_FILE = "A file of the lamp is missing: ";
  private static final String MISSING_TEXT = "A text of the lamp is missing: ";
  private static final String UNREADABLE_FILE = "A file of the lamp cannot be read: ";

  private LampFilesGameTests() {}

  @GameTest(template = ARENA)
  public static void theFilesAndTextsOfTheLampExist(GameTestHelper helper) {
    for (String file : List.of(LIGHT, TIP_GLOW, HALO)) {
      assertFileExists(helper, file);
    }
    for (String key : List.of(ScreenKeys.LAMP, ScreenKeys.SWITCH_ON, ScreenKeys.SWITCH_OFF)) {
      helper.assertTrue(Language.getInstance().has(key), MISSING_TEXT + key);
    }
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void everyPictureOfTheLitScreenHasItsGlow(GameTestHelper helper) {
    for (RobotExpression expression : RobotExpression.values()) {
      if (expression.glows()) {
        assertFileExists(helper, SCREEN_GLOW.formatted(lower(expression)));
      }
    }
    for (GazeDirection gaze : GazeDirection.values()) {
      for (BlinkFrame frame : BlinkFrame.values()) {
        // The open eyes looking straight ahead are the normal expression itself.
        if (gaze != GazeDirection.LEVEL || frame != BlinkFrame.OPEN) {
          String name = EYES_NAME.formatted(lower(gaze), lower(frame));
          assertFileExists(helper, SCREEN_GLOW.formatted(name));
        }
      }
    }
    helper.succeed();
  }

  private static String lower(Enum<?> value) {
    return value.name().toLowerCase(Locale.ROOT);
  }

  private static void assertFileExists(GameTestHelper helper, String file) {
    try (InputStream stream = LampFilesGameTests.class.getResourceAsStream(file)) {
      helper.assertTrue(stream != null, MISSING_FILE + file);
    } catch (IOException exception) {
      throw new IllegalStateException(UNREADABLE_FILE + file, exception);
    }
  }
}
