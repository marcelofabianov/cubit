package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;

/**
 * What the defense tab needs and only changes with the tier, which closes the screen: the modes the
 * tier allows, the tier that first allows each mode and the numbers of the shock.
 */
public record DefenseOpening(
    List<Boolean> allowed,
    List<Integer> unlockTiers,
    int damage,
    int rangeBlocks,
    int intervalTicks,
    long cost,
    boolean hitsAllInRange) {
  /** A mode no tier allows, so the screen says nothing about when it unlocks. */
  public static final int NEVER = -1;

  private static final int MODES = DefenseMode.values().length;

  public DefenseOpening {
    allowed = List.copyOf(allowed);
    unlockTiers = List.copyOf(unlockTiers);
  }

  public boolean allows(DefenseMode mode) {
    return mode.ordinal() < allowed.size() && allowed.get(mode.ordinal());
  }

  public Optional<RobotTier> unlockTier(DefenseMode mode) {
    int tier = mode.ordinal() < unlockTiers.size() ? unlockTiers.get(mode.ordinal()) : NEVER;
    return tier < 0 || tier >= RobotTier.values().length
        ? Optional.empty()
        : Optional.of(RobotTier.values()[tier]);
  }

  public boolean canShock() {
    return damage > 0 && rangeBlocks > 0;
  }

  void write(FriendlyByteBuf data) {
    data.writeCollection(allowed, FriendlyByteBuf::writeBoolean);
    data.writeCollection(unlockTiers, FriendlyByteBuf::writeVarInt);
    data.writeVarInt(damage);
    data.writeVarInt(rangeBlocks);
    data.writeVarInt(intervalTicks);
    data.writeVarLong(cost);
    data.writeBoolean(hitsAllInRange);
  }

  /** Longer lists than the modes are cut, so a bad packet cannot grow the screen. */
  static DefenseOpening read(FriendlyByteBuf data) {
    List<Boolean> allowed = data.readList(FriendlyByteBuf::readBoolean);
    List<Integer> unlock = data.readList(FriendlyByteBuf::readVarInt);
    return new DefenseOpening(
        allowed.subList(0, Math.min(allowed.size(), MODES)),
        unlock.subList(0, Math.min(unlock.size(), MODES)),
        data.readVarInt(),
        data.readVarInt(),
        data.readVarInt(),
        data.readVarLong(),
        data.readBoolean());
  }
}
