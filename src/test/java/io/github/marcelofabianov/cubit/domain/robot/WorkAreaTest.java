package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.tier.AreaSpec;
import org.junit.jupiter.api.Test;

class WorkAreaTest {
  private static final String OVERWORLD = "minecraft:overworld";
  private static final String NETHER = "minecraft:the_nether";
  private static final String BLANK = " ";
  private static final AreaCorner LOW = new AreaCorner(10, 64, -5);
  private static final AreaCorner HIGH = new AreaCorner(25, 71, 10);
  private static final WorkArea AREA = WorkArea.between(HIGH, LOW, OVERWORLD);
  private static final int SIDE = 16;
  private static final int HEIGHT = 8;
  private static final AreaSpec ADVANCED = new AreaSpec(SIDE, HEIGHT);
  private static final AreaSpec TOO_LOW = new AreaSpec(SIDE, HEIGHT - 1);
  private static final int LAST_CORNER = 3;
  private static final int AROUND = 4;
  private static final double INSIDE_X = 12.5;
  private static final double ON_TOP_Y = 72.0;
  private static final double ABOVE_Y = 73.0;
  private static final double INSIDE_Z = 0.5;
  private static final double PAST_X = 26.0;
  private static final int REACH = 24;
  private static final int SHORT_REACH = 4;

  @Test
  void theCornersGoInAnyOrderAndMakeAVolume() {
    assertEquals(LOW, AREA.min());
    assertEquals(HIGH, AREA.max());
    assertEquals(SIDE, AREA.width());
    assertEquals(HEIGHT, AREA.height());
    assertEquals(SIDE, AREA.depth());
  }

  @Test
  void itFitsATierByItsBaseAndItsHeight() {
    assertTrue(AREA.fits(ADVANCED));
    assertFalse(AREA.fits(TOO_LOW));
    assertFalse(AREA.fits(AreaSpec.NONE));
  }

  /** One block over the top holds the items and the Cubit standing on the marked blocks. */
  @Test
  void itHoldsTheBlockOverItsTop() {
    assertTrue(AREA.contains(INSIDE_X, ON_TOP_Y, INSIDE_Z));
    assertFalse(AREA.contains(INSIDE_X, ABOVE_Y, INSIDE_Z));
    assertFalse(AREA.contains(PAST_X, ON_TOP_Y, INSIDE_Z));
  }

  @Test
  void theRoundGoesAroundTheCornersOfTheBase() {
    assertEquals(new AreaCorner(LOW.x(), LOW.y(), LOW.z()), AREA.corner(0));
    assertEquals(new AreaCorner(LOW.x(), LOW.y(), HIGH.z()), AREA.corner(LAST_CORNER));
    assertEquals(AREA.corner(0), AREA.corner(AROUND));
    assertEquals(AREA.corner(LAST_CORNER), AREA.corner(-1));
  }

  @Test
  void theCenterIsWithinTheReachOfAPlace() {
    assertTrue(AREA.centerWithin(LOW.x(), LOW.z(), REACH));
    assertFalse(AREA.centerWithin(LOW.x(), LOW.z(), SHORT_REACH));
  }

  @Test
  void itKnowsItsDimension() {
    assertTrue(AREA.isIn(OVERWORLD));
    assertFalse(AREA.isIn(NETHER));
  }

  @Test
  void anAreaNeedsADimensionAndCornersInOrder() {
    assertThrows(IllegalArgumentException.class, () -> new WorkArea(HIGH, LOW, OVERWORLD));
    assertThrows(IllegalArgumentException.class, () -> new WorkArea(LOW, HIGH, BLANK));
  }
}
