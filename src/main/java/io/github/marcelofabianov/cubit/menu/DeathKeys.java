package io.github.marcelofabianov.cubit.menu;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * The translation keys of the death point: the texts of the Cubit, the Deaths tab of the Tablet and
 * the names of the dimensions (answers 1111 and 1127). They are a contract with whoever translates
 * the mod.
 */
public final class DeathKeys {
  private static final String NOTICE = "notice.cubit.";
  private static final String TAB = "gui.cubit.tablet.deaths.";
  private static final String DIMENSION = "dimension.cubit.";
  private static final String DIMENSION_SEPARATOR = ".";

  public static final String NOTICE_FELL = NOTICE + "death_point_fell";
  public static final String NOTICE_ARRIVED = NOTICE + "death_point_arrived";
  public static final String NOTICE_STUCK = NOTICE + "death_point_stuck";

  public static final String TAB_TITLE = TAB + "title";
  public static final String EMPTY = TAB + "empty";
  public static final String WHEN = TAB + "when";
  public static final String WHERE = TAB + "where";
  public static final String DAY_TIME = TAB + "day_time";
  public static final String COORDINATES = TAB + "coordinates";
  public static final String GUIDE = TAB + "guide";
  public static final String CANCEL = TAB + "cancel";
  public static final String DELETE = TAB + "delete";
  public static final String GUIDE_DETAIL = TAB + "guide_detail";
  public static final String CANCEL_DETAIL = TAB + "cancel_detail";
  public static final String DELETE_DETAIL = TAB + "delete_detail";
  public static final String SWITCHED_OFF = TAB + "switched_off";
  public static final String DEATH_ELSEWHERE = TAB + "death_elsewhere";
  public static final String CUBIT_ELSEWHERE = TAB + "cubit_elsewhere";
  public static final String NOT_FOLLOWING = TAB + "not_following";

  /** The dimensions of the game. Another dimension shows its own name, as saved. */
  public static final String OVERWORLD = DIMENSION + "minecraft.overworld";

  public static final String NETHER = DIMENSION + "minecraft.the_nether";
  public static final String END = DIMENSION + "minecraft.the_end";

  private DeathKeys() {}

  /** The name of a dimension by its ID, as saved in the death, with the ID as the fallback. */
  public static Component dimension(String id) {
    ResourceLocation location = ResourceLocation.tryParse(id);
    if (location == null) {
      return Component.literal(id);
    }
    String key = DIMENSION + location.getNamespace() + DIMENSION_SEPARATOR + location.getPath();
    return Component.translatableWithFallback(key, location.getPath());
  }
}
