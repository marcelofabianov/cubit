package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.domain.behavior.SpotReservations;
import io.github.zenndg.cubit.domain.energy.PowerState;
import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.domain.robot.WorkArea;
import io.github.zenndg.cubit.domain.tier.CookSpec;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

/**
 * The work of the Cook in its area: it keeps the marked smokers and furnaces going with raw food
 * and fuel from the chests of the Unload app and takes the cooked food out, one action at a time,
 * with the pause and the cost of its tier (answers 1314 to 1329). The cooked food goes back by the
 * common unload (answer 1315).
 */
public final class RobotCooking implements JobErrands {
  /** The ovens every Cook holds, only in the memory of the server (answer 1323). */
  private static final SpotReservations OVENS = new SpotReservations();

  private static final int STACK = 64;

  private final RobotEntity robot;
  private final CookPlanner planner;
  private CookTask task;
  private long nextLook = Long.MIN_VALUE;
  private int pause;

  RobotCooking(RobotEntity robot) {
    this.robot = robot;
    this.planner = new CookPlanner(robot);
  }

  @Override
  public boolean active() {
    return cook() && robot.workArea().working() && robot.powerState() == PowerState.ACTIVE;
  }

  @Override
  public boolean resting() {
    return pause > 0;
  }

  /** Called on the server on every tick, so a Cook that stopped lets go of its oven. */
  void tick() {
    if (pause > 0) {
      pause--;
    }
    if (!active() && task != null) {
      drop();
    }
  }

  /** It only looks after an action or once per scan interval (rule 21). */
  @Override
  public void plan(WorkArea area) {
    long now = robot.level().getGameTime();
    if (task != null || now < nextLook) {
      return;
    }
    task = planner.next(area).orElse(null);
    if (task == null) {
      nextLook = now + spec().scanIntervalTicks();
    }
  }

  @Override
  public Optional<BlockPos> target() {
    return Optional.ofNullable(task).map(CookTask::pos);
  }

  /** Does the errand, pays for what moved and rests, as the tier says (answer 1326). */
  @Override
  public void act() {
    CookTask done = task;
    drop();
    if (perform(done) > 0) {
      robot.setBattery(spec().payForAction(robot.battery()));
    }
    pause = spec().actionPauseTicks();
    nextLook = Long.MIN_VALUE;
  }

  @Override
  public void giveUp() {
    drop();
  }

  private void drop() {
    OVENS.release(robot.getUUID());
    task = null;
  }

  private int perform(CookTask done) {
    IItemHandler carried = new InvWrapper(robot.inventory());
    return switch (done.kind()) {
      case TAKE -> into(carried, done.pos(), Direction.DOWN, CookPlanner::isFood, false);
      case FEED -> into(carried, done.pos(), Direction.UP, raw(done), true);
      case FUEL -> into(carried, done.pos(), Direction.NORTH, fuel(done), true);
      case FETCH_RAW -> fromChest(carried, done.pos(), raw(done));
      case FETCH_FUEL -> fromChest(carried, done.pos(), fuel(done));
    };
  }

  /** Between the oven, through the face, and what it carries, in either direction (rule 13). */
  private int into(
      IItemHandler carried,
      BlockPos oven,
      Direction face,
      Predicate<ItemStack> wanted,
      boolean put) {
    return OrganizerStock.at(robot.level(), oven, face)
        .map(handler ->
            put
                ? OrganizerStock.move(carried, handler, wanted, STACK)
                : OrganizerStock.move(handler, carried, wanted, STACK))
        .orElse(0);
  }

  private int fromChest(IItemHandler carried, BlockPos chest, Predicate<ItemStack> wanted) {
    return robot
        .docking()
        .unload()
        .handlerAt(chest)
        .map(handler -> OrganizerStock.move(handler, carried, wanted, STACK))
        .orElse(0);
  }

  private Predicate<ItemStack> raw(CookTask done) {
    return done.like().isEmpty()
        ? stack -> CookOvens.isRaw(robot.level(), stack)
        : stack -> ItemStack.isSameItemSameComponents(stack, done.like());
  }

  private static Predicate<ItemStack> fuel(CookTask done) {
    return done.like().isEmpty()
        ? CookOvens::isFuel
        : stack -> ItemStack.isSameItemSameComponents(stack, done.like());
  }

  static boolean reserve(RobotEntity robot, BlockPos oven) {
    return OVENS.reserve(RobotUnload.dimensionOf(robot.level()), oven.asLong(), robot.getUUID());
  }

  static boolean free(RobotEntity robot, BlockPos oven) {
    return OVENS.freeFor(RobotUnload.dimensionOf(robot.level()), oven.asLong(), robot.getUUID());
  }

  private CookSpec spec() {
    return robot.spec().jobs().cook();
  }

  private boolean cook() {
    return robot.workArea().work().job().filter(RobotJob.COOK::equals).isPresent();
  }
}
