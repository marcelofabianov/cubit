package io.github.marcelofabianov.cubit.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.WakeRecharge;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitRecipes;
import java.util.Optional;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
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
 * Wakes a stored robot with a Power Bank in any position of the grid (answers 180 to 183 and issue
 * #51). The robot gets only what is missing to wake, the bank loses exactly that and stays in the
 * grid, and the Damaged Cubit never matches, because it is another item.
 */
public final class RobotRechargeRecipe extends CustomRecipe {
  private static final String ROBOT_FIELD = "robot";
  private static final String POWER_BANK_FIELD = "power_bank";
  private static final String RESULT_FIELD = "result";
  private static final int INGREDIENTS = 2;

  public static final MapCodec<RobotRechargeRecipe> CODEC =
      RecordCodecBuilder.mapCodec(instance ->
          instance
              .group(
                  Ingredient.CODEC_NONEMPTY.fieldOf(ROBOT_FIELD).forGetter(recipe -> recipe.robot),
                  Ingredient.CODEC_NONEMPTY
                      .fieldOf(POWER_BANK_FIELD)
                      .forGetter(recipe -> recipe.powerBank),
                  BuiltInRegistries.ITEM
                      .byNameCodec()
                      .fieldOf(RESULT_FIELD)
                      .forGetter(recipe -> recipe.result))
              .apply(instance, RobotRechargeRecipe::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, RobotRechargeRecipe> STREAM_CODEC =
      StreamCodec.composite(
          Ingredient.CONTENTS_STREAM_CODEC,
          recipe -> recipe.robot,
          Ingredient.CONTENTS_STREAM_CODEC,
          recipe -> recipe.powerBank,
          ByteBufCodecs.registry(Registries.ITEM),
          recipe -> recipe.result,
          RobotRechargeRecipe::new);

  private final Ingredient robot;
  private final Ingredient powerBank;
  private final Item result;

  public RobotRechargeRecipe(Ingredient robot, Ingredient powerBank, Item result) {
    super(CraftingBookCategory.MISC);
    this.robot = robot;
    this.powerBank = powerBank;
    this.result = result;
  }

  @Override
  public boolean matches(CraftingInput input, Level level) {
    return recharge(input).isPresent();
  }

  @Override
  public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
    return recharge(input).map(Outcome::robot).orElse(ItemStack.EMPTY);
  }

  /** The bank stays in the grid with what is left (answer 181). */
  @Override
  public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
    NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
    recharge(input).ifPresent(outcome -> remaining.set(outcome.bankSlot(), outcome.bank()));
    return remaining;
  }

  /** Shown in the recipe book, which hides special recipes (answer 908). */
  @Override
  public boolean isSpecial() {
    return false;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return NonNullList.of(Ingredient.EMPTY, robot, powerBank);
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
  public RecipeSerializer<RobotRechargeRecipe> getSerializer() {
    return CubitRecipes.ROBOT_RECHARGE.get();
  }

  private Optional<Outcome> recharge(CraftingInput input) {
    int robotSlot = -1;
    int bankSlot = -1;
    int filled = 0;
    for (int slot = 0; slot < input.size(); slot++) {
      ItemStack stack = input.getItem(slot);
      if (stack.isEmpty()) {
        continue;
      }
      filled++;
      if (robot.test(stack) && stack.has(CubitDataComponents.ROBOT_STATE.get())) {
        robotSlot = slot;
      } else if (powerBank.test(stack) && PowerBankItem.is(stack)) {
        bankSlot = slot;
      }
    }
    if (filled != INGREDIENTS || robotSlot < 0 || bankSlot < 0) {
      return Optional.empty();
    }
    return wake(input.getItem(robotSlot), input.getItem(bankSlot), bankSlot);
  }

  private static Optional<Outcome> wake(ItemStack robotStack, ItemStack bankStack, int bankSlot) {
    RobotState state = robotStack.get(CubitDataComponents.ROBOT_STATE.get());
    TierSpec spec = CubitMod.CONFIG.tierCatalog().spec(CubitConfig.GENERAL_ROBOT, state.tier());
    Battery battery = state.batteryFor(spec);
    return WakeRecharge.of(
            battery, CubitMod.CONFIG.wakeCharge(), PowerBankItem.batteryOf(bankStack))
        .map(result -> {
          ItemStack woken = robotStack.copyWithCount(1);
          woken.set(
              CubitDataComponents.ROBOT_STATE.get(), state.withEnergy(result.robot().charge()));
          ItemStack bank = bankStack.copyWithCount(1);
          PowerBankItem.store(bank, result.bank());
          return new Outcome(woken, bank, bankSlot);
        });
  }

  private record Outcome(ItemStack robot, ItemStack bank, int bankSlot) {}
}
