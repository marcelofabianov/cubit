package io.github.marcelofabianov.cubit.domain.robot;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/** The order and the search of the selection screen of the Tablet (answers 1003 and 1005). */
public final class RobotListings {
  private static final int FOLLOWING_GROUP = 0;
  private static final int NEAR_GROUP = 1;
  private static final int OTHER_DIMENSION_GROUP = 2;
  private static final int UNLOADED_GROUP = 3;

  private static final Comparator<RobotListing> ORDER =
      Comparator.comparingInt(RobotListings::group)
          .thenComparingDouble(RobotListings::distanceInGroup)
          .thenComparing(RobotListing::name, String.CASE_INSENSITIVE_ORDER);

  private RobotListings() {}

  /**
   * The Cubit that follows first, then the others by distance, with the ones in other dimensions
   * and in unloaded chunks last.
   */
  public static List<RobotListing> ordered(List<RobotListing> listings) {
    return listings.stream().sorted(ORDER).toList();
  }

  /** A name matches a search that it contains, ignoring case. An empty search keeps everyone. */
  public static boolean matches(String name, String search) {
    return name.toLowerCase(Locale.ROOT).contains(search.strip().toLowerCase(Locale.ROOT));
  }

  private static int group(RobotListing listing) {
    if (listing.following()) {
      return FOLLOWING_GROUP;
    }
    if (!listing.loaded()) {
      return UNLOADED_GROUP;
    }
    return listing.sameDimension() ? NEAR_GROUP : OTHER_DIMENSION_GROUP;
  }

  private static double distanceInGroup(RobotListing listing) {
    return listing.sameDimension() ? listing.distance() : 0;
  }
}
