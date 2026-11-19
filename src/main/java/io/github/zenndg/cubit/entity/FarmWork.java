package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.domain.tier.FarmerSpec;
import io.github.zenndg.cubit.menu.RobotStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;

/**
 * What the Farmer does at a block, with the drops of the game through a virtual hoe with the
 * Fortune of its tier (answers 1219 to 1221, 1232 and 1236). The drops go straight to its
 * inventory, and what does not fit falls by it, so nothing is lost.
 */
final class FarmWork {
  private static final int UPDATE_ALL = Block.UPDATE_ALL;

  /** The particles the bone meal of the game shows, as BoneMealItem.useOn does. */
  private static final int GROWTH_PARTICLES = 15;

  private static final float VOLUME = 1;
  private static final float PITCH = 1;

  private FarmWork() {}

  /**
   * Harvests the plant and plants the same again with its own seed, or one it carries, with the
   * fertilizer of its slot on a tier that takes it, as any plant it just planted (1220, 1243).
   */
  static void harvest(RobotEntity robot, BlockPos pos, FarmerSpec spec) {
    ServerLevel level = (ServerLevel) robot.level();
    BlockState state = level.getBlockState(pos);
    Item seed = state.getBlock().getCloneItemStack(level, pos, state).getItem();
    List<ItemStack> drops = new ArrayList<>(drops(robot, pos, state, spec));
    level.destroyBlock(pos, false, robot);
    boolean replanted = takeSeed(drops, seed) || FarmSeeds.takeOne(robot.inventory(), seed);
    drops.forEach(stack -> store(robot, stack));
    if (replanted && place(robot, pos, seed) && spec.boneMeal()) {
      fertilize(robot, pos);
    }
  }

  /** Cuts a cane or a cactus from the top down to the block over its base (answer 1219). */
  static void cutTall(RobotEntity robot, BlockPos lowest, FarmerSpec spec) {
    ServerLevel level = (ServerLevel) robot.level();
    Block block = level.getBlockState(lowest).getBlock();
    BlockPos top = lowest;
    while (level.getBlockState(top.above()).is(block)) {
      top = top.above();
    }
    for (BlockPos pos = top; pos.getY() >= lowest.getY(); pos = pos.below()) {
      drops(robot, pos, level.getBlockState(pos), spec).forEach(stack -> store(robot, stack));
      level.destroyBlock(pos, false, robot);
    }
  }

  /** Picks a fruit off its stem, which stays (answer 1219). */
  static void pick(RobotEntity robot, BlockPos pos, FarmerSpec spec) {
    drops(robot, pos, robot.level().getBlockState(pos), spec).forEach(stack -> store(robot, stack));
    robot.level().destroyBlock(pos, false, robot);
  }

  /** Tills as the hoe of the game does, with its sound (answer 1232). */
  static void till(RobotEntity robot, BlockPos pos) {
    BlockState state = robot.level().getBlockState(pos);
    BlockState tilled = FarmPlants.tilledState(robot.level(), pos, state, false);
    if (tilled != null) {
      robot.level().setBlock(pos, tilled, UPDATE_ALL);
      robot.level().playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, VOLUME, PITCH);
    }
  }

  /** Plants the chosen seed, and the fertilizer of its slot on it, on a tier that takes it. */
  static void plant(RobotEntity robot, BlockPos farmland, boolean fertilize) {
    BlockPos above = farmland.above();
    Optional<Item> seed = FarmSeeds.choose(robot.inventory(), robot.level(), above);
    if (seed.isPresent() && FarmSeeds.takeOne(robot.inventory(), seed.get())) {
      if (place(robot, above, seed.get()) && fertilize) {
        fertilize(robot, above);
      }
    }
  }

  static void store(RobotEntity robot, ItemStack stack) {
    RobotStorage.insert(robot.inventory(), stack);
    if (!stack.isEmpty()) {
      Containers.dropItemStack(robot.level(), robot.getX(), robot.getY(), robot.getZ(), stack);
    }
  }

  /** Places the plant of the seed, or gives the seed back when it cannot grow there. */
  private static boolean place(RobotEntity robot, BlockPos pos, Item seed) {
    if (seed instanceof BlockItem item) {
      BlockState plant = item.getBlock().defaultBlockState();
      if (plant.canSurvive(robot.level(), pos)) {
        robot.level().setBlock(pos, plant, UPDATE_ALL);
        return true;
      }
    }
    store(robot, new ItemStack(seed));
    return false;
  }

  /**
   * Any fertilizer acts as the bone meal of the game, one use a plant (answer 1243), with the green
   * particles and the sound of the game, which only the use by a player shows by itself.
   */
  private static void fertilize(RobotEntity robot, BlockPos pos) {
    ItemStack fertilizer = robot.robotInventory().fertilizerSlot().stack();
    if (!fertilizer.isEmpty() && BoneMealItem.applyBonemeal(fertilizer, robot.level(), pos, null)) {
      robot.level().levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, pos, GROWTH_PARTICLES);
    }
  }

  private static boolean takeSeed(List<ItemStack> drops, Item seed) {
    for (ItemStack stack : drops) {
      if (stack.is(seed)) {
        stack.shrink(1);
        return true;
      }
    }
    return false;
  }

  private static List<ItemStack> drops(
      RobotEntity robot, BlockPos pos, BlockState state, FarmerSpec spec) {
    ServerLevel level = (ServerLevel) robot.level();
    return Block.getDrops(
        state, level, pos, level.getBlockEntity(pos), robot, hoe(level, spec.fortune()));
  }

  private static ItemStack hoe(ServerLevel level, int fortune) {
    ItemStack hoe = new ItemStack(Items.IRON_HOE);
    if (fortune > 0) {
      hoe.enchant(
          level
              .registryAccess()
              .registryOrThrow(Registries.ENCHANTMENT)
              .getHolderOrThrow(Enchantments.FORTUNE),
          fortune);
    }
    return hoe;
  }
}
