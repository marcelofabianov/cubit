package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.robot.TabletApp;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.item.TabletItem;
import io.github.marcelofabianov.cubit.network.RobotListPayload;
import io.github.marcelofabianov.cubit.player.TabletSelection;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The Tablet: its item, its list and the choice of a Cubit (answers 998 to 1026). */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class TabletGameTests {
  private static final String ARENA = "arena";
  private static final BlockPos FIRST = new BlockPos(1, 1, 1);
  private static final BlockPos SECOND = new BlockPos(5, 1, 1);
  private static final String WRONG_LIST = "The Tablet listed the wrong Cubits";
  private static final String WRONG_CHOICE = "The Tablet chose the wrong Cubit";
  private static final String WRONG_APP = "A new Tablet should open";

  private TabletGameTests() {}

  @GameTest(template = ARENA)
  public static void aNewTabletOpensAndDoesNotStack(GameTestHelper helper) {
    ItemStack tablet = new ItemStack(CubitItems.TABLET.get());

    helper.assertValueEqual(TabletItem.appOf(tablet), TabletApp.OPEN, WRONG_APP);
    helper.assertValueEqual(tablet.getMaxStackSize(), 1, WRONG_APP);
    helper.succeed();
  }

  /** Only the Cubits of the player, with the one that follows first (answers 1003 and 1004). */
  @GameTest(template = ARENA)
  public static void theListHasOnlyTheCubitsOfThePlayer(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    ServerPlayer stranger = GameTestRobots.onlinePlayer(helper);
    RobotEntity first = GameTestRobots.placeNewRobot(helper, owner, FIRST);
    GameTestRobots.placeNewRobot(helper, stranger, SECOND);

    List<UUID> listed = ids(TabletSelection.entriesFor(owner));

    helper.assertValueEqual(listed, List.of(first.getUUID()), WRONG_LIST);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void onlyAnOwnCubitCanBeChosen(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    ServerPlayer stranger = GameTestRobots.onlinePlayer(helper);
    RobotEntity mine = GameTestRobots.placeNewRobot(helper, owner, FIRST);
    RobotEntity theirs = GameTestRobots.placeNewRobot(helper, stranger, SECOND);

    TabletSelection.choose(owner, theirs.getUUID());
    helper.assertValueEqual(chosen(owner), Optional.empty(), WRONG_CHOICE);
    TabletSelection.choose(owner, mine.getUUID());
    helper.assertValueEqual(chosen(owner), Optional.of(mine.getUUID()), WRONG_CHOICE);
    helper.succeed();
  }

  /** The Tablet comes first: a click with it on an own Cubit chooses it (answer 1007). */
  @GameTest(template = ARENA)
  public static void aClickWithTheTabletChoosesTheCubit(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FIRST);
    owner.moveTo(robot.getX(), robot.getY(), robot.getZ());

    owner.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(CubitItems.TABLET.get()));
    owner.interactOn(robot, InteractionHand.MAIN_HAND);

    helper.assertValueEqual(chosen(owner), Optional.of(robot.getUUID()), WRONG_CHOICE);
    helper.succeed();
  }

  /** An unloaded Cubit stays in the list by its snapshot (answers 1004 and 1026). */
  @GameTest(template = ARENA)
  public static void anUnloadedCubitStaysInTheList(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FIRST);
    UUID id = robot.getUUID();

    robot.remove(Entity.RemovalReason.UNLOADED_TO_CHUNK);

    List<RobotListPayload.Entry> entries = TabletSelection.entriesFor(owner);
    helper.assertValueEqual(ids(entries), List.of(id), WRONG_LIST);
    helper.assertFalse(entries.getFirst().where().loaded(), WRONG_LIST);
    helper.succeed();
  }

  /** A picked up or destroyed Cubit leaves the list and the choice (answer 1009). */
  @GameTest(template = ARENA)
  public static void aDiscardedCubitLeavesTheListAndTheChoice(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FIRST);
    TabletSelection.choose(owner, robot.getUUID());

    robot.discard();

    helper.assertTrue(TabletSelection.entriesFor(owner).isEmpty(), WRONG_LIST);
    helper.assertValueEqual(chosen(owner), Optional.empty(), WRONG_CHOICE);
    helper.succeed();
  }

  private static Optional<UUID> chosen(ServerPlayer player) {
    return player.getData(CubitAttachments.CHOSEN_ROBOT).robot();
  }

  private static List<UUID> ids(List<RobotListPayload.Entry> entries) {
    return entries.stream().map(RobotListPayload.Entry::robot).toList();
  }
}
