package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.dock.DockStatus;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * The drawings of the screens, cut from the concept images at four texels per screen pixel, so they
 * stay sharp at any interface scale (answer 690).
 */
final class ScreenSprites {
  private static final String SHEET_PATH = "textures/gui/screen_sprites.png";
  private static final ResourceLocation SHEET =
      ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, SHEET_PATH);
  private static final int SHEET_WIDTH = 512;
  private static final int SHEET_HEIGHT = 256;
  private static final int PICTURE = 136;
  private static final int PICTURE_SHOWN = 34;
  private static final int CHARGING_U = 0;
  private static final int IDLE_U = 136;
  private static final int NO_POWER_U = 272;
  private static final int EMPTY_PLATE_U = 408;
  private static final int BOLT_V = 136;
  private static final int BOLT_WIDTH = 32;
  private static final int BOLT_HEIGHT = 40;
  private static final int BOLT_SHOWN_WIDTH = 8;
  private static final int BOLT_SHOWN_HEIGHT = 10;

  private ScreenSprites() {}

  /** The tower lights up as the dock does: green charging, blue idle and orange without power. */
  static void tower(GuiGraphics graphics, int x, int y, DockStatus status) {
    int u =
        switch (status) {
          case CHARGING, COMPLETE -> CHARGING_U;
          case IDLE -> IDLE_U;
          case OFF, NO_ENERGY -> NO_POWER_U;
        };
    picture(graphics, x, y, u);
  }

  static void emptyPlate(GuiGraphics graphics, int x, int y) {
    picture(graphics, x, y, EMPTY_PLATE_U);
  }

  static void bolt(GuiGraphics graphics, int x, int y) {
    graphics.blit(
        SHEET,
        x,
        y,
        BOLT_SHOWN_WIDTH,
        BOLT_SHOWN_HEIGHT,
        0,
        BOLT_V,
        BOLT_WIDTH,
        BOLT_HEIGHT,
        SHEET_WIDTH,
        SHEET_HEIGHT);
  }

  private static void picture(GuiGraphics graphics, int x, int y, int u) {
    graphics.blit(
        SHEET,
        x,
        y,
        PICTURE_SHOWN,
        PICTURE_SHOWN,
        u,
        0,
        PICTURE,
        PICTURE,
        SHEET_WIDTH,
        SHEET_HEIGHT);
  }
}
