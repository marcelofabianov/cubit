package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class RobotTypeTest {
  private static final String GENERAL = "general";
  private static final String MINER = "miner";
  private static final String SNAKE_CASE_ID = "deep_miner_2";
  private static final String EMPTY = "";
  private static final String UPPERCASE = "General";
  private static final String WITH_SPACE = "deep miner";
  private static final String STARTS_WITH_DIGIT = "2_miner";

  @Test
  void isIdentifiedByItsId() {
    assertEquals(new RobotType(GENERAL), new RobotType(GENERAL));
    assertNotEquals(new RobotType(GENERAL), new RobotType(MINER));
  }

  @Test
  void acceptsLowercaseSnakeCaseIds() {
    assertEquals(SNAKE_CASE_ID, new RobotType(SNAKE_CASE_ID).id());
  }

  @Test
  void rejectsIdsThatCannotBeUsedInConfigKeysOrRegistryNames() {
    assertThrows(IllegalArgumentException.class, () -> new RobotType(EMPTY));
    assertThrows(IllegalArgumentException.class, () -> new RobotType(UPPERCASE));
    assertThrows(IllegalArgumentException.class, () -> new RobotType(WITH_SPACE));
    assertThrows(IllegalArgumentException.class, () -> new RobotType(STARTS_WITH_DIGIT));
  }
}
