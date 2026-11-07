package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ChatRulesTest {
  private static final int MANY_PHRASES = 8;
  private static final int FEW_PHRASES = 6;
  private static final int CHANCE = 33;

  @Test
  void onlyTheActiveRobotThatFollowsChats() {
    assertTrue(ChatRules.runs(true, PowerState.ACTIVE, true));
    assertFalse(ChatRules.runs(false, PowerState.ACTIVE, true));
    assertFalse(ChatRules.runs(true, PowerState.ACTIVE, false));
    assertFalse(ChatRules.runs(true, PowerState.HIBERNATING, true));
    assertFalse(ChatRules.runs(true, PowerState.INACTIVE_SWITCHED_OFF, true));
  }

  @Test
  void itOnlyChatsWithACalmFace() {
    Set<RobotExpression> calm = Set.of(RobotExpression.NORMAL, RobotExpression.HAPPY);
    for (RobotExpression expression : RobotExpression.values()) {
      assertEquals(calm.contains(expression), ChatRules.isCalm(expression));
    }
  }

  @Test
  void onlyTheChangeFromTheNormalFaceBeepsByItself() {
    for (RobotExpression expression : RobotExpression.values()) {
      assertEquals(expression != RobotExpression.NORMAL, ChatRules.needsItsOwnBeep(expression));
    }
  }

  @Test
  void aKilledAnimalIsARemarkByChance() {
    assertTrue(ChatRules.remarksOnAnimal(false, CHANCE - 1, CHANCE));
    assertFalse(ChatRules.remarksOnAnimal(false, CHANCE, CHANCE));
  }

  @Test
  void aPetIsNeverARemark() {
    assertFalse(ChatRules.remarksOnAnimal(true, 0, CHANCE));
  }

  @Test
  void eachWeatherTopicHasEightPhrases() {
    assertEquals(MANY_PHRASES, ChatTopic.DAWN.phrases());
    assertEquals(MANY_PHRASES, ChatTopic.COLD.phrases());
    assertEquals(MANY_PHRASES, ChatTopic.ACHIEVEMENT.phrases());
    assertEquals(FEW_PHRASES, ChatTopic.CHALLENGE.phrases());
  }

  @Test
  void aChallengeHasItsOwnPhrases() {
    assertEquals(ChatTopic.CHALLENGE, ChatTopic.ofAchievement(true));
    assertEquals(ChatTopic.ACHIEVEMENT, ChatTopic.ofAchievement(false));
  }
}
