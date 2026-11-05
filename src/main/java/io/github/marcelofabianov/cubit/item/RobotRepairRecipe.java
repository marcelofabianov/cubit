package io.github.marcelofabianov.cubit.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitRecipes;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Repairs a damaged robot, in any position of the grid. It is a recipe type of its own because in
 * 1.21.1 a shapeless recipe always gives a fixed result, without the data of the ingredients.
 */
public final class RobotRepairRecipe extends CustomRecipe {
  private static final String DAMAGED_ROBOT_FIELD = "damaged_robot";
  private static final String TOOLBOX_FIELD = "toolbox";
  private static final String RESULT_FIELD = "result";
  private static final int INGREDIENTS = 2;
  private static final RobotState UNSAVED_STATE = new RobotState(RobotTier.BASE, Energy.ZERO, 0f);

  public static final MapCodec<RobotRepairRecipe> CODEC =
      RecordCodecBuilder.mapCodec(instance ->
          instance
              .group(
                  Ingredient.CODEC_NONEMPTY
                      .fieldOf(DAMAGED_ROBOT_FIELD)
                      .forGetter(recipe -> recipe.damagedRobot),
                  Ingredient.CODEC_NONEMPTY
                      .fieldOf(TOOLBOX_FIELD)
                      .forGetter(recipe -> recipe.toolbox),
                  BuiltInRegistries.ITEM
                      .byNameCodec()
                      .fieldOf(RESULT_FIELD)
                      .forGetter(recipe -> recipe.result))
              .apply(instance, RobotRepairRecipe::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, RobotRepairRecipe> STREAM_CODEC =
      StreamCodec.composite(
          Ingredient.CONTENTS_STREAM_CODEC,
          recipe -> recipe.damagedRobot,
          Ingredient.CONTENTS_STREAM_CODEC,
          recipe -> recipe.toolbox,
          ByteBufCodecs.registry(Registries.ITEM),
          recipe -> recipe.result,
          RobotRepairRecipe::new);

  private final Ingredient damagedRobot;
  private final Ingredient toolbox;
  private final Item result;

  public RobotRepairRecipe(Ingredient damagedRobot, Ingredient toolbox, Item result) {
    super(CraftingBookCategory.MISC);
    this.damagedRobot = damagedRobot;
    this.toolbox = toolbox;
    this.result = result;
  }

  @Override
  public boolean matches(CraftingInput input, Level level) {
    List<ItemStack> stacks = stacksIn(input);
    if (stacks.size() != INGREDIENTS) {
      return false;
    }
    ItemStack first = stacks.getFirst();
    ItemStack second = stacks.getLast();
    return (damagedRobot.test(first) && toolbox.test(second))
        || (damagedRobot.test(second) && toolbox.test(first));
  }

  @Override
  public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
    if (!(result instanceof RobotItem robotItem)) {
      return new ItemStack(result);
    }
    return stacksIn(input).stream()
        .filter(damagedRobot)
        .findFirst()
        .map(damaged -> repair(robotItem, damaged))
        .orElseGet(() -> new ItemStack(result));
  }

  @Override
  public ItemStack getResultItem(HolderLookup.Provider registries) {
    return new ItemStack(result);
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width * height >= INGREDIENTS;
  }

  @Override
  public RecipeSerializer<RobotRepairRecipe> getSerializer() {
    return CubitRecipes.ROBOT_REPAIR.get();
  }

  private static ItemStack repair(RobotItem robotItem, ItemStack damaged) {
    ItemStack repaired =
        robotItem.repaired(
            damaged.getOrDefault(CubitDataComponents.ROBOT_STATE.get(), UNSAVED_STATE));
    repaired.set(
        CubitDataComponents.ROBOT_INVENTORY.get(),
        damaged.get(CubitDataComponents.ROBOT_INVENTORY.get()));
    repaired.set(
        CubitDataComponents.ENERGY_SLOT.get(), damaged.get(CubitDataComponents.ENERGY_SLOT.get()));
    return repaired;
  }

  private static List<ItemStack> stacksIn(CraftingInput input) {
    return input.items().stream().filter(stack -> !stack.isEmpty()).toList();
  }
}
