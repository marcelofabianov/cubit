package io.github.zenndg.cubit.menu;

import io.github.zenndg.cubit.domain.desk.DeskStatus;
import java.util.Locale;

/** The translation keys of the Engineering Desk screen, shared by the screen and the datagen. */
public final class DeskScreenKeys {
  private static final String PREFIX = "gui.cubit.engineering_desk.";
  private static final String STATUS_PREFIX = PREFIX + "status.";
  public static final String TITLE_TIER = PREFIX + "title_tier";
  public static final String PROJECT_TAB = PREFIX + "project";
  public static final String STORAGE_TAB = PREFIX + "storage";
  public static final String INPUT = PREFIX + "input";
  public static final String OUTPUT = PREFIX + "output";
  public static final String STORED_ENERGY = PREFIX + "stored_energy";
  public static final String ORDER = PREFIX + "order";
  public static final String CANCEL = PREFIX + "cancel";
  public static final String LOCKED = PREFIX + "locked";
  public static final String CATALOG = PREFIX + "catalog";
  public static final String NO_RECIPE = PREFIX + "no_recipe";
  public static final String COST = PREFIX + "cost";

  private DeskScreenKeys() {}

  public static String status(DeskStatus status) {
    return STATUS_PREFIX + status.name().toLowerCase(Locale.ROOT);
  }
}
