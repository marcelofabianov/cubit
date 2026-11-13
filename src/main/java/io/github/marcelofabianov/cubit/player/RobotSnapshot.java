package io.github.marcelofabianov.cubit.player;

import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.behavior.RobotScreenStatus;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * What the Tablet knows of a Cubit: read live while it is loaded, and kept as its last snapshot
 * while its chunk is unloaded (answers 1004 and 1026). The name is empty without a custom name.
 */
public record RobotSnapshot(
    UUID robot,
    UUID owner,
    String name,
    RobotLook look,
    RobotScreenStatus status,
    int batteryPercent,
    BlockPos pos,
    ResourceKey<Level> dimension,
    long gameTime) {
  private static final String ROBOT_KEY = "Robot";
  private static final String OWNER_KEY = "Owner";
  private static final String NAME_KEY = "Name";
  private static final String TIER_KEY = "Tier";
  private static final String COLOR_KEY = "Color";
  private static final String EXPRESSION_KEY = "Expression";
  private static final String STATUS_KEY = "Status";
  private static final String BATTERY_KEY = "Battery";
  private static final String POS_KEY = "Pos";
  private static final String DIMENSION_KEY = "Dimension";
  private static final String TIME_KEY = "Time";

  /** The looks of the portrait: tier, shell color and face. */
  public record RobotLook(RobotTier tier, RobotColor color, RobotExpression expression) {}

  CompoundTag save() {
    CompoundTag tag = new CompoundTag();
    tag.putUUID(ROBOT_KEY, robot);
    tag.putUUID(OWNER_KEY, owner);
    tag.putString(NAME_KEY, name);
    tag.putString(TIER_KEY, look.tier().key());
    tag.putString(COLOR_KEY, look.color().key());
    tag.putString(EXPRESSION_KEY, look.expression().name());
    tag.putString(STATUS_KEY, status.name());
    tag.putInt(BATTERY_KEY, batteryPercent);
    tag.putLong(POS_KEY, pos.asLong());
    tag.putString(DIMENSION_KEY, dimension.location().toString());
    tag.putLong(TIME_KEY, gameTime);
    return tag;
  }

  /** A broken entry is skipped, so one bad value never hides the other Cubits (rule 17). */
  static Optional<RobotSnapshot> load(CompoundTag tag) {
    ResourceLocation dimension = ResourceLocation.tryParse(tag.getString(DIMENSION_KEY));
    Optional<RobotTier> tier = RobotTier.fromKey(tag.getString(TIER_KEY));
    if (!tag.hasUUID(ROBOT_KEY) || !tag.hasUUID(OWNER_KEY) || dimension == null || tier.isEmpty()) {
      return Optional.empty();
    }
    RobotLook look =
        new RobotLook(
            tier.get(),
            RobotColor.fromKey(tag.getString(COLOR_KEY)).orElse(RobotColor.WHITE),
            enumOr(RobotExpression.class, tag.getString(EXPRESSION_KEY), RobotExpression.NORMAL));
    return Optional.of(
        new RobotSnapshot(
            tag.getUUID(ROBOT_KEY),
            tag.getUUID(OWNER_KEY),
            tag.getString(NAME_KEY),
            look,
            enumOr(RobotScreenStatus.class, tag.getString(STATUS_KEY), RobotScreenStatus.OFF),
            tag.getInt(BATTERY_KEY),
            BlockPos.of(tag.getLong(POS_KEY)),
            ResourceKey.create(Registries.DIMENSION, dimension),
            tag.getLong(TIME_KEY)));
  }

  private static <E extends Enum<E>> E enumOr(Class<E> type, String name, E fallback) {
    for (E value : type.getEnumConstants()) {
      if (value.name().equals(name)) {
        return value;
      }
    }
    return fallback;
  }
}
