package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.domain.behavior.SpotReservations;
import io.github.zenndg.cubit.domain.energy.PowerState;
import io.github.zenndg.cubit.domain.ownership.Ownership;
import io.github.zenndg.cubit.domain.robot.OrganizerMode;
import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.domain.robot.WorkArea;
import io.github.zenndg.cubit.domain.tier.OrganizerSpec;
import io.github.zenndg.cubit.menu.OrganizerControl;
import io.github.zenndg.cubit.menu.SupplyRow;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

/**
 * The work of the Organizer in its area: one mode at a time, it organizes what machines give out
 * and what lies on the ground, or supplies the marked destinations, one action at a time, with the
 * pause and the cost of its tier (answers 1285 to 1304).
 */
public final class RobotOrganizing implements OrganizerControl, JobErrands {
  /** The blocks and the items every Organizer holds, only in the memory of the server. */
  static final SpotReservations SPOTS = new SpotReservations();

  private final RobotEntity robot;
  private final RobotMarks marks;
  private final OrganizerPlanner planner;
  private OrganizerMode mode = OrganizerMode.INITIAL;
  private OrganizerTask task;
  private int pause;

  RobotOrganizing(RobotEntity robot, RobotMarks marks) {
    this.robot = robot;
    this.marks = marks;
    this.planner = new OrganizerPlanner(robot, this);
  }

  /** The card of the Organizer, active in its area: the work that replaces the round. */
  @Override
  public boolean active() {
    return organizer() && robot.workArea().working() && robot.powerState() == PowerState.ACTIVE;
  }

  /** Whether its own storing replaces the common unload, which takes no rule (answer 1303). */
  boolean storesItself() {
    return organizer() && robot.workArea().working();
  }

  public OrganizerMode mode() {
    return mode;
  }

  void setMode(OrganizerMode next) {
    mode = next;
  }

  /** The mode, only with the card of the Organizer, for the line Mode of the Work tab. */
  @Override
  public Optional<OrganizerMode> organizerMode() {
    return organizer() ? Optional.of(mode) : Optional.empty();
  }

  @Override
  public List<SupplyRow> rows() {
    return marks.targets().stream()
        .map(target ->
            new SupplyRow(
                target.pos(), target.mark().face(), target.wanted(), target.amount().count()))
        .toList();
  }

  @Override
  public int limit() {
    return marks.limit();
  }

  @Override
  public boolean place(Player player, int index, ItemStack carried) {
    return marks.setWanted(player, index, carried.isEmpty() ? ItemStack.EMPTY : carried);
  }

  @Override
  public boolean step(Player player, int index, int direction, boolean big) {
    return marks.stepAmount(player, index, direction, big);
  }

  /** The line Mode of the Work tab, only for the owner (answer 1290 and rule 20). */
  @Override
  public boolean switchMode(Player player) {
    if (!Ownership.owns(robot.ownerId(), player.getUUID())) {
      return false;
    }
    mode = mode.next();
    drop();
    return true;
  }

  @Override
  public Optional<BlockPos> target() {
    return Optional.ofNullable(task).map(OrganizerTask::pos);
  }

  @Override
  public boolean resting() {
    return pause > 0;
  }

  /** Called on the server on every tick, so an Organizer that stopped lets go of its task. */
  void tick() {
    if (pause > 0) {
      pause--;
    }
    if (!active() && task != null) {
      drop();
    }
  }

  /** Takes the next task, when there is none (rule 21: it only looks when it is time). */
  @Override
  public void plan(WorkArea area) {
    if (task == null) {
      task = planner.next(area).orElse(null);
    }
  }

  /** Does the task, pays for what moved and rests, as the tier says (answer 1300). */
  @Override
  public void act() {
    OrganizerTask done = task;
    drop();
    if (perform(done) > 0) {
      robot.setBattery(spec().payForAction(robot.battery()));
    }
    pause = spec().actionPauseTicks();
    planner.lookNow();
  }

  /** A place it cannot reach is left until the next look. */
  @Override
  public void giveUp() {
    drop();
  }

  void drop() {
    SPOTS.release(robot.getUUID());
    task = null;
  }

  private int perform(OrganizerTask done) {
    IItemHandler carried = new InvWrapper(robot.inventory());
    return switch (done.kind()) {
      case STORE -> planner.store(done.pos(), carried);
      case TAKE_OUTPUT ->
          OrganizerStock.at(robot.level(), done.pos(), Direction.DOWN)
              .map(machine ->
                  OrganizerStock.move(machine, carried, stack -> true, Integer.MAX_VALUE))
              .orElse(0);
      case PICK_UP -> pickUp(done.target(), carried);
      case FETCH -> planner.fetch(done, carried);
      case DELIVER -> planner.deliver(done, carried);
    };
  }

  /** Takes what fits of an item on the ground, and the rest stays there (rule 13). */
  private int pickUp(int id, IItemHandler carried) {
    if (!(robot.level().getEntity(id) instanceof ItemEntity item) || !item.isAlive()) {
      return 0;
    }
    ItemStack stack = item.getItem();
    ItemStack left = ItemHandlerHelper.insertItemStacked(carried, stack.copy(), false);
    int moved = stack.getCount() - left.getCount();
    if (left.isEmpty()) {
      item.discard();
    } else {
      item.setItem(left);
    }
    return moved;
  }

  OrganizerSpec spec() {
    return robot.spec().jobs().organizer();
  }

  private boolean organizer() {
    return robot.workArea().work().job().filter(RobotJob.ORGANIZER::equals).isPresent();
  }
}
