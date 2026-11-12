package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FocusTargetTest {
  private static final UUID ZOMBIE = new UUID(1, 1);
  private static final UUID SKELETON = new UUID(2, 2);
  private static final int FORGET_BLOCKS = 32;
  private static final double NEAR = 10;
  private static final double FAR = 33;
  private static final double AT_THE_LIMIT = 32;

  @Test
  void noTargetAtFirst() {
    assertFalse(FocusTarget.NONE.hasTarget());
    assertFalse(FocusTarget.NONE.is(ZOMBIE));
  }

  @Test
  void aHostileOrNeutralMobHurtByTheOwnerBecomesTheTarget() {
    FocusTarget target = FocusTarget.NONE.hurtByOwner(ZOMBIE, ShockCandidate.HOSTILE_MOB);

    assertTrue(target.is(ZOMBIE));
    assertTrue(FocusTarget.NONE.hurtByOwner(ZOMBIE, ShockCandidate.NEUTRAL_MOB).is(ZOMBIE));
  }

  @Test
  void whatIsNeverShockedNeverBecomesTheTarget() {
    FocusTarget target = FocusTarget.NONE.hurtByOwner(ZOMBIE, ShockCandidate.HOSTILE_MOB);

    for (ShockCandidate kind :
        new ShockCandidate[] {
          ShockCandidate.OWNER,
          ShockCandidate.PLAYER,
          ShockCandidate.ROBOT,
          ShockCandidate.PEACEFUL_MOB
        }) {
      assertEquals(target, target.hurtByOwner(SKELETON, kind));
    }
  }

  @Test
  void aNewMobHurtByTheOwnerTakesThePlace() {
    FocusTarget target =
        FocusTarget.NONE
            .hurtByOwner(ZOMBIE, ShockCandidate.HOSTILE_MOB)
            .hurtByOwner(SKELETON, ShockCandidate.HOSTILE_MOB);

    assertTrue(target.is(SKELETON));
    assertFalse(target.is(ZOMBIE));
  }

  @Test
  void theTargetStaysWhileAliveInTheDimensionAndNearTheOwner() {
    FocusTarget target = FocusTarget.NONE.hurtByOwner(ZOMBIE, ShockCandidate.HOSTILE_MOB);

    assertEquals(target, target.followed(Optional.of(NEAR), FORGET_BLOCKS));
    assertEquals(target, target.followed(Optional.of(AT_THE_LIMIT), FORGET_BLOCKS));
  }

  @Test
  void theTargetEndsWhenFarFromTheOwner() {
    FocusTarget target = FocusTarget.NONE.hurtByOwner(ZOMBIE, ShockCandidate.HOSTILE_MOB);

    assertFalse(target.followed(Optional.of(FAR), FORGET_BLOCKS).hasTarget());
  }

  @Test
  void theTargetEndsWhenDeadOrInAnotherDimension() {
    FocusTarget target = FocusTarget.NONE.hurtByOwner(ZOMBIE, ShockCandidate.HOSTILE_MOB);

    assertFalse(target.followed(Optional.empty(), FORGET_BLOCKS).hasTarget());
  }

  @Test
  void onlyANewTargetCountsAsAChange() {
    FocusTarget zombie = FocusTarget.NONE.hurtByOwner(ZOMBIE, ShockCandidate.HOSTILE_MOB);

    assertTrue(zombie.changedFrom(FocusTarget.NONE));
    assertFalse(zombie.hurtByOwner(ZOMBIE, ShockCandidate.HOSTILE_MOB).changedFrom(zombie));
    assertFalse(FocusTarget.NONE.changedFrom(zombie));
  }
}
