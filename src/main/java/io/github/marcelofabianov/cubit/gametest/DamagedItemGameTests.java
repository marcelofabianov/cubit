package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Nothing destroys a damaged robot, and a toolbox repairs it. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DamagedItemGameTests {
  private static final String ARENA = "arena";
  private static final int FLOOR_Y = 1;
  private static final int START_X = 1;
  private static final int MIDDLE_Z = 2;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos ABOVE_FLOOR = FLOOR.above();
  private static final int SHORT_DELAY = 5;
  private static final int LONG_DELAY = 20;
  private static final int VOID_DELAY = 80;
  private static final int VOID_TIMEOUT = 120;
  private static final int SHORT_LIFESPAN = 5;
  private static final float HUGE_DAMAGE = 1000f;
  private static final float REPAIRED_HEALTH = 6.25f;
  private static final float SAVED_HEALTH = 10f;
  private static final Energy SAVED_ENERGY = new Energy(5_000);
  private static final int ROBOT_STACK = 1;
  private static final int TOOLBOX_STACK = 16;
  private static final int THREE_BY_THREE = 3;
  private static final double BLOCK_CENTER = 0.5;
  private static final double CLOSE_ENOUGH = 1;
  private static final String ITEM_HURT = "Something hurt the damaged robot";
  private static final String ITEM_GONE = "The damaged robot was destroyed";
  private static final String NOT_RESCUED = "The item did not return to the safe position";
  private static final String NOT_DELIVERED = "The owner did not receive the item";
  private static final String PLACED = "A damaged robot was placed";
  private static final String WRONG_STACK = "Wrong stack rules";
  private static final String FITS = "The damaged robot fits inside a container";
  private static final String SPAWN_LOADED_BATCH = "spawn_loaded";
  private static final String SPAWN_UNLOADED_BATCH = "spawn_unloaded";
  private static final int RETRY_DELAY = 25;
  private static final int FAR_AWAY = 20_000_000;
  private static final BlockPos UNLOADED_POSITION = new BlockPos(FAR_AWAY, 0, FAR_AWAY);
  private static final String NOT_AT_SPAWN = "The item did not go to the world spawn";
  private static final String NOT_WAITING = "The item did not wait below the world";
  private static final String NOT_RETRIED = "The waiting item did not try again";
  private static final String NO_RECIPE = "The recipe did not match";
  private static final String BAD_RECIPE = "The recipe matched the wrong items";
  private static final String WRONG_RESULT = "Wrong recipe result";

  private DamagedItemGameTests() {}

  @GameTest(template = ARENA)
  public static void nothingDestroysTheItem(GameTestHelper helper) {
    ItemEntity entity = dropAt(helper, damagedStack());
    DamageSources sources = helper.getLevel().damageSources();

    helper.assertFalse(entity.hurt(sources.explosion(null, null), HUGE_DAMAGE), ITEM_HURT);
    helper.assertFalse(entity.hurt(sources.inFire(), HUGE_DAMAGE), ITEM_HURT);
    helper.assertFalse(entity.hurt(sources.lava(), HUGE_DAMAGE), ITEM_HURT);
    helper.assertFalse(entity.hurt(sources.cactus(), HUGE_DAMAGE), ITEM_HURT);
    helper.assertTrue(entity.isAlive(), ITEM_GONE);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theItemDoesNotExpire(GameTestHelper helper) {
    ItemEntity entity = dropAt(helper, damagedStack());
    entity.lifespan = SHORT_LIFESPAN;

    helper.runAfterDelay(
        LONG_DELAY,
        () -> {
          helper.assertTrue(entity.isAlive(), ITEM_GONE);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void theVoidReturnsTheItemToTheSafePosition(GameTestHelper helper) {
    ServerLevel level = helper.getLevel();
    BlockPos safe = helper.absolutePos(ABOVE_FLOOR);
    ItemStack stack = damagedStack();
    stack.set(CubitDataComponents.LAST_SAFE_POSITION.get(), GlobalPos.of(level.dimension(), safe));
    ItemEntity entity = dropAt(helper, stack);
    dropIntoTheVoid(level, entity);

    helper.runAfterDelay(
        SHORT_DELAY,
        () -> {
          helper.assertTrue(entity.isAlive(), ITEM_GONE);
          double distance = entity.position().distanceTo(Vec3.atBottomCenterOf(safe));
          helper.assertTrue(distance < CLOSE_ENOUGH, NOT_RESCUED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void theVoidDeliversTheItemToTheOnlineOwner(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    ItemStack stack = damagedStack();
    stack.set(CubitDataComponents.ROBOT_OWNER.get(), owner.getUUID());
    ItemEntity entity = dropAt(helper, stack);
    dropIntoTheVoid(helper.getLevel(), entity);

    helper.runAfterDelay(
        SHORT_DELAY,
        () -> {
          helper.assertTrue(
              owner.getInventory().items.stream()
                  .anyMatch(item -> item.is(CubitItems.DAMAGED_GENERAL_ROBOT.get())),
              NOT_DELIVERED);
          helper.assertFalse(entity.isAlive(), NOT_DELIVERED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, batch = SPAWN_LOADED_BATCH)
  public static void withoutAnyOtherDestinationTheItemGoesToTheLoadedSpawn(GameTestHelper helper) {
    ServerLevel level = helper.getLevel();
    BlockPos spawn = helper.absolutePos(ABOVE_FLOOR);
    BlockPos before = setWorldSpawn(level, spawn);
    ItemEntity entity = dropAt(helper, damagedStack());
    dropIntoTheVoid(level, entity);

    helper.runAfterDelay(
        SHORT_DELAY,
        () -> {
          setWorldSpawn(level, before);
          helper.assertTrue(entity.isAlive(), ITEM_GONE);
          helper.assertTrue(
              entity.position().distanceTo(Vec3.atBottomCenterOf(spawn)) < CLOSE_ENOUGH,
              NOT_AT_SPAWN);
          helper.succeed();
        });
  }

  /** An unloaded spawn is not loaded by force: the item waits below the world and tries again. */
  @GameTest(template = ARENA, batch = SPAWN_UNLOADED_BATCH, timeoutTicks = VOID_TIMEOUT)
  public static void withoutAnyDestinationTheItemWaitsAndTriesAgain(GameTestHelper helper) {
    ServerLevel level = helper.getLevel();
    BlockPos before = setWorldSpawn(level, UNLOADED_POSITION);
    ItemEntity entity = dropAt(helper, damagedStack());
    dropIntoTheVoid(level, entity);
    BlockPos loadedSpawn = helper.absolutePos(ABOVE_FLOOR);

    helper.runAfterDelay(
        VOID_DELAY,
        () -> {
          helper.assertTrue(entity.isAlive(), ITEM_GONE);
          helper.assertTrue(entity.isNoGravity(), NOT_WAITING);
          helper.assertTrue(entity.getY() < level.getMinBuildHeight(), NOT_WAITING);
          setWorldSpawn(level, loadedSpawn);
        });
    helper.runAfterDelay(
        VOID_DELAY + RETRY_DELAY,
        () -> {
          setWorldSpawn(level, before);
          helper.assertTrue(
              entity.position().distanceTo(Vec3.atBottomCenterOf(loadedSpawn)) < CLOSE_ENOUGH,
              NOT_RETRIED);
          helper.assertFalse(entity.isNoGravity(), NOT_RETRIED);
          helper.succeed();
        });
  }

  /** Changes only the stored position: the game's own setter would add a chunk ticket for it. */
  private static BlockPos setWorldSpawn(ServerLevel level, BlockPos position) {
    BlockPos before = level.getSharedSpawnPos();
    level.getServer().getWorldData().overworldData().setSpawn(position, 0);
    return before;
  }

  @GameTest(template = ARENA)
  public static void theDamagedRobotCannotBePlaced(GameTestHelper helper) {
    Player player = helper.makeMockPlayer(GameType.SURVIVAL);
    ItemStack stack = damagedStack();

    GameTestRobots.use(helper, player, stack, FLOOR);

    helper.assertTrue(GameTestRobots.robots(helper).isEmpty(), PLACED);
    helper.assertValueEqual(stack.getCount(), ROBOT_STACK, PLACED);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theItemsFollowTheStackRules(GameTestHelper helper) {
    ItemStack damaged = damagedStack();

    helper.assertValueEqual(damaged.getMaxStackSize(), ROBOT_STACK, WRONG_STACK);
    helper.assertFalse(damaged.getItem().canFitInsideContainerItems(), FITS);
    helper.assertValueEqual(
        new ItemStack(CubitItems.TOOLBOX.get()).getMaxStackSize(), TOOLBOX_STACK, WRONG_STACK);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theToolboxRepairsTheRobotInAnyPosition(GameTestHelper helper) {
    ItemStack damaged = damagedStack();
    damaged.set(
        CubitDataComponents.ROBOT_STATE.get(),
        new RobotState(RobotTier.BASE, SAVED_ENERGY, SAVED_HEALTH));
    ItemStack toolbox = new ItemStack(CubitItems.TOOLBOX.get());
    RobotState repaired = new RobotState(RobotTier.BASE, Energy.ZERO, REPAIRED_HEALTH);

    for (List<ItemStack> grid : List.of(List.of(damaged, toolbox), List.of(toolbox, damaged))) {
      ItemStack result = craft(helper, CraftingInput.of(2, 1, grid));
      helper.assertTrue(result.is(CubitItems.GENERAL_ROBOT.get()), WRONG_RESULT);
      helper.assertValueEqual(
          result.get(CubitDataComponents.ROBOT_STATE.get()), repaired, WRONG_RESULT);
    }
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theRepairNeedsBothItems(GameTestHelper helper) {
    ItemStack damaged = damagedStack();
    ItemStack toolbox = new ItemStack(CubitItems.TOOLBOX.get());
    ItemStack iron = new ItemStack(Items.IRON_INGOT);

    for (List<ItemStack> grid : List.of(List.of(damaged, iron), List.of(toolbox, iron))) {
      helper.assertTrue(recipe(helper, CraftingInput.of(2, 1, grid)).isEmpty(), BAD_RECIPE);
    }
    helper.assertTrue(
        recipe(helper, CraftingInput.of(THREE_BY_THREE, 1, List.of(damaged, toolbox, iron)))
            .isEmpty(),
        BAD_RECIPE);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theToolboxRecipeUsesVanillaMaterials(GameTestHelper helper) {
    ItemStack iron = new ItemStack(Items.IRON_INGOT);
    ItemStack redstone = new ItemStack(Items.REDSTONE);
    ItemStack obsidian = new ItemStack(Items.OBSIDIAN);
    List<ItemStack> grid = List.of(iron, iron, iron, iron, redstone, iron, iron, obsidian, iron);

    ItemStack result = craft(helper, CraftingInput.of(THREE_BY_THREE, THREE_BY_THREE, grid));

    helper.assertTrue(result.is(CubitItems.TOOLBOX.get()), WRONG_RESULT);
    helper.succeed();
  }

  private static ItemStack craft(GameTestHelper helper, CraftingInput input) {
    ServerLevel level = helper.getLevel();
    return recipe(helper, input)
        .map(holder -> holder.value().assemble(input, level.registryAccess()))
        .orElseThrow(() -> new IllegalStateException(NO_RECIPE));
  }

  private static Optional<RecipeHolder<CraftingRecipe>> recipe(
      GameTestHelper helper, CraftingInput input) {
    ServerLevel level = helper.getLevel();
    return level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level);
  }

  private static ItemStack damagedStack() {
    return new ItemStack(CubitItems.DAMAGED_GENERAL_ROBOT.get());
  }

  private static ItemEntity dropAt(GameTestHelper helper, ItemStack stack) {
    BlockPos position = helper.absolutePos(ABOVE_FLOOR);
    ItemEntity entity =
        new ItemEntity(
            helper.getLevel(),
            position.getX() + BLOCK_CENTER,
            position.getY(),
            position.getZ() + BLOCK_CENTER,
            stack);
    helper.getLevel().addFreshEntity(entity);
    return entity;
  }

  private static void dropIntoTheVoid(ServerLevel level, ItemEntity entity) {
    entity.teleportTo(entity.getX(), level.getMinBuildHeight() - 1, entity.getZ());
  }
}
