package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.dock.DockStatus;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
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
  private static final int SHEET_HEIGHT = 320;
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

  private static final int ICON_V = 176;
  private static final int CHEST_WIDTH = 64;
  private static final int CHEST_HEIGHT = 52;
  private static final int CHEST_SHOWN_WIDTH = 16;
  private static final int CHEST_SHOWN_HEIGHT = 13;
  private static final int ICON = 64;
  private static final int ICON_SHOWN = 16;
  private static final int GEAR_U = 64;
  private static final int FIRST_FUNCTION_U = 128;

  /** The shield of the defense tab, in the free place after the chatter icon. */
  private static final int SHIELD_U = 320;

  /** The Power button and the solar switch, in the free places after the shield. */
  private static final int POWER_U = 384;

  private static final int SOLAR_U = 448;

  /** The icon of each switch, in the order of the buttons: lamp, scanners, chatter and solar. */
  private static final int[] FUNCTION_U = {
    FIRST_FUNCTION_U, FIRST_FUNCTION_U + ICON, FIRST_FUNCTION_U + 2 * ICON, SOLAR_U
  };

  private static final int POWER_SHOWN = 12;

  /** The funnel of the behavior tab, in the row added below the icons (answer 897). */
  private static final int FUNNEL_U = 0;

  private static final int FUNNEL_V = 256;

  /** The icons of the dock tabs, after the funnel, from prompts (answers 900 and 957). */
  private static final int DOCK_TAB_U = 64;

  private static final int STORAGE_TAB_U = 128;

  /** The torch of the settings tab, after the dock tabs, from a prompt (answer 982). */
  private static final int TORCHES_U = 192;

  /** The magnet and fetch switches, after the torch, from prompts (answer 1039). */
  private static final int MAGNET_U = 256;

  private static final int FETCH_U = 320;

  private ScreenSprites() {}

  static void chest(GuiGraphics graphics, int x, int y) {
    graphics.blit(
        SHEET,
        x,
        y,
        CHEST_SHOWN_WIDTH,
        CHEST_SHOWN_HEIGHT,
        0,
        ICON_V,
        CHEST_WIDTH,
        CHEST_HEIGHT,
        SHEET_WIDTH,
        SHEET_HEIGHT);
  }

  static void gear(GuiGraphics graphics, int x, int y) {
    icon(graphics, x, y, GEAR_U);
  }

  static void shield(GuiGraphics graphics, int x, int y) {
    icon(graphics, x, y, SHIELD_U);
  }

  static void funnel(GuiGraphics graphics, int x, int y) {
    graphics.blit(
        SHEET,
        x,
        y,
        ICON_SHOWN,
        ICON_SHOWN,
        FUNNEL_U,
        FUNNEL_V,
        ICON,
        ICON,
        SHEET_WIDTH,
        SHEET_HEIGHT);
  }

  static void dockTab(GuiGraphics graphics, int x, int y) {
    rowIcon(graphics, x, y, DOCK_TAB_U);
  }

  static void storageTab(GuiGraphics graphics, int x, int y) {
    rowIcon(graphics, x, y, STORAGE_TAB_U);
  }

  static void torches(GuiGraphics graphics, int x, int y) {
    rowIcon(graphics, x, y, TORCHES_U);
  }

  /**
   * The icon of a switch of the robot, by the button the server knows it by, so every tab that
   * lists the switches shows the same icon.
   */
  static void switchIcon(GuiGraphics graphics, int x, int y, int button) {
    switch (button) {
      case RobotMenu.PICKUP_BUTTON -> funnel(graphics, x, y);
      case RobotMenu.MAGNET_BUTTON -> rowIcon(graphics, x, y, MAGNET_U);
      case RobotMenu.FETCH_BUTTON -> rowIcon(graphics, x, y, FETCH_U);
      case RobotMenu.TORCHES_BUTTON -> torches(graphics, x, y);
      default -> function(graphics, x, y, button);
    }
  }

  /** An icon in the row added below the others, where the funnel is. */
  private static void rowIcon(GuiGraphics graphics, int x, int y, int u) {
    graphics.blit(
        SHEET, x, y, ICON_SHOWN, ICON_SHOWN, u, FUNNEL_V, ICON, ICON, SHEET_WIDTH, SHEET_HEIGHT);
  }

  /** The lamp, the scanners, the chatter and the solar panel, in the order of their buttons. */
  static void function(GuiGraphics graphics, int x, int y, int button) {
    icon(graphics, x, y, FUNCTION_U[button]);
  }

  static void power(GuiGraphics graphics, int x, int y) {
    graphics.blit(
        SHEET,
        x,
        y,
        POWER_SHOWN,
        POWER_SHOWN,
        POWER_U,
        ICON_V,
        ICON,
        ICON,
        SHEET_WIDTH,
        SHEET_HEIGHT);
  }

  private static void icon(GuiGraphics graphics, int x, int y, int u) {
    graphics.blit(
        SHEET, x, y, ICON_SHOWN, ICON_SHOWN, u, ICON_V, ICON, ICON, SHEET_WIDTH, SHEET_HEIGHT);
  }

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
