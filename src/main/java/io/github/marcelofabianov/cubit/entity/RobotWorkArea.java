package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.behavior.RobotScreenStatus;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.AreaCorner;
import io.github.marcelofabianov.cubit.domain.robot.WorkArea;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

/**
 * The area the owner marked with the Area app of the Tablet, where the Cubit works instead of
 * following (answers 1051 to 1064). It is saved with the Cubit and lost when it is picked up.
 */
public final class RobotWorkArea {
  private static final String KEY = "WorkArea";
  private static final String MIN_KEY = "Min";
  private static final String MAX_KEY = "Max";
  private static final String DIMENSION_KEY = "Dimension";

  private final RobotEntity robot;
  private Optional<WorkArea> area = Optional.empty();

  RobotWorkArea(RobotEntity robot) {
    this.robot = robot;
  }

  public Optional<WorkArea> area() {
    return area;
  }

  /** The area in the dimension where the Cubit is, the only one it can work in. */
  public Optional<WorkArea> here() {
    String dimension = robot.level().dimension().location().toString();
    return area.filter(found -> found.isIn(dimension));
  }

  /** Active in its area, so it works there instead of following (answer 1061). */
  public boolean working() {
    return robot.powerState() == PowerState.ACTIVE && here().isPresent();
  }

  /** A new area replaces the old one, and the Cubit gives up the place to follow (1051, 1060). */
  public void assign(WorkArea next, Player owner) {
    area = Optional.of(next);
    RobotFollowing.release(robot, owner);
    robot.getNavigation().stop();
  }

  /** `Follow` takes the Cubit out of its area (answer 1055). */
  public void clear() {
    area = Optional.empty();
  }

  RobotScreenStatus.AreaWork work() {
    return new RobotScreenStatus.AreaWork(working(), robot.robotInventory().isFull());
  }

  void save(CompoundTag tag) {
    area.ifPresent(found -> {
      CompoundTag saved = new CompoundTag();
      saved.putLong(MIN_KEY, asLong(found.min()));
      saved.putLong(MAX_KEY, asLong(found.max()));
      saved.putString(DIMENSION_KEY, found.dimension());
      tag.put(KEY, saved);
    });
  }

  /** A save without the key, or with a broken one, has no area (rule 17). */
  void load(CompoundTag tag) {
    CompoundTag saved = tag.getCompound(KEY);
    String dimension = saved.getString(DIMENSION_KEY);
    area =
        saved.contains(MIN_KEY) && saved.contains(MAX_KEY) && !dimension.isBlank()
            ? Optional.of(
                WorkArea.between(
                    corner(saved.getLong(MIN_KEY)), corner(saved.getLong(MAX_KEY)), dimension))
            : Optional.empty();
  }

  static AreaCorner corner(BlockPos pos) {
    return new AreaCorner(pos.getX(), pos.getY(), pos.getZ());
  }

  private static AreaCorner corner(long packed) {
    return corner(BlockPos.of(packed));
  }

  private static long asLong(AreaCorner corner) {
    return new BlockPos(corner.x(), corner.y(), corner.z()).asLong();
  }
}
