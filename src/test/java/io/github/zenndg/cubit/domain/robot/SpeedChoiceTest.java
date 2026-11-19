package io.github.zenndg.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SpeedChoiceTest {
  private static final int MIN = 50;
  private static final int STEP = 10;
  private static final SpeedSteps STEPS = new SpeedSteps(MIN, STEP);
  private static final int ODD_MIN = 55;
  private static final SpeedSteps ODD_STEPS = new SpeedSteps(ODD_MIN, STEP);
  private static final int UP = 1;
  private static final int DOWN = -1;
  private static final int STAY = 0;
  private static final int NINETY = 90;
  private static final int EIGHTY = 80;
  private static final int SIXTY = 60;
  private static final int BETWEEN_STEPS = 84;
  private static final int NEAR_ODD_MIN = 56;
  private static final int BELOW_MIN = 20;
  private static final int TOO_HIGH = 101;
  private static final int TOO_LOW = 0;
  private static final double EIGHTY_FACTOR = 0.8;
  private static final double FULL_FACTOR = 1.0;
  private static final double TOLERANCE = 1e-9;
  private static final boolean SAFE = false;
  private static final boolean IN_DANGER = true;

  /** A Cubit nobody changed walks at the whole speed of its tier (answers 1259 and 1266). */
  @Test
  void startsAtTheWholeSpeedOfTheTier() {
    assertEquals(SpeedChoice.MAX_PERCENT, SpeedChoice.FULL.percent());
  }

  /** The buttons go 10% a step, from the minimum to 100%, and stop at both ends (1259). */
  @Test
  void theButtonsStepBetweenTheMinimumAndTheWholeSpeed() {
    assertEquals(new SpeedChoice(NINETY), SpeedChoice.FULL.step(DOWN, STEPS));
    assertEquals(SpeedChoice.FULL, SpeedChoice.FULL.step(UP, STEPS));
    assertEquals(new SpeedChoice(MIN), new SpeedChoice(MIN).step(DOWN, STEPS));
    assertEquals(new SpeedChoice(SIXTY), new SpeedChoice(MIN).step(UP, STEPS));
    assertEquals(new SpeedChoice(EIGHTY), new SpeedChoice(EIGHTY).step(STAY, STEPS));
  }

  /** A minimum off the steps is still the last step, and + leaves it to the step above. */
  @Test
  void aMinimumOffTheStepsIsTheLastStep() {
    assertEquals(new SpeedChoice(ODD_MIN), new SpeedChoice(SIXTY).step(DOWN, ODD_STEPS));
    assertEquals(new SpeedChoice(SIXTY), new SpeedChoice(ODD_MIN).step(UP, ODD_STEPS));
  }

  /** A pack that changes the config moves a saved value to the nearest valid step (1266). */
  @Test
  void aSavedValueIsFittedToTheNearestStep() {
    assertEquals(new SpeedChoice(EIGHTY), new SpeedChoice(BETWEEN_STEPS).fitted(STEPS));
    assertEquals(new SpeedChoice(MIN), new SpeedChoice(BELOW_MIN).fitted(STEPS));
    assertEquals(new SpeedChoice(ODD_MIN), new SpeedChoice(NEAR_ODD_MIN).fitted(ODD_STEPS));
    assertEquals(SpeedChoice.FULL, SpeedChoice.FULL.fitted(STEPS));
  }

  /** In danger the Cubit flees at the whole speed, because fleeing comes first (1268). */
  @Test
  void theFactorIsWholeInDanger() {
    assertEquals(EIGHTY_FACTOR, new SpeedChoice(EIGHTY).factor(SAFE), TOLERANCE);
    assertEquals(FULL_FACTOR, new SpeedChoice(EIGHTY).factor(IN_DANGER), TOLERANCE);
  }

  @Test
  void rejectsPercentagesOutsideTheRange() {
    assertThrows(IllegalArgumentException.class, () -> new SpeedChoice(TOO_HIGH));
    assertThrows(IllegalArgumentException.class, () -> new SpeedChoice(TOO_LOW));
  }

  @Test
  void rejectsInvalidSteps() {
    assertThrows(IllegalArgumentException.class, () -> new SpeedSteps(TOO_LOW, STEP));
    assertThrows(IllegalArgumentException.class, () -> new SpeedSteps(TOO_HIGH, STEP));
    assertThrows(IllegalArgumentException.class, () -> new SpeedSteps(MIN, TOO_LOW));
  }
}
